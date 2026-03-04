# Architecture

## Vue d’ensemble

- `config` → constantes globales de simulation et de finance.
- `data` → modèle métier de la ligue, des équipes, des joueurs et des matchs.
- `process` → chargement des données, génération du calendrier et simulation.
- `gui` → interface Swing, navigation et dashboards.

Le projet suit une séparation simple :

- les objets de `data` stockent surtout l’état ;
- les classes de `process` construisent et font évoluer cet état ;
- les classes de `gui` affichent une maquette d’application autour de ces données.

## Vue simple des dossiers

- `src/config`
  - `FinanceConfiguration`
  - `SimulationConfiguration`

- `src/data`
  - `league`, `team`, `player` : noyau métier
  - `calendar` : jours de match et événements spéciaux
  - `sport` : contexte, résultat et actions de jeu
  - `finance` : budgets, revenus, dépenses, transferts

- `src/process`
  - `LeagueBuilder` : charge les données CSV
  - `CalendarBuilder` : place les matchs dans le calendrier
  - `GameGenerator` : crée les affiches
  - `GameSimulator` : simule un match
  - `LeagueManager` : enchaîne les grandes étapes
  - `repositery/*` : registres partagés

- `src/gui`
  - `frame` : fenêtre principale
  - `layout` : barre latérale
  - `dashboard` : vues principales
  - `components` : briques graphiques réutilisables

## Rôle des grands blocs

### CONFIG

- Fournit les constantes utilisées partout dans le projet.
- Évite de dupliquer les valeurs de dates, de quotas, de probabilités et de seuils financiers.

### DATA

- Contient les entités du domaine.
- `League` relie conférences, divisions, saisons et finance globale.
- `Team` et `Player` portent l’essentiel des données manipulées par la simulation.

### PROCESS

- Porte la logique métier réelle.
- Lit le fichier CSV, instancie les objets, génère les rencontres puis simule les journées.
- Utilise des registres (`PlayerRepositery`, `TeamRepositery`, etc.) pour retrouver rapidement les objets déjà créés.

### GUI

- Assemble une interface Swing à base de `JFrame`, `JPanel` et `CardLayout`.
- La GUI reste surtout une couche de présentation.
- Elle ne contient pas le cœur de la simulation sportive.

## Classes centrales du système

- `LeagueManager`
  - Orchestrateur principal.
  - Lance la construction de la ligue, du calendrier puis la simulation d’une journée.

- `LeagueBuilder`
  - Lit `src/test/nba.csv`.
  - Construit `League`, `Team`, `Player` et initialise les registres.

- `League`
  - Objet racine du domaine.
  - Donne accès aux conférences, à la saison régulière, aux playoffs et à la finance de ligue.

- `CalendarBuilder`
  - Prépare la saison régulière.
  - Ajoute les événements spéciaux, génère les affiches et répartit les matchs par date.

- `GameSimulator`
  - Moteur de simulation.
  - Produit score, actions de jeu, fatigue et mises à jour statistiques.

- `MainGui`
  - Fenêtre principale de l’interface.
  - Passe de l’écran d’ouverture aux dashboards via `CardLayout`.

## Dépendances principales

- `process` dépend fortement de `data` et de `config`.

- `LeagueBuilder` dépend de :
  - `PlayerFactory`
  - `TeamFactory`
  - `DivisionRepositery`
  - `PlayerRepositery`
  - `TeamRepositery`
  - `PreSeasonAssetRepositery`
  - `CurrentSeasonAssetRepositery`

- `CalendarBuilder` dépend de :
  - `League`
  - `RegularSeason`
  - `GameGenerator`
  - `GameManager`
  - `Schedule`

- `GameSimulator` dépend de :
  - `Player`, `Team`, `Game`, `GameResult`
  - les actions de `data/sport/play`
  - les statistiques de `Asset`
  - `FinanceManager` pour certaines pondérations économiques

- `gui` dépend surtout de :
  - `gui/components`
  - `gui/dashboard`
  - `gui/layout`

## Points structurants à retenir

- Le modèle métier est assez riche, mais beaucoup de classes `data` sont surtout des conteneurs.

- La logique métier est concentrée dans peu de classes :
  - `LeagueBuilder`
  - `CalendarBuilder`
  - `GameGenerator`
  - `GameManager`
  - `GameSimulator`
  - `LeagueManager`

- `FinanceBuilder` existe, mais reste peu exploité dans l’état actuel du code.

- L’entrée exécutable repérée est dans `src/test/TestGui.java`, alors que la vraie classe centrale de l’IHM est `MainGui`.
