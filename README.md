# ProjetSpace - Gestion d’une station spatiale

**Léo, Théo Des, Steven, Félix, Tristan**

→ [Accès au Google Drive](https://drive.google.com/drive/folders/1CvHRe-K5fAUiOAJ-cgyEAGiXb8Ol7E5S) ←

## Description du projet

## Classes principales

- **Astronaute** : un nom, un rôle (chercheur, agriculteur (serre), maintenance …) et un module auquel il est affecté. <br>
  - L'astronaute sait dire son rôle et à quel module il appartient.
- **Module** : un nom, un type (labo, dortoir, serre, stockage…), et la liste des astronautes qui y sont affectés. <br>
  - Module sait accueillir des astronautes et donner la liste de ceux qui y sont affectés.
- **Ressource** : un type (oxygène, eau, énergie, nourriture), une quantité actuelle, et un seuil d'alerte en dessous duquel il faut prévenir. <br>
  - Ressource sait se faire consommer, se faire réapprovisionner, et dire si elle est en alerte.
- **Station spatiale** : la classe centrale qui regroupe tous les modules et toutes les ressources. <br>
  - Station Spatiale sait ajouter des modules et des ressources, faire consommer une ressource, vérifier toutes les alertes, et afficher l'état global ou celui d’un module en particulier. <br>
  - Station Spatiale consulte les quantités des ressources et affiche un message d'alerte ou d'alerte critique selon les seuils de chaque module. Exemples :
    - Si stock d'oxygène < 20% → alerte "critique"
    - Si stock d'eau < 30% → alerte "attention"
    - Si énergie insuffisante → certains modules se désactivent automatiquement (ex : serre en premier, labo en dernier)
  - Gère les cycles

### Prioritaire

#### Notion de cycle (1 journée)

1. Répartition des astronautes
2. Production par les modules
3. Consommation des ressources par les modules
4. Consommation vitale par les astronautes
5. Vérification des seuils
6. Rapport du cycle

#### Gestion des ressources

- ***Module(s)*** → <mark>consommation énergie (électricité) si - actif  (1)</mark>
- ***Astronaute(s)*** → <mark>consommation d’O2 + eau + nourriture</mark>
- **Serre** → production d’O2 + nourriture, <mark>consommation de l’eau + (1)</mark>
- **Salle recyclage de l’eau** → production d’eau
- **Laboratoire** → <mark>(1)</mark>
- **Dortoir** →  <mark>(1)</mark>
- **Salle de commandement (gestion des panneaux solaires)** → Production d’énergie (électricité)

#### Gestion des alertes

| **Ressources**        | **Seuil “danger”** | **Seuil”critique”** |
|-----------------------|--------------------|---------------------|
| O2                    |        <30%        |         <10%        |
| Eau                   |        <30%        |         <10%        |
| Nourriture            |        <30%        |         <10%        |
| Energie (électricité) |        <30%        |         <10%        |

**Pour chaque fin de cycle**

→ Pour n seuil **danger** → Message *Faites attention vous avez n incohérences de ressources* <br>
→ Si un seuil **critique** → Message *Seuil critique (nom_ressources) détectée*
→ Si deux seuils **critique** → Message *Seuil critique (nom_ressources) détectée  + évacuation recommandée* <br>
→ Si trois ou plus seuils **critique** → Message *Seuil critique (nom_ressources) détectée  + évacuation immédiate* → **GAME OVER** ? <br>

### Secondaire

Idées en vrac :

- Combien d’astronautes ? 4/5
- Est c’est que le réapprovisionnement ca serait pas juste le fait que à chaque cycle modifier la position des astronautes ?
- une quantité maximale sera à définir
- Si oxygène/eau/nourriture <=0 → GAME OVER
- Si conso élec > prod elec et ressource électricité <=0 → Quelle module s’arrête en premier ou GAME OVER
- Mort des astronautes →quels rôles essentiels qui sans eux, la survie est impossible ? (mécanicien, agriculteur)
- affichage de la tête des astronautes dans la vue du superviseur + la map ?
- Missions de recherche / réparation en EVA ?
- Récupérer trajectoire autres satellites ? => API
  - Localisation ISS via API pour fenêtre de ravitaillement ? <br>
  => [NASA APIs](https://api.nasa.gov/) et [GitHub - International Space Station APIs](https://github.com/corquaid/international-space-station-APIs)

#### Selon les rôles

- **Agriculteur** → serre → O2 et nourriture supplémentaires
- **Technicien de maintenance** → salle de commandement → énergie (électricité) supplémentaires
- **Chercheur** → point de compétences débloquer plus rapidement
- **Superviseur**  → admin, réattribue les astronautes et les ressources
- **Médecin** → restaure la santé des astronautes

#### Notion de points de compétences

#### Notion de repos des astronautes

#### Evènements aléatoires

- Pannes (dépressurisation d’un module, panne des systèmes de survie…)
- maladies
- Tempêtes cosmiques (mdr j’aime bien l’idée)

#### Recruter d’autre membres (voyage Terre-vaisseau)
