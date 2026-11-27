package station.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PositionTest {
    
    @Test 
    @DisplayName ("Deux cases côte à côte (horizontal) sont voisines")
    void voisineHorizontalement(){
        assertTrue(new Position(3, 1).estVoisineDe(new Position(2, 1)));
        assertTrue(new Position(3, 1).estVoisineDe(new Position(4, 1)));
    }

    @Test 
    @DisplayName ("Deux cases côte à côte (vertical) sont voisines")
    void voidVerticalement(){
        assertTrue(new Position(3,1).estVoisineDe(new Position(3, 0)));
        assertTrue(new Position(3, 1).estVoisineDe(new Position(3, 2)));
    }

    @Test 
    @DisplayName ("Deux cases en diagonales ne sont pas voisines")
    void diagoPasVoisines(){
        assertFalse(new Position(3, 1).estVoisineDe(new Position(4, 2)));
    }

    @Test 
    @DisplayName ("Une case n'est pas sa propre voisin")
    void memeCaseNonVoisine(){
        assertFalse(new Position(3, 1).estVoisineDe(new Position(3, 1)));
    }

    @Test
    @DisplayName ("Deux cases séparées par une autre ne sont pas voisines")
    void casesSepareesPasVoisines(){
        assertFalse(new Position(3, 1).estVoisineDe(new Position(1, 1)));
    }

    @Test 
    @DisplayName ("Deux positions ayant les mêmes coordonnées sont égales")
    void egaliteParCoordonnees(){
        assertEquals(new Position(3, 1), new Position(3, 1));
        assertEquals(new Position(3, 1).hashCode(), new Position(3, 1).hashCode());
    }

    @Test 
    @DisplayName ("Init et setters")
    void initEtSetters(){
        Position position = new Position();

        position.setX(3);
        position.setY(1);

        assertEquals(3, position.getX());
        assertEquals(1, position.getY());
    }
    
}
