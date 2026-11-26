export enum RessourceEnum {
  EAU = 'Eau',
  NOURRITURE = 'Nourriture',
  OXYGENE = 'Oxygène',
  ENERGIE = 'Énergie'
}
export interface Ressource {
  type: RessourceEnum;
  start: number;
  danger: number;
  critique: number;
  quantite: number;
}
