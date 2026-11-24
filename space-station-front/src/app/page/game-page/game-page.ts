import { Component, OnInit, computed, inject, signal, ViewEncapsulation } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { StationSpatialeService } from '../../service/station-spatiale-service';
import { StationSpatiale } from '../../model/station-spatiale';
import { Module } from '../../model/module';

type TypeModule = 'machines' | 'serre' | 'recyclage';

type Etape = {
  titre: string;
  sousTitre: string;
  texte: string;
  liste?: string[]; // puces pop-up 3 sous le texte
  bouton?: string; // texte du bouton (par défaut « Suivant »)
  action?: string; // si présent, le joueur doit faire cette action pour continuer
};

// ---------- AFFICHAGE DES MODULES SUR L'IMAGE ----------
// Où se trouve chaque module sur l'image du vaisseau (en % de l'image), clé = "x,y" comme dans Grille.PLAN du back
type Emplacement = {
  label: string;
  type: TypeModule;
  left: number;
  top: number;
  width: number;
  height: number;
};

const EMPLACEMENTS: Record<string, Emplacement> = {
  // Recyclage : le 1 est le plus proche du centre
  '2,1': { label: 'Recyclage Eau / Air 1', type: 'recyclage', left: 29.5, top: 24, width: 10, height: 11.6 },
  '1,1': { label: 'Recyclage Eau / Air 2', type: 'recyclage', left: 18.6, top: 24, width: 10, height: 11.6},
  '0,1': { label: 'Recyclage Eau / Air 3', type: 'recyclage', left: 7.6, top: 24, width: 10, height: 11.6 },

  // Serres
  '4,1': { label: 'Serre 1', type: 'serre', left: 60.6, top: 24, width: 10, height: 11.6 },
  '5,1': { label: 'Serre 2', type: 'serre', left: 71.7, top: 24, width: 10, height: 11.6 },
  '6,1': { label: 'Serre 3', type: 'serre', left: 82.6, top: 24, width: 10, height: 11.6 },

  // Salles des machines
  '3,2': { label: 'Salle des Machines 1', type: 'machines', left: 44, top: 50, width: 12, height: 11.6 },
  '3,3': { label: 'Salle des Machines 2', type: 'machines', left: 44, top: 62.1, width: 12, height: 11.6 },
  '3,4': { label: 'Salle des Machines 3', type: 'machines', left: 44, top: 73.7, width: 12, height: 11.6 },
};

// Les 3 modules débloqués gratuitement pendant le tuto (comme Grille.PREMIERS_MODULES dans le back)
const POSITIONS_TUTO = ['2,1', '3,2', '4,1'];

const CYCLES_PAR_DEBLOCAGE = 5;

type ModuleVue = Module & Emplacement & { tuto: boolean };

@Component({
  imports: [],
  selector: 'app-game-page',
  styleUrl: './game-page.css',
  templateUrl: './game-page.html',
  encapsulation: ViewEncapsulation.None,
})
export class GamePage implements OnInit {
  private route = inject(ActivatedRoute);
  private stationService = inject(StationSpatialeService);

  stationId = Number(this.route.snapshot.paramMap.get('id'));

  // ---------- ETAT DE LA PARTIE (vient du back) ----------
  station = signal<StationSpatiale | null>(null); // rappel : un signal continient une valeur, quand elle change angular met à jour direct dans html
  enCours = signal(false); // une requête est en cours : on bloque les boutons
  erreur = signal('');// message d'erreur du back (ex : déblocage refusé)

  cycle = computed(() => this.station()?.cycle ?? 0);// cycle en cours, pour l'afficher dans le centre de commandement
  deblocagesDisponibles = computed(() => this.station()?.nbDeblocagesDisponibles ?? 0);// combien de modules le joueur peut encore débloquer avant le prochain palier genre 5, 10, 15…
  deblocageObligatoire = computed(() => this.station()?.deblocageObligatoire ?? false);// le joueur doit débloquer un module avant de passer au cycle suivant
  prochainDeblocage = computed(
    () => (Math.floor(this.cycle() / CYCLES_PAR_DEBLOCAGE) + 1) * CYCLES_PAR_DEBLOCAGE,
  );// le cycle où le joueur devra débloquer un module (ex : 5, 10, 15…)

