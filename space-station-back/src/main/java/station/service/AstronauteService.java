package station.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import station.api.request.CreateOrUpdateAstronauteRequest;
import station.config.PartieConfig;
import station.config.SqlLoader;
import station.enumerator.ModuleEnum;
import station.enumerator.RoleEnum;
import station.exception.AstronauteNotFoundException;
import station.exception.AstronauteReferenceNotFoundException;
import station.exception.ModuleNotFoundException;
import station.exception.StationSpatialeNotFoundException;
import station.model.Astronaute;
import station.model.AstronauteReference;
import station.model.Module;
import station.model.StationSpatiale;
import station.repo.AstronauteReferenceRepository;
import station.repo.AstronauteRepository;
import station.repo.ModuleRepository;
import station.repo.StationSpatialeRepository;

@Service
public class AstronauteService {
    private static final Logger log = LoggerFactory.getLogger(AstronauteService.class);
    private final AstronauteRepository repository;
    private final AstronauteReferenceRepository referenceRepository;

    private final ModuleRepository moduleRepository;
    private final StationSpatialeRepository stationRepository;

    private final SqlLoader sqlLoader;
    private final PartieConfig config;

    private final Random random = new Random();

    public AstronauteService(
            AstronauteRepository repository,
            AstronauteReferenceRepository referenceRepository,
            ModuleRepository moduleRepository,
            StationSpatialeRepository stationRepository,
            SqlLoader sqlLoader,
            PartieConfig config) {
        this.repository = repository;
        this.referenceRepository = referenceRepository;
        this.moduleRepository = moduleRepository;
        this.stationRepository = stationRepository;
        this.sqlLoader = sqlLoader;
        this.config = config;
    }

    public List<Astronaute> findAll() {
        log.debug("Liste des astronautes ...");

        return this.repository.findAll();
    }

    public List<Astronaute> findByRole(RoleEnum role) {
        log.debug("Liste des astronautes de rôle {} ...", role);

        return this.repository.findByReferenceRole(role);
    }

    public Astronaute findById(Integer id) {
        log.debug("Recherche de l'astronaute {} ...", id);

        return this.repository.findById(id).orElseThrow(
                () -> {
                    log.warn("Astronaute introuvable : id={}", id);
                    return new AstronauteNotFoundException();
                });
    }

    public Astronaute save(CreateOrUpdateAstronauteRequest request) {
        Astronaute astronaute = this.save(new Astronaute(), request);
        log.debug("Astronaute {} ajouté !", astronaute.getId());

        return astronaute;
    }

    public Astronaute save(Integer id, CreateOrUpdateAstronauteRequest request) {
        Astronaute astronaute = this.findById(id);
        astronaute = this.save(astronaute, request);
        log.debug("Astronaute {} modifié !", id);

        return astronaute;
    }

    public void deleteById(Integer id) {
        this.repository.deleteById(id);
        log.debug("Astronaute {} supprimé !", id);
    }

    private Astronaute save(Astronaute astronaute, CreateOrUpdateAstronauteRequest request) {
        astronaute.setReference(
                this.referenceRepository.findById(request.getReferenceId())
                        .orElseThrow(
                                () -> {
                                    log.warn("AstronauteReference introuvable : id={}", request.getReferenceId());
                                    return new AstronauteReferenceNotFoundException();
                                }));

        // Le module est optionnel : un astronaute peut ne pas encore être affecté
        if (request.getModuleId() != null) {
            Module module = moduleRepository.findById(request.getModuleId())
                    .orElseThrow(
                            () -> {
                                log.warn("Module introuvable : id={}", request.getModuleId());
                                return new ModuleNotFoundException();
                            });
            astronaute.setModule(module);
        }

        StationSpatiale station = stationRepository.findById(request.getStationId())
                .orElseThrow(
                        () -> {
                            log.warn("Station spatiale introuvable : id={}", request.getStationId());
                            return new StationSpatialeNotFoundException();
                        });
        astronaute.setStation(station);

        return this.repository.save(astronaute);
    }

