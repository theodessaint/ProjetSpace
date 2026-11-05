public class Bonus {

    private Role role;             // C'est notre enum Role
    private Ressource ressource;   // ressource impactée
    private double multiplicateur; // 1.15 = +15 %, 0.90 = -10 %

 public Bonus(Role role, Ressource ressource, double multiplicateur) {
        this.role = role;
        this.ressource = ressource;
        this.multiplicateur = multiplicateur;
    }

    public Role getRole() { return role; }
    public Ressource getRessource() { return ressource; }
    public double getMultiplicateur() { return multiplicateur; }

    // Liste des bonus du jeu (un par rôle), à ajuster
    public static final List<Bonus> BONUS = List.of(
        new Bonus(Role.AGRICULTEUR, Ressource.NOURRITURE, 1.15), // +15 % nourriture
        new Bonus(Role.MECANICIEN,  Ressource.ENERGIE,    1.10), // +10 % énergie
        new Bonus(Role.CHERCHEUR,   Ressource.ENERGIE,    0.90), // -10 % conso d'énergie
        new Bonus(Role.PLOMBIER,    Ressource.EAU,    0.90), // -10 % conso d'eau
        new Bonus(Role.MEDECIN,     Ressource.EAU,        0.85)  // -15 % conso d'eau
    );
}