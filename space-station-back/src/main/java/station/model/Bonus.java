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

    // Liste des bonus du jeu (un par rôle), à ajuster
    public static List<Bonus> bonus = List.of(
        new Bonus(RoleEnum.Agriculteur, Ressource.NOURRITURE, 1.15),
        new Bonus(RoleEnum.Mecanicien,  Ressource.ENERGIE,    1.10),
        new Bonus(RoleEnum.Plombier,    Ressource.EAU,        1.10),
        new Bonus(RoleEnum.Mecanicien,  Ressource.OXYGENE,    1.10)
    );

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @ManyToOne
    private Ressource ressource;

    private double multiplicateur;

    public Bonus() {}

    public Bonus(RoleEnum role, Ressource ressource, double multiplicateur) {
        this.role = role;
        this.ressource = ressource;
        this.multiplicateur = multiplicateur;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public Ressource getRessource() {
        return ressource;
    }

    public void setRessource(Ressource ressource) {
        this.ressource = ressource;
    }

    public double getMultiplicateur() {
        return multiplicateur;
    }

    public void setMultiplicateur(double multiplicateur) {
        this.multiplicateur = multiplicateur;
    }


    public static void setBonus(List<Bonus> bonus) {
        Bonus.bonus = bonus;
    }

    public static List<Bonus> getBonus() {
        return bonus;
    }      
    
}