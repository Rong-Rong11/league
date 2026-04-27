package process.orchestrator.manager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import config.FinanceConfiguration;
import data.calendar.GameDay;
import data.finance.GameStat;
import data.finance.budget.Budget;
import data.finance.budget.income.Income;
import data.finance.budget.income.IncomeCategory;
import data.finance.transfer.Trade;
import data.league.League;
import data.sport.live.LiveMatchState;
import data.sport.setup.Game;
import data.team.Team;
import process.orchestrator.interfaces.SimulationClock;
import process.repository.TeamRepository;
import process.service.finance.FinanceManager;
import process.service.game.GameManager;
import process.service.live.LiveMatchService;
import process.service.trade.TradeService;
import process.utility.FinanceSummaryUtility;
import process.utility.TeamMetricsUtility;
import process.utility.TeamNameUtility;

class SimulationDataManager {
	private static final DateTimeFormatter WEEK_FORMATTER = DateTimeFormatter.ofPattern("dd/MM");

	private final League league;
	private final SimulationClock clock;
	private final GameManager gameManager;
	private final FinanceManager financeManager;
	private final LiveMatchService liveMatchService;
	private final TradeService preSeasonTradeService;
	private final TradeService regularSeasonTradeService;

	SimulationDataManager(League league, SimulationClock clock, GameManager gameManager, FinanceManager financeManager,
			LiveMatchService liveMatchService, TradeService preSeasonTradeService,
			TradeService regularSeasonTradeService) {
		this.league = league;
		this.clock = clock;
		this.gameManager = gameManager;
		this.financeManager = financeManager;
		this.liveMatchService = liveMatchService;
		this.preSeasonTradeService = preSeasonTradeService;
		this.regularSeasonTradeService = regularSeasonTradeService;
	}

	League getLeague() {
		return league;
	}

	ArrayList<Trade> getTradesForTeam(Team team) {
		ArrayList<Trade> teamTrades = new ArrayList<Trade>();
		teamTrades.addAll(preSeasonTradeService.getTradesForTeam(team));
		teamTrades.addAll(regularSeasonTradeService.getTradesForTeam(team));
		return teamTrades;
	}

	int getCurrentFinanceMonth() {
		return clock == null ? 1 : clock.getCurrentMonth();
	}

	double getLeagueNetForMonth(int month) {
		if (league == null || league.getLeagueFinance() == null || league.getLeagueFinance().getBudget() == null) {
			return 0.0;
		}
		return league.getLeagueFinance().getBudget().getNetForMonth(month);
	}

	double getTeamNetForMonth(Team team, int month) {
		if (team == null || team.getTeamFinance() == null || team.getTeamFinance().getBudget() == null) {
			return 0.0;
		}
		return team.getTeamFinance().getBudget().getNetForMonth(month);
	}

	double getLeagueTotalNet() {
		return FinanceSummaryUtility.getTotalNet(getLeagueBudget(), lastFinanceMonth());
	}

	double getTeamTotalNet(Team team) {
		return FinanceSummaryUtility.getTotalNet(getTeamBudget(team), lastFinanceMonth());
	}

	double getTotalTvRevenue() {
		return getTotalRevenueByCategory(IncomeCategory.MEDIA);
	}

	double getTotalMerchandisingRevenue() {
		return getTotalRevenueByCategory(IncomeCategory.MERCHANDISING);
	}

	private int lastFinanceMonth() {
		return Math.max(1, FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS - 1);
	}

	private double getTotalRevenueByCategory(IncomeCategory category) {
		double total = FinanceSummaryUtility.getRevenueByCategory(getLeagueBudget(), category);
		for (Team team : getTeams()) {
			total += FinanceSummaryUtility.getRevenueByCategory(getTeamBudget(team), category);
		}
		return total;
	}

	private Budget getLeagueBudget() {
		if (league == null || league.getLeagueFinance() == null) {
			return null;
		}
		return league.getLeagueFinance().getBudget();
	}

