import { Routes } from '@angular/router';
import { LoginPage } from './page/login-page/login-page';
import { LogoutPage } from './page/logout-page/logout-page';
import { InscriptionPage } from './page/inscription-page/inscription-page';
import { HomePageJoueur } from './page/home-page-joueur/home-page-joueur';
import { HomePageAdmin } from './page/home-page-admin/home-page-admin';
import { GamePage } from './page/game-page/game-page';
import { FinDePartiePage } from './page/fin-de-partie-page/fin-de-partie-page';
import { JordanPage } from './page/jordan-page/jordan-page';
import { authGuard } from './guard/auth-guard';

export const routes: Routes = [
    {
        path: 'login',
        component: LoginPage,
        title: "Connexion"
    },

    {
        path: 'inscription',
        component: InscriptionPage,
        title: "Inscription"
    },

    {
        path: 'logout',
        component: LogoutPage,
        title: "Déconnexion"
    },

    {
        path: 'home-joueur',
        title: "Accueil",
        component: HomePageJoueur,
        canActivate: [ authGuard ],
        data: { role: 'JOUEUR' }
    },

    {
        path: 'game',
        title: "Partie",
        component: GamePage,
        canActivate: [ authGuard ],
        data: { role: 'JOUEUR' }
    },

    {
        path: 'fin-de-partie',
        title: "Fin de partie",
        component: FinDePartiePage,
        canActivate: [ authGuard ],
        data: { role: 'JOUEUR' }
    },


    {
        path: 'home-admin',
        title: "Accueil administrateur",
        component: HomePageAdmin,
        canActivate: [ authGuard ],
        data: { role: 'ADMIN' }
    },

    {
        path: 'jordan',
        title: "Jordan",
        component: JordanPage
    },

    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: '**', redirectTo: 'login' }
];//ca gère les URL qui ne correspondant à aucun page