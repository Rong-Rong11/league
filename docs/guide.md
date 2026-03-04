# Guide de lecture
Commencer par `process/LeagueManager.java`, qui montre le cycle global : construire la ligue, générer le calendrier puis simuler une journée.
Le point d’entrée fonctionnel de l’interface est `gui/frame/MainGui.java` ; le seul `main` repéré est dans `src/test/TestGui.java`, hors périmètre métier.
L’interface graphique repose sur Swing avec un `CardLayout` : `MainGui` alterne entre l’écran d’ouverture et les dashboards métier, tandis que `SidebarPanel` pilote la navigation.
Les données sont concentrées dans `data` : `League`, `Conference`, `Division`, `Team`, `Player`, `Season` et les classes financières structurent l’état de l’application.
Les traitements sont concentrés dans `process` : `LeagueBuilder` charge le CSV, `CalendarBuilder` planifie la saison, `GameGenerator` et `GameManager` appliquent les règles, `GameSimulator` produit les résultats.
Ordre conseillé de lecture : `LeagueManager`, `League`, `LeagueBuilder`, `Team` et `Player`, `CalendarBuilder`, `GameSimulator`, puis `MainGui` et les dashboards.
Terminer par les classes `repositery`, `factory` et les composants `gui/components` pour comprendre les détails d’assemblage et les utilitaires transverses.
