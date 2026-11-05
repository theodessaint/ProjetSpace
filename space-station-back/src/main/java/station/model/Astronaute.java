package station.model;

public abstract class Astronaute {
    protected Integer id;
    protected String nom;
    protected Genre genre;
    protected Role role;

    protected Astronaute(Integer id, String nom, Genre genre, Role role){
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

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
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
