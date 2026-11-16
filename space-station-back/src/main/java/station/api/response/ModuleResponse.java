package station.api.response;

import station.model.Module;

public class ModuleResponse {
    private Integer id;
    private String nom;
    private Integer nbAstroModuleMax;
    private Integer nbModuleMax;
    private boolean debloque;
    private Integer positionX;
    private Integer positionY;
    private Integer stationId;
    
    
    public static ModuleResponse convert(Module Module) {
        ModuleResponse response = new ModuleResponse();
        
        response.id = Module.getId();
        response.nom = Module.getNom();
        response.nbAstroModuleMax = Module.getNbAstroModuleMax();
        response.nbModuleMax = Module.getNbModuleMax();
        response.debloque = Module.isDebloque();
        response.positionX = Module.getPosition().getX();
        response.positionY = Module.getPosition().getY();
        response.stationId = Module.getStation().getId();
        
        return response;
    }


    public Integer getId() {
        return id;
    }


    public String getNom() {
        return nom;
    }


    public Integer getNbAstroModuleMax() {
        return nbAstroModuleMax;
    }


    public Integer getNbModuleMax() {
        return nbModuleMax;
    }


    public boolean isDebloque() {
        return debloque;
    }


    public Integer getPositionX() {
        return positionX;
    }


    public Integer getPositionY() {
        return positionY;
    }


    public Integer getStationId() {
        return stationId;
    }
    
    
}
