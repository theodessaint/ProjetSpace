package station.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CompteTest {
    
    @Test 
    @DisplayName ("Le constructeur fonctionne correctement")
    void constructeur(){
        Compte compte = new Compte(1, "jordan", "abid");
        assertEquals(1, compte.getId());
        assertEquals("jordan", compte.getLogin());
        assertEquals("abid", compte.getPassword());
    }

    @Test 
    @DisplayName ("Le constructeur vide reste vide")
    void constructeurVide(){
        assertNull(new Compte().getId());
    }

    @Test 
    @DisplayName ("Les attributs rendent bien ce qu'on leur donne")
    void attributs(){
        Compte compte = new Compte();

        compte.setId(1);
        compte.setLogin("jordan");
        compte.setPassword("abid");

        assertEquals(1, compte.getId());
        assertEquals("jordan", compte.getLogin());
        assertEquals("abid", compte.getPassword());
    }
    
}
