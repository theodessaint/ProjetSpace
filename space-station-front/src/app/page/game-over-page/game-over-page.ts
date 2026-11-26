import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { StationSpatialeService } from '../../service/station-spatiale-service';
import { StationSpatiale } from '../../model/station-spatiale';

@Component({
  imports: [RouterLink],
  selector: 'app-game-over-page',
  styleUrl: './game-over-page.css',
  templateUrl: './game-over-page.html',
})
export class GameOverPage implements OnInit{
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private service = inject(StationSpatialeService)

  station?: StationSpatiale;
  erreur = false;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.service.findById(id).subscribe({
      next: (station) => {
        if (station.statut === 'DEFAITE') {
          this.station = station;
        } else if (!this.service.redirigerSiTerminee(station)) {
          this.router.navigate(['/jeu', station.id]);   // adapte la route du jeu
        }                                          // sinon : redirigé vers victoire
      },
      error: () => (this.erreur = true),
    });
  }
}
