# Classes
Documentation synthétique des classes Java hors `src/test`.

# config
## FinanceConfiguration
Fichier : [FinanceConfiguration.java](../src/config/FinanceConfiguration.java)
Rôle : Définit les constantes de configuration utilisées par la simulation.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : Aucune dépendance métier directe
Utilisée par : LeagueFinance, LeagueBuilder, TeamFactory
## SimulationConfiguration
Fichier : [SimulationConfiguration.java](../src/config/SimulationConfiguration.java)
Rôle : Définit les constantes de configuration utilisées par la simulation.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : Aucune dépendance métier directe
Utilisée par : League, LeagueFinance, HealthStatus, Team

# data
## GameDay
Fichier : [GameDay.java](../src/data/calendar/GameDay.java)
Rôle : Représente une journée de calendrier et les matchs associés.
Méthodes importantes : `getDate()`, `isEmpty()`, `getGames()`, `setGames()`
Classes utilisées : Game
Utilisée par : NBACalendar, SpecialEvent, LeagueManager, CalendarBuilder
## NBACalendar
Fichier : [NBACalendar.java](../src/data/calendar/NBACalendar.java)
Rôle : Stocke la correspondance entre dates et journées de match.
Méthodes importantes : `getCalendar()`, `setCalendar()`
Classes utilisées : GameDay
Utilisée par : Season
## SpecialEvent
Fichier : [SpecialEvent.java](../src/data/calendar/SpecialEvent.java)
Rôle : Décrit un événement spécial placé dans la saison.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : GameDay
Utilisée par : Season, CalendarBuilder
## Budget
Fichier : [Budget.java](../src/data/finance/budget/Budget.java)
Rôle : Agrège le budget initial, les revenus et les dépenses.
Méthodes importantes : `addIncome()`, `addExpense()`, `getInitialAmount()`, `setInitialAmount()`
Classes utilisées : Expense, Income
Utilisée par : LeagueFinance, TeamFinance, FinanceManager, LeagueBuilder
## Expense
Fichier : [Expense.java](../src/data/finance/budget/Expense.java)
Rôle : Valeur simple représentant une dépense.
Méthodes importantes : `getName()`, `getAmount()`
Classes utilisées : Aucune dépendance métier directe
Utilisée par : Budget, FinanceManager
## Income
Fichier : [Income.java](../src/data/finance/budget/Income.java)
Rôle : Valeur simple représentant une source de revenu.
Méthodes importantes : `getName()`, `getAmount()`
Classes utilisées : Aucune dépendance métier directe
Utilisée par : Budget, LeagueFinance, FinanceManager, LeagueBuilder
## PlayerExchange
Fichier : [PlayerExchange.java](../src/data/finance/transfer/PlayerExchange.java)
Rôle : Modélise une opération de transfert de joueur.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : Aucune dépendance métier directe
Utilisée par : Aucune classe métier identifiée
## PlayerPurchase
Fichier : [PlayerPurchase.java](../src/data/finance/transfer/PlayerPurchase.java)
Rôle : Modélise une opération de transfert de joueur.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : Aucune dépendance métier directe
Utilisée par : Aucune classe métier identifiée
## Conference
Fichier : [Conference.java](../src/data/league/Conference.java)
Rôle : Regroupe les divisions et équipes d’une conférence.
Méthodes importantes : `getDivisions()`, `setDivisions()`, `addTeam()`, `addDivision()`
Classes utilisées : Team, Division
Utilisée par : League, GameGenerator, GameManager, CalendarBuilder
## Division
Fichier : [Division.java](../src/data/league/Division.java)
Rôle : Regroupe les équipes appartenant à une division.
Méthodes importantes : `getTeams()`, `setTeams()`, `addTeam()`, `getName()`
Classes utilisées : Team
Utilisée par : Conference, League, GameGenerator, GameManager
## League
Fichier : [League.java](../src/data/league/League.java)
Rôle : Racine métier contenant conférences, saisons et finances de ligue.
Méthodes importantes : `getWesternConference()`, `setWesternConference()`, `getEasternConference()`, `setEasternConfernce()`
Classes utilisées : SimulationConfiguration, Player, Team, Conference
Utilisée par : MainGui, SidebarPanel, GameGenerator, GameManager
## LeagueFinance
Fichier : [LeagueFinance.java](../src/data/league/LeagueFinance.java)
Rôle : Porte les paramètres financiers globaux de la ligue.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : FinanceConfiguration, SimulationConfiguration, Budget, Income
Utilisée par : League, FinanceManager, LeagueBuilder
## Playoff
Fichier : [Playoff.java](../src/data/league/Playoff.java)
Rôle : Spécialise une saison pour la phase de playoffs.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : Season
Utilisée par : League
## Ranking
Fichier : [Ranking.java](../src/data/league/Ranking.java)
Rôle : Stocke les indicateurs de classement d’une équipe.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : Team
Utilisée par : Season
## RegularSeason
Fichier : [RegularSeason.java](../src/data/league/RegularSeason.java)
Rôle : Spécialise une saison pour la phase régulière.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : Season
Utilisée par : League, GameManager, CalendarBuilder
## Season
Fichier : [Season.java](../src/data/league/Season.java)
Rôle : Porte les dates, le calendrier et les événements d’une saison.
Méthodes importantes : `getDebutDate()`, `setDebutDate()`, `getEndDate()`, `setEndDate()`
Classes utilisées : NBACalendar, SpecialEvent, Ranking
Utilisée par : Playoff, RegularSeason, LeagueManager
## Asset
Fichier : [Asset.java](../src/data/player/Asset.java)
Rôle : Regroupe les statistiques et indicateurs d’un joueur.
Méthodes importantes : `getNote()`, `setNote()`, `getMinutesPlayedPerMatch()`, `setMinutesPlayed()`
Classes utilisées : Aucune dépendance métier directe
Utilisée par : Player, GameSimulator, PlayerFactory, CurrentSeasonAssetRepositery
## HealthStatus
Fichier : [HealthStatus.java](../src/data/player/HealthStatus.java)
Rôle : Suit la fatigue et les blessures d’un joueur.
Méthodes importantes : `getFatigue()`, `setFatigue()`, `isInjured()`, `setInjured()`
Classes utilisées : SimulationConfiguration, Injury
Utilisée par : Player, GameSimulator
## Injury
Fichier : [Injury.java](../src/data/player/Injury.java)
Rôle : Décrit une blessure et sa durée.
Méthodes importantes : `getInjuryType()`, `setInjuryType()`, `getInjuryDuration()`, `setInjuryDuration()`
Classes utilisées : Aucune dépendance métier directe
Utilisée par : HealthStatus, GameSimulator
## Player
Fichier : [Player.java](../src/data/player/Player.java)
Rôle : Modélise un joueur avec ses attributs sportifs et financiers.
Méthodes importantes : `isStar()`, `setStar()`, `getName()`, `getPreSeasonAssets()`
Classes utilisées : Asset, HealthStatus
Utilisée par : League, Block, PointScored, Rebound
## ActionResult
Fichier : [ActionResult.java](../src/data/sport/play/ActionResult.java)
Rôle : Base commune des actions produites pendant un match.
Méthodes importantes : `getName()`, `setName()`, `setActionTime()`, `getActionTime()`
Classes utilisées : OffensiveAction
Utilisée par : Block, EndOfTime, PointScored, Rebound
## Block
Fichier : [Block.java](../src/data/sport/play/Block.java)
Rôle : Action représentant un contre défensif.
Méthodes importantes : `getBlockingPlayer()`, `setBlockingPlayer()`
Classes utilisées : Player, ActionResult
Utilisée par : GameSimulator
## EndOfTime
Fichier : [EndOfTime.java](../src/data/sport/play/EndOfTime.java)
Rôle : Action marquant la fin d’une période de jeu.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : ActionResult
Utilisée par : GameSimulator
## OffensiveAction
Fichier : [OffensiveAction.java](../src/data/sport/play/OffensiveAction.java)
Rôle : Décrit le type d’action offensive choisie.
Méthodes importantes : `getName()`
Classes utilisées : Aucune dépendance métier directe
Utilisée par : ActionResult, GameSimulator
## PointScored
Fichier : [PointScored.java](../src/data/sport/play/PointScored.java)
Rôle : Action représentant un panier ou des points marqués.
Méthodes importantes : `getPointsScored()`, `getScorerPlayer()`, `setScorerPlayer()`, `getAssistPlayer()`
Classes utilisées : Player, ActionResult
Utilisée par : GameSimulator
## Rebound
Fichier : [Rebound.java](../src/data/sport/play/Rebound.java)
Rôle : Action représentant un rebond après un tir manqué.
Méthodes importantes : `getReboundPlayer()`, `setReboundPlayer()`, `getMissedPlayer()`, `setMissedPlayer()`
Classes utilisées : Player, ActionResult
Utilisée par : GameSimulator
## Turnover
Fichier : [Turnover.java](../src/data/sport/play/Turnover.java)
Rôle : Action représentant une perte de balle ou interception.
Méthodes importantes : `getInterceptedPlayer()`, `getDefensePlayer()`
Classes utilisées : Player, ActionResult
Utilisée par : GameSimulator
## Game
Fichier : [Game.java](../src/data/sport/setup/Game.java)
Rôle : Regroupe le contexte et les résultats d’un match.
Méthodes importantes : `getGameContext()`, `setGameContext()`, `getQuarterResults()`, `setQuarterResults()`
Classes utilisées : GameContext, GameResult
Utilisée par : GameDay, Team, Schedule, GameGenerator
## GameContext
Fichier : [GameContext.java](../src/data/sport/setup/GameContext.java)
Rôle : Décrit les équipes, le type et l’état de planification d’un match.
Méthodes importantes : `isScheduled()`, `setScheduled()`, `getHomeTeam()`, `setHomeTeam()`
Classes utilisées : Team, GameManager
Utilisée par : Game, Team, GameGenerator, GameManager
## GameResult
Fichier : [GameResult.java](../src/data/sport/setup/GameResult.java)
Rôle : Stocke le score et les actions d’un match simulé.
Méthodes importantes : `addActions()`, `getScorehomeTeam()`, `setScorehomeTeam()`, `getScoreAwayTeam()`
Classes utilisées : Player, ActionResult, Team
Utilisée par : Game, GameSimulator
## Team
Fichier : [Team.java](../src/data/team/Team.java)
Rôle : Modélise une équipe avec ses joueurs, son calendrier et ses finances.
Méthodes importantes : `getName()`, `setNom()`, `getRival()`, `setRival()`
Classes utilisées : SimulationConfiguration, Player, Game, GameContext
Utilisée par : Conference, Division, League, Ranking
## Schedule
Fichier : [Schedule.java](../src/data/team/calendar/Schedule.java)
Rôle : Suit le planning, les compteurs et les matchs d’une équipe.
Méthodes importantes : `getNumberOfPlayedGames()`, `setNumberOfPlayedGames()`, `getNumberOfAwayGames()`, `setNumberOfAwayGames()`
Classes utilisées : Game
Utilisée par : Team, CalendarBuilder
## AmbitiousProfil
Fichier : [AmbitiousProfil.java](../src/data/team/finance/AmbitiousProfil.java)
Rôle : Profil financier orienté vers l’investissement sportif.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : FinancialProfil
Utilisée par : GameSimulator, LeagueBuilder, TeamFactory
## BalancedProfil
Fichier : [BalancedProfil.java](../src/data/team/finance/BalancedProfil.java)
Rôle : Profil financier intermédiaire entre coût et performance.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : FinancialProfil
Utilisée par : GameSimulator, LeagueBuilder, TeamFactory
## EconomicalProfil
Fichier : [EconomicalProfil.java](../src/data/team/finance/EconomicalProfil.java)
Rôle : Profil financier privilégiant la maîtrise des coûts.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : FinancialProfil
Utilisée par : LeagueBuilder, TeamFactory
## FinancialProfil
Fichier : [FinancialProfil.java](../src/data/team/finance/FinancialProfil.java)
Rôle : Type de base des politiques financières d’équipe.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : Aucune dépendance métier directe
Utilisée par : Team, AmbitiousProfil, BalancedProfil, EconomicalProfil
## MarketSize
Fichier : [MarketSize.java](../src/data/team/finance/MarketSize.java)
Rôle : Indique la taille de marché associée à une franchise.
Méthodes importantes : `getSize()`, `setSize()`
Classes utilisées : Aucune dépendance métier directe
Utilisée par : TeamFinance, TeamFactory
## TeamFinance
Fichier : [TeamFinance.java](../src/data/team/finance/TeamFinance.java)
Rôle : Regroupe budget, profil financier et marché d’une équipe.
Méthodes importantes : `getFinancialProfil()`
Classes utilisées : Budget, FinancialProfil, MarketSize
Utilisée par : Team, TeamFactory

