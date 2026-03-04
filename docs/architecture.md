# Architecture
Le projet est structuré autour de `src/config`, `src/data`, `src/process` et `src/gui`.
`config` centralise les constantes de simulation et de finance utilisées partout dans le code.
`data` contient les objets métier : ligue, équipes, joueurs, calendrier, finances et résultats de match.
`process` porte la logique applicative : construction de la ligue depuis le CSV, génération du calendrier, simulation des matchs et accès aux registres partagés.
`gui` assemble une interface Swing en tableaux de bord, pilotée par `MainGui` et `SidebarPanel`.
Les classes centrales sont `League`, `LeagueManager`, `LeagueBuilder`, `CalendarBuilder`, `GameSimulator` et `MainGui`.
Le flux principal est : chargement des données avec `LeagueBuilder`, organisation de la saison avec `CalendarBuilder`, simulation via `GameSimulator`, puis affichage dans les dashboards Swing.
Les dépendances vont surtout de `process` vers `data` et `config`, tandis que `gui` consomme surtout les objets métier de haut niveau sans contenir de logique de simulation lourde.
