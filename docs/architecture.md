# Architecture

## Vue des dossiers

- `src/config`
- `src/data`
- `src/process`
- `src/gui`

## Rôle des couches

- `CONFIG` : paramètres globaux, constantes financières et règles de simulation.
- `DATA` : objets métier persistants de la ligue, des équipes, des joueurs et des matchs.
- `PROCESS` : construction du monde, orchestration des managers, simulation et visitors.
- `GUI` : dashboards Swing, panneaux et navigation utilisateur.

## Classes centrales

- [League](../src/data/league/League.java) : modèle central de la ligue et des saisons.
- [Team](../src/data/team/Team.java) : franchise avec effectif, finances et performances.
- [Player](../src/data/player/Player.java) : joueur, santé et valeur sportive.
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java) : initialisation des structures métier.
- [GameManager](../src/process/manager/GameManager.java) : pilotage des journées et résultats.
- [MainGui](../src/gui/frame/MainGui.java) : point de coordination de l’interface Swing.

## Dépendances principales

- `GUI` -> `PROCESS` : les vues déclenchent les traitements via les managers.
- `PROCESS` -> `DATA` : les builders/managers manipulent le modèle métier.
- `PROCESS` -> `CONFIG` : la logique lit les politiques et constantes globales.
- `DATA` reste majoritairement passif ; la mutation est pilotée par `PROCESS`.

## Répartition actuelle

- `CONFIG` : 3 classes Java.
- `DATA` : 51 classes Java.
- `PROCESS` : 43 classes Java.
- `GUI` : 14 classes Java.
