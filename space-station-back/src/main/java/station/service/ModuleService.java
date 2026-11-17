package station.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import station.api.request.CreateOrUpdateModuleRequest;
import station.exception.StationSpatialeNotFoundException;
import station.model.Module;
import station.model.Position;
import station.model.StationSpatiale;
import station.repo.ModuleRepository;
import station.repo.StationSpatialeRepository;

@Service
public class ModuleService {
    
    private static final int cyclesAvantDeblocageModule = 5; // Combien de cycles avant de débloquer un nouveau module (a voir comment la rendre dynamique selon la difficulté ?)
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
    public List<Module> findByStationId(Integer stationId){ // Affiche la liste des modules d'une partie
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
            }
        );
        module.setStation(station);
        
        return this.repository.save(module);
    }

    public Module debloqueModule(Integer id) { // Permet de debloquer un module
        
        Module module = this.findById(id);

        if(module.isDebloque()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,  "Ce module est déjà débloqué"); // Si le module est déjà débloqué on recoit une erreur
        }

        StationSpatiale station = module.getStation(); // Vérifie le cycle pour savoir si c'est déblocable
        if(station.getCycle() % cyclesAvantDeblocageModule !=0) { // Il faut que ce soit modulo de 5 dans notre cas
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pas possible de débloquer un module sur ce cycle");
        }

        List<Module> modulesDeLaStation = this.repository.findByStationId(station.getId()); // On récupère tous les modules contenus dans la partie

        boolean aUnVoisinDebloque = false;

        for(Module autre : modulesDeLaStation) {
            if(autre.isDebloque() && autre.getPosition().estVoisineDe(module.getPosition())) { // Vérifie si le module est débloqué et est voisin de celui qu'on veut débloquer
                aUnVoisinDebloque = true;
                break; // S'arrête dès que ca trouve un voisin débloqué
            }
        }

        if(!aUnVoisinDebloque) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pas possible de débloquer un module car aucun module voisin n'est débloqué");
        }

        module.setDebloque(true);
        return this.repository.save(module);
    }

}