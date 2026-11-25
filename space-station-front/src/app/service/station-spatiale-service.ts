import { inject, Service } from '@angular/core';
import { StationSpatiale } from '../model/station-spatiale';
import { Observable } from 'rxjs/internal/Observable';
import { HttpClient } from '@angular/common/http';
import { Module } from '../model/module';
import { Astronaute } from '../model/astronaute';
@Service()
export class StationSpatialeService {
  private http = inject(HttpClient);

  findById(id: number): Observable<StationSpatiale> {
    return this.http.get<StationSpatiale>(`/station/${id}`);
  }

  debloquerModule(moduleId: number): Observable<Module> {
    return this.http.put<Module>(`/module/${moduleId}/debloquer`, {});
  }

  cycleSuivant(id: number): Observable<StationSpatiale> {
    return this.http.post<StationSpatiale>(`/station/${id}/cycle-suivant`, {});
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
}
