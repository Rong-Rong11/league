# Classes

## Table des matières

### CONFIG

Configuration globale des règles et constantes du projet.

**CONFIG**

- [FinanceConfiguration](#financeconfiguration) — constantes financières de la ligue
- [FinancialPolicy](#financialpolicy) — règles de politique financière
- [SimulationConfiguration](#simulationconfiguration) — paramètres généraux de simulation

### DATA

Modèle métier de la ligue, des équipes, des joueurs, des matchs et de la finance.

**CALENDAR**

- [GameDay](#gameday) — élément du calendrier
- [NBACalendar](#nbacalendar) — élément du calendrier
- [SpecialEvent](#specialevent) — élément du calendrier
**FINANCE**

- [GameStat](#gamestat) — donnée financière
- [TeamGameFinance](#teamgamefinance) — donnée financière
**FINANCE/BUDGET**

- [Budget](#budget) — élément de budget
- [Expense](#expense) — élément de budget
- [Income](#income) — élément de budget
**FINANCE/TRANSFER**

- [Trade](#trade) — modèle de transfert
**LEAGUE**

- [Conference](#conference) — élément du modèle de ligue
- [Division](#division) — élément du modèle de ligue
- [League](#league) — élément du modèle de ligue
- [LeagueFinance](#leaguefinance) — élément du modèle de ligue
- [Playoff](#playoff) — élément du modèle de ligue
- [Ranking](#ranking) — élément du modèle de ligue
- [RegularSeason](#regularseason) — élément du modèle de ligue
- [Season](#season) — élément du modèle de ligue
**PLAYER**

- [Asset](#asset) — élément du modèle joueur
- [HealthStatus](#healthstatus) — élément du modèle joueur
- [Injury](#injury) — élément du modèle joueur
- [Player](#player) — élément du modèle joueur
**SPORT/PLAY**

- [OffensiveTry](#offensivetry) — tentative offensive
**SPORT/PLAY/ACTION**

- [ActionResult](#actionresult) — action de match
- [Block](#block) — action de match
- [EndOfTime](#endoftime) — action de match
- [PointScored](#pointscored) — action de match
- [Rebound](#rebound) — action de match
- [Turnover](#turnover) — action de match
**SPORT/SETUP**

- [Game](#game) — contexte ou résultat de rencontre
- [GameContext](#gamecontext) — contexte ou résultat de rencontre
- [GameResult](#gameresult) — contexte ou résultat de rencontre
**TEAM**

- [Stadium](#stadium) — élément du modèle équipe
- [Team](#team) — élément du modèle équipe
- [TeamPerformance](#teamperformance) — élément du modèle équipe
**TEAM/CALENDAR**

- [Schedule](#schedule) — élément du calendrier
**TEAM/FINANCE**

- [TeamFinance](#teamfinance) — finance d’équipe
**TEAM/FINANCE/FINANCIALPROFIL**

- [AmbitiousProfil](#ambitiousprofil) — profil financier de franchise
- [BalancedProfil](#balancedprofil) — profil financier de franchise
- [EconomicalProfil](#economicalprofil) — profil financier de franchise
- [FinancialProfil](#financialprofil) — profil financier de franchise
**TEAM/FINANCE/MARKETSIZE**

- [LargeSize](#largesize) — taille de marché de franchise
- [MarketSize](#marketsize) — taille de marché de franchise
- [MediumSize](#mediumsize) — taille de marché de franchise
- [SmallSize](#smallsize) — taille de marché de franchise
**TEAM/FINANCE/TRANSFER**

- [AllIn](#allin) — modèle de transfert
- [Balanced](#balanced) — modèle de transfert
- [Rebuild](#rebuild) — modèle de transfert
- [SalaryDump](#salarydump) — modèle de transfert
- [SmallAdjust](#smalladjust) — modèle de transfert
- [SuperstarBuild](#superstarbuild) — modèle de transfert
- [TeamTransferStrategy](#teamtransferstrategy) — modèle de transfert

### PROCESS

Logique applicative de construction, simulation, calcul et orchestration.

**BUILDER**

- [CalendarBuilder](#calendarbuilder) — construction et initialisation
- [LeagueBuilder](#leaguebuilder) — construction et initialisation
**BUILDER/CALENDARTOOLS**

- [GameGenerator](#gamegenerator) — outil de génération du calendrier
- [GameSelector](#gameselector) — outil de génération du calendrier
- [ScheduleReset](#schedulereset) — outil de génération du calendrier
- [SpecialEventPlanner](#specialeventplanner) — outil de génération du calendrier
**FACTORY**

- [PlayerFactory](#playerfactory) — création d’objets métier
- [TeamFactory](#teamfactory) — création d’objets métier
**MANAGER**

- [FinanceManager](#financemanager) — orchestration métier
- [GameManager](#gamemanager) — orchestration métier
- [LeagueManager](#leaguemanager) — orchestration métier
- [RevenueSharingManager](#revenuesharingmanager) — orchestration métier
- [SimulationManager](#simulationmanager) — orchestration métier
- [TradeManager](#trademanager) — orchestration métier
**REPOSITERY**

- [CurrentSeasonAssetRepositery](#currentseasonassetrepositery) — registre partagé en mémoire
- [DivisionRepositery](#divisionrepositery) — registre partagé en mémoire
- [PlayerRepositery](#playerrepositery) — registre partagé en mémoire
- [PreSeasonAssetRepositery](#preseasonassetrepositery) — registre partagé en mémoire
- [TeamRepositery](#teamrepositery) — registre partagé en mémoire
**SIMULATOR**

- [GameExpenseSimulator](#gameexpensesimulator) — simulation métier
- [GameRevenueSimulator](#gamerevenuesimulator) — simulation métier
- [GameSimulator](#gamesimulator) — simulation métier
- [TradeSimulator](#tradesimulator) — simulation métier
**UTILITARY**

- [CalendarUtilitary](#calendarutilitary) — fonctions utilitaires métier
- [FinanceUtilitary](#financeutilitary) — fonctions utilitaires métier
- [PlayerUtilitary](#playerutilitary) — fonctions utilitaires métier
- [TeamUtilitary](#teamutilitary) — fonctions utilitaires métier
- [TransferStrategyUtilitary](#transferstrategyutilitary) — fonctions utilitaires métier
**VISITOR/ACTIONRESULT**

- [ActionResultVisitor](#actionresultvisitor) — visitor de résultat de jeu
- [AssetUpdateVisitor](#assetupdatevisitor) — visitor de résultat de jeu
- [GameResultVisitor](#gameresultvisitor) — visitor de résultat de jeu
**VISITOR/MARKETSIZE**

- [CalculateBaseTicketVisitor](#calculatebaseticketvisitor) — visitor de taille de marché
- [CalculateInitialTeamBudgetVisitor](#calculateinitialteambudgetvisitor) — visitor de taille de marché
- [CalculateStadiumCostVisitor](#calculatestadiumcostvisitor) — visitor de taille de marché
- [GenerateStadiumCapacityVisitor](#generatestadiumcapacityvisitor) — visitor de taille de marché
- [MarketSizeVisitor](#marketsizevisitor) — visitor de taille de marché
**VISITOR/TEAMTRANSFER**

- [EvaluateSeasonIntentVisitor](#evaluateseasonintentvisitor) — visitor de stratégie de transfert
- [PreSeasonPlayerToTradeVisitor](#preseasonplayertotradevisitor) — visitor de stratégie de transfert
- [PreSeasonTradeSatisfactionVisitor](#preseasontradesatisfactionvisitor) — visitor de stratégie de transfert
- [SeasonPlayerToTradeVisitor](#seasonplayertotradevisitor) — visitor de stratégie de transfert
- [SeasonTradeSatisfactionVisitor](#seasontradesatisfactionvisitor) — visitor de stratégie de transfert
- [TeamTransferVisitor](#teamtransfervisitor) — visitor de stratégie de transfert

### GUI

Interface Swing, dashboards et composants de présentation.

**COMPONENTS**

- [BuildBox](#buildbox) — bloc visuel réutilisable
- [DashboardCard](#dashboardcard) — carte graphique réutilisable
- [SectionTitle](#sectiontitle) — composant Swing réutilisable
- [TitledCard](#titledcard) — carte graphique réutilisable
**DASHBOARD**

- [CalendarDashboard](#calendardashboard) — vue Swing calendar
- [FinanceDashboard](#financedashboard) — vue Swing finance
- [MapDashboard](#mapdashboard) — vue Swing map
- [MatchDashboard](#matchdashboard) — vue Swing match
- [OpeningDashboard](#openingdashboard) — vue Swing opening
- [RankingDashboard](#rankingdashboard) — vue Swing ranking
**FRAME**

- [MainGui](#maingui) — fenêtre principale Swing
**LAYOUT**

- [SidebarPanel](#sidebarpanel) — navigation latérale

## CONFIG

### FinanceConfiguration

📄 Fichier
[src/config/FinanceConfiguration.java](../src/config/FinanceConfiguration.java)

Rôle
Regroupe des constantes ou règles transverses utilisées dans plusieurs couches du projet.

Méthodes importantes
- Aucune méthode publique marquante.

Classes utilisées
- [SimulationConfiguration](../src/config/SimulationConfiguration.java)

Utilisée par
- [LeagueFinance](../src/data/league/LeagueFinance.java)
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
- [TeamFactory](../src/process/factory/TeamFactory.java)
- [FinanceManager](../src/process/manager/FinanceManager.java)

### FinancialPolicy

📄 Fichier
[src/config/FinancialPolicy.java](../src/config/FinancialPolicy.java)

Rôle
Expose les règles communes de politique financière appliquées aux franchises.

Méthodes importantes
- Aucune méthode publique marquante.

Classes utilisées
- Aucune dépendance métier notable.

Utilisée par
- [TradeManager](../src/process/manager/TradeManager.java)
- [TradeSimulator](../src/process/simulator/TradeSimulator.java)
- [TeamUtilitary](../src/process/utilitary/TeamUtilitary.java)
- [TransferStrategyUtilitary](../src/process/utilitary/TransferStrategyUtilitary.java)

### SimulationConfiguration

📄 Fichier
[src/config/SimulationConfiguration.java](../src/config/SimulationConfiguration.java)

Rôle
Regroupe des constantes ou règles transverses utilisées dans plusieurs couches du projet.

Méthodes importantes
- Aucune méthode publique marquante.

Classes utilisées
- Aucune dépendance métier notable.

Utilisée par
- [FinanceConfiguration](../src/config/FinanceConfiguration.java)
- [League](../src/data/league/League.java)
- [LeagueFinance](../src/data/league/LeagueFinance.java)
- [HealthStatus](../src/data/player/HealthStatus.java)

## DATA

### GameDay

📄 Fichier
[src/data/calendar/GameDay.java](../src/data/calendar/GameDay.java)

Rôle
Modèle une portion du calendrier NBA, des journées de match ou des événements spéciaux.

Méthodes importantes
- `GameDay()`
- `getDate()`
- `isEmpty()`

Classes utilisées
- [Game](../src/data/sport/setup/Game.java)

Utilisée par
- [NBACalendar](../src/data/calendar/NBACalendar.java)
- [SpecialEvent](../src/data/calendar/SpecialEvent.java)
- [CalendarBuilder](../src/process/builder/CalendarBuilder.java)
- [GameManager](../src/process/manager/GameManager.java)

### NBACalendar

📄 Fichier
[src/data/calendar/NBACalendar.java](../src/data/calendar/NBACalendar.java)

Rôle
Modèle une portion du calendrier NBA, des journées de match ou des événements spéciaux.

Méthodes importantes
- `NBACalendar()`
- `getCalendar()`
- `setCalendar()`

Classes utilisées
- [GameDay](../src/data/calendar/GameDay.java)

Utilisée par
- [Season](../src/data/league/Season.java)

### SpecialEvent

📄 Fichier
[src/data/calendar/SpecialEvent.java](../src/data/calendar/SpecialEvent.java)

Rôle
Modèle une portion du calendrier NBA, des journées de match ou des événements spéciaux.

Méthodes importantes
- `SpecialEvent()`

Classes utilisées
- [GameDay](../src/data/calendar/GameDay.java)

Utilisée par
- [Season](../src/data/league/Season.java)
- [SpecialEventPlanner](../src/process/builder/calendartools/SpecialEventPlanner.java)

### GameStat

📄 Fichier
[src/data/finance/GameStat.java](../src/data/finance/GameStat.java)

Rôle
Stocke des données financières attachées à un match, une équipe ou la ligue.

Méthodes importantes
- `GameStat()`
- `getGame()`
- `setGame()`

Classes utilisées
- [TeamGameFinance](../src/data/finance/TeamGameFinance.java)
- [Game](../src/data/sport/setup/Game.java)

Utilisée par
- [FinanceManager](../src/process/manager/FinanceManager.java)
- [GameExpenseSimulator](../src/process/simulator/GameExpenseSimulator.java)
- [GameRevenueSimulator](../src/process/simulator/GameRevenueSimulator.java)
- [FinanceUtilitary](../src/process/utilitary/FinanceUtilitary.java)

### TeamGameFinance

📄 Fichier
[src/data/finance/TeamGameFinance.java](../src/data/finance/TeamGameFinance.java)

Rôle
Stocke des données financières attachées à un match, une équipe ou la ligue.

Méthodes importantes
- `TeamGameFinance()`
- `getTicketRevenue()`
- `setTicketRevenue()`

Classes utilisées
- Aucune dépendance métier notable.

Utilisée par
- [GameStat](../src/data/finance/GameStat.java)
- [FinanceUtilitary](../src/process/utilitary/FinanceUtilitary.java)

### Budget

📄 Fichier
[src/data/finance/budget/Budget.java](../src/data/finance/budget/Budget.java)

Rôle
Représente un élément comptable manipulé par les calculs de budget de la ligue ou des équipes.

Méthodes importantes
- `Budget()`
- `getInitialAmount()`
- `setInitialAmount()`

Classes utilisées
- [Income](../src/data/finance/budget/Income.java)
- [Expense](../src/data/finance/budget/Expense.java)

Utilisée par
- [LeagueFinance](../src/data/league/LeagueFinance.java)
- [TeamFinance](../src/data/team/finance/TeamFinance.java)
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
- [TeamFactory](../src/process/factory/TeamFactory.java)

### Expense

📄 Fichier
[src/data/finance/budget/Expense.java](../src/data/finance/budget/Expense.java)

Rôle
Représente un élément comptable manipulé par les calculs de budget de la ligue ou des équipes.

Méthodes importantes
- `Expense()`
- `getName()`
- `getAmount()`

Classes utilisées
- Aucune dépendance métier notable.

Utilisée par
- [Budget](../src/data/finance/budget/Budget.java)
- [RevenueSharingManager](../src/process/manager/RevenueSharingManager.java)
- [FinanceUtilitary](../src/process/utilitary/FinanceUtilitary.java)

### Income

📄 Fichier
[src/data/finance/budget/Income.java](../src/data/finance/budget/Income.java)

Rôle
Représente un élément comptable manipulé par les calculs de budget de la ligue ou des équipes.

Méthodes importantes
- `Income()`
- `getName()`
- `getAmount()`

Classes utilisées
- Aucune dépendance métier notable.

Utilisée par
- [Budget](../src/data/finance/budget/Budget.java)
- [LeagueFinance](../src/data/league/LeagueFinance.java)
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
- [FinanceManager](../src/process/manager/FinanceManager.java)

### Trade

📄 Fichier
[src/data/finance/transfer/Trade.java](../src/data/finance/transfer/Trade.java)

Rôle
Décrit un échange entre équipes avec les joueurs et franchises concernés par l’opération.

Méthodes importantes
- `Trade()`
- `getPlayerA()`
- `setPlayerA()`

Classes utilisées
- [Player](../src/data/player/Player.java)
- [Team](../src/data/team/Team.java)

Utilisée par
- [TradeManager](../src/process/manager/TradeManager.java)

### Conference

📄 Fichier
[src/data/league/Conference.java](../src/data/league/Conference.java)

Rôle
Représente un objet central du modèle de ligue: conférence, division, saison ou classement.

Méthodes importantes
- `Conference()`
- `getDivisions()`
- `setDivisions()`

Classes utilisées
- [Division](../src/data/league/Division.java)
- [Team](../src/data/team/Team.java)

Utilisée par
- [League](../src/data/league/League.java)
- [GameGenerator](../src/process/builder/calendartools/GameGenerator.java)

### Division

📄 Fichier
[src/data/league/Division.java](../src/data/league/Division.java)

Rôle
Représente un objet central du modèle de ligue: conférence, division, saison ou classement.

Méthodes importantes
- `Division()`
- `getTeams()`
- `setTeams()`

Classes utilisées
- [Team](../src/data/team/Team.java)

Utilisée par
- [Conference](../src/data/league/Conference.java)
- [League](../src/data/league/League.java)
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
- [GameGenerator](../src/process/builder/calendartools/GameGenerator.java)

### League

📄 Fichier
[src/data/league/League.java](../src/data/league/League.java)

Rôle
Agrège l’état principal de la ligue: conférences, saisons, calendrier global et finance commune.

Méthodes importantes
- `League()`
- `getWesternConference()`
- `setWesternConference()`

Classes utilisées
- [Conference](../src/data/league/Conference.java)
- [Team](../src/data/team/Team.java)
- [SimulationConfiguration](../src/config/SimulationConfiguration.java)
- [Player](../src/data/player/Player.java)

Utilisée par
- [MainGui](../src/gui/frame/MainGui.java)
- [SidebarPanel](../src/gui/layout/SidebarPanel.java)
- [CalendarBuilder](../src/process/builder/CalendarBuilder.java)
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)

### LeagueFinance

📄 Fichier
[src/data/league/LeagueFinance.java](../src/data/league/LeagueFinance.java)

Rôle
Représente un objet central du modèle de ligue: conférence, division, saison ou classement.

Méthodes importantes
- `LeagueFinance()`
- `getBudget()`
- `getSalaryCap()`

Classes utilisées
- [Budget](../src/data/finance/budget/Budget.java)
- [FinanceConfiguration](../src/config/FinanceConfiguration.java)
- [SimulationConfiguration](../src/config/SimulationConfiguration.java)
- [Income](../src/data/finance/budget/Income.java)

Utilisée par
- [League](../src/data/league/League.java)
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
- [TradeSimulator](../src/process/simulator/TradeSimulator.java)

### Playoff

📄 Fichier
[src/data/league/Playoff.java](../src/data/league/Playoff.java)

Rôle
Représente un objet central du modèle de ligue: conférence, division, saison ou classement.

Méthodes importantes
- `Playoff()`

Classes utilisées
- [Season](../src/data/league/Season.java)

Utilisée par
- [League](../src/data/league/League.java)
- [GameManager](../src/process/manager/GameManager.java)

### Ranking

📄 Fichier
[src/data/league/Ranking.java](../src/data/league/Ranking.java)

Rôle
Représente un objet central du modèle de ligue: conférence, division, saison ou classement.

Méthodes importantes
- `Ranking()`

Classes utilisées
- [Team](../src/data/team/Team.java)

Utilisée par
- [Season](../src/data/league/Season.java)

### RegularSeason

📄 Fichier
[src/data/league/RegularSeason.java](../src/data/league/RegularSeason.java)

Rôle
Représente un objet central du modèle de ligue: conférence, division, saison ou classement.

Méthodes importantes
- `RegularSeason()`

Classes utilisées
- [Season](../src/data/league/Season.java)

Utilisée par
- [League](../src/data/league/League.java)
- [CalendarBuilder](../src/process/builder/CalendarBuilder.java)
- [GameSelector](../src/process/builder/calendartools/GameSelector.java)
- [SpecialEventPlanner](../src/process/builder/calendartools/SpecialEventPlanner.java)

### Season

📄 Fichier
[src/data/league/Season.java](../src/data/league/Season.java)

Rôle
Représente un objet central du modèle de ligue: conférence, division, saison ou classement.

Méthodes importantes
- `Season()`
- `getDebutDate()`
- `setDebutDate()`

Classes utilisées
- [NBACalendar](../src/data/calendar/NBACalendar.java)
- [SpecialEvent](../src/data/calendar/SpecialEvent.java)
- [Ranking](../src/data/league/Ranking.java)

Utilisée par
- [Playoff](../src/data/league/Playoff.java)
- [RegularSeason](../src/data/league/RegularSeason.java)

### Asset

📄 Fichier
[src/data/player/Asset.java](../src/data/player/Asset.java)

Rôle
Décrit un joueur ou son état sportif, médical et statistique dans la simulation.

Méthodes importantes
- `Asset()`
- `getNote()`
- `setNote()`

Classes utilisées
- Aucune dépendance métier notable.

Utilisée par
- [Player](../src/data/player/Player.java)
- [PlayerFactory](../src/process/factory/PlayerFactory.java)
- [CurrentSeasonAssetRepositery](../src/process/repositery/CurrentSeasonAssetRepositery.java)
- [PreSeasonAssetRepositery](../src/process/repositery/PreSeasonAssetRepositery.java)

### HealthStatus

📄 Fichier
[src/data/player/HealthStatus.java](../src/data/player/HealthStatus.java)

Rôle
Décrit un joueur ou son état sportif, médical et statistique dans la simulation.

Méthodes importantes
- `HealthStatus()`
- `getFatigue()`
- `setFatigue()`

Classes utilisées
- [Injury](../src/data/player/Injury.java)
- [SimulationConfiguration](../src/config/SimulationConfiguration.java)

Utilisée par
- [Player](../src/data/player/Player.java)
- [PlayerUtilitary](../src/process/utilitary/PlayerUtilitary.java)

### Injury

📄 Fichier
[src/data/player/Injury.java](../src/data/player/Injury.java)

Rôle
Décrit un joueur ou son état sportif, médical et statistique dans la simulation.

Méthodes importantes
- `Injury()`
- `getInjuryType()`
- `setInjuryType()`

Classes utilisées
- Aucune dépendance métier notable.

Utilisée par
- [HealthStatus](../src/data/player/HealthStatus.java)

### Player

📄 Fichier
[src/data/player/Player.java](../src/data/player/Player.java)

Rôle
Représente un joueur avec son profil, ses statistiques, son état physique et son appartenance à une équipe.

Méthodes importantes
- `isStar()`
- `Player()`
- `setStar()`

Classes utilisées
- [Asset](../src/data/player/Asset.java)
- [HealthStatus](../src/data/player/HealthStatus.java)

Utilisée par
- [Trade](../src/data/finance/transfer/Trade.java)
- [League](../src/data/league/League.java)
- [Block](../src/data/sport/play/action/Block.java)
- [PointScored](../src/data/sport/play/action/PointScored.java)

### OffensiveTry

📄 Fichier
[src/data/sport/play/OffensiveTry.java](../src/data/sport/play/OffensiveTry.java)

Rôle
Décrit une tentative offensive et les choix pris pendant une séquence de jeu simulée.

Méthodes importantes
- `OffensiveTry()`
- `getName()`

Classes utilisées
- Aucune dépendance métier notable.

Utilisée par
- [ActionResult](../src/data/sport/play/action/ActionResult.java)
- [GameSimulator](../src/process/simulator/GameSimulator.java)

### ActionResult

📄 Fichier
[src/data/sport/play/action/ActionResult.java](../src/data/sport/play/action/ActionResult.java)

Rôle
Représente une action élémentaire produite pendant la simulation d’une possession ou d’un match.

Méthodes importantes
- `ActionResult()`
- `getName()`
- `setName()`

Classes utilisées
- [OffensiveTry](../src/data/sport/play/OffensiveTry.java)
- [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java)

Utilisée par
- [Block](../src/data/sport/play/action/Block.java)
- [EndOfTime](../src/data/sport/play/action/EndOfTime.java)
- [PointScored](../src/data/sport/play/action/PointScored.java)
- [Rebound](../src/data/sport/play/action/Rebound.java)

### Block

📄 Fichier
[src/data/sport/play/action/Block.java](../src/data/sport/play/action/Block.java)

Rôle
Représente une action élémentaire produite pendant la simulation d’une possession ou d’un match.

Méthodes importantes
- `Block()`
- `getBlockingPlayer()`
- `setBlockingPlayer()`

Classes utilisées
- [Player](../src/data/player/Player.java)
- [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java)
- [ActionResult](../src/data/sport/play/action/ActionResult.java)

Utilisée par
- [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java)
- [AssetUpdateVisitor](../src/process/visitor/actionresult/AssetUpdateVisitor.java)
- [GameResultVisitor](../src/process/visitor/actionresult/GameResultVisitor.java)

### EndOfTime

📄 Fichier
[src/data/sport/play/action/EndOfTime.java](../src/data/sport/play/action/EndOfTime.java)

Rôle
Représente une action élémentaire produite pendant la simulation d’une possession ou d’un match.

Méthodes importantes
- `EndOfTime()`
- `accept()`

Classes utilisées
- [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java)
- [ActionResult](../src/data/sport/play/action/ActionResult.java)

Utilisée par
- [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java)
- [AssetUpdateVisitor](../src/process/visitor/actionresult/AssetUpdateVisitor.java)
- [GameResultVisitor](../src/process/visitor/actionresult/GameResultVisitor.java)

### PointScored

📄 Fichier
[src/data/sport/play/action/PointScored.java](../src/data/sport/play/action/PointScored.java)

Rôle
Représente une action élémentaire produite pendant la simulation d’une possession ou d’un match.

Méthodes importantes
- `PointScored()`
- `getPointsScored()`
- `getScorerPlayer()`

Classes utilisées
- [Player](../src/data/player/Player.java)
- [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java)
- [ActionResult](../src/data/sport/play/action/ActionResult.java)

Utilisée par
- [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java)
- [AssetUpdateVisitor](../src/process/visitor/actionresult/AssetUpdateVisitor.java)
- [GameResultVisitor](../src/process/visitor/actionresult/GameResultVisitor.java)

### Rebound

📄 Fichier
[src/data/sport/play/action/Rebound.java](../src/data/sport/play/action/Rebound.java)

Rôle
Représente une action élémentaire produite pendant la simulation d’une possession ou d’un match.

Méthodes importantes
- `Rebound()`
- `getReboundPlayer()`
- `setReboundPlayer()`

Classes utilisées
- [Player](../src/data/player/Player.java)
- [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java)
- [ActionResult](../src/data/sport/play/action/ActionResult.java)

Utilisée par
- [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java)
- [AssetUpdateVisitor](../src/process/visitor/actionresult/AssetUpdateVisitor.java)
- [GameResultVisitor](../src/process/visitor/actionresult/GameResultVisitor.java)

### Turnover

📄 Fichier
[src/data/sport/play/action/Turnover.java](../src/data/sport/play/action/Turnover.java)

Rôle
Représente une action élémentaire produite pendant la simulation d’une possession ou d’un match.

Méthodes importantes
- `Turnover()`
- `getInterceptedPlayer()`
- `getDefensePlayer()`

Classes utilisées
- [Player](../src/data/player/Player.java)
- [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java)
- [ActionResult](../src/data/sport/play/action/ActionResult.java)

Utilisée par
- [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java)
- [AssetUpdateVisitor](../src/process/visitor/actionresult/AssetUpdateVisitor.java)
- [GameResultVisitor](../src/process/visitor/actionresult/GameResultVisitor.java)

### Game

📄 Fichier
[src/data/sport/setup/Game.java](../src/data/sport/setup/Game.java)

Rôle
Décrit une rencontre entre deux équipes avec son score, son contexte et son résultat final.

Méthodes importantes
- `Game()`
- `getGameContext()`
- `setGameContext()`

Classes utilisées
- [GameContext](../src/data/sport/setup/GameContext.java)
- [GameResult](../src/data/sport/setup/GameResult.java)

Utilisée par
- [GameDay](../src/data/calendar/GameDay.java)
- [GameStat](../src/data/finance/GameStat.java)
- [Team](../src/data/team/Team.java)
- [Schedule](../src/data/team/calendar/Schedule.java)

### GameContext

📄 Fichier
[src/data/sport/setup/GameContext.java](../src/data/sport/setup/GameContext.java)

Rôle
Encapsule le contexte d’un match, son déroulement et son résultat exploitable par le reste du projet.

Méthodes importantes
- `GameContext()`
- `isScheduled()`
- `setScheduled()`

Classes utilisées
- [Team](../src/data/team/Team.java)
- [CalendarUtilitary](../src/process/utilitary/CalendarUtilitary.java)

Utilisée par
- [Game](../src/data/sport/setup/Game.java)
- [Team](../src/data/team/Team.java)
- [GameGenerator](../src/process/builder/calendartools/GameGenerator.java)
- [CalendarUtilitary](../src/process/utilitary/CalendarUtilitary.java)

### GameResult

📄 Fichier
[src/data/sport/setup/GameResult.java](../src/data/sport/setup/GameResult.java)

Rôle
Stocke le résultat détaillé d’un match et la liste des actions produites pendant sa simulation.

Méthodes importantes
- `GameResult()`
- `addActions()`
- `getScorehomeTeam()`

Classes utilisées
- [ActionResult](../src/data/sport/play/action/ActionResult.java)
- [Team](../src/data/team/Team.java)
- [Player](../src/data/player/Player.java)

Utilisée par
- [Game](../src/data/sport/setup/Game.java)
- [GameSimulator](../src/process/simulator/GameSimulator.java)
- [GameResultVisitor](../src/process/visitor/actionresult/GameResultVisitor.java)

### Stadium

📄 Fichier
[src/data/team/Stadium.java](../src/data/team/Stadium.java)

Rôle
Représente une franchise, son stade ou ses performances collectives dans le modèle métier.

Méthodes importantes
- `Stadium()`
- `getName()`
- `setName()`

Classes utilisées
- Aucune dépendance métier notable.

Utilisée par
- [Team](../src/data/team/Team.java)
- [TeamFactory](../src/process/factory/TeamFactory.java)
- [GameRevenueSimulator](../src/process/simulator/GameRevenueSimulator.java)

### Team

📄 Fichier
[src/data/team/Team.java](../src/data/team/Team.java)

Rôle
Représente une franchise avec son effectif, ses performances, son planning et ses données financières.

Méthodes importantes
- `Team()`
- `getName()`
- `setNom()`

Classes utilisées
- [Player](../src/data/player/Player.java)
- [Schedule](../src/data/team/calendar/Schedule.java)
- [TeamFinance](../src/data/team/finance/TeamFinance.java)
- [TeamPerformance](../src/data/team/TeamPerformance.java)

Utilisée par
- [Trade](../src/data/finance/transfer/Trade.java)
- [Conference](../src/data/league/Conference.java)
- [Division](../src/data/league/Division.java)
- [League](../src/data/league/League.java)

### TeamPerformance

📄 Fichier
[src/data/team/TeamPerformance.java](../src/data/team/TeamPerformance.java)

Rôle
Représente une franchise, son stade ou ses performances collectives dans le modèle métier.

Méthodes importantes
- `TeamPerformance()`
- `getPerformanceRating()`
- `setPerformanceRating()`

Classes utilisées
- Aucune dépendance métier notable.

Utilisée par
- [Team](../src/data/team/Team.java)

### Schedule

📄 Fichier
[src/data/team/calendar/Schedule.java](../src/data/team/calendar/Schedule.java)

Rôle
Stocke le planning d’une équipe et les matchs à venir ou déjà joués.

Méthodes importantes
- `Schedule()`
- `getNumberOfPlayedGames()`
- `setNumberOfPlayedGames()`

Classes utilisées
- [Game](../src/data/sport/setup/Game.java)

Utilisée par
- [Team](../src/data/team/Team.java)
- [GameSelector](../src/process/builder/calendartools/GameSelector.java)

### TeamFinance

📄 Fichier
[src/data/team/finance/TeamFinance.java](../src/data/team/finance/TeamFinance.java)

Rôle
Concentre le budget d’une franchise, son marché, son profil financier et sa stratégie de transfert.

Méthodes importantes
- `TeamFinance()`
- `getFinancialProfil()`
- `getPayroll()`

Classes utilisées
- [Budget](../src/data/finance/budget/Budget.java)
- [FinancialProfil](../src/data/team/finance/financialprofil/FinancialProfil.java)
- [MarketSize](../src/data/team/finance/marketsize/MarketSize.java)
- [TeamTransferStrategy](../src/data/team/finance/transfer/TeamTransferStrategy.java)

Utilisée par
- [Team](../src/data/team/Team.java)
- [TeamFactory](../src/process/factory/TeamFactory.java)

### AmbitiousProfil

📄 Fichier
[src/data/team/finance/financialprofil/AmbitiousProfil.java](../src/data/team/finance/financialprofil/AmbitiousProfil.java)

Rôle
Définit un profil financier qui influence les choix économiques et sportifs d’une franchise.

Méthodes importantes
- `AmbitiousProfil()`

Classes utilisées
- [FinancialProfil](../src/data/team/finance/financialprofil/FinancialProfil.java)

Utilisée par
- [TeamUtilitary](../src/process/utilitary/TeamUtilitary.java)

### BalancedProfil

📄 Fichier
[src/data/team/finance/financialprofil/BalancedProfil.java](../src/data/team/finance/financialprofil/BalancedProfil.java)

Rôle
Définit un profil financier qui influence les choix économiques et sportifs d’une franchise.

Méthodes importantes
- `BalancedProfil()`

Classes utilisées
- [FinancialProfil](../src/data/team/finance/financialprofil/FinancialProfil.java)

Utilisée par
- [TeamUtilitary](../src/process/utilitary/TeamUtilitary.java)

### EconomicalProfil

📄 Fichier
[src/data/team/finance/financialprofil/EconomicalProfil.java](../src/data/team/finance/financialprofil/EconomicalProfil.java)

Rôle
Définit un profil financier qui influence les choix économiques et sportifs d’une franchise.

Méthodes importantes
- `EconomicalProfil()`

Classes utilisées
- [FinancialProfil](../src/data/team/finance/financialprofil/FinancialProfil.java)

Utilisée par
- [TeamUtilitary](../src/process/utilitary/TeamUtilitary.java)

### FinancialProfil

📄 Fichier
[src/data/team/finance/financialprofil/FinancialProfil.java](../src/data/team/finance/financialprofil/FinancialProfil.java)

Rôle
Définit un profil financier qui influence les choix économiques et sportifs d’une franchise.

Méthodes importantes
- `FinancialProfil()`
- `getName()`

Classes utilisées
- Aucune dépendance métier notable.

Utilisée par
- [TeamFinance](../src/data/team/finance/TeamFinance.java)
- [AmbitiousProfil](../src/data/team/finance/financialprofil/AmbitiousProfil.java)
- [BalancedProfil](../src/data/team/finance/financialprofil/BalancedProfil.java)
- [EconomicalProfil](../src/data/team/finance/financialprofil/EconomicalProfil.java)

### LargeSize

📄 Fichier
[src/data/team/finance/marketsize/LargeSize.java](../src/data/team/finance/marketsize/LargeSize.java)

Rôle
Modélise la taille de marché d’une franchise et les calculs économiques qui en dépendent.

Méthodes importantes
- `LargeSize()`
- `accept()`

Classes utilisées
- [MarketSizeVisitor](../src/process/visitor/marketsize/MarketSizeVisitor.java)
- [MarketSize](../src/data/team/finance/marketsize/MarketSize.java)

Utilisée par
- [CalculateBaseTicketVisitor](../src/process/visitor/marketsize/CalculateBaseTicketVisitor.java)
- [CalculateInitialTeamBudgetVisitor](../src/process/visitor/marketsize/CalculateInitialTeamBudgetVisitor.java)
- [CalculateStadiumCostVisitor](../src/process/visitor/marketsize/CalculateStadiumCostVisitor.java)
- [GenerateStadiumCapacityVisitor](../src/process/visitor/marketsize/GenerateStadiumCapacityVisitor.java)

### MarketSize

📄 Fichier
[src/data/team/finance/marketsize/MarketSize.java](../src/data/team/finance/marketsize/MarketSize.java)

Rôle
Définit la taille de marché d’une franchise, utilisée ensuite par plusieurs calculs économiques.

Méthodes importantes
- `getSize()`
- `setSize()`
- `MarketSize()`

Classes utilisées
- [MarketSizeVisitor](../src/process/visitor/marketsize/MarketSizeVisitor.java)

Utilisée par
- [TeamFinance](../src/data/team/finance/TeamFinance.java)
- [LargeSize](../src/data/team/finance/marketsize/LargeSize.java)
- [MediumSize](../src/data/team/finance/marketsize/MediumSize.java)
- [SmallSize](../src/data/team/finance/marketsize/SmallSize.java)

### MediumSize

📄 Fichier
[src/data/team/finance/marketsize/MediumSize.java](../src/data/team/finance/marketsize/MediumSize.java)

Rôle
Modélise la taille de marché d’une franchise et les calculs économiques qui en dépendent.

Méthodes importantes
- `MediumSize()`
- `accept()`

Classes utilisées
- [MarketSizeVisitor](../src/process/visitor/marketsize/MarketSizeVisitor.java)
- [MarketSize](../src/data/team/finance/marketsize/MarketSize.java)

Utilisée par
- [CalculateBaseTicketVisitor](../src/process/visitor/marketsize/CalculateBaseTicketVisitor.java)
- [CalculateInitialTeamBudgetVisitor](../src/process/visitor/marketsize/CalculateInitialTeamBudgetVisitor.java)
- [CalculateStadiumCostVisitor](../src/process/visitor/marketsize/CalculateStadiumCostVisitor.java)
- [GenerateStadiumCapacityVisitor](../src/process/visitor/marketsize/GenerateStadiumCapacityVisitor.java)

### SmallSize

📄 Fichier
[src/data/team/finance/marketsize/SmallSize.java](../src/data/team/finance/marketsize/SmallSize.java)

Rôle
Modélise la taille de marché d’une franchise et les calculs économiques qui en dépendent.

Méthodes importantes
- `SmallSize()`
- `accept()`

Classes utilisées
- [MarketSizeVisitor](../src/process/visitor/marketsize/MarketSizeVisitor.java)
- [MarketSize](../src/data/team/finance/marketsize/MarketSize.java)

Utilisée par
- [CalculateBaseTicketVisitor](../src/process/visitor/marketsize/CalculateBaseTicketVisitor.java)
- [CalculateInitialTeamBudgetVisitor](../src/process/visitor/marketsize/CalculateInitialTeamBudgetVisitor.java)
- [CalculateStadiumCostVisitor](../src/process/visitor/marketsize/CalculateStadiumCostVisitor.java)
- [GenerateStadiumCapacityVisitor](../src/process/visitor/marketsize/GenerateStadiumCapacityVisitor.java)

### AllIn

📄 Fichier
[src/data/team/finance/transfer/AllIn.java](../src/data/team/finance/transfer/AllIn.java)

Rôle
Définit une stratégie de transfert utilisée pour orienter les décisions d’effectif d’une équipe.

Méthodes importantes
- `AllIn()`
- `accept()`

Classes utilisées
- [TeamTransferVisitor](../src/process/visitor/teamtransfer/TeamTransferVisitor.java)
- [Player](../src/data/player/Player.java)
- [TeamTransferStrategy](../src/data/team/finance/transfer/TeamTransferStrategy.java)

Utilisée par
- [TransferStrategyUtilitary](../src/process/utilitary/TransferStrategyUtilitary.java)
- [EvaluateSeasonIntentVisitor](../src/process/visitor/teamtransfer/EvaluateSeasonIntentVisitor.java)
- [PreSeasonPlayerToTradeVisitor](../src/process/visitor/teamtransfer/PreSeasonPlayerToTradeVisitor.java)
- [PreSeasonTradeSatisfactionVisitor](../src/process/visitor/teamtransfer/PreSeasonTradeSatisfactionVisitor.java)

### Balanced

📄 Fichier
[src/data/team/finance/transfer/Balanced.java](../src/data/team/finance/transfer/Balanced.java)

Rôle
Définit une stratégie de transfert utilisée pour orienter les décisions d’effectif d’une équipe.

Méthodes importantes
- `Balanced()`
- `accept()`

Classes utilisées
- [TeamTransferVisitor](../src/process/visitor/teamtransfer/TeamTransferVisitor.java)
- [Player](../src/data/player/Player.java)
- [TeamTransferStrategy](../src/data/team/finance/transfer/TeamTransferStrategy.java)

Utilisée par
- [TransferStrategyUtilitary](../src/process/utilitary/TransferStrategyUtilitary.java)
- [EvaluateSeasonIntentVisitor](../src/process/visitor/teamtransfer/EvaluateSeasonIntentVisitor.java)
- [PreSeasonPlayerToTradeVisitor](../src/process/visitor/teamtransfer/PreSeasonPlayerToTradeVisitor.java)
- [PreSeasonTradeSatisfactionVisitor](../src/process/visitor/teamtransfer/PreSeasonTradeSatisfactionVisitor.java)

### Rebuild

📄 Fichier
[src/data/team/finance/transfer/Rebuild.java](../src/data/team/finance/transfer/Rebuild.java)

Rôle
Définit une stratégie de transfert utilisée pour orienter les décisions d’effectif d’une équipe.

Méthodes importantes
- `Rebuild()`
- `accept()`

Classes utilisées
- [TeamTransferVisitor](../src/process/visitor/teamtransfer/TeamTransferVisitor.java)
- [Player](../src/data/player/Player.java)
- [TeamTransferStrategy](../src/data/team/finance/transfer/TeamTransferStrategy.java)

Utilisée par
- [TransferStrategyUtilitary](../src/process/utilitary/TransferStrategyUtilitary.java)
- [EvaluateSeasonIntentVisitor](../src/process/visitor/teamtransfer/EvaluateSeasonIntentVisitor.java)
- [PreSeasonPlayerToTradeVisitor](../src/process/visitor/teamtransfer/PreSeasonPlayerToTradeVisitor.java)
- [PreSeasonTradeSatisfactionVisitor](../src/process/visitor/teamtransfer/PreSeasonTradeSatisfactionVisitor.java)

### SalaryDump

📄 Fichier
[src/data/team/finance/transfer/SalaryDump.java](../src/data/team/finance/transfer/SalaryDump.java)

Rôle
Définit une stratégie de transfert utilisée pour orienter les décisions d’effectif d’une équipe.

Méthodes importantes
- `SalaryDump()`
- `accept()`

Classes utilisées
- [TeamTransferVisitor](../src/process/visitor/teamtransfer/TeamTransferVisitor.java)
- [Player](../src/data/player/Player.java)
- [TeamTransferStrategy](../src/data/team/finance/transfer/TeamTransferStrategy.java)

Utilisée par
- [TransferStrategyUtilitary](../src/process/utilitary/TransferStrategyUtilitary.java)
- [EvaluateSeasonIntentVisitor](../src/process/visitor/teamtransfer/EvaluateSeasonIntentVisitor.java)
- [PreSeasonPlayerToTradeVisitor](../src/process/visitor/teamtransfer/PreSeasonPlayerToTradeVisitor.java)
- [PreSeasonTradeSatisfactionVisitor](../src/process/visitor/teamtransfer/PreSeasonTradeSatisfactionVisitor.java)

### SmallAdjust

📄 Fichier
[src/data/team/finance/transfer/SmallAdjust.java](../src/data/team/finance/transfer/SmallAdjust.java)

Rôle
Définit une stratégie de transfert utilisée pour orienter les décisions d’effectif d’une équipe.

Méthodes importantes
- `SmallAdjust()`
- `accept()`

Classes utilisées
- [TeamTransferVisitor](../src/process/visitor/teamtransfer/TeamTransferVisitor.java)
- [Player](../src/data/player/Player.java)
- [TeamTransferStrategy](../src/data/team/finance/transfer/TeamTransferStrategy.java)

Utilisée par
- [TransferStrategyUtilitary](../src/process/utilitary/TransferStrategyUtilitary.java)
- [EvaluateSeasonIntentVisitor](../src/process/visitor/teamtransfer/EvaluateSeasonIntentVisitor.java)
- [PreSeasonPlayerToTradeVisitor](../src/process/visitor/teamtransfer/PreSeasonPlayerToTradeVisitor.java)
- [PreSeasonTradeSatisfactionVisitor](../src/process/visitor/teamtransfer/PreSeasonTradeSatisfactionVisitor.java)

### SuperstarBuild

📄 Fichier
[src/data/team/finance/transfer/SuperstarBuild.java](../src/data/team/finance/transfer/SuperstarBuild.java)

Rôle
Définit une stratégie de transfert utilisée pour orienter les décisions d’effectif d’une équipe.

Méthodes importantes
- `SuperstarBuild()`
- `accept()`

Classes utilisées
- [TeamTransferVisitor](../src/process/visitor/teamtransfer/TeamTransferVisitor.java)
- [Player](../src/data/player/Player.java)
- [TeamTransferStrategy](../src/data/team/finance/transfer/TeamTransferStrategy.java)

Utilisée par
- [EvaluateSeasonIntentVisitor](../src/process/visitor/teamtransfer/EvaluateSeasonIntentVisitor.java)
- [PreSeasonTradeSatisfactionVisitor](../src/process/visitor/teamtransfer/PreSeasonTradeSatisfactionVisitor.java)
- [SeasonPlayerToTradeVisitor](../src/process/visitor/teamtransfer/SeasonPlayerToTradeVisitor.java)
- [TeamTransferVisitor](../src/process/visitor/teamtransfer/TeamTransferVisitor.java)

### TeamTransferStrategy

📄 Fichier
[src/data/team/finance/transfer/TeamTransferStrategy.java](../src/data/team/finance/transfer/TeamTransferStrategy.java)

Rôle
Définit une stratégie de transfert utilisée pour orienter les décisions d’effectif d’une équipe.

Méthodes importantes
- `TeamTransferStrategy()`
- `getName()`
- `setName()`

Classes utilisées
- [TeamTransferVisitor](../src/process/visitor/teamtransfer/TeamTransferVisitor.java)
- [Player](../src/data/player/Player.java)

Utilisée par
- [TeamFinance](../src/data/team/finance/TeamFinance.java)
- [AllIn](../src/data/team/finance/transfer/AllIn.java)
- [Balanced](../src/data/team/finance/transfer/Balanced.java)
- [Rebuild](../src/data/team/finance/transfer/Rebuild.java)

## PROCESS

### CalendarBuilder

📄 Fichier
[src/process/builder/CalendarBuilder.java](../src/process/builder/CalendarBuilder.java)

Rôle
Prépare une partie de l’état applicatif en construisant les objets ou le calendrier à partir des données source.

Méthodes importantes
- `CalendarBuilder()`
- `buildRegulaSeasonCalendar()`

Classes utilisées
- [GameDay](../src/data/calendar/GameDay.java)
- [League](../src/data/league/League.java)
- [Game](../src/data/sport/setup/Game.java)
- [GameSelector](../src/process/builder/calendartools/GameSelector.java)

Utilisée par
- [LeagueManager](../src/process/manager/LeagueManager.java)

### LeagueBuilder

📄 Fichier
[src/process/builder/LeagueBuilder.java](../src/process/builder/LeagueBuilder.java)

Rôle
Prépare une partie de l’état applicatif en construisant les objets ou le calendrier à partir des données source.

Méthodes importantes
- `LeagueBuilder()`
- `build()`

Classes utilisées
- [FinanceConfiguration](../src/config/FinanceConfiguration.java)
- [FinanceUtilitary](../src/process/utilitary/FinanceUtilitary.java)
- [Income](../src/data/finance/budget/Income.java)
- [League](../src/data/league/League.java)

Utilisée par
- [LeagueManager](../src/process/manager/LeagueManager.java)

### GameGenerator

📄 Fichier
[src/process/builder/calendartools/GameGenerator.java](../src/process/builder/calendartools/GameGenerator.java)

Rôle
Prépare une partie de l’état applicatif en construisant les objets ou le calendrier à partir des données source.

Méthodes importantes
- `generateAllGamesRegularSeason()`
- `generateInterConference()`

Classes utilisées
- [Team](../src/data/team/Team.java)
- [Game](../src/data/sport/setup/Game.java)
- [GameContext](../src/data/sport/setup/GameContext.java)
- [Division](../src/data/league/Division.java)

Utilisée par
- [CalendarBuilder](../src/process/builder/CalendarBuilder.java)

### GameSelector

📄 Fichier
[src/process/builder/calendartools/GameSelector.java](../src/process/builder/calendartools/GameSelector.java)

Rôle
Prépare une partie de l’état applicatif en construisant les objets ou le calendrier à partir des données source.

Méthodes importantes
- `GameSelector()`
- `selectGamesForDay()`
- `setDate()`

Classes utilisées
- [Game](../src/data/sport/setup/Game.java)
- [Team](../src/data/team/Team.java)
- [League](../src/data/league/League.java)
- [CalendarUtilitary](../src/process/utilitary/CalendarUtilitary.java)

Utilisée par
- [CalendarBuilder](../src/process/builder/CalendarBuilder.java)

### ScheduleReset

📄 Fichier
[src/process/builder/calendartools/ScheduleReset.java](../src/process/builder/calendartools/ScheduleReset.java)

Rôle
Prépare une partie de l’état applicatif en construisant les objets ou le calendrier à partir des données source.

Méthodes importantes
- `initialization()`

Classes utilisées
- [Team](../src/data/team/Team.java)
- [TeamRepositery](../src/process/repositery/TeamRepositery.java)

Utilisée par
- [CalendarBuilder](../src/process/builder/CalendarBuilder.java)

### SpecialEventPlanner

📄 Fichier
[src/process/builder/calendartools/SpecialEventPlanner.java](../src/process/builder/calendartools/SpecialEventPlanner.java)

Rôle
Prépare une partie de l’état applicatif en construisant les objets ou le calendrier à partir des données source.

Méthodes importantes
- `specialEventsPlacement()`

Classes utilisées
- [SpecialEvent](../src/data/calendar/SpecialEvent.java)
- [SimulationConfiguration](../src/config/SimulationConfiguration.java)
- [RegularSeason](../src/data/league/RegularSeason.java)
- [CalendarUtilitary](../src/process/utilitary/CalendarUtilitary.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### PlayerFactory

📄 Fichier
[src/process/factory/PlayerFactory.java](../src/process/factory/PlayerFactory.java)

Rôle
Instancie des objets métier à partir de données brutes ou de règles de création dédiées.

Méthodes importantes
- `createPlayer()`

Classes utilisées
- [Player](../src/data/player/Player.java)
- [Asset](../src/data/player/Asset.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### TeamFactory

📄 Fichier
[src/process/factory/TeamFactory.java](../src/process/factory/TeamFactory.java)

Rôle
Instancie des objets métier à partir de données brutes ou de règles de création dédiées.

Méthodes importantes
- `createTeam()`

Classes utilisées
- [MarketSize](../src/data/team/finance/marketsize/MarketSize.java)
- [FinanceConfiguration](../src/config/FinanceConfiguration.java)
- [Team](../src/data/team/Team.java)
- [Budget](../src/data/finance/budget/Budget.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### FinanceManager

📄 Fichier
[src/process/manager/FinanceManager.java](../src/process/manager/FinanceManager.java)

Rôle
Orchestre une étape clé de la logique métier, de la simulation ou de la gestion financière.

Méthodes importantes
- `FinanceManager()`
- `applyRevenueSharing()`
- `distributeCentralRevenue()`

Classes utilisées
- [FinanceConfiguration](../src/config/FinanceConfiguration.java)
- [FinanceUtilitary](../src/process/utilitary/FinanceUtilitary.java)
- [GameStat](../src/data/finance/GameStat.java)
- [Income](../src/data/finance/budget/Income.java)

Utilisée par
- [GameManager](../src/process/manager/GameManager.java)

### GameManager

📄 Fichier
[src/process/manager/GameManager.java](../src/process/manager/GameManager.java)

Rôle
Orchestre une étape clé de la logique métier, de la simulation ou de la gestion financière.

Méthodes importantes
- `GameManager()`
- `simulateDay()`
- `setLeague()`

Classes utilisées
- [GameDay](../src/data/calendar/GameDay.java)
- [League](../src/data/league/League.java)
- [GameSimulator](../src/process/simulator/GameSimulator.java)
- [CalendarUtilitary](../src/process/utilitary/CalendarUtilitary.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### LeagueManager

📄 Fichier
[src/process/manager/LeagueManager.java](../src/process/manager/LeagueManager.java)

Rôle
Orchestre la construction de la ligue, le calendrier, la simulation quotidienne et les profils financiers.

Méthodes importantes
- `LeagueManager()`
- `startSeason()`
- `simulateDay()`

Classes utilisées
- [League](../src/data/league/League.java)
- [CalendarBuilder](../src/process/builder/CalendarBuilder.java)
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
- [Team](../src/data/team/Team.java)

Utilisée par
- [SimulationManager](../src/process/manager/SimulationManager.java)

### RevenueSharingManager

📄 Fichier
[src/process/manager/RevenueSharingManager.java](../src/process/manager/RevenueSharingManager.java)

Rôle
Orchestre une étape clé de la logique métier, de la simulation ou de la gestion financière.

Méthodes importantes
- `RevenueSharingManager()`
- `applyRevenueSharing()`

Classes utilisées
- [FinanceUtilitary](../src/process/utilitary/FinanceUtilitary.java)
- [FinanceConfiguration](../src/config/FinanceConfiguration.java)
- [Budget](../src/data/finance/budget/Budget.java)
- [Team](../src/data/team/Team.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### SimulationManager

📄 Fichier
[src/process/manager/SimulationManager.java](../src/process/manager/SimulationManager.java)

Rôle
Orchestre une étape clé de la logique métier, de la simulation ou de la gestion financière.

Méthodes importantes
- `SimulationManager()`
- `randomFinance()`
- `startSeason()`

Classes utilisées
- [SimulationConfiguration](../src/config/SimulationConfiguration.java)
- [LeagueManager](../src/process/manager/LeagueManager.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### TradeManager

📄 Fichier
[src/process/manager/TradeManager.java](../src/process/manager/TradeManager.java)

Rôle
Orchestre une étape clé de la logique métier, de la simulation ou de la gestion financière.

Méthodes importantes
- `TradeManager()`
- `simulatePreSeasonTrade()`
- `simulateSeasonTrade()`

Classes utilisées
- [Team](../src/data/team/Team.java)
- [TeamTransferStrategy](../src/data/team/finance/transfer/TeamTransferStrategy.java)
- [Player](../src/data/player/Player.java)
- [FinanceConfiguration](../src/config/FinanceConfiguration.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### CurrentSeasonAssetRepositery

📄 Fichier
[src/process/repositery/CurrentSeasonAssetRepositery.java](../src/process/repositery/CurrentSeasonAssetRepositery.java)

Rôle
Conserve un registre partagé pour retrouver rapidement des objets déjà créés dans la simulation.

Méthodes importantes
- `getInstance()`
- `register()`
- `getCurrentSeasonAsset()`

Classes utilisées
- [Asset](../src/data/player/Asset.java)
- [Player](../src/data/player/Player.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### DivisionRepositery

📄 Fichier
[src/process/repositery/DivisionRepositery.java](../src/process/repositery/DivisionRepositery.java)

Rôle
Conserve un registre partagé pour retrouver rapidement des objets déjà créés dans la simulation.

Méthodes importantes
- `getInstance()`
- `register()`
- `getDivision()`

Classes utilisées
- [Division](../src/data/league/Division.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### PlayerRepositery

📄 Fichier
[src/process/repositery/PlayerRepositery.java](../src/process/repositery/PlayerRepositery.java)

Rôle
Conserve un registre partagé pour retrouver rapidement des objets déjà créés dans la simulation.

Méthodes importantes
- `getInstance()`
- `register()`
- `getPlayer()`

Classes utilisées
- [Player](../src/data/player/Player.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### PreSeasonAssetRepositery

📄 Fichier
[src/process/repositery/PreSeasonAssetRepositery.java](../src/process/repositery/PreSeasonAssetRepositery.java)

Rôle
Conserve un registre partagé pour retrouver rapidement des objets déjà créés dans la simulation.

Méthodes importantes
- `getInstance()`
- `register()`
- `getPreSeasonAsset()`

Classes utilisées
- [Asset](../src/data/player/Asset.java)
- [Player](../src/data/player/Player.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### TeamRepositery

📄 Fichier
[src/process/repositery/TeamRepositery.java](../src/process/repositery/TeamRepositery.java)

Rôle
Conserve un registre partagé pour retrouver rapidement des objets déjà créés dans la simulation.

Méthodes importantes
- `getInstance()`
- `register()`
- `getTeam()`

Classes utilisées
- [Team](../src/data/team/Team.java)

Utilisée par
- [GameSelector](../src/process/builder/calendartools/GameSelector.java)
- [ScheduleReset](../src/process/builder/calendartools/ScheduleReset.java)
- [FinanceManager](../src/process/manager/FinanceManager.java)
- [LeagueManager](../src/process/manager/LeagueManager.java)

### GameExpenseSimulator

📄 Fichier
[src/process/simulator/GameExpenseSimulator.java](../src/process/simulator/GameExpenseSimulator.java)

Rôle
Simule une partie du comportement métier et produit des effets sur les objets de domaine.

Méthodes importantes
- `GameExpenseSimulator()`
- `calculateGameExpenses()`

Classes utilisées
- [FinanceConfiguration](../src/config/FinanceConfiguration.java)
- [Game](../src/data/sport/setup/Game.java)
- [SimulationConfiguration](../src/config/SimulationConfiguration.java)
- [GameStat](../src/data/finance/GameStat.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### GameRevenueSimulator

📄 Fichier
[src/process/simulator/GameRevenueSimulator.java](../src/process/simulator/GameRevenueSimulator.java)

Rôle
Simule une partie du comportement métier et produit des effets sur les objets de domaine.

Méthodes importantes
- `GameRevenueSimulator()`
- `calculateGameRevenue()`

Classes utilisées
- [GameStat](../src/data/finance/GameStat.java)
- [Game](../src/data/sport/setup/Game.java)
- [Stadium](../src/data/team/Stadium.java)
- [Team](../src/data/team/Team.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### GameSimulator

📄 Fichier
[src/process/simulator/GameSimulator.java](../src/process/simulator/GameSimulator.java)

Rôle
Simule le déroulement d’un match, produit les actions de jeu et met à jour les statistiques des joueurs.

Méthodes importantes
- `updateRest()`
- `simulateGame()`

Classes utilisées
- [Player](../src/data/player/Player.java)
- [SimulationConfiguration](../src/config/SimulationConfiguration.java)
- [Asset](../src/data/player/Asset.java)
- [PlayerUtilitary](../src/process/utilitary/PlayerUtilitary.java)

Utilisée par
- [GameManager](../src/process/manager/GameManager.java)

### TradeSimulator

📄 Fichier
[src/process/simulator/TradeSimulator.java](../src/process/simulator/TradeSimulator.java)

Rôle
Simule une partie du comportement métier et produit des effets sur les objets de domaine.

Méthodes importantes
- `validateTrade()`
- `respectEconomicPayroll()`
- `respectAmbitiousPayroll()`

Classes utilisées
- [Player](../src/data/player/Player.java)
- [Team](../src/data/team/Team.java)
- [FinancialPolicy](../src/config/FinancialPolicy.java)
- [FinanceUtilitary](../src/process/utilitary/FinanceUtilitary.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### CalendarUtilitary

📄 Fichier
[src/process/utilitary/CalendarUtilitary.java](../src/process/utilitary/CalendarUtilitary.java)

Rôle
Regroupe des opérations utilitaires réutilisées par les builders, managers ou simulateurs.

Méthodes importantes
- `isWeekend()`
- `isImportantDay()`
- `isSpecialEvent()`

Classes utilisées
- [SimulationConfiguration](../src/config/SimulationConfiguration.java)
- [Team](../src/data/team/Team.java)
- [RegularSeason](../src/data/league/RegularSeason.java)
- [Game](../src/data/sport/setup/Game.java)

Utilisée par
- [GameContext](../src/data/sport/setup/GameContext.java)
- [GameSelector](../src/process/builder/calendartools/GameSelector.java)
- [SpecialEventPlanner](../src/process/builder/calendartools/SpecialEventPlanner.java)
- [GameManager](../src/process/manager/GameManager.java)

### FinanceUtilitary

📄 Fichier
[src/process/utilitary/FinanceUtilitary.java](../src/process/utilitary/FinanceUtilitary.java)

Rôle
Regroupe des opérations utilitaires réutilisées par les builders, managers ou simulateurs.

Méthodes importantes
- `initiateBudget()`
- `updateLeaguePayroll()`
- `updateTeamPayroll()`

Classes utilisées
- [FinanceConfiguration](../src/config/FinanceConfiguration.java)
- [Income](../src/data/finance/budget/Income.java)
- [Expense](../src/data/finance/budget/Expense.java)
- [Budget](../src/data/finance/budget/Budget.java)

Utilisée par
- [LeagueFinance](../src/data/league/LeagueFinance.java)
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)
- [FinanceManager](../src/process/manager/FinanceManager.java)
- [LeagueManager](../src/process/manager/LeagueManager.java)

### PlayerUtilitary

📄 Fichier
[src/process/utilitary/PlayerUtilitary.java](../src/process/utilitary/PlayerUtilitary.java)

Rôle
Regroupe des opérations utilitaires réutilisées par les builders, managers ou simulateurs.

Méthodes importantes
- `getPlayerAttackNote()`
- `getPlayerDefenseNote()`
- `getPlayerOverAllNote()`

Classes utilisées
- [Asset](../src/data/player/Asset.java)
- [Player](../src/data/player/Player.java)
- [SimulationConfiguration](../src/config/SimulationConfiguration.java)
- [HealthStatus](../src/data/player/HealthStatus.java)

Utilisée par
- [GameSimulator](../src/process/simulator/GameSimulator.java)
- [PreSeasonPlayerToTradeVisitor](../src/process/visitor/teamtransfer/PreSeasonPlayerToTradeVisitor.java)
- [SeasonPlayerToTradeVisitor](../src/process/visitor/teamtransfer/SeasonPlayerToTradeVisitor.java)

### TeamUtilitary

📄 Fichier
[src/process/utilitary/TeamUtilitary.java](../src/process/utilitary/TeamUtilitary.java)

Rôle
Regroupe des opérations utilitaires réutilisées par les builders, managers ou simulateurs.

Méthodes importantes
- `getTeamSportProfile()`
- `setStarPlayer()`
- `updatePerformanceRating()`

Classes utilisées
- [Team](../src/data/team/Team.java)
- [FinanceConfiguration](../src/config/FinanceConfiguration.java)
- [FinancialPolicy](../src/config/FinancialPolicy.java)
- [SimulationConfiguration](../src/config/SimulationConfiguration.java)

Utilisée par
- [LeagueManager](../src/process/manager/LeagueManager.java)
- [TradeManager](../src/process/manager/TradeManager.java)
- [GameSimulator](../src/process/simulator/GameSimulator.java)

### TransferStrategyUtilitary

📄 Fichier
[src/process/utilitary/TransferStrategyUtilitary.java](../src/process/utilitary/TransferStrategyUtilitary.java)

Rôle
Regroupe des opérations utilitaires réutilisées par les builders, managers ou simulateurs.

Méthodes importantes
- `chooseTransferStrategy()`
- `chooseTransferStrategyAmbitious()`
- `chooseTransferStrategyBalanced()`

Classes utilisées
- [FinancialPolicy](../src/config/FinancialPolicy.java)
- [Balanced](../src/data/team/finance/transfer/Balanced.java)
- [SalaryDump](../src/data/team/finance/transfer/SalaryDump.java)
- [SmallAdjust](../src/data/team/finance/transfer/SmallAdjust.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### ActionResultVisitor

📄 Fichier
[src/process/visitor/actionresult/ActionResultVisitor.java](../src/process/visitor/actionresult/ActionResultVisitor.java)

Rôle
Implémente un visitor spécialisé pour évaluer une règle ou transformer un résultat de simulation.

Méthodes importantes
- Aucune méthode publique marquante.

Classes utilisées
- [Block](../src/data/sport/play/action/Block.java)
- [EndOfTime](../src/data/sport/play/action/EndOfTime.java)
- [PointScored](../src/data/sport/play/action/PointScored.java)
- [Rebound](../src/data/sport/play/action/Rebound.java)

Utilisée par
- [ActionResult](../src/data/sport/play/action/ActionResult.java)
- [Block](../src/data/sport/play/action/Block.java)
- [EndOfTime](../src/data/sport/play/action/EndOfTime.java)
- [PointScored](../src/data/sport/play/action/PointScored.java)

### AssetUpdateVisitor

📄 Fichier
[src/process/visitor/actionresult/AssetUpdateVisitor.java](../src/process/visitor/actionresult/AssetUpdateVisitor.java)

Rôle
Implémente un visitor spécialisé pour évaluer une règle ou transformer un résultat de simulation.

Méthodes importantes
- `AssetUpdateVisitor()`
- `visit()`

Classes utilisées
- [Player](../src/data/player/Player.java)
- [Asset](../src/data/player/Asset.java)
- [Block](../src/data/sport/play/action/Block.java)
- [EndOfTime](../src/data/sport/play/action/EndOfTime.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### GameResultVisitor

📄 Fichier
[src/process/visitor/actionresult/GameResultVisitor.java](../src/process/visitor/actionresult/GameResultVisitor.java)

Rôle
Implémente un visitor spécialisé pour évaluer une règle ou transformer un résultat de simulation.

Méthodes importantes
- `GameResultVisitor()`
- `visit()`

Classes utilisées
- [Player](../src/data/player/Player.java)
- [SimulationConfiguration](../src/config/SimulationConfiguration.java)
- [GameResult](../src/data/sport/setup/GameResult.java)
- [Block](../src/data/sport/play/action/Block.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### CalculateBaseTicketVisitor

📄 Fichier
[src/process/visitor/marketsize/CalculateBaseTicketVisitor.java](../src/process/visitor/marketsize/CalculateBaseTicketVisitor.java)

Rôle
Implémente un visitor spécialisé pour évaluer une règle ou transformer un résultat de simulation.

Méthodes importantes
- `CalculateBaseTicketVisitor()`
- `visit()`

Classes utilisées
- [FinanceConfiguration](../src/config/FinanceConfiguration.java)
- [LargeSize](../src/data/team/finance/marketsize/LargeSize.java)
- [MediumSize](../src/data/team/finance/marketsize/MediumSize.java)
- [SmallSize](../src/data/team/finance/marketsize/SmallSize.java)

Utilisée par
- [TeamFactory](../src/process/factory/TeamFactory.java)

### CalculateInitialTeamBudgetVisitor

📄 Fichier
[src/process/visitor/marketsize/CalculateInitialTeamBudgetVisitor.java](../src/process/visitor/marketsize/CalculateInitialTeamBudgetVisitor.java)

Rôle
Implémente un visitor spécialisé pour évaluer une règle ou transformer un résultat de simulation.

Méthodes importantes
- `CalculateInitialTeamBudgetVisitor()`
- `visit()`

Classes utilisées
- [LargeSize](../src/data/team/finance/marketsize/LargeSize.java)
- [MediumSize](../src/data/team/finance/marketsize/MediumSize.java)
- [SmallSize](../src/data/team/finance/marketsize/SmallSize.java)
- [MarketSizeVisitor](../src/process/visitor/marketsize/MarketSizeVisitor.java)

Utilisée par
- [TeamFactory](../src/process/factory/TeamFactory.java)

### CalculateStadiumCostVisitor

📄 Fichier
[src/process/visitor/marketsize/CalculateStadiumCostVisitor.java](../src/process/visitor/marketsize/CalculateStadiumCostVisitor.java)

Rôle
Implémente un visitor spécialisé pour évaluer une règle ou transformer un résultat de simulation.

Méthodes importantes
- `CalculateStadiumCostVisitor()`
- `visit()`

Classes utilisées
- [FinanceConfiguration](../src/config/FinanceConfiguration.java)
- [LargeSize](../src/data/team/finance/marketsize/LargeSize.java)
- [MediumSize](../src/data/team/finance/marketsize/MediumSize.java)
- [SmallSize](../src/data/team/finance/marketsize/SmallSize.java)

Utilisée par
- [GameExpenseSimulator](../src/process/simulator/GameExpenseSimulator.java)

### GenerateStadiumCapacityVisitor

📄 Fichier
[src/process/visitor/marketsize/GenerateStadiumCapacityVisitor.java](../src/process/visitor/marketsize/GenerateStadiumCapacityVisitor.java)

Rôle
Implémente un visitor spécialisé pour évaluer une règle ou transformer un résultat de simulation.

Méthodes importantes
- `GenerateStadiumCapacityVisitor()`
- `visit()`

Classes utilisées
- [LargeSize](../src/data/team/finance/marketsize/LargeSize.java)
- [MediumSize](../src/data/team/finance/marketsize/MediumSize.java)
- [SmallSize](../src/data/team/finance/marketsize/SmallSize.java)
- [MarketSizeVisitor](../src/process/visitor/marketsize/MarketSizeVisitor.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### MarketSizeVisitor

📄 Fichier
[src/process/visitor/marketsize/MarketSizeVisitor.java](../src/process/visitor/marketsize/MarketSizeVisitor.java)

Rôle
Implémente un visitor spécialisé pour évaluer une règle ou transformer un résultat de simulation.

Méthodes importantes
- Aucune méthode publique marquante.

Classes utilisées
- [LargeSize](../src/data/team/finance/marketsize/LargeSize.java)
- [MediumSize](../src/data/team/finance/marketsize/MediumSize.java)
- [SmallSize](../src/data/team/finance/marketsize/SmallSize.java)

Utilisée par
- [LargeSize](../src/data/team/finance/marketsize/LargeSize.java)
- [MarketSize](../src/data/team/finance/marketsize/MarketSize.java)
- [MediumSize](../src/data/team/finance/marketsize/MediumSize.java)
- [SmallSize](../src/data/team/finance/marketsize/SmallSize.java)

### EvaluateSeasonIntentVisitor

📄 Fichier
[src/process/visitor/teamtransfer/EvaluateSeasonIntentVisitor.java](../src/process/visitor/teamtransfer/EvaluateSeasonIntentVisitor.java)

Rôle
Implémente un visitor spécialisé pour évaluer une règle ou transformer un résultat de simulation.

Méthodes importantes
- `EvaluateSeasonIntentVisitor()`
- `visit()`

Classes utilisées
- [FinanceConfiguration](../src/config/FinanceConfiguration.java)
- [Team](../src/data/team/Team.java)
- [AllIn](../src/data/team/finance/transfer/AllIn.java)
- [Balanced](../src/data/team/finance/transfer/Balanced.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### PreSeasonPlayerToTradeVisitor

📄 Fichier
[src/process/visitor/teamtransfer/PreSeasonPlayerToTradeVisitor.java](../src/process/visitor/teamtransfer/PreSeasonPlayerToTradeVisitor.java)

Rôle
Implémente un visitor spécialisé pour évaluer une règle ou transformer un résultat de simulation.

Méthodes importantes
- `PreSeasonPlayerToTradeVisitor()`
- `visit()`

Classes utilisées
- [Player](../src/data/player/Player.java)
- [PlayerUtilitary](../src/process/utilitary/PlayerUtilitary.java)
- [Team](../src/data/team/Team.java)
- [AllIn](../src/data/team/finance/transfer/AllIn.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### PreSeasonTradeSatisfactionVisitor

📄 Fichier
[src/process/visitor/teamtransfer/PreSeasonTradeSatisfactionVisitor.java](../src/process/visitor/teamtransfer/PreSeasonTradeSatisfactionVisitor.java)

Rôle
Implémente un visitor spécialisé pour évaluer une règle ou transformer un résultat de simulation.

Méthodes importantes
- `PreSeasonTradeSatisfactionVisitor()`
- `visit()`

Classes utilisées
- [AllIn](../src/data/team/finance/transfer/AllIn.java)
- [Balanced](../src/data/team/finance/transfer/Balanced.java)
- [Rebuild](../src/data/team/finance/transfer/Rebuild.java)
- [SalaryDump](../src/data/team/finance/transfer/SalaryDump.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### SeasonPlayerToTradeVisitor

📄 Fichier
[src/process/visitor/teamtransfer/SeasonPlayerToTradeVisitor.java](../src/process/visitor/teamtransfer/SeasonPlayerToTradeVisitor.java)

Rôle
Implémente un visitor spécialisé pour évaluer une règle ou transformer un résultat de simulation.

Méthodes importantes
- `SeasonPlayerToTradeVisitor()`
- `visit()`

Classes utilisées
- [Player](../src/data/player/Player.java)
- [FinanceConfiguration](../src/config/FinanceConfiguration.java)
- [Team](../src/data/team/Team.java)
- [AllIn](../src/data/team/finance/transfer/AllIn.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### SeasonTradeSatisfactionVisitor

📄 Fichier
[src/process/visitor/teamtransfer/SeasonTradeSatisfactionVisitor.java](../src/process/visitor/teamtransfer/SeasonTradeSatisfactionVisitor.java)

Rôle
Implémente un visitor spécialisé pour évaluer une règle ou transformer un résultat de simulation.

Méthodes importantes
- Aucune méthode publique marquante.

Classes utilisées
- [TeamTransferVisitor](../src/process/visitor/teamtransfer/TeamTransferVisitor.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### TeamTransferVisitor

📄 Fichier
[src/process/visitor/teamtransfer/TeamTransferVisitor.java](../src/process/visitor/teamtransfer/TeamTransferVisitor.java)

Rôle
Implémente un visitor spécialisé pour évaluer une règle ou transformer un résultat de simulation.

Méthodes importantes
- Aucune méthode publique marquante.

Classes utilisées
- [AllIn](../src/data/team/finance/transfer/AllIn.java)
- [Balanced](../src/data/team/finance/transfer/Balanced.java)
- [Rebuild](../src/data/team/finance/transfer/Rebuild.java)
- [SalaryDump](../src/data/team/finance/transfer/SalaryDump.java)

Utilisée par
- [AllIn](../src/data/team/finance/transfer/AllIn.java)
- [Balanced](../src/data/team/finance/transfer/Balanced.java)
- [Rebuild](../src/data/team/finance/transfer/Rebuild.java)
- [SalaryDump](../src/data/team/finance/transfer/SalaryDump.java)

## GUI

### BuildBox

📄 Fichier
[src/gui/components/BuildBox.java](../src/gui/components/BuildBox.java)

Rôle
Fournit un composant Swing réutilisable pour structurer et habiller les écrans de la GUI.

Méthodes importantes
- `BuildBox()`

Classes utilisées
- [SectionTitle](../src/gui/components/SectionTitle.java)
- [DashboardCard](../src/gui/components/DashboardCard.java)

Utilisée par
- [CalendarDashboard](../src/gui/dashboard/CalendarDashboard.java)
- [FinanceDashboard](../src/gui/dashboard/FinanceDashboard.java)
- [MapDashboard](../src/gui/dashboard/MapDashboard.java)
- [MatchDashboard](../src/gui/dashboard/MatchDashboard.java)

### DashboardCard

📄 Fichier
[src/gui/components/DashboardCard.java](../src/gui/components/DashboardCard.java)

Rôle
Fournit un composant Swing réutilisable pour structurer et habiller les écrans de la GUI.

Méthodes importantes
- `DashboardCard()`

Classes utilisées
- Aucune dépendance métier notable.

Utilisée par
- [BuildBox](../src/gui/components/BuildBox.java)
- [TitledCard](../src/gui/components/TitledCard.java)

### SectionTitle

📄 Fichier
[src/gui/components/SectionTitle.java](../src/gui/components/SectionTitle.java)

Rôle
Fournit un composant Swing réutilisable pour structurer et habiller les écrans de la GUI.

Méthodes importantes
- `SectionTitle()`

Classes utilisées
- Aucune dépendance métier notable.

Utilisée par
- [BuildBox](../src/gui/components/BuildBox.java)
- [TitledCard](../src/gui/components/TitledCard.java)
- [CalendarDashboard](../src/gui/dashboard/CalendarDashboard.java)
- [FinanceDashboard](../src/gui/dashboard/FinanceDashboard.java)

### TitledCard

📄 Fichier
[src/gui/components/TitledCard.java](../src/gui/components/TitledCard.java)

Rôle
Fournit un composant Swing réutilisable pour structurer et habiller les écrans de la GUI.

Méthodes importantes
- `TitledCard()`

Classes utilisées
- [DashboardCard](../src/gui/components/DashboardCard.java)
- [SectionTitle](../src/gui/components/SectionTitle.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### CalendarDashboard

📄 Fichier
[src/gui/dashboard/CalendarDashboard.java](../src/gui/dashboard/CalendarDashboard.java)

Rôle
Affiche le tableau de bord calendar dans l’interface Swing et présente une vue métier ciblée.

Méthodes importantes
- `CalendarDashboard()`

Classes utilisées
- [BuildBox](../src/gui/components/BuildBox.java)
- [SectionTitle](../src/gui/components/SectionTitle.java)

Utilisée par
- [MainGui](../src/gui/frame/MainGui.java)

### FinanceDashboard

📄 Fichier
[src/gui/dashboard/FinanceDashboard.java](../src/gui/dashboard/FinanceDashboard.java)

Rôle
Affiche le tableau de bord finance dans l’interface Swing et présente une vue métier ciblée.

Méthodes importantes
- `FinanceDashboard()`

Classes utilisées
- [BuildBox](../src/gui/components/BuildBox.java)
- [SectionTitle](../src/gui/components/SectionTitle.java)

Utilisée par
- [MainGui](../src/gui/frame/MainGui.java)

### MapDashboard

📄 Fichier
[src/gui/dashboard/MapDashboard.java](../src/gui/dashboard/MapDashboard.java)

Rôle
Affiche le tableau de bord map dans l’interface Swing et présente une vue métier ciblée.

Méthodes importantes
- `MapDashboard()`

Classes utilisées
- [BuildBox](../src/gui/components/BuildBox.java)
- [SectionTitle](../src/gui/components/SectionTitle.java)

Utilisée par
- [MainGui](../src/gui/frame/MainGui.java)

### MatchDashboard

📄 Fichier
[src/gui/dashboard/MatchDashboard.java](../src/gui/dashboard/MatchDashboard.java)

Rôle
Affiche le tableau de bord match dans l’interface Swing et présente une vue métier ciblée.

Méthodes importantes
- `MatchDashboard()`

Classes utilisées
- [BuildBox](../src/gui/components/BuildBox.java)
- [SectionTitle](../src/gui/components/SectionTitle.java)

Utilisée par
- [MainGui](../src/gui/frame/MainGui.java)

### OpeningDashboard

📄 Fichier
[src/gui/dashboard/OpeningDashboard.java](../src/gui/dashboard/OpeningDashboard.java)

Rôle
Affiche le tableau de bord opening dans l’interface Swing et présente une vue métier ciblée.

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
[src/gui/dashboard/RankingDashboard.java](../src/gui/dashboard/RankingDashboard.java)

Rôle
Affiche le tableau de bord ranking dans l’interface Swing et présente une vue métier ciblée.

Méthodes importantes
- `RankingDashboard()`

Classes utilisées
- [BuildBox](../src/gui/components/BuildBox.java)
- [SectionTitle](../src/gui/components/SectionTitle.java)

Utilisée par
- [MainGui](../src/gui/frame/MainGui.java)

### MainGui

📄 Fichier
[src/gui/frame/MainGui.java](../src/gui/frame/MainGui.java)

Rôle
Construit la fenêtre principale Swing, gère les `CardLayout` et relie les dashboards entre eux.

Méthodes importantes
- `MainGui()`

Classes utilisées
- [OpeningDashboard](../src/gui/dashboard/OpeningDashboard.java)
- [SidebarPanel](../src/gui/layout/SidebarPanel.java)
- [CalendarDashboard](../src/gui/dashboard/CalendarDashboard.java)
- [FinanceDashboard](../src/gui/dashboard/FinanceDashboard.java)

Utilisée par
- Aucune utilisation directe notable dans `src`.

### SidebarPanel

📄 Fichier
[src/gui/layout/SidebarPanel.java](../src/gui/layout/SidebarPanel.java)

Rôle
Assemble la barre latérale et expose les boutons de navigation de l’application.

Méthodes importantes
- `SidebarPanel()`
- `getMatchButton()`
- `getCalendarButton()`

Classes utilisées
- [League](../src/data/league/League.java)

Utilisée par
- [MainGui](../src/gui/frame/MainGui.java)
