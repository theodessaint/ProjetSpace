package station.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.persistence.DiscriminatorValue;

public class JoueurTest {
    
    @Test 
    @DisplayName ("Un joueur hérite bien d'un compte")
    void heriteCompte(){
        assertTrue(new Joueur() instanceof Compte);
    }
    
    @Test 
    @DisplayName ("Un compte joueur est bien du type joueur")
    void typeAdmin(){
        DiscriminatorValue type = Joueur.class.getAnnotation(DiscriminatorValue.class);
        assertEquals("Joueur", type.value());
    }
    
    @Test 
    @DisplayName ("Le constructeur se comporte pareil que pour un compte admin")
    void memeConstruction(){
        Joueur joueur = new Joueur(1, "joueur", "joueur");
        
        assertNull(joueur.getId());
        assertEquals("joueur", joueur.getLogin());
        assertEquals("joueur", joueur.getPassword());
    }
}
