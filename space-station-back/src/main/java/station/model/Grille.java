package station.model;

import java.util.List;

import station.enumerator.ModuleEnum;

public class Grille {
    
    public static final int nb_Colonnes = 7; // A modifier si on change la taille du jeu mais faudra tout modifier derrière x)
    public static final int nb_Lignes = 5;

    public static final List<Emplacement> PLAN = List.of( // Attribution des emplacements et types de modules
        
        new Emplacement(ModuleEnum.Dortoir, new Position(3,0), true), // ajout de true ou false pour les 5 modules qui sont automatiquement débloqués au lancement de CHAQUE partie
        new Emplacement(ModuleEnum.CentreDeCommandement, new Position(3,1), true),

        new Emplacement(ModuleEnum.RecyclageEauEtOxygene, new Position(0, 1), false),
        new Emplacement(ModuleEnum.RecyclageEauEtOxygene, new Position(1, 1), false),
        new Emplacement(ModuleEnum.RecyclageEauEtOxygene, new Position(2, 1), true),

        new Emplacement(ModuleEnum.SalleDesMachines, new Position(3, 2), true),
        new Emplacement(ModuleEnum.SalleDesMachines, new Position(3, 3), false),
        new Emplacement(ModuleEnum.SalleDesMachines, new Position(3, 4), false),

        new Emplacement(ModuleEnum.Serre, new Position(4, 1), true),
        new Emplacement(ModuleEnum.Serre, new Position(5, 1), false),
        new Emplacement(ModuleEnum.Serre, new Position(6, 1), false)

    );

    public static boolean contient(Position p) {

        return p.getX() >= 0 && p.getX() < nb_Colonnes
        && p.getY() >= 0 && p.getY() < nb_Lignes;

    }

}
