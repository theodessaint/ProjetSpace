import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';
import { Astronaute } from '../model/astronaute';
import { BonusResponse } from '../model/bonus';
import { Module } from '../model/module';
import { StationSpatiale } from '../model/station-spatiale';
import { RessourceEnum } from '../model/ressource';

@Injectable({
  providedIn: 'root',
})
export class StationSpatialeService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private apiUrl = '/station';

  findById(id: number): Observable<StationSpatiale> {
    return this.http.get<StationSpatiale>(`/station/${id}`);
  }

  debloquerModule(moduleId: number): Observable<Module> {
    return this.http.put<Module>(`/module/${moduleId}/debloquer`, {});
  }

  cycleSuivant(id: number, stats: Record<RessourceEnum, number>): Observable<StationSpatiale> {

    return this.http.post<StationSpatiale>(
      `/station/${id}/cycle-suivant`,
      { stats }
    );
  }

  findAllBonus() {
    return this.http.get<BonusResponse[]>('/bonus');
  }

  debloquerTuto(id: number): Observable<StationSpatiale> {
    return this.http.post<StationSpatiale>(`/station/${id}/debloquer-tuto`, {});
  }
  affecterAstronaute(astronauteId: number, moduleId: number): Observable<Astronaute> {
    return this.http.put<Astronaute>(`/astronaute/${astronauteId}/affecter`, { moduleId });
  }
  accueillirAstronaute(id: number): Observable<StationSpatiale> {
    return this.http.post<StationSpatiale>(`/station/${id}/accueillir-astronaute`, {});
  }

  getRouteFin(station: StationSpatiale): (string | number)[] | null {
    switch (station.statut) {
      case 'VICTOIRE':
      return ['/victoire', station.id];
      case 'DEFAITE':
      return ['/game-over', station.id];
      default:
      return null;
    }
  }

  // redirige vers la page de fin si la partie est terminée ; renvoie true dans ce cas
  redirigerSiTerminee(station: StationSpatiale): boolean {
    const route = this.getRouteFin(station);
    if (route) {
      this.router.navigate(route);
      return true;
    }
    return false;
  }
}
