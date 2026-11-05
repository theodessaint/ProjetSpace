package station.model;

public class Ressource {

    // Ressources du jeu :
    public static final Ressource EAU        = new Ressource(1, "Eau",        100, 30, 15);
    public static final Ressource NOURRITURE = new Ressource(2, "Nourriture", 100, 30, 10);
    public static final Ressource OXYGENE    = new Ressource(3, "Oxygène",    100, 40, 20);
    public static final Ressource ENERGIE    = new Ressource(4, "Énergie",    100, 25, 10);

    private Integer id;
    private String nom;
    private double start;     // niveau de départ d'une partie (A définir)
    private double danger;    // en dessous de ce seuil : alerte "danger" (< 30 %)
    private double critique;  // en dessous de ce seuil : alerte "critique" (< 10 %)

    public Ressource(Integer id, String nom, double start, double danger, double critique) {
        this.id = id;
        this.nom = nom;
        this.start = start;
        this.danger = danger;
        this.critique = critique;
    }

    public Integer getId() { return id; }
    public String getNom() { return nom; }
    public double getStart() { return start; }
    public double getDanger() { return danger; }
    public double getCritique() { return critique; }

    @Override
    public String toString() {
        return nom;
    }
}