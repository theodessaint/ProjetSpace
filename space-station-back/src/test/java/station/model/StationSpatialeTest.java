package station.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import station.enumerator.RessourceEnum;
import station.enumerator.StatutPartieEnum;

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
}
    