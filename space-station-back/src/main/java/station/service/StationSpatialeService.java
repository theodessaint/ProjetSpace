package station.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import station.model.Module;
import station.model.Position;
import station.api.request.CreateOrUpdateStationSpatialeRequest;
import station.exception.CompteNotFoundException;
import station.exception.PositionNotFoundException;
import station.exception.StationSpatialeNotFoundException;
import station.model.Compte;
import station.model.Emplacement;
import station.model.Grille;
import station.model.Joueur;
import station.model.StationSpatiale;
import station.repo.CompteRepository;
import station.repo.ModuleRepository;
import station.repo.StationSpatialeRepository;

@Service
public class StationSpatialeService {
    
    private static final Logger log = LoggerFactory.getLogger(StationSpatialeService.class);
    private final StationSpatialeRepository repository;
    private final ModuleRepository moduleRepository;
    private final CompteRepository compteRepository;
    
    
    public StationSpatialeService(StationSpatialeRepository repository, ModuleRepository moduleRepository, CompteRepository compteRepository) {
        this.repository = repository;
        this.moduleRepository = moduleRepository;
        this.compteRepository = compteRepository;
    }
    
    public List<StationSpatiale> findAll() {
        return this.repository.findAll();
    }
    
    public StationSpatiale findById(Integer id) {
        return this.repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Station introuvable"));
    }
    
    public List<StationSpatiale> findByLoginJoueur(String login){
        
        Compte compte = this.compteRepository.findByLogin(login)
        .orElseThrow(() -> {
            log.warn("Compte introuvable pour le login {}", login);
            return new CompteNotFoundException();
        });
        
        return this.repository.findByJoueurId(compte.getId());
        
    }
    
    public StationSpatiale findByIdEtJoueur(Integer id, String login){
        
        StationSpatiale station = this.findById(id);
        
        if(!station.getJoueur().getLogin().equals(login)) {
            log.warn("Le joueur {} a tenté d'accéder à la station {}", login, id);
            throw new AccessDeniedException("Accès interdit");
        }
        
        return station;
    }
    
    @Transactional // Soit Station et Module sont tous les deux validés, soit rien n'est créé
    public StationSpatiale save(CreateOrUpdateStationSpatialeRequest request, String login) {
        
        StationSpatiale station = new StationSpatiale();
        
        Joueur joueur = this.compteRepository.findJoueurByLogin(login)
        .orElseThrow(() -> {
            log.warn("Joueur introuvable pour le login {}", login);
            return new CompteNotFoundException();
        });

        station.setJoueur(joueur);
        StationSpatiale stationCree = this.save(station, request);
        this.creerModules(stationCree);
        
        return stationCree;
    }
    
    private StationSpatiale save(StationSpatiale station,
        CreateOrUpdateStationSpatialeRequest request) {
            station.setNom(request.getNom());
            station.setCycle(request.getCycle());
            
            return this.repository.save(station);
        }
        
    public StationSpatiale save(Integer id, CreateOrUpdateStationSpatialeRequest request) {
        
        StationSpatiale station = this.findById(id);
        station = this.save(station, request);
        log.debug("Station {} modifié !", id);
        
        return station;
    }
        
        public void deleteById(Integer id) {
            this.repository.deleteById(id);
        }
        
        private void creerModules(StationSpatiale station) { // créer un module par position pour UNE station précise. Changement a private car ce ne doit pas être accessible en dehors de la station
            List<Module> modules = new ArrayList<>();
            
            for(Emplacement emplacement : Grille.PLAN) {
                Position position = emplacement.getPosition();
                
                if(!Grille.contient(position)){
                    throw new PositionNotFoundException(); // Erreur si la position n'est pas contenue dans la grille
                }
                
                Module module = new Module();
                
                module.setNom(emplacement.getType().name()); // Attribue le type selon le nom ("Serre") dans l'enum
                
                module.setPosition(new Position(position.getX(), position.getY())); // On récupère la position et on l'attribue au module
                module.setDebloque(emplacement.isOuvertAuDepart()); // 
                
                station.ajouterModule(module);
                
                modules.add(module);
            }
            
            this.moduleRepository.saveAll(modules);
        }
    }