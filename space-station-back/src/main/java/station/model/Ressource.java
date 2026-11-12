package station.model;

import station.enumerator.RessourceEnum;

public class Ressource {

    // Ressources du jeu :
    public static final Ressource EAU        = new Ressource(RessourceEnum.Eau,        100, 30, 15);
    public static final Ressource NOURRITURE = new Ressource(RessourceEnum.Nourriture, 100, 30, 10);
    public static final Ressource OXYGENE    = new Ressource(RessourceEnum.Oxygène,    100, 40, 20);
    public static final Ressource ENERGIE    = new Ressource(RessourceEnum.Energie,    100, 25, 10);

    private final RessourceEnum type;
    private final double start;     // niveau de départ d'une partie (à définir)
    private final double danger;    // en dessous de ce seuil : alerte "danger"
    private final double critique;  // en dessous de ce seuil : alerte "critique"

    public Ressource(RessourceEnum type, double start, double danger, double critique) {
        this.type = type;
        this.start = start;
        this.danger = danger;
        this.critique = critique;
    }

    public RessourceEnum getType() { return type; }
    public int getId() { return type.ordinal() + 1; }   // compatibilité : 1 à 4
    public String getNom() { return type.name(); }     // compatibilité
    public double getStart() { return start; }
    public double getDanger() { return danger; }
    public double getCritique() { return critique; }

    @Override
    public String toString() {
        return type.name();
    }
}