package station.api.request;

import jakarta.validation.constraints.NotNull;
import station.enumerator.RoleEnum;

public class CreateOrUpdateBonusRequest {

    @NotNull  
    private RoleEnum role;

    @NotNull 
    private Integer ressourceId;

    @NotNull
    private Double multiplicateur;

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public Integer getRessourceId() {
        return ressourceId;
    }

    public void setRessourceId(Integer ressourceId) {
        this.ressourceId = ressourceId;
    }

    public Double getMultiplicateur() {
        return multiplicateur;
    }

    public void setMultiplicateur(Double multiplicateur) {
        this.multiplicateur = multiplicateur;
    }
    
}
