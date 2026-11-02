package station.model;

import java.util.List;
import java.util.Map;

import javax.management.relation.RelationSupport;

public class StationSpatiale {
    
    private Integer id;
    
    private String nom;
    
    private Map<Ressource, Double> stats;
    
    private int cycle;
    
    private Joueur joueur;
    
    private List<Astronaute> astronautes;
    
    private List<ModuleDansMaGame> modules;
    
    public StationSpatiale() {}
    
    
    public StationSpatiale(Integer id, String nom, Map<Ressource, Double> stats, int cycle, Joueur joueur,
        List<ModuleDansMaGame> modules) {
            this.id = id;
            this.nom = nom;
            this.stats = stats;
            this.cycle = cycle;
            this.joueur = joueur;
            this.modules = modules;
        }   
        
        public Integer getId() {
            return id;
        }
        
        public void setId(Integer id) {
            this.id = id;
        }
        
        public String getNom() {
            return nom;
        }
        
        public void setNom(String nom) {
            this.nom = nom;
        }
        
        public Map<Ressource, Double> getStats() {
            return stats;
        }
        
        public void setStats(Map<Ressource, Double> stats) {
            this.stats = stats;
        }
        
        public int getCycle() {
            return cycle;
        }
        
        public void setCycle(int cycle) {
            this.cycle = cycle;
        }
        
        public Joueur getJoueur() {
            return joueur;
        }
        
        public void setJoueur(Joueur joueur) {
            this.joueur = joueur;
        }
        
        public List<Astronaute> getAstronautes() {
            return astronautes;
        }
        
        public void setAstronautes(List<Astronaute> astronautes) {
            this.astronautes = astronautes;
        }
        
        public List<ModuleDansMaGame> getModules() {
            return modules;
        }
        
        public void setModules(List<ModuleDansMaGame> modules) {
            this.modules = modules;
        }
        
        public void executerCycle() {
            
            // DEROULEMENT DU CYCLE
            
            if (this.cycle == 0) {
                // Découverte du jeu, constitution de l'équipage
                // Appel d'une fonction spécifique ?
            } else {
                // Réorganisation de l'équipage
                
                // Ajout de modules
                
                // Affectation des astronautes aux modules
            }
            
            
            // CONSOMMATION DES RESSOURCES ET EVALUATION DES SEUILS
            
            List<Ressource> ressourcesCritique;
            List<Ressource> ressourcesDanger;
            
            for (Ressource ressource : ressources) {
                
                
                // Calcul de consommation de chaque ressource
                
                // Ajout des objets "Ressource" à des listes => si besoin de récupérer d'autres attributs que le nom.
                
                if (ressource.get() <= ressource.getCritique()) {
                    ressourcesCritique.add(ressource);
                    
                } else if (ressource.get() <= ressource.getDanger() && ressource.get() > ressource.getCritique) {
                    ressourcesDanger.add(ressource);
                }
                
                if (ressourcesCritique.size() > 0) {
                    List<String> nomsResCrit = ressourcesCritique.stream()
                    .map(Ressource::getNom)
                    .toList();
                    
                    switch (ressourcesCritique.size()) {
                        case 1:
                        System.out.println(nomsResCrit.getFirst() + " : seuil critique détecté !");
                        break;
                        
                        case 2:
                        System.out.println("Seuils critiques détectés pour les ressources " + nomsResCrit.toString().replaceAll("[\\[\\]]", "") +"\nÉvacuation recommandée !");
                        break;
                        
                        default:
                        System.out.println("Seuils critiques détectés pour les ressources " + nomsResCrit.toString().replaceAll("[\\[\\]]", "") +"\nÉvacuation immédiate !");
                        
                        //GAME OVER ?
                        break;
                    }
                }
                
                if (ressourcesDanger.size() > 0) {
                    System.out.println("Attention ! Vous avez " ressourcesDanger.size() + " incohérences de ressources.");
                    
                }
                
                
            }
            
            
        }

        public void afficherEtat(){}

        public Ressource ajouterRessource(){}

        public ModuleDansMaGame ajouterModule(){}
}