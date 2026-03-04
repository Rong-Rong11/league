# Classes
Documentation synthétique des classes Java de `src` hors `src/test`.

## CONFIG
### FinanceConfiguration
📄 Fichier
[FinanceConfiguration.java](../src/config/FinanceConfiguration.java)
- Rôle : Constantes de budget, revenus et seuils financiers de la ligue.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : aucune.
- Utilisée par : [LeagueFinance](../src/data/league/LeagueFinance.java), [LeagueBuilder](../src/process/builder/LeagueBuilder.java), [TeamFactory](../src/process/factory/TeamFactory.java)
### SimulationConfiguration
📄 Fichier
[SimulationConfiguration.java](../src/config/SimulationConfiguration.java)
- Rôle : Constantes globales pour la saison, les matchs et les équipes.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : aucune.
- Utilisée par : [League](../src/data/league/League.java), [LeagueFinance](../src/data/league/LeagueFinance.java), [HealthStatus](../src/data/player/HealthStatus.java), [Team](../src/data/team/Team.java), [GameGenerator](../src/process/GameGenerator.java), [GameManager](../src/process/GameManager.java)

## DATA
### ActionResult
📄 Fichier
[ActionResult.java](../src/data/sport/play/ActionResult.java)
- Rôle : Base commune des actions enregistrées pendant un match.
- Méthodes importantes : `getName()`, `setName(...)`, `setActionTime(...)`, `getActionTime()`
- Classes utilisées : [OffensiveAction](../src/data/sport/play/OffensiveAction.java)
- Utilisée par : [Block](../src/data/sport/play/Block.java), [EndOfTime](../src/data/sport/play/EndOfTime.java), [PointScored](../src/data/sport/play/PointScored.java), [Rebound](../src/data/sport/play/Rebound.java), [Turnover](../src/data/sport/play/Turnover.java), [GameResult](../src/data/sport/setup/GameResult.java)
### AmbitiousProfil
📄 Fichier
[AmbitiousProfil.java](../src/data/team/finance/AmbitiousProfil.java)
- Rôle : Profil financier orienté investissement et performance.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : [FinancialProfil](../src/data/team/finance/FinancialProfil.java)
- Utilisée par : [GameSimulator](../src/process/GameSimulator.java), [LeagueBuilder](../src/process/builder/LeagueBuilder.java), [TeamFactory](../src/process/factory/TeamFactory.java)
### Asset
📄 Fichier
[Asset.java](../src/data/player/Asset.java)
- Rôle : Statistiques sportives utilisées pour évaluer un joueur.
- Méthodes importantes : `getNote()`, `setNote(...)`, `getMinutesPlayedPerMatch()`, `setMinutesPlayed(...)`
- Classes utilisées : aucune.
- Utilisée par : [Player](../src/data/player/Player.java), [GameSimulator](../src/process/GameSimulator.java), [PlayerFactory](../src/process/factory/PlayerFactory.java), [CurrentSeasonAssetRepositery](../src/process/repositery/CurrentSeasonAssetRepositery.java), [PreSeasonAssetRepositery](../src/process/repositery/PreSeasonAssetRepositery.java)
### BalancedProfil
📄 Fichier
[BalancedProfil.java](../src/data/team/finance/BalancedProfil.java)
- Rôle : Profil financier intermédiaire entre coût et ambition.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : [FinancialProfil](../src/data/team/finance/FinancialProfil.java)
- Utilisée par : [GameSimulator](../src/process/GameSimulator.java), [LeagueBuilder](../src/process/builder/LeagueBuilder.java), [TeamFactory](../src/process/factory/TeamFactory.java)
### Block
📄 Fichier
[Block.java](../src/data/sport/play/Block.java)
- Rôle : Action de jeu représentant un contre défensif.
- Méthodes importantes : `getBlockingPlayer()`, `setBlockingPlayer(...)`
- Classes utilisées : [Player](../src/data/player/Player.java), [ActionResult](../src/data/sport/play/ActionResult.java)
- Utilisée par : [GameSimulator](../src/process/GameSimulator.java)
### Budget
📄 Fichier
[Budget.java](../src/data/finance/budget/Budget.java)
- Rôle : Budget cumulant montant initial, revenus, dépenses et solde restant.
- Méthodes importantes : `addIncome(...)`, `addExpense(...)`, `getInitialAmount()`, `setInitialAmount(...)`
- Classes utilisées : [Expense](../src/data/finance/budget/Expense.java), [Income](../src/data/finance/budget/Income.java)
- Utilisée par : [LeagueFinance](../src/data/league/LeagueFinance.java), [TeamFinance](../src/data/team/finance/TeamFinance.java), [FinanceManager](../src/process/FinanceManager.java), [LeagueBuilder](../src/process/builder/LeagueBuilder.java), [TeamFactory](../src/process/factory/TeamFactory.java)
### Conference
📄 Fichier
[Conference.java](../src/data/league/Conference.java)
- Rôle : Regroupe les divisions et équipes d’une conférence NBA.
- Méthodes importantes : `getDivisions()`, `setDivisions(...)`, `addTeam(...)`, `addDivision(...)`
- Classes utilisées : [Division](../src/data/league/Division.java), [Team](../src/data/team/Team.java)
- Utilisée par : [League](../src/data/league/League.java), [GameGenerator](../src/process/GameGenerator.java), [GameManager](../src/process/GameManager.java), [CalendarBuilder](../src/process/builder/CalendarBuilder.java)
### Division
📄 Fichier
[Division.java](../src/data/league/Division.java)
- Rôle : Regroupe les équipes d’une division et leur nom.
- Méthodes importantes : `getTeams()`, `setTeams(...)`, `addTeam(...)`, `getName()`
- Classes utilisées : [Team](../src/data/team/Team.java)
- Utilisée par : [Conference](../src/data/league/Conference.java), [League](../src/data/league/League.java), [GameGenerator](../src/process/GameGenerator.java), [GameManager](../src/process/GameManager.java), [CalendarBuilder](../src/process/builder/CalendarBuilder.java), [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
### EconomicalProfil
📄 Fichier
[EconomicalProfil.java](../src/data/team/finance/EconomicalProfil.java)
- Rôle : Profil financier centré sur la maîtrise des coûts.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : [FinancialProfil](../src/data/team/finance/FinancialProfil.java)
- Utilisée par : [LeagueBuilder](../src/process/builder/LeagueBuilder.java), [TeamFactory](../src/process/factory/TeamFactory.java)
### EndOfTime
📄 Fichier
[EndOfTime.java](../src/data/sport/play/EndOfTime.java)
- Rôle : Action marquant la fin d’une période de jeu.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : [ActionResult](../src/data/sport/play/ActionResult.java)
- Utilisée par : [GameSimulator](../src/process/GameSimulator.java)
### Expense
📄 Fichier
[Expense.java](../src/data/finance/budget/Expense.java)
- Rôle : Valeur métier représentant une dépense nommée.
- Méthodes importantes : `getName()`, `getAmount()`
- Classes utilisées : aucune.
- Utilisée par : [Budget](../src/data/finance/budget/Budget.java), [FinanceManager](../src/process/FinanceManager.java)
### FinancialProfil
📄 Fichier
[FinancialProfil.java](../src/data/team/finance/FinancialProfil.java)
- Rôle : Type de base des politiques financières d’équipe.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : aucune.
- Utilisée par : [Team](../src/data/team/Team.java), [AmbitiousProfil](../src/data/team/finance/AmbitiousProfil.java), [BalancedProfil](../src/data/team/finance/BalancedProfil.java), [EconomicalProfil](../src/data/team/finance/EconomicalProfil.java), [TeamFinance](../src/data/team/finance/TeamFinance.java), [GameSimulator](../src/process/GameSimulator.java)
### Game
📄 Fichier
[Game.java](../src/data/sport/setup/Game.java)
- Rôle : Objet match reliant contexte, score final et résultats intermédiaires.
- Méthodes importantes : `getGameContext()`, `setGameContext(...)`, `getQuarterResults()`, `setQuarterResults(...)`
- Classes utilisées : [GameContext](../src/data/sport/setup/GameContext.java), [GameResult](../src/data/sport/setup/GameResult.java)
- Utilisée par : [GameDay](../src/data/calendar/GameDay.java), [Team](../src/data/team/Team.java), [Schedule](../src/data/team/calendar/Schedule.java), [GameGenerator](../src/process/GameGenerator.java), [GameManager](../src/process/GameManager.java), [GameSimulator](../src/process/GameSimulator.java)
### GameContext
📄 Fichier
[GameContext.java](../src/data/sport/setup/GameContext.java)
- Rôle : Contexte d’un match avec équipes, type et planification.
- Méthodes importantes : `isScheduled()`, `setScheduled(...)`, `getHomeTeam()`, `setHomeTeam(...)`
- Classes utilisées : [Team](../src/data/team/Team.java), [GameManager](../src/process/GameManager.java)
- Utilisée par : [Game](../src/data/sport/setup/Game.java), [Team](../src/data/team/Team.java), [GameGenerator](../src/process/GameGenerator.java), [GameManager](../src/process/GameManager.java)
### GameDay
📄 Fichier
[GameDay.java](../src/data/calendar/GameDay.java)
- Rôle : Jour de calendrier qui regroupe les matchs planifiés à une date.
- Méthodes importantes : `getDate()`, `isEmpty()`, `getGames()`, `setGames(...)`
- Classes utilisées : [Game](../src/data/sport/setup/Game.java)
- Utilisée par : [NBACalendar](../src/data/calendar/NBACalendar.java), [SpecialEvent](../src/data/calendar/SpecialEvent.java), [LeagueManager](../src/process/LeagueManager.java), [CalendarBuilder](../src/process/builder/CalendarBuilder.java)
### GameResult
📄 Fichier
[GameResult.java](../src/data/sport/setup/GameResult.java)
- Rôle : Résultat détaillé d’un match avec scores et actions.
- Méthodes importantes : `addActions(...)`, `getScorehomeTeam()`, `setScorehomeTeam(...)`, `getScoreAwayTeam()`
- Classes utilisées : [Player](../src/data/player/Player.java), [ActionResult](../src/data/sport/play/ActionResult.java), [Team](../src/data/team/Team.java)
- Utilisée par : [Game](../src/data/sport/setup/Game.java), [GameSimulator](../src/process/GameSimulator.java)
### HealthStatus
📄 Fichier
[HealthStatus.java](../src/data/player/HealthStatus.java)
- Rôle : Suit fatigue, blessure et disponibilité d’un joueur.
- Méthodes importantes : `getFatigue()`, `setFatigue(...)`, `isInjured()`, `setInjured(...)`
- Classes utilisées : [SimulationConfiguration](../src/config/SimulationConfiguration.java), [Injury](../src/data/player/Injury.java)
- Utilisée par : [Player](../src/data/player/Player.java), [GameSimulator](../src/process/GameSimulator.java)
### Income
📄 Fichier
[Income.java](../src/data/finance/budget/Income.java)
- Rôle : Valeur métier représentant une source de revenu.
- Méthodes importantes : `getName()`, `getAmount()`
- Classes utilisées : aucune.
- Utilisée par : [Budget](../src/data/finance/budget/Budget.java), [LeagueFinance](../src/data/league/LeagueFinance.java), [FinanceManager](../src/process/FinanceManager.java), [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
### Injury
📄 Fichier
[Injury.java](../src/data/player/Injury.java)
- Rôle : Décrit le type et la durée d’une blessure.
- Méthodes importantes : `getInjuryType()`, `setInjuryType(...)`, `getInjuryDuration()`, `setInjuryDuration(...)`
- Classes utilisées : aucune.
- Utilisée par : [HealthStatus](../src/data/player/HealthStatus.java), [GameSimulator](../src/process/GameSimulator.java)
### League
📄 Fichier
[League.java](../src/data/league/League.java)
- Rôle : Agrégat principal contenant conférences, saisons, finances et équipes.
- Méthodes importantes : `getWesternConference()`, `setWesternConference(...)`, `getEasternConference()`, `setEasternConfernce(...)`
- Classes utilisées : [SimulationConfiguration](../src/config/SimulationConfiguration.java), [Conference](../src/data/league/Conference.java), [Division](../src/data/league/Division.java), [LeagueFinance](../src/data/league/LeagueFinance.java), [Playoff](../src/data/league/Playoff.java), [RegularSeason](../src/data/league/RegularSeason.java)
- Utilisée par : [MainGui](../src/gui/frame/MainGui.java), [SidebarPanel](../src/gui/layout/SidebarPanel.java), [GameGenerator](../src/process/GameGenerator.java), [GameManager](../src/process/GameManager.java), [LeagueManager](../src/process/LeagueManager.java), [CalendarBuilder](../src/process/builder/CalendarBuilder.java)
### LeagueFinance
📄 Fichier
[LeagueFinance.java](../src/data/league/LeagueFinance.java)
- Rôle : Porte salary cap, luxury tax et budget global de la ligue.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : [FinanceConfiguration](../src/config/FinanceConfiguration.java), [SimulationConfiguration](../src/config/SimulationConfiguration.java), [Budget](../src/data/finance/budget/Budget.java), [Income](../src/data/finance/budget/Income.java), [FinanceManager](../src/process/FinanceManager.java)
- Utilisée par : [League](../src/data/league/League.java), [FinanceManager](../src/process/FinanceManager.java), [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
### MarketSize
📄 Fichier
[MarketSize.java](../src/data/team/finance/MarketSize.java)
- Rôle : Indique la taille de marché d’une franchise.
- Méthodes importantes : `getSize()`, `setSize(...)`
- Classes utilisées : aucune.
- Utilisée par : [TeamFinance](../src/data/team/finance/TeamFinance.java), [TeamFactory](../src/process/factory/TeamFactory.java)
### NBACalendar
📄 Fichier
[NBACalendar.java](../src/data/calendar/NBACalendar.java)
- Rôle : Calendrier principal associant chaque date à un objet GameDay.
- Méthodes importantes : `getCalendar()`, `setCalendar(...)`
- Classes utilisées : [GameDay](../src/data/calendar/GameDay.java)
- Utilisée par : [Season](../src/data/league/Season.java)
### OffensiveAction
📄 Fichier
[OffensiveAction.java](../src/data/sport/play/OffensiveAction.java)
- Rôle : Décrit le type d’action offensive exécutée.
- Méthodes importantes : `getName()`
- Classes utilisées : aucune.
- Utilisée par : [ActionResult](../src/data/sport/play/ActionResult.java), [GameSimulator](../src/process/GameSimulator.java)
### Player
📄 Fichier
[Player.java](../src/data/player/Player.java)
- Rôle : Entité joueur avec identité, profil sportif et salaire.
- Méthodes importantes : `isStar()`, `setStar(...)`, `getName()`, `getPreSeasonAssets()`
- Classes utilisées : [Asset](../src/data/player/Asset.java), [HealthStatus](../src/data/player/HealthStatus.java)
- Utilisée par : [League](../src/data/league/League.java), [Block](../src/data/sport/play/Block.java), [PointScored](../src/data/sport/play/PointScored.java), [Rebound](../src/data/sport/play/Rebound.java), [Turnover](../src/data/sport/play/Turnover.java), [GameResult](../src/data/sport/setup/GameResult.java)
### PlayerExchange
📄 Fichier
[PlayerExchange.java](../src/data/finance/transfer/PlayerExchange.java)
- Rôle : Structure prévue pour décrire un échange de joueurs.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : aucune.
- Utilisée par : aucune.
### PlayerPurchase
📄 Fichier
[PlayerPurchase.java](../src/data/finance/transfer/PlayerPurchase.java)
- Rôle : Structure prévue pour décrire un achat ou transfert de joueur.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : aucune.
- Utilisée par : aucune.
### Playoff
📄 Fichier
[Playoff.java](../src/data/league/Playoff.java)
- Rôle : Spécialisation de Season pour la phase de playoffs.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : [Season](../src/data/league/Season.java)
- Utilisée par : [League](../src/data/league/League.java)
### PointScored
📄 Fichier
[PointScored.java](../src/data/sport/play/PointScored.java)
- Rôle : Action de jeu représentant des points marqués.
- Méthodes importantes : `getPointsScored()`, `getScorerPlayer()`, `setScorerPlayer(...)`, `getAssistPlayer()`
- Classes utilisées : [Player](../src/data/player/Player.java), [ActionResult](../src/data/sport/play/ActionResult.java)
- Utilisée par : [GameSimulator](../src/process/GameSimulator.java)
### Ranking
📄 Fichier
[Ranking.java](../src/data/league/Ranking.java)
- Rôle : Stocke les indicateurs de classement associés à une équipe.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : [Team](../src/data/team/Team.java)
- Utilisée par : [Season](../src/data/league/Season.java)
### Rebound
📄 Fichier
[Rebound.java](../src/data/sport/play/Rebound.java)
- Rôle : Action de jeu représentant un rebond.
- Méthodes importantes : `getReboundPlayer()`, `setReboundPlayer(...)`, `getMissedPlayer()`, `setMissedPlayer(...)`
- Classes utilisées : [Player](../src/data/player/Player.java), [ActionResult](../src/data/sport/play/ActionResult.java)
- Utilisée par : [GameSimulator](../src/process/GameSimulator.java)
### RegularSeason
📄 Fichier
[RegularSeason.java](../src/data/league/RegularSeason.java)
- Rôle : Spécialisation de Season pour la saison régulière.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : [Season](../src/data/league/Season.java)
- Utilisée par : [League](../src/data/league/League.java), [GameManager](../src/process/GameManager.java), [CalendarBuilder](../src/process/builder/CalendarBuilder.java)
### Schedule
📄 Fichier
[Schedule.java](../src/data/team/calendar/Schedule.java)
- Rôle : Planning et compteurs de matchs d’une équipe.
- Méthodes importantes : `getNumberOfPlayedGames()`, `setNumberOfPlayedGames(...)`, `getNumberOfAwayGames()`, `setNumberOfAwayGames(...)`
- Classes utilisées : [Game](../src/data/sport/setup/Game.java)
- Utilisée par : [Team](../src/data/team/Team.java), [CalendarBuilder](../src/process/builder/CalendarBuilder.java)
### Season
📄 Fichier
[Season.java](../src/data/league/Season.java)
- Rôle : Définit les dates, le calendrier et les événements d’une saison.
- Méthodes importantes : `getDebutDate()`, `setDebutDate(...)`, `getEndDate()`, `setEndDate(...)`
- Classes utilisées : [NBACalendar](../src/data/calendar/NBACalendar.java), [SpecialEvent](../src/data/calendar/SpecialEvent.java), [Ranking](../src/data/league/Ranking.java)
- Utilisée par : [Playoff](../src/data/league/Playoff.java), [RegularSeason](../src/data/league/RegularSeason.java), [LeagueManager](../src/process/LeagueManager.java)
### SpecialEvent
📄 Fichier
[SpecialEvent.java](../src/data/calendar/SpecialEvent.java)
- Rôle : Événement spécial inséré dans le calendrier de saison.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : [GameDay](../src/data/calendar/GameDay.java)
- Utilisée par : [Season](../src/data/league/Season.java), [CalendarBuilder](../src/process/builder/CalendarBuilder.java)
### Team
📄 Fichier
[Team.java](../src/data/team/Team.java)
- Rôle : Entité équipe avec joueurs, rivalité, calendrier et finances.
- Méthodes importantes : `getName()`, `setNom(...)`, `getRival()`, `setRival(...)`
- Classes utilisées : [SimulationConfiguration](../src/config/SimulationConfiguration.java), [Player](../src/data/player/Player.java), [Game](../src/data/sport/setup/Game.java), [GameContext](../src/data/sport/setup/GameContext.java), [Schedule](../src/data/team/calendar/Schedule.java), [FinancialProfil](../src/data/team/finance/FinancialProfil.java)
- Utilisée par : [Conference](../src/data/league/Conference.java), [Division](../src/data/league/Division.java), [League](../src/data/league/League.java), [Ranking](../src/data/league/Ranking.java), [GameContext](../src/data/sport/setup/GameContext.java), [GameResult](../src/data/sport/setup/GameResult.java)
### TeamFinance
📄 Fichier
[TeamFinance.java](../src/data/team/finance/TeamFinance.java)
- Rôle : Regroupe budget, profil financier et marché d’une équipe.
- Méthodes importantes : `getFinancialProfil()`
- Classes utilisées : [Budget](../src/data/finance/budget/Budget.java), [FinancialProfil](../src/data/team/finance/FinancialProfil.java), [MarketSize](../src/data/team/finance/MarketSize.java)
- Utilisée par : [Team](../src/data/team/Team.java), [TeamFactory](../src/process/factory/TeamFactory.java)
### Turnover
📄 Fichier
[Turnover.java](../src/data/sport/play/Turnover.java)
- Rôle : Action de jeu représentant une perte de balle.
- Méthodes importantes : `getInterceptedPlayer()`, `getDefensePlayer()`
- Classes utilisées : [Player](../src/data/player/Player.java), [ActionResult](../src/data/sport/play/ActionResult.java)
- Utilisée par : [GameSimulator](../src/process/GameSimulator.java)

## PROCESS
### CalendarBuilder
📄 Fichier
[CalendarBuilder.java](../src/process/builder/CalendarBuilder.java)
- Rôle : Construit le calendrier régulier et place les événements spéciaux.
- Méthodes importantes : `initialization(...)`, `specialEventsPlacement(...)`, `generateAllGames(...)`, `generateRegulaSeasonCalendar(...)`
- Classes utilisées : [SimulationConfiguration](../src/config/SimulationConfiguration.java), [GameDay](../src/data/calendar/GameDay.java), [SpecialEvent](../src/data/calendar/SpecialEvent.java), [Conference](../src/data/league/Conference.java), [Division](../src/data/league/Division.java), [League](../src/data/league/League.java)
- Utilisée par : [LeagueManager](../src/process/LeagueManager.java)
### CurrentSeasonAssetRepositery
📄 Fichier
[CurrentSeasonAssetRepositery.java](../src/process/repositery/CurrentSeasonAssetRepositery.java)
- Rôle : Registre des statistiques de saison courante par joueur.
- Méthodes importantes : `getInstance()`, `register(...)`, `getCurrentSeasonAsset(...)`
- Classes utilisées : [Asset](../src/data/player/Asset.java), [Player](../src/data/player/Player.java)
- Utilisée par : [GameSimulator](../src/process/GameSimulator.java), [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
### DivisionRepositery
📄 Fichier
[DivisionRepositery.java](../src/process/repositery/DivisionRepositery.java)
- Rôle : Registre des divisions accessibles par nom.
- Méthodes importantes : `getInstance()`, `register(...)`, `getDivision(...)`
- Classes utilisées : [Division](../src/data/league/Division.java), [Team](../src/data/team/Team.java)
- Utilisée par : [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
### FinanceBuilder
📄 Fichier
[FinanceBuilder.java](../src/process/builder/FinanceBuilder.java)
- Rôle : Ébauche de builder financier reposant sur TeamRepositery.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : [TeamRepositery](../src/process/repositery/TeamRepositery.java)
- Utilisée par : aucune.
### FinanceManager
📄 Fichier
[FinanceManager.java](../src/process/FinanceManager.java)
- Rôle : Calcule salaires moyens et mise à jour des budgets.
- Méthodes importantes : `getAverageSalary(...)`, `updateBudget(...)`
- Classes utilisées : [Budget](../src/data/finance/budget/Budget.java), [Expense](../src/data/finance/budget/Expense.java), [Income](../src/data/finance/budget/Income.java), [LeagueFinance](../src/data/league/LeagueFinance.java), [Player](../src/data/player/Player.java), [Team](../src/data/team/Team.java)
- Utilisée par : [LeagueFinance](../src/data/league/LeagueFinance.java), [GameSimulator](../src/process/GameSimulator.java)
### GameGenerator
📄 Fichier
[GameGenerator.java](../src/process/GameGenerator.java)
- Rôle : Génère les affiches de saison selon divisions et conférences.
- Méthodes importantes : `generateIntraDivision(...)`, `generateIntraConference(...)`, `generateInterConference(...)`
- Classes utilisées : [SimulationConfiguration](../src/config/SimulationConfiguration.java), [Conference](../src/data/league/Conference.java), [Division](../src/data/league/Division.java), [League](../src/data/league/League.java), [Game](../src/data/sport/setup/Game.java), [GameContext](../src/data/sport/setup/GameContext.java)
- Utilisée par : [CalendarBuilder](../src/process/builder/CalendarBuilder.java)
### GameManager
📄 Fichier
[GameManager.java](../src/process/GameManager.java)
- Rôle : Fournit les règles métier autour des dates et affiches.
- Méthodes importantes : `isWeekend(...)`, `isImportantDay(...)`, `isSpecialEvent(...)`, `playedYesterday(...)`
- Classes utilisées : [SimulationConfiguration](../src/config/SimulationConfiguration.java), [Conference](../src/data/league/Conference.java), [Division](../src/data/league/Division.java), [League](../src/data/league/League.java), [RegularSeason](../src/data/league/RegularSeason.java), [Game](../src/data/sport/setup/Game.java)
- Utilisée par : [GameContext](../src/data/sport/setup/GameContext.java), [GameGenerator](../src/process/GameGenerator.java), [CalendarBuilder](../src/process/builder/CalendarBuilder.java)
### GameSimulator
📄 Fichier
[GameSimulator.java](../src/process/GameSimulator.java)
- Rôle : Simule un match et produit les actions puis le score.
- Méthodes importantes : `simulateGame(...)`
- Classes utilisées : [SimulationConfiguration](../src/config/SimulationConfiguration.java), [Asset](../src/data/player/Asset.java), [HealthStatus](../src/data/player/HealthStatus.java), [Injury](../src/data/player/Injury.java), [Player](../src/data/player/Player.java), [ActionResult](../src/data/sport/play/ActionResult.java)
- Utilisée par : [LeagueManager](../src/process/LeagueManager.java)
### LeagueBuilder
📄 Fichier
[LeagueBuilder.java](../src/process/builder/LeagueBuilder.java)
- Rôle : Charge le CSV NBA et construit la ligue métier.
- Méthodes importantes : `build()`
- Classes utilisées : [FinanceConfiguration](../src/config/FinanceConfiguration.java), [SimulationConfiguration](../src/config/SimulationConfiguration.java), [Budget](../src/data/finance/budget/Budget.java), [Income](../src/data/finance/budget/Income.java), [Division](../src/data/league/Division.java), [League](../src/data/league/League.java)
- Utilisée par : [LeagueManager](../src/process/LeagueManager.java)
### LeagueManager
📄 Fichier
[LeagueManager.java](../src/process/LeagueManager.java)
- Rôle : Orchestre construction de ligue, calendrier et simulation journalière.
- Méthodes importantes : `buildLeague()`, `buildRegularSeasonCalendar()`, `simulateDay(...)`, `getLeague()`
- Classes utilisées : [GameDay](../src/data/calendar/GameDay.java), [League](../src/data/league/League.java), [Season](../src/data/league/Season.java), [Game](../src/data/sport/setup/Game.java), [GameSimulator](../src/process/GameSimulator.java), [CalendarBuilder](../src/process/builder/CalendarBuilder.java)
- Utilisée par : aucune.
### PlayerFactory
📄 Fichier
[PlayerFactory.java](../src/process/factory/PlayerFactory.java)
- Rôle : Fabrique des joueurs à partir d’une ligne source.
- Méthodes importantes : `createPlayer(...)`
- Classes utilisées : [Asset](../src/data/player/Asset.java), [Player](../src/data/player/Player.java)
- Utilisée par : [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
### PlayerRepositery
📄 Fichier
[PlayerRepositery.java](../src/process/repositery/PlayerRepositery.java)
- Rôle : Registre singleton des joueurs accessibles par nom.
- Méthodes importantes : `getInstance()`, `register(...)`, `getPlayer(...)`, `getAllPlayers()`
- Classes utilisées : [Player](../src/data/player/Player.java)
- Utilisée par : [GameSimulator](../src/process/GameSimulator.java), [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
### PreSeasonAssetRepositery
📄 Fichier
[PreSeasonAssetRepositery.java](../src/process/repositery/PreSeasonAssetRepositery.java)
- Rôle : Registre des statistiques de pré-saison par joueur.
- Méthodes importantes : `getInstance()`, `register(...)`, `getPreSeasonAsset(...)`
- Classes utilisées : [Asset](../src/data/player/Asset.java), [Player](../src/data/player/Player.java)
- Utilisée par : [GameSimulator](../src/process/GameSimulator.java), [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
### TeamFactory
📄 Fichier
[TeamFactory.java](../src/process/factory/TeamFactory.java)
- Rôle : Fabrique des équipes et leur profil financier.
- Méthodes importantes : `createTeam(...)`
- Classes utilisées : [FinanceConfiguration](../src/config/FinanceConfiguration.java), [SimulationConfiguration](../src/config/SimulationConfiguration.java), [Budget](../src/data/finance/budget/Budget.java), [Team](../src/data/team/Team.java), [AmbitiousProfil](../src/data/team/finance/AmbitiousProfil.java), [BalancedProfil](../src/data/team/finance/BalancedProfil.java)
- Utilisée par : [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
### TeamRepositery
📄 Fichier
[TeamRepositery.java](../src/process/repositery/TeamRepositery.java)
- Rôle : Registre singleton des équipes accessibles par nom.
- Méthodes importantes : `getInstance()`, `register(...)`, `getTeam(...)`, `getAllTeams()`
- Classes utilisées : [Team](../src/data/team/Team.java)
- Utilisée par : [FinanceBuilder](../src/process/builder/FinanceBuilder.java), [LeagueBuilder](../src/process/builder/LeagueBuilder.java)

## GUI
### BuildBox
📄 Fichier
[BuildBox.java](../src/gui/components/BuildBox.java)
- Rôle : Composant Swing réutilisable pour les cartes de construction d’écran.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : [DashboardCard](../src/gui/components/DashboardCard.java), [SectionTitle](../src/gui/components/SectionTitle.java)
- Utilisée par : [CalendarDashboard](../src/gui/dashboard/CalendarDashboard.java), [FinanceDashboard](../src/gui/dashboard/FinanceDashboard.java), [MapDashboard](../src/gui/dashboard/MapDashboard.java), [MatchDashboard](../src/gui/dashboard/MatchDashboard.java), [OpeningDashboard](../src/gui/dashboard/OpeningDashboard.java), [RankingDashboard](../src/gui/dashboard/RankingDashboard.java)
### CalendarDashboard
📄 Fichier
[CalendarDashboard.java](../src/gui/dashboard/CalendarDashboard.java)
- Rôle : Tableau de bord Swing dédié au calendrier.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : [BuildBox](../src/gui/components/BuildBox.java), [SectionTitle](../src/gui/components/SectionTitle.java)
- Utilisée par : [MainGui](../src/gui/frame/MainGui.java)
### DashboardCard
📄 Fichier
[DashboardCard.java](../src/gui/components/DashboardCard.java)
- Rôle : Carte visuelle Swing servant de conteneur de contenu.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : aucune.
- Utilisée par : [BuildBox](../src/gui/components/BuildBox.java), [TitledCard](../src/gui/components/TitledCard.java)
### FinanceDashboard
📄 Fichier
[FinanceDashboard.java](../src/gui/dashboard/FinanceDashboard.java)
- Rôle : Tableau de bord Swing dédié aux finances.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : [BuildBox](../src/gui/components/BuildBox.java), [SectionTitle](../src/gui/components/SectionTitle.java)
- Utilisée par : [MainGui](../src/gui/frame/MainGui.java)
### MainGui
📄 Fichier
[MainGui.java](../src/gui/frame/MainGui.java)
- Rôle : Fenêtre principale qui orchestre navigation et écrans Swing.
- Méthodes importantes : `MainGui()`
- Classes utilisées : [League](../src/data/league/League.java), [CalendarDashboard](../src/gui/dashboard/CalendarDashboard.java), [FinanceDashboard](../src/gui/dashboard/FinanceDashboard.java), [MapDashboard](../src/gui/dashboard/MapDashboard.java), [MatchDashboard](../src/gui/dashboard/MatchDashboard.java), [OpeningDashboard](../src/gui/dashboard/OpeningDashboard.java)
- Utilisée par : aucune.
### MapDashboard
📄 Fichier
[MapDashboard.java](../src/gui/dashboard/MapDashboard.java)
- Rôle : Tableau de bord Swing dédié à la carte des franchises.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : [BuildBox](../src/gui/components/BuildBox.java), [SectionTitle](../src/gui/components/SectionTitle.java)
- Utilisée par : [MainGui](../src/gui/frame/MainGui.java)
### MatchDashboard
📄 Fichier
[MatchDashboard.java](../src/gui/dashboard/MatchDashboard.java)
- Rôle : Tableau de bord Swing dédié aux matchs.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : [BuildBox](../src/gui/components/BuildBox.java), [SectionTitle](../src/gui/components/SectionTitle.java)
- Utilisée par : [MainGui](../src/gui/frame/MainGui.java)
### OpeningDashboard
📄 Fichier
[OpeningDashboard.java](../src/gui/dashboard/OpeningDashboard.java)
- Rôle : Écran d’ouverture pour préparer la création de la ligue.
- Méthodes importantes : `getContinueButton()`, `hasSelectedProfil()`, `showSelectionWarning()`
- Classes utilisées : [BuildBox](../src/gui/components/BuildBox.java), [SectionTitle](../src/gui/components/SectionTitle.java)
- Utilisée par : [MainGui](../src/gui/frame/MainGui.java)
### RankingDashboard
📄 Fichier
[RankingDashboard.java](../src/gui/dashboard/RankingDashboard.java)
- Rôle : Tableau de bord Swing dédié au classement.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : [BuildBox](../src/gui/components/BuildBox.java), [SectionTitle](../src/gui/components/SectionTitle.java)
- Utilisée par : [MainGui](../src/gui/frame/MainGui.java)
### SectionTitle
📄 Fichier
[SectionTitle.java](../src/gui/components/SectionTitle.java)
- Rôle : En-tête Swing affichant titre et sous-titre de section.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : aucune.
- Utilisée par : [BuildBox](../src/gui/components/BuildBox.java), [TitledCard](../src/gui/components/TitledCard.java), [CalendarDashboard](../src/gui/dashboard/CalendarDashboard.java), [FinanceDashboard](../src/gui/dashboard/FinanceDashboard.java), [MapDashboard](../src/gui/dashboard/MapDashboard.java), [MatchDashboard](../src/gui/dashboard/MatchDashboard.java)
### SidebarPanel
📄 Fichier
[SidebarPanel.java](../src/gui/layout/SidebarPanel.java)
- Rôle : Barre latérale de navigation entre les dashboards.
- Méthodes importantes : `SidebarPanel()`, `getMatchButton()`, `getCalendarButton()`, `getRankingButton()`
- Classes utilisées : [League](../src/data/league/League.java)
- Utilisée par : [MainGui](../src/gui/frame/MainGui.java)
### TitledCard
📄 Fichier
[TitledCard.java](../src/gui/components/TitledCard.java)
- Rôle : Carte Swing combinant un titre de section et un contenu.
- Méthodes importantes : aucune méthode publique principale.
- Classes utilisées : [DashboardCard](../src/gui/components/DashboardCard.java), [SectionTitle](../src/gui/components/SectionTitle.java)
- Utilisée par : aucune.
