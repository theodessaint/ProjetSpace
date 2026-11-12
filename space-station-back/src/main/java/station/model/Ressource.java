package station.model;

import station.enumerator.RessourceEnum;

public class Ressource {

    // Ressources du jeu :
    public static final Ressource EAU        = new Ressource(RessourceEnum.EAU,        100, 30, 15);
    public static final Ressource NOURRITURE = new Ressource(RessourceEnum.NOURRITURE, 100, 30, 10);
    public static final Ressource OXYGENE    = new Ressource(RessourceEnum.OXYGENE,    100, 40, 20);
    public static final Ressource ENERGIE    = new Ressource(RessourceEnum.ENERGIE,    100, 25, 10);

    private RessourceEnum type;
    private double start;     // niveau de départ d'une partie (à définir)
    private double danger;    // en dessous de ce seuil : alerte "danger"
    private double critique;  // en dessous de ce seuil : alerte "critique"

    public Ressource(RessourceEnum type, double start, double danger, double critique) {
        this.type = type;
        this.start = start;
        this.danger = danger;
        this.critique = critique;
    }

    public RessourceEnum getType() { return type; }
    public Integer getId() { return type.getId(); }
    public String getNom() { return type.getLibelle(); }
    public double getStart() { return start; }
    public double getDanger() { return danger; }
    public double getCritique() { return critique; }

    @Override
    public String toString() {
        return type.getLibelle();
    }
}