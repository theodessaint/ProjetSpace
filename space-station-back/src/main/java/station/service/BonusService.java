package station.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import station.api.request.CreateOrUpdateBonusRequest;
import station.config.PartieConfig;
import station.config.modelConfig.BonusConfig;
import station.enumerator.RessourceEnum;
import station.exception.BonusNotFoundException;
import station.model.Bonus;
import station.repo.BonusRepository;

@Service
public class BonusService {
    private static final Logger log = LoggerFactory.getLogger(BonusService.class);
    private final BonusRepository repository;
    private final PartieConfig config; 
    
    public BonusService(BonusRepository repository, PartieConfig config) {
        this.repository = repository;
        this.config = config;
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
                RessourceEnum.EAU, RessourceEnum.ENERGIE, RessourceEnum.NOURRITURE, RessourceEnum.OXYGENE)
                .stream()
                .filter(ressource -> ressource == request.getRessource())
                .findFirst()
                .orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ressource inconnue : " + request.getRessource()
                )
            )
            
        );
        bonus.setMultiplicateur(request.getMultiplicateur());
        
        return this.repository.save(bonus);
    }
    
    @Transactional
    public void initialiserBonus() {
        
        List<Bonus> bonusList = new ArrayList<>();
        
        for (BonusConfig bc : config.bonus()) {
            
            if (this.repository.existsByRoleAndModuleAndRessource(
                bc.role(),
                bc.module(),
                bc.ressource())) {
                    continue;
                }
                
                Bonus bonus = new Bonus();
                
                bonus.setRole(bc.role());
                bonus.setModule(bc.module());
                bonus.setRessource(bc.ressource());
                bonus.setMultiplicateur(bc.multiplicateur());
                
                bonusList.add(bonus);
            }
            
            this.repository.saveAll(bonusList);
        }
        
    }