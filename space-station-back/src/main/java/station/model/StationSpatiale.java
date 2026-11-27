package station.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;
import station.enumerator.RessourceEnum;
import station.enumerator.StatutPartieEnum;
import station.exception.RessourceNotFoundException;

@Entity 
@Table(name = "stations")
public class StationSpatiale {
    
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String nom;
    
    @ManyToOne
    @JoinColumn(name = "joueur_id", nullable = false)
    private Joueur joueur;
    
    @Column(nullable = false)
    private StatutPartieEnum statut;
    
    @PositiveOrZero
    @Column(nullable = false)
    private int cycle;
    
    @ElementCollection
    @CollectionTable (
        name = "stations_stats",
        joinColumns = @JoinColumn(name = "station_id")
    )
    @MapKeyJoinColumn(name = "ressource_id")
    @Column(name = "quantite")
    private Map<Ressource, Double> stats;
    
    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL) // Ce qui est fait a la station est aussi fait aux astronautes
    private List<Astronaute> astronautes = new ArrayList<Astronaute>();
    
    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL) // Idem
    private List<Module> modules = new ArrayList<Module>();
    
    public StationSpatiale() {}
    
    public StationSpatiale(Integer id, String nom, Joueur joueur, StatutPartieEnum statut, @PositiveOrZero int cycle,
        Map<Ressource, Double> stats, List<Astronaute> astronautes, List<Module> modules
    ) 
    {
        this.id = id;
        this.nom = nom;
        this.joueur = joueur;
        this.statut = statut;
        this.cycle = cycle;
        this.stats = stats;
        this.astronautes = astronautes;
        this.modules = modules;
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
    
    public Joueur getJoueur() {
        return joueur;
    }
    
    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }
    
    public StatutPartieEnum getStatut() {
        return statut;
    }
    
    public void setStatut(StatutPartieEnum statut) {
        this.statut = statut;
    }
    
    public int getCycle() {
        return cycle;
    }
    
    public void setCycle(int cycle) {
        this.cycle = cycle;
    }
    
    public Map<Ressource, Double> getStats() {
        return stats;
    }
    
    public void setStats(Map<Ressource, Double> stats) {
        this.stats = stats;
    }
    
    public List<Astronaute> getAstronautes() {
        return astronautes;
    }
    
    public void setAstronautes(List<Astronaute> astronautes) {
        this.astronautes = astronautes;
    }
    
    public List<Module> getModules() {
        return modules;
    }
    
    public void setModules(List<Module> modules) {
        this.modules = modules;
    }
    
    private Ressource getRessource(RessourceEnum type) {
        return stats.keySet().stream()
        .filter(r -> r.getType() == type)
        .findFirst()
        .orElseThrow(RessourceNotFoundException::new);
    }
    
    public double getQuantite(RessourceEnum type) {
        return stats.get(getRessource(type));
    }
    
    public void setQuantite(RessourceEnum type, double variation) {
        Ressource ressource = getRessource(type);
        
        stats.compute(
            ressource, (r, quantite) -> quantite + variation
        );
        
    }
    
    
    public void executerCycle() {
        this.cycle++;
    }
    
    public void ajouterModule(Module module){
        module.setStation(this);
        modules.add(module);
    }
}