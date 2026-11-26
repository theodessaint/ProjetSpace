import { StatutPartie } from "./StatutPartie";

export interface StationSpatialeResume {
    
    id: number;
    nom: string;
    cycle: number;
    nbModulesDebloques: number;
    statut: StatutPartie
}
