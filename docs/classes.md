# Classes

## Table des matières

### CONFIG

- [FinanceConfiguration](#financeconfiguration) — classe métier
- [FinancialPolicy](#financialpolicy) — classe métier
- [SimulationConfiguration](#simulationconfiguration) — classe métier

### DATA

- [GameDay](#gameday) — calendrier saison
- [NBACalendar](#nbacalendar) — calendrier saison
- [SpecialEvent](#specialevent) — calendrier saison
- [GameStat](#gamestat) — données financières
- [TeamGameFinance](#teamgamefinance) — données financières
- [Budget](#budget) — données financières
- [Expense](#expense) — données financières
- [Income](#income) — données financières
- [Trade](#trade) — données financières
- [Conference](#conference) — structure de ligue
- [Division](#division) — structure de ligue
- [League](#league) — structure de ligue
- [LeagueFinance](#leaguefinance) — structure de ligue
- [Playoff](#playoff) — structure de ligue
- [Ranking](#ranking) — structure de ligue
- [RegularSeason](#regularseason) — structure de ligue
- [Season](#season) — structure de ligue
- [Asset](#asset) — données joueur
- [HealthStatus](#healthstatus) — données joueur
- [Injury](#injury) — données joueur
- [Player](#player) — données joueur
- [OffensiveTry](#offensivetry) — classe métier
- [ActionResult](#actionresult) — classe métier
- [Block](#block) — classe métier
- [EndOfTime](#endoftime) — classe métier
- [PointScored](#pointscored) — classe métier
- [Rebound](#rebound) — classe métier
- [Turnover](#turnover) — classe métier
- [Game](#game) — classe métier
- [GameContext](#gamecontext) — classe métier
- [GameResult](#gameresult) — classe métier
- [Stadium](#stadium) — structure équipe
- [Team](#team) — structure équipe
- [TeamPerformance](#teamperformance) — structure équipe
- [Schedule](#schedule) — structure équipe
- [TeamFinance](#teamfinance) — structure équipe
- [AmbitiousProfil](#ambitiousprofil) — structure équipe
- [BalancedProfil](#balancedprofil) — structure équipe
- [EconomicalProfil](#economicalprofil) — structure équipe
- [FinancialProfil](#financialprofil) — structure équipe
- [LargeSize](#largesize) — structure équipe
- [MarketSize](#marketsize) — structure équipe
- [MediumSize](#mediumsize) — structure équipe
- [SmallSize](#smallsize) — structure équipe
- [AllIn](#allin) — structure équipe
- [Balanced](#balanced) — structure équipe
- [Rebuild](#rebuild) — structure équipe
- [SalaryDump](#salarydump) — structure équipe
- [SmallAdjust](#smalladjust) — structure équipe
- [SuperstarBuild](#superstarbuild) — structure équipe
- [TeamTransferStrategy](#teamtransferstrategy) — structure équipe

### PROCESS

- [LeagueManager (process-process)](#leaguemanager-process-process) — classe métier
- [CalendarBuilder](#calendarbuilder) — construction métier
- [LeagueBuilder](#leaguebuilder) — construction métier
- [GameGenerator](#gamegenerator) — construction métier
- [GameSelector](#gameselector) — construction métier
- [ScheduleReset](#schedulereset) — construction métier
- [SpecialEventPlanner](#specialeventplanner) — construction métier
- [PlayerFactory](#playerfactory) — création entités
- [TeamFactory](#teamfactory) — création entités
- [FinanceManager](#financemanager) — orchestration métier
- [GameManager](#gamemanager) — orchestration métier
- [LeagueManager (process-manager)](#leaguemanager-process-manager) — orchestration métier
- [RevenueSharingManager](#revenuesharingmanager) — orchestration métier
- [SimulationManager](#simulationmanager) — orchestration métier
- [TradeManager](#trademanager) — orchestration métier
- [CurrentSeasonAssetRepositery](#currentseasonassetrepositery) — registre partagé
- [DivisionRepositery](#divisionrepositery) — registre partagé
- [PlayerRepositery](#playerrepositery) — registre partagé
- [PreSeasonAssetRepositery](#preseasonassetrepositery) — registre partagé
- [TeamRepositery](#teamrepositery) — registre partagé
- [GameExpenseSimulator](#gameexpensesimulator) — simulation métier
- [GameRevenueSimulator](#gamerevenuesimulator) — simulation métier
- [GameSimulator](#gamesimulator) — simulation métier
- [TradeSimulator](#tradesimulator) — simulation métier
- [CalendarUtilitary](#calendarutilitary) — utilitaires métier
- [FinanceUtilitary](#financeutilitary) — utilitaires métier
- [PlayerUtilitary](#playerutilitary) — utilitaires métier
- [TeamUtilitary](#teamutilitary) — utilitaires métier
- [TransferStrategyUtilitary](#transferstrategyutilitary) — utilitaires métier
- [ActionResultVisitor](#actionresultvisitor) — classe métier
- [AssetUpdateVisitor](#assetupdatevisitor) — classe métier
- [GameResultVisitor](#gameresultvisitor) — classe métier
- [CalculateBaseTicketVisitor](#calculatebaseticketvisitor) — classe métier
- [CalculateInitialTeamBudgetVisitor](#calculateinitialteambudgetvisitor) — classe métier
- [CalculateStadiumCostVisitor](#calculatestadiumcostvisitor) — classe métier
- [GenerateStadiumCapacityVisitor](#generatestadiumcapacityvisitor) — classe métier
- [MarketSizeVisitor](#marketsizevisitor) — classe métier
- [EvaluateSeasonIntentVisitor](#evaluateseasonintentvisitor) — classe métier
- [PreSeasonPlayerToTradeVisitor](#preseasonplayertotradevisitor) — classe métier
- [PreSeasonTradeSatisfactionVisitor](#preseasontradesatisfactionvisitor) — classe métier
- [SeasonPlayerToTradeVisitor](#seasonplayertotradevisitor) — classe métier
- [SeasonTradeSatisfactionVisitor](#seasontradesatisfactionvisitor) — classe métier
- [TeamTransferVisitor](#teamtransfervisitor) — classe métier

### GUI

- [CalendarDashboard](#calendardashboard) — vue dashboard
- [FinanceDashboard](#financedashboard) — vue dashboard
- [MapDashboard](#mapdashboard) — vue dashboard
- [MatchDashboard](#matchdashboard) — vue dashboard
- [OpeningDashboard](#openingdashboard) — vue dashboard
- [RankingDashboard](#rankingdashboard) — vue dashboard
- [MainGui](#maingui) — fenêtre principale
- [SidebarPanel](#sidebarpanel) — agencement interface
- [CalendarSimulationPanel](#calendarsimulationpanel) — composant interface
- [BuildBox](#buildbox) — composant interface
- [DashboardCard](#dashboardcard) — composant interface
- [PlaceholderPanel](#placeholderpanel) — composant interface
- [SectionTitle](#sectiontitle) — composant interface
- [TitledCard](#titledcard) — composant interface

## CONFIG

### FinanceConfiguration

📄 Fichier
[FinanceConfiguration](../src/config/FinanceConfiguration.java)

Rôle
Centralise des constantes et règles de configuration liées à FinanceConfiguration.

Méthodes importantes
- Aucune méthode publique notable.

Classes utilisées
- [SimulationConfiguration](../src/config/SimulationConfiguration.java)

Utilisée par
- [LeagueFinance](../src/data/league/LeagueFinance.java), [LeagueBuilder](../src/process/builder/LeagueBuilder.java), [TeamFactory](../src/process/factory/TeamFactory.java), [FinanceManager](../src/process/manager/FinanceManager.java)

### FinancialPolicy

📄 Fichier
[FinancialPolicy](../src/config/FinancialPolicy.java)

Rôle
Centralise des constantes et règles de configuration liées à FinancialPolicy.

Méthodes importantes
- Aucune méthode publique notable.

Classes utilisées
- Aucune dépendance métier explicite.

Utilisée par
- [TeamFactory](../src/process/factory/TeamFactory.java), [TradeManager](../src/process/manager/TradeManager.java), [TradeSimulator](../src/process/simulator/TradeSimulator.java), [TeamUtilitary](../src/process/utilitary/TeamUtilitary.java)

### SimulationConfiguration

📄 Fichier
[SimulationConfiguration](../src/config/SimulationConfiguration.java)

Rôle
Centralise des constantes et règles de configuration liées à SimulationConfiguration.

Méthodes importantes
- Aucune méthode publique notable.

Classes utilisées
- Aucune dépendance métier explicite.

Utilisée par
- [FinanceConfiguration](../src/config/FinanceConfiguration.java), [League](../src/data/league/League.java), [LeagueFinance](../src/data/league/LeagueFinance.java), [HealthStatus](../src/data/player/HealthStatus.java)

## DATA

### GameDay

📄 Fichier
[GameDay](../src/data/calendar/GameDay.java)

Rôle
Représente une entité du modèle métier pour calendrier saison.

Méthodes importantes
- `GameDay()`
- `getDate()`
- `isEmpty()`

Classes utilisées
- [Game](../src/data/sport/setup/Game.java)

Utilisée par
- [NBACalendar](../src/data/calendar/NBACalendar.java), [SpecialEvent](../src/data/calendar/SpecialEvent.java), [CalendarSimulationPanel](../src/gui/panel/calendarPanel/CalendarSimulationPanel.java), [CalendarBuilder](../src/process/builder/CalendarBuilder.java)

### NBACalendar

📄 Fichier
[NBACalendar](../src/data/calendar/NBACalendar.java)

Rôle
Représente une entité du modèle métier pour calendrier saison.

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
[SpecialEvent](../src/data/calendar/SpecialEvent.java)

Rôle
Représente une entité du modèle métier pour calendrier saison.

Méthodes importantes
- `SpecialEvent()`

Classes utilisées
- [GameDay](../src/data/calendar/GameDay.java)

Utilisée par
- [Season](../src/data/league/Season.java), [SpecialEventPlanner](../src/process/builder/calendartools/SpecialEventPlanner.java)

### GameStat

📄 Fichier
[GameStat](../src/data/finance/GameStat.java)

Rôle
Représente une entité du modèle métier pour données financières.

Méthodes importantes
- `GameStat()`
- `getGame()`
- `setGame()`

Classes utilisées
- [Game](../src/data/sport/setup/Game.java), [TeamGameFinance](../src/data/finance/TeamGameFinance.java)

Utilisée par
- [FinanceManager](../src/process/manager/FinanceManager.java), [GameExpenseSimulator](../src/process/simulator/GameExpenseSimulator.java), [GameRevenueSimulator](../src/process/simulator/GameRevenueSimulator.java), [FinanceUtilitary](../src/process/utilitary/FinanceUtilitary.java)

### TeamGameFinance

📄 Fichier
[TeamGameFinance](../src/data/finance/TeamGameFinance.java)

Rôle
Représente une entité du modèle métier pour données financières.

Méthodes importantes
- `TeamGameFinance()`
- `getTicketRevenue()`
- `setTicketRevenue()`

Classes utilisées
- Aucune dépendance métier explicite.

Utilisée par
- [GameStat](../src/data/finance/GameStat.java), [FinanceUtilitary](../src/process/utilitary/FinanceUtilitary.java)

### Budget

📄 Fichier
[Budget](../src/data/finance/budget/Budget.java)

Rôle
Représente une entité du modèle métier pour données financières.

Méthodes importantes
- `Budget()`
- `getInitialAmount()`
- `setInitialAmount()`

Classes utilisées
- [Expense](../src/data/finance/budget/Expense.java), [Income](../src/data/finance/budget/Income.java)

Utilisée par
- [LeagueFinance](../src/data/league/LeagueFinance.java), [TeamFinance](../src/data/team/finance/TeamFinance.java), [LeagueBuilder](../src/process/builder/LeagueBuilder.java), [TeamFactory](../src/process/factory/TeamFactory.java)

### Expense

📄 Fichier
[Expense](../src/data/finance/budget/Expense.java)

Rôle
Représente une entité du modèle métier pour données financières.

Méthodes importantes
- `Expense()`
- `getName()`
- `getAmount()`

Classes utilisées
- Aucune dépendance métier explicite.

Utilisée par
- [Budget](../src/data/finance/budget/Budget.java), [RevenueSharingManager](../src/process/manager/RevenueSharingManager.java), [TradeSimulator](../src/process/simulator/TradeSimulator.java), [FinanceUtilitary](../src/process/utilitary/FinanceUtilitary.java)

### Income

📄 Fichier
[Income](../src/data/finance/budget/Income.java)

Rôle
Représente une entité du modèle métier pour données financières.

Méthodes importantes
- `Income()`
- `getName()`
- `getAmount()`

Classes utilisées
- Aucune dépendance métier explicite.

Utilisée par
- [Budget](../src/data/finance/budget/Budget.java), [LeagueFinance](../src/data/league/LeagueFinance.java), [LeagueBuilder](../src/process/builder/LeagueBuilder.java), [FinanceManager](../src/process/manager/FinanceManager.java)

### Trade

📄 Fichier
[Trade](../src/data/finance/transfer/Trade.java)

Rôle
Représente une entité du modèle métier pour données financières.

Méthodes importantes
- `Trade()`
- `getPlayerA()`
- `setPlayerA()`

Classes utilisées
- [Player](../src/data/player/Player.java), [Team](../src/data/team/Team.java)

Utilisée par
- [TradeManager](../src/process/manager/TradeManager.java)

### Conference

📄 Fichier
[Conference](../src/data/league/Conference.java)

Rôle
Représente une entité du modèle métier pour structure de ligue.

Méthodes importantes
- `Conference()`
- `getDivisions()`
- `setDivisions()`

Classes utilisées
- [Team](../src/data/team/Team.java), [Division](../src/data/league/Division.java)

Utilisée par
- [League](../src/data/league/League.java), [GameGenerator](../src/process/builder/calendartools/GameGenerator.java)

### Division

📄 Fichier
[Division](../src/data/league/Division.java)

Rôle
Représente une entité du modèle métier pour structure de ligue.

Méthodes importantes
- `Division()`
- `getTeams()`
- `setTeams()`

Classes utilisées
- [Team](../src/data/team/Team.java)

Utilisée par
- [Conference](../src/data/league/Conference.java), [League](../src/data/league/League.java), [LeagueBuilder](../src/process/builder/LeagueBuilder.java), [GameGenerator](../src/process/builder/calendartools/GameGenerator.java)

### League

📄 Fichier
[League](../src/data/league/League.java)

Rôle
Représente une entité du modèle métier pour structure de ligue.

Méthodes importantes
- `League()`
- `getWesternConference()`
- `setWesternConference()`

Classes utilisées
- [SimulationConfiguration](../src/config/SimulationConfiguration.java), [Player](../src/data/player/Player.java), [Team](../src/data/team/Team.java), [Conference](../src/data/league/Conference.java)

Utilisée par
- [MainGui](../src/gui/frame/MainGui.java), [SidebarPanel](../src/gui/layout/SidebarPanel.java), [LeagueManager](../src/process/LeagueManager.java), [CalendarBuilder](../src/process/builder/CalendarBuilder.java)

### LeagueFinance

📄 Fichier
[LeagueFinance](../src/data/league/LeagueFinance.java)

Rôle
Représente une entité du modèle métier pour structure de ligue.

Méthodes importantes
- `LeagueFinance()`
- `getBudget()`
- `getSalaryCap()`

Classes utilisées
- [FinanceConfiguration](../src/config/FinanceConfiguration.java), [SimulationConfiguration](../src/config/SimulationConfiguration.java), [Budget](../src/data/finance/budget/Budget.java), [Income](../src/data/finance/budget/Income.java)

Utilisée par
- [League](../src/data/league/League.java), [LeagueBuilder](../src/process/builder/LeagueBuilder.java), [TradeSimulator](../src/process/simulator/TradeSimulator.java)

### Playoff

📄 Fichier
[Playoff](../src/data/league/Playoff.java)

Rôle
Représente une entité du modèle métier pour structure de ligue.

Méthodes importantes
- `Playoff()`

Classes utilisées
- [Season](../src/data/league/Season.java)

Utilisée par
- [League](../src/data/league/League.java), [GameManager](../src/process/manager/GameManager.java)

### Ranking

📄 Fichier
[Ranking](../src/data/league/Ranking.java)

Rôle
Représente une entité du modèle métier pour structure de ligue.

Méthodes importantes
- `Ranking()`

Classes utilisées
- [Team](../src/data/team/Team.java)

Utilisée par
- [Season](../src/data/league/Season.java)

### RegularSeason

📄 Fichier
[RegularSeason](../src/data/league/RegularSeason.java)

Rôle
Représente une entité du modèle métier pour structure de ligue.

Méthodes importantes
- `RegularSeason()`

Classes utilisées
- [Season](../src/data/league/Season.java)

Utilisée par
- [League](../src/data/league/League.java), [CalendarSimulationPanel](../src/gui/panel/calendarPanel/CalendarSimulationPanel.java), [LeagueManager](../src/process/LeagueManager.java), [CalendarBuilder](../src/process/builder/CalendarBuilder.java)

### Season

📄 Fichier
[Season](../src/data/league/Season.java)

Rôle
Représente une entité du modèle métier pour structure de ligue.

Méthodes importantes
- `Season()`
- `getDebutDate()`
- `setDebutDate()`

Classes utilisées
- [NBACalendar](../src/data/calendar/NBACalendar.java), [SpecialEvent](../src/data/calendar/SpecialEvent.java), [Ranking](../src/data/league/Ranking.java)

Utilisée par
- [Playoff](../src/data/league/Playoff.java), [RegularSeason](../src/data/league/RegularSeason.java)

### Asset

📄 Fichier
[Asset](../src/data/player/Asset.java)

Rôle
Représente une entité du modèle métier pour données joueur.

Méthodes importantes
- `Asset()`
- `getNote()`
- `setNote()`

Classes utilisées
- Aucune dépendance métier explicite.

Utilisée par
- [Player](../src/data/player/Player.java), [PlayerFactory](../src/process/factory/PlayerFactory.java), [CurrentSeasonAssetRepositery](../src/process/repositery/CurrentSeasonAssetRepositery.java), [PreSeasonAssetRepositery](../src/process/repositery/PreSeasonAssetRepositery.java)

### HealthStatus

📄 Fichier
[HealthStatus](../src/data/player/HealthStatus.java)

Rôle
Représente une entité du modèle métier pour données joueur.

Méthodes importantes
- `HealthStatus()`
- `getFatigue()`
- `setFatigue()`

Classes utilisées
- [SimulationConfiguration](../src/config/SimulationConfiguration.java), [Injury](../src/data/player/Injury.java)

Utilisée par
- [Player](../src/data/player/Player.java), [GameSimulator](../src/process/simulator/GameSimulator.java), [PlayerUtilitary](../src/process/utilitary/PlayerUtilitary.java)

### Injury

📄 Fichier
[Injury](../src/data/player/Injury.java)

Rôle
Représente une entité du modèle métier pour données joueur.

Méthodes importantes
- `Injury()`
- `getInjuryType()`
- `setInjuryType()`

Classes utilisées
- Aucune dépendance métier explicite.

Utilisée par
- [HealthStatus](../src/data/player/HealthStatus.java), [GameSimulator](../src/process/simulator/GameSimulator.java)

### Player

📄 Fichier
[Player](../src/data/player/Player.java)

Rôle
Représente une entité du modèle métier pour données joueur.

Méthodes importantes
- `Player()`
- `isStar()`
- `setStar()`

Classes utilisées
- [Asset](../src/data/player/Asset.java), [HealthStatus](../src/data/player/HealthStatus.java)

Utilisée par
- [Trade](../src/data/finance/transfer/Trade.java), [League](../src/data/league/League.java), [Block](../src/data/sport/play/action/Block.java), [PointScored](../src/data/sport/play/action/PointScored.java)

### OffensiveTry

📄 Fichier
[OffensiveTry](../src/data/sport/play/OffensiveTry.java)

Rôle
Représente une entité du modèle métier pour classe métier.

Méthodes importantes
- `OffensiveTry()`
- `getName()`

Classes utilisées
- Aucune dépendance métier explicite.

Utilisée par
- [ActionResult](../src/data/sport/play/action/ActionResult.java), [GameSimulator](../src/process/simulator/GameSimulator.java)

### ActionResult

📄 Fichier
[ActionResult](../src/data/sport/play/action/ActionResult.java)

Rôle
Représente une entité du modèle métier pour classe métier.

Méthodes importantes
- `ActionResult()`
- `getName()`
- `setName()`

Classes utilisées
- [OffensiveTry](../src/data/sport/play/OffensiveTry.java), [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java)

Utilisée par
- [Block](../src/data/sport/play/action/Block.java), [EndOfTime](../src/data/sport/play/action/EndOfTime.java), [PointScored](../src/data/sport/play/action/PointScored.java), [Rebound](../src/data/sport/play/action/Rebound.java)

### Block

📄 Fichier
[Block](../src/data/sport/play/action/Block.java)

Rôle
Représente une entité du modèle métier pour classe métier.

Méthodes importantes
- `Block()`
- `getBlockingPlayer()`
- `setBlockingPlayer()`

Classes utilisées
- [Player](../src/data/player/Player.java), [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java), [ActionResult](../src/data/sport/play/action/ActionResult.java)

Utilisée par
- [GameSimulator](../src/process/simulator/GameSimulator.java), [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java), [AssetUpdateVisitor](../src/process/visitor/actionresult/AssetUpdateVisitor.java), [GameResultVisitor](../src/process/visitor/actionresult/GameResultVisitor.java)

### EndOfTime

📄 Fichier
[EndOfTime](../src/data/sport/play/action/EndOfTime.java)

Rôle
Représente une entité du modèle métier pour classe métier.

Méthodes importantes
- `EndOfTime()`
- `accept()`

Classes utilisées
- [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java), [ActionResult](../src/data/sport/play/action/ActionResult.java)

Utilisée par
- [GameSimulator](../src/process/simulator/GameSimulator.java), [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java), [AssetUpdateVisitor](../src/process/visitor/actionresult/AssetUpdateVisitor.java), [GameResultVisitor](../src/process/visitor/actionresult/GameResultVisitor.java)

### PointScored

📄 Fichier
[PointScored](../src/data/sport/play/action/PointScored.java)

Rôle
Représente une entité du modèle métier pour classe métier.

Méthodes importantes
- `PointScored()`
- `getPointsScored()`
- `getScorerPlayer()`

Classes utilisées
- [Player](../src/data/player/Player.java), [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java), [ActionResult](../src/data/sport/play/action/ActionResult.java)

Utilisée par
- [GameSimulator](../src/process/simulator/GameSimulator.java), [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java), [AssetUpdateVisitor](../src/process/visitor/actionresult/AssetUpdateVisitor.java), [GameResultVisitor](../src/process/visitor/actionresult/GameResultVisitor.java)

### Rebound

📄 Fichier
[Rebound](../src/data/sport/play/action/Rebound.java)

Rôle
Représente une entité du modèle métier pour classe métier.

Méthodes importantes
- `Rebound()`
- `getReboundPlayer()`
- `setReboundPlayer()`

Classes utilisées
- [Player](../src/data/player/Player.java), [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java), [ActionResult](../src/data/sport/play/action/ActionResult.java)

Utilisée par
- [GameSimulator](../src/process/simulator/GameSimulator.java), [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java), [AssetUpdateVisitor](../src/process/visitor/actionresult/AssetUpdateVisitor.java), [GameResultVisitor](../src/process/visitor/actionresult/GameResultVisitor.java)

### Turnover

📄 Fichier
[Turnover](../src/data/sport/play/action/Turnover.java)

Rôle
Représente une entité du modèle métier pour classe métier.

Méthodes importantes
- `Turnover()`
- `getInterceptedPlayer()`
- `getDefensePlayer()`

Classes utilisées
- [Player](../src/data/player/Player.java), [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java), [ActionResult](../src/data/sport/play/action/ActionResult.java)

Utilisée par
- [GameSimulator](../src/process/simulator/GameSimulator.java), [ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java), [AssetUpdateVisitor](../src/process/visitor/actionresult/AssetUpdateVisitor.java), [GameResultVisitor](../src/process/visitor/actionresult/GameResultVisitor.java)

### Game

📄 Fichier
[Game](../src/data/sport/setup/Game.java)

Rôle
Représente une entité du modèle métier pour classe métier.

Méthodes importantes
- `Game()`
- `getGameContext()`
- `setGameContext()`

Classes utilisées
- [GameContext](../src/data/sport/setup/GameContext.java), [GameResult](../src/data/sport/setup/GameResult.java)

Utilisée par
- [GameDay](../src/data/calendar/GameDay.java), [GameStat](../src/data/finance/GameStat.java), [Team](../src/data/team/Team.java), [Schedule](../src/data/team/calendar/Schedule.java)

### GameContext

📄 Fichier
[GameContext](../src/data/sport/setup/GameContext.java)

Rôle
Représente une entité du modèle métier pour classe métier.

Méthodes importantes
- `GameContext()`
- `isScheduled()`
- `setScheduled()`

Classes utilisées
- [Team](../src/data/team/Team.java), [CalendarUtilitary](../src/process/utilitary/CalendarUtilitary.java)

Utilisée par
- [Game](../src/data/sport/setup/Game.java), [Team](../src/data/team/Team.java), [GameGenerator](../src/process/builder/calendartools/GameGenerator.java), [CalendarUtilitary](../src/process/utilitary/CalendarUtilitary.java)

### GameResult

📄 Fichier
[GameResult](../src/data/sport/setup/GameResult.java)

Rôle
Représente une entité du modèle métier pour classe métier.

Méthodes importantes
- `GameResult()`
- `addActions()`
- `getScorehomeTeam()`

Classes utilisées
- [Player](../src/data/player/Player.java), [ActionResult](../src/data/sport/play/action/ActionResult.java), [Team](../src/data/team/Team.java)

Utilisée par
- [Game](../src/data/sport/setup/Game.java), [GameSimulator](../src/process/simulator/GameSimulator.java), [GameResultVisitor](../src/process/visitor/actionresult/GameResultVisitor.java)

### Stadium

📄 Fichier
[Stadium](../src/data/team/Stadium.java)

Rôle
Représente une entité du modèle métier pour structure équipe.

Méthodes importantes
- `Stadium()`
- `getName()`
- `setName()`

Classes utilisées
- Aucune dépendance métier explicite.

Utilisée par
- [Team](../src/data/team/Team.java), [TeamFactory](../src/process/factory/TeamFactory.java), [GameRevenueSimulator](../src/process/simulator/GameRevenueSimulator.java)

### Team

📄 Fichier
[Team](../src/data/team/Team.java)

Rôle
Représente une entité du modèle métier pour structure équipe.

Méthodes importantes
- `Team()`
- `getName()`
- `setNom()`

Classes utilisées
- [SimulationConfiguration](../src/config/SimulationConfiguration.java), [Player](../src/data/player/Player.java), [Game](../src/data/sport/setup/Game.java), [GameContext](../src/data/sport/setup/GameContext.java)

Utilisée par
- [Trade](../src/data/finance/transfer/Trade.java), [Conference](../src/data/league/Conference.java), [Division](../src/data/league/Division.java), [League](../src/data/league/League.java)

### TeamPerformance

📄 Fichier
[TeamPerformance](../src/data/team/TeamPerformance.java)

Rôle
Représente une entité du modèle métier pour structure équipe.

Méthodes importantes
- `TeamPerformance()`
- `getPerformanceRating()`
- `setPerformanceRating()`

Classes utilisées
- Aucune dépendance métier explicite.

Utilisée par
- [Team](../src/data/team/Team.java)

### Schedule

📄 Fichier
[Schedule](../src/data/team/calendar/Schedule.java)

Rôle
Représente une entité du modèle métier pour structure équipe.

Méthodes importantes
- `Schedule()`
- `getNumberOfPlayedGames()`
- `setNumberOfPlayedGames()`

Classes utilisées
- [Game](../src/data/sport/setup/Game.java)

Utilisée par
- [Team](../src/data/team/Team.java), [GameSelector](../src/process/builder/calendartools/GameSelector.java)

### TeamFinance

📄 Fichier
[TeamFinance](../src/data/team/finance/TeamFinance.java)

Rôle
Représente une entité du modèle métier pour structure équipe.

Méthodes importantes
- `TeamFinance()`
- `getFinancialProfil()`
- `getPayroll()`

Classes utilisées
- [Budget](../src/data/finance/budget/Budget.java), [FinancialProfil](../src/data/team/finance/financialprofil/FinancialProfil.java), [MarketSize](../src/data/team/finance/marketsize/MarketSize.java), [TeamTransferStrategy](../src/data/team/finance/transfer/TeamTransferStrategy.java)

Utilisée par
- [Team](../src/data/team/Team.java), [TeamFactory](../src/process/factory/TeamFactory.java)

### AmbitiousProfil

📄 Fichier
[AmbitiousProfil](../src/data/team/finance/financialprofil/AmbitiousProfil.java)

Rôle
Représente une entité du modèle métier pour structure équipe.

Méthodes importantes
- `AmbitiousProfil()`

Classes utilisées
- [FinancialProfil](../src/data/team/finance/financialprofil/FinancialProfil.java)

Utilisée par
- [GameSimulator](../src/process/simulator/GameSimulator.java), [TeamUtilitary](../src/process/utilitary/TeamUtilitary.java)

### BalancedProfil

📄 Fichier
[BalancedProfil](../src/data/team/finance/financialprofil/BalancedProfil.java)

Rôle
Représente une entité du modèle métier pour structure équipe.

Méthodes importantes
- `BalancedProfil()`

Classes utilisées
- [FinancialProfil](../src/data/team/finance/financialprofil/FinancialProfil.java)

Utilisée par
- [TeamFactory](../src/process/factory/TeamFactory.java), [GameSimulator](../src/process/simulator/GameSimulator.java), [TeamUtilitary](../src/process/utilitary/TeamUtilitary.java)

### EconomicalProfil

📄 Fichier
[EconomicalProfil](../src/data/team/finance/financialprofil/EconomicalProfil.java)

Rôle
Représente une entité du modèle métier pour structure équipe.

Méthodes importantes
- `EconomicalProfil()`

Classes utilisées
- [FinancialProfil](../src/data/team/finance/financialprofil/FinancialProfil.java)

Utilisée par
- [TeamUtilitary](../src/process/utilitary/TeamUtilitary.java)

### FinancialProfil

📄 Fichier
[FinancialProfil](../src/data/team/finance/financialprofil/FinancialProfil.java)

Rôle
Représente une entité du modèle métier pour structure équipe.

Méthodes importantes
- `FinancialProfil()`
- `getName()`

Classes utilisées
- Aucune dépendance métier explicite.

Utilisée par
- [Team](../src/data/team/Team.java), [TeamFinance](../src/data/team/finance/TeamFinance.java), [AmbitiousProfil](../src/data/team/finance/financialprofil/AmbitiousProfil.java), [BalancedProfil](../src/data/team/finance/financialprofil/BalancedProfil.java)

### LargeSize

📄 Fichier
[LargeSize](../src/data/team/finance/marketsize/LargeSize.java)

Rôle
Représente une entité du modèle métier pour structure équipe.

Méthodes importantes
- `LargeSize()`
- `accept()`

Classes utilisées
- [MarketSizeVisitor](../src/process/visitor/marketsize/MarketSizeVisitor.java), [MarketSize](../src/data/team/finance/marketsize/MarketSize.java)

Utilisée par
- [TeamFactory](../src/process/factory/TeamFactory.java), [TeamUtilitary](../src/process/utilitary/TeamUtilitary.java), [CalculateBaseTicketVisitor](../src/process/visitor/marketsize/CalculateBaseTicketVisitor.java), [CalculateInitialTeamBudgetVisitor](../src/process/visitor/marketsize/CalculateInitialTeamBudgetVisitor.java)

### MarketSize

📄 Fichier
[MarketSize](../src/data/team/finance/marketsize/MarketSize.java)

Rôle
Représente une entité du modèle métier pour structure équipe.

Méthodes importantes
- `MarketSize()`
- `getSize()`
- `setSize()`

Classes utilisées
- [MarketSizeVisitor](../src/process/visitor/marketsize/MarketSizeVisitor.java)

Utilisée par
- [TeamFinance](../src/data/team/finance/TeamFinance.java), [LargeSize](../src/data/team/finance/marketsize/LargeSize.java), [MediumSize](../src/data/team/finance/marketsize/MediumSize.java), [SmallSize](../src/data/team/finance/marketsize/SmallSize.java)

### MediumSize

📄 Fichier
[MediumSize](../src/data/team/finance/marketsize/MediumSize.java)

Rôle
Représente une entité du modèle métier pour structure équipe.

Méthodes importantes
- `MediumSize()`
- `accept()`

Classes utilisées
- [MarketSizeVisitor](../src/process/visitor/marketsize/MarketSizeVisitor.java), [MarketSize](../src/data/team/finance/marketsize/MarketSize.java)

Utilisée par
- [TeamFactory](../src/process/factory/TeamFactory.java), [TeamUtilitary](../src/process/utilitary/TeamUtilitary.java), [CalculateBaseTicketVisitor](../src/process/visitor/marketsize/CalculateBaseTicketVisitor.java), [CalculateInitialTeamBudgetVisitor](../src/process/visitor/marketsize/CalculateInitialTeamBudgetVisitor.java)

### SmallSize

📄 Fichier
[SmallSize](../src/data/team/finance/marketsize/SmallSize.java)

Rôle
Représente une entité du modèle métier pour structure équipe.

Méthodes importantes
- `SmallSize()`
- `accept()`

Classes utilisées
- [MarketSizeVisitor](../src/process/visitor/marketsize/MarketSizeVisitor.java), [MarketSize](../src/data/team/finance/marketsize/MarketSize.java)

Utilisée par
- [TeamFactory](../src/process/factory/TeamFactory.java), [TeamUtilitary](../src/process/utilitary/TeamUtilitary.java), [CalculateBaseTicketVisitor](../src/process/visitor/marketsize/CalculateBaseTicketVisitor.java), [CalculateInitialTeamBudgetVisitor](../src/process/visitor/marketsize/CalculateInitialTeamBudgetVisitor.java)

### AllIn

📄 Fichier
[AllIn](../src/data/team/finance/transfer/AllIn.java)

Rôle
Représente une entité du modèle métier pour structure équipe.

Méthodes importantes
- `AllIn()`
- `accept()`

Classes utilisées
- [Player](../src/data/player/Player.java), [TeamTransferVisitor](../src/process/visitor/teamtransfer/TeamTransferVisitor.java), [TeamTransferStrategy](../src/data/team/finance/transfer/TeamTransferStrategy.java)

Utilisée par
- [TransferStrategyUtilitary](../src/process/utilitary/TransferStrategyUtilitary.java), [EvaluateSeasonIntentVisitor](../src/process/visitor/teamtransfer/EvaluateSeasonIntentVisitor.java), [PreSeasonPlayerToTradeVisitor](../src/process/visitor/teamtransfer/PreSeasonPlayerToTradeVisitor.java), [PreSeasonTradeSatisfactionVisitor](../src/process/visitor/teamtransfer/PreSeasonTradeSatisfactionVisitor.java)

### Balanced

📄 Fichier
[Balanced](../src/data/team/finance/transfer/Balanced.java)

Rôle
Représente une entité du modèle métier pour structure équipe.

Méthodes importantes
- `Balanced()`
- `accept()`

Classes utilisées
- [Player](../src/data/player/Player.java), [TeamTransferVisitor](../src/process/visitor/teamtransfer/TeamTransferVisitor.java), [TeamTransferStrategy](../src/data/team/finance/transfer/TeamTransferStrategy.java)

Utilisée par
- [TransferStrategyUtilitary](../src/process/utilitary/TransferStrategyUtilitary.java), [EvaluateSeasonIntentVisitor](../src/process/visitor/teamtransfer/EvaluateSeasonIntentVisitor.java), [PreSeasonPlayerToTradeVisitor](../src/process/visitor/teamtransfer/PreSeasonPlayerToTradeVisitor.java), [PreSeasonTradeSatisfactionVisitor](../src/process/visitor/teamtransfer/PreSeasonTradeSatisfactionVisitor.java)

### Rebuild

📄 Fichier
[Rebuild](../src/data/team/finance/transfer/Rebuild.java)

Rôle
Représente une entité du modèle métier pour structure équipe.

Méthodes importantes
- `Rebuild()`
- `accept()`

Classes utilisées
- [Player](../src/data/player/Player.java), [TeamTransferVisitor](../src/process/visitor/teamtransfer/TeamTransferVisitor.java), [TeamTransferStrategy](../src/data/team/finance/transfer/TeamTransferStrategy.java)

Utilisée par
- [TransferStrategyUtilitary](../src/process/utilitary/TransferStrategyUtilitary.java), [EvaluateSeasonIntentVisitor](../src/process/visitor/teamtransfer/EvaluateSeasonIntentVisitor.java), [PreSeasonPlayerToTradeVisitor](../src/process/visitor/teamtransfer/PreSeasonPlayerToTradeVisitor.java), [PreSeasonTradeSatisfactionVisitor](../src/process/visitor/teamtransfer/PreSeasonTradeSatisfactionVisitor.java)

### SalaryDump

📄 Fichier
[SalaryDump](../src/data/team/finance/transfer/SalaryDump.java)

Rôle
Représente une entité du modèle métier pour structure équipe.

Méthodes importantes
- `SalaryDump()`
- `accept()`

Classes utilisées
- [Player](../src/data/player/Player.java), [TeamTransferVisitor](../src/process/visitor/teamtransfer/TeamTransferVisitor.java), [TeamTransferStrategy](../src/data/team/finance/transfer/TeamTransferStrategy.java)

Utilisée par
- [TransferStrategyUtilitary](../src/process/utilitary/TransferStrategyUtilitary.java), [EvaluateSeasonIntentVisitor](../src/process/visitor/teamtransfer/EvaluateSeasonIntentVisitor.java), [PreSeasonPlayerToTradeVisitor](../src/process/visitor/teamtransfer/PreSeasonPlayerToTradeVisitor.java), [PreSeasonTradeSatisfactionVisitor](../src/process/visitor/teamtransfer/PreSeasonTradeSatisfactionVisitor.java)

### SmallAdjust

📄 Fichier
[SmallAdjust](../src/data/team/finance/transfer/SmallAdjust.java)

Rôle
Représente une entité du modèle métier pour structure équipe.

Méthodes importantes
- `SmallAdjust()`
- `accept()`

Classes utilisées
- [Player](../src/data/player/Player.java), [TeamTransferVisitor](../src/process/visitor/teamtransfer/TeamTransferVisitor.java), [TeamTransferStrategy](../src/data/team/finance/transfer/TeamTransferStrategy.java)

Utilisée par
- [TransferStrategyUtilitary](../src/process/utilitary/TransferStrategyUtilitary.java), [EvaluateSeasonIntentVisitor](../src/process/visitor/teamtransfer/EvaluateSeasonIntentVisitor.java), [PreSeasonPlayerToTradeVisitor](../src/process/visitor/teamtransfer/PreSeasonPlayerToTradeVisitor.java), [PreSeasonTradeSatisfactionVisitor](../src/process/visitor/teamtransfer/PreSeasonTradeSatisfactionVisitor.java)

### SuperstarBuild

📄 Fichier
[SuperstarBuild](../src/data/team/finance/transfer/SuperstarBuild.java)

Rôle
Représente une entité du modèle métier pour structure équipe.

Méthodes importantes
- `SuperstarBuild()`
- `accept()`

Classes utilisées
- [Player](../src/data/player/Player.java), [TeamTransferVisitor](../src/process/visitor/teamtransfer/TeamTransferVisitor.java), [TeamTransferStrategy](../src/data/team/finance/transfer/TeamTransferStrategy.java)

Utilisée par
- [TransferStrategyUtilitary](../src/process/utilitary/TransferStrategyUtilitary.java), [EvaluateSeasonIntentVisitor](../src/process/visitor/teamtransfer/EvaluateSeasonIntentVisitor.java), [PreSeasonPlayerToTradeVisitor](../src/process/visitor/teamtransfer/PreSeasonPlayerToTradeVisitor.java), [PreSeasonTradeSatisfactionVisitor](../src/process/visitor/teamtransfer/PreSeasonTradeSatisfactionVisitor.java)

### TeamTransferStrategy

📄 Fichier
[TeamTransferStrategy](../src/data/team/finance/transfer/TeamTransferStrategy.java)

Rôle
Représente une entité du modèle métier pour structure équipe.

Méthodes importantes
- `TeamTransferStrategy()`
- `getName()`
- `setName()`

Classes utilisées
- [Player](../src/data/player/Player.java), [TeamTransferVisitor](../src/process/visitor/teamtransfer/TeamTransferVisitor.java)

Utilisée par
- [Team](../src/data/team/Team.java), [TeamFinance](../src/data/team/finance/TeamFinance.java), [AllIn](../src/data/team/finance/transfer/AllIn.java), [Balanced](../src/data/team/finance/transfer/Balanced.java)

## PROCESS

### LeagueManager (process-process)

📄 Fichier
[LeagueManager](../src/process/LeagueManager.java)

Rôle
Implémente une partie de la logique applicative liée à classe métier.

Méthodes importantes
- `LeagueManager()`
- `buildLeague()`
- `buildRegularSeasonCalendar()`

Classes utilisées
- [SimulationConfiguration](../src/config/SimulationConfiguration.java), [League](../src/data/league/League.java), [RegularSeason](../src/data/league/RegularSeason.java), [CalendarBuilder](../src/process/builder/CalendarBuilder.java)

Utilisée par
- [CalendarSimulationPanel](../src/gui/panel/calendarPanel/CalendarSimulationPanel.java), [SimulationManager](../src/process/manager/SimulationManager.java)

### CalendarBuilder

📄 Fichier
[CalendarBuilder](../src/process/builder/CalendarBuilder.java)

Rôle
Implémente une partie de la logique applicative liée à construction métier.

Méthodes importantes
- `CalendarBuilder()`
- `buildRegulaSeasonCalendar()`

Classes utilisées
- [SimulationConfiguration](../src/config/SimulationConfiguration.java), [GameDay](../src/data/calendar/GameDay.java), [League](../src/data/league/League.java), [RegularSeason](../src/data/league/RegularSeason.java)

Utilisée par
- [LeagueManager](../src/process/LeagueManager.java), [LeagueManager](../src/process/manager/LeagueManager.java)

### LeagueBuilder

📄 Fichier
[LeagueBuilder](../src/process/builder/LeagueBuilder.java)

Rôle
Implémente une partie de la logique applicative liée à construction métier.

Méthodes importantes
- `LeagueBuilder()`
- `build()`

Classes utilisées
- [FinanceConfiguration](../src/config/FinanceConfiguration.java), [SimulationConfiguration](../src/config/SimulationConfiguration.java), [Budget](../src/data/finance/budget/Budget.java), [Income](../src/data/finance/budget/Income.java)

Utilisée par
- [LeagueManager](../src/process/LeagueManager.java), [LeagueManager](../src/process/manager/LeagueManager.java)

### GameGenerator

📄 Fichier
[GameGenerator](../src/process/builder/calendartools/GameGenerator.java)

Rôle
Implémente une partie de la logique applicative liée à construction métier.

Méthodes importantes
- `generateAllGamesRegularSeason()`
- `generateInterConference()`

Classes utilisées
- [SimulationConfiguration](../src/config/SimulationConfiguration.java), [Conference](../src/data/league/Conference.java), [Division](../src/data/league/Division.java), [League](../src/data/league/League.java)

Utilisée par
- [CalendarBuilder](../src/process/builder/CalendarBuilder.java)

### GameSelector

📄 Fichier
[GameSelector](../src/process/builder/calendartools/GameSelector.java)

Rôle
Implémente une partie de la logique applicative liée à construction métier.

Méthodes importantes
- `GameSelector()`
- `selectGamesForDay()`
- `setDate()`

Classes utilisées
- [SimulationConfiguration](../src/config/SimulationConfiguration.java), [League](../src/data/league/League.java), [RegularSeason](../src/data/league/RegularSeason.java), [Game](../src/data/sport/setup/Game.java)

Utilisée par
- [CalendarBuilder](../src/process/builder/CalendarBuilder.java)

### ScheduleReset

📄 Fichier
[ScheduleReset](../src/process/builder/calendartools/ScheduleReset.java)

Rôle
Implémente une partie de la logique applicative liée à construction métier.

Méthodes importantes
- `initialization()`

Classes utilisées
- [Team](../src/data/team/Team.java), [TeamRepositery](../src/process/repositery/TeamRepositery.java)

Utilisée par
- [CalendarBuilder](../src/process/builder/CalendarBuilder.java)

### SpecialEventPlanner

📄 Fichier
[SpecialEventPlanner](../src/process/builder/calendartools/SpecialEventPlanner.java)

Rôle
Implémente une partie de la logique applicative liée à construction métier.

Méthodes importantes
- `specialEventsPlacement()`

Classes utilisées
- [SimulationConfiguration](../src/config/SimulationConfiguration.java), [SpecialEvent](../src/data/calendar/SpecialEvent.java), [RegularSeason](../src/data/league/RegularSeason.java), [CalendarUtilitary](../src/process/utilitary/CalendarUtilitary.java)

Utilisée par
- [CalendarBuilder](../src/process/builder/CalendarBuilder.java)

### PlayerFactory

📄 Fichier
[PlayerFactory](../src/process/factory/PlayerFactory.java)

Rôle
Implémente une partie de la logique applicative liée à création entités.

Méthodes importantes
- `createPlayer()`

Classes utilisées
- [Asset](../src/data/player/Asset.java), [Player](../src/data/player/Player.java)

Utilisée par
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)

### TeamFactory

📄 Fichier
[TeamFactory](../src/process/factory/TeamFactory.java)

Rôle
Implémente une partie de la logique applicative liée à création entités.

Méthodes importantes
- `createTeam()`

Classes utilisées
- [FinanceConfiguration](../src/config/FinanceConfiguration.java), [FinancialPolicy](../src/config/FinancialPolicy.java), [SimulationConfiguration](../src/config/SimulationConfiguration.java), [Budget](../src/data/finance/budget/Budget.java)

Utilisée par
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)

### FinanceManager

📄 Fichier
[FinanceManager](../src/process/manager/FinanceManager.java)

Rôle
Implémente une partie de la logique applicative liée à orchestration métier.

Méthodes importantes
- `FinanceManager()`
- `applyRevenueSharing()`
- `distributeCentralRevenue()`

Classes utilisées
- [FinanceConfiguration](../src/config/FinanceConfiguration.java), [GameStat](../src/data/finance/GameStat.java), [Budget](../src/data/finance/budget/Budget.java), [Income](../src/data/finance/budget/Income.java)

Utilisée par
- [LeagueManager](../src/process/LeagueManager.java), [GameManager](../src/process/manager/GameManager.java), [LeagueManager](../src/process/manager/LeagueManager.java)

### GameManager

📄 Fichier
[GameManager](../src/process/manager/GameManager.java)

Rôle
Implémente une partie de la logique applicative liée à orchestration métier.

Méthodes importantes
- `GameManager()`
- `simulateDay()`
- `setLeague()`

Classes utilisées
- [GameDay](../src/data/calendar/GameDay.java), [League](../src/data/league/League.java), [Playoff](../src/data/league/Playoff.java), [RegularSeason](../src/data/league/RegularSeason.java)

Utilisée par
- [LeagueManager](../src/process/LeagueManager.java), [LeagueManager](../src/process/manager/LeagueManager.java)

### LeagueManager (process-manager)

📄 Fichier
[LeagueManager](../src/process/manager/LeagueManager.java)

Rôle
Implémente une partie de la logique applicative liée à orchestration métier.

Méthodes importantes
- `LeagueManager()`
- `startSeason()`
- `simulateDay()`

Classes utilisées
- [League](../src/data/league/League.java), [Team](../src/data/team/Team.java), [FinancialProfil](../src/data/team/finance/financialprofil/FinancialProfil.java), [CalendarBuilder](../src/process/builder/CalendarBuilder.java)

Utilisée par
- [CalendarSimulationPanel](../src/gui/panel/calendarPanel/CalendarSimulationPanel.java), [SimulationManager](../src/process/manager/SimulationManager.java)

### RevenueSharingManager

📄 Fichier
[RevenueSharingManager](../src/process/manager/RevenueSharingManager.java)

Rôle
Implémente une partie de la logique applicative liée à orchestration métier.

Méthodes importantes
- `RevenueSharingManager()`
- `applyRevenueSharing()`

Classes utilisées
- [FinanceConfiguration](../src/config/FinanceConfiguration.java), [Budget](../src/data/finance/budget/Budget.java), [Expense](../src/data/finance/budget/Expense.java), [Income](../src/data/finance/budget/Income.java)

Utilisée par
- [FinanceManager](../src/process/manager/FinanceManager.java)

### SimulationManager

📄 Fichier
[SimulationManager](../src/process/manager/SimulationManager.java)

Rôle
Implémente une partie de la logique applicative liée à orchestration métier.

Méthodes importantes
- `SimulationManager()`
- `randomFinance()`
- `startSeason()`

Classes utilisées
- [SimulationConfiguration](../src/config/SimulationConfiguration.java), [LeagueManager](../src/process/LeagueManager.java)

Utilisée par
- Aucune utilisation détectée dans `src`.

### TradeManager

📄 Fichier
[TradeManager](../src/process/manager/TradeManager.java)

Rôle
Implémente une partie de la logique applicative liée à orchestration métier.

Méthodes importantes
- `TradeManager()`
- `simulatePreSeasonTrade()`
- `simulateSeasonTrade()`

Classes utilisées
- [FinanceConfiguration](../src/config/FinanceConfiguration.java), [FinancialPolicy](../src/config/FinancialPolicy.java), [Trade](../src/data/finance/transfer/Trade.java), [Player](../src/data/player/Player.java)

Utilisée par
- [LeagueManager](../src/process/manager/LeagueManager.java)

### CurrentSeasonAssetRepositery

📄 Fichier
[CurrentSeasonAssetRepositery](../src/process/repositery/CurrentSeasonAssetRepositery.java)

Rôle
Implémente une partie de la logique applicative liée à registre partagé.

Méthodes importantes
- `getInstance()`
- `register()`
- `getCurrentSeasonAsset()`

Classes utilisées
- [Asset](../src/data/player/Asset.java), [Player](../src/data/player/Player.java)

Utilisée par
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java), [GameSimulator](../src/process/simulator/GameSimulator.java)

### DivisionRepositery

📄 Fichier
[DivisionRepositery](../src/process/repositery/DivisionRepositery.java)

Rôle
Implémente une partie de la logique applicative liée à registre partagé.

Méthodes importantes
- `getInstance()`
- `register()`
- `getDivision()`

Classes utilisées
- [Division](../src/data/league/Division.java)

Utilisée par
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java)

### PlayerRepositery

📄 Fichier
[PlayerRepositery](../src/process/repositery/PlayerRepositery.java)

Rôle
Implémente une partie de la logique applicative liée à registre partagé.

Méthodes importantes
- `getInstance()`
- `register()`
- `getPlayer()`

Classes utilisées
- [Player](../src/data/player/Player.java)

Utilisée par
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java), [GameSimulator](../src/process/simulator/GameSimulator.java)

### PreSeasonAssetRepositery

📄 Fichier
[PreSeasonAssetRepositery](../src/process/repositery/PreSeasonAssetRepositery.java)

Rôle
Implémente une partie de la logique applicative liée à registre partagé.

Méthodes importantes
- `getInstance()`
- `register()`
- `getPreSeasonAsset()`

Classes utilisées
- [Asset](../src/data/player/Asset.java), [Player](../src/data/player/Player.java)

Utilisée par
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java), [GameSimulator](../src/process/simulator/GameSimulator.java)

### TeamRepositery

📄 Fichier
[TeamRepositery](../src/process/repositery/TeamRepositery.java)

Rôle
Implémente une partie de la logique applicative liée à registre partagé.

Méthodes importantes
- `getInstance()`
- `register()`
- `getTeam()`

Classes utilisées
- [Team](../src/data/team/Team.java)

Utilisée par
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java), [GameSelector](../src/process/builder/calendartools/GameSelector.java), [ScheduleReset](../src/process/builder/calendartools/ScheduleReset.java), [FinanceManager](../src/process/manager/FinanceManager.java)

### GameExpenseSimulator

📄 Fichier
[GameExpenseSimulator](../src/process/simulator/GameExpenseSimulator.java)

Rôle
Implémente une partie de la logique applicative liée à simulation métier.

Méthodes importantes
- `GameExpenseSimulator()`
- `calculateGameExpenses()`

Classes utilisées
- [FinanceConfiguration](../src/config/FinanceConfiguration.java), [SimulationConfiguration](../src/config/SimulationConfiguration.java), [GameStat](../src/data/finance/GameStat.java), [Game](../src/data/sport/setup/Game.java)

Utilisée par
- [FinanceManager](../src/process/manager/FinanceManager.java)

### GameRevenueSimulator

📄 Fichier
[GameRevenueSimulator](../src/process/simulator/GameRevenueSimulator.java)

Rôle
Implémente une partie de la logique applicative liée à simulation métier.

Méthodes importantes
- `GameRevenueSimulator()`
- `calculateGameRevenue()`

Classes utilisées
- [GameStat](../src/data/finance/GameStat.java), [Game](../src/data/sport/setup/Game.java), [Stadium](../src/data/team/Stadium.java), [Team](../src/data/team/Team.java)

Utilisée par
- [FinanceManager](../src/process/manager/FinanceManager.java)

### GameSimulator

📄 Fichier
[GameSimulator](../src/process/simulator/GameSimulator.java)

Rôle
Implémente une partie de la logique applicative liée à simulation métier.

Méthodes importantes
- `updateRest()`
- `simulateGame()`

Classes utilisées
- [SimulationConfiguration](../src/config/SimulationConfiguration.java), [Asset](../src/data/player/Asset.java), [HealthStatus](../src/data/player/HealthStatus.java), [Injury](../src/data/player/Injury.java)

Utilisée par
- [CalendarSimulationPanel](../src/gui/panel/calendarPanel/CalendarSimulationPanel.java), [GameManager](../src/process/manager/GameManager.java)

### TradeSimulator

📄 Fichier
[TradeSimulator](../src/process/simulator/TradeSimulator.java)

Rôle
Implémente une partie de la logique applicative liée à simulation métier.

Méthodes importantes
- `validateTrade()`
- `respectEconomicPayroll()`
- `respectAmbitiousPayroll()`

Classes utilisées
- [FinanceConfiguration](../src/config/FinanceConfiguration.java), [FinancialPolicy](../src/config/FinancialPolicy.java), [Budget](../src/data/finance/budget/Budget.java), [Expense](../src/data/finance/budget/Expense.java)

Utilisée par
- [TradeManager](../src/process/manager/TradeManager.java), [EvaluateSeasonIntentVisitor](../src/process/visitor/teamtransfer/EvaluateSeasonIntentVisitor.java)

### CalendarUtilitary

📄 Fichier
[CalendarUtilitary](../src/process/utilitary/CalendarUtilitary.java)

Rôle
Implémente une partie de la logique applicative liée à utilitaires métier.

Méthodes importantes
- `isWeekend()`
- `isImportantDay()`
- `isSpecialEvent()`

Classes utilisées
- [SimulationConfiguration](../src/config/SimulationConfiguration.java), [RegularSeason](../src/data/league/RegularSeason.java), [Game](../src/data/sport/setup/Game.java), [GameContext](../src/data/sport/setup/GameContext.java)

Utilisée par
- [GameContext](../src/data/sport/setup/GameContext.java), [GameSelector](../src/process/builder/calendartools/GameSelector.java), [SpecialEventPlanner](../src/process/builder/calendartools/SpecialEventPlanner.java), [GameManager](../src/process/manager/GameManager.java)

### FinanceUtilitary

📄 Fichier
[FinanceUtilitary](../src/process/utilitary/FinanceUtilitary.java)

Rôle
Implémente une partie de la logique applicative liée à utilitaires métier.

Méthodes importantes
- `initiateBudget()`
- `updateLeaguePayroll()`
- `updateTeamPayroll()`

Classes utilisées
- [FinanceConfiguration](../src/config/FinanceConfiguration.java), [GameStat](../src/data/finance/GameStat.java), [TeamGameFinance](../src/data/finance/TeamGameFinance.java), [Budget](../src/data/finance/budget/Budget.java)

Utilisée par
- [LeagueFinance](../src/data/league/LeagueFinance.java), [LeagueBuilder](../src/process/builder/LeagueBuilder.java), [TeamFactory](../src/process/factory/TeamFactory.java), [FinanceManager](../src/process/manager/FinanceManager.java)

### PlayerUtilitary

📄 Fichier
[PlayerUtilitary](../src/process/utilitary/PlayerUtilitary.java)

Rôle
Implémente une partie de la logique applicative liée à utilitaires métier.

Méthodes importantes
- `getPlayerAttackNote()`
- `getPlayerDefenseNote()`
- `getPlayerOverAllNote()`

Classes utilisées
- [SimulationConfiguration](../src/config/SimulationConfiguration.java), [Asset](../src/data/player/Asset.java), [HealthStatus](../src/data/player/HealthStatus.java), [Player](../src/data/player/Player.java)

Utilisée par
- [GameSimulator](../src/process/simulator/GameSimulator.java), [TeamUtilitary](../src/process/utilitary/TeamUtilitary.java), [PreSeasonPlayerToTradeVisitor](../src/process/visitor/teamtransfer/PreSeasonPlayerToTradeVisitor.java), [SeasonPlayerToTradeVisitor](../src/process/visitor/teamtransfer/SeasonPlayerToTradeVisitor.java)

### TeamUtilitary

📄 Fichier
[TeamUtilitary](../src/process/utilitary/TeamUtilitary.java)

Rôle
Implémente une partie de la logique applicative liée à utilitaires métier.

Méthodes importantes
- `getTeamSportProfile()`
- `setStarPlayer()`
- `updatePerformanceRating()`

Classes utilisées
- [FinanceConfiguration](../src/config/FinanceConfiguration.java), [FinancialPolicy](../src/config/FinancialPolicy.java), [SimulationConfiguration](../src/config/SimulationConfiguration.java), [Player](../src/data/player/Player.java)

Utilisée par
- [LeagueBuilder](../src/process/builder/LeagueBuilder.java), [LeagueManager](../src/process/manager/LeagueManager.java), [TradeManager](../src/process/manager/TradeManager.java), [GameSimulator](../src/process/simulator/GameSimulator.java)

### TransferStrategyUtilitary

📄 Fichier
[TransferStrategyUtilitary](../src/process/utilitary/TransferStrategyUtilitary.java)

Rôle
Implémente une partie de la logique applicative liée à utilitaires métier.

Méthodes importantes
- `chooseTransferStrategy()`
- `chooseTransferStrategyAmbitious()`
- `chooseTransferStrategyBalanced()`

Classes utilisées
- [FinancialPolicy](../src/config/FinancialPolicy.java), [SimulationConfiguration](../src/config/SimulationConfiguration.java), [FinancialProfil](../src/data/team/finance/financialprofil/FinancialProfil.java), [AllIn](../src/data/team/finance/transfer/AllIn.java)

Utilisée par
- [TeamFactory](../src/process/factory/TeamFactory.java), [LeagueManager](../src/process/manager/LeagueManager.java)

### ActionResultVisitor

📄 Fichier
[ActionResultVisitor](../src/process/visitor/actionresult/ActionResultVisitor.java)

Rôle
Implémente une partie de la logique applicative liée à classe métier.

Méthodes importantes
- Aucune méthode publique notable.

Classes utilisées
- [Block](../src/data/sport/play/action/Block.java), [EndOfTime](../src/data/sport/play/action/EndOfTime.java), [PointScored](../src/data/sport/play/action/PointScored.java), [Rebound](../src/data/sport/play/action/Rebound.java)

Utilisée par
- [ActionResult](../src/data/sport/play/action/ActionResult.java), [Block](../src/data/sport/play/action/Block.java), [EndOfTime](../src/data/sport/play/action/EndOfTime.java), [PointScored](../src/data/sport/play/action/PointScored.java)

### AssetUpdateVisitor

📄 Fichier
[AssetUpdateVisitor](../src/process/visitor/actionresult/AssetUpdateVisitor.java)

Rôle
Implémente une partie de la logique applicative liée à classe métier.

Méthodes importantes
- `AssetUpdateVisitor()`
- `visit()`

Classes utilisées
- [Asset](../src/data/player/Asset.java), [Player](../src/data/player/Player.java), [Block](../src/data/sport/play/action/Block.java), [EndOfTime](../src/data/sport/play/action/EndOfTime.java)

Utilisée par
- [GameSimulator](../src/process/simulator/GameSimulator.java)

### GameResultVisitor

📄 Fichier
[GameResultVisitor](../src/process/visitor/actionresult/GameResultVisitor.java)

Rôle
Implémente une partie de la logique applicative liée à classe métier.

Méthodes importantes
- `GameResultVisitor()`
- `visit()`

Classes utilisées
- [SimulationConfiguration](../src/config/SimulationConfiguration.java), [Player](../src/data/player/Player.java), [Block](../src/data/sport/play/action/Block.java), [EndOfTime](../src/data/sport/play/action/EndOfTime.java)

Utilisée par
- [GameSimulator](../src/process/simulator/GameSimulator.java)

### CalculateBaseTicketVisitor

📄 Fichier
[CalculateBaseTicketVisitor](../src/process/visitor/marketsize/CalculateBaseTicketVisitor.java)

Rôle
Implémente une partie de la logique applicative liée à classe métier.

Méthodes importantes
- `CalculateBaseTicketVisitor()`
- `visit()`

Classes utilisées
- [FinanceConfiguration](../src/config/FinanceConfiguration.java), [LargeSize](../src/data/team/finance/marketsize/LargeSize.java), [MediumSize](../src/data/team/finance/marketsize/MediumSize.java), [SmallSize](../src/data/team/finance/marketsize/SmallSize.java)

Utilisée par
- [TeamFactory](../src/process/factory/TeamFactory.java)

### CalculateInitialTeamBudgetVisitor

📄 Fichier
[CalculateInitialTeamBudgetVisitor](../src/process/visitor/marketsize/CalculateInitialTeamBudgetVisitor.java)

Rôle
Implémente une partie de la logique applicative liée à classe métier.

Méthodes importantes
- `CalculateInitialTeamBudgetVisitor()`
- `visit()`

Classes utilisées
- [LargeSize](../src/data/team/finance/marketsize/LargeSize.java), [MediumSize](../src/data/team/finance/marketsize/MediumSize.java), [SmallSize](../src/data/team/finance/marketsize/SmallSize.java), [MarketSizeVisitor](../src/process/visitor/marketsize/MarketSizeVisitor.java)

Utilisée par
- [TeamFactory](../src/process/factory/TeamFactory.java)

### CalculateStadiumCostVisitor

📄 Fichier
[CalculateStadiumCostVisitor](../src/process/visitor/marketsize/CalculateStadiumCostVisitor.java)

Rôle
Implémente une partie de la logique applicative liée à classe métier.

Méthodes importantes
- `CalculateStadiumCostVisitor()`
- `visit()`

Classes utilisées
- [FinanceConfiguration](../src/config/FinanceConfiguration.java), [LargeSize](../src/data/team/finance/marketsize/LargeSize.java), [MediumSize](../src/data/team/finance/marketsize/MediumSize.java), [SmallSize](../src/data/team/finance/marketsize/SmallSize.java)

Utilisée par
- [GameExpenseSimulator](../src/process/simulator/GameExpenseSimulator.java)

### GenerateStadiumCapacityVisitor

📄 Fichier
[GenerateStadiumCapacityVisitor](../src/process/visitor/marketsize/GenerateStadiumCapacityVisitor.java)

Rôle
Implémente une partie de la logique applicative liée à classe métier.

Méthodes importantes
- `GenerateStadiumCapacityVisitor()`
- `visit()`

Classes utilisées
- [LargeSize](../src/data/team/finance/marketsize/LargeSize.java), [MediumSize](../src/data/team/finance/marketsize/MediumSize.java), [SmallSize](../src/data/team/finance/marketsize/SmallSize.java), [MarketSizeVisitor](../src/process/visitor/marketsize/MarketSizeVisitor.java)

Utilisée par
- [TeamFactory](../src/process/factory/TeamFactory.java)

### MarketSizeVisitor

📄 Fichier
[MarketSizeVisitor](../src/process/visitor/marketsize/MarketSizeVisitor.java)

Rôle
Implémente une partie de la logique applicative liée à classe métier.

Méthodes importantes
- Aucune méthode publique notable.

Classes utilisées
- [LargeSize](../src/data/team/finance/marketsize/LargeSize.java), [MediumSize](../src/data/team/finance/marketsize/MediumSize.java), [SmallSize](../src/data/team/finance/marketsize/SmallSize.java)

Utilisée par
- [LargeSize](../src/data/team/finance/marketsize/LargeSize.java), [MarketSize](../src/data/team/finance/marketsize/MarketSize.java), [MediumSize](../src/data/team/finance/marketsize/MediumSize.java), [SmallSize](../src/data/team/finance/marketsize/SmallSize.java)

### EvaluateSeasonIntentVisitor

📄 Fichier
[EvaluateSeasonIntentVisitor](../src/process/visitor/teamtransfer/EvaluateSeasonIntentVisitor.java)

Rôle
Implémente une partie de la logique applicative liée à classe métier.

Méthodes importantes
- `EvaluateSeasonIntentVisitor()`
- `visit()`

Classes utilisées
- [FinanceConfiguration](../src/config/FinanceConfiguration.java), [Team](../src/data/team/Team.java), [AllIn](../src/data/team/finance/transfer/AllIn.java), [Balanced](../src/data/team/finance/transfer/Balanced.java)

Utilisée par
- [TradeManager](../src/process/manager/TradeManager.java)

### PreSeasonPlayerToTradeVisitor

📄 Fichier
[PreSeasonPlayerToTradeVisitor](../src/process/visitor/teamtransfer/PreSeasonPlayerToTradeVisitor.java)

Rôle
Implémente une partie de la logique applicative liée à classe métier.

Méthodes importantes
- `PreSeasonPlayerToTradeVisitor()`
- `visit()`

Classes utilisées
- [Player](../src/data/player/Player.java), [Team](../src/data/team/Team.java), [AllIn](../src/data/team/finance/transfer/AllIn.java), [Balanced](../src/data/team/finance/transfer/Balanced.java)

Utilisée par
- [TradeManager](../src/process/manager/TradeManager.java)

### PreSeasonTradeSatisfactionVisitor

📄 Fichier
[PreSeasonTradeSatisfactionVisitor](../src/process/visitor/teamtransfer/PreSeasonTradeSatisfactionVisitor.java)

Rôle
Implémente une partie de la logique applicative liée à classe métier.

Méthodes importantes
- `PreSeasonTradeSatisfactionVisitor()`
- `visit()`

Classes utilisées
- [AllIn](../src/data/team/finance/transfer/AllIn.java), [Balanced](../src/data/team/finance/transfer/Balanced.java), [Rebuild](../src/data/team/finance/transfer/Rebuild.java), [SalaryDump](../src/data/team/finance/transfer/SalaryDump.java)

Utilisée par
- [TradeManager](../src/process/manager/TradeManager.java)

### SeasonPlayerToTradeVisitor

📄 Fichier
[SeasonPlayerToTradeVisitor](../src/process/visitor/teamtransfer/SeasonPlayerToTradeVisitor.java)

Rôle
Implémente une partie de la logique applicative liée à classe métier.

Méthodes importantes
- `SeasonPlayerToTradeVisitor()`
- `visit()`

Classes utilisées
- [FinanceConfiguration](../src/config/FinanceConfiguration.java), [Player](../src/data/player/Player.java), [Team](../src/data/team/Team.java), [AllIn](../src/data/team/finance/transfer/AllIn.java)

Utilisée par
- [TradeManager](../src/process/manager/TradeManager.java)

### SeasonTradeSatisfactionVisitor

📄 Fichier
[SeasonTradeSatisfactionVisitor](../src/process/visitor/teamtransfer/SeasonTradeSatisfactionVisitor.java)

Rôle
Implémente une partie de la logique applicative liée à classe métier.

Méthodes importantes
- Aucune méthode publique notable.

Classes utilisées
- [TeamTransferVisitor](../src/process/visitor/teamtransfer/TeamTransferVisitor.java)

Utilisée par
- [TradeManager](../src/process/manager/TradeManager.java)

### TeamTransferVisitor

📄 Fichier
[TeamTransferVisitor](../src/process/visitor/teamtransfer/TeamTransferVisitor.java)

Rôle
Implémente une partie de la logique applicative liée à classe métier.

Méthodes importantes
- Aucune méthode publique notable.

Classes utilisées
- [AllIn](../src/data/team/finance/transfer/AllIn.java), [Balanced](../src/data/team/finance/transfer/Balanced.java), [Rebuild](../src/data/team/finance/transfer/Rebuild.java), [SalaryDump](../src/data/team/finance/transfer/SalaryDump.java)

Utilisée par
- [AllIn](../src/data/team/finance/transfer/AllIn.java), [Balanced](../src/data/team/finance/transfer/Balanced.java), [Rebuild](../src/data/team/finance/transfer/Rebuild.java), [SalaryDump](../src/data/team/finance/transfer/SalaryDump.java)

## GUI

### CalendarDashboard

📄 Fichier
[CalendarDashboard](../src/gui/dashboard/CalendarDashboard.java)

Rôle
Porte un élément d’interface utilisateur lié à vue dashboard.

Méthodes importantes
- `CalendarDashboard()`

Classes utilisées
- [CalendarSimulationPanel](../src/gui/panel/calendarPanel/CalendarSimulationPanel.java), [BuildBox](../src/gui/panel/common/BuildBox.java), [SectionTitle](../src/gui/panel/common/SectionTitle.java)

Utilisée par
- [MainGui](../src/gui/frame/MainGui.java)

### FinanceDashboard

📄 Fichier
[FinanceDashboard](../src/gui/dashboard/FinanceDashboard.java)

Rôle
Porte un élément d’interface utilisateur lié à vue dashboard.

Méthodes importantes
- `FinanceDashboard()`

Classes utilisées
- [BuildBox](../src/gui/panel/common/BuildBox.java), [SectionTitle](../src/gui/panel/common/SectionTitle.java)

Utilisée par
- [MainGui](../src/gui/frame/MainGui.java)

### MapDashboard

📄 Fichier
[MapDashboard](../src/gui/dashboard/MapDashboard.java)

Rôle
Porte un élément d’interface utilisateur lié à vue dashboard.

Méthodes importantes
- `MapDashboard()`

Classes utilisées
- [BuildBox](../src/gui/panel/common/BuildBox.java), [SectionTitle](../src/gui/panel/common/SectionTitle.java)

Utilisée par
- [MainGui](../src/gui/frame/MainGui.java)

### MatchDashboard

📄 Fichier
[MatchDashboard](../src/gui/dashboard/MatchDashboard.java)

Rôle
Porte un élément d’interface utilisateur lié à vue dashboard.

Méthodes importantes
- `MatchDashboard()`

Classes utilisées
- [BuildBox](../src/gui/panel/common/BuildBox.java), [SectionTitle](../src/gui/panel/common/SectionTitle.java)

Utilisée par
- [MainGui](../src/gui/frame/MainGui.java)

### OpeningDashboard

📄 Fichier
[OpeningDashboard](../src/gui/dashboard/OpeningDashboard.java)

Rôle
Porte un élément d’interface utilisateur lié à vue dashboard.

Méthodes importantes
- `OpeningDashboard()`
- `getContinueButton()`
- `hasSelectedProfil()`

Classes utilisées
- [BuildBox](../src/gui/panel/common/BuildBox.java), [SectionTitle](../src/gui/panel/common/SectionTitle.java)

Utilisée par
- [MainGui](../src/gui/frame/MainGui.java)

### RankingDashboard

📄 Fichier
[RankingDashboard](../src/gui/dashboard/RankingDashboard.java)

Rôle
Porte un élément d’interface utilisateur lié à vue dashboard.

Méthodes importantes
- `RankingDashboard()`

Classes utilisées
- [BuildBox](../src/gui/panel/common/BuildBox.java), [SectionTitle](../src/gui/panel/common/SectionTitle.java)

Utilisée par
- [MainGui](../src/gui/frame/MainGui.java)

### MainGui

📄 Fichier
[MainGui](../src/gui/frame/MainGui.java)

Rôle
Porte un élément d’interface utilisateur lié à fenêtre principale.

Méthodes importantes
- `MainGui()`
- `actionPerformed()`

Classes utilisées
- [CalendarDashboard](../src/gui/dashboard/CalendarDashboard.java), [FinanceDashboard](../src/gui/dashboard/FinanceDashboard.java), [MapDashboard](../src/gui/dashboard/MapDashboard.java), [MatchDashboard](../src/gui/dashboard/MatchDashboard.java)

Utilisée par
- Aucune utilisation détectée dans `src`.

### SidebarPanel

📄 Fichier
[SidebarPanel](../src/gui/layout/SidebarPanel.java)

Rôle
Porte un élément d’interface utilisateur lié à agencement interface.

Méthodes importantes
- `SidebarPanel()`
- `actionPerformed()`
- `getMatchButton()`

Classes utilisées
- [League](../src/data/league/League.java)

Utilisée par
- [MainGui](../src/gui/frame/MainGui.java)

### CalendarSimulationPanel

📄 Fichier
[CalendarSimulationPanel](../src/gui/panel/calendarPanel/CalendarSimulationPanel.java)

Rôle
Porte un élément d’interface utilisateur lié à composant interface.

Méthodes importantes
- `CalendarSimulationPanel()`
- `actionPerformed()`
- `getPreferredSize()`

Classes utilisées
- [GameDay](../src/data/calendar/GameDay.java), [RegularSeason](../src/data/league/RegularSeason.java), [Game](../src/data/sport/setup/Game.java), [LeagueManager](../src/process/LeagueManager.java)

Utilisée par
- [CalendarDashboard](../src/gui/dashboard/CalendarDashboard.java)

### BuildBox

📄 Fichier
[BuildBox](../src/gui/panel/common/BuildBox.java)

Rôle
Porte un élément d’interface utilisateur lié à composant interface.

Méthodes importantes
- `BuildBox()`

Classes utilisées
- [DashboardCard](../src/gui/panel/common/DashboardCard.java), [PlaceholderPanel](../src/gui/panel/common/PlaceholderPanel.java), [SectionTitle](../src/gui/panel/common/SectionTitle.java)

Utilisée par
- [CalendarDashboard](../src/gui/dashboard/CalendarDashboard.java), [FinanceDashboard](../src/gui/dashboard/FinanceDashboard.java), [MapDashboard](../src/gui/dashboard/MapDashboard.java), [MatchDashboard](../src/gui/dashboard/MatchDashboard.java)

### DashboardCard

📄 Fichier
[DashboardCard](../src/gui/panel/common/DashboardCard.java)

Rôle
Porte un élément d’interface utilisateur lié à composant interface.

Méthodes importantes
- `DashboardCard()`

Classes utilisées
- Aucune dépendance métier explicite.

Utilisée par
- [BuildBox](../src/gui/panel/common/BuildBox.java), [TitledCard](../src/gui/panel/common/TitledCard.java)

### PlaceholderPanel

📄 Fichier
[PlaceholderPanel](../src/gui/panel/common/PlaceholderPanel.java)

Rôle
Porte un élément d’interface utilisateur lié à composant interface.

Méthodes importantes
- `PlaceholderPanel()`

Classes utilisées
- Aucune dépendance métier explicite.

Utilisée par
- [BuildBox](../src/gui/panel/common/BuildBox.java)

### SectionTitle

📄 Fichier
[SectionTitle](../src/gui/panel/common/SectionTitle.java)

Rôle
Porte un élément d’interface utilisateur lié à composant interface.

Méthodes importantes
- `SectionTitle()`

Classes utilisées
- Aucune dépendance métier explicite.

Utilisée par
- [CalendarDashboard](../src/gui/dashboard/CalendarDashboard.java), [FinanceDashboard](../src/gui/dashboard/FinanceDashboard.java), [MapDashboard](../src/gui/dashboard/MapDashboard.java), [MatchDashboard](../src/gui/dashboard/MatchDashboard.java)

### TitledCard

📄 Fichier
[TitledCard](../src/gui/panel/common/TitledCard.java)

Rôle
Porte un élément d’interface utilisateur lié à composant interface.

Méthodes importantes
- `TitledCard()`

Classes utilisées
- [DashboardCard](../src/gui/panel/common/DashboardCard.java), [SectionTitle](../src/gui/panel/common/SectionTitle.java)

Utilisée par
- Aucune utilisation détectée dans `src`.
