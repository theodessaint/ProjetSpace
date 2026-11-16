package station.api.request;

import jakarta.validation.constraints.NotNull;

public class CreateOrUpdateModuleProductionRequest {
    
    @NotNull
    private Integer ressourceId;

    @NotNull 
    private Double valeur;

    @NotNull 
    private Integer moduleId;

    public Integer getRessourceId() {
        return ressourceId;
    }

    public void setRessourceId(Integer ressourceId) {
        this.ressourceId = ressourceId;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }
}
