package station.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import station.enumerator.RessourceEnum;

@Entity 
@Table(name = "modules_prod")
public class ModuleProd {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING) 
    private RessourceEnum ressource;

    @Column(nullable = false)
    private Double valeur;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    private Integer nbAstroAffectes;

    public ModuleProd () {}

    public ModuleProd(Integer id, RessourceEnum ressource, Double valeur, Module module, Integer nbAstroAffectes) {
        this.id = id;
        this.ressource = ressource;
        this.valeur = valeur;
        this.module = module;
        this.nbAstroAffectes = nbAstroAffectes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RessourceEnum getRessource() {
        return ressource;
    }

    public void setRessource(RessourceEnum ressource) {
        this.ressource = ressource;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Integer getNbAstroAffectes() {
        return nbAstroAffectes;
    }

    public void setNbAstroAffectes(Integer nbAstroAffectes) {
        this.nbAstroAffectes = nbAstroAffectes;
    }

    @Override
    public String toString() {
        return "ModuleProd [id=" + id + ", ressource=" + ressource + ", valeur=" + valeur + ", module=" + module
                + ", nbAstroAffectes=" + nbAstroAffectes + "]";
    }
}
