package station.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.persistence.DiscriminatorValue;

public class AdminTest {
    
    @Test 
    @DisplayName ("Un administrateur est bien un compte")
    void heriteDeCompte(){
        assertTrue(new Admin() instanceof Compte);
    }

    @Test 
    @DisplayName ("Un compte admin est bien du type Admin")
    void typeAdmin(){
        DiscriminatorValue type = Admin.class.getAnnotation(DiscriminatorValue.class);
        assertEquals("Admin", type.value());
    }

    @Test 
    @DisplayName ("Le constructeur se comporte pareil que pour un compte joueur")
    void memeConstruction(){
        Admin admin = new Admin(1, "admin", "admin");

        assertNull(admin.getId());
        assertEquals("admin", admin.getLogin());
        assertEquals("admin", admin.getPassword());
    }
}
