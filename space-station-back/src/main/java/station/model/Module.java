package station.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ManyToAny;

import jakarta.annotation.Generated;
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

    // @XtoY
    private List<moduleProd> prodModules = new ArrayList<moduleProd>();

    // @XtoY
    private List<moduleConso> consoModules = new ArrayList<moduleConso>();

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

    public List<moduleProd> getProdModules() {
        return prodModules;
    }

    public void setProdModules(List<moduleProd> prodModules) {
        this.prodModules = prodModules;
    }

    public List<moduleConso> getConsoModules() {
        return consoModules;
    }

    public void setConsoModules(List<moduleConso> consoModules) {
        this.consoModules = consoModules;
    }

    @Override
    public String toString() {
        return "Module [id=" + id + ", nom=" + nom + ", nbAstroModuleMax=" + nbAstroModuleMax + ", nbModuleMax="
                + nbModuleMax + ", debloque=" + debloque + ", position=" + position + "]";
    }

}