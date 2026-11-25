package station.service;
 
import java.util.List;
import java.util.Map;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
 
import station.api.request.CreateOrUpdateModuleRequest;
import station.enumerator.RessourceEnum;
import station.exception.StationSpatialeNotFoundException;
import station.model.Grille;
import station.model.Module;
import station.model.Position;
import station.model.Ressource;
import station.model.StationSpatiale;
import station.repo.ModuleRepository;
import station.repo.StationSpatialeRepository;
 
@Service
public class ModuleService {
 
    private static final int cyclesAvantDeblocageModule = 5; // Combien de cycles avant de débloquer un nouveau module (a voir comment la rendre dynamique selon la difficulté ?)
    private static final int COUT_ENERGIE_DEBLOCAGE = 20;
    private static final Logger log = LoggerFactory.getLogger(ModuleService.class);
    private final ModuleRepository repository;
    private final StationSpatialeRepository stationRepository;
 
    public ModuleService(ModuleRepository repository, StationSpatialeRepository stationRepository) {
        this.repository = repository;
        this.stationRepository = stationRepository;
    }
 
    public List<Module> findAll() {
        return this.repository.findAll();
    }
 
    public Module findById(Integer id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Module introuvable"));
    }
 
    public List<Module> findByStationId(Integer stationId) { // Affiche la liste des modules d'une partie
        return this.repository.findByStationId(stationId);
    }
 
    public Module save(CreateOrUpdateModuleRequest request) {
        Module module = this.save(new Module(), request);
        log.debug("Module {} ajouté !", module.getId());
 
        return module;
    }
 
    public Module save(Integer id, CreateOrUpdateModuleRequest request) {
        Module module = this.findById(id);
        module = this.save(module, request);
        log.debug("Module {} modifié !", id);
 
        return module;
    }
 
    public void deleteById(Integer id) {
        this.repository.deleteById(id);
        log.debug("Module {} supprimé !", id);
    }
 
    private Module save(Module module, CreateOrUpdateModuleRequest request) {
        module.setNom(request.getNom());
        module.setNbAstroModuleMax(request.getNbAstroModuleMax());
        module.setNbModuleMax(request.getNbModuleMax());
        module.setDebloque(request.isDebloque());
        module.setPosition(new Position(request.getPositionX(), request.getPositionY()));
 
        StationSpatiale station = stationRepository.findById(request.getStationId())
                .orElseThrow(
                        () -> {
                            log.warn("Station spatiale introuvable : id={}", request.getStationId());
                            return new StationSpatialeNotFoundException();
                        });
        module.setStation(station);
 
        return this.repository.save(module);
    }
 
    // ---------- DEBLOCAGE DES MODULES ----------
 
    @Transactional // Soit tout est validé (énergie retirée + module débloqué), soit rien
    public Module debloqueModule(Integer id) { // Permet de debloquer un module
 
        Module module = this.findById(id);
 
        if (module.isDebloque()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce module est déjà débloqué");
        }
 
        StationSpatiale station = module.getStation();
 
        // Les 3 premiers modules (tuto) sont gratuits et sans condition de cycle
        boolean estTuto = Grille.estPremierModule(module.getPosition());
 
        if (!estTuto && !this.peutDebloquer(station)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Aucun déblocage disponible (1 tous les 5 cycles)");
        }
 
        List<Module> modulesDeLaStation = this.repository.findByStationId(station.getId()); // Tous les modules de la partie
 
        boolean aUnVoisinDebloque = false;
 
        for (Module autre : modulesDeLaStation) {
            if (autre.isDebloque() && autre.getPosition().estVoisineDe(module.getPosition())) { // Module débloqué ET voisin de celui qu'on veut débloquer
                aUnVoisinDebloque = true;
                break; // S'arrête dès qu'on trouve un voisin débloqué
            }
        }
 
        if (!aUnVoisinDebloque) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Pas possible de débloquer un module car aucun module voisin n'est débloqué");
        }
 
        // Toutes les vérifications sont passées : on fait payer (sauf les modules du tuto)
       if (!estTuto) {
    // TODO ressources : à réactiver quand stats sera initialisé
    // this.retirerEnergie(station, COUT_ENERGIE_DEBLOCAGE);
}
 
        module.setDebloque(true);
        return this.repository.save(module);
    }
 
    // Retire du stock d'énergie de la station (erreur si pas assez)
    private void retirerEnergie(StationSpatiale station, double cout) {
        Map.Entry<Ressource, Double> energie = station.getStats().entrySet().stream()
                .filter(e -> e.getKey().getType() == RessourceEnum.ENERGIE)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Ressource énergie introuvable pour cette station"));
 
        Ressource cle = energie.getKey();
        double restante = energie.getValue();
 
        if (restante < cout) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Pas assez d'énergie pour débloquer un module (" + (int) cout + " requis)");
        }
 
        station.getStats().put(cle, restante - cout);
        this.stationRepository.save(station);
    }
 
    // Déblocages gagnés (1 tous les 5 cycles) - déblocages déjà utilisés (hors départ et hors tuto)
    public int nbDeblocagesDisponibles(StationSpatiale station) {
        long utilises = this.repository.findByStationId(station.getId()).stream()
                .filter(m -> m.isDebloque()) // garde ceux qui sont débloqués
                .filter(m -> !Grille.estPremierModule(m.getPosition())) // retire les 3 modules du tuto
                .filter(m -> !Grille.estOuvertAuDepart(m.getPosition())) // retire Dortoir et Centre de commandement
                .count(); // modules débloqués grâce à un déblocage
        return (int) (station.getCycle() / cyclesAvantDeblocageModule - utilises);
    }
 
    // Vrai s'il reste au moins un déblocage disponible (utilisé aussi par StationSpatialeResponse)
    public boolean peutDebloquer(StationSpatiale station) {
        return this.nbDeblocagesDisponibles(station) > 0;
    }
 
    // Vrai si le joueur doitdébloquer un module avant de passer au cycle suivant
    public boolean deblocageObligatoireEnAttente(StationSpatiale station) {
        boolean resteUnModuleVerrouille = this.repository.findByStationId(station.getId()).stream()
                .anyMatch(m -> !m.isDebloque());
        return resteUnModuleVerrouille && this.nbDeblocagesDisponibles(station) > 0;
    }
 
}
 