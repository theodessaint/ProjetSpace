package station.api.request;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import station.model.Astronaute;
import station.model.Joueur;
import station.model.Ressource;

public class CreateOrUpdateStationSpatialeRequest {

    @NotBlank
    private String nom;

    @NotNull
    private Map<Ressource, Double> stats;

    @NotNull
    private Integer cycle;

    @NotNull 
    private Joueur joueur;

    @NotNull
    private List<Astronaute> astronautes;

    @NotNull 
    private List<Ressource> ressources;

    @NotNull 
    private List<Module> modules;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Map<Ressource, Double> getStats() {
        return stats;
    }

    public void setStats(Map<Ressource, Double> stats) {
        this.stats = stats;
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public List<Astronaute> getAstronautes() {
        return astronautes;
    }

    public void setAstronautes(List<Astronaute> astronautes) {
        this.astronautes = astronautes;
    }

    public List<Ressource> getRessources() {
        return ressources;
    }

    public void setRessources(List<Ressource> ressources) {
        this.ressources = ressources;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }
    
}
