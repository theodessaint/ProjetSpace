package station.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import station.enumerator.ModuleEnum;

public class EmplacementTest {
    
    @Test 
    @DisplayName ("Un emplacement conserve son type, sa position et son état débloqué de départ")
    void attributsConserves(){
        Emplacement emplacement = new Emplacement(ModuleEnum.SalleDesMachines, new Position(3, 2), false);

        assertEquals(ModuleEnum.SalleDesMachines, emplacement.getType());
        assertEquals(new Position(3, 2), emplacement.getPosition());
        assertFalse(emplacement.isOuvertAuDepart());
    }

    @Test 
    @DisplayName ("Un emplacement ouvert au départ reste ouvert")
    void ouvertDepart(){
        Emplacement centre = new Emplacement(ModuleEnum.CentreDeCommandement, new Position(3, 1), true);
        assertTrue(centre.isOuvertAuDepart());
    }
}
