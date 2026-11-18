package station.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import station.api.request.CreateOrUpdateCompteRequest;
import station.exception.CompteNotFoundException;
import station.model.Compte;
import station.repo.CompteRepository;

@Service
public class CompteService {
    private static final Logger log = LoggerFactory.getLogger(CompteService.class);
    private final CompteRepository repository;
    
    public CompteService(CompteRepository repository) {
        this.repository = repository;
    }
    
    public List<Compte> findAll() {
        log.debug("Liste des comptes ...");
        
        return this.repository.findAll();
    }
    
    public Compte findById(Integer id) {
        log.debug("Recherche du compte {} ...", id);
        
        return this.repository.findById(id).orElseThrow(CompteNotFoundException::new);
    }
    
    public Compte findByUsername(String username) {
        log.debug("Recherche du compte avec pour username {} ...", username);
        
        return this.repository.findByLogin(username).orElseThrow(CompteNotFoundException::new);
    }
    
    public Compte save(CreateOrUpdateCompteRequest request) {
        Compte compte = this.save(new Compte(), request);
        log.debug("Compte {} ajouté !", compte.getId());
        
        return compte;
    }
    
    public Compte save(Integer id, CreateOrUpdateCompteRequest request) {
        Compte compte = this.findById(id);
        compte = this.save(compte, request);
        log.debug("Compte {} modifié !", id);
        
        return compte;
    }
    
    public void deleteById(Integer id) {        
        this.repository.deleteById(id);
        
        log.debug("Compte {} supprimé !", id);
    }
    
    private Compte save(Compte compte, CreateOrUpdateCompteRequest request) {
        compte.setId(request.getId());
        compte.setLogin(request.getLogin());
        compte.setPassword(request.getPassword());
        
        return this.repository.save(compte);
    }
    
}