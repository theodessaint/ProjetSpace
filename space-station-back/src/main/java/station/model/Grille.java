package station.model;

import java.util.List;

import station.enumerator.ModuleEnum;

public class Grille {

    public static final int nb_Colonnes = 7; // A modifier si on change la taille du jeu mais faudra tout modifier
                                             // derrière x)
    public static final int nb_Lignes = 5;

    public static final List<Emplacement> PLAN = List.of( // Attribution des emplacements et types de modules

            new Emplacement(ModuleEnum.Dortoir, new Position(3, 0), true), // ajout de true ou false pour les 5 modules
                                                                           // qui sont automatiquement débloqués au
                                                                           // lancement de CHAQUE partie
            new Emplacement(ModuleEnum.CentreDeCommandement, new Position(3, 1), true),

            new Emplacement(ModuleEnum.RecyclageEauEtOxygene, new Position(0, 1), false),
            new Emplacement(ModuleEnum.RecyclageEauEtOxygene, new Position(1, 1), false),
            new Emplacement(ModuleEnum.RecyclageEauEtOxygene, new Position(2, 1), false),

            new Emplacement(ModuleEnum.SalleDesMachines, new Position(3, 2), false),
            new Emplacement(ModuleEnum.SalleDesMachines, new Position(3, 3), false),
            new Emplacement(ModuleEnum.SalleDesMachines, new Position(3, 4), false),

            new Emplacement(ModuleEnum.Serre, new Position(4, 1), false),
            new Emplacement(ModuleEnum.Serre, new Position(5, 1), false),
            new Emplacement(ModuleEnum.Serre, new Position(6, 1), false)

    );

    public static final List<Position> PREMIERS_MODULES = List.of(

            new Position(2, 1),
            new Position(3, 2),
            new Position(4, 1));
// prend serre1, salle des machines 1, recyclage 1

    public static boolean estPremierModule(Position p) {
        return PREMIERS_MODULES.contains(p);
    }
//parcourt liste des PREMIERS_MODULES et regarde si une des positions est égale à p

    public static boolean estOuvertAuDepart(Position p) {
        return PLAN.stream().anyMatch(e -> e.getPosition().equals(p) && e.isOuvertAuDepart());
    }
//regarde si la position p correspond à un emplacement du PLAN marqué True (genre ouvert au départ)

    public static boolean contient(Position p) {

        return p.getX() >= 0 && p.getX() < nb_Colonnes
                && p.getY() >= 0 && p.getY() < nb_Lignes;

    }

}
