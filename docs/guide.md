# Guide de lecture

## Parcours conseillé
- Commencer par `process/LeagueManager.java` pour voir le cycle global du projet.
- Lire ensuite `process/builder/LeagueBuilder.java` pour comprendre le chargement des données CSV et l’initialisation des registres.
- Continuer avec `data/league/League.java`, `data/team/Team.java` et `data/player/Player.java` pour le coeur du modèle métier.
- Poursuivre avec `process/builder/CalendarBuilder.java`, `process/GameGenerator.java` et `process/GameManager.java` pour la logique de calendrier.
- Terminer la partie métier avec `process/GameSimulator.java` et les classes `data/sport/*` qui portent les résultats de match.

## Interface graphique
- `gui/frame/MainGui.java` assemble l’application Swing avec un `CardLayout` racine et un `CardLayout` pour les dashboards.
- `gui/dashboard/OpeningDashboard.java` ouvre le parcours avant l’accès aux écrans principaux.
- `gui/layout/SidebarPanel.java` pilote la navigation entre match, calendrier, classement, finance et carte.
- Les composants `gui/components/*` fournissent les briques visuelles réutilisées par les dashboards.

## Points d’attention
- Le builder de ligue lit `src/test/nba.csv` même si `src/test` est hors périmètre de documentation des classes.
- Les classes `repositery` sont des singletons globaux : utiles pour lire le code, mais elles créent un couplage fort.
- Plusieurs classes de données sont surtout des conteneurs d’état avec peu de logique métier.
- `FinanceBuilder` est une ébauche et ne joue pas encore un rôle central dans le flux principal.
