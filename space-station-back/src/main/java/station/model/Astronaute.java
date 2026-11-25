package station.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import station.enumerator.GenreEnum;
import station.enumerator.RoleEnum;

@Entity
@Table(name = "astronautes")
public class Astronaute {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "astronaute_reference_id")
    private AstronauteReference reference;
    
    @ManyToOne
    @JoinColumn(name="module_id")
    private Module module;
    
    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private StationSpatiale station;
    
    public Astronaute() {}
    
    public Astronaute(Integer id, AstronauteReference reference, Module module,
        StationSpatiale station) {
        this.id = id;
        this.reference = reference;
        this.module = module;
        this.station = station;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public AstronauteReference getReference() {
        return this.reference;
    }
    
    public void setReference(AstronauteReference reference) {
        this.reference = reference;
    }
    
    public String getNom() {
        return this.getReference().getNom();
    }
    
    public GenreEnum getGenre() {
        return this.getReference().getGenre();
    }
    
    public RoleEnum getRole() {
        return this.getReference().getRole();
    }
    
    public Module getModule() {
        return module;
    }
    
    public void setModule(Module module) {
        this.module = module;
    }
    
    public StationSpatiale getStation() {
        return station;
    }
    
    public void setStation(StationSpatiale station) {
        this.station = station;
    }
    
    @Override
    public String toString() {
        return "Astronaute [id=" + id + ", nom=" + getNom() + ", genre=" + getGenre() + ", role="
        + getRole() + "]";
    }    
}