# process
## FinanceManager
Fichier : [FinanceManager.java](../src/process/FinanceManager.java)
Rôle : Calcule les budgets et salaires utilisés pendant la simulation financière.
Méthodes importantes : `getAverageSalary()`, `updateBudget()`
Classes utilisées : Budget, Expense, Income, LeagueFinance
Utilisée par : LeagueFinance, GameSimulator
## GameGenerator
Fichier : [GameGenerator.java](../src/process/GameGenerator.java)
Rôle : Génère les affiches entre équipes selon les règles de conférence et division.
Méthodes importantes : `generateIntraDivision()`, `generateIntraConference()`, `generateInterConference()`
Classes utilisées : SimulationConfiguration, Conference, Division, League
Utilisée par : CalendarBuilder
## GameManager
Fichier : [GameManager.java](../src/process/GameManager.java)
Rôle : Fournit les règles utilitaires de planification et de sélection des matchs.
Méthodes importantes : `isWeekend()`, `isImportantDay()`, `isSpecialEvent()`, `playedYesterday()`
Classes utilisées : SimulationConfiguration, Conference, Division, League
Utilisée par : GameContext, GameGenerator, CalendarBuilder
## GameSimulator
Fichier : [GameSimulator.java](../src/process/GameSimulator.java)
Rôle : Simule le déroulement d’un match et met à jour ses résultats.
Méthodes importantes : `simulateGame()`
Classes utilisées : SimulationConfiguration, Asset, HealthStatus, Injury
Utilisée par : LeagueManager
## LeagueManager
Fichier : [LeagueManager.java](../src/process/LeagueManager.java)
Rôle : Coordonne la construction de la ligue, du calendrier et la simulation des journées.
Méthodes importantes : `buildLeague()`, `buildRegularSeasonCalendar()`, `simulateDay()`, `getLeague()`
Classes utilisées : GameDay, League, Season, Game
Utilisée par : Aucune classe métier identifiée
## CalendarBuilder
Fichier : [CalendarBuilder.java](../src/process/builder/CalendarBuilder.java)
Rôle : Initialise et planifie le calendrier de saison régulière.
Méthodes importantes : `initialization()`, `specialEventsPlacement()`, `generateAllGames()`, `generateRegulaSeasonCalendar()`
Classes utilisées : SimulationConfiguration, GameDay, SpecialEvent, Conference
Utilisée par : LeagueManager
## FinanceBuilder
Fichier : [FinanceBuilder.java](../src/process/builder/FinanceBuilder.java)
Rôle : Prépare la construction des éléments financiers liés aux équipes.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : TeamRepositery
Utilisée par : Aucune classe métier identifiée
## LeagueBuilder
Fichier : [LeagueBuilder.java](../src/process/builder/LeagueBuilder.java)
Rôle : Assemble la ligue complète depuis le fichier CSV et les registres.
Méthodes importantes : `build()`
Classes utilisées : FinanceConfiguration, SimulationConfiguration, Budget, Income
Utilisée par : LeagueManager
## PlayerFactory
Fichier : [PlayerFactory.java](../src/process/factory/PlayerFactory.java)
Rôle : Construit un joueur à partir d’une ligne de données source.
Méthodes importantes : `createPlayer()`
Classes utilisées : Asset, Player
Utilisée par : LeagueBuilder
## TeamFactory
Fichier : [TeamFactory.java](../src/process/factory/TeamFactory.java)
Rôle : Construit une équipe et son contexte financier initial.
Méthodes importantes : `createTeam()`
Classes utilisées : FinanceConfiguration, SimulationConfiguration, Budget, Team
Utilisée par : LeagueBuilder
## CurrentSeasonAssetRepositery
Fichier : [CurrentSeasonAssetRepositery.java](../src/process/repositery/CurrentSeasonAssetRepositery.java)
Rôle : Registre singleton des statistiques courantes associées aux joueurs.
Méthodes importantes : `getInstance()`, `register()`, `getCurrentSeasonAsset()`
Classes utilisées : Asset, Player
Utilisée par : GameSimulator, LeagueBuilder
## DivisionRepositery
Fichier : [DivisionRepositery.java](../src/process/repositery/DivisionRepositery.java)
Rôle : Centralise l'accès et l'enregistrement des divisions.
Méthodes importantes : `getInstance()`, `register()`, `getDivision()`
Classes utilisées : Division, Team
Utilisée par : LeagueBuilder
## PlayerRepositery
Fichier : [PlayerRepositery.java](../src/process/repositery/PlayerRepositery.java)
Rôle : Registre singleton permettant de retrouver les joueurs chargés.
Méthodes importantes : `getInstance()`, `register()`, `getPlayer()`, `getAllPlayers()`
Classes utilisées : Player
Utilisée par : GameSimulator, LeagueBuilder
## PreSeasonAssetRepositery
Fichier : [PreSeasonAssetRepositery.java](../src/process/repositery/PreSeasonAssetRepositery.java)
Rôle : Registre singleton des statistiques de présaison des joueurs.
Méthodes importantes : `getInstance()`, `register()`, `getPreSeasonAsset()`
Classes utilisées : Asset, Player
Utilisée par : GameSimulator, LeagueBuilder
## TeamRepositery
Fichier : [TeamRepositery.java](../src/process/repositery/TeamRepositery.java)
Rôle : Registre singleton permettant de retrouver les équipes construites.
Méthodes importantes : `getInstance()`, `register()`, `getTeam()`, `getAllTeams()`
Classes utilisées : Team
Utilisée par : FinanceBuilder, LeagueBuilder

