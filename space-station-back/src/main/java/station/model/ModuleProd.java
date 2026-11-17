package station.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
public class ModuleProd {
    
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

    public ModuleProd () {}

    public ModuleProd(Integer id, RessourceEnum ressource, Integer value, Module module) {
        this.id = id;
        this.ressource = ressource;
        this.value = value;
        this.module = module;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RessourceEnum getRessourceEnum() {
        return ressource;
    }

    public void setRessourceEnum(RessourceEnum ressource) {
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

    public String toString() {
        return "ModuleProd [id=" + id + ", ressourceEnum=" + ressource + ", value=" + value + ", module=" + module + "]";
    }
    
    

}
