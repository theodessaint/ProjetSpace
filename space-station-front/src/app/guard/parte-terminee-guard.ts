import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { map, catchError, of } from 'rxjs';
import { StatutPartieEnum } from '../model/statut-partie';
import { StationSpatialeService } from '../service/station-spatiale-service';

export const parteTermineeGuard: CanActivateFn = (route, state) => {

  const stationService = inject(StationSpatialeService);
  const router = inject(Router);

  const id = Number(route.paramMap.get('id'));

  if (!Number.isInteger(id) || id <= 0) {
    return router.createUrlTree(['/home-joueur']);
  }

  return stationService.findById(id).pipe(
    map(
      station => station.statut === StatutPartieEnum.EN_COURS
      ? true
      : router.createUrlTree(['/home-joueur'])
    ),
    catchError(
      () => of(router.createUrlTree(['/home-joueur']))
    )
  );
};
