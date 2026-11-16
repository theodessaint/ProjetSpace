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
    
    
    public static ModuleResponse convert(Module module) {
        ModuleResponse response = new ModuleResponse();
        
        response.id = module.getId();
        response.nom = module.getNom();
        response.nbAstroModuleMax = module.getNbAstroModuleMax();
        response.nbModuleMax = module.getNbModuleMax();
        response.debloque = module.isDebloque();
        response.positionX = module.getPosition().getX();
        response.positionY = module.getPosition().getY();
        response.stationId = module.getStation().getId();
        
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
