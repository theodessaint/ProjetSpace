package station.enumerator;

public enum RessourceEnum {

    EAU(1, "Eau"),
    NOURRITURE(2, "Nourriture"),
    OXYGENE(3, "Oxygène"),
    ENERGIE(4, "Énergie");

    private final int id;
    private final String libelle;

    private RessourceEnum(int id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public int getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}