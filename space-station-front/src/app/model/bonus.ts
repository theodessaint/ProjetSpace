export interface BonusResponse {
  id: number;
  role: string;           // nom de la constante RoleEnum
  module: string;         // nom de la constante ModuleEnum
  ressourceId: number;
  ressource: string;      // libellé : "Eau", "Oxygène"…
  multiplicateur: number;
}