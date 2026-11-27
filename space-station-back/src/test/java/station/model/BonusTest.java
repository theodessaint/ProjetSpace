package station.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import station.enumerator.ModuleEnum;
import station.enumerator.RessourceEnum;
import station.enumerator.RoleEnum;

public class BonusTest {
    
    @Test 
    @DisplayName ("Init et getters")
    void initEtGetters(){
        
        Bonus bonus = new Bonus(1, RoleEnum.Plombier, ModuleEnum.RecyclageEauEtOxygene, RessourceEnum.EAU, 2.0);
        
        assertEquals(1, bonus.getId());
        assertEquals(RoleEnum.Plombier, bonus.getRole());
        assertEquals(ModuleEnum.RecyclageEauEtOxygene, bonus.getModule());
        assertEquals(RessourceEnum.EAU, bonus.getRessource());
        assertEquals(2.0, bonus.getMultiplicateur());
    }
    
    @Test 
    @DisplayName ("Setters")
    void setters(){
        Bonus bonus = new Bonus();
        
        bonus.setId(1);
        bonus.setRole(RoleEnum.Agriculteur);
        bonus.setModule(ModuleEnum.Serre);
        bonus.setRessource(RessourceEnum.NOURRITURE);
        bonus.setMultiplicateur(10);
        
        assertEquals(1, bonus.getId());
        assertEquals(RoleEnum.Agriculteur, bonus.getRole());
        assertEquals(ModuleEnum.Serre, bonus.getModule());
        assertEquals(RessourceEnum.NOURRITURE, bonus.getRessource());
        assertEquals(10, bonus.getMultiplicateur());
    }
    
    @Test
    @DisplayName("Un seul bonus par triplet rôle, module, ressource")
    void contrainteDUnicite() {
        Table table = Bonus.class.getAnnotation(Table.class);
        UniqueConstraint contrainte = table.uniqueConstraints()[0];
        
        // C'est la règle métier : un plombier dans une salle des machines ne peut
        // avoir qu'un seul multiplicateur sur l'énergie. La base le garantit,
        // ce test garantit que la base le sait encore.
        assertArrayEquals(new String[] { "role", "module", "ressource" }, contrainte.columnNames());
        assertEquals("uk_bonus_role_module_ressource", contrainte.name());
    }
}
