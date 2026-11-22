package station.api.response;

import java.util.List;

import station.model.Grille;
import station.model.StationSpatiale;

public class StationSpatialeResponse {
    
    private Integer id;
    private String nom;
    private int cycle;
    private int colonnes;
    private int lignes;
    private boolean peutDebloquer;
    private List<ModuleResponse> modules;
    
    public static StationSpatialeResponse convert (StationSpatiale station, boolean peutDebloquer) {
        
        StationSpatialeResponse response = new StationSpatialeResponse();
        
        response.id = station.getId();
        response.nom = station.getNom();
        response.cycle = station.getCycle();
        
        response.colonnes = Grille.nb_Colonnes;
        response.lignes = Grille.nb_Lignes;
        
        response.peutDebloquer = peutDebloquer;
        
        response.modules = station.getModules().stream()
        .map(ModuleResponse::convert)
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
    
    public List<ModuleResponse> getModules() {
        return modules;
    }
}
