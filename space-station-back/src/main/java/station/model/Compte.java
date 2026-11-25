package station.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity 
@Table(name = "comptes")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn (name="TYPE_COMPTE")
public class Compte implements Serializable { //    Pas de abstract pour qu'un Compte puisse etre save dans la classe CompteService
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected  Integer id;

    @Column(nullable = false)
    protected  String login;

    @Column(nullable = false)
    protected  String password;
    
    public Compte(Integer id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public Compte() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }    
}
