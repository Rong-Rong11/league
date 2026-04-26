package process.orchestrator.manager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import config.CalendarConfiguration;
import data.calendar.GameDay;
import data.finance.GameStat;
import data.league.League;
import data.league.Playoff;
import data.league.PlayoffRound;
import data.league.finance.LeagueFinancialRules;
import data.sport.live.LiveMatchState;
import data.sport.setup.Game;
import data.sport.setup.PlayoffSeries;
import data.team.Team;
import data.team.finance.financialpolicy.AmbitiousPolicy;
import data.team.finance.financialpolicy.BalancedPolicy;
import data.team.finance.financialpolicy.ThriftyPolicy;
import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;
import gui.utility.TeamDisplayUtility;
import gui.utility.TeamStatUtility;
import log.LoggerUtility;
import process.builder.calendar.FirstRoundCalendarBuilder;
import process.builder.calendar.RegularSeasonCalendarBuilder;
import process.builder.league.LeagueBuilder;
import process.builder.league.PlayoffBuilder;
import process.orchestrator.interf.GUIInterface;
import process.orchestrator.interf.SimulationClock;
import process.repository.TeamRepository;
import process.service.finance.FinanceManager;
import process.service.game.GameManager;
import process.service.league.TeamPopularityUpdater;
import process.service.live.LiveMatchService;
import process.service.trade.TradeService;
import process.service.trade.preseason.PreSeasonTradeService;
import process.service.trade.regularseason.RegularSeasonTradeService;
import process.utility.CalendarUtility;
import process.utility.FinanceUtility;

//cerveau de la simulation 
public class SimulationManager implements GUIInterface {
	private static final DateTimeFormatter WEEK_FORMATTER = DateTimeFormatter.ofPattern("dd/MM");
	private static final Logger logger = LoggerUtility.getLogger(SimulationManager.class, "text");

	private League league;
	private LeagueBuilder leagueBuilder = new LeagueBuilder();
	private RegularSeasonCalendarBuilder regularSeasonCalendarBuilder;
	private FirstRoundCalendarBuilder firstRoundCalendarBuilder;
	private SimulationClock clock;

	private GameManager gameManager = null;
	private TradeService preSeasonTradeService;
	private TradeService regularSeasonTradeService;
	private FinanceManager financeManager;

	private TeamPopularityUpdater teamPopularityUpdater = new TeamPopularityUpdater();
	private PlayoffBuilder playoffBuilder;
	private LiveMatchService liveMatchService = new LiveMatchService();

	public SimulationManager() {
		logger.debug("Initializing simulation manager");
		league = leagueBuilder.build();
		logger.debug("League built for simulation manager");
		FinanceUtility.updateFormerLeaguePayroll();
		logger.trace("Former league payroll updated");
		playoffBuilder = new PlayoffBuilder(league);
		firstRoundCalendarBuilder = new FirstRoundCalendarBuilder(league);

		clock = new SimulationClock(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE);
		logger.debug("Simulation clock initialized at " + CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE);
		regularSeasonCalendarBuilder = new RegularSeasonCalendarBuilder(league);
		financeManager = new FinanceManager(league);
		gameManager = new GameManager(league, financeManager, regularSeasonCalendarBuilder, playoffBuilder,
				firstRoundCalendarBuilder, teamPopularityUpdater);
		LeagueFinancialRules leagueFinancialRules = league.getLeagueFinance().getLeagueFinancialRules();
		preSeasonTradeService = new PreSeasonTradeService(leagueFinancialRules.getSalaryCap(),
				leagueFinancialRules.getLuxuryTaxLine());
		regularSeasonTradeService = new RegularSeasonTradeService(leagueFinancialRules.getSalaryCap(),
				leagueFinancialRules.getLuxuryTaxLine());
		playoffBuilder = new PlayoffBuilder(league);
		firstRoundCalendarBuilder = new FirstRoundCalendarBuilder(league);
		logger.debug("Simulation manager ready");
	}

	@Override
	public League getLeague() {
		return league;
	}

	@Override
	public int getCurrentFinanceMonth() {
		return clock == null ? 1 : clock.getCurrentMonth();
	}

	@Override
	public double getLeagueNetForMonth(int month) {
		if (league == null || league.getLeagueFinance() == null || league.getLeagueFinance().getBudget() == null) {
			return 0.0;
		}
		return league.getLeagueFinance().getBudget().getNetForMonth(month);
	}

