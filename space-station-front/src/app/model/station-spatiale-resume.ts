import { StatutPartieEnum } from "./statut-partie";

export interface StationSpatialeResume {

    id: number;
    nom: string;
    cycle: number;
    nbModulesDebloques: number;
    statut: StatutPartieEnum
}
