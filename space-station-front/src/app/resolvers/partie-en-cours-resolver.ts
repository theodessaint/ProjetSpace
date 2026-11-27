import { inject } from '@angular/core';
import { ResolveFn, Router, RedirectCommand } from '@angular/router';
import { catchError, map, of } from 'rxjs';

import { StationSpatialeService } from '../service/station-spatiale-service';
import { StationSpatiale } from '../model/station-spatiale';import { StatutPartieEnum } from '../model/statut-partie';
;

export const partieEnCoursResolver: ResolveFn<StationSpatiale> = (route) => {

  const service = inject(StationSpatialeService);
  const router = inject(Router);

  const id = Number(route.paramMap.get('id'));

  const redirection = new RedirectCommand(
    router.createUrlTree(['/home-joueur'])
  );

  if (!Number.isInteger(id) || id <= 0) {
    return redirection;
  }

  return service.findById(id).pipe(
    map(station =>
      station.statut === StatutPartieEnum.EN_COURS
        ? station
        : redirection
    ),
    catchError(() => of(redirection))
  );
};
