package station.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

public class ModuleTest {
    
    @Test
    @DisplayName("Le constructeur complet initialise tous les champs")
    void constructeurComplet() {
        Position position = new Position(3, 0);
        StationSpatiale station = new StationSpatiale();
        List<ModuleProd> prods = new ArrayList<>();
        List<ModuleConso> consos = new ArrayList<>();
        List<Astronaute> astros = new ArrayList<>();
        
        Module m = new Module();
        
        m.setId(1);
        m.setNom("Dortoir");
        m.setNbAstroModuleMax(4);
        m.setNbModuleMax(1);
        m.setDebloque(true);
        m.setPosition(position);
        m.setStation(station);
        m.setProdModules(prods);
        m.setConsoModules(consos);
        m.setAstronautes(astros);
        
        assertEquals(1, m.getId());
        assertEquals("Dortoir", m.getNom());
        assertEquals(4, m.getNbAstroModuleMax());
        assertEquals(1, m.getNbModuleMax());
        assertTrue(m.isDebloque());
        assertSame(position, m.getPosition());
        assertSame(station, m.getStation());
        assertTrue(m.getProdModules().isEmpty());
        assertTrue(m.getConsoModules().isEmpty());
        assertTrue(m.getAstronautes().isEmpty());
    }
    
    @Test 
    @DisplayName ("Unique constraint")
    void contrainteDUnicite(){
        Table table = Module.class.getAnnotation(Table.class);
        
        UniqueConstraint contrainte = table.uniqueConstraints()[0];
        
        assertEquals("modules", table.name());
        assertArrayEquals(new String[] {"station_id", "x", "y"}, contrainte.columnNames());
    }
    
    @Test
    @DisplayName("toString contient l'id, le nom, les capacités et l'état de déblocage")
    void toStringComplet() {
        Module m = new Module(5, "Dortoir", 4, 1, true, new Position(3, 0), new StationSpatiale());
        
        String texte = m.toString();
        
        assertTrue(texte.contains("id=5"), "id absent : " + texte);
        assertTrue(texte.contains("nom=Dortoir"), "nom absent : " + texte);
        assertTrue(texte.contains("nbAstroModuleMax=4"), "nbAstroModuleMax absent : " + texte);
        assertTrue(texte.contains("nbModuleMax=1"), "nbModuleMax absent : " + texte);
        assertTrue(texte.contains("debloque=true"), "debloque absent : " + texte);
    }
    
    @Test
    @DisplayName("Un module neuf a ses collections prêtes et naît verrouillé")
    void etatInitial() {
        Module m = new Module();

        assertTrue(m.getProdModules().isEmpty());
        assertTrue(m.getConsoModules().isEmpty());
        assertTrue(m.getAstronautes().isEmpty());

        assertFalse(m.isDebloque());
    }

    @Test
    @DisplayName("La station est obligatoire")
    void stationObligatoire() throws Exception {
        JoinColumn colonne = Module.class.getDeclaredField("station").getAnnotation(JoinColumn.class);

        // Le nom de la colonne est repris tel quel par la contrainte d'unicité.
        assertEquals("station_id", colonne.name());

        // nullable = false : un module orphelin n'a aucun sens, il appartient
        // forcément à une partie. C'est la base qui le garantit.
        assertFalse(colonne.nullable(), "un module orphelin n'a aucun sens");
    }
}
