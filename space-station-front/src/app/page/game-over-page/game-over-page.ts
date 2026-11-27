import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { StationSpatiale } from '../../model/station-spatiale';
import { StationSpatialeService } from '../../service/station-spatiale-service'; 

@Component({
  selector: 'app-game-over-page',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './game-over-page.html',
  styleUrl: './game-over-page.css',
})
export class GameOverPage implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private service = inject(StationSpatialeService);

  station = signal<StationSpatiale | null>(null);
  erreur = signal(false);

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    console.log('[GameOver] chargement de la station', id);

    this.service.findById(id).subscribe({
      next: (station) => {
        console.log('[GameOver] station reçue :', station);

        if (station.statut === undefined) {
          console.warn('[GameOver] Le back ne renvoie pas de champ "statut" : vérifie le nom exact du champ dans le JSON.');
        }

        if (station.statut === 'VICTOIRE') {
          this.router.navigate(['/victoire', station.id]);   // mauvaise page : on va sur victoire
          return;
        }
        if (station.statut === 'EN_COURS') {
          this.router.navigate(['/jeu', station.id]);         // partie pas finie : retour au jeu (adapte la route)
          return;
        }

        this.station.set(station);   // DEFAITE (ou statut inconnu) : on affiche la fenêtre
      },
      error: (err) => {
        console.error('[GameOver] erreur de chargement', err);
        this.erreur.set(true);
      },
    });
  }
}