package station.model;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import station.enumerator.RoleEnum;

@Entity 
public class ModuleConso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private RoleEnum roleEnum;

    @Column(nullable = false)
    private Integer value;

    @Column(nullable = false)
    private Module module;

    public ModuleConso () {}

    public ModuleConso(Integer id, RoleEnum roleEnum, Integer value, Module module) {
        this.id = id;
        this.roleEnum = roleEnum;
        this.value = value;
        this.module = module;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RoleEnum getRoleEnum() {
        return roleEnum;
    }

    public void setRoleEnum(RoleEnum roleEnum) {
        this.roleEnum = roleEnum;
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
        return "ModuleConso [id=" + id + ", roleEnum=" + roleEnum + ", value=" + value + ", module=" + module + "]";
    }

}
