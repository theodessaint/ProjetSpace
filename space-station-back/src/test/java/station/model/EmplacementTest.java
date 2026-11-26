package station.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
}