	@Override
	public double getTeamNetForMonth(Team team, int month) {
		if (team == null || team.getTeamFinance() == null || team.getTeamFinance().getBudget() == null) {
			return 0.0;
		}
		return team.getTeamFinance().getBudget().getNetForMonth(month);
	}

	@Override
	public void randomFinance() {
		logger.debug("Randomizing financial setup for all teams");
		financeManager.randomFinancialPolicy();
		financeManager.randomMarketSize();
	}

	@Override
	public void chooseAmbitiousPolicy(Team team) {
		logger.debug("Choosing ambitious financial policy for " + (team == null ? "<none>" : team.getName()));
		financeManager.chooseFinancialPolicy(team, new AmbitiousPolicy());
	}

	@Override
	public void chooseBalancedPolicy(Team team) {
		logger.debug("Choosing balanced financial policy for " + (team == null ? "<none>" : team.getName()));
		financeManager.chooseFinancialPolicy(team, new BalancedPolicy());
	}

	@Override
	public void chooseThriftyPolicy(Team team) {
		logger.debug("Choosing thrifty financial policy for " + (team == null ? "<none>" : team.getName()));
		financeManager.chooseFinancialPolicy(team, new ThriftyPolicy());
	}

	@Override
	public void chooseLargeMarketSize(Team team) {
		logger.debug("Choosing large market size for " + (team == null ? "<none>" : team.getName()));
		financeManager.chooseMarketSize(team, new LargeSize());
	}

	@Override
	public void chooseMediumMarketSize(Team team) {
		logger.debug("Choosing medium market size for " + (team == null ? "<none>" : team.getName()));
		financeManager.chooseMarketSize(team, new MediumSize());
	}

	@Override
	public void chooseSmallMarketSize(Team team) {
		logger.debug("Choosing small market size for " + (team == null ? "<none>" : team.getName()));
		financeManager.chooseMarketSize(team, new SmallSize());
	}

	// methode a utiliser pour lancer la saison
	@Override
	public void startSeason() {
		logger.info("Starting season initialization");
		logger.debug("Initializing team and league finance");
		financeManager.initializeFinance();
		simulatePreSeasonTrade();
		logger.debug("Updating team popularity before season");
		teamPopularityUpdater.updateBeforeSeason();
		league.getRegularSeason().setNbaCalendar(regularSeasonCalendarBuilder.buildCalendar());
		logger.debug("Regular season calendar initialized with "
				+ league.getRegularSeason().getNbaCalendar().getCalendar().size()
				+ " game days");
		league.getLeagueFinance().getBudget().getInitialAmount();
		clock.reset();
		logger.debug("Simulation clock reset to " + clock.getCurrentDate());
		logger.info("Season initialized successfully");
	}

	private void simulatePreSeasonTrade() {
		logger.info("Simulating preseason trades");
		preSeasonTradeService.simulateTrade(config.FinanceConfiguration.PRESEASON_TRADE, 0);
	}

	// passe le prochain jour, methode a utiliser pour la simulation et tout se fais
	// tous seul
	@Override
	public void simulateDay(LocalDate date) {
		logger.debug("Simulating day " + date);
		clock.setDate(date);
		if (isRegularSeasonDate(date)) {
			logger.trace("Day " + date + " is in regular season");
			gameManager.simulateRegularSeasonDay(date, clock.getCurrentMonth());
		}

		if (isPlayoffDate(date)) {
			logger.trace("Day " + date + " is in playoffs");
			gameManager.simulatePlayoffDay(date, clock.getCurrentMonth(), league.getPlayoff().getCurrentRound());
		}
		verifyTimeline();
		logger.trace("Day simulation completed for " + date);
	}

	@Override
	public void simulateAndDisplayDay(LocalDate date) {
		if (!isSeasonInitialized() || date == null) {
			logger.warn("Ignoring simulateAndDisplayDay because season is not initialized or date is null");
			return;
		}
		simulateDay(date);
		displayGameDay(date);
	}

