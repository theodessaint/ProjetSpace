package station.api.response;

import station.enumerator.RoleEnum;
import station.model.Bonus;

public class BonusResponse {
    private Integer id;
    private RoleEnum role;
    private Integer ressourceId;
    private Double multiplicateur;

    
    public static BonusResponse convert(Bonus bonus) {
        BonusResponse response = new BonusResponse();
        
        response.id = bonus.getId();
        response.role = bonus.getRole();
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

    public Integer getRessourceId() {
        return ressourceId;
    }

    public Double getMultiplicateur() {
        return multiplicateur;
    }
    
}