	private Budget getTeamBudget(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			return null;
		}
		return team.getTeamFinance().getBudget();
	}

	LocalDate getCurrentDate() {
		return clock.getCurrentDate();
	}

	LocalDate getRegularSeasonStartDate() {
		return league.getRegularSeason().getDebutDate();
	}

	LocalDate getRegularSeasonEndDate() {
		return league.getRegularSeason().getEndDate();
	}

	LocalDate getCalendarDisplayDate(LocalDate simulationDate) {
		if (!isSeasonInitialized() || simulationDate == null) {
			return null;
		}

		LocalDate firstPlayoffGameDay = getFirstPlayoffGameDay();
		if (simulationDate.equals(getRegularSeasonEndDate()) && firstPlayoffGameDay != null) {
			return firstPlayoffGameDay;
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

	LocalDate getCurrentWeekIndicatorDate() {
		if (!isSeasonInitialized()) {
			return null;
		}
		return getCurrentCalendarOrSimulationDate();
	}

	LocalDate getDisplayedDateAfterDaySimulation(LocalDate displayedDate) {
		if (!isSeasonInitialized() || displayedDate == null) {
			return null;
		}
		LocalDate nextGameDay = getNextGameDay(displayedDate.plusDays(1));
		if (nextGameDay != null) {
			return nextGameDay;
		}
		return displayedDate;
	}

	LocalDate getDisplayedDateAfterWeekSimulation(LocalDate displayedDate) {
		if (!isSeasonInitialized() || displayedDate == null) {
			return null;
		}
		return getCurrentCalendarOrSimulationDate();
	}

	LocalDate getDisplayedDateAfterSeasonSimulation(LocalDate displayedDate) {
		if (!isSeasonInitialized()) {
			return null;
		}
		LocalDate currentDisplayDate = getCurrentCalendarOrSimulationDate();
		return currentDisplayDate != null ? currentDisplayDate : displayedDate;
	}

	private LocalDate getCurrentCalendarOrSimulationDate() {
		LocalDate simulationDate = getCurrentDate();
		LocalDate calendarDisplayDate = getCalendarDisplayDate(simulationDate);
		if (calendarDisplayDate != null) {
			return calendarDisplayDate;
		}
		return simulationDate;
	}

	LocalDate getNextGameDay(LocalDate startDate) {
		if (!isSeasonInitialized() || startDate == null) {
			return null;
		}
		return getSeasonCalendar().ceilingKey(startDate);
	}

	LocalDate getPreviousGameDay(LocalDate startDate) {
		if (!isSeasonInitialized() || startDate == null) {
			return null;
		}
		return getSeasonCalendar().floorKey(startDate);
	}

	LocalDate getMatchDisplayDate() {
		if (!isSeasonInitialized()) {
			return getRegularSeasonStartDate();
		}
		LocalDate currentDate = getCurrentCalendarOrSimulationDate();
		if (currentDate == null) {
			return getNextGameDay(getRegularSeasonStartDate());
		}
		GameDay currentGameDay = getGameDay(currentDate);
		if (currentGameDay != null && !currentGameDay.isEmpty()) {
			return currentDate;
		}
		LocalDate previousGameDay = getPreviousGameDay(currentDate.minusDays(1));
		if (previousGameDay != null) {
			return previousGameDay;
		}
		return getNextGameDay(currentDate);
	}

	LocalDate getWeekStartDate(LocalDate date) {
		if (date == null) {
			return null;
		}
		return date.minusDays(date.getDayOfWeek().getValue() - 1L);
	}

	LocalDate getWeekDisplayDate(LocalDate weekStart) {
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

	LocalDate getPreviousWeekDisplayDate(LocalDate displayedDate) {
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

	LocalDate getNextWeekDisplayDate(LocalDate displayedDate) {
		if (!isSeasonInitialized() || displayedDate == null) {
			return null;
		}
		LocalDate currentWeekStart = getWeekStartDate(displayedDate);
		LocalDate nextWeekStart = currentWeekStart.plusDays(7);
		LocalDate endSeasonWeekStart = getWeekStartDate(getDisplayedSeasonEndDate());
		if (nextWeekStart.isAfter(endSeasonWeekStart)) {
			return displayedDate;
		}
		LocalDate weekDisplayDate = getWeekDisplayDate(nextWeekStart);
		if (weekDisplayDate != null) {
			return weekDisplayDate;
		}
		return displayedDate;
	}

	String getWeekText(LocalDate displayedDate) {
		if (displayedDate == null) {
			return "Semaine -";
		}
		LocalDate weekStart = getWeekStartDate(displayedDate);
		LocalDate weekEnd = weekStart.plusDays(6);
		return "Semaine du " + WEEK_FORMATTER.format(weekStart) + " au " + WEEK_FORMATTER.format(weekEnd);
	}

	GameDay getGameDay(LocalDate date) {
		if (!isSeasonInitialized() || date == null) {
			return null;
		}
		GameDay regularSeasonGameDay = league.getRegularSeason().getNbaCalendar().getCalendar().get(date);
		if (regularSeasonGameDay != null) {
			return regularSeasonGameDay;
		}
		if (league.getPlayoff() == null || league.getPlayoff().getNbaCalendar() == null) {
			return null;
		}
		return league.getPlayoff().getNbaCalendar().getCalendar().get(date);
	}

	TreeMap<LocalDate, GameDay> getSeasonCalendar() {
		if (!isSeasonInitialized()) {
			return new TreeMap<LocalDate, GameDay>();
		}
		return getCombinedSeasonCalendar();
	}

	TreeMap<LocalDate, GameDay> getRegularSeasonCalendar() {
		if (!isSeasonInitialized()) {
			return new TreeMap<LocalDate, GameDay>();
		}
		return new TreeMap<LocalDate, GameDay>(league.getRegularSeason().getNbaCalendar().getCalendar());
	}

	boolean isSeasonInitialized() {
		return league != null
				&& league.getRegularSeason() != null
				&& league.getRegularSeason().getNbaCalendar() != null
				&& !league.getRegularSeason().getNbaCalendar().getCalendar().isEmpty();
	}

	private TreeMap<LocalDate, GameDay> getCombinedSeasonCalendar() {
		TreeMap<LocalDate, GameDay> combinedCalendar = new TreeMap<LocalDate, GameDay>();
		if (league == null) {
			return combinedCalendar;
		}
		if (league.getRegularSeason() != null && league.getRegularSeason().getNbaCalendar() != null) {
			combinedCalendar.putAll(league.getRegularSeason().getNbaCalendar().getCalendar());
		}
		if (league.getPlayoff() != null && league.getPlayoff().getNbaCalendar() != null) {
			combinedCalendar.putAll(league.getPlayoff().getNbaCalendar().getCalendar());
		}
		return combinedCalendar;
	}

	private LocalDate getDisplayedSeasonEndDate() {
		TreeMap<LocalDate, GameDay> seasonCalendar = getCombinedSeasonCalendar();
		if (seasonCalendar.isEmpty()) {
			return getRegularSeasonEndDate();
		}
		return seasonCalendar.lastKey();
	}

	private LocalDate getFirstPlayoffGameDay() {
		if (league == null || league.getPlayoff() == null || league.getPlayoff().getNbaCalendar() == null) {
			return null;
		}
		TreeMap<LocalDate, GameDay> playoffCalendar = league.getPlayoff().getNbaCalendar().getCalendar();
		if (playoffCalendar.isEmpty()) {
			return null;
		}
		return playoffCalendar.firstKey();
	}

	ArrayList<Team> getTeams() {
		return new ArrayList<Team>(TeamRepository.getInstance().getAllTeams());
	}

	ArrayList<Team> getGlobalRanking() {
		return gameManager.getGlobalRanking();
	}

	ArrayList<Team> getEastRanking() {
		return gameManager.getEastRanking();
	}

	ArrayList<Team> getWestRanking() {
		return gameManager.getWestRanking();
	}

	Team getTeamByName(String teamName) {
		return TeamRepository.getInstance().getTeam(teamName);
	}

	String getConferenceName(Team team) {
		return TeamNameUtility.getConferenceName(team);
	}

	String getDivisionName(Team team) {
		return TeamNameUtility.getDivisionName(team);
	}

	double getAverageNote(Team team) {
		return TeamMetricsUtility.getAverageNote(team);
	}

	double getAveragePoints(Team team, boolean currentSeasonSelected) {
		return TeamMetricsUtility.getAveragePoints(team, currentSeasonSelected);
	}

	double getAverageRebounds(Team team, boolean currentSeasonSelected) {
		return TeamMetricsUtility.getAverageRebounds(team, currentSeasonSelected);
	}

	double getAverageAssists(Team team, boolean currentSeasonSelected) {
		return TeamMetricsUtility.getAverageAssists(team, currentSeasonSelected);
	}

	String getTeamAbbreviation(String teamName) {
		return TeamNameUtility.getAbbreviation(getTeamByName(teamName));
	}

	double getTeamCurrentPayroll(Team team) {
		return financeManager.getTeamCurrentPayroll(team);
	}

	String getTeamFinancialPolicyLabel(Team team) {
		if (team == null || team.getTeamFinance() == null
				|| team.getTeamFinance().getBehavior().getFinancialPolicy() == null) {
			return "-";
		}
		String className = team.getTeamFinance().getBehavior().getFinancialPolicy().getClass().getSimpleName();
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

	String getTeamMarketSizeLabel(Team team) {
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

	int getTeamCurrentWinStreak(Team team) {
		return team.getTeamPerformance().getCurrentWinStreak();
	}

	int getTeamCurrentLoseStreak(Team team) {
		return team.getTeamPerformance().getCurrentLoseStreak();
	}

	int getTeamMaxWinStreak(Team team) {
		return TeamMetricsUtility.getBestWinStreak(team);
	}

	int getTeamMaxLoseStreak(Team team) {
		return TeamMetricsUtility.getBestLoseStreak(team);
	}

	int getTeamNumberWin(Team team) {
		return team.getTeamPerformance().getNumberWin();
	}

	int getTeamNumberLose(Team team) {
		return team.getTeamPerformance().getNumberLose();
	}

	int getTeamNumberPlayedGames(Team team) {
		return team.getTeamPerformance().getNumberPlayedGames();
	}

	ArrayList<Boolean> getTeamLastGamesResults(Team team, int numberOfGames) {
		return TeamMetricsUtility.getLastResults(team, numberOfGames);
	}

	GameStat getGameStat(Game game) {
		return financeManager.getGameStat(game);
	}

	void displayCurrentSeason() {
		for (GameDay gameDay : getSeasonCalendar().values()) {
			gameDay.setDisplayed(true);
			for (Game game : gameDay.getGames()) {
				game.setDisplayed(true);
			}
		}
	}

	void displayWeek(LocalDate startDate) {
		if (startDate == null) {
			return;
		}
		for (int offset = 0; offset < 7; offset++) {
			displayGameDay(startDate.plusDays(offset));
		}
	}

	void displayGameDay(LocalDate date) {
		GameDay gameDay = getGameDay(date);
		if (gameDay == null) {
			return;
		}
		gameDay.setDisplayed(true);
		for (Game game : gameDay.getGames()) {
			game.setDisplayed(true);
		}
	}

	boolean isLiveMatchAvailable(Game game) {
		return liveMatchService.isLiveMatchAvailable(game);
	}

	void setLiveGame(Game game) {
		liveMatchService.setGame(game);
	}

	void startLiveMatch() {
		liveMatchService.startLiveMatch();
	}

	void pauseLiveMatch() {
		liveMatchService.pauseLiveMatch();
	}

	void playCurrentLiveQuarter() {
		liveMatchService.playCurrentLiveQuarter();
	}

	void resetLiveMatch() {
		liveMatchService.resetLiveMatch();
	}

	void tickLiveMatch() {
		liveMatchService.tickLiveMatch();
	}

	boolean isLiveMatchRunning() {
		return liveMatchService.isRunning();
	}

	LiveMatchState getCurrentLiveState() {
		return liveMatchService.getCurrentState();
	}
}
