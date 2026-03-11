# Guide de lecture

## 1. Par où commencer

- Lire [LeagueBuilder](../src/process/builder/LeagueBuilder.java) pour la création de la ligue.
- Enchaîner avec [CalendarBuilder](../src/process/builder/CalendarBuilder.java) pour la saison.
- Continuer avec [GameManager](../src/process/manager/GameManager.java) et [FinanceManager](../src/process/manager/FinanceManager.java).

## 2. Point d’entrée du programme

- Aucun `public static void main` détecté dans `src` (hors tests).
- Le démarrage visuel s’articule autour de [MainGui](../src/gui/frame/MainGui.java).

## 3. Fonctionnement de l’interface graphique

- [MainGui](../src/gui/frame/MainGui.java) construit la fenêtre et connecte les dashboards.
- Les vues principales sont dans `src/gui/dashboard` (calendrier, classement, finance, carte, match).
- Les composants réutilisables sont dans `src/gui/panel` et `src/gui/layout`.

## 4. Organisation des données

- `src/data/league` : saisons, conférences, divisions, classements.
- `src/data/team` : équipe, stade, performance, finance et stratégies de transfert.
- `src/data/player` : joueur, statut médical, blessures et actifs.
- `src/data/sport` : contexte d’un match, actions de jeu et résultat final.

## 5. Logique métier

- `builder` et `factory` initialisent les objets et le calendrier.
- `manager` orchestre simulation, finance, transferts et partage des revenus.
- `simulator` calcule les issues sportives et financières.
- `visitor` applique des règles spécialisées (market size, transferts, action result).

## 6. Ordre conseillé de lecture

1. [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
2. [TeamFactory](../src/process/factory/TeamFactory.java)
3. [PlayerFactory](../src/process/factory/PlayerFactory.java)
4. [CalendarBuilder](../src/process/builder/CalendarBuilder.java)
5. [LeagueManager](../src/process/manager/LeagueManager.java)
6. [SimulationManager](../src/process/manager/SimulationManager.java)
7. [GameSimulator](../src/process/simulator/GameSimulator.java)
8. [FinanceManager](../src/process/manager/FinanceManager.java)
9. [TradeManager](../src/process/manager/TradeManager.java)
10. [MainGui](../src/gui/frame/MainGui.java)
