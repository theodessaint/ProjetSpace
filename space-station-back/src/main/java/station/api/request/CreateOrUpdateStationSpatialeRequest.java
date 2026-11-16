package station.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateOrUpdateStationSpatialeRequest {

    @NotBlank
    private String nom;

    @NotNull
    private Integer cycle;

    @NotNull 
    private Integer joueurId;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public Integer getJoueurId() {
        return joueurId;
    }

    public void setJoueurId(Integer joueurId) {
        this.joueurId = joueurId;
    }

      
}
