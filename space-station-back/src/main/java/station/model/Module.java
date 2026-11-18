package station.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity 
@Table(uniqueConstraints = 
@UniqueConstraint(columnNames = {"station_id", "x","y"})
)
public class Module {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private int nbAstroModuleMax;

    @Column(nullable = false)
    private int nbModuleMax;

    @Column(nullable = false)
    private boolean debloque;

    @Embedded
    private Position position;

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private StationSpatiale station;

    @OneToMany(mappedBy = "module")
    private List<ModuleProd> prodModules = new ArrayList<ModuleProd>();

    @OneToMany(mappedBy = "module")
    private List<ModuleConso> consoModules = new ArrayList<ModuleConso>();

    @OneToMany(mappedBy = "module")
    private List<Astronaute> astronautes = new ArrayList<Astronaute>();

    public Module () {}

    public Module(Integer id, String nom, int nbAstroModuleMax, int nbModuleMax, boolean debloque, Position position,
            StationSpatiale station) {
        this.id = id;
        this.nom = nom;
        this.nbAstroModuleMax = nbAstroModuleMax;
        this.nbModuleMax = nbModuleMax;
        this.debloque = debloque;
        this.position = position;
        this.station = station;
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

    public int getNbAstroModuleMax() {
        return nbAstroModuleMax;
    }

    public void setNbAstroModuleMax(int nbAstroModuleMax) {
        this.nbAstroModuleMax = nbAstroModuleMax;
    }

    public int getNbModuleMax() {
        return nbModuleMax;
    }

    public void setNbModuleMax(int nbModuleMax) {
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

    public List<ModuleProd> getProdModules() {
        return prodModules;
    }

    public void setProdModules(List<ModuleProd> prodModules) {
        this.prodModules = prodModules;
    }

    public List<ModuleConso> getConsoModules() {
        return consoModules;
    }

    public void setConsoModules(List<ModuleConso> consoModules) {
        this.consoModules = consoModules;
    }

    public List<Astronaute> getAstronautes() {
        return astronautes;
    }

    public void setAstronautes(List<Astronaute> astronautes) {
        this.astronautes = astronautes;
    }

    @Override
    public String toString() {
        return "Module [id=" + id + ", nom=" + nom + ", nbAstroModuleMax=" + nbAstroModuleMax + ", nbModuleMax="
                + nbModuleMax + ", debloque=" + debloque + ", position=" + position + "]";
    }

}