	@Override
	public boolean makeLiveMatchAvailable(Game game, LocalDate date) {
		if (game == null || date == null) {
			logger.warn("Unable to make live match available because game or date is null");
			return false;
		}
		if (isLiveMatchAvailable(game)) {
			logger.debug("Live match already available for date " + date);
			return true;
		}
		logger.debug("Simulating day to make live match available for date " + date);
		simulateAndDisplayDay(date);
		return isLiveMatchAvailable(game);
	}

	@Override
	public void simulateWeek(LocalDate startDate) {
		if (!isSeasonInitialized() || startDate == null) {
			logger.warn("Ignoring simulateWeek because season is not initialized or date is null");
			return;
		}
		logger.info("Simulating week from " + startDate);
		LocalDate weekStart = getWeekStartDate(startDate);
		LocalDate weekEnd = weekStart.plusDays(6);
		int simulatedDays = 0;
		for (LocalDate day = weekStart; !day.isAfter(weekEnd); day = day.plusDays(1)) {
			GameDay gameDay = getGameDay(day);
			if (gameDay != null && !gameDay.isEmpty()) {
				simulateAndDisplayDay(day);
				simulatedDays++;
			}
		}
		logger.info("Week simulation completed from " + weekStart + " to " + weekEnd + " with " + simulatedDays
				+ " simulated game days");
	}

	@Override
	public void simulateSeasonFrom(LocalDate startDate) {
		if (!isSeasonInitialized() || startDate == null) {
			logger.warn("Ignoring simulateSeasonFrom because season is not initialized or date is null");
			return;
		}
		logger.info("Simulating season from " + startDate);
		int simulatedDays = 0;
		for (LocalDate day : getSeasonCalendar().keySet()) {
			if (day.isBefore(startDate)) {
				continue;
			}
			simulateAndDisplayDay(day);
			simulatedDays++;
		}
		logger.info("Season simulation from " + startDate + " completed with " + simulatedDays
				+ " simulated game days");
	}

	private boolean isRegularSeasonDate(LocalDate date) {
		return !date.isAfter(getRegularSeasonEndDate());
	}

	private boolean isPlayoffDate(LocalDate date) {
		return !date.isBefore(league.getPlayoff().getDebutDate());
	}

	private void verifyTimeline() {
		if (clock.hasMonthChanged()) {
			logger.trace("Month transition detected at " + clock.getCurrentDate());
			newMonth(clock.refreshMonth());
		}
		if (clock.hasWeekChanged()) {
			logger.trace("Week transition detected at " + clock.getCurrentDate());
			newWeek(clock.getCurrentDate(), clock.refreshWeek());
		}
		if (clock.isRegularSeasonEnd()) {
			logger.trace("Regular season end detected at " + clock.getCurrentDate());
			endRegularSeason();
		}
	}

	private void newMonth(int month) {
		logger.debug("Applying month transition for month " + month);
		teamPopularityUpdater.updateMonthlyPopularity();
		if (isRegularSeasonDate(clock.getCurrentDate())) {
			financeManager.applyMonthlyFinance(month);
			return;
		}

		financeManager.applyPlayoffMonthlyFinance(month, getActivePlayoffTeams());
	}

	private ArrayList<Team> getActivePlayoffTeams() {
		ArrayList<Team> activeTeams = new ArrayList<>();
		Playoff playoff = league.getPlayoff();

		if (playoff == null || playoff.getCurrentRound() == null) {
			logger.warn("No active playoff teams because playoff or current round is null");
			return new ArrayList<>(activeTeams);
		}
		for (PlayoffSeries series : CalendarUtility.getCurrentRoundSeries(playoff)) {
			if (series == null || series.isFinished()) {
				continue;
			}
			activeTeams.add(series.getHigherTeam());
			activeTeams.add(series.getLowerTeam());
		}
		logger.debug("Found " + activeTeams.size() + " active playoff teams for round " + playoff.getCurrentRound());
		return activeTeams;
	}

	private void newWeek(LocalDate date, int month) {
		logger.debug("Applying week transition at " + date + " for finance month " + month);
		regularSeasonTradeService.simulateTrade(date, month);
	}

	@Override
	public void endRegularSeason() {
		logger.info("Ending regular season and initializing playoffs");
		league.setPlayoff(playoffBuilder.buldFirstRoundPlayoffs());
		applyPlayoffQualificationBonuses(clock.getCurrentMonth());
		applyPlayoffQualificationPopularityBonuses();
		applyMissedPlayoffPenalties();
		league.getPlayoff().setCurrentRound(PlayoffRound.FIRST_ROUND);
		league.getPlayoff().setNbaCalendar(firstRoundCalendarBuilder.buildCalendar());
		logger.debug("Playoff calendar initialized with "
				+ league.getPlayoff().getNbaCalendar().getCalendar().size()
				+ " game days");
		logger.info("Playoffs initialized");
	}

