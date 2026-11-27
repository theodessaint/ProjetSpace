package station.api.response;

import station.enumerator.StatutPartieEnum;
import station.model.StationSpatiale;

public class StationSpatialeResumeResponse {
    
    private Integer id;
    private String nom;
    private Integer cycle;
    private StatutPartieEnum statut;
    private long nbModulesDebloques;
    
    public static StationSpatialeResumeResponse convert(StationSpatiale station){
        StationSpatialeResumeResponse response = new StationSpatialeResumeResponse();
        
        response.id = station.getId();
        response.nom = station.getNom();
        response.cycle = station.getCycle();
        response.statut = station.getStatut();
        
        response.nbModulesDebloques = station.getModules().stream()
        .filter(module -> module.isDebloque())
        .count();

        return response;
    }

    public Integer getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public Integer getCycle() {
        return cycle;
    }

    public StatutPartieEnum getStatut(){
        return statut;
    }

    public long getNbModulesDebloques() {
        return nbModulesDebloques;
    }

}
