package station.model;

import java.util.List;

import station.enumerator.ModuleEnum;

public class Grille {
    
    private static final int nb_Colonnes = 7; // A modifier si on change la taille du jeu mais faudra tout modifier derrière x)
    private static final int nb_Lignes = 5;

    private static final List<Emplacement> PLAN = List.of( // Attribution des emplacements et types de modules
        
        new Emplacement(ModuleEnum.Dortoir, new Position(3,0)),
        new Emplacement(ModuleEnum.CentreDeCommandement, new Position(3,1)),

        new Emplacement(ModuleEnum.RecyclageEauEtOxygene, new Position(0, 1)),
        new Emplacement(ModuleEnum.RecyclageEauEtOxygene, new Position(1, 1)),
        new Emplacement(ModuleEnum.RecyclageEauEtOxygene, new Position(2, 1)),

        new Emplacement(ModuleEnum.SalleDesMachines, new Position(3, 3)),
        new Emplacement(ModuleEnum.SalleDesMachines, new Position(3, 4)),
        new Emplacement(ModuleEnum.SalleDesMachines, new Position(3, 5)),

        new Emplacement(ModuleEnum.Serre, new Position(4, 1)),
        new Emplacement(ModuleEnum.Serre, new Position(5, 1)),
        new Emplacement(ModuleEnum.Serre, new Position(6, 1))

    );

    public static boolean contient(Position p) {

        return p.getX() >= 0 && p.getX() < nb_Colonnes
        && p.getY() >= 0 && p.getY() < nb_Lignes;

    }

}
