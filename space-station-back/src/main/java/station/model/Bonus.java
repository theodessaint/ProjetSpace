package station.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import station.enumerator.RoleEnum;

@Entity
public class Bonus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @ManyToOne
    private Ressource ressource;

    private double multiplicateur;

    public Bonus() {
    }

    public Bonus(RoleEnum role, Ressource ressource, double multiplicateur) {
        this.role = role;
        this.ressource = ressource;
        this.multiplicateur = multiplicateur;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public RoleEnum getRole() { return role; }
    public void setRole(RoleEnum role) { this.role = role; }
    public Ressource getRessource() { return ressource; }
    public void setRessource(Ressource ressource) { this.ressource = ressource; }
    public double getMultiplicateur() { return multiplicateur; }
    public void setMultiplicateur(double multiplicateur) { this.multiplicateur = multiplicateur; }

    // Liste des bonus du jeu (inchangée)
    public static final List<Bonus> BONUS = List.of(
        new Bonus(RoleEnum.Agriculteur, Ressource.NOURRITURE, 1.15),
        new Bonus(RoleEnum.Mecanicien,  Ressource.ENERGIE,    1.10),
        new Bonus(RoleEnum.Chercheur,   Ressource.ENERGIE,    0.90),
        new Bonus(RoleEnum.Plombier,    Ressource.EAU,        0.90),
        new Bonus(RoleEnum.Medecin,     Ressource.EAU,        0.85)
    );
}