# Architecture

## Vue simple du projet

- `src/config` : constantes et règles globales de la simulation.
- `src/data` : objets métier, états sportifs, calendrier et finance.
- `src/process` : construction de la ligue, calculs, visitors et simulateurs.
- `src/gui` : interface Swing, navigation et dashboards.

## Répartition des classes

- `CONFIG` : 3 classes
- `DATA` : 51 classes
- `PROCESS` : 42 classes
- `GUI` : 12 classes

## Rôle des grands blocs

- `CONFIG` → fixe les seuils, constantes financières et paramètres de saison.
- `DATA` → porte le modèle de ligue: conférences, équipes, joueurs, matchs et budgets.
- `PROCESS` → transforme les données brutes en objets, génère le calendrier puis simule la saison.
- `GUI` → affiche une interface Swing autour des dashboards match, calendrier, classement, finance et carte.

## Classes centrales

- [`LeagueManager`](../src/process/manager/LeagueManager.java) : point d’orchestration de la construction de ligue et de la simulation quotidienne.
- [`LeagueBuilder`](../src/process/builder/LeagueBuilder.java) : lit `src/test/nba.csv`, crée `League`, `Team`, `Player` et initialise les registres.
- [`CalendarBuilder`](../src/process/builder/CalendarBuilder.java) : prépare la saison régulière avec `GameGenerator`, `GameSelector` et `SpecialEventPlanner`.
- [`GameManager`](../src/process/manager/GameManager.java) et [`GameSimulator`](../src/process/simulator/GameSimulator.java) : jouent les matchs, mettent à jour le score, les statistiques et l’état sportif.
- [`FinanceManager`](../src/process/manager/FinanceManager.java), [`RevenueSharingManager`](../src/process/manager/RevenueSharingManager.java) et [`TradeManager`](../src/process/manager/TradeManager.java) : couvrent les revenus, le partage financier et les transferts.
- [`MainGui`](../src/gui/frame/MainGui.java) : assemble les écrans Swing et la navigation par `CardLayout`.

## Dépendances principales

- `process` dépend fortement de `data` et de `config`; c’est la couche qui pilote presque tout le comportement.
- `LeagueBuilder` dépend surtout de `PlayerFactory`, `TeamFactory` et des repositories pour hydrater les objets métier.
- `CalendarBuilder` et `GameManager` manipulent `League`, `RegularSeason`, `Schedule`, `GameDay` et `Game` pour organiser les rencontres.
- `GameSimulator` dépend des classes de `data/sport`, des joueurs, des équipes et des visitors de résultat.
- `FinanceManager`, `GameRevenueSimulator` et `GameExpenseSimulator` s’appuient sur `Budget`, `Income`, `Expense`, `LeagueFinance` et `TeamFinance`.
- `gui` dépend principalement de `gui/components`, `gui/dashboard` et `gui/layout`, avec peu de logique métier embarquée.

## Ce qu’il faut retenir

- Le cœur du domaine est concentré dans [`League`](../src/data/league/League.java), [`Team`](../src/data/team/Team.java), [`Player`](../src/data/player/Player.java) et les classes de saison.
- Les visitors servent surtout à factoriser des règles de calcul sur les `ActionResult`, les `MarketSize` et les stratégies de transfert.
- Le point d’entrée exécutable repéré est [`TestGui`](../src/test/TestGui.java), mais la classe centrale de l’interface reste [`MainGui`](../src/gui/frame/MainGui.java).
- Le dépôt contient déjà une modification locale dans `src/process/manager/TradeManager.java`; la documentation n’y touche pas.
