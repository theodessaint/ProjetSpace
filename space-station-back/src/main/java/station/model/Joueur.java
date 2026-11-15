package station.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Joueur")
public class Joueur extends Compte {

    public Joueur(Integer id, String login, String password) {
        super(null, login, password);
    }

    public Joueur() {
    }
    
}
