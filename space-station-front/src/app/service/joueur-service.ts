import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';

@Service()
export class JoueurService {

    private http = inject(HttpClient);
    private url = '/station';

    
}
