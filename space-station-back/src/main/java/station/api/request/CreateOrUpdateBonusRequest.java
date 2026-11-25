package station.api.request;

import jakarta.validation.constraints.NotNull;
import station.enumerator.RessourceEnum;
import station.enumerator.RoleEnum;

public class CreateOrUpdateBonusRequest {

    @NotNull 
    private RessourceEnum ressource;

    @NotNull  
    private RoleEnum role;

    @NotNull
    private Double multiplicateur;

    public RessourceEnum getRessource() {
        return ressource;
    }

    public void setRessource(RessourceEnum ressource) {
        this.ressource = ressource;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public Double getMultiplicateur() {
        return multiplicateur;
    }

    public void setMultiplicateur(Double multiplicateur) {
        this.multiplicateur = multiplicateur;
    }

       
}
