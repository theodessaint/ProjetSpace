package station.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

@Entity
@Embeddable 
@DiscriminatorValue("Joueur")
public class Joueur extends Compte {

    public Joueur(Integer id, String login, String password) {
        super(null, login, password);
    }

    public Joueur() {
    }
    
}