    @Transactional
    public void initialiserCatalogueAstronautes() {
        sqlLoader.loadSqlFile("config/astronaute_choix.sql");
        log.debug("Initialisation du catalogue d'astronautes terminée !");
    }

    // Fait arriver un astronaute tiré au hasard et l'installe dans le Dortoir
    @Transactional
    public void faireArriverAstronaute(StationSpatiale station) {

        Module dortoir = station.getModules().stream()
                .filter(m -> ModuleEnum.Dortoir.name().equals(m.getNom()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Pas de dortoir dans la station " + station.getId()));

        AstronauteReference choisi = this.choisirAstronauteAleatoire(station);

        if (choisi == null) {
            log.warn("Station {} : plus aucun astronaute disponible dans le catalogue", station.getId());
            return;
        }

        Astronaute astronaute = new Astronaute();
        astronaute.setReference(choisi);
        astronaute.setStation(station);
        astronaute.setModule(dortoir); // lieu d'arrivée : le Dortoir

        astronaute = this.repository.save(astronaute);
        station.getAstronautes().add(astronaute);

        log.debug("Station {} : arrivée de {} (rôle : {})", station.getId(), choisi.getNom(), choisi.getRole());
    }

    // Choisit au hasard un astronaute du catalogue qui peut encore arriver (null
    // s'il n'y en a plus)
    private AstronauteReference choisirAstronauteAleatoire(StationSpatiale station) {

        List<Astronaute> presents = this.repository.findByStationId(station.getId());

        // Références déjà arrivées dans cette partie (jamais deux fois le même
        // astronaute)
        Set<Integer> referencesPresentes = presents.stream()
                .map(a -> a.getReference().getId())
                .collect(Collectors.toSet());

        // Combien d'astronautes de chaque rôle sont déjà là
        Map<RoleEnum, Long> nbParRole = presents.stream()
                .map(a -> a.getReference().getRole())
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(role -> role, Collectors.counting()));

        // Candidats : pas déjà dans la partie, et rôle pas déjà au maximum
        List<AstronauteReference> candidats = this.referenceRepository.findAll().stream()
                .filter(r -> !referencesPresentes.contains(r.getId()))
                .filter(r -> this.roleDisponible(r.getRole(), nbParRole))
                .toList();

        if (candidats.isEmpty()) {
            return null;
        }

        return candidats.get(this.random.nextInt(candidats.size()));
    }

    // Vrai si le rôle n'a pas encore atteint son maximum (défini dans le JSON)
    private boolean roleDisponible(RoleEnum role, Map<RoleEnum, Long> nbParRole) {

        if (role == null) {
            return true;
        }

        Map<RoleEnum, Integer> maxParRole = this.config.nbAstronautesMaxParRole();

        if (!maxParRole.containsKey(role)) {
            return true; // pas de limite pour ce rôle (Chercheur, Medecin)
        }

        return nbParRole.getOrDefault(role, 0L) < maxParRole.get(role);
    }

    @Transactional
    public Astronaute affecter(Integer astronauteId, Integer moduleId) {

        Astronaute astronaute = this.findById(astronauteId);

        Module module = this.moduleRepository.findById(moduleId).orElseThrow(
                () -> {
                    log.warn("Module introuvable : id={}", moduleId);
                    return new ModuleNotFoundException();
                });
        if (!module.getStation().getId().equals(astronaute.getStation().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ce module n'appartient pas à la station de cet astronaute");
        }

        if (!module.isDebloque()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce module n'est pas encore débloqué");
        }

        boolean estDortoir = ModuleEnum.Dortoir.name().equals(module.getNom());

        if (!estDortoir) { // le Dortoir n'a pas de limite
            if (module.getNbAstroModuleMax() == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Aucun astronaute ne peut être affecté à ce module");
            }

            boolean dejaDansCeModule = astronaute.getModule() != null
                    && astronaute.getModule().getId().equals(module.getId());

            if (!dejaDansCeModule
                    && this.repository.countByModuleId(module.getId()) >= module.getNbAstroModuleMax()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Ce module est complet (" + module.getNbAstroModuleMax() + " astronaute(s) maximum)");
            }
        }

        astronaute.setModule(module);
        return this.repository.save(astronaute);
    }
}