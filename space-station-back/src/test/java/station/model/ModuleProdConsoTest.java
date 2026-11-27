package station.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import station.enumerator.RessourceEnum;

public class ModuleProdConsoTest {
    // --------------------------- CONSO --------------------------------------
    
    @Test 
    @DisplayName ("Constructeur, getters et setters")
    void moduleConso(){
        
        ModuleConso conso = new ModuleConso();
        Module module = new Module();
        
        conso.setId(1);
        conso.setRessource(RessourceEnum.EAU);
        conso.setValeur(10.0);
        conso.setModule(module);
        conso.setNbAstroAffectes(3);
        
        assertEquals(1, conso.getId());
        assertEquals(RessourceEnum.EAU, conso.getRessource());
        assertEquals(10.0, conso.getValeur());
        assertSame(module, conso.getModule());
        assertEquals(3, conso.getNbAstroAffectes());
    }
    
    @Test 
    @DisplayName ("Constructeur plein")
    void constructeurComplet (){
        
        Module module = new Module();
        ModuleConso conso = new ModuleConso(1, RessourceEnum.EAU, 10.0, module, 3);
        
        assertEquals(1, conso.getId());
        assertEquals(RessourceEnum.EAU, conso.getRessource());
        assertEquals(10.0, conso.getValeur());
        assertSame(module, conso.getModule());
        assertEquals(3, conso.getNbAstroAffectes()); 
    }
    
    // --------------------------- PROD --------------------------------------
    
    
    @Test 
    @DisplayName ("Constructeur, getters et setters")
    void moduleProd(){
        
        ModuleProd prod = new ModuleProd();
        Module module = new Module();
        
        prod.setId(1);
        prod.setRessource(RessourceEnum.EAU);
        prod.setValeur(10.0);
        prod.setModule(module);
        prod.setNbAstroAffectes(3);
        
        assertEquals(1, prod.getId());
        assertEquals(RessourceEnum.EAU, prod.getRessource());
        assertEquals(10.0, prod.getValeur());
        assertSame(module, prod.getModule());
        assertEquals(3, prod.getNbAstroAffectes());
    }
    
    @Test 
    @DisplayName ("Constructeur plein")
    void constructeurCompletProd (){
        
        Module module = new Module();
        ModuleProd conso = new ModuleProd(1, RessourceEnum.EAU, 10.0, module, 3);
        
        assertEquals(1, conso.getId());
        assertEquals(RessourceEnum.EAU, conso.getRessource());
        assertEquals(10.0, conso.getValeur());
        assertSame(module, conso.getModule());
        assertEquals(3, conso.getNbAstroAffectes()); 
    }
    
    @Test
    @DisplayName("toString contient l'id, ressource, valeur, module et nbAstroAffectes")
    void toStringComplet() {
        ModuleProd p = new ModuleProd(1, RessourceEnum.ENERGIE, 3.0, new Module(), 4);
        
        String texte = p.toString();
        
        assertTrue(texte.contains("id=1"), "id absent : " + texte);
        assertTrue(texte.contains("ressource=Énergie"), "energie absente : " + texte);
        assertTrue(texte.contains("valeur=3.0"), "valeur absente : " + texte);
        assertTrue(texte.contains("nbAstroAffectes=4"), "nbAstroAffectes absent : " + texte);
    }
    
    @Test
    @DisplayName("La ressource est stockée en texte, pas en numéro d'ordre")
    void ressourceEnTexte() throws Exception {
        for (Class<?> classe : new Class<?>[] { ModuleProd.class, ModuleConso.class }) {
            
            Enumerated annotation = classe.getDeclaredField("ressource").getAnnotation(Enumerated.class);
            
            // Sans EnumType.STRING, JPA stocke le numéro d'ordre de la constante.
            // Insérer une valeur au milieu de RessourceEnum ferait alors basculer
            // toutes les lignes existantes : l'eau deviendrait de l'oxygène, en
            // silence, sans la moindre erreur.
            assertEquals(EnumType.STRING, annotation.value(),
            classe.getSimpleName() + " : la ressource doit être stockée en texte");
        }
    }
    
    @Test
    @DisplayName("Productions et consommations vivent dans deux tables séparées")
    void deuxTablesDistinctes() {
        // Les deux classes sont jumelles, mais ce sont bien deux tables :
        // c'est ce qui permet à Module d'avoir deux listes indépendantes.
        assertEquals("modules_prod", ModuleProd.class.getAnnotation(Table.class).name());
        assertEquals("modules_conso", ModuleConso.class.getAnnotation(Table.class).name());
    }
}
