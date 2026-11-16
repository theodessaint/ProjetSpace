package station.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import station.api.request.CreateOrUpdateBonusRequest;
import station.exception.BonusNotFoundException;
import station.model.Bonus;
import station.model.Ressource;
import station.repo.BonusRepository;

@Service
public class BonusService {
    private static final Logger log = LoggerFactory.getLogger(BonusService.class);
    private final BonusRepository repository;
    
    public BonusService(BonusRepository repository) {
        this.repository = repository;
    }
    
    public List<Bonus> findAll() {
        log.debug("Liste des bonus ...");
        
        return this.repository.findAll();
    }
    
    public Bonus findById(Integer id) {
        log.debug("Recherche du bonus {} ...", id);
        
        return this.repository.findById(id).orElseThrow(
            () -> {
                log.warn("Bonus introuvable : id={}", id);
                return new BonusNotFoundException();
            }
        );
    }
    
    public Bonus save(CreateOrUpdateBonusRequest request) {
        Bonus bonus = this.save(new Bonus(), request);
        log.debug("Bonus {} ajouté !", bonus.getId());
        
        return bonus;
    }
    
    public Bonus save(Integer id, CreateOrUpdateBonusRequest request) {
        Bonus bonus = this.findById(id);
        bonus = this.save(bonus, request);
        log.debug("Bonus {} modifié !", id);
        
        return bonus;
    }
    
    public void deleteById(Integer id) {
        this.repository.deleteById(id);
        log.debug("Bonus {} supprimé !", id);
    }
    
    private Bonus save(Bonus bonus, CreateOrUpdateBonusRequest request) {
        bonus.setRole(request.getRole());
        
        bonus.setRessource(
            List.of(
                Ressource.EAU, Ressource.ENERGIE, Ressource.NOURRITURE, Ressource.OXYGENE)
                .stream()
                .filter(ressource -> ressource.getId() == request.getRessourceId())
                .findFirst()
                .orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ressource inconnue : " + request.getRessourceId()
                )
            )
            
        );
        bonus.setMultiplicateur(request.getMultiplicateur());
        
        return this.repository.save(bonus);
    }
    
}