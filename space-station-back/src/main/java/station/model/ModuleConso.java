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
import station.enumerator.RessourceEnum;

@Entity 
public class ModuleConso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING) 
    private RessourceEnum ressource;

    @Column(nullable = false)
    private Integer value;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    private Integer nbAstroAffectes;

    public ModuleConso(Integer id, RessourceEnum ressource, Integer value, Module module, Integer nbAstroAffectes) {
        this.id = id;
        this.ressource = ressource;
        this.value = value;
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

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Integer getnbAstroAffectes() {
        return nbAstroAffectes;
    }

    public void setnbAstroAffectes(Integer nbAstroAffectes) {
        this.nbAstroAffectes = nbAstroAffectes;
    }

}
