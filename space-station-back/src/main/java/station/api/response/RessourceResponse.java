package station.api.response;

import station.model.Ressource;

public class RessourceResponse {

    private Integer id;
    private Integer ressourceTypeId;
    private Double start;
    private Double danger;
    private Double critique;

    public static RessourceResponse convert(Ressource ressource) {
        RessourceResponse response = new RessourceResponse();

        response.id = ressource.getId();
        response.ressourceTypeId = ressource.getType().getId();
        response.start = ressource.getStart();
        response.danger = ressource.getDanger();
        response.critique = ressource.getCritique();

        return response;
    }

    public Integer getId() {
        return id;
    }

    public Integer getRessourceTypeId() {
        return ressourceTypeId;
    }

    public Double getStart() {
        return start;
    }

    public Double getDanger() {
        return danger;
    }

    public Double getCritique() {
        return critique;
    }

}
