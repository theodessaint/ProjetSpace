package station.api.request;

import jakarta.validation.constraints.NotNull;

public class CreateOrUpdateRessourceRequest {

    @NotNull
    private Integer ressourceTypeId;

    @NotNull
    private Double start;

    @NotNull
    private Double danger;

    @NotNull
    private Double critique;

    public Integer getRessourceTypeId() {
        return ressourceTypeId;
    }

    public void setRessourceTypeId(Integer ressourceTypeId) {
        this.ressourceTypeId = ressourceTypeId;
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
