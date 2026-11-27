import { Astronaute } from "./astronaute";
import { Module } from "./module";
import { Ressource } from "./ressource";
import { StatutPartieEnum } from "./statut-partie";
export interface StationSpatiale {

    id: number;
    nom: string;
    cycle: number;
    statut: StatutPartieEnum;
    colonnes: number;
    lignes: number;
    peutDebloquer: boolean;
    nbDeblocagesDisponibles: number;
    deblocageObligatoire: boolean;
    modules: Module[];
    astronautes: Astronaute[];
    idJoueur: number;
    loginJoueur: string;
    stats: Ressource[];

}
