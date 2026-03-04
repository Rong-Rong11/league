# Guide de lecture

## 1. Par où commencer

Commencer par [`LeagueManager.java`](../src/process/manager/LeagueManager.java).

Cette classe donne la meilleure vue d’ensemble du flux principal :

- création de la ligue ;
- génération du calendrier ;
- simulation d’une journée ;
- initialisation des profils financiers et des transferts.

Lire ensuite [`LeagueBuilder.java`](../src/process/builder/LeagueBuilder.java), puis [`CalendarBuilder.java`](../src/process/builder/CalendarBuilder.java), pour comprendre comment le projet passe des données source à une saison jouable.

## 2. Point d’entrée du programme

Aucune méthode `main` n’est présente dans `src` hors tests.

Le point d’entrée exécutable repéré aujourd’hui est [`TestGui.java`](../src/test/TestGui.java), dans `src/test`.

Pour comprendre l’application, il faut cependant lire surtout [`MainGui.java`](../src/gui/frame/MainGui.java), car c’est cette classe qui construit la fenêtre Swing et branche les dashboards.

## 3. Comment fonctionne l’interface graphique

Lire d’abord [`MainGui.java`](../src/gui/frame/MainGui.java).

La fenêtre utilise un `CardLayout` racine pour passer de l’écran d’ouverture à l’application, puis un second `CardLayout` pour alterner entre les dashboards.

Poursuivre avec :

1. [`OpeningDashboard.java`](../src/gui/dashboard/OpeningDashboard.java) pour la sélection initiale.
2. [`SidebarPanel.java`](../src/gui/layout/SidebarPanel.java) pour la navigation.
3. les dashboards de [`src/gui/dashboard`](../src/gui/dashboard) pour les vues métier.
4. les composants de [`src/gui/components`](../src/gui/components) pour les briques visuelles partagées.

## 4. Comment les données sont organisées

Le cœur du modèle se trouve dans `src/data`.

Ordre conseillé :

1. [`League.java`](../src/data/league/League.java)
2. [`Conference.java`](../src/data/league/Conference.java) et [`Division.java`](../src/data/league/Division.java)
3. [`Team.java`](../src/data/team/Team.java) puis [`TeamFinance.java`](../src/data/team/finance/TeamFinance.java)
4. [`Player.java`](../src/data/player/Player.java), [`Asset.java`](../src/data/player/Asset.java) et [`Injury.java`](../src/data/player/Injury.java)
5. [`RegularSeason.java`](../src/data/league/RegularSeason.java), [`Playoff.java`](../src/data/league/Playoff.java) et [`Ranking.java`](../src/data/league/Ranking.java)
6. [`NBACalendar.java`](../src/data/calendar/NBACalendar.java), [`GameDay.java`](../src/data/calendar/GameDay.java) et [`SpecialEvent.java`](../src/data/calendar/SpecialEvent.java)
7. [`Game.java`](../src/data/sport/setup/Game.java), [`GameContext.java`](../src/data/sport/setup/GameContext.java) et [`GameResult.java`](../src/data/sport/setup/GameResult.java)

## 5. Comment la logique métier est implémentée

La logique métier est concentrée dans `src/process`.

Ordre de compréhension recommandé :

1. [`LeagueBuilder.java`](../src/process/builder/LeagueBuilder.java)
2. [`PlayerFactory.java`](../src/process/factory/PlayerFactory.java) et [`TeamFactory.java`](../src/process/factory/TeamFactory.java)
3. [`CalendarBuilder.java`](../src/process/builder/CalendarBuilder.java)
4. [`GameGenerator.java`](../src/process/builder/calendartools/GameGenerator.java), [`GameSelector.java`](../src/process/builder/calendartools/GameSelector.java) et [`SpecialEventPlanner.java`](../src/process/builder/calendartools/SpecialEventPlanner.java)
5. [`GameManager.java`](../src/process/manager/GameManager.java) puis [`GameSimulator.java`](../src/process/simulator/GameSimulator.java)
6. [`FinanceManager.java`](../src/process/manager/FinanceManager.java), [`RevenueSharingManager.java`](../src/process/manager/RevenueSharingManager.java) et [`TradeManager.java`](../src/process/manager/TradeManager.java)
7. les repositories, utilitaires et visitors si un détail d’implémentation reste flou.

## 6. Dans quel ordre lire les classes importantes

Pour un nouveau développeur, un parcours efficace est :

1. [`LeagueManager.java`](../src/process/manager/LeagueManager.java)
2. [`LeagueBuilder.java`](../src/process/builder/LeagueBuilder.java)
3. [`League.java`](../src/data/league/League.java)
4. [`Team.java`](../src/data/team/Team.java)
5. [`Player.java`](../src/data/player/Player.java)
6. [`CalendarBuilder.java`](../src/process/builder/CalendarBuilder.java)
7. [`GameManager.java`](../src/process/manager/GameManager.java)
8. [`GameSimulator.java`](../src/process/simulator/GameSimulator.java)
9. [`FinanceManager.java`](../src/process/manager/FinanceManager.java)
10. [`MainGui.java`](../src/gui/frame/MainGui.java)

## 7. Points d’attention

- Le chargement initial dépend encore du fichier `src/test/nba.csv`, même si `src/test` est hors périmètre de documentation des classes.
- Les classes `repositery` jouent le rôle de registres globaux; elles simplifient l’accès aux objets mais augmentent le couplage.
- Les classes `visitor` encapsulent des règles spécialisées et deviennent importantes quand on touche au calcul financier ou aux transferts.
- Une partie importante du projet est constituée d’objets de données; l’intelligence métier est surtout centralisée dans `process`.
