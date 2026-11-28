import {
  ApplicationConfig,
  provideBrowserGlobalErrorListeners,
  provideAppInitializer,
  inject
} from '@angular/core';

import { provideRouter } from '@angular/router';

import {
  HttpClient,
  provideHttpClient,
  withInterceptors,
  withXsrfConfiguration
} from '@angular/common/http';

import { firstValueFrom } from 'rxjs';

import { routes } from './app.routes';
import { jwtHeaderInterceptor } from './interceptor/jwt-header-interceptor';
import { apiUrlInterceptor } from './interceptor/url-interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),

    provideRouter(routes),

    provideHttpClient(
      withInterceptors([
        apiUrlInterceptor,
        jwtHeaderInterceptor
      ]),
      withXsrfConfiguration({
        cookieName: 'XSRF-TOKEN',
        headerName: 'X-XSRF-TOKEN'
      })
    ),

    // Initialisation du jeton CSRF au démarrage
    provideAppInitializer(() => {
      const http = inject(HttpClient);

      return firstValueFrom(
        http.get<void>('/csrf')
      );
    })
  ]
};
