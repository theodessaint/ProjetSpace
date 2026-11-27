package station.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import station.enumerator.RessourceEnum;

public class RessourceTest {
        @Test
    @DisplayName("Le constructeur vide laisse le type à null et les seuils à 0")
    void constructeurVide() {
        Ressource r = new Ressource();

        assertNull(r.getType());
        assertEquals(0.0, r.getStart());
        assertEquals(0.0, r.getDanger());
        assertEquals(0.0, r.getCritique());
    }

    @Test
    @DisplayName("Le constructeur complet initialise tous les champs")
    void constructeurComplet() {
        Ressource r = new Ressource(RessourceEnum.EAU, 100.0, 30.0, 10.0);

        assertEquals(RessourceEnum.EAU, r.getType());
        assertEquals(100.0, r.getStart());
        assertEquals(30.0, r.getDanger());
        assertEquals(10.0, r.getCritique());
    }

    @Test
    @DisplayName("Les setters modifient bien les valeurs")
    void settersModifientLesValeurs() {
        Ressource r = new Ressource();

        r.setType(RessourceEnum.OXYGENE);
        r.setStart(80.0);
        r.setDanger(25.0);
        r.setCritique(5.0);

        assertEquals(RessourceEnum.OXYGENE, r.getType());
        assertEquals(80.0, r.getStart());
        assertEquals(25.0, r.getDanger());
        assertEquals(5.0, r.getCritique());
    }

    @Test 
    @DisplayName ("getNom() retourne le libellé de l'énumération")
    void getNomRetourneLeLibelle() {
        Ressource r = new Ressource(RessourceEnum.NOURRITURE, 50.0, 15.0, 5.0);
        assertEquals("Nourriture", r.getNom());
    }   

    @Test 
    @DisplayName ("toString() retourne le libellé de l'énumération")
    void toStringRetourneLeLibelle() {
        Ressource r = new Ressource(RessourceEnum.ENERGIE, 200.0, 50.0, 20.0);
        assertEquals("Énergie", r.toString());
    }   

}
