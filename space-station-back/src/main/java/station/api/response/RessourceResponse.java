package station.api.response;

import station.enumerator.RessourceEnum;
import station.model.Ressource;

public record RessourceResponse(
    RessourceEnum type,
    double start,
    double danger,
    double critique,
    double quantite
) {
    public static RessourceResponse convert(Ressource ressource, double quantite) {
        return new RessourceResponse(
            ressource.getType(),
            ressource.getStart(),
            ressource.getDanger(),
            ressource.getCritique(),
            quantite
        );
    }
}