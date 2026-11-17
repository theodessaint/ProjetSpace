package station.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@DiscriminatorValue ("Admin")
public class Admin extends Compte {

    public Admin(Integer id, String login, String password) {
        super(null, login, password);
    }

    public Admin() {
    }
    
    //1 compte admin (id=0)

}
