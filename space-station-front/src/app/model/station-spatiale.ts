import { Module } from "./module";
import { Astronaute } from "./astronaute";
import { StatutPartie } from "./StatutPartie";
import { Ressource } from "./ressource";
export interface StationSpatiale {

    id: number;
    nom: string;
    cycle: number;
    colonnes: number;
    lignes: number;
    peutDebloquer: boolean;
    nbDeblocagesDisponibles: number;
    deblocageObligatoire: boolean;
    modules: Module[];
    astronautes: Astronaute[];
    idJoueur: number;
    loginJoueur: string;
    statut: StatutPartie;
    stats: Ressource[];

}
