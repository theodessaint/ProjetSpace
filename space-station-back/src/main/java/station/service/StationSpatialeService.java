package station.service;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import station.api.request.CreateOrUpdateStationSpatialeRequest;
import station.config.PartieConfig;
import station.config.modelConfig.ModuleConfig;
import station.config.modelConfig.RessourceConfig;
import station.enumerator.ModuleEnum;
import station.enumerator.RessourceEnum;
import station.exception.CompteNotFoundException;
import station.exception.PositionNotFoundException;
import station.exception.StationSpatialeNotFoundException;
import station.model.Compte;
import station.model.Emplacement;
import station.model.Grille;
import station.model.Joueur;
import station.model.Module;
import station.model.ModuleConso;
import station.model.ModuleProd;
import station.model.Position;
import station.model.Ressource;
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

    private final ModuleService moduleService;

    private final PartieConfig config;
    
    public StationSpatialeService(
        StationSpatialeRepository repository, 
        ModuleRepository moduleRepository, 
        CompteRepository compteRepository,
        ModuleService moduleService,
        PartieConfig config
    ) {
        
        this.repository = repository;
        this.moduleRepository = moduleRepository;
        this.compteRepository = compteRepository;
        this.moduleService = moduleService;
        this.config = config;
    }
 
    public List<StationSpatiale> findAll() {
        log.debug("Liste des stations spatiales ...");
        return this.repository.findAll();
    }
 
    public StationSpatiale findById(Integer id) {
        log.debug("Recherche de la station spatiale {} ...", id);
        
        return this.repository.findById(id).orElseThrow(
            () -> {
                log.warn("Station spatiale introuvable : id={}", id);
                return new StationSpatialeNotFoundException();
            }
        );
    }
 
    public List<StationSpatiale> findByLoginJoueur(String login) {
 
        Compte compte = this.compteRepository.findByLogin(login)
                .orElseThrow(() -> {
                    log.warn("Compte introuvable pour le login {}", login);
                    return new CompteNotFoundException();
                });
 
        return this.repository.findByJoueurId(compte.getId());
 
    }
 
    public StationSpatiale findByIdEtJoueur(Integer id, String login) {
 
        StationSpatiale station = this.findById(id);
 
        if (!station.getJoueur().getLogin().equals(login)) {
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
        log.debug("Station spatiale {} ajoutée !", station.getId());
        
        this.creerModules(stationCree);
        this.creerRessources(stationCree);
        
        return stationCree;
    }
    
    public StationSpatiale save(Integer id, CreateOrUpdateStationSpatialeRequest request) {
 
        StationSpatiale station = this.findById(id);
        station = this.save(station, request);
        log.debug("Station {} modifiée !", id);
        
        return station;
    }
    
    private StationSpatiale save(StationSpatiale station, CreateOrUpdateStationSpatialeRequest request) {
        station.setNom(request.getNom());
        station.setCycle(request.getCycle());
        
        return this.repository.save(station);
    }
    
    public void deleteById(Integer id) {
        this.repository.deleteById(id);
        log.debug("Station spatiale {} supprimée !", id);
    }
    
    private void creerModules(StationSpatiale station) { // créer un module par position pour UNE station précise. Changement a private car ce ne doit pas être accessible en dehors de la station
        List<Module> modules = new ArrayList<>();
        List<ModuleConfig> modulesConfig = config.modules();
        
        for(Emplacement emplacement : Grille.PLAN) {
            Position position = emplacement.getPosition();
            
            if(!Grille.contient(position)){
                throw new PositionNotFoundException(); // Erreur si la position n'est pas contenue dans la grille
            }
            
            ModuleConfig moduleConfig = modulesConfig.stream()
            .filter(mc -> mc.module() == emplacement.getType())
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(
                "Configuration introuvable pour le module : " + emplacement.getType()
            )); // récupère la configuration initiale pour le module "Serre" par exemple
            
            Module module = new Module();
            
            module.setNom(emplacement.getType().name()); // Attribue le type selon le nom ("Serre") dans l'enum
            
            module.setPosition(new Position(position.getX(), position.getY())); // On récupère la position et on l'attribue au module
            module.setDebloque(emplacement.isOuvertAuDepart());           
            module.setNbModuleMax(moduleConfig.nbModuleMax());
            module.setNbAstroModuleMax(moduleConfig.nbAstroModuleMax());
            
            station.ajouterModule(module);
            
            // Initialisation des productions et consommations
            initialiserProductions(module, emplacement.getType());
            initialiserConsommations(module, emplacement.getType());
            
            modules.add(module);
        }
        
        this.moduleRepository.saveAll(modules);
    }
    
    private void creerRessources(StationSpatiale station) {
        Map<Ressource, Double> ressources = new HashMap<Ressource, Double>();
        List<RessourceConfig> ressourcesConfig = config.ressources();
        
        for (RessourceEnum ressourceType : List.of(RessourceEnum.EAU, RessourceEnum.ENERGIE, RessourceEnum.NOURRITURE, RessourceEnum.OXYGENE)) {
            RessourceConfig ressourceConfig = ressourcesConfig.stream()
            .filter(rc -> rc.type() == ressourceType)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(
                "Configuration introuvable pour le module : " + ressourceType
            )); // récupère la configuration initiale pour chaque ressource
            
            Ressource ressource = new Ressource();
            
            ressource.setType(ressourceConfig.type());
            ressource.setStart(ressourceConfig.start());
            ressource.setDanger(ressourceConfig.danger());
            ressource.setCritique(ressourceConfig.critique());
            
            ressources.put(ressource, ressourceConfig.start());
        }
        
        station.setStats(ressources);
    }
    
    private void creerAstronautes(StationSpatiale station) {
        
    }
    
    private void initialiserProductions(Module module, ModuleEnum type) {
        
        config.moduleProd().stream()
        .filter(prod -> prod.module() == type)
        .forEach(prodConfig -> {
            
            ModuleProd prod = new ModuleProd();
            
            prod.setModule(module);
            prod.setNbAstroAffectes(prodConfig.nbAstroAffectes());
            prod.setValeur(prodConfig.valeur());
            
            prod.setRessource(prodConfig.ressource());
            
            module.getProdModules().add(prod);
        });
    }
    
    private void initialiserConsommations(Module module, ModuleEnum type) {
        
        config.moduleConso().stream()
        .filter(conso -> conso.module() == type)
        .forEach(consoConfig -> {
            
            ModuleConso conso = new ModuleConso();
            
            conso.setModule(module);
            conso.setNbAstroAffectes(consoConfig.nbAstroAffectes());
            conso.setValeur(consoConfig.valeur());
            
            conso.setRessource(consoConfig.ressource());
            
            module.getConsoModules().add(conso);
        });
    }


        // Bouton "Passer le tuto" : les 3 premiers modules sont débloqués gratuitement d'un coup
    @Transactional
    public StationSpatiale debloquerModulesTuto(Integer id, String login) {

        StationSpatiale station = this.findByIdEtJoueur(id, login);

        for (Module module : station.getModules()) {
            if (Grille.estPremierModule(module.getPosition())) {
                module.setDebloque(true);
            }
        }

        return this.repository.save(station);
    }
    
    // Passe au cycle suivant, mais seulement si le joueur a fait son déblocage obligatoire (tous les 5 cycles)
    @Transactional
    public StationSpatiale cycleSuivant(Integer id, String login) {
 
        StationSpatiale station = this.findByIdEtJoueur(id, login); // vérifie aussi que la station appartient au joueur
 
        if (this.moduleService.deblocageObligatoireEnAttente(station)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Vous devez débloquer un module avant de passer au cycle suivant");
        }
 
        station.executerCycle(); // le cycle avance à la fin de cette méthode
        log.debug("Station {} : passage au cycle {}", id, station.getCycle());
 
        return this.repository.save(station);
    }
    
}
