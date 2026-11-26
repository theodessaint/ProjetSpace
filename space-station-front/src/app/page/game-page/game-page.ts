import { Component, OnInit, computed, inject, signal, ViewEncapsulation } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { StationSpatialeService } from '../../service/station-spatiale-service';
import { StationSpatiale } from '../../model/station-spatiale';
import { Module } from '../../model/module';
import { Astronaute } from '../../model/astronaute';


type TypeModule = 'machines' | 'serre' | 'recyclage';

type Etape = {
  titre: string;
  sousTitre: string;
  texte: string;
  liste?: string[]; // puces pop-up 3 sous le texte
  bouton?: string; // texte du bouton (par défaut « Suivant »)
  action?: string; // si présent, le joueur doit faire cette action pour continuer
  cible?: 'ressources' | 'modules' | 'centre' | 'cycle'; // élément mis en surbrillance pendant cette le tuto (félix ca va te plaire)
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

// ---------- RAPPORT DE FIN DE CYCLE ----------
type FluxRessource = { ressource: string; consommee: number; produite: number };
type RapportModule = { id: number; label: string; astronautes: string[]; flux: FluxRessource[] };
const RESSOURCES = ['Oxygène', 'Eau', 'Nourriture', 'Énergie'];
type Flux = Partial<Record<string, number>>; // ex : { 'Énergie': 5, 'Eau': 6 }

// Consommation de chaque astronaute par cycle (peu importe ou il est)
const CONSO_ASTRONAUTE: Flux = { 'Nourriture': 3, 'Eau': 3, 'Oxygène': 2 };

// consommation fixe, sans astronaute (juste spawn et commandes)
const CONSO_FIXE: Record<string, Flux> = {
  'Centre de commandement': { 'Énergie': 2 }, 
  'Dortoir': { 'Énergie': 1 },
};

// Modules de production : l'index du tableau = nombre d'astronautes dans le module
// (0 astronaute = le module ne produit et ne consomme rien)
const TABLE_MODULES: Record<TypeModule, { conso: Flux; prod: Flux }[]> = {
  serre: [
    { conso: {}, prod: {} },
    { conso: { 'Énergie': 5, 'Eau': 6 }, prod: { 'Nourriture': 8 } },
    { conso: { 'Énergie': 6, 'Eau': 7 }, prod: { 'Nourriture': 12 } }, // 2eme astronaute : +1 énergie, +1 eau, +4 nourriture (look doc)
  ],
  recyclage: [
    { conso: {}, prod: {} },
    { conso: { 'Énergie': 4 }, prod: { 'Eau': 14, 'Oxygène': 5 } },
  ],
  machines: [
    { conso: {}, prod: {} },
    { conso: {}, prod: { 'Énergie': 16 } }, // production nette (sa conso est déjà déduite)
  ],
};




const CENTRE_COMMANDE = { left: 44.2, top: 29.3, width: 12, height: 9.2 };
const EMPLACEMENTS: Record<string, Emplacement> = {
  // Recyclage : le 1 est le plus proche du centre
  '2,1': { label: 'Recyclage Eau / Air 1', type: 'recyclage', left: 29.5, top: 24, width: 10, height: 11.6 },
  '1,1': { label: 'Recyclage Eau / Air 2', type: 'recyclage', left: 18.6, top: 24, width: 10, height: 11.6 },
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
type Destination = { id: number; label: string; occupes: number; max: number | null };

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
  private router = inject(Router);

  stationId = Number(this.route.snapshot.paramMap.get('id'));

  // ---------- ETAT DE LA PARTIE (vient du back) ----------
  station = signal<StationSpatiale | null>(null); // rappel : un signal continient une valeur, quand elle change angular met à jour direct dans html
  enCours = signal(false); // une requête est en cours : on bloque les boutons
  erreur = signal('');// message d'erreur du back (ex : déblocage refusé)

  cycle = computed(() => this.station()?.cycle ?? 0);// cycle en cours, pour l'afficher dans le centre de commandement
  deblocagesDisponibles = computed(() => this.station()?.nbDeblocagesDisponibles ?? 0);// combien de modules le joueur peut encore débloquer avant le prochain palier genre 5, 10, 15…
  //?? 0 si le résultat est null ou undefined, on renvoie 0 par défaut.
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


// ---------- AFFECTATION DES ASTRONAUTES ----------

  // Le HTML ne peut pas lire les constantes du haut du fichier (CENTRE_COMMANDE),
  // on la copie dans un champ de la classe pour que le HTML puisse l'utiliser
  centre = CENTRE_COMMANDE;

  // signal = une valeur que Angular surveille. true : la pop-up est affichée, false : elle est cachée (au cas ou on sait plus ce qu'est un signal=)
  affectationOuverte = signal(false);

  // Liste des astronautes de la station, recalculée (automatique en somme) toute seule quand la station change.
  // station()?.astronautes : le ?. évite une erreur si la station n'est pas encore arrivée du back.
  // ?? [] : si on n'a rien (null), on prend une liste vide
  astronautes = computed(() => this.station()?.astronautes ?? []);

  // Cherche dans les modules celui qui s'appelle 'Dortoir' (find renvoie le premier trouvé, ou undefined)
  dortoir = computed(() => this.station()?.modules.find((m) => m.nom === 'Dortoir'));

  // Astronautes arrivés mais pas encore envoyés dans un module : ils attendent au Dortoir
  // (sert à afficher la pastille orange sur le bouton)
  nbEnAttente = computed(() => {
    const dortoirId = this.dortoir()?.id; // l'id du Dortoir (undefined si pas encore chargé)
    // filter garde les astronautes dont le module est le Dortoir, .length les compte
    return this.astronautes().filter((a) => a.moduleId === dortoirId).length;
  });

  // Liste des modules proposés dans les listes déroulantes :
  // débloqués, avec de la place (le Centre est exclu car il a 0 place)
  destinations = computed<Destination[]>(() => {
    const s = this.station();
    if (!s) return []; // pas de station reçue : aucune destination

    return s.modules
      // On garde un module s'il est débloqué ET (c'est le Dortoir OU il a au moins 1 place)
      .filter((m) => m.debloque && (m.nom === 'Dortoir' || m.nbAstroModuleMax > 0))
      // map transforme chaque module en un petit objet {id, label, occupes, max}
      .map((m) => ({
        id: m.id,
        // Nom affiché : "Dortoir", ou le label du tableau EMPLACEMENTS (ex : "Serre 1").
        // Si le label n'existe pas, on prend le nom du back (m.nom)
        label: m.nom === 'Dortoir' ? 'Dortoir' : (EMPLACEMENTS[this.cle(m)]?.label ?? m.nom),
        // Nombre d'astronautes déjà dans ce module
        occupes: this.astronautes().filter((a) => a.moduleId === m.id).length,
        max: m.nom === 'Dortoir' ? null : m.nbAstroModuleMax, // le Dortoir n'a pas de limite : null
      }))
      // Tri alphabétique par nom pour que la liste soit toujours dans le même ordre
      .sort((a, b) => a.label.localeCompare(b.label));
  });

  // Vrai si le module est plein, sauf pour l'astronaute qui s'y trouve déjà
  // (sinon il ne pourrait pas rester sélectionné dans sa propre liste)
  complet(d: Destination, a: Astronaute): boolean {
    // d.max !== null : le module a une limite (pas le Dortoir)
    // d.occupes >= d.max : il n'y a plus de place
    // a.moduleId !== d.id : l'astronaute n'est pas déjà dans ce module
    return d.max !== null && d.occupes >= d.max && a.moduleId !== d.id;
  }

  // Bouton du Centre de commandement : ouvre la pop-up d'affectation.
  // Si aucun astronaute n'est encore arrivé, on fait d'abord arriver les 3 premiers.
  ouvrirAffectation() {
    if (this.enCours()) return; // une requête est déjà en cours : on ignore le clic

    // Aucun astronaute : on demande au back de faire arriver les 3 premiers
    if (this.astronautes().length === 0) {
      this.enCours.set(true); // on bloque les boutons pendant la requête
      this.stationService.accueillirAstronaute(this.stationId).subscribe({
        next: (s) => { // succès : le back renvoie la station avec ses 3 astronautes
          this.station.set(s); // on met à jour l'écran avec la nouvelle station
          this.enCours.set(false); // on débloque les boutons
          this.affectationOuverte.set(true); // on ouvre la pop-up
          this.actionFaite('accueillir'); // on prévient le tuto : l'étape « accueillir » est faite
        },
        error: (e) => { // échec : on débloque les boutons et on affiche le message d'erreur
          this.enCours.set(false);
          this.afficherErreur(e);
        },
      });
      return; // on s'arrête là : la pop-up s'ouvrira quand le back aura répondu
    }

    // Il y a déjà des astronautes : on ouvre simplement la pop-up
    this.affectationOuverte.set(true);
    this.actionFaite('accueillir'); // utile si le tuto attend encore cette étape
  }

  // Ferme la pop-up (appelée par la croix ✕ ou par un clic sur le fond sombre)
  fermerAffectation() {
    this.affectationOuverte.set(false);
  }

  // Appelée quand le joueur choisit un autre module dans la liste déroulante d'un astronaute
  affecter(a: Astronaute, event: Event) {
    // event.target est la liste déroulante cliquée. On la convertit en HTMLSelectElement
    // pour pouvoir lire sa valeur
    const select = event.target as HTMLSelectElement;
    // select.value est un texte (« 9 »), on le transforme en nombre
    const moduleId = Number(select.value);
    // On ne fait rien si une requête est en cours ou si le joueur a choisi le module où l'astronaute est déjà
    if (this.enCours() || moduleId === a.moduleId) return;

    this.enCours.set(true); // on bloque les boutons pendant la requête
    // Envoie le PUT /astronaute/{id}/affecter au back avec le module choisi
    this.stationService.affecterAstronaute(a.id, moduleId).subscribe({
      next: () => { // le back a accepté l'affectation
        // Vrai si le joueur a envoyé l'astronaute vers le Dortoir
        const versDortoir = moduleId === this.dortoir()?.id;
        // Pour le tuto : seule une affectation hors Dortoir compte.
        // recharger() redemande la station au back, puis exécute la fonction fournie
        this.recharger(() => {
          if (!versDortoir) this.actionFaite('affecter'); // prévient le tuto que l'étape « affecter » est faite
        });
      },
      error: (e) => { // le back a refusé (module plein, pas débloqué...)
        this.enCours.set(false); // on débloque les boutons
        this.afficherErreur(e); // on affiche le message d'erreur du back
        // La liste déroulante affiche encore le mauvais choix : on la remet sur le vrai module
        select.value = String(a.moduleId ?? '');
      },
    });
  }



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





 // ---- RAPPORT DE FIN DE CYCLE ----------
  finCycleVisible = signal(false);      // pop-up du rapport affichée ou non
  cycleTermine = signal(0);             // numéro du cycle qui vient de se terminer
  ongletRapport = signal<'global' | 'modules'>('global'); // onglet affiché dans le rapport
  moduleOuvert = signal<number | null>(null); // id du module déplié dans l'onglet "Par module"
 
  icones: Record<string, string> = {
    'Oxygène': 'Assets/Images/oxygene.png',
    'Eau': 'Assets/Images/eau.png',
    'Nourriture': 'Assets/Images/nourriture.png',
    'Énergie': 'Assets/Images/energie.png',
  };
 
  // Un rapport par module débloqué, avec les astronautes qui sont dedans
  // computed = se recalcule tout seul quand station() ou astronautes() changent
  rapportModules = computed<RapportModule[]>(() => {
    const s = this.station(); // on récupère la station actuelle (venue du back)
    if (!s) return []; // pas encore de station reçue : rapport vide

    return s.modules // on part de la liste de tous les modules de la station
      .filter((m) => m.debloque) // on garde seulement les modules débloqués (les verrouillés ne comptent pas)
      .map((m) => { // pour chaque module, on fabrique une "fiche" de rapport (car on fait pa rmodule aussi)
        const emplacement = EMPLACEMENTS[this.cle(m)]; // on cherche son emplacement grâce à sa clé "x,y" (undefined pour Dortoir / Centre)
        const presents = this.astronautes().filter((a) => a.moduleId === m.id); // les astronautes qui sont dans ce module
        return { // la fiche du module :
          id: m.id, // l'id du module (sert pour le track du @for (html en gros))
          label: emplacement?.label ?? m.nom, // nom affiché : "Serre 1"… sinon le nom du back ("Dortoir", "Centre de commandement")
          astronautes: presents.map((a) => (a.role ? `${a.nom} (${a.role})` : a.nom)), // liste de textes : "Yuri (Agriculteur)" ou juste "Yuri" si pas de rôle (je trouve pas pertinent de mettre "pas de rôle")
          flux: this.calculerFlux(emplacement?.type ?? m.nom, presents.length), // ce que le module consomme/produit, selon son type et son nb d'astronautes
        };
      })
      .sort((a, b) => a.label.localeCompare(b.label)); // tri alphabétique pour avoir toujours le même ordre d'affichage (comme les modules je préfère mais facultatif)
  });

  // Rapport global = somme de tous les modules, ressource par ressource
  rapportGlobal = computed<FluxRessource[]>(() => // se recalcule tout seul quand rapportModules() change
    RESSOURCES.map((r) => { // pour chaque ressource 
      let consommee = 0; // total consommé de cette ressource, on part de 0
      let produite = 0; // total produit de cette ressource, on part de 0
      for (const m of this.rapportModules()) { // on passe sur chaque module du rapport
        const f = m.flux.find((x) => x.ressource === r); // on cherche la ligne de cette ressource dans le module
        if (f) { // si le module touche à cette ressource…
          consommee += f.consommee; // … on ajoute sa consommation au total
          produite += f.produite; // … et sa production au total
        }
      }
      return { ressource: r, consommee, produite }; // une ligne du tableau global pour cette ressource
    }),
  );

  // Calcule ce qu'un module consomme et produit pendant un cycle
  // (valeurs de base, sans les bonus de rôle pour l'instant)
  // type = 'serre' | 'recyclage' | 'machines' | 'Dortoir' | 'Centre de commandement' (voir en haut du .ts)
  private calculerFlux(type: string, nbAstro: number): FluxRessource[] {
    const total: Record<string, FluxRessource> = {}; // un "compteur" par ressource, ex : total['Eau']
    for (const r of RESSOURCES) total[r] = { ressource: r, consommee: 0, produite: 0 }; // on met tous les compteurs à 0

    // petite fonction interne : ajoute un Flux (ex : { 'Énergie': 5, 'Eau': 6 }) dans le bon compteur
    const ajouter = (flux: Flux, champ: 'consommee' | 'produite') => { // champ = on ajoute en conso ou en prod
      for (const [r, valeur] of Object.entries(flux)) total[r][champ] += valeur ?? 0; // pour chaque ressource du flux, on l'ajoute au total (0 si vide)
    };

    // pour les modules Centre, Dortoir : conso fixe
    const fixe = CONSO_FIXE[type]; // on regarde si ce type a une conso fixe 
    if (fixe) ajouter(fixe, 'consommee'); // si oui, on l'ajoute en consommation

    // Modules de production (les autres) : selon le nombre d'astronautes
    const paliers = TABLE_MODULES[type as TypeModule]; // le tableau des paliers du type 
    if (paliers) { // si c'est un module de production…
      const palier = paliers[Math.min(nbAstro, paliers.length - 1)]; // on prend la ligne qui correspond au nb d'astronautes (sans dépasser la dernière)
      ajouter(palier.conso, 'consommee'); // on ajoute ce que le module consomme
      ajouter(palier.prod, 'produite'); // on ajoute ce que le module produit
    }

    // Les astronautes présents dans le module consomment aussi
    for (let i = 0; i < nbAstro; i++) ajouter(CONSO_ASTRONAUTE, 'consommee'); // chaque astronaute : +3 nourriture, +3 eau, +2 oxygène consommés

    // on n'affiche que les ressources qui bougent
    return Object.values(total).filter((f) => f.consommee > 0 || f.produite > 0); // on transforme les compteurs en liste, sans les lignes à 0/0
  }

  // Bilan d'une ressource : positif = on en a gagné, négatif = on en a perdu
  bilan(f: FluxRessource): number {
    return f.produite - f.consommee; // produit moins consommé
  }

  // Bilan total d'un module : somme des bilans de toutes ses ressources
  bilanModule(m: RapportModule): number {
    return m.flux.reduce((total, f) => total + this.bilan(f), 0); //  additionne les bilans un par un, en partant de 0
  }

  // Clic sur un module dans l'onglet "Par module" : le déplie ou le replie
  toggleModule(id: number) {
    this.moduleOuvert.update((actuel) => (actuel === id ? null : id)); // si ce module était déjà ouvert, on le ferme (null), sinon on l'ouvre
  }
 
  continuerCycle() {
    this.finCycleVisible.set(false);
    // Nouveau palier : on ouvre le menu des modules seulement maintenant
    if (this.deblocageObligatoire()) this.menuModulesOuvert.set(true);
  }
 
  sauvegarderEtQuitter() {
    this.finCycleVisible.set(false);
    this.router.navigate(['/home-joueur']); //redirection home-joueur 
  }







  // ---------- ACTIONS DU JEU ----------

    cycleSuivant() {
    if (this.enCours() || this.cycleBloque()) return; //on ignore le clic sur cyclesuivant si une requête est deja en cours

    this.enCours.set(true);
    this.stationService.cycleSuivant(this.stationId).subscribe({
      next: (s) => {
        this.station.set(s);
        this.enCours.set(false);

        this.actionFaite('cycle'); // ferme le tuto s'il attendait cette étape

        // On ouvre le rapport du cycle qui vient de se terminer
        // (le menu des modules s'ouvrira à la fermeture du rapport, voir continuerCycle)
        this.cycleTermine.set(s.cycle);
        this.ongletRapport.set('global');
        this.moduleOuvert.set(null);
        this.finCycleVisible.set(true);
      },
      error: (e) => {
        this.enCours.set(false);
        this.afficherErreur(e);
      },
    });
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
      cible: 'ressources',
    },
    {
      // Pop-up 6
      titre: 'Votre premier module',
      sousTitre: 'Commençons par l’énergie',
      texte:
        'Sans énergie, la station s’éteint. Ouvrez « Débloquer modules » et débloquez la salle des machines : elle alimente le reste du vaisseau.',
      action: 'machines',
      cible: 'modules',
    },
    {
      // Pop-up 7
      titre: 'La serre',
      sousTitre: 'De quoi nourrir l’équipage',
      texte:
        'La serre produit la nourriture dont votre équipage a besoin. Chaque astronaute en consomme à chaque cycle : sans serre, la faim s’installe vite. Débloquez votre première serre.',
      action: 'serre',
      cible: 'modules',
    },
    {
      // Pop-up 8
      titre: 'Le recyclage eau / air',
      sousTitre: 'Respirer et boire',
      texte:
        'Le recyclage purifie l’air et récupère l’eau utilisée à bord : c’est lui qui fournit l’oxygène et l’eau de l’équipage. Débloquez votre premier module de recyclage.',
      action: 'recyclage',
      cible: 'modules',
    },
    {
      // Pop-up 9
      titre: 'Une navette arrive',
      sousTitre: 'Ils comptent sur vous',
      texte:
        'Une navette de TPT vient de s’amarrer avec vos trois premiers astronautes. Cliquez sur le Centre de commandement pour les accueillir. Chaque astronaute peut posséder un rôle qui détermine là où il sera le plus utile. Par exemple, un agriculteur dans la serre produit davantage de nourriture.',
      action: 'accueillir',
      cible: 'centre',
    },
    {
      // Pop-up 10
      titre: 'Les affectations',
      sousTitre: 'Chacun à sa place',
      texte:
        'Les astronautes arrivent au Dortoir. Depuis le Centre de commandement, envoyez-les dans vos modules débloqués : une serre accueille 2 astronautes, les autres modules un seul. Affectez au moins un astronaute pour continuer. Pensez-y au début de chaque cycle.',
      action: 'affecter',
    },
    {
      // Pop-up 11
      titre: 'Fin du cycle',
      sousTitre: 'Le temps passe',
      texte:
        'Vos choix sont faits. Cliquez sur « Cycle suivant » : les ressources seront consommées et produites selon vos modules et vos astronautes. Tous les 5 cycles, vous devrez débloquer un nouveau module. Bonne chance, commandant.',
      action: 'cycle',
      cible: 'cycle',
    },
  ];

  index = signal(0); // indique à quelle étape on est dans le tuto
  visible = signal(false); // le tuto s'affiche seulement sur une partie neuve (voir ngOnInit)
  etape = computed(() => this.etapes[this.index()]); // donne le pop-up en cours
  cycleBloque = computed(() => this.visible() && this.etape().action !== 'cycle');//cyclesuivant bloquer pdt tuto
  cible = computed(() => (this.visible() ? this.etape().cible : undefined));// element à mettre en surbrillance (seulement pendant le tuto)

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
    // Tuto réaffiché avec « ? » : on saute ces étapes si elles sont déjà faites
    if (action === 'accueillir') return this.astronautes().length > 0;
    if (action === 'affecter') {
      const dortoirId = this.dortoir()?.id;
      return this.astronautes().some((a) => a.moduleId !== null && a.moduleId !== dortoirId);
    }
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
    // bouton « Passer le tuto » : on ferme le tuto et le back débloque les 3 modules gratuits
    this.visible.set(false);
    this.stationService.debloquerTuto(this.stationId).subscribe({
      next: (s) => this.station.set(s),
      error: (e) => this.afficherErreur(e),
    });
  }

  afficherTuto() {
    this.index.set(0);
    this.visible.set(true);
  }
}