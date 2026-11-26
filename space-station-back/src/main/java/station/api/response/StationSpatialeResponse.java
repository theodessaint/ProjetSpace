package station.api.response;

import java.util.List;

import station.model.Grille;
import station.model.StationSpatiale;
import station.service.ModuleService;

public class StationSpatialeResponse {
    
    private Integer id;
    private String nom;
    private int cycle;
    private int colonnes;
    private int lignes;
    private boolean peutDebloquer; // au moins un déblocage disponible
    private int nbDeblocagesDisponibles;
    private boolean deblocageObligatoire; // le joueur doit débloquer un module avant de passer au cycle suivant
    private List<ModuleResponse> modules;
    private List<AstronauteResponse> astronautes;
    private List<RessourceResponse> stats;
    
    private Integer idJoueur;
    private String loginJoueur;
    
    
    public static StationSpatialeResponse convert(StationSpatiale station, ModuleService moduleService) {
        
        StationSpatialeResponse response = new StationSpatialeResponse();
        
        response.id = station.getId();
        response.nom = station.getNom();
        response.cycle = station.getCycle();
        response.loginJoueur = station.getJoueur().getLogin();
        response.idJoueur = station.getJoueur().getId();
        
        response.colonnes = Grille.nb_Colonnes;
        response.lignes = Grille.nb_Lignes;
        
        response.peutDebloquer = moduleService.peutDebloquer(station);
        response.nbDeblocagesDisponibles = moduleService.nbDeblocagesDisponibles(station);
        response.deblocageObligatoire = moduleService.deblocageObligatoireEnAttente(station);
        
        response.modules = station.getModules().stream()
        .map(ModuleResponse::convert)
        .toList();
        
        response.astronautes = station.getAstronautes().stream()
        .map(AstronauteResponse::convert)
        .toList();
        
        response.stats = station.getStats().entrySet().stream()
        .map(entry -> RessourceResponse.convert(
            entry.getKey(),
            entry.getValue()
        ))
        .toList();
        
        return response;
    }
    
    public Integer getId() {
        return id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public int getCycle() {
        return cycle;
    }
    
    public int getColonnes() {
        return colonnes;
    }
    
    public int getLignes() {
        return lignes;
    }
    
    public boolean isPeutDebloquer() {
        return peutDebloquer;
    }
    
    public int getNbDeblocagesDisponibles() {
        return nbDeblocagesDisponibles;
    }
    
    public boolean isDeblocageObligatoire() {
        return deblocageObligatoire;
    }
    
    public List<ModuleResponse> getModules() {
        return modules;
    }
    
    public Integer getIdJoueur() {
        return idJoueur;
    }
    
    public void setIdJoueur(Integer idJoueur) {
        this.idJoueur = idJoueur;
    }
    
    public String getLoginJoueur() {
        return loginJoueur;
    }
    
    public void setLoginJoueur(String loginJoueur) {
        this.loginJoueur = loginJoueur;
    }
    
    public List<AstronauteResponse> getAstronautes() {
        return astronautes;
    }
    
    public List<RessourceResponse> getStats() {
        return stats;
    }
    
}