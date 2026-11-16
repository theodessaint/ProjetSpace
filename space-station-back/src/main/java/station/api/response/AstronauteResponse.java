package station.api.response;

import station.enumerator.GenreEnum;
import station.enumerator.RoleEnum;
import station.model.Astronaute;

public class AstronauteResponse {

    private Integer id;
    private String nom;
    private GenreEnum genre;
    private RoleEnum role;
    private Integer stationId;

    public static AstronauteResponse convert(Astronaute astronaute) {
        AstronauteResponse response = new AstronauteResponse();

        response.id = astronaute.getId();
        response.nom = astronaute.getNom();
        response.genre = astronaute.getGenre();
        response.role = astronaute.getRole();
        response.stationId = astronaute.getStation().getId();

        return response;
    }

    public Integer getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public GenreEnum getGenre() {
        return genre;
    }

    public RoleEnum getRole() {
        return role;
    }

    public Integer getStationId() {
        return stationId;
    }

}