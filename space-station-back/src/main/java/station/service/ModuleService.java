package station.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import station.api.request.CreateOrUpdateModuleRequest;
import station.api.request.CreateOrUpdateModuleRequest;
import station.model.Module;
import station.model.Module;
import station.model.StationSpatiale;
import station.repo.ModuleRepository;

@Service
public class ModuleService {
    
    private static final int cyclesAvantDeblocageModule = 5; // Combien de cycles avant de débloquer un nouveau module (a voir comment la rendre dynamique selon la difficulté ?)
    private static final Logger log = LoggerFactory.getLogger(ModuleService.class);
    private final ModuleRepository repository;

    public ModuleService(ModuleRepository repository) {
        this.repository = repository;
    }

    public List<Module> findAll() {
        log.debug("Liste des modules ...");

        return this.repository.findAll();
    }

    public Module findById(Integer id) {
        log.debug("Recherche du module {} ...", id);

        return this.repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Module introuvable"));
    }

    public List<Module> findByStationId(Integer stationId){ // Affiche la liste des modules d'une partie
    log.debug("Liste des modules de la station {} ", stationId);
        return this.repository.findByStationId(stationId);
    }

    public Module save(CreateOrUpdateModuleRequest request) {
        log.debug("Création d'un Module ...");

        Module Module = this.save(new Module(), request);

        log.debug("Module {} ajouté !", Module.getId());

        return Module;
    }

    public Module save(Integer id, CreateOrUpdateModuleRequest request) {
        log.debug("Modification du module {} ...", id);

        Module Module = this.findById(id);

        Module = this.save(Module, request);

        log.debug("Module {} modifié !", id);

        return Module;
    }

   public void deleteById(Integer id) {
        log.debug("Suppression du module {} ...", id);

        this.repository.deleteById(id);

        log.debug("Module {} supprimé !", id);
    }

     private Module save(Module Module, CreateOrUpdateModuleRequest request) {
        Module.setNom(request.getNom());
        Module.setNbAstroModuleMax(request.getNbAstroModuleMax());
        Module.setNbModuleMax(request.getNbModuleMax());
        Module.setDebloque(request.isDebloque());
        Module.setPosition(request.getPosition());
        Module.setStation(request.getStation());
        Module.setProdModules(request.getProdModules());
        Module.setConsoModules(request.getConsoModules());

        return this.repository.save(Module);
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