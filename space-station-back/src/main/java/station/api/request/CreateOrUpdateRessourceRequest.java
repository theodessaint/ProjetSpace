package station.api.request;

import jakarta.validation.constraints.NotNull;
import station.enumerator.RessourceEnum;
import station.model.Ressource;

public class CreateOrUpdateRessourceRequest {

    @NotNull
    private RessourceEnum ressource;

    @NotNull
    private Double start;

    @NotNull
    private Double danger;

    @NotNull
    private Double critique;

    public RessourceEnum getRessource() {
        return ressource;
    }

    public void setRessource(RessourceEnum ressource) {
        this.ressource = ressource;
    }

    public Double getStart() {
        return start;
    }

    public void setStart(Double start) {
        this.start = start;
    }

    public Double getDanger() {
        return danger;
    }

    public void setDanger(Double danger) {
        this.danger = danger;
    }

    public Double getCritique() {
        return critique;
    }

    public void setCritique(Double critique) {
        this.critique = critique;
    }

}
