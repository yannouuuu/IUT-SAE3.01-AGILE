# Cluedo — Groupe 7

Projet Java : un Cluedo en interface console.

## Le jeu

- **Cluedo classique** : retrouver qui a tué qui, où et avec quelle arme.
- **Portes fermées** : certaines portes du plateau sont verrouillées. Pour les ouvrir, il faut parler aux **PNJ** présents sur la map : ils donnent une **énigme** dont la solution indique dans quelle autre salle se trouve la **clé** de la porte.
- **Deux modes de jeu** :
  - **Solo** (priorité du sprint en cours) : le joueur contre des bots.
  - **Multijoueur** (si on prend de l'avance) : chacun son tour sur le même PC, avec un **countdown** qui affiche qui est le tueur (pour les tests du jeu).

## Interface

```
+---------------------------------------------------------+
|                        LOGO                             |
+-------------------------------------------+-------------+
|                                           |             |
|                  MAP                      |   CARNET    |
|            (plateau de Cluedo)            |  (suspects, |
|                                           |   armes,    |
|                                           |   lieux)    |
+-------------------------------------------+-------------+
|                SAISIE DES COMMANDES                     |
+---------------------------------------------------------+
```

- **Menu** : écran d'accueil avec un logo au-dessus.
- **Partie** :
  - **Map** au centre/gauche (plateau ASCII).
  - **Carnet des suspects** à droite de la map : on y coche les éléments comme sur le carnet papier du vrai Cluedo.
  - **Saisie des commandes** en dessous de tout ça.

## Déplacement au dé

- On lance le **dé** en amont, puis on se déplace d'autant de cases que le résultat, avec les **flèches directionnelles**.
- Pendant le déplacement, le dé **remplace le carnet** à droite de la map ; une fois le déplacement fini, le carnet revient.

## Commandes

| Commande  | Effet |
|-----------|-------|
| `/carnet` | Ouvre le carnet : il **s'entoure de jaune** pour indiquer qu'on est sur la vue Carnet. On coche les éléments avec **Espace**. |
| `/map`    | Revient sur la vue Map. |

## Structure du code (proposition)

```
src/main/java/
├── Main.java                   Point d'entrée : lance le Menu
├── ui/
│   ├── Menu.java               Logo + écran d'accueil, choix du mode
│   ├── Rendu.java              Layout général : assemblage map + carnet + saisie
│   ├── AffichageMap.java       Rendu du plateau ASCII (ex-CluedoMap)
│   ├── AffichageCarnet.java    Vue carnet + contour jaune
│   └── AffichageDe.java        Vue dé qui remplace le carnet pendant le déplacement
├── moteur/
│   ├── BoucleDeJeu.java        Machine à états : Menu → Partie → Fin
│   ├── TourManager.java        Ordre des tours, tour des bots
│   └── EtatPartie.java         État partagé : où est qui, quelle porte est fermée, etc.
├── modele/
│   ├── Joueur.java / Bot.java
│   ├── Carte.java              (arme + lieu + suspect)
│   ├── Arm.java / Lieux.java / Suspect.java   Enums
│   └── Plateau.java            Grille logique : pièces, portes, passages
├── deplacement/
│   ├── De.java                 Lancer du dé
│   └── Mouvement.java          Déplacement aux flèches, cases restantes, collisions portes
├── carnet/
│   └── Carnet.java             Coches suspects/armes/lieux, commande /carnet et /map
├── interaction/
│   ├── Saisie.java             Boîte de saisie + contrôle de saisie (ex-SaisieUtilisateur)
│   ├── Commandes.java          Parser des commandes (/carnet, /map, ...)
│   └── Pnj.java                PNJ sur la map
├── enigmes/
│   ├── Enigme.java             Une énigme + son emplacement sur la map
│   └── PortesFermees.java      Portes verrouillées, clé dans la salle indiquée par l'énigme
└── multi/                      (à faire seulement si on prend de l'avance)
    └── TourLocal.java          Alternance sur le même PC + countdown pour pas montrer qui est le tueur
```

## Lancer le jeu

```bash
./run.sh        # Linux / macOS
run.bat         # Windows
```

ou

```bash
./mvnw compile exec:java
```
