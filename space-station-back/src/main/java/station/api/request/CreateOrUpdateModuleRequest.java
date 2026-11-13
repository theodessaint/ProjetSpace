package station.api.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import station.model.StationSpatiale;

public class CreateOrUpdateModuleRequest {

    @NotBlank
    private String nom;

    @NotNull
    private Integer nbAstroModuleMax;

    @NotNull
    private Integer nbModuleMax;

    @NotNull
    private boolean debloque;

    @NotNull
    private Position position;

    @NotNull
    private StationSpatiale station;

    @NotNull
    private List<moduleProd> prodModules;

    @NotNull
    private List<moduleConso> consoModules

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getNbAstroModuleMax() {
        return nbAstroModuleMax;
    }

    public void setNbAstroModuleMax(Integer nbAstroModuleMax) {
        this.nbAstroModuleMax = nbAstroModuleMax;
    }

    public Integer getNbModuleMax() {
        return nbModuleMax;
    }

    public void setNbModuleMax(Integer nbModuleMax) {
        this.nbModuleMax = nbModuleMax;
    }

    public boolean isDebloque() {
        return debloque;
    }

    public void setDebloque(boolean debloque) {
        this.debloque = debloque;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public StationSpatiale getStation() {
        return station;
    }

    public void setStation(StationSpatiale station) {
        this.station = station;
    }

    public List<moduleProd> getProdModules() {
        return prodModules;
    }

    public void setProdModules(List<moduleProd> prodModules) {
        this.prodModules = prodModules;
    }

    public List<moduleConso> getConsoModules() {
        return consoModules;
    }

    public void setConsoModules(List<moduleConso> consoModules) {
        this.consoModules = consoModules;
    }
    
}
