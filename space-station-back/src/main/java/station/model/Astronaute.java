package station.model;

import javax.management.relation.Role;

import station.enumerator.GenreEnum;

public abstract class Astronaute {
    protected Integer id;
    protected String nom;
    protected GenreEnum genre;
    protected Role role;

    protected Astronaute(Integer id, String nom, GenreEnum genre, Role role){
        this.id=id;
        this.nom=nom;
        this.genre=genre;
        this.role=role;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Astronaute [id=" + id + ", nom=" + nom + ", genre=" + genre + ", role=" + role;
    }
    
}
