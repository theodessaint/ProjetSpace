import { HttpErrorResponse } from '@angular/common/http';
import { Component, computed, inject, OnInit, signal, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Astronaute } from '../../model/astronaute';
import { Module } from '../../model/module';
import { RessourceEnum } from '../../model/ressource';
import { StationSpatiale } from '../../model/station-spatiale';
import { StationSpatialeService } from '../../service/station-spatiale-service';
import { DecimalPipe } from '@angular/common';
import { StatutPartieEnum } from '../../model/statut-partie';


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
type RapportModule = { id: number; label: string; actif: boolean, astronautes: string[]; flux: FluxRessource[]; bonus: string[] };
const RESSOURCES = ['Oxygène', 'Eau', 'Nourriture', 'Énergie'];
type Flux = Partial<Record<string, number>>; // ex : { 'Énergie': 5, 'Eau': 6 }
const SEUILS_RESSOURCES = {
  danger: 55,
  critique: 45
} as const;

// Un bonus de rôle (reçu du back via GET /bonus, puis traduit pour le front)
type Bonus = { role: string; module: TypeModule; ressource: string; multiplicateur: number };

// Consommation de chaque astronaute par cycle (peu importe où il est)
const CONSO_ASTRONAUTE: Flux = { 'Nourriture': 3, 'Eau': 3, 'Oxygène': 2 };

// Consommation fixe, sans astronaute (clés = nom du module dans le back)
const CONSO_FIXE: Record<string, Flux> = {
  'CentreDeCommandement': { 'Énergie': 2 },
  'Dortoir': { 'Énergie': 1 },
};

// Noms affichés pour les modules qui n'ont pas d'emplacement sur l'image
const LABELS_SPECIAUX: Record<string, string> = {
  CentreDeCommandement: 'Centre de commandement',
  Dortoir: 'Dortoir',
};

// Met un nom d'enum sous une forme unique : 'SALLE_DES_MACHINES' et 'SalleDesMachines' → 'salledesmachines'
const normaliser = (texte: string) => texte.toLowerCase().replace(/[^a-z]/g, '');

