package station.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import station.enumerator.GenreEnum;
import station.enumerator.RoleEnum;

@Entity
public class Astronaute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;

    @Enumerated(EnumType.STRING)
    private GenreEnum genre;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    public Astronaute() {
    }

    public Astronaute(Integer id, String nom, GenreEnum genre, RoleEnum role) {
        this.id = id;
        this.nom = nom;
        this.genre = genre;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        return "Astronaute [id=" + id + ", nom=" + nom + ", genre=" + genre + ", role=" + role + "]";
    }
}