package station.api.request;

import jakarta.validation.constraints.NotNull;
import station.enumerator.RessourceEnum;

public class CreateOrUpdateModuleProductionRequest {
    
    @NotNull 
    private RessourceEnum ressource;
    
    @NotNull 
    private Double valeur;
    
    @NotNull 
    private Module module;
    
    public RessourceEnum getRessource() {
        return ressource;
    }
    
    public void setRessource(RessourceEnum ressource) {
        this.ressource = ressource;
    }
    
    public Double getValeur() {
        return valeur;
    }
    
    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }
    
    public Module getModule() {
        return module;
    }
    
    public void setModule(Module module) {
        this.module = module;
    }
}