	private void applyPlayoffQualificationBonuses(int month) {
		ArrayList<Team> qualifiedTeams = new ArrayList<Team>();
		qualifiedTeams.addAll(league.getPlayoff().getQualifiedEastTeams());
		qualifiedTeams.addAll(league.getPlayoff().getQualifiedWestTeams());

		logger.debug("Applying playoff qualification finance bonuses to " + qualifiedTeams.size() + " teams");
		financeManager.applyPlayoffQualificationBonus(qualifiedTeams, month);
	}

	private void applyPlayoffQualificationPopularityBonuses() {
		ArrayList<Team> qualifiedTeams = new ArrayList<Team>();
		qualifiedTeams.addAll(league.getPlayoff().getQualifiedEastTeams());
		qualifiedTeams.addAll(league.getPlayoff().getQualifiedWestTeams());

		logger.debug("Applying playoff qualification popularity bonuses to " + qualifiedTeams.size() + " teams");
		for (Team team : qualifiedTeams) {
			logger.trace("Applying playoff qualification popularity bonus to " + team.getName());
			teamPopularityUpdater.applyPlayoffQualificationBonus(team);
		}
	}

	private void applyMissedPlayoffPenalties() {
		ArrayList<Team> qualifiedTeams = new ArrayList<Team>();
		qualifiedTeams.addAll(league.getPlayoff().getQualifiedEastTeams());
		qualifiedTeams.addAll(league.getPlayoff().getQualifiedWestTeams());

		logger.debug("Applying missed playoff penalties");
		for (Team team : TeamRepository.getInstance().getAllTeams()) {
			if (!qualifiedTeams.contains(team)) {
				logger.trace("Applying missed playoff penalty to " + team.getName());
				teamPopularityUpdater.applyMissedPlayoffPenalty(team);
			}
		}
	}

	// simuler la fin de saison régulière ou fin playoff
	@Override
	public void simulateRegularSeason() {
		logger.info("Simulating full regular season");
		while (!clock.getCurrentDate().equals(CalendarConfiguration.REGULAR_SEASON_END_DATE)) {
			simulateDay(clock.getCurrentDate());
			nextDay();
		}
		endRegularSeason();
		logger.info("Regular season simulation completed");
	}

	private void nextDay() {
		clock.nextDay();
	}

	@Override
	public LocalDate getCurrentDate() {
		return clock.getCurrentDate();
	}

	@Override
	public LocalDate getRegularSeasonStartDate() {
		return league.getRegularSeason().getDebutDate();
	}

	@Override
	public LocalDate getRegularSeasonEndDate() {
		return league.getRegularSeason().getEndDate();
	}

	@Override
	public LocalDate getCalendarDisplayDate(LocalDate simulationDate) {
		if (!isSeasonInitialized() || simulationDate == null) {
			return null;
		}

		GameDay currentGameDay = getGameDay(simulationDate);
		if (currentGameDay == null || currentGameDay.isEmpty()) {
			return getNextGameDay(simulationDate);
		}

		if (!currentGameDay.isDisplayed()) {
			return simulationDate;
		}

		LocalDate nextGameDay = getNextGameDay(simulationDate.plusDays(1));
		if (nextGameDay != null) {
			return nextGameDay;
		}
		return simulationDate;
	}

	@Override
	public LocalDate getCurrentWeekIndicatorDate() {
		if (!isSeasonInitialized()) {
			return null;
		}
		return getCurrentCalendarOrSimulationDate();
	}

	@Override
	public LocalDate getDisplayedDateAfterDaySimulation(LocalDate displayedDate) {
		if (!isSeasonInitialized() || displayedDate == null) {
			return null;
		}
		LocalDate nextGameDay = getNextGameDay(displayedDate.plusDays(1));
		if (nextGameDay != null) {
			return nextGameDay;
		}
		return displayedDate;
	}

