import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { StationSpatialeService } from '../../service/station-spatiale-service';
import { StationSpatiale } from '../../model/station-spatiale';

@Component({
  imports: [RouterLink],
  selector: 'app-victoire-page',
  styleUrl: './victoire-page.css',
  templateUrl: './victoire-page.html',
})
export class VictoirePage implements OnInit{
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private service = inject(StationSpatialeService);

  station?: StationSpatiale;
  erreur = false;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.service.findById(id).subscribe({
      next: (station) => {
        if (station.statut === 'VICTOIRE') {
          this.station = station;                 // bonne page : on affiche le résumé
        } else if (!this.service.redirigerSiTerminee(station)) {
          this.router.navigate(['/game-page', station.id]);   // en cours : retour au jeu (adapte la route)
        }                                          // sinon : redirigé vers game-over
      },
      error: () => (this.erreur = true),
    });
  }
}
