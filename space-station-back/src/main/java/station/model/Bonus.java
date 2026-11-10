package station.model;

import java.util.List;

import station.enumerator.RoleEnum;

public class Bonus {

    private RoleEnum role;             // C'est notre enum Role
    private Ressource ressource;   // ressource impactée
    private double multiplicateur; // 1.15 = +15 %, 0.90 = -10 %

 public Bonus(RoleEnum role, Ressource ressource, double multiplicateur) {
        this.role = role;
        this.ressource = ressource;
        this.multiplicateur = multiplicateur;
    }

    public RoleEnum getRole() { return role; }
    public Ressource getRessource() { return ressource; }
    public double getMultiplicateur() { return multiplicateur; }

    // Liste des bonus du jeu (un par rôle), à ajuster
    public static final List<Bonus> BONUS = List.of(
        new Bonus(RoleEnum.Agriculteur, Ressource.NOURRITURE, 1.15), // +15 % nourriture
        new Bonus(RoleEnum.Mecanicien,  Ressource.ENERGIE,    1.10), // +10 % énergie
        new Bonus(RoleEnum.Chercheur,   Ressource.ENERGIE,    0.90), // -10 % conso d'énergie
        new Bonus(RoleEnum.Plombier,    Ressource.EAU,    0.90), // -10 % conso d'eau
        new Bonus(RoleEnum.Medecin,     Ressource.EAU,        0.85)  // -15 % conso d'eau
    );
}