import { inject, Service } from '@angular/core';
import { StationSpatiale } from '../model/station-spatiale';
import { Observable } from 'rxjs/internal/Observable';
import { HttpClient } from '@angular/common/http';
import { Module } from '../model/module';

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
}
