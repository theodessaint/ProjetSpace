package station.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import station.enumerator.GenreEnum;
import station.enumerator.RoleEnum;

public class AstronauteTest {
    @Test 
    @DisplayName ("Le constructeur vide laisse tous les champs à null")
    public void testConstructeurVide() {
       Astronaute a = new Astronaute();
        assertNull(a.getId());
        assertNull(a.getReference());
        assertNull(a.getModule());
        assertNull(a.getStation());
    }

    @Test
    @DisplayName("Le constructeur complet initialise tous les champs")
    void constructeurComplet() {
        AstronauteReference ref = new AstronauteReference(13, "Alan SHEPARD", GenreEnum.Homme, RoleEnum.Agriculteur);
        Module module = new Module();
        StationSpatiale station = new StationSpatiale();

        Astronaute a = new Astronaute(10, ref, module, station);

        assertEquals(10, a.getId());
        assertSame(ref, a.getReference());
        assertSame(module, a.getModule());
        assertSame(station, a.getStation());
    } 
    @Test
    @DisplayName("Les setters modifient bien les valeurs")
    void settersModifientLesValeurs() {
        Astronaute a = new Astronaute();
        AstronauteReference ref = new AstronauteReference(15, "Sally RIDE", GenreEnum.Femme, RoleEnum.Agriculteur);
        Module module = new Module();
        StationSpatiale station = new StationSpatiale();

        a.setId(3);
        a.setReference(ref);
        a.setModule(module);
        a.setStation(station);

        assertEquals(3, a.getId());
        assertSame(ref, a.getReference());
        assertSame(module, a.getModule());
        assertSame(station, a.getStation());
    }

    @Test
    @DisplayName("Nom, genre et rôle viennent de la référence")
    void delegationALaReference() {
        Astronaute a = new Astronaute();
        a.setReference(new AstronauteReference(31, "Liwei YANG", GenreEnum.Homme, RoleEnum.Plombier));

        assertEquals("Liwei YANG", a.getNom());
        assertEquals(GenreEnum.Homme, a.getGenre());
        assertEquals(RoleEnum.Plombier, a.getRole());
    }
    
    @Test 
    @DisplayName ("toString() retourne une chaîne de caractères contenant les informations de l'astronaute")
    void testToString() {
        Astronaute a = new Astronaute();
        a.setId(7);
        a.setReference(new AstronauteReference(31, "Liwei YANG", GenreEnum.Homme, RoleEnum.Plombier));

        String texte = a.toString();

        assertTrue(texte.contains("id=7"), "id absent : " + texte);
        assertTrue(texte.contains("nom=Liwei YANG"), "nom absent : " + texte);
        assertTrue(texte.contains("genre=Homme"), "genre absent : " + texte);
        assertTrue(texte.contains("role=Plombier"), "rôle absent : " + texte);
    }     
}
