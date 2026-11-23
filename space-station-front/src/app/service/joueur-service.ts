import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Observable } from 'rxjs';
import { StationSpatialeResume } from '../model/station-spatiale-resume';
import { StationSpatiale } from '../model/station-spatiale';

@Service()
export class JoueurService {

    private http = inject(HttpClient);
    private url = '/station';

    creerPartie(nom: string): Observable<{id:number}> {
        return this.http.post<{id:number}>(this.url, {nom, cycle:0});
    }

    findMesParties(): Observable<StationSpatialeResume[]> {
        return this.http.get<StationSpatialeResume[]>(`${this.url}/mes-parties`);
    }

    findById(id: number): Observable<StationSpatiale> {
        return this.http.get<StationSpatiale>(`${this.url}/${id}`)
    }
}
