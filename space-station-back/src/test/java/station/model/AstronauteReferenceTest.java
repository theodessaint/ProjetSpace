package station.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import station.enumerator.GenreEnum;
import station.enumerator.RoleEnum;

public class AstronauteReferenceTest {
    @Test 
    @DisplayName ("Le constructeur vide laisse tous les champs à null")
    void constructeurVide(){
        AstronauteReference a = new AstronauteReference();
        assertNull(a.getId());
        assertNull(a.getNom());
        assertNull(a.getGenre());
        assertNull(a.getRole());
    }

    @Test
    @DisplayName("Le constructeur complet initialise tous les champs (Alan SHEPARD)")
    void constructeurComplet() {
        AstronauteReference a = new AstronauteReference(13, "Alan SHEPARD", GenreEnum.Homme, RoleEnum.Agriculteur);

        assertEquals(13, a.getId());
        assertEquals("Alan SHEPARD", a.getNom());
        assertEquals(GenreEnum.Homme, a.getGenre());
        assertEquals(RoleEnum.Agriculteur, a.getRole());
    }
    @Test 
    @DisplayName ("Le constructeur complet accepte un astronaute sans rôle")
    void constructeurCompletSansRole() {
        AstronauteReference a = new AstronauteReference(5,  "Thomas PESQUET", GenreEnum.NB, null);
        assertEquals(5, a.getId());
        assertEquals("Thomas PESQUET", a.getNom());
        assertEquals(GenreEnum.NB, a.getGenre());
        assertNull(a.getRole());
    }

    @Test
    @DisplayName("Les setters modifient bien les valeurs")
    void settersModifientLesValeurs() {
        AstronauteReference ref = new AstronauteReference();

        ref.setId(15);
        ref.setNom("Sally RIDE");
        ref.setGenre(GenreEnum.Femme);
        ref.setRole(RoleEnum.Agriculteur);

        assertEquals(15, ref.getId());
        assertEquals("Sally RIDE", ref.getNom());
        assertEquals(GenreEnum.Femme, ref.getGenre());
        assertEquals(RoleEnum.Agriculteur, ref.getRole());
    }   
}
