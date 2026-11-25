export interface Astronaute {
  id: number;
  nom: string;
  genre: string;
  role: string | null;
  moduleId: number | null; // le module où se trouve l'astronaute (Dortoir en théorie)
}