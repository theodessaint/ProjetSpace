import { AfterViewInit, Component, ElementRef, OnDestroy, ViewChild, ViewChildren, QueryList, ViewEncapsulation, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth-service';
import { JoueurService } from '../../service/joueur-service';
import { StationSpatialeResume } from '../../model/station-spatiale-resume';
import { FormsModule } from '@angular/forms';

@Component({
  imports: [FormsModule],
  selector: 'app-home-page-joueur',
  styleUrl: './home-page-joueur.css',
  templateUrl: './home-page-joueur.html',
  encapsulation: ViewEncapsulation.None,
})
export class HomePageJoueur implements AfterViewInit, OnDestroy {
  private router = inject(Router);
  private authService = inject(AuthService);
  private joueurService = inject(JoueurService);
  
  @ViewChildren('astronaute') astros!: QueryList<ElementRef<HTMLImageElement>>;
  @ViewChild('regles') regles!: ElementRef<HTMLDialogElement>;
  @ViewChild('dialogNom') dialogNom!: ElementRef<HTMLDialogElement>;
  private timer?: ReturnType<typeof setInterval>;
  
  nouveauNom = '';
  
  username = this.authService.username;
  
  difficulte = 'normal';
  aides: Record<string, string> = {
    facile:    'Ressources de départ généreuses / Les ressources sont consommées plus lentement et leurs créations sont augmentées.',
    normal:    'Ressources de départ standard / Les ressources sont consommées et produites de manière équilibrée.',
    difficile: 'Ressources de départ limitées / Les ressources sont consommées plus vite et leurs créations sont ralenties.',
  };
  
  parties: StationSpatialeResume[] = [];
  
  listeAffichee = false;
  
  ngAfterViewInit(): void {
    this.deplacer();
    this.timer = setInterval(() => this.deplacer(), 10000);
  }
  
  ngOnDestroy(): void {
    clearInterval(this.timer);
  }
  
  private deplacer(): void {
    this.astros.forEach(astros => {
      const x = Math.random() * (window.innerWidth - 200);
      const y = Math.random() * (window.innerHeight - 200);
      const angle = Math.random() * 60 - 30;
      astros.nativeElement.style.transform = `translate(${x}px, ${y}px) rotate(${angle}deg)`;
    });
    
  }
  
  ouvrirRegles(): void {
    this.regles.nativeElement.showModal();
  }
  
  nouvellePartie(): void {
    this.nouveauNom = '';
    this.dialogNom.nativeElement.showModal();
    
  }
  
  confirmerCreation(): void {
    console.log('Créer une partie : ', this.nouveauNom,  ' | difficulté : ', this.difficulte);   
    
    this.joueurService.creerPartie(this.nouveauNom).subscribe(reponse => {
      console.log('Partie créée, id : ', reponse.id);
      this.router.navigate(['/game', reponse.id]);
    });
  }
  
  chargerListeParties(): void {
    console.log('Liste des parties');   // à brancher sur le back
    
    this.joueurService.findMesParties().subscribe(parties => {
      console.log('Parties du joueur :', parties);
      this.parties = parties;
      this.listeAffichee = true;
    });
  }
  
  demarrerPartie(id: number): void {
    console.log('Chargement de la partie', id);
    
    this.router.navigate(['/game/', id]);
  }
  
  deconnexion(event: Event): void {
    event.preventDefault();
    this.authService.resetAuth();
    this.router.navigate(['/login']);
  }
}