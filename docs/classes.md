# Classes

## Table des matières

### CONFIG

Configuration globale de la simulation et des règles financières.

- [FinanceConfiguration](#financeconfiguration) — constantes financières de la ligue
- [SimulationConfiguration](#simulationconfiguration) — paramètres globaux de saison et de match

### DATA

Modèle métier de la ligue, des équipes, des joueurs, du calendrier et des matchs.

- [ActionResult](#actionresult) — base des actions de match
- [AmbitiousProfil](#ambitiousprofil) — profil financier ambitieux
- [Asset](#asset) — statistiques sportives d’un joueur
- [BalancedProfil](#balancedprofil) — profil financier équilibré
- [Block](#block) — action de contre
- [Budget](#budget) — budget avec revenus, dépenses et solde
- [Conference](#conference) — regroupement de divisions NBA
- [Division](#division) — regroupement d’équipes
- [EconomicalProfil](#economicalprofil) — profil financier prudent
- [EndOfTime](#endoftime) — fin de période
- [Expense](#expense) — dépense élémentaire
- [FinancialProfil](#financialprofil) — base des politiques financières
- [Game](#game) — match et score final
- [GameContext](#gamecontext) — contexte d’une rencontre
- [GameDay](#gameday) — journée de matchs
- [GameResult](#gameresult) — résultat détaillé d’un match
- [HealthStatus](#healthstatus) — état physique d’un joueur
- [Income](#income) — revenu élémentaire
- [Injury](#injury) — détail d’une blessure
- [League](#league) — agrégat principal de la ligue
- [LeagueFinance](#leaguefinance) — finance globale de la ligue
- [MarketSize](#marketsize) — taille de marché d’une franchise
- [NBACalendar](#nbacalendar) — calendrier date → journée
- [OffensiveAction](#offensiveaction) — type d’action offensive
- [Player](#player) — entité joueur
- [PlayerExchange](#playerexchange) — modèle d’échange de joueurs
- [PlayerPurchase](#playerpurchase) — modèle d’achat ou transfert
- [Playoff](#playoff) — saison de playoffs
- [PointScored](#pointscored) — action de points marqués
- [Ranking](#ranking) — état de classement
- [Rebound](#rebound) — action de rebond
- [RegularSeason](#regularseason) — saison régulière
- [Schedule](#schedule) — planning d’équipe
- [Season](#season) — cadre commun d’une saison
- [SpecialEvent](#specialevent) — événement spécial de calendrier
- [Team](#team) — entité franchise et effectif
- [TeamFinance](#teamfinance) — finance d’une équipe
- [Turnover](#turnover) — perte de balle ou interception

### PROCESS

Logique applicative de chargement, génération, simulation et registres partagés.

- [CalendarBuilder](#calendarbuilder) — construction du calendrier
- [CurrentSeasonAssetRepositery](#currentseasonassetrepositery) — registre des stats courantes
- [DivisionRepositery](#divisionrepositery) — registre des divisions
- [FinanceBuilder](#financebuilder) — ébauche de builder financier
- [FinanceManager](#financemanager) — calculs financiers utilitaires
- [GameGenerator](#gamegenerator) — génération des affiches
- [GameManager](#gamemanager) — règles utilitaires de calendrier
- [GameSimulator](#gamesimulator) — simulation d’un match
- [LeagueBuilder](#leaguebuilder) — chargement initial de la ligue
- [LeagueManager](#leaguemanager) — orchestrateur métier principal
- [PlayerFactory](#playerfactory) — création des joueurs
- [PlayerRepositery](#playerrepositery) — registre des joueurs
- [PreSeasonAssetRepositery](#preseasonassetrepositery) — registre des stats de présaison
- [TeamFactory](#teamfactory) — création des équipes
- [TeamRepositery](#teamrepositery) — registre des équipes

### GUI

Interface Swing, composants visuels et navigation entre dashboards.

- [BuildBox](#buildbox) — bloc visuel réutilisable
- [CalendarDashboard](#calendardashboard) — vue calendrier
- [DashboardCard](#dashboardcard) — carte graphique de base
- [FinanceDashboard](#financedashboard) — vue finance
- [MainGui](#maingui) — fenêtre principale Swing
- [MapDashboard](#mapdashboard) — vue carte
- [MatchDashboard](#matchdashboard) — vue match
- [OpeningDashboard](#openingdashboard) — écran d’ouverture
- [RankingDashboard](#rankingdashboard) — vue classement
- [SectionTitle](#sectiontitle) — en-tête de section Swing
- [SidebarPanel](#sidebarpanel) — navigation latérale
- [TitledCard](#titledcard) — carte avec titre intégré

## CONFIG

### FinanceConfiguration

📄 Fichier
[FinanceConfiguration.java](../src/config/FinanceConfiguration.java)

Rôle

Centralise les constantes liées au budget de ligue, au salary cap et aux revenus globaux.

Méthodes importantes

- Aucune méthode publique marquante.

Classes utilisées

- Aucune classe métier notable.

Utilisée par

- [LeagueFinance](../src/data/league/LeagueFinance.java)
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
- [TeamFactory](../src/process/factory/TeamFactory.java)

### SimulationConfiguration

📄 Fichier
[SimulationConfiguration.java](../src/config/SimulationConfiguration.java)

Rôle

Regroupe les constantes de calendrier, d’effectif et de simulation sportive.

Méthodes importantes

- Aucune méthode publique marquante.

Classes utilisées

- Aucune classe métier notable.

Utilisée par

- [League](../src/data/league/League.java)
- [GameGenerator](../src/process/GameGenerator.java)
- [GameSimulator](../src/process/GameSimulator.java)

## DATA

### ActionResult

📄 Fichier
[ActionResult.java](../src/data/sport/play/ActionResult.java)

Rôle

Fournit la base commune aux actions produites pendant la simulation d’un match.

Méthodes importantes

- `ActionResult()`
- `getName()`
- `getActionTime()`

Classes utilisées

- [OffensiveAction](../src/data/sport/play/OffensiveAction.java)

Utilisée par

- [Block](../src/data/sport/play/Block.java)
- [PointScored](../src/data/sport/play/PointScored.java)
- [GameResult](../src/data/sport/setup/GameResult.java)

### AmbitiousProfil

📄 Fichier
[AmbitiousProfil.java](../src/data/team/finance/AmbitiousProfil.java)

Rôle

Spécialise une politique financière orientée investissement et compétitivité sportive.

Méthodes importantes

- `AmbitiousProfil()`

Classes utilisées

- [FinancialProfil](../src/data/team/finance/FinancialProfil.java)

Utilisée par

- [GameSimulator](../src/process/GameSimulator.java)
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
- [TeamFactory](../src/process/factory/TeamFactory.java)

### Asset

📄 Fichier
[Asset.java](../src/data/player/Asset.java)

Rôle

Regroupe les statistiques individuelles utilisées pour évaluer un joueur.

Méthodes importantes

- `Asset()`
- `getNote()`
- `getMinutesPlayedPerMatch()`

Classes utilisées

- Aucune classe métier notable.

Utilisée par

- [Player](../src/data/player/Player.java)
- [GameSimulator](../src/process/GameSimulator.java)
- [PlayerFactory](../src/process/factory/PlayerFactory.java)

### BalancedProfil

📄 Fichier
[BalancedProfil.java](../src/data/team/finance/BalancedProfil.java)

Rôle

Spécialise une politique financière intermédiaire entre prudence et ambition.

Méthodes importantes

- `BalancedProfil()`

Classes utilisées

- [FinancialProfil](../src/data/team/finance/FinancialProfil.java)

Utilisée par

- [GameSimulator](../src/process/GameSimulator.java)
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
- [TeamFactory](../src/process/factory/TeamFactory.java)

### Block

📄 Fichier
[Block.java](../src/data/sport/play/Block.java)

Rôle

Représente un contre défensif dans le journal détaillé d’un match.

Méthodes importantes

- `Block()`
- `getBlockingPlayer()`
- `setBlockingPlayer()`

Classes utilisées

- [Player](../src/data/player/Player.java)
- [ActionResult](../src/data/sport/play/ActionResult.java)

Utilisée par

- [GameSimulator](../src/process/GameSimulator.java)

### Budget

📄 Fichier
[Budget.java](../src/data/finance/budget/Budget.java)

Rôle

Porte le budget d’une entité avec revenus, dépenses et montant restant.

Méthodes importantes

- `Budget()`
- `addIncome()`
- `addExpense()`

Classes utilisées

- [Income](../src/data/finance/budget/Income.java)
- [Expense](../src/data/finance/budget/Expense.java)

Utilisée par

- [LeagueFinance](../src/data/league/LeagueFinance.java)
- [TeamFinance](../src/data/team/finance/TeamFinance.java)
- [FinanceManager](../src/process/FinanceManager.java)

### Conference

📄 Fichier
[Conference.java](../src/data/league/Conference.java)

Rôle

Représente une conférence NBA et regroupe ses divisions et franchises.

Méthodes importantes

- `Conference()`
- `getDivisions()`
- `addTeam()`

Classes utilisées

- [Division](../src/data/league/Division.java)
- [Team](../src/data/team/Team.java)

Utilisée par

- [League](../src/data/league/League.java)
- [GameGenerator](../src/process/GameGenerator.java)
- [CalendarBuilder](../src/process/builder/CalendarBuilder.java)

### Division

📄 Fichier
[Division.java](../src/data/league/Division.java)

Rôle

Représente une division sportive servant de conteneur aux équipes.

Méthodes importantes

- `Division()`
- `getTeams()`
- `addTeam()`

Classes utilisées

- [Team](../src/data/team/Team.java)

Utilisée par

- [Conference](../src/data/league/Conference.java)
- [League](../src/data/league/League.java)
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)

### EconomicalProfil

📄 Fichier
[EconomicalProfil.java](../src/data/team/finance/EconomicalProfil.java)

Rôle

Spécialise une politique financière orientée maîtrise des coûts.

Méthodes importantes

- `EconomicalProfil()`

Classes utilisées

- [FinancialProfil](../src/data/team/finance/FinancialProfil.java)

Utilisée par

- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
- [TeamFactory](../src/process/factory/TeamFactory.java)

### EndOfTime

📄 Fichier
[EndOfTime.java](../src/data/sport/play/EndOfTime.java)

Rôle

Marque la fin d’une période dans le déroulé d’un match simulé.

Méthodes importantes

- `EndOfTime()`

Classes utilisées

- [ActionResult](../src/data/sport/play/ActionResult.java)

Utilisée par

- [GameSimulator](../src/process/GameSimulator.java)

### Expense

📄 Fichier
[Expense.java](../src/data/finance/budget/Expense.java)

Rôle

Représente une ligne simple de dépense utilisée par les budgets.

Méthodes importantes

- `Expense()`
- `getName()`
- `getAmount()`

Classes utilisées

- Aucune classe métier notable.

Utilisée par

- [Budget](../src/data/finance/budget/Budget.java)
- [FinanceManager](../src/process/FinanceManager.java)

### FinancialProfil

📄 Fichier
[FinancialProfil.java](../src/data/team/finance/FinancialProfil.java)

Rôle

Fournit le type de base des politiques financières attribuées aux équipes.

Méthodes importantes

- `FinancialProfil()`

Classes utilisées

- Aucune classe métier notable.

Utilisée par

- [Team](../src/data/team/Team.java)
- [TeamFinance](../src/data/team/finance/TeamFinance.java)
- [AmbitiousProfil](../src/data/team/finance/AmbitiousProfil.java)

### Game

📄 Fichier
[Game.java](../src/data/sport/setup/Game.java)

Rôle

Représente un match avec son contexte, ses résultats intermédiaires et son score final.

Méthodes importantes

- `Game()`
- `getGameContext()`
- `getQuarterResults()`

Classes utilisées

- [GameContext](../src/data/sport/setup/GameContext.java)
- [GameResult](../src/data/sport/setup/GameResult.java)

Utilisée par

- [GameDay](../src/data/calendar/GameDay.java)
- [Schedule](../src/data/team/calendar/Schedule.java)
- [GameSimulator](../src/process/GameSimulator.java)

### GameContext

📄 Fichier
[GameContext.java](../src/data/sport/setup/GameContext.java)

Rôle

Regroupe les équipes, le type de match et l’état de planification d’une rencontre.

Méthodes importantes

- `GameContext()`
- `isScheduled()`
- `getHomeTeam()`

Classes utilisées

- [Team](../src/data/team/Team.java)
- [GameManager](../src/process/GameManager.java)

Utilisée par

- [Game](../src/data/sport/setup/Game.java)
- [GameGenerator](../src/process/GameGenerator.java)
- [GameManager](../src/process/GameManager.java)

### GameDay

📄 Fichier
[GameDay.java](../src/data/calendar/GameDay.java)

Rôle

Représente une date de saison et la liste des matchs associés.

Méthodes importantes

- `GameDay()`
- `getDate()`
- `getGames()`

Classes utilisées

- [Game](../src/data/sport/setup/Game.java)

Utilisée par

- [NBACalendar](../src/data/calendar/NBACalendar.java)
- [Season](../src/data/league/Season.java)
- [LeagueManager](../src/process/LeagueManager.java)

### GameResult

📄 Fichier
[GameResult.java](../src/data/sport/setup/GameResult.java)

Rôle

Stocke le score et le détail des actions produites pendant un match.

Méthodes importantes

- `GameResult()`
- `addActions()`
- `getScorehomeTeam()`

Classes utilisées

- [ActionResult](../src/data/sport/play/ActionResult.java)
- [Player](../src/data/player/Player.java)
- [Team](../src/data/team/Team.java)

Utilisée par

- [Game](../src/data/sport/setup/Game.java)
- [GameSimulator](../src/process/GameSimulator.java)

### HealthStatus

📄 Fichier
[HealthStatus.java](../src/data/player/HealthStatus.java)

Rôle

Suit la fatigue, les blessures et la disponibilité d’un joueur.

Méthodes importantes

- `HealthStatus()`
- `getFatigue()`
- `isInjured()`

Classes utilisées

- [Injury](../src/data/player/Injury.java)
- [SimulationConfiguration](../src/config/SimulationConfiguration.java)

Utilisée par

- [Player](../src/data/player/Player.java)
- [GameSimulator](../src/process/GameSimulator.java)

### Income

📄 Fichier
[Income.java](../src/data/finance/budget/Income.java)

Rôle

Représente une ligne simple de revenu ajoutée à un budget.

Méthodes importantes

- `Income()`
- `getName()`
- `getAmount()`

Classes utilisées

- Aucune classe métier notable.

Utilisée par

- [Budget](../src/data/finance/budget/Budget.java)
- [LeagueFinance](../src/data/league/LeagueFinance.java)
- [FinanceManager](../src/process/FinanceManager.java)

### Injury

📄 Fichier
[Injury.java](../src/data/player/Injury.java)

Rôle

Décrit la nature et la durée d’indisponibilité d’une blessure.

Méthodes importantes

- `Injury()`
- `getInjuryType()`
- `getInjuryDuration()`

Classes utilisées

- Aucune classe métier notable.

Utilisée par

- [HealthStatus](../src/data/player/HealthStatus.java)
- [GameSimulator](../src/process/GameSimulator.java)

### League

📄 Fichier
[League.java](../src/data/league/League.java)

Rôle

Constitue l’objet racine du domaine en reliant conférences, saisons et finance.

Méthodes importantes

- `League()`
- `getWesternConference()`
- `getReagularSeason()`

Classes utilisées

- [Conference](../src/data/league/Conference.java)
- [RegularSeason](../src/data/league/RegularSeason.java)
- [Playoff](../src/data/league/Playoff.java)

Utilisée par

- [LeagueManager](../src/process/LeagueManager.java)
- [GameGenerator](../src/process/GameGenerator.java)
- [MainGui](../src/gui/frame/MainGui.java)

### LeagueFinance

📄 Fichier
[LeagueFinance.java](../src/data/league/LeagueFinance.java)

Rôle

Porte les valeurs financières globales de la ligue et son budget central.

Méthodes importantes

- `LeagueFinance()`

Classes utilisées

- [Budget](../src/data/finance/budget/Budget.java)
- [Income](../src/data/finance/budget/Income.java)
- [FinanceManager](../src/process/FinanceManager.java)

Utilisée par

- [League](../src/data/league/League.java)
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
- [FinanceManager](../src/process/FinanceManager.java)

### MarketSize

📄 Fichier
[MarketSize.java](../src/data/team/finance/MarketSize.java)

Rôle

Représente la taille de marché utilisée dans la finance d’une franchise.

Méthodes importantes

- `MarketSize()`
- `getSize()`
- `setSize()`

Classes utilisées

- Aucune classe métier notable.

Utilisée par

- [TeamFinance](../src/data/team/finance/TeamFinance.java)
- [TeamFactory](../src/process/factory/TeamFactory.java)

### NBACalendar

📄 Fichier
[NBACalendar.java](../src/data/calendar/NBACalendar.java)

Rôle

Stocke le calendrier complet d’une saison sous forme de dates et de journées.

Méthodes importantes

- `NBACalendar()`
- `getCalendar()`
- `setCalendar()`

Classes utilisées

- [GameDay](../src/data/calendar/GameDay.java)

Utilisée par

- [Season](../src/data/league/Season.java)

### OffensiveAction

📄 Fichier
[OffensiveAction.java](../src/data/sport/play/OffensiveAction.java)

Rôle

Code le type d’action offensive choisi pendant la simulation.

Méthodes importantes

- `OffensiveAction()`
- `getName()`

Classes utilisées

- Aucune classe métier notable.

Utilisée par

- [ActionResult](../src/data/sport/play/ActionResult.java)
- [GameSimulator](../src/process/GameSimulator.java)

### Player

📄 Fichier
[Player.java](../src/data/player/Player.java)

Rôle

Représente un joueur avec son identité, son salaire, ses statistiques et sa santé.

Méthodes importantes

- `Player()`
- `getName()`
- `getPreSeasonAssets()`

Classes utilisées

- [Asset](../src/data/player/Asset.java)
- [HealthStatus](../src/data/player/HealthStatus.java)

Utilisée par

- [Team](../src/data/team/Team.java)
- [GameSimulator](../src/process/GameSimulator.java)
- [GameResult](../src/data/sport/setup/GameResult.java)

### PlayerExchange

📄 Fichier
[PlayerExchange.java](../src/data/finance/transfer/PlayerExchange.java)

Rôle

Prépare un futur modèle d’échange de joueurs, sans logique active pour l’instant.

Méthodes importantes

- Aucune méthode publique marquante.

Classes utilisées

- Aucune classe métier notable.

Utilisée par

- Aucune utilisation directe identifiée.

### PlayerPurchase

📄 Fichier
[PlayerPurchase.java](../src/data/finance/transfer/PlayerPurchase.java)

Rôle

Prépare un futur modèle d’achat ou de transfert de joueur.

Méthodes importantes

- Aucune méthode publique marquante.

Classes utilisées

- Aucune classe métier notable.

Utilisée par

- Aucune utilisation directe identifiée.

### Playoff

📄 Fichier
[Playoff.java](../src/data/league/Playoff.java)

Rôle

Spécialise `Season` pour porter la phase finale du championnat.

Méthodes importantes

- `Playoff()`

Classes utilisées

- [Season](../src/data/league/Season.java)

Utilisée par

- [League](../src/data/league/League.java)

### PointScored

📄 Fichier
[PointScored.java](../src/data/sport/play/PointScored.java)

Rôle

Représente une action qui ajoute des points au score du match.

Méthodes importantes

- `PointScored()`
- `getPointsScored()`
- `getScorerPlayer()`

Classes utilisées

- [Player](../src/data/player/Player.java)
- [ActionResult](../src/data/sport/play/ActionResult.java)

Utilisée par

- [GameSimulator](../src/process/GameSimulator.java)

### Ranking

📄 Fichier
[Ranking.java](../src/data/league/Ranking.java)

Rôle

Stocke les indicateurs de classement associés à une équipe sur une saison.

Méthodes importantes

- `Ranking()`

Classes utilisées

- [Team](../src/data/team/Team.java)

Utilisée par

- [Season](../src/data/league/Season.java)

### Rebound

📄 Fichier
[Rebound.java](../src/data/sport/play/Rebound.java)

Rôle

Représente la récupération d’un ballon après un tir manqué.

Méthodes importantes

- `Rebound()`
- `getReboundPlayer()`
- `getMissedPlayer()`

Classes utilisées

- [Player](../src/data/player/Player.java)
- [ActionResult](../src/data/sport/play/ActionResult.java)

Utilisée par

- [GameSimulator](../src/process/GameSimulator.java)

### RegularSeason

📄 Fichier
[RegularSeason.java](../src/data/league/RegularSeason.java)

Rôle

Spécialise `Season` pour la saison régulière et son calendrier quotidien.

Méthodes importantes

- `RegularSeason()`

Classes utilisées

- [Season](../src/data/league/Season.java)

Utilisée par

- [League](../src/data/league/League.java)
- [CalendarBuilder](../src/process/builder/CalendarBuilder.java)
- [GameManager](../src/process/GameManager.java)

### Schedule

📄 Fichier
[Schedule.java](../src/data/team/calendar/Schedule.java)

Rôle

Porte les matchs d’une équipe et leurs compteurs de programmation.

Méthodes importantes

- `Schedule()`
- `getGames()`
- `addGame()`

Classes utilisées

- [Game](../src/data/sport/setup/Game.java)

Utilisée par

- [Team](../src/data/team/Team.java)
- [CalendarBuilder](../src/process/builder/CalendarBuilder.java)

### Season

📄 Fichier
[Season.java](../src/data/league/Season.java)

Rôle

Définit les dates, le calendrier et les événements spéciaux d’une saison.

Méthodes importantes

- `Season()`
- `getDebutDate()`
- `getCalendar()`

Classes utilisées

- [NBACalendar](../src/data/calendar/NBACalendar.java)
- [SpecialEvent](../src/data/calendar/SpecialEvent.java)
- [Ranking](../src/data/league/Ranking.java)

Utilisée par

- [RegularSeason](../src/data/league/RegularSeason.java)
- [Playoff](../src/data/league/Playoff.java)
- [LeagueManager](../src/process/LeagueManager.java)

### SpecialEvent

📄 Fichier
[SpecialEvent.java](../src/data/calendar/SpecialEvent.java)

Rôle

Modélise une date marquante de la saison, comme Noël ou l’ouverture.

Méthodes importantes

- `SpecialEvent()`

Classes utilisées

- [GameDay](../src/data/calendar/GameDay.java)

Utilisée par

- [Season](../src/data/league/Season.java)
- [CalendarBuilder](../src/process/builder/CalendarBuilder.java)

### Team

📄 Fichier
[Team.java](../src/data/team/Team.java)

Rôle

Représente une franchise avec son effectif, son rival, son planning et sa finance.

Méthodes importantes

- `Team()`
- `getName()`
- `addPlayer()`

Classes utilisées

- [Player](../src/data/player/Player.java)
- [Schedule](../src/data/team/calendar/Schedule.java)
- [TeamFinance](../src/data/team/finance/TeamFinance.java)

Utilisée par

- [Conference](../src/data/league/Conference.java)
- [League](../src/data/league/League.java)
- [GameSimulator](../src/process/GameSimulator.java)

### TeamFinance

📄 Fichier
[TeamFinance.java](../src/data/team/finance/TeamFinance.java)

Rôle

Regroupe le budget d’équipe, le profil financier et la taille de marché.

Méthodes importantes

- `TeamFinance()`
- `getFinancialProfil()`

Classes utilisées

- [Budget](../src/data/finance/budget/Budget.java)
- [FinancialProfil](../src/data/team/finance/FinancialProfil.java)
- [MarketSize](../src/data/team/finance/MarketSize.java)

Utilisée par

- [Team](../src/data/team/Team.java)
- [TeamFactory](../src/process/factory/TeamFactory.java)

### Turnover

📄 Fichier
[Turnover.java](../src/data/sport/play/Turnover.java)

Rôle

Représente une perte de balle ou une interception créée par la défense.

Méthodes importantes

- `Turnover()`
- `getInterceptedPlayer()`
- `getDefensePlayer()`

Classes utilisées

- [Player](../src/data/player/Player.java)
- [ActionResult](../src/data/sport/play/ActionResult.java)

Utilisée par

- [GameSimulator](../src/process/GameSimulator.java)

## PROCESS

### CalendarBuilder

📄 Fichier
[CalendarBuilder.java](../src/process/builder/CalendarBuilder.java)

Rôle

Construit le calendrier régulier en plaçant événements et matchs sur les dates disponibles.

Méthodes importantes

- `initialization()`
- `generateAllGames()`
- `generateRegulaSeasonCalendar()`

Classes utilisées

- [League](../src/data/league/League.java)
- [RegularSeason](../src/data/league/RegularSeason.java)
- [GameGenerator](../src/process/GameGenerator.java)

Utilisée par

- [LeagueManager](../src/process/LeagueManager.java)

### CurrentSeasonAssetRepositery

📄 Fichier
[CurrentSeasonAssetRepositery.java](../src/process/repositery/CurrentSeasonAssetRepositery.java)

Rôle

Expose un registre partagé des statistiques de saison en cours par joueur.

Méthodes importantes

- `getInstance()`
- `register()`
- `getCurrentSeasonAsset()`

Classes utilisées

- [Asset](../src/data/player/Asset.java)
- [Player](../src/data/player/Player.java)

Utilisée par

- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
- [GameSimulator](../src/process/GameSimulator.java)

### DivisionRepositery

📄 Fichier
[DivisionRepositery.java](../src/process/repositery/DivisionRepositery.java)

Rôle

Expose un registre partagé pour retrouver rapidement les divisions déjà créées.

Méthodes importantes

- `getInstance()`
- `register()`
- `getDivision()`

Classes utilisées

- [Division](../src/data/league/Division.java)
- [Team](../src/data/team/Team.java)

Utilisée par

- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)

### FinanceBuilder

📄 Fichier
[FinanceBuilder.java](../src/process/builder/FinanceBuilder.java)

Rôle

Doit porter la construction financière, mais reste encore très peu implémenté.

Méthodes importantes

- Aucune méthode publique marquante.

Classes utilisées

- [TeamRepositery](../src/process/repositery/TeamRepositery.java)

Utilisée par

- Aucune utilisation directe identifiée.

### FinanceManager

📄 Fichier
[FinanceManager.java](../src/process/FinanceManager.java)

Rôle

Regroupe les calculs utilitaires liés aux salaires et à la mise à jour des budgets.

Méthodes importantes

- `getAverageSalary()`
- `updateBudget()`

Classes utilisées

- [Budget](../src/data/finance/budget/Budget.java)
- [LeagueFinance](../src/data/league/LeagueFinance.java)
- [Team](../src/data/team/Team.java)

Utilisée par

- [LeagueFinance](../src/data/league/LeagueFinance.java)
- [GameSimulator](../src/process/GameSimulator.java)

### GameGenerator

📄 Fichier
[GameGenerator.java](../src/process/GameGenerator.java)

Rôle

Génère les affiches de saison à partir des divisions, conférences et rivalités.

Méthodes importantes

- `generateIntraDivision()`
- `generateIntraConference()`
- `generateInterConference()`

Classes utilisées

- [League](../src/data/league/League.java)
- [Division](../src/data/league/Division.java)
- [Game](../src/data/sport/setup/Game.java)

Utilisée par

- [CalendarBuilder](../src/process/builder/CalendarBuilder.java)

### GameManager

📄 Fichier
[GameManager.java](../src/process/GameManager.java)

Rôle

Centralise les règles utilitaires de calendrier, d’importance et de rivalité.

Méthodes importantes

- `isWeekend()`
- `isImportantDay()`
- `isSpecialEvent()`

Classes utilisées

- [League](../src/data/league/League.java)
- [RegularSeason](../src/data/league/RegularSeason.java)
- [Game](../src/data/sport/setup/Game.java)

Utilisée par

- [GameContext](../src/data/sport/setup/GameContext.java)
- [GameGenerator](../src/process/GameGenerator.java)
- [CalendarBuilder](../src/process/builder/CalendarBuilder.java)

### GameSimulator

📄 Fichier
[GameSimulator.java](../src/process/GameSimulator.java)

Rôle

Simule le déroulement d’un match en produisant actions, score, fatigue et statistiques.

Méthodes importantes

- `simulateGame()`

Classes utilisées

- [Game](../src/data/sport/setup/Game.java)
- [GameResult](../src/data/sport/setup/GameResult.java)
- [Player](../src/data/player/Player.java)

Utilisée par

- [LeagueManager](../src/process/LeagueManager.java)

### LeagueBuilder

📄 Fichier
[LeagueBuilder.java](../src/process/builder/LeagueBuilder.java)

Rôle

Charge les données CSV et instancie la ligue, les équipes, les joueurs et les registres.

Méthodes importantes

- `LeagueBuilder()`
- `build()`

Classes utilisées

- [League](../src/data/league/League.java)
- [PlayerFactory](../src/process/factory/PlayerFactory.java)
- [TeamFactory](../src/process/factory/TeamFactory.java)

Utilisée par

- [LeagueManager](../src/process/LeagueManager.java)

### LeagueManager

📄 Fichier
[LeagueManager.java](../src/process/LeagueManager.java)

Rôle

Orchestre la construction de la ligue, du calendrier et la simulation jour par jour.

Méthodes importantes

- `LeagueManager()`
- `buildLeague()`
- `simulateDay()`

Classes utilisées

- [League](../src/data/league/League.java)
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
- [CalendarBuilder](../src/process/builder/CalendarBuilder.java)

Utilisée par

- Aucune utilisation directe identifiée.

### PlayerFactory

📄 Fichier
[PlayerFactory.java](../src/process/factory/PlayerFactory.java)

Rôle

Fabrique un objet `Player` à partir d’une ligne de données source.

Méthodes importantes

- `createPlayer()`

Classes utilisées

- [Player](../src/data/player/Player.java)
- [Asset](../src/data/player/Asset.java)

Utilisée par

- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)

### PlayerRepositery

📄 Fichier
[PlayerRepositery.java](../src/process/repositery/PlayerRepositery.java)

Rôle

Expose un registre partagé des joueurs chargés dans la ligue.

Méthodes importantes

- `getInstance()`
- `register()`
- `getAllPlayers()`

Classes utilisées

- [Player](../src/data/player/Player.java)

Utilisée par

- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
- [GameSimulator](../src/process/GameSimulator.java)

### PreSeasonAssetRepositery

📄 Fichier
[PreSeasonAssetRepositery.java](../src/process/repositery/PreSeasonAssetRepositery.java)

Rôle

Expose un registre partagé des statistiques de présaison des joueurs.

Méthodes importantes

- `getInstance()`
- `register()`
- `getPreSeasonAsset()`

Classes utilisées

- [Player](../src/data/player/Player.java)
- [Asset](../src/data/player/Asset.java)

Utilisée par

- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
- [GameSimulator](../src/process/GameSimulator.java)

### TeamFactory

📄 Fichier
[TeamFactory.java](../src/process/factory/TeamFactory.java)

Rôle

Fabrique une équipe complète avec son profil financier et sa structure économique.

Méthodes importantes

- `createTeam()`

Classes utilisées

- [Team](../src/data/team/Team.java)
- [TeamFinance](../src/data/team/finance/TeamFinance.java)
- [Budget](../src/data/finance/budget/Budget.java)

Utilisée par

- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)

### TeamRepositery

📄 Fichier
[TeamRepositery.java](../src/process/repositery/TeamRepositery.java)

Rôle

Expose un registre partagé des équipes chargées par le builder.

Méthodes importantes

- `getInstance()`
- `register()`
- `getAllTeams()`

Classes utilisées

- [Team](../src/data/team/Team.java)

Utilisée par

- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
- [FinanceBuilder](../src/process/builder/FinanceBuilder.java)

## GUI

### BuildBox

📄 Fichier
[BuildBox.java](../src/gui/components/BuildBox.java)

Rôle

Construit un bloc visuel réutilisable employé dans plusieurs écrans.

Méthodes importantes

- `BuildBox()`

Classes utilisées

- [DashboardCard](../src/gui/components/DashboardCard.java)
- [SectionTitle](../src/gui/components/SectionTitle.java)

Utilisée par

- [OpeningDashboard](../src/gui/dashboard/OpeningDashboard.java)
- [MatchDashboard](../src/gui/dashboard/MatchDashboard.java)
- [MapDashboard](../src/gui/dashboard/MapDashboard.java)

### CalendarDashboard

📄 Fichier
[CalendarDashboard.java](../src/gui/dashboard/CalendarDashboard.java)

Rôle

Affiche la vue calendrier de l’application graphique.

Méthodes importantes

- `CalendarDashboard()`

Classes utilisées

- [BuildBox](../src/gui/components/BuildBox.java)
- [SectionTitle](../src/gui/components/SectionTitle.java)

Utilisée par

- [MainGui](../src/gui/frame/MainGui.java)

### DashboardCard

📄 Fichier
[DashboardCard.java](../src/gui/components/DashboardCard.java)

Rôle

Fournit une carte Swing de base pour présenter du contenu de dashboard.

Méthodes importantes

- `DashboardCard()`

Classes utilisées

- Aucune classe métier notable.

Utilisée par

- [BuildBox](../src/gui/components/BuildBox.java)
- [TitledCard](../src/gui/components/TitledCard.java)

### FinanceDashboard

📄 Fichier
[FinanceDashboard.java](../src/gui/dashboard/FinanceDashboard.java)

Rôle

Affiche la vue finance de l’application graphique.

Méthodes importantes

- `FinanceDashboard()`

Classes utilisées

- [BuildBox](../src/gui/components/BuildBox.java)
- [SectionTitle](../src/gui/components/SectionTitle.java)

Utilisée par

- [MainGui](../src/gui/frame/MainGui.java)

### MainGui

📄 Fichier
[MainGui.java](../src/gui/frame/MainGui.java)

Rôle

Initialise la fenêtre principale et orchestre la navigation entre ouverture et dashboards.

Méthodes importantes

- `MainGui()`
- `actionPerformed()`

Classes utilisées

- [OpeningDashboard](../src/gui/dashboard/OpeningDashboard.java)
- [SidebarPanel](../src/gui/layout/SidebarPanel.java)
- [MatchDashboard](../src/gui/dashboard/MatchDashboard.java)

Utilisée par

- Aucune classe hors `src/test` identifiée.

### MapDashboard

📄 Fichier
[MapDashboard.java](../src/gui/dashboard/MapDashboard.java)

Rôle

Affiche la vue carte des franchises dans l’interface.

Méthodes importantes

- `MapDashboard()`

Classes utilisées

- [BuildBox](../src/gui/components/BuildBox.java)
- [SectionTitle](../src/gui/components/SectionTitle.java)

Utilisée par

- [MainGui](../src/gui/frame/MainGui.java)

### MatchDashboard

📄 Fichier
[MatchDashboard.java](../src/gui/dashboard/MatchDashboard.java)

Rôle

Affiche la vue dédiée aux matchs et à leur suivi.

Méthodes importantes

- `MatchDashboard()`

Classes utilisées

- [BuildBox](../src/gui/components/BuildBox.java)
- [SectionTitle](../src/gui/components/SectionTitle.java)

Utilisée par

- [MainGui](../src/gui/frame/MainGui.java)

### OpeningDashboard

📄 Fichier
[OpeningDashboard.java](../src/gui/dashboard/OpeningDashboard.java)

Rôle

Présente l’écran initial avant l’accès au reste de l’application.

Méthodes importantes

- `OpeningDashboard()`
- `getContinueButton()`
- `hasSelectedProfil()`

Classes utilisées

- [BuildBox](../src/gui/components/BuildBox.java)
- [SectionTitle](../src/gui/components/SectionTitle.java)

Utilisée par

- [MainGui](../src/gui/frame/MainGui.java)

### RankingDashboard

📄 Fichier
[RankingDashboard.java](../src/gui/dashboard/RankingDashboard.java)

Rôle

Affiche la vue classement de l’application.

Méthodes importantes

- `RankingDashboard()`

Classes utilisées

- [BuildBox](../src/gui/components/BuildBox.java)
- [SectionTitle](../src/gui/components/SectionTitle.java)

Utilisée par

- [MainGui](../src/gui/frame/MainGui.java)

### SectionTitle

📄 Fichier
[SectionTitle.java](../src/gui/components/SectionTitle.java)

Rôle

Affiche un titre et un sous-titre réutilisables dans l’interface Swing.

Méthodes importantes

- `SectionTitle()`

Classes utilisées

- Aucune classe métier notable.

Utilisée par

- [BuildBox](../src/gui/components/BuildBox.java)
- [OpeningDashboard](../src/gui/dashboard/OpeningDashboard.java)
- [FinanceDashboard](../src/gui/dashboard/FinanceDashboard.java)

### SidebarPanel

📄 Fichier
[SidebarPanel.java](../src/gui/layout/SidebarPanel.java)

Rôle

Construit la barre latérale et gère la mise en avant de l’onglet actif.

Méthodes importantes

- `SidebarPanel()`
- `getMatchButton()`
- `getExitButton()`

Classes utilisées

- Aucune dépendance métier notable.

Utilisée par

- [MainGui](../src/gui/frame/MainGui.java)

### TitledCard

📄 Fichier
[TitledCard.java](../src/gui/components/TitledCard.java)

Rôle

Assemble une carte graphique avec un titre standardisé.

Méthodes importantes

- `TitledCard()`

Classes utilisées

- [DashboardCard](../src/gui/components/DashboardCard.java)
- [SectionTitle](../src/gui/components/SectionTitle.java)

Utilisée par

- Aucune utilisation directe identifiée.