  // Modules encore verrouillés, avec leur emplacement sur l'image (rubans « travaux » + menu)
  modulesVerrouilles = computed<ModuleVue[]>(() => { // on ne peut pas faire de computed<ModuleVue[]> car on retourne null pour les modules sans emplacement, donc on doit filtrer après
    const s = this.station();
    if (!s) return [];// pas encore reçu du back, on ne peut rien afficher

    return s.modules
      .filter((m) => !m.debloque)// filtre les modules déjà débloqués
      .map((m) => {
        const cle = this.cle(m);
        const emplacement = EMPLACEMENTS[cle];
        return emplacement ? { ...m, ...emplacement, tuto: POSITIONS_TUTO.includes(cle) } : null;
      }) //fabrique la clé m --> genre x y par ex 2,1 va chercher dans la table la position du ruban pour cette clé
      //{} en gros ca fusionne dans un seul objet les données du back celles de la table et un booléen tuto. 
      //POSITIONS_TUTO.includes(cle) vaut TRUE si c emodule est l'un des 3 du tutos

//le back m'envoie m = { id: 12, nom: 'Serre', debloque: false, positionX: 4, positionY: 1, stationId: 8, ... }
//et le front emplacement = { label: 'Serre 1', type: 'serre', left: 60.3, top: 17.6, width: 10.9, height: 19.9 }
//on les fusionne en un seul objet car sinon le back ne sait pas ou dessiner le ruban

      .filter((m): m is ModuleVue => m !== null) //on retire de snull sinon la partie m is modelvue croirait qu'il peut rester des null
      .sort((a, b) => a.label.localeCompare(b.label));
  });// tri alphabétique pour que les modules du tuto apparaissent en premier dans le menu (pas utile mais vas y )

  ngOnInit(): void {
    this.stationService.findById(this.stationId).subscribe({ 
      next: (s) => {
        this.station.set(s);//

        // Le tuto ne se lance que sur une partie toute nouvelle (neuve)(cycle 0 et aucun module du tuto débloqué)
        const neuve = s.cycle === 0 && !this.modulesTutoDebloques(s); //
        this.visible.set(neuve); //s=si on rafraichit la page, le tuto ne se relance pas
      },
      error: (e) => this.afficherErreur(e),
    });
  }

  private cle(m: Module): string {
    return `${m.positionX},${m.positionY}`;
  }//fabrique la clé texte à partir de la position. Pour x=2 et y=1, ça donne "2,1"

  private modulesTutoDebloques(s: StationSpatiale): boolean {
    return s.modules.some((m) => m.debloque && POSITIONS_TUTO.includes(this.cle(m)));
  }//some répond true si au moins un module vérifie la condition. Ici : un module débloqué dont la position est dans la liste du tuto.

  // Même règle que le back : il faut un module voisin déjà débloqué (ce qui impose l'ordre serre 1 → 2 → 3)
  private aVoisinDebloque(m: Module): boolean {
    const s = this.station();
    if (!s) return false;

    return s.modules.some(
      (autre) =>
        autre.debloque &&
        Math.abs(autre.positionX - m.positionX) + Math.abs(autre.positionY - m.positionY) === 1,
    );// ca c'est chat de a à z j'ai pas capté mais askip Deux modules sont voisins quand la distance entre eux vaut exactement 1 case.
   // Math.abs prend la valeur absolue, pour ignorer le signe.
   //On additionne l'écart en X et l'écart en Y.
  //Serre 2 en (5,1) et Serre 1 en (4,1) : 1 + 0 = 1, donc voisines.
  //Serre 3 en (6,1) et Serre 1 en (4,1) : 2 + 0 = 2, donc pas voisines.
  }

  peutDebloquer(m: ModuleVue): boolean {
    if (this.enCours() || !this.aVoisinDebloque(m)) return false;//peut pas si requête deja en cours ou si module voisin pas débloqué

    // Pendant une étape du tuto qui demande un module, seul celui-là est possible
    const action = this.visible() ? this.etape().action : undefined; //Si le tuto est affiché, on regarde l'action que l'étape demande. Sinon il n'y a pas d'action.
    if (action === 'machines' || action === 'serre' || action === 'recyclage') {
      return m.tuto && m.type === action;
    }

    if (m.tuto) return true; // modules du tuto : gratuits, sans condition de cycle
    //en gros à l'étape des machines dans le tuto, on ne peut pas débloquer une serre 
    return this.deblocagesDisponibles() > 0;
  }

