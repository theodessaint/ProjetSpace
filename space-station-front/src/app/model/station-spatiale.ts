import { Module } from "./module";
import { Astronaute } from "./astronaute";
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

}