	@Override
	public LocalDate getDisplayedDateAfterWeekSimulation(LocalDate displayedDate) {
		if (!isSeasonInitialized() || displayedDate == null) {
			return null;
		}
		return getCurrentCalendarOrSimulationDate();
	}

	@Override
	public LocalDate getDisplayedDateAfterSeasonSimulation(LocalDate displayedDate) {
		if (!isSeasonInitialized()) {
			return null;
		}
		LocalDate simulationDate = getCurrentDate();
		return simulationDate != null ? simulationDate : displayedDate;
	}

	private LocalDate getCurrentCalendarOrSimulationDate() {
		LocalDate simulationDate = getCurrentDate();
		LocalDate calendarDisplayDate = getCalendarDisplayDate(simulationDate);
		if (calendarDisplayDate != null) {
			return calendarDisplayDate;
		}
		return simulationDate;
	}

	@Override
	public LocalDate getNextGameDay(LocalDate startDate) {
		if (!isSeasonInitialized() || startDate == null) {
			return null;
		}

		for (LocalDate day = startDate; !day.isAfter(getRegularSeasonEndDate()); day = day.plusDays(1)) {
			GameDay gameDay = getGameDay(day);
			if (gameDay != null && !gameDay.isEmpty()) {
				return day;
			}
		}

		return null;
	}

	@Override
	public LocalDate getPreviousGameDay(LocalDate startDate) {
		if (!isSeasonInitialized() || startDate == null) {
			return null;
		}
		for (LocalDate day = startDate; !day.isBefore(getRegularSeasonStartDate()); day = day.minusDays(1)) {
			GameDay gameDay = getGameDay(day);
			if (gameDay != null && !gameDay.isEmpty()) {
				return day;
			}
		}
		return null;
	}

	@Override
	public LocalDate getMatchDisplayDate() {
		if (!isSeasonInitialized()) {
			return getRegularSeasonStartDate();
		}
		LocalDate currentDate = getCurrentDate();
		LocalDate previousGameDay = getPreviousGameDay(currentDate.minusDays(1));
		if (previousGameDay != null) {
			return previousGameDay;
		}
		LocalDate currentGameDay = getPreviousGameDay(currentDate);
		if (currentGameDay != null) {
			return currentGameDay;
		}
		return getNextGameDay(getRegularSeasonStartDate());
	}

	@Override
	public LocalDate getWeekStartDate(LocalDate date) {
		if (date == null) {
			return null;
		}
		return date.minusDays(date.getDayOfWeek().getValue() - 1L);
	}

	@Override
	public LocalDate getWeekDisplayDate(LocalDate weekStart) {
		if (!isSeasonInitialized() || weekStart == null) {
			return null;
		}

		LocalDate weekEnd = weekStart.plusDays(6);
		LocalDate searchStart = weekStart;
		if (searchStart.isBefore(getRegularSeasonStartDate())) {
			searchStart = getRegularSeasonStartDate();
		}

		LocalDate nextGameDay = getNextGameDay(searchStart);
		if (nextGameDay != null && !nextGameDay.isAfter(weekEnd)) {
			return nextGameDay;
		}
		if (weekStart.isBefore(getRegularSeasonStartDate())) {
			return getRegularSeasonStartDate();
		}
		return weekStart;
	}

	@Override
	public LocalDate getPreviousWeekDisplayDate(LocalDate displayedDate) {
		if (!isSeasonInitialized() || displayedDate == null) {
			return null;
		}
		LocalDate currentWeekStart = getWeekStartDate(displayedDate);
		LocalDate previousWeekStart = currentWeekStart.minusDays(7);
		LocalDate firstSeasonWeekStart = getWeekStartDate(getRegularSeasonStartDate());
		if (previousWeekStart.isBefore(firstSeasonWeekStart)) {
			return displayedDate;
		}
		LocalDate weekDisplayDate = getWeekDisplayDate(previousWeekStart);
		if (weekDisplayDate != null) {
			return weekDisplayDate;
		}
		return displayedDate;
	}

	@Override
	public LocalDate getNextWeekDisplayDate(LocalDate displayedDate) {
		if (!isSeasonInitialized() || displayedDate == null) {
			return null;
		}
		LocalDate currentWeekStart = getWeekStartDate(displayedDate);
		LocalDate nextWeekStart = currentWeekStart.plusDays(7);
		LocalDate endSeasonWeekStart = getWeekStartDate(getRegularSeasonEndDate());
		if (nextWeekStart.isAfter(endSeasonWeekStart)) {
			return displayedDate;
		}
		LocalDate weekDisplayDate = getWeekDisplayDate(nextWeekStart);
		if (weekDisplayDate != null) {
			return weekDisplayDate;
		}
		return displayedDate;
	}