// Module du back (normalisé) → type du front
const MODULES_BACK: Record<string, TypeModule> = {
  serre: 'serre',
  salledesmachines: 'machines',
  recyclageeauetoxygene: 'recyclage',
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
const CYCLES_PAR_ARRIVEE_ASTRONAUTE = 4;

type ModuleVue = Module & Emplacement & { tuto: boolean };
type Destination = { id: number; label: string; occupes: number; max: number | null };

@Component({
  imports: [DecimalPipe],
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

  protected readonly partiePerdue = computed(
    () => this.station()?.statut === StatutPartieEnum.DEFAITE
  );

  protected readonly partieGagnee = computed(
    () => this.station()?.statut === StatutPartieEnum.VICTOIRE
  );

  protected readonly partieTerminee = computed(
    () => this.partiePerdue() || this.partieGagnee()
  );

  protected stats_oxygene = computed(
    () => this.station()?.stats.find(
      r => r.type === RessourceEnum.OXYGENE
    )
  );

  protected stats_eau = computed(
    () => this.station()?.stats.find(
      r => r.type === RessourceEnum.EAU
    )
  );

  protected stats_nourriture = computed(
    () => this.station()?.stats.find(
      r => r.type === RessourceEnum.NOURRITURE
    )
  );

  protected stats_energie = computed(
    () => this.station()?.stats.find(
      r => r.type === RessourceEnum.ENERGIE
    )
  );

  protected readonly SEUILS_RESSOURCES = SEUILS_RESSOURCES;

  protected readonly COUT_DEBLOCAGE_ENERGIE = 20;
  protected readonly energieSuffisante = computed(() =>
    (this.stats_energie()?.quantite ?? 0)
  >= this.COUT_DEBLOCAGE_ENERGIE
);

cycle = computed(() => this.station()?.cycle ?? 0);// cycle en cours, pour l'afficher dans le centre de commandement
deblocagesDisponibles = computed(() => this.station()?.nbDeblocagesDisponibles ?? 0);// combien de modules le joueur peut encore débloquer avant le prochain palier genre 5, 10, 15…
//?? 0 si le résultat est null ou undefined, on renvoie 0 par défaut.
deblocageObligatoire = computed(() => this.station()?.deblocageObligatoire ?? false);// le joueur doit débloquer un module avant de passer au cycle suivant
prochainDeblocage = computed(
  () => (Math.floor(this.cycle() / CYCLES_PAR_DEBLOCAGE) + 1) * CYCLES_PAR_DEBLOCAGE,
);// le cycle où le joueur devra débloquer un module (ex : 5, 10, 15…)
// Nombre de cycles restants avant le prochain déblocage de module
cyclesAvantDeblocage = computed(() => this.prochainDeblocage() - this.cycle());

// Le cycle où le prochain astronaute arrivera (même calcul que prochainDeblocage)
prochainAstronaute = computed(
  () => (Math.floor(this.cycle() / CYCLES_PAR_ARRIVEE_ASTRONAUTE) + 1) * CYCLES_PAR_ARRIVEE_ASTRONAUTE,
);

// Nombre de cycles restants avant l'arrivée d'un astronaute
cyclesAvantAstronaute = computed(() => this.prochainAstronaute() - this.cycle());
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
  const s = this.route.snapshot.data['station'] as StationSpatiale;

  this.stationId = s.id;
  this.station.set(s);

  // Le tuto ne se lance que sur une partie toute nouvelle (neuve)(cycle 0 et aucun module du tuto débloqué)
  const neuve = s.cycle === 0 && !this.modulesTutoDebloques(s);
  this.visible.set(neuve); //si on rafraichit la page, le tuto ne se relance pas

  // On charge les bonus du back une seule fois, et on traduit les noms pour le front
  this.stationService.findAllBonus().subscribe({
    next: (liste) =>
      this.bonus.set(
      liste.map((b) => ({
        role: b.role, // comparé en minuscules dans meilleurBonus
        module: MODULES_BACK[normaliser(b.module)], // 'SERRE' / 'Serre' → 'serre'
        ressource: b.ressource, // déjà "Eau", "Oxygène"… (libellé envoyé par le back)
        multiplicateur: b.multiplicateur,
      })),
    ),
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

  if (!this.energieSuffisante()) return false;

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



// ? "continue suelement si ca existe"
// ?? "sinon"

// ---- RAPPORT DE FIN DE CYCLE ----------
finCycleVisible = signal(false);      // pop-up du rapport affichée ou non
cycleTermine = signal(0);             // numéro du cycle qui vient de se terminer
ongletRapport = signal<'global' | 'modules'>('global'); // onglet affiché dans le rapport
moduleOuvert = signal<number | null>(null); // id du module déplié dans l'onglet "Par module"
bonus = signal<Bonus[]>([]);          // bonus de rôle reçus du back (GET /bonus)
alertesDanger = signal<string[]>([]);
alertesCritique = signal<string[]>([]);
alertesEpuisement = signal<string[]>([]);

defaite = computed(
  () => this.alertesEpuisement().length > 0 ||
  this.alertesCritique().length >= 3
);

icones: Record<string, string> = {
  'Oxygène': 'Assets/Images/oxygene.png',
  'Eau': 'Assets/Images/eau.png',
  'Nourriture': 'Assets/Images/nourriture.png',
  'Énergie': 'Assets/Images/energie.png',
};

private moduleActif(type: string, presents: Astronaute[]): boolean {
  return presents.length > 0 ||
  type === 'Dortoir' ||
  type === 'CentreDeCommandement';
}

nbModulesActifs = computed(() =>
  this.rapportModules().filter(m => m.actif).length
);

// Un rapport par module débloqué, avec les astronautes qui sont dedans
// computed = se recalcule tout seul quand station(), astronautes() ou bonus() changent
rapportModules = computed<RapportModule[]>(() => {
  const s = this.station(); // on récupère la station actuelle (venue du back)
  if (!s) return []; // pas encore de station reçue : rapport vide

  return s.modules
  .filter((m) => m.debloque) // on garde seulement les modules débloqués
  .map((m) => {
    const emplacement = EMPLACEMENTS[this.cle(m)]; // undefined pour Dortoir / Centre
    const presents = this.astronautes().filter((a) => a.moduleId === m.id); // les astronautes de ce module
    const calcul = this.calculerFlux(emplacement?.type ?? m.nom, presents); // conso/prod + bonus appliqués
    return {
      id: m.id, // sert pour le track du @for
      label: emplacement?.label ?? LABELS_SPECIAUX[m.nom] ?? m.nom, // "Serre 1"… sinon "Centre de commandement" / "Dortoir"
      actif: this.moduleActif(emplacement?.type ?? m.nom, presents),
      astronautes: presents.map((a) => (a.role ? `${a.nom} (${a.role})` : a.nom)), // "Yuri (Agriculteur)" ou "Yuri"
      flux: calcul.flux, // ce que le module consomme/produit, bonus compris, si astronautes présents
      bonus: calcul.bonus, // les bonus appliqués, pour les afficher
    };
  })
  .sort((a, b) => a.label.localeCompare(b.label)); // tri alphabétique
});

// Rapport global = somme de tous les modules, ressource par ressource
rapportGlobal = computed<FluxRessource[]>(() =>
  RESSOURCES.map((r) => { // pour chaque ressource
  let consommee = 0;
  let produite = 0;
  for (const m of this.rapportModules()) { // on passe sur chaque module du rapport
    const f = m.flux.find((x) => x.ressource === r); // la ligne de cette ressource dans le module
    if (f) {
      consommee += f.consommee;
      produite += f.produite;
    }
  }
  return { ressource: r, consommee: Math.round(consommee * 100) / 100, produite: Math.round(produite * 100) / 100 };
}),
);

// Calcule ce qu'un module consomme et produit pendant un cycle, bonus de rôle compris
// type = 'serre' | 'recyclage' | 'machines' | 'Dortoir' | 'CentreDeCommandement'
private calculerFlux(type: string, presents: Astronaute[]): { flux: FluxRessource[]; bonus: string[] } {

  // Définit la conso et la production des modules sans astronaute hors Dortoir et Centre de commandement
  if (!this.moduleActif(type, presents)) {
    return {
      flux: RESSOURCES.map(r => ({
        ressource: r,
        consommee: 0,
        produite: 0
      })),
      bonus: []
    };
  }

  const nbAstro = presents.length; // nombre d'astronautes dans le module
  const roles = presents // rôles présents, en minuscules pour comparer sans souci de majuscules
  .map((a) => a.role?.toLowerCase())
  .filter((r): r is string => !!r); // on enlève les astronautes sans rôle
  const bonusAppliques: string[] = []; // textes des bonus utilisés, pour l'affichage

  const total: Record<string, FluxRessource> = {}; // un compteur par ressource
  for (const r of RESSOURCES) total[r] = { ressource: r, consommee: 0, produite: 0 };

  // ajoute un Flux dans les compteurs ; si sens est donné, on applique les bonus du module
  const ajouter = (flux: Flux, champ: 'consommee' | 'produite', sens?: 'conso' | 'prod') => {
    for (const [r, valeur] of Object.entries(flux)) {
      let v = valeur ?? 0;
      if (sens) {
        const b = this.meilleurBonus(type, r, roles, sens); // le bonus le plus avantageux (ou undefined)
        if (b) {
          v = Math.round(v * b.multiplicateur * 100) / 100; // valeur avec bonus, arrondie à 2 décimales
          const pourcent = Math.round((b.multiplicateur - 1) * 100); // 1.15 → +15 ; 0.80 → -20
          bonusAppliques.push(`${b.role} : ${r} ${pourcent > 0 ? '+' : ''}${pourcent} %`);
        }
      }
      total[r][champ] += v;
    }
  };

  // Centre, Dortoir : conso fixe (pas de bonus)
  const fixe = CONSO_FIXE[type];
  if (fixe) ajouter(fixe, 'consommee');

  // Modules de production : palier selon le nb d'astronautes, avec bonus
  const paliers = TABLE_MODULES[type as TypeModule];
  if (paliers) {
    const palier = paliers[Math.min(nbAstro, paliers.length - 1)];
    ajouter(palier.conso, 'consommee', 'conso'); // bonus < 1 : moins de conso
    ajouter(palier.prod, 'produite', 'prod');    // bonus > 1 : plus de prod
  }

  // Conso personnelle des astronautes (pas de bonus)
  for (let i = 0; i < nbAstro; i++) ajouter(CONSO_ASTRONAUTE, 'consommee');

  return {
    flux: Object.values(total).filter((f) => f.consommee > 0 || f.produite > 0), // sans les lignes à 0/0
    bonus: bonusAppliques,
  };
}

// Parmi les bonus possibles pour ce module et cette ressource, renvoie le plus avantageux
// (pas de cumul : un seul bonus par ressource)
private meilleurBonus(type: string, ressource: string, roles: string[], sens: 'conso' | 'prod'): Bonus | undefined {
  const candidats = this.bonus().filter(
    (b) =>
      b.module === type && // bon module
    b.ressource === ressource && // bonne ressource
    roles.includes(b.role.toLowerCase()) && // un astronaute de ce rôle est présent
    (sens === 'conso' ? b.multiplicateur < 1 : b.multiplicateur > 1), // réduit la conso OU augmente la prod
  );
  if (candidats.length === 0) return undefined; // aucun bonus applicable

  // conso : on garde le plus petit multiplicateur ; prod : le plus grand
  return candidats.reduce((meilleur, b) =>
    sens === 'conso'
  ? (b.multiplicateur < meilleur.multiplicateur ? b : meilleur)
  : (b.multiplicateur > meilleur.multiplicateur ? b : meilleur),
);
}

// Bilan d'une ressource : positif = on en a gagné, négatif = on en a perdu
bilan(f: FluxRessource): number {
  return Math.round((f.produite - f.consommee) * 100) / 100; // produit moins consommé, arrondi
}

// Bilan total d'un module : somme des bilans de toutes ses ressources
bilanModule(m: RapportModule): number {
  return Math.round(m.flux.reduce((total, f) => total + this.bilan(f), 0) * 100) / 100;
}

// Met à jour les valeurs des stats affichées en bas à gauche dans la page
private actualiserStats(): void {

  const rapport = this.rapportGlobal();

  this.station.update(station => {

    if (!station) return null;

    return {
      ...station,
      stats: station.stats.map(ressource => {

        const flux = rapport.find(
          r => r.ressource === ressource.type
        );

        if (!flux) return ressource;

        return {
          ...ressource,
          quantite: ressource.quantite
          + flux.produite
          - flux.consommee
        };
      })
    };
  });
}

private verifierSeuilsRessources(): void {

  const stats = this.station()?.stats ?? [];

  this.alertesDanger.set(
    stats
    .filter(r =>
      r.quantite <= SEUILS_RESSOURCES.danger &&
      r.quantite > SEUILS_RESSOURCES.critique
    )
    .map(r => r.type)
  );

  this.alertesCritique.set(
    stats
    .filter(r => r.quantite <= SEUILS_RESSOURCES.critique)
    .map(r => r.type)
  );

  this.alertesEpuisement.set(
    stats
    .filter(r => r.quantite <= 0)
    .map(r => r.type)
  );
}

// Clic sur un module dans l'onglet "Par module" : le déplie ou le replie
toggleModule(id: number) {
  this.moduleOuvert.update((actuel) => (actuel === id ? null : id)); // déjà ouvert → on ferme, sinon on ouvre
}

continuerCycle() {
  if (this.partieTerminee()) {
    this.terminerPartie();
    return;
  }
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
  if (this.enCours() || this.cycleBloque() || this.partieTerminee() ) return; //on ignore le clic sur cyclesuivant si une requête est deja en cours

  this.enCours.set(true);

  // Calcul et actualisation des ressources côté front
  this.actualiserStats();


  // Vérification des nouvelles quantités
  this.verifierSeuilsRessources();

  // Préparation des données à envoyer
  const stats = Object.fromEntries(
    this.station()!.stats.map(r => [r.type, r.quantite])
  ) as Record<RessourceEnum, number>;

  this.stationService.cycleSuivant(this.stationId, stats).subscribe({
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


terminerPartie(): void {
  const station = this.station();

  if (station) {
    this.stationService.redirigerSiTerminee(station);
  }
}


}
