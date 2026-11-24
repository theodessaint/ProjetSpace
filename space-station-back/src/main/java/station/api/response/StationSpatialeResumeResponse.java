package station.api.response;

import station.model.StationSpatiale;

public class StationSpatialeResumeResponse {
    
    private Integer id;
    private String nom;
    private Integer cycle;
    private long nbModulesDebloques;
    
    public static StationSpatialeResumeResponse convert(StationSpatiale station){
        StationSpatialeResumeResponse response = new StationSpatialeResumeResponse();
        
        response.id = station.getId();
        response.nom = station.getNom();
        response.cycle = station.getCycle();
        
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

    public long getNbModulesDebloques() {
        return nbModulesDebloques;
    }

}
