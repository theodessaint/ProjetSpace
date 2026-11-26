package station.api.response;

import station.enumerator.ModuleEnum;
import station.enumerator.RoleEnum;
import station.model.Bonus;

public class BonusResponse {
    private Integer id;
    private RoleEnum role;
    private ModuleEnum module;
    private String ressource;
    private Integer ressourceId;
    private Double multiplicateur;

    
    public static BonusResponse convert(Bonus bonus) {
        BonusResponse response = new BonusResponse();
        
        response.id = bonus.getId();
        response.role = bonus.getRole();
        response.module = bonus.getModule();
        response.ressource = bonus.getRessource().getLibelle();
        response.ressourceId = bonus.getRessource().getId();
        response.multiplicateur = bonus.getMultiplicateur();
        
        return response;
    }


    public Integer getId() {
        return id;
    }


    public RoleEnum getRole() {
        return role;
    }


    public ModuleEnum getModule() {
        return module;
    }


    public Integer getRessourceId() {
        return ressourceId;
    }


    public String getRessource() { 
        return ressource;
        }

    public Double getMultiplicateur() {
        return multiplicateur;
    }
    
}
