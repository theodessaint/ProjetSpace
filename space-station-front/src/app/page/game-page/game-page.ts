import { Component, computed, signal } from '@angular/core';
 
type Etape = {
  titre: string;
  sousTitre: string;
  texte: string;
  liste?: string[]; // puces pop-up 3 sous le texte
  bouton?: string; // texte du bouton (par défaut « Suivant »)
  action?: string; // si présent, le joueur doit faire cette action pour continuer
};
 
@Component({
  imports: [],
  selector: 'app-game-page',
  styleUrl: './game-page.css',
  templateUrl: './game-page.html',
})
export class GamePage {

  // ---------- TUTO (le lore + cycle 1(tuto joueur)) ----------
  // Pop-ups 1 à 4 : le lore, puis pop-ups 5 à 11 : le tuto du cycle 1
  etapes: Etape[] = [
    {
      // Pop-up 1
      titre: 'L’appel',
      sousTitre: 'Signal de détresse reçu',
      texte:
        "An 2187. La planète TPT, devenue inhabitable, est en train de s’éteindre. Ses habitants ont lancé un dernier appel avant que les communications ne soient coupées.",
    },
    {
      // Pop-up 2
      titre: 'La station',
      sousTitre: 'Bienvenue à bord',
      texte:
        "Vous voilà aux commandes de la station Aurore, un vaisseau à l’abandon, mais encore debout. Il est devenu le dernier refuge pour les astronautes qui fuient TPT. Leurs navettes arrivent déjà, et la station est presque vide.",
    },
    {
      // Pop-up 3
      titre: 'La mission',
      sousTitre: 'Votre mission, commandant',
      texte:
        "Chaque astronaute accueilli doit trouver sa place à bord. Pour la prospérité du vaisseau, vous allez devoir :",
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
        "Les ressources sont limitées et d’autres astronautes arrivent. Chaque décision compte : une ressource qui tombe à zéro, et c’est tout l’équipage qui est en danger. Saurez-vous les maintenir en vie jusqu’à ce qu’une nouvelle terre se présente ?",
      bouton: 'Prendre le commandement',
    },
    {
      // Pop-up 5
      titre: 'Le tableau de bord',
      sousTitre: 'Quatre ressources, une seule règle',
      texte:
        "Vos quatre ressources sont affichées en bas à gauche de l’écran : énergie, eau, oxygène et nourriture. Elles diminuent à chaque cycle, car l’équipage consomme. Gardez-les au-dessus de zéro et évitez d’avoir plus de trois seuils critiques détectés, sinon c’est la fin de la mission.",
    },
    {
      // Pop-up 6
      titre: 'Votre premier module',
      sousTitre: 'Commençons par l’énergie',
      texte:
        "Sans énergie, la station s’éteint. Construisez la salle des machines : elle alimente le reste du vaisseau. Cliquez sur la salle des machines pour la mettre en service.",
      action: 'machines',
    },
    {
      // Pop-up 7
      titre: 'La serre',
      sousTitre: 'De quoi nourrir l’équipage',
      texte:
        "La serre produit la nourriture dont votre équipage a besoin. Chaque astronaute en consomme à chaque cycle : sans serre, la faim s’installe vite. Construisez votre première serre.",
      action: 'serre',
    },
    {
      // Pop-up 8
      titre: 'Le recyclage eau / air',
      sousTitre: 'Respirer et boire',
      texte:
        "Le recyclage purifie l’air et récupère l’eau utilisée à bord : c’est lui qui fournit l’oxygène et l’eau de l’équipage. Construisez votre premier module de recyclage.",
      action: 'recyclage',
    },
    {
      // Pop-up 9
      titre: 'Une navette arrive',
      sousTitre: 'Ils comptent sur vous',
      texte:
        "Une navette de TPT vient de s’amarrer. Accueillez votre premier astronaute. Chaque astronaute peut posséder un rôle qui détermine là où il sera le plus utile. Par exemple, un agriculteur dans la serre produit davantage de nourriture.",
      action: 'accueillir',
    },
    {
      // Pop-up 10
      titre: 'Les alertes',
      sousTitre: 'Ne laissez pas tomber une jauge',
      texte:
        "Quand une ressource passe sous 30 %, une alerte de danger s’affiche. Sous 10 %, elle devient critique. Anticipez en construisant des modules et en affectant vos astronautes avant d’en arriver là.",
    },
    {
      // Pop-up 11
      titre: 'Fin du cycle',
      sousTitre: 'Le temps passe',
      texte:
        "Vos choix sont faits. Cliquez sur « Cycle suivant » : les ressources seront consommées et produites selon vos modules et vos astronautes. Bonne chance, commandant.",
      action: 'cycle',
    },
  ];
 
  index = signal(0); //indique a quelle etape on est dans le tuto
  visible = signal(true); // mettre false pour ne plus afficher le tuto
  etape = computed(() => this.etapes[this.index()]); 
  //donne le pop-up en cours 
  //Cette ligne donne « le pop-up en cours ». this.etapes est ta liste des 11 pop-ups,
  // et this.index() est le numéro actuel. Si index vaut 2, etape()
  // renvoie le 3e pop-up (« La mission »). computed signifie que cette valeur
  // se recalcule toute seule dès que index change. C'est ce qui permet à l'HTML 
  // d'afficher etape().titre. (mrc chat pour cette commande)
 
  suivant() {
    if (this.index() < this.etapes.length - 1) {
      this.index.update((i) => i + 1);
    } else {
      this.visible.set(false); // dernier pop-up : le tuto se ferme
    }
  }
 
  // Fait avancer le tuto si le joueur vient de faire l’action que l'on attendait de lui. Sinon, ne fait rien.
  actionFaite(nom: string) {
    if (this.visible() && this.etape().action === nom) {
      this.suivant();
    }
  }
 
  passer() { //bouton fait passer le tuto
    this.visible.set(false);
  }
 
  // ---------- ACTIONS DU JEU ----------
  // Pour l’instant elles ne font que prévenir le tuto.
  // méthode à faire
 
  construireModule(type: 'machines' | 'serre' | 'recyclage') {
    // construire le module (appel au back, mise à jour des ressources...)
    this.actionFaite(type);
  }
 
  accueillirAstronaute() {
    // accueillir un astronaute
    this.actionFaite('accueillir');
  }
 
  cycleSuivant() {
    // passer au cycle suivant
    this.actionFaite('cycle');
  }
}