	@Override
	public String getWeekText(LocalDate displayedDate) {
		if (displayedDate == null) {
			return "Semaine -";
		}
		LocalDate weekStart = getWeekStartDate(displayedDate);
		LocalDate weekEnd = weekStart.plusDays(6);
		return "Semaine du " + WEEK_FORMATTER.format(weekStart) + " au " + WEEK_FORMATTER.format(weekEnd);
	}

	@Override
	public GameDay getGameDay(LocalDate date) {
		if (!isSeasonInitialized() || date == null) {
			return null;
		}
		return league.getRegularSeason().getNbaCalendar().getCalendar().get(date);
	}

	@Override
	public TreeMap<LocalDate, GameDay> getSeasonCalendar() {
		if (!isSeasonInitialized()) {
			return new TreeMap<LocalDate, GameDay>();
		}
		return new TreeMap<LocalDate, GameDay>(league.getRegularSeason().getNbaCalendar().getCalendar());
	}

	@Override
	public boolean isSeasonInitialized() {
		return league != null
				&& league.getRegularSeason() != null
				&& league.getRegularSeason().getNbaCalendar() != null
				&& !league.getRegularSeason().getNbaCalendar().getCalendar().isEmpty();
	}

	@Override
	public ArrayList<Team> getTeams() {
		return new ArrayList<Team>(TeamRepository.getInstance().getAllTeams());
	}

	@Override
	public ArrayList<Team> getGlobalRanking() {
		return gameManager.getGlobalRanking();
	}

	@Override
	public ArrayList<Team> getEastRanking() {
		return gameManager.getEastRanking();
	}

	@Override
	public ArrayList<Team> getWestRanking() {
		return gameManager.getWestRanking();
	}

	@Override
	public Team getTeamByName(String teamName) {
		return TeamRepository.getInstance().getTeam(teamName);
	}

	@Override
	public String getConferenceName(Team team) {
		return TeamStatUtility.getConferenceName(team, league);
	}

	@Override
	public String getDivisionName(Team team) {
		return TeamStatUtility.getDivisionName(team, league);
	}

	@Override
	public double getAverageNote(Team team) {
		return TeamStatUtility.getAverageNote(team);
	}

	@Override
	public double getAveragePoints(Team team, boolean currentSeasonSelected) {
		return TeamStatUtility.getAveragePoints(team, currentSeasonSelected);
	}

	@Override
	public double getAverageRebounds(Team team, boolean currentSeasonSelected) {
		return TeamStatUtility.getAverageRebounds(team, currentSeasonSelected);
	}

	@Override
	public double getAverageAssists(Team team, boolean currentSeasonSelected) {
		return TeamStatUtility.getAverageAssists(team, currentSeasonSelected);
	}

	@Override
	public String getTeamAbbreviation(String teamName) {
		return TeamDisplayUtility.getAbbreviation(getTeamByName(teamName));
	}

	@Override
	public double getTeamCurrentPayroll(Team team) {
		return financeManager.getTeamCurrentPayroll(team);
	}

	@Override
	public String getTeamFinancialPolicyLabel(Team team) {
		if (team == null || team.getTeamFinance() == null
				|| team.getTeamFinance().getBehavior().getFinancialProfil() == null) {
			return "-";
		}
		String className = team.getTeamFinance().getBehavior().getFinancialProfil().getClass().getSimpleName();
		if ("AmbitiousPolicy".equals(className)) {
			return "Ambitieuse";
		}
		if ("BalancedPolicy".equals(className)) {
			return "Equilibree";
		}
		if ("ThriftyPolicy".equals(className)) {
			return "Economique";
		}
		return className;
	}

	@Override
	public String getTeamMarketSizeLabel(Team team) {
		if (team == null || team.getTeamFinance() == null
				|| team.getTeamFinance().getStructure().getMarketSize() == null) {
			return "-";
		}
		String className = team.getTeamFinance().getStructure().getMarketSize().getClass().getSimpleName();
		if ("LargeSize".equals(className)) {
			return "Grand";
		}
		if ("MediumSize".equals(className)) {
			return "Moyen";
		}
		if ("SmallSize".equals(className)) {
			return "Petit";
		}
		return className;
	}

