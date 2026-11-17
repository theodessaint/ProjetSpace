package station.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
    private Integer positionX;

    @NotNull
    private Integer positionY;

    @NotNull
    private Integer stationId;

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

    public Integer getPositionX() {
        return positionX;
    }

    public void setPositionX(Integer positionX) {
        this.positionX = positionX;
    }

    public Integer getPositionY() {
        return positionY;
    }

    public void setPositionY(Integer positionY) {
        this.positionY = positionY;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }
}
