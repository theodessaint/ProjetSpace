package station.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import station.enumerator.GenreEnum;
import station.enumerator.RoleEnum;

public class CreateOrUpdateAstronauteRequest {
    
    @NotBlank
    private String nom;

    @NotNull
    private GenreEnum genre;

    @NotNull 
    private RoleEnum role;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public GenreEnum getGenre() {
        return genre;
    }

    public void setGenre(GenreEnum genre) {
        this.genre = genre;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

}