  debloquerModule(m: ModuleVue) {
    if (!this.peutDebloquer(m)) return; //si on peut on peut lol

    this.enCours.set(true);
    this.stationService.debloquerModule(m.id).subscribe({ //nvoie le PUT /module/{id}/debloquer au back
      next: () => {
        this.menuModulesOuvert.set(false);
        // On recharge l'état depuis le back, puis on prévient le tuto
        this.recharger(() => this.actionFaite(m.type));//en cas de succès --> ferme le menu, recharge station d epuis le back, le callback () => this.actionFaite(m.type) 
        // s'exécute ensuite et prévient le tuto qu'un module de ce type vient d'être débloqué, pour qu'il passe à l'étape suivante.
      },
      error: (e) => {
        this.enCours.set(false);
        this.afficherErreur(e);// en cas d'erreur --> les boutons sont débloquer (encours à false en gros) et on affiche le message d'erreur
      },
    });// en cas de succès on ferme le succès puis on recharge la station depuis le back 
  }

  private recharger(apres?: () => void) {
    this.stationService.findById(this.stationId).subscribe({
      next: (s) => {
        this.station.set(s);
        this.enCours.set(false);
        apres?.();
      },
      error: (e) => {
        this.enCours.set(false);
        this.afficherErreur(e);
      },//après est utile car il faut d'abord recharger la station puis prevenir le tuto (ne me demandé pas pq ca maeche que comme ca)
    });
  }// quand on debloque un module permet d erechargez la page pour que le ruban ne soit plus la 

  private afficherErreur(e: HttpErrorResponse) {
    // Le back renvoie un message seulement si server.error.include-message=always est activé
    this.erreur.set(e.error?.message || `Action impossible (erreur ${e.status})`);
    setTimeout(() => this.erreur.set(''), 4000);
  }//en gros pour chaque erreur débloqué, on renvoie la réponse de l'erreur programmé dans le back ex : « Aucun déblocage disponible… »

  // ---------- ACTIONS DU JEU ----------

  accueillirAstronaute() {
    // TODO : appel au back quand les astronautes seront faits (A FAIRE)
    this.actionFaite('accueillir');
  }

  cycleSuivant() {
    if (this.enCours()) return; //on ignore le clic sur cyclesuivant si une requête est deja en cours

    this.enCours.set(true);
    this.stationService.cycleSuivant(this.stationId).subscribe({
      next: (s) => {
        this.station.set(s);
        this.enCours.set(false);

        // Nouveau palier : on ouvre le menu pour que le joueur choisisse son module
        if (s.deblocageObligatoire) this.menuModulesOuvert.set(true);

        this.actionFaite('cycle');
      },
      error: (e) => {
        this.enCours.set(false);
        this.afficherErreur(e);
      },
    }); //Le POST /station/{id}/cycle-suivant fait exécuter le cycle côté back.
    //si le nv cycle est un palier on a deblocageObligatoire qui vaut true --> ouverture de menu auto
    //de plus actionFaite fait vancer le tuto s'il attendait l'action qd on n'en demande une 
  }

  // ---------- MENU « ACHETER MODULES » ----------
  menuModulesOuvert = signal(false);

  toggleMenuModules() {
    this.menuModulesOuvert.update((ouvert) => !ouvert);
  }

