package station.model;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import station.enumerator.RessourceEnum;

@Entity 
@DiscriminatorValue ("Consommation")
public class ModuleConso {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private final RessourceEnum type;

    @Column(nullable = false)
    private int valeur;

    @Embedded
    private Module module;

    public ModuleConso(Integer id, RessourceEnum type, int valeur, Module module) {
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
        return "ModuleConso [id=" + id + ", type=" + type + ", valeur=" + valeur + ", module=" + module + "]";
    }

    

    
}