# gui
## BuildBox
Fichier : [BuildBox.java](../src/gui/components/BuildBox.java)
Rôle : Bloc visuel réutilisable pour afficher une carte de contenu.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : DashboardCard, SectionTitle
Utilisée par : CalendarDashboard, FinanceDashboard, MapDashboard, MatchDashboard
## DashboardCard
Fichier : [DashboardCard.java](../src/gui/components/DashboardCard.java)
Rôle : Carte graphique de base utilisée par les écrans.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : Aucune dépendance métier directe
Utilisée par : BuildBox, TitledCard
## SectionTitle
Fichier : [SectionTitle.java](../src/gui/components/SectionTitle.java)
Rôle : En-tête visuel standardisé pour les sections de l’interface.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : Aucune dépendance métier directe
Utilisée par : BuildBox, TitledCard, CalendarDashboard, FinanceDashboard
## TitledCard
Fichier : [TitledCard.java](../src/gui/components/TitledCard.java)
Rôle : Carte réutilisable combinant un titre et une zone de contenu.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : DashboardCard, SectionTitle
Utilisée par : Aucune classe métier identifiée
## CalendarDashboard
Fichier : [CalendarDashboard.java](../src/gui/dashboard/CalendarDashboard.java)
Rôle : Vue dédiée au calendrier des matchs.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : BuildBox, SectionTitle
Utilisée par : MainGui
## FinanceDashboard
Fichier : [FinanceDashboard.java](../src/gui/dashboard/FinanceDashboard.java)
Rôle : Vue dédiée aux informations financières.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : BuildBox, SectionTitle
Utilisée par : MainGui
## MapDashboard
Fichier : [MapDashboard.java](../src/gui/dashboard/MapDashboard.java)
Rôle : Vue de localisation et de navigation générale.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : BuildBox, SectionTitle
Utilisée par : MainGui
## MatchDashboard
Fichier : [MatchDashboard.java](../src/gui/dashboard/MatchDashboard.java)
Rôle : Vue centrée sur les matchs et leur suivi.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : BuildBox, SectionTitle
Utilisée par : MainGui
## OpeningDashboard
Fichier : [OpeningDashboard.java](../src/gui/dashboard/OpeningDashboard.java)
Rôle : Écran d’ouverture pour préparer la ligue avant l’application.
Méthodes importantes : `getContinueButton()`, `hasSelectedProfil()`, `showSelectionWarning()`
Classes utilisées : BuildBox, SectionTitle
Utilisée par : MainGui
## RankingDashboard
Fichier : [RankingDashboard.java](../src/gui/dashboard/RankingDashboard.java)
Rôle : Vue dédiée au classement de la ligue.
Méthodes importantes : Aucune méthode publique principale
Classes utilisées : BuildBox, SectionTitle
Utilisée par : MainGui
## MainGui
Fichier : [MainGui.java](../src/gui/frame/MainGui.java)
Rôle : Fenêtre principale qui orchestre les écrans et la navigation.
Méthodes importantes : Aucune méthode publique principale hors constructeur
Classes utilisées : CalendarDashboard, FinanceDashboard, MapDashboard, MatchDashboard
Utilisée par : Aucune classe métier identifiée
## SidebarPanel
Fichier : [SidebarPanel.java](../src/gui/layout/SidebarPanel.java)
Rôle : Panneau latéral qui regroupe les actions de navigation.
Méthodes importantes : `getMatchButton()`, `getCalendarButton()`, `getRankingButton()`, `getFinanceButton()`
Classes utilisées : League
Utilisée par : MainGui
