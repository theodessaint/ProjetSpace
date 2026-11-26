package station.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GrilleTest {
    
    @Test 
    @DisplayName ("Les quatre coins sont dans la grille")
    void contientLesCoins(){
        assertTrue(Grille.contient(new Position(0, 0)));
        assertTrue(Grille.contient(new Position(Grille.nb_Colonnes - 1, Grille.nb_Lignes - 1)));
    }

    @Test 
    @DisplayName ("Refus d'une position en dehors de la grille")
    void refusHorsGrille(){
        assertFalse(Grille.contient(new Position(-1, 0)));
        assertFalse(Grille.contient(new Position(0, -1)));
        assertFalse(Grille.contient(new Position(Grille.nb_Colonnes, 0)));
        assertFalse(Grille.contient(new Position(0, Grille.nb_Lignes)));
    }

    @Test 
    @DisplayName ("Tout le plan est dans la grille")
    void planDansGrille(){
        for (Emplacement emplacement : Grille.PLAN){
            assertTrue(Grille.contient(emplacement.getPosition()), "Position hors grille dans le plan" + emplacement.getPosition()); // 2e partie permet de savoir à quel position le test fail si jamais il fail et pas juste expected true but was false
                                                                                                                                     // j'ai rajouté un toString dans Position pour que la concatenation renvoie la position correctement
        }
    }

    @Test 
    @DisplayName ("Plan contient 11 emplacements distinct")
    void emplacementsIndependants(){
        assertEquals(11, Grille.PLAN.size());
        long distincts = Grille.PLAN.stream().map(Emplacement::getPosition).distinct().count(); // Permet de compter le nombre d'emplacements avec une position unique 
        assertEquals(Grille.PLAN.size(), distincts, "Deux emplacements sont identiques");
    }

    @Test 
    @DisplayName ("Centre de commandement et dortoir ouverts au début de partie")
    void centreEtDortoirOuverts(){
        assertTrue(Grille.estOuvertAuDepart(new Position(3, 0))); // Dortoir 
        assertTrue(Grille.estOuvertAuDepart(new Position(3, 1))); // Centre de commandement
        assertFalse(Grille.estOuvertAuDepart(new Position(1, 1))); // Recyclage 2
    }

    @Test 
    @DisplayName ("Trois premiers modules touchent le centre de commandement")
    void troisPremierModules(){
        Position centre = new Position(3, 1);
        assertEquals(3, Grille.PREMIERS_MODULES.size()); // Il y a bien 3 premiers modules

        for (Position p : Grille.PREMIERS_MODULES){
            assertTrue(Grille.estPremierModule(p)); // la position est bien la position d'un des premiers modules
            assertTrue(p.estVoisineDe(centre), p + "n'est pas voisin du centre de commadnement"); // Module est voisin de centre de commandement 
        }
    }

}