	@Override
	public int getTeamCurrentWinStreak(Team team) {
		return team.getTeamPerformance().getCurrentWinStreak();
	}

	@Override
	public int getTeamCurrentLoseStreak(Team team) {
		return team.getTeamPerformance().getCurrentLoseStreak();
	}

	@Override
	public int getTeamMaxWinStreak(Team team) {
		return TeamStatUtility.getBestWinStreak(team);
	}

	@Override
	public int getTeamMaxLoseStreak(Team team) {
		return TeamStatUtility.getBestLoseStreak(team);
	}

	@Override
	public int getTeamNumberWin(Team team) {
		return team.getTeamPerformance().getNumberWin();
	}

	@Override
	public int getTeamNumberLose(Team team) {
		return team.getTeamPerformance().getNumberLose();
	}

	@Override
	public int getTeamNumberPlayedGames(Team team) {
		return team.getTeamPerformance().getNumberPlayedGames();
	}

	@Override
	public ArrayList<Boolean> getTeamLastGamesResults(Team team, int numberOfGames) {
		return TeamStatUtility.getLastResults(team, numberOfGames);
	}

	@Override
	public GameStat getGameStat(Game game) {
		return financeManager.getGameStat(game);
	}

	@Override
	public void displayCurrentSeason() {
		logger.debug("Displaying current season calendar");
		for (GameDay gameDay : getSeasonCalendar().values()) {
			gameDay.setDisplayed(true);
			for (Game game : gameDay.getGames()) {
				game.setDisplayed(true);
			}
		}
	}

	@Override
	public void displayWeek(LocalDate startDate) {
		if (startDate == null) {
			logger.warn("Skipping week display because start date is null");
			return;
		}
		logger.debug("Displaying week from " + startDate);
		for (int offset = 0; offset < 7; offset++) {
			displayGameDay(startDate.plusDays(offset));
		}
	}

	@Override
	public void displayGameDay(LocalDate date) {
		GameDay gameDay = getGameDay(date);
		if (gameDay == null) {
			logger.trace("No game day to display for " + date);
			return;
		}
		logger.trace("Displaying game day " + date + " with " + gameDay.getGames().size() + " games");
		gameDay.setDisplayed(true);
		for (Game game : gameDay.getGames()) {
			game.setDisplayed(true);
		}
	}

	@Override
	public boolean isLiveMatchAvailable(Game game) {
		return liveMatchService.isLiveMatchAvailable(game);
	}

	@Override
	public void setLiveGame(Game game) {
		logger.debug("Setting live game " + getGameLabel(game));
		liveMatchService.setGame(game);
	}

	private String getGameLabel(Game game) {
		if (game == null || game.getGameContext() == null) {
			return "<none>";
		}
		Team awayTeam = game.getGameContext().getAwayTeam();
		Team homeTeam = game.getGameContext().getHomeTeam();
		String awayName = awayTeam == null ? "<unknown>" : awayTeam.getName();
		String homeName = homeTeam == null ? "<unknown>" : homeTeam.getName();
		return awayName + " at " + homeName;
	}

	@Override
	public void startLiveMatch() {
		logger.debug("Starting live match from simulation manager");
		liveMatchService.startLiveMatch();
	}

	@Override
	public void pauseLiveMatch() {
		logger.debug("Pausing live match from simulation manager");
		liveMatchService.pauseLiveMatch();
	}

	@Override
	public void playCurrentLiveQuarter() {
		logger.debug("Playing current live quarter from simulation manager");
		liveMatchService.playCurrentLiveQuarter();
	}

	@Override
	public void resetLiveMatch() {
		logger.debug("Resetting live match from simulation manager");
		liveMatchService.resetLiveMatch();
	}

	@Override
	public void tickLiveMatch() {
		logger.trace("Ticking live match from simulation manager");
		liveMatchService.tickLiveMatch();
	}

	@Override
	public boolean isLiveMatchRunning() {
		return liveMatchService.isRunning();
	}

	@Override
	public LiveMatchState getCurrentLiveState() {
		return liveMatchService.getCurrentState();
	}

}
