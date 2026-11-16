package station.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import station.enumerator.RessourceEnum;

@Entity
public class Ressource {

    public static final Ressource EAU        = new Ressource(RessourceEnum.EAU,        100, 30, 15);
    public static final Ressource NOURRITURE = new Ressource(RessourceEnum.NOURRITURE, 100, 30, 10);
    public static final Ressource OXYGENE    = new Ressource(RessourceEnum.OXYGENE,    100, 40, 20);
    public static final Ressource ENERGIE    = new Ressource(RessourceEnum.ENERGIE,    100, 25, 10);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private RessourceEnum type;

    private double start;
    private double danger;
    private double critique;

    public Ressource() {
    }

    public Ressource(RessourceEnum type, double start, double danger, double critique) {
        this.type = type;
        this.start = start;
        this.danger = danger;
        this.critique = critique;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public RessourceEnum getType() { return type; }
    public void setType(RessourceEnum type) { this.type = type; }
    public double getStart() { return start; }
    public void setStart(double start) { this.start = start; }
    public double getDanger() { return danger; }
    public void setDanger(double danger) { this.danger = danger; }
    public double getCritique() { return critique; }
    public void setCritique(double critique) { this.critique = critique; }

    // utilisé par StationSpatiale.executerCycle()
    public String getNom() { return type.getLibelle(); }

    @Override
    public String toString() {
        return type.getLibelle();
    }
}