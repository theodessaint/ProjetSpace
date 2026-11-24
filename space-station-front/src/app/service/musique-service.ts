import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class MusiqueService {

  // providedIn: 'root' garantit une seule instance pour toute l'application,
  // donc un seul Audio, qui survit aux changements de page.
  private audio = new Audio('Assets/Sons/mondamusic-space-589110.mp3');

  // Signal et non booléen simple : en mode zoneless, c'est ce qui permet
  // au bouton de coupure de se redessiner quand l'état change.
  active = signal(false);

  constructor() {
    this.audio.loop = true;
    this.audio.volume = 0.15;   // 1.0 est beaucoup trop fort pour un fond sonore
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

  /** Volume entre 0 et 1. */
  setVolume(valeur: number): void {
    this.audio.volume = valeur;
  }
}