  // ---------- TUTO (le lore + cycle 1 (tuto joueur)) ----------
  // Pop-ups 1 à 4 : le lore, puis pop-ups 5 à 11 : le tuto du cycle 1
  etapes: Etape[] = [
    {
      // Pop-up 1
      titre: 'L’appel',
      sousTitre: 'Signal de détresse reçu',
      texte:
        'An 2187. La planète TPT, devenue inhabitable, est en train de s’éteindre. Ses habitants ont lancé un dernier appel avant que les communications ne soient coupées.',
    },
    {
      // Pop-up 2
      titre: 'La station',
      sousTitre: 'Bienvenue à bord',
      texte:
        'Vous voilà aux commandes de la station Aurore, un vaisseau à l’abandon, mais encore debout. Il est devenu le dernier refuge pour les astronautes qui fuient TPT. Leurs navettes arrivent déjà, et la station est presque vide.',
    },
    {
      // Pop-up 3
      titre: 'La mission',
      sousTitre: 'Votre mission, commandant',
      texte:
        'Chaque astronaute accueilli doit trouver sa place à bord. Pour la prospérité du vaisseau, vous allez devoir :',
      liste: [
        'construire des modules : salle des machines, serre…',
        'gérer vos ressources : énergie, eau, oxygène, nourriture ;',
        'accueillir les astronautes et les affecter là où on a besoin d’eux.',
      ],
    },
    {
      // Pop-up 4
      titre: 'Le départ',
      sousTitre: 'Le temps presse',
      texte:
        'Les ressources sont limitées et d’autres astronautes arrivent. Chaque décision compte : une ressource qui tombe à zéro, et c’est tout l’équipage qui est en danger. Saurez-vous les maintenir en vie jusqu’à ce qu’une nouvelle terre se présente ?',
      bouton: 'Prendre le commandement',
    },
    {
      // Pop-up 5
      titre: 'Le tableau de bord',
      sousTitre: 'Quatre ressources, une seule règle',
      texte:
        'Vos quatre ressources sont affichées en bas à gauche de l’écran : énergie, eau, oxygène et nourriture. Elles diminuent à chaque cycle, car l’équipage consomme. Gardez-les au-dessus de zéro et évitez d’avoir plus de trois seuils critiques détectés, sinon c’est la fin de la mission.',
    },
    {
      // Pop-up 6
      titre: 'Votre premier module',
      sousTitre: 'Commençons par l’énergie',
      texte:
        'Sans énergie, la station s’éteint. Ouvrez « Débloquer modules » et débloquez la salle des machines : elle alimente le reste du vaisseau.',
      action: 'machines',
    },
    {
      // Pop-up 7
      titre: 'La serre',
      sousTitre: 'De quoi nourrir l’équipage',
      texte:
        'La serre produit la nourriture dont votre équipage a besoin. Chaque astronaute en consomme à chaque cycle : sans serre, la faim s’installe vite. Débloquez votre première serre.',
      action: 'serre',
    },
    {
      // Pop-up 8
      titre: 'Le recyclage eau / air',
      sousTitre: 'Respirer et boire',
      texte:
        'Le recyclage purifie l’air et récupère l’eau utilisée à bord : c’est lui qui fournit l’oxygène et l’eau de l’équipage. Débloquez votre premier module de recyclage.',
      action: 'recyclage',
    },
    {
      // Pop-up 9
      titre: 'Une navette arrive',
      sousTitre: 'Ils comptent sur vous',
      texte:
        'Une navette de TPT vient de s’amarrer. Accueillez votre premier astronaute. Chaque astronaute peut posséder un rôle qui détermine là où il sera le plus utile. Par exemple, un agriculteur dans la serre produit davantage de nourriture.',
      action: 'accueillir',
    },
    {
      // Pop-up 10
      titre: 'Les alertes',
      sousTitre: 'Ne laissez pas tomber une jauge',
      texte:
        'Quand une ressource passe sous 30 %, une alerte de danger s’affiche. Sous 10 %, elle devient critique. Anticipez en débloquant des modules et en affectant vos astronautes avant d’en arriver là.',
    },
    {
      // Pop-up 11
      titre: 'Fin du cycle',
      sousTitre: 'Le temps passe',
      texte:
        'Vos choix sont faits. Cliquez sur « Cycle suivant » : les ressources seront consommées et produites selon vos modules et vos astronautes. Tous les 5 cycles, vous devrez débloquer un nouveau module. Bonne chance, commandant.',
      action: 'cycle',
    },
  ];

  index = signal(0); // indique à quelle étape on est dans le tuto
  visible = signal(false); // le tuto s'affiche seulement sur une partie neuve (voir ngOnInit)
  etape = computed(() => this.etapes[this.index()]); // donne le pop-up en cours

  suivant() {
    this.avancer();
    this.sauterEtapesFaites();
  }

  private avancer() {
    if (this.index() < this.etapes.length - 1) {
      this.index.update((i) => i + 1);
    } else {
      this.visible.set(false); // dernier pop-up : le tuto se ferme
    }
  }

  // Si le joueur a déjà débloqué le module demandé (tuto réaffiché avec « ? »), on passe l'étape
  private sauterEtapesFaites() {
    while (this.visible()) {
      const action = this.etape().action;
      if (action && this.actionDejaAccomplie(action)) {
        this.avancer();
      } else {
        break;
      }
    }
  }

  private actionDejaAccomplie(action: string): boolean {
    if (action !== 'machines' && action !== 'serre' && action !== 'recyclage') return false;

    const s = this.station();
    if (!s) return false;

    return s.modules.some((m) => {
      const cle = this.cle(m);
      return m.debloque && POSITIONS_TUTO.includes(cle) && EMPLACEMENTS[cle].type === action;
    });
  }

  // Fait avancer le tuto si le joueur vient de faire l'action attendue. Sinon, ne fait rien.
  actionFaite(nom: string) {
    if (this.visible() && this.etape().action === nom) {
      this.suivant();
    }
  }

  passer() {
    // bouton « Passer le tuto »
    this.visible.set(false);
  }

  afficherTuto() {
    this.index.set(0);
    this.visible.set(true);
  }
}