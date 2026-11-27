package station.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import station.enumerator.RessourceEnum;
import station.enumerator.StatutPartieEnum;
import station.exception.RessourceNotFoundException;

public class StationSpatialeTest {
    
    @Test 
    @DisplayName ("Constructeur vide")
    void constructeur(){
        
        Joueur joueur = new Joueur(1, "jordan", "abid");
        
        Map<Ressource, Double> stats = new HashMap<>();
        stats.put(new Ressource(RessourceEnum.EAU, 100, 30, 10), 100.0);
        
        List<Astronaute> astros = new ArrayList<>();
        List<Module> modules = new ArrayList<>();
        
        StationSpatiale station = new StationSpatiale();
        
        station.setId(1);
        station.setNom("jordan1");
        station.setJoueur(joueur);
        station.setStatut(StatutPartieEnum.VICTOIRE);
        station.setCycle(25);
        station.setStats(stats);
        station.setAstronautes(astros);
        station.setModules(modules);
        
        station.setQuantite(RessourceEnum.EAU, 25);
        
        assertEquals(1, station.getId());
        assertEquals("jordan1", station.getNom());
        assertSame(joueur, station.getJoueur());
        assertEquals(StatutPartieEnum.VICTOIRE, station.getStatut());
        assertEquals(25, station.getCycle());
        assertSame(stats, station.getStats());
        assertSame(astros, station.getAstronautes());
        assertSame(modules, station.getModules());
        
        // 100 au départ, + 25 de variation : setQuantite ajoute, elle ne remplace pas
        assertEquals(125.0, station.getQuantite(RessourceEnum.EAU));
    }
    
    @Test
    @DisplayName("Constructeur plein")
    void constructeurPlein() {
        
        Joueur joueur = new Joueur(1, "jordan", "abid");
        
        Map<Ressource, Double> stats = new HashMap<>();
        stats.put(new Ressource(RessourceEnum.EAU, 100, 30, 10), 100.0);
        
        List<Astronaute> astros = new ArrayList<>();
        List<Module> modules = new ArrayList<>();
        
        // ordre des parametres : id, nom, joueur, statut, cycle, stats, astronautes, modules
        StationSpatiale station = new StationSpatiale(
            1, "jordan1", joueur, StatutPartieEnum.VICTOIRE, 25, stats, astros, modules
        );
        
        assertEquals(1, station.getId());
        assertEquals("jordan1", station.getNom());
        assertSame(joueur, station.getJoueur());
        assertEquals(StatutPartieEnum.VICTOIRE, station.getStatut());
        assertEquals(25, station.getCycle());
        assertSame(stats, station.getStats());       // meme reference : pas de copie defensive
        assertSame(astros, station.getAstronautes());
        assertSame(modules, station.getModules());
        
        // les methodes metier doivent fonctionner sur un objet construit ainsi
        assertFalse(station.estEnCours());           // statut = VICTOIRE, donc pas EN_COURS
        station.setQuantite(RessourceEnum.EAU, 25);
        assertEquals(125.0, station.getQuantite(RessourceEnum.EAU));
    }
    
    @Test
    @DisplayName("executerCycle incrémente le cycle de 1")
    void executerCycle() {
        StationSpatiale s = new StationSpatiale();
        
        s.executerCycle();
        s.executerCycle();
        
        assertEquals(2, s.getCycle());
    }
    
    @Test
    @DisplayName("ajouterModule ajoute le module à la liste et lui assigne la station")
    void ajouterModule() {
        StationSpatiale s = new StationSpatiale();
        Module module = new Module();
        
        s.ajouterModule(module);
        
        assertEquals(1, s.getModules().size());
        assertSame(module, s.getModules().get(0));
        assertSame(s, module.getStation());
    }
    
    @Test
    @DisplayName("estEnCours est vrai uniquement pour le statut EN_COURS")
    void estEnCours() {
        StationSpatiale s = new StationSpatiale();
        
        s.setStatut(StatutPartieEnum.EN_COURS);
        assertTrue(s.estEnCours());
        
        s.setStatut(StatutPartieEnum.VICTOIRE);
        assertFalse(s.estEnCours());
        
        s.setStatut(StatutPartieEnum.DEFAITE);
        assertFalse(s.estEnCours());
    }
    
    @Test
    @DisplayName("getQuantite sur une ressource absente leve une exception")
    void ressourceAbsente() {
        
        Map<Ressource, Double> stats = new HashMap<>();
        stats.put(new Ressource(RessourceEnum.EAU, 100, 30, 10), 100.0);   // que de l'eau dans la map
        
        StationSpatiale station = new StationSpatiale();
        station.setStats(stats);
        
        // on demande l'oxygene : le filter ne trouve rien, le orElseThrow se declenche
        assertThrows(RessourceNotFoundException.class,
            () -> station.getQuantite(RessourceEnum.OXYGENE));
            
            // meme chemin via le setter, qui passe aussi par getRessource()
            assertThrows(RessourceNotFoundException.class,
                () -> station.setQuantite(RessourceEnum.OXYGENE, 10));
            }
        }
        