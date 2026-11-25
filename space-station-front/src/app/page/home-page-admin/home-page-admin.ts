import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild, ViewEncapsulation, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth-service';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../service/admin-service';
import { StationSpatiale } from '../../model/station-spatiale';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-home-page-admin',
  imports: [FormsModule],
  templateUrl: './home-page-admin.html',
  styleUrl: './home-page-admin.css',
  encapsulation: ViewEncapsulation.None,
})
export class HomePageAdmin implements OnInit {
  private router = inject(Router);
  private authService = inject(AuthService);
  private adminService = inject(AdminService);

  @ViewChild('astronaute') astro!: ElementRef<HTMLImageElement>;
  private timer?: ReturnType<typeof setInterval>;

  username = this.authService.username;

  parties = signal<StationSpatiale[]>([]);

  nombreParties: number = 0;

  listeAffichee = false;
  

  ngOnInit(): void {
    // Chargement au montage du composant : l'admin vient pour voir
    // la liste, inutile de lui faire cliquer un bouton.
    this.adminService.findAll().subscribe(parties => {
      console.log('Première partie :', parties[0]);
      this.parties.set(parties);
    });
  }

  private charger(): void {
    this.adminService.findAll().subscribe(parties => this.parties.set(parties));
  }

  modifier(partie: StationSpatiale): void {
    const nouveauNom = prompt('Nouveau nom de la partie :', partie.nom)?.trim();
    if (!nouveauNom || nouveauNom === partie.nom) return;

    const maj: StationSpatiale = { ...partie, nom: nouveauNom };
    console.log('Envoi de la modification :', maj);

    this.adminService.modifier(maj)
      .pipe(finalize(() => this.charger()))   // recharge la liste quoi qu'il arrive
      .subscribe({
        next: (reponse) => console.log('Réponse du back :', reponse),
        error: (err) => console.error('Erreur lors de la modification', err),
      });
  }

  supprimer(partie: StationSpatiale): void {
    if (!confirm(`Supprimer définitivement la partie « ${partie.nom} » ?`)) return;

    this.adminService.supprimer(partie.id).subscribe({
      next: () => {
        this.parties.update(liste => liste.filter(p => p.id !== partie.id));
      },
      error: (err) => console.error('Erreur lors de la suppression', err),
    });
  }

  // chargerNbParties() {
  //   console.log('Nombre des parties');
  //   for()
  // }
  
  deconnexion(event: Event): void {
    event.preventDefault();
    this.authService.resetAuth();
    this.router.navigate(['/login']);
  }
}