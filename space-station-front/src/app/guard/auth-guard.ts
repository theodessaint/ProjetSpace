import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../service/auth-service';

export const authGuard: CanActivateFn = (route) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  // pas connecté on va retourner au login ici
  if (!auth.isLogged()) {
    return router.createUrlTree(['/login']);
  }
  // On demande un rôle précis et ce n'est pas le bon : retour à sa propre home
  const roleAttendu = route.data['role'];
  if (roleAttendu && auth.role !== roleAttendu) {
    return router.createUrlTree([auth.isAdmin() ? '/home-admin' : '/home-joueur']);
  }

  return true;
};