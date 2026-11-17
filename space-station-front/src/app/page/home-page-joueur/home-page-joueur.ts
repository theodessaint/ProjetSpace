import { AfterViewInit, Component, ElementRef, OnDestroy, ViewChild, ViewEncapsulation, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth-service';

@Component({
  imports: [],
  selector: 'app-home-page-joueur',
  styleUrl: './home-page-joueur.css',
  templateUrl: './home-page-joueur.html',
  encapsulation: ViewEncapsulation.None,
})
export class HomePageJoueur implements AfterViewInit, OnDestroy {
  private router = inject(Router);
  private authService = inject(AuthService);

  @ViewChild('astronaute') astro!: ElementRef<HTMLImageElement>;
  @ViewChild('regles') regles!: ElementRef<HTMLDialogElement>;
  private timer?: ReturnType<typeof setInterval>;

  username = this.authService.username;

  difficulte = 'normal';
  aides: Record<string, string> = {
    facile:    'Ressources de départ généreuses / Les ressources sont consommées plus lentement et leurs créations sont augmentées.',
    normal:    'Ressources de départ standard / Les ressources sont consommées et produites de manière équilibrée.',
    difficile: 'Ressources de départ limitées / Les ressources sont consommées plus vite et leurs créations sont ralenties.',
  };

  ngAfterViewInit(): void {
    this.deplacer();
    this.timer = setInterval(() => this.deplacer(), 10000);
  }

  ngOnDestroy(): void {
    clearInterval(this.timer);
  }

  private deplacer(): void {
    const x = Math.random() * (window.innerWidth - 200);
    const y = Math.random() * (window.innerHeight - 200);
    const angle = Math.random() * 60 - 30;
    this.astro.nativeElement.style.transform = `translate(${x}px, ${y}px) rotate(${angle}deg)`;
  }

  ouvrirRegles(): void {
    this.regles.nativeElement.showModal();
  }

  nouvellePartie(): void {
    console.log('Créer une partie, difficulté :', this.difficulte);   // à brancher sur le back
  }

  reprendrePartie(): void {
    console.log('Reprendre la partie');   // à brancher sur le back
  }

  deconnexion(event: Event): void {
    event.preventDefault();
    this.authService.resetAuth();
    this.router.navigate(['/login']);
  }
}