package station.api.request;

import jakarta.validation.constraints.NotNull;
import station.enumerator.RoleEnum;
import station.model.Ressource;

public class CreateOrUpdateBonusRequest {

    @NotNull  
    private RoleEnum role;

    @NotNull 
    private Ressource ressource;

    @NotNull
    private Double multiplicateur;

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public Ressource getRessource() {
        return ressource;
    }

    public void setRessource(Ressource ressource) {
        this.ressource = ressource;
    }

    public double getMultiplicateur() {
        return multiplicateur;
    }

    public void setMultiplicateur(double multiplicateur) {
        this.multiplicateur = multiplicateur;
    }

}
