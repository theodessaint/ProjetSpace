import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { StationSpatiale } from '../../model/station-spatiale';                 // adapte le chemin
import { StationSpatialeService } from '../../service/station-spatiale-service'; // adapte le chemin

@Component({
  selector: 'app-victoire-page',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './victoire-page.html',
  styleUrl: './victoire-page.css',
})
export class VictoirePage implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private service = inject(StationSpatialeService);

  station = signal<StationSpatiale | null>(null);
  erreur = signal(false);

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    console.log('[Victoire] chargement de la station', id);

    this.service.findById(id).subscribe({
      next: (station) => {
        console.log('[Victoire] station reçue :', station);

        if (station.statut === undefined) {
          console.warn('[Victoire] Le back ne renvoie pas de champ "statut" : vérifie le nom exact du champ dans le JSON.');
        }

        if (station.statut === 'DEFAITE') {
          this.router.navigate(['/game-over', station.id]);   // mauvaise page : on va sur game over
          return;
        }
        if (station.statut === 'EN_COURS') {
          this.router.navigate(['/jeu', station.id]);         // partie pas finie : retour au jeu (adapte la route)
          return;
        }

        this.station.set(station);   // VICTOIRE (ou statut inconnu) : on affiche la fenêtre
      },
      error: (err) => {
        console.error('[Victoire] erreur de chargement', err);
        this.erreur.set(true);
      },
    });
  }
}