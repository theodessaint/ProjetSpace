import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { StationSpatiale } from '../model/station-spatiale';

@Injectable({
  providedIn: 'root',
})
export class AdminService {

    private http = inject(HttpClient);
    private url = '/station';

    findAll(): Observable<StationSpatiale[]> {
        return this.http.get<StationSpatiale[]>(this.url);
    }

    nombreParties(): Observable<number> {
        return this.findAll().pipe(
            map(parties => parties.length)
        );
    }

    modifier(station: StationSpatiale): Observable<string> {
        return this.http.put(`${this.url}/${station.id}`, station, { responseType: 'text' });
    }

    supprimer(id: number): Observable<string> {
        return this.http.delete(`${this.url}/${id}`, { responseType: 'text' });
    }

    // afficherJoueur(id:number): Observable<void> {
    //     return this.http.put<StationSpatiale>((`${this.url}/${station.id}`, station);
    // }

    //DeleteJoueur():

    // // Récupérer les parties terminées
    // partiesTerminees(): Observable<StationSpatialeResume[]> {
    //     return this.findMesParties().pipe(
    //         map(parties => parties.filter(partie => partie.terminee))
    //     );
    // }

    // // Récupérer les parties en cours
    // partiesEnCours(): Observable<StationSpatialeResume[]> {
    //     return this.findMesParties().pipe(
    //         map(parties => parties.filter(partie => !partie.terminee))
    //     );
    // }
}
