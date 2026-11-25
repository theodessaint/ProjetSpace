package station.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import station.api.request.CreateOrUpdateAstronauteRequest;
import station.config.SqlLoader;
import station.enumerator.RoleEnum;
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
    
    public AstronauteService(
        AstronauteRepository repository,
        AstronauteReferenceRepository referenceRepository,
        ModuleRepository moduleRepository,
        StationSpatialeRepository stationRepository,
        SqlLoader sqlLoader
    ) {
        this.repository = repository;
        this.referenceRepository = referenceRepository;
        this.moduleRepository = moduleRepository;
        this.stationRepository = stationRepository;
        this.sqlLoader = sqlLoader;
    }
    
    public List<Astronaute> findAll() {
        log.debug("Liste des astronautes ...");
        
        return this.repository.findAll();
    }
    
    public List<Astronaute> findByRole(RoleEnum role) {
        log.debug("Liste des astronautes de rôle {} ...", role);
        
        return this.repository.findByRole(role);
    }
    
    public Astronaute findById(Integer id) {
        log.debug("Recherche de l'astronaute {} ...", id);
        
        return this.repository.findById(id).orElseThrow(
            () -> {
                log.warn("Astronaute introuvable : id={}", id);
                return new AstronauteReferenceNotFoundException();
            }
        );
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
                }
            )
        );
        
        Module module = moduleRepository.findById(request.getModuleId())
        .orElseThrow(
            () -> {
                log.warn("Module introuvable : id={}", request.getModuleId());
                return new ModuleNotFoundException();
            }
        );
        astronaute.setModule(module);
        
        StationSpatiale station = stationRepository.findById(request.getStationId())
        .orElseThrow(
            () -> {
                log.warn("Station spatiale introuvable : id={}", request.getStationId());
                return new StationSpatialeNotFoundException();
            }
        );
        astronaute.setStation(station);
        
        return this.repository.save(astronaute);
    }
    
    @Transactional
    public void initialiserCatalogueAstronautes() {
        sqlLoader.loadSqlFile("config/astronaute_choix.sql");
    }
}