# Architecture

## Dossiers
- `config` centralise les constantes de simulation et de finance partagées par tout le projet.
- `data` regroupe le modèle métier : ligue, équipes, joueurs, calendrier, résultats et finances.
- `process` porte la logique applicative : chargement CSV, construction de saison, génération des matchs et simulation.
- `gui` assemble l’interface Swing avec un écran d’ouverture, une barre latérale et plusieurs dashboards.

## Relations principales
- `LeagueManager` orchestre le flux principal : `LeagueBuilder` charge la ligue, `CalendarBuilder` construit la saison, puis `GameSimulator` simule les journées.
- `League` est l’agrégat central : il relie conférences, divisions, équipes, saisons et `LeagueFinance`.
- `CalendarBuilder` s’appuie sur `GameGenerator` et `GameManager` pour placer les affiches dans `RegularSeason`.
- `GameSimulator` consomme `Game`, `GameContext`, `GameResult`, `Player` et les actions de jeu pour produire un score simulé.
- `MainGui` pilote l’interface Swing et navigue entre `OpeningDashboard`, `SidebarPanel` et les dashboards métier.

## Classes centrales
- `LeagueManager` : point de coordination métier.
- `LeagueBuilder` : construction de la ligue depuis `src/test/nba.csv`.
- `CalendarBuilder` : placement des événements et des matchs.
- `GameSimulator` : moteur de simulation d’un match.
- `League` et `Team` : noyau des données manipulées par tout le projet.
- `MainGui` : point d’entrée principal de l’interface graphique.

## Dépendances importantes
- `process` dépend fortement de `data` et de `config`.
- `gui` dépend surtout de `gui/components` et des objets métier de haut niveau, sans logique de simulation lourde.
- Les classes `repositery` servent de registres partagés pour retrouver équipes, joueurs, divisions et statistiques.
- Les `factory` créent `Player` et `Team`, tandis que les profils financiers spécialisent `FinancialProfil`.
- `FinanceBuilder` est présent mais reste très peu implémenté par rapport aux autres builders.
