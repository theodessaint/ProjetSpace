package station.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import station.api.request.CreateOrUpdateRessourceRequest;
import station.enumerator.RessourceEnum;
import station.exception.RessourceNotFoundException;
import station.model.Ressource;
import station.repo.RessourceRepository;

@Service
public class RessourceService {
    private static final Logger log = LoggerFactory.getLogger(RessourceService.class);
    private final RessourceRepository repository;
    
    public RessourceService(RessourceRepository repository) {
        this.repository = repository;
    }
    
    public List<Ressource> findAll() {
        log.debug("Liste des ressources ...");
        
        return this.repository.findAll();
    }
    
    public Ressource findById(Integer id) {
        log.debug("Recherche de la ressource {} ...", id);
        
        return this.repository.findById(id).orElseThrow(
            () -> {
                log.warn("Ressource introuvable : id={}", id);
                return new RessourceNotFoundException();
            }
        );
    }
    
    public Ressource save(CreateOrUpdateRessourceRequest request) {
        Ressource ressource = this.save(new Ressource(), request);
        log.debug("Ressource {} ajoutée !", ressource.getId());
        
        return ressource;
    }
    
    public Ressource save(Integer id, CreateOrUpdateRessourceRequest request) {
        Ressource ressource = this.findById(id);
        ressource = this.save(ressource, request);
        log.debug("Ressource {} modifiée !", id);
        
        return ressource;
    }
    
    public void deleteById(Integer id) {
        this.repository.deleteById(id);
        log.debug("Ressource {} supprimée !", id);
    }
    
    private Ressource save(Ressource ressource, CreateOrUpdateRessourceRequest request) {
        
        ressource.setType(
            Arrays.stream(RessourceEnum.values())
            .filter(type -> type.getId() == request.getRessourceTypeId())
            .findFirst()
            .orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Type de ressource inconnu : " + request.getRessourceTypeId()
            )
        )
    );
    
    ressource.setStart(request.getStart());
    ressource.setDanger(request.getDanger());
    ressource.setCritique(request.getCritique());
    
    
    return this.repository.save(ressource);
}

}