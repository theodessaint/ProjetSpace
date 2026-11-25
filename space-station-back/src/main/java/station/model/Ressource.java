package station.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import station.enumerator.RessourceEnum;

@Embeddable
public class Ressource {

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