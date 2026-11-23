import { Module } from "./module";

export interface StationSpatiale {

    id: number;
    nom: string;
    cycle: number;
    colonnes: number;
    lignes: number;
    peutDebloquer: boolean;
    modules: Module[];

}
