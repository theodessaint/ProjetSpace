import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class MusiqueService {

  // providedIn: 'root' garantit une seule instance pour toute l'application,
  // donc un seul Audio, qui survit aux changements de page.
  private audio = new Audio('Assets/Sons/mondamusic-space-589110.mp3');

  // Volume de référence. Le fondu s'y rapporte, il ne le remplace pas.
  private volumeMax = 0.15;

  // Durée du fondu en entrée et en sortie, en secondes.
  private dureeFondu = 5;

  // Signal et non booléen simple : en mode zoneless, c'est ce qui permet
  // au bouton de coupure de se redessiner quand l'état change.
  active = signal(false);

  constructor() {
    this.audio.loop = true;
    this.audio.volume = this.volumeMax;

    // timeupdate se déclenche environ quatre fois par seconde pendant
    // la lecture : assez fin pour piloter un fondu de volume.
    this.audio.addEventListener('timeupdate', () => this.ajusterVolume());
  }

  /**
   * À appeler depuis un vrai clic de l'utilisateur : les navigateurs
   * refusent toute lecture sonore qui ne suit pas une interaction.
   */
  demarrer(): void {
    this.audio.play()
      .then(() => this.active.set(true))
      .catch(() => console.warn('Lecture audio refusée par le navigateur'));
  }

  arreter(): void {
    this.audio.pause();
    this.active.set(false);
  }

  /** Coupe ou relance, pour un bouton unique. */
  basculer(): void {
    if (this.active()) {
      this.arreter();
    } else {
      this.demarrer();
    }
  }

  /**
   * Volume de référence, entre 0 et 1.
   * On modifie volumeMax et non audio.volume, sinon le réglage serait
   * écrasé au prochain ajustement du fondu.
   */
  setVolume(valeur: number): void {
    this.volumeMax = Math.max(0, Math.min(1, valeur));
  }

  /**
   * Baisse le volume sur les dernières secondes du morceau et le remonte
   * sur les premières, pour que la reprise de la boucle ne s'entende pas.
   */
  private ajusterVolume(): void {
    const duree = this.audio.duration;

    // duration vaut NaN tant que les métadonnées du fichier
    // ne sont pas chargées.
    if (!duree || isNaN(duree)) {
      return;
    }

    const position = this.audio.currentTime;
    const restant = duree - position;
    let facteur = 1;

    if (restant < this.dureeFondu) {
      facteur = restant / this.dureeFondu;      // fin : on descend vers 0
    } else if (position < this.dureeFondu) {
      facteur = position / this.dureeFondu;     // début : on remonte vers 1
    }

    // Le volume doit rester entre 0 et 1, sinon le navigateur lève une erreur.
    this.audio.volume = Math.max(0, Math.min(1, this.volumeMax * facteur));
  }
}