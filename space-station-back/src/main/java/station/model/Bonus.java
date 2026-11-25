package station.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import station.enumerator.ModuleEnum;
import station.enumerator.RessourceEnum;
import station.enumerator.RoleEnum;

@Entity
public class Bonus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Enumerated(EnumType.STRING)
    private RoleEnum role;
    
    @Enumerated(EnumType.STRING)
    private ModuleEnum module;
    
    @Enumerated(EnumType.STRING)
    private RessourceEnum ressource;
    
    private double multiplicateur;
    
    public Bonus() {}

    public Bonus(Integer id, RoleEnum role, ModuleEnum module, RessourceEnum ressource, double multiplicateur) {
        this.id = id;
        this.role = role;
        this.module = module;
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

    public ModuleEnum getModule() {
        return module;
    }

    public void setModule(ModuleEnum module) {
        this.module = module;
    }

    public RessourceEnum getRessource() {
        return ressource;
    }

    public void setRessource(RessourceEnum ressource) {
        this.ressource = ressource;
    }

    public double getMultiplicateur() {
        return multiplicateur;
    }

    public void setMultiplicateur(double multiplicateur) {
        this.multiplicateur = multiplicateur;
    }

    @Override
    public String toString() {
        return "Bonus [id=" + id + ", role=" + role + ", module=" + module + ", ressource=" + ressource
                + ", multiplicateur=" + multiplicateur + "]";
    }
    
}