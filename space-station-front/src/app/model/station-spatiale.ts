import { Module } from "./module";

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
    nomJoueur: string;

}
