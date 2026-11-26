import { Routes } from '@angular/router';
import { LoginPage } from './page/login-page/login-page';
import { LogoutPage } from './page/logout-page/logout-page';
import { InscriptionPage } from './page/inscription-page/inscription-page';
import { HomePageJoueur } from './page/home-page-joueur/home-page-joueur';
import { HomePageAdmin } from './page/home-page-admin/home-page-admin';
import { GamePage } from './page/game-page/game-page';
import { JordanPage } from './page/jordan-page/jordan-page';
import { authGuard } from './guard/auth-guard';
import { GameOverPage } from './page/game-over-page/game-over-page';
import { VictoirePage } from './page/victoire-page/victoire-page';

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
        path: 'game/:id',
        title: "Partie",
        component: GamePage,
        canActivate: [ authGuard ],
        data: { role: 'JOUEUR' }
    },

    {
        path: 'victoire/:id',
        title: "Victoire",
        component: VictoirePage,
        canActivate: [ authGuard ],
        data: { role: 'JOUEUR' }
    },

    {
        path: 'game-over/:id',
        title: "Game over",
        component: GameOverPage,
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