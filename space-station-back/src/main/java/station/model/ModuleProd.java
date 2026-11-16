package station.model;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import station.enumerator.ModuleEnum;
import station.enumerator.RessourceEnum;

@Entity 
@DiscriminatorValue ("Production")
public class ModuleProd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private final RessourceEnum type;

    @Column(nullable = false)
    private int valeur;

    @Embedded
    private Module module;

    public ModuleProd (Integer id, RessourceEnum type, int valeur, Module module) {
        
        this.id = id;
        this.type = type;
        this.valeur = valeur;
        this.module = module;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RessourceEnum getType() {
        return type;
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @Override
    public String toString() {
        return "ModuleProd [id=" + id + ", type=" + type + ", valeur=" + valeur + ", module=" + module + "]";
    }
    
    

}
