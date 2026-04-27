package process.orchestrator.manager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import config.CalendarConfiguration;
import data.calendar.GameDay;
import data.finance.GameStat;
import data.finance.budget.Budget;
import data.finance.budget.income.Income;
import data.finance.budget.income.IncomeCategory;
import data.finance.transfer.Trade;
import data.league.League;
import data.league.Playoff;
import data.league.PlayoffRound;
import data.league.finance.LeagueFinancialRules;
import data.sport.live.LiveMatchState;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.financialpolicy.AmbitiousPolicy;
import data.team.finance.financialpolicy.BalancedPolicy;
import data.team.finance.financialpolicy.ThriftyPolicy;
import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;
import log.LoggerUtility;
import process.builder.calendar.FirstRoundCalendarBuilder;
import process.builder.calendar.RegularSeasonCalendarBuilder;
import process.builder.league.LeagueBuilder;
import process.builder.league.PlayoffBuilder;
import process.orchestrator.interfaces.GUIInterface;
import process.orchestrator.interfaces.SimulationClock;
import process.service.finance.FinanceManager;
import process.service.game.GameManager;
import process.service.league.TeamPopularityUpdater;
import process.service.live.LiveMatchService;
import process.service.trade.TradeService;
import process.service.trade.preseason.PreSeasonTradeService;
import process.service.trade.regularseason.RegularSeasonTradeService;

public class SimulationManager implements GUIInterface {
	private static final Logger logger = LoggerUtility.getLogger(SimulationManager.class, "text");

	private final League league;
	private final SimulationClock clock;
	private final GameManager gameManager;
	private final TradeService preSeasonTradeService;
	private final TradeService regularSeasonTradeService;
	private final FinanceManager financeManager;
	private final TeamPopularityUpdater teamPopularityUpdater;
	private final PlayoffBuilder playoffBuilder;
	private final FirstRoundCalendarBuilder firstRoundCalendarBuilder;
	private final RegularSeasonCalendarBuilder regularSeasonCalendarBuilder;
	private final LiveMatchService liveMatchService;
	private final SimulationDataManager simulationDataManager;
	private final PlayoffManager playoffManager;
	private final SeasonManager seasonManager;

	public SimulationManager() {
		logger.debug("Initializing simulation manager");
		league = new LeagueBuilder().build();
		playoffBuilder = new PlayoffBuilder(league);
		firstRoundCalendarBuilder = new FirstRoundCalendarBuilder(league);
		regularSeasonCalendarBuilder = new RegularSeasonCalendarBuilder(league);
		clock = new SimulationClock(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE);
		financeManager = new FinanceManager(league);
		teamPopularityUpdater = new TeamPopularityUpdater();
		gameManager = new GameManager(league, financeManager, regularSeasonCalendarBuilder, playoffBuilder,
				firstRoundCalendarBuilder, teamPopularityUpdater);
		liveMatchService = new LiveMatchService();

		LeagueFinancialRules leagueFinancialRules = league.getLeagueFinance().getLeagueFinancialRules();
		preSeasonTradeService = new PreSeasonTradeService(leagueFinancialRules.getSalaryCap(),
				leagueFinancialRules.getLuxuryTaxLine());
		regularSeasonTradeService = new RegularSeasonTradeService(leagueFinancialRules.getSalaryCap(),
				leagueFinancialRules.getLuxuryTaxLine());

		simulationDataManager = new SimulationDataManager(league, clock, gameManager, financeManager, liveMatchService,
				preSeasonTradeService, regularSeasonTradeService);
		playoffManager = new PlayoffManager(league, playoffBuilder, firstRoundCalendarBuilder, financeManager,
				teamPopularityUpdater);
		seasonManager = new SeasonManager(clock, financeManager, teamPopularityUpdater, gameManager,
				preSeasonTradeService, regularSeasonTradeService, regularSeasonCalendarBuilder, simulationDataManager,
				playoffManager);
		logger.debug("Simulation manager ready");
	}

	@Override
	public League getLeague() {
		return simulationDataManager.getLeague();
	}

	@Override
	public ArrayList<Trade> getTradesForTeam(Team team) {
		return simulationDataManager.getTradesForTeam(team);
	}

	public Playoff getPlayoff() {
		return playoffManager.getPlayoff();
	}

	public PlayoffRound getCurrentPlayoffRound() {
		return playoffManager.getCurrentPlayoffRound();
	}

	@Override
	public boolean hasPlayoffsStarted() {
		return playoffManager.hasPlayoffsStarted();
	}

	@Override
	public boolean hasPlayoffData() {
		return playoffManager.hasPlayoffData();
	}

	@Override
	public boolean arePlayoffsFinished() {
		return playoffManager.arePlayoffsFinished();
	}

	@Override
	public boolean isRegularSeasonFinished() {
		return hasPlayoffsStarted() || clock.getCurrentDate().isAfter(getRegularSeasonEndDate());
	}

	@Override
	public boolean hasUserConfirmedPlayoffs() {
		return playoffManager.hasUserConfirmedPlayoffs();
	}

	@Override
	public void setUserConfirmedPlayoffs(boolean confirmed) {
		playoffManager.setUserConfirmedPlayoffs(confirmed);
	}

	@Override
	public void initializePlayoffs() {
		playoffManager.initializePlayoffs(clock.getCurrentMonth());
	}

	@Override
	public Map<String, String> getPlayoffPositionMap() {
		return playoffManager.getPlayoffPositionMap();
	}

	@Override
	public int getPlayoffQualifiedTeamCount() {
		return playoffManager.getPlayoffQualifiedTeamCount();
	}

	@Override
	public int getPlayoffSeriesCount() {
		return playoffManager.getPlayoffSeriesCount();
	}

	@Override
	public String getCurrentPlayoffRoundLabel() {
		return playoffManager.getCurrentPlayoffRoundLabel();
	}

	@Override
	public String getPlayoffChampionName() {
		return playoffManager.getPlayoffChampionName();
	}

	@Override
	public String getPlayoffGameLabel(Game game) {
		return playoffManager.getPlayoffGameLabel(game);
	}

	@Override
	public void simulateNextPlayoffRound() {
		playoffManager.simulateNextPlayoffRound(new PlayoffManager.PlayoffDayRunner() {
			@Override
			public void run(GameDay gameDay) {
				simulateAndDisplayDay(gameDay.getDate());
			}
		});
	}

	@Override
	public int getCurrentFinanceMonth() {
		return simulationDataManager.getCurrentFinanceMonth();
	}

	@Override
	public double getLeagueNetForMonth(int month) {
		return simulationDataManager.getLeagueNetForMonth(month);
	}

	@Override
	public double getTeamNetForMonth(Team team, int month) {
		return simulationDataManager.getTeamNetForMonth(team, month);
	}

	@Override
	public double getLeagueTotalNet() {
		return simulationDataManager.getLeagueTotalNet();
	}

	@Override
	public double getTeamTotalNet(Team team) {
		return simulationDataManager.getTeamTotalNet(team);
	}

	@Override
	public double getTotalTvRevenue() {
		return simulationDataManager.getTotalTvRevenue();
	}

	@Override
	public double getTotalMerchandisingRevenue() {
		return simulationDataManager.getTotalMerchandisingRevenue();
	}

	@Override
	public void randomFinance() {
		financeManager.randomFinancialPolicy();
		financeManager.randomMarketSize();
	}

	@Override
	public void chooseAmbitiousPolicy(Team team) {
		financeManager.chooseFinancialPolicy(team, new AmbitiousPolicy());
	}

	@Override
	public void chooseBalancedPolicy(Team team) {
		financeManager.chooseFinancialPolicy(team, new BalancedPolicy());
	}

	@Override
	public void chooseThriftyPolicy(Team team) {
		financeManager.chooseFinancialPolicy(team, new ThriftyPolicy());
	}

	@Override
	public void chooseLargeMarketSize(Team team) {
		financeManager.chooseMarketSize(team, new LargeSize());
	}

	@Override
	public void chooseMediumMarketSize(Team team) {
		financeManager.chooseMarketSize(team, new MediumSize());
	}

	@Override
	public void chooseSmallMarketSize(Team team) {
		financeManager.chooseMarketSize(team, new SmallSize());
	}

	@Override
	public void startSeason() {
		seasonManager.startSeason();
	}

	@Override
	public void simulateDay(LocalDate date) {
		seasonManager.simulateDay(date);
	}

	@Override
	public void simulateAndDisplayDay(LocalDate date) {
		if (!isSeasonInitialized() || date == null) {
			return;
		}
		simulateDay(date);
		displayGameDay(date);
	}

	@Override
	public boolean makeLiveMatchAvailable(Game game, LocalDate date) {
		if (game == null || date == null) {
			return false;
		}
		if (isLiveMatchAvailable(game)) {
			return true;
		}
		simulateAndDisplayDay(date);
		return isLiveMatchAvailable(game);
	}

	@Override
	public void simulateWeek(LocalDate startDate) {
		seasonManager.simulateWeek(startDate);
	}

	@Override
	public void simulateSeasonFrom(LocalDate startDate) {
		seasonManager.simulateSeasonFrom(startDate);
	}

	@Override
	public void endRegularSeason() {
		playoffManager.startPlayoffs(clock.getCurrentMonth());
	}

	@Override
	public void simulateRegularSeason() {
		seasonManager.simulateRegularSeason();
	}

	@Override
	public LocalDate getCurrentDate() {
		return simulationDataManager.getCurrentDate();
	}

	@Override
	public LocalDate getRegularSeasonStartDate() {
		return simulationDataManager.getRegularSeasonStartDate();
	}

	@Override
	public LocalDate getRegularSeasonEndDate() {
		return simulationDataManager.getRegularSeasonEndDate();
	}

	@Override
	public LocalDate getCalendarDisplayDate(LocalDate simulationDate) {
		return simulationDataManager.getCalendarDisplayDate(simulationDate);
	}

	@Override
	public LocalDate getCurrentWeekIndicatorDate() {
		return simulationDataManager.getCurrentWeekIndicatorDate();
	}

	@Override
	public LocalDate getDisplayedDateAfterDaySimulation(LocalDate displayedDate) {
		return simulationDataManager.getDisplayedDateAfterDaySimulation(displayedDate);
	}

	@Override
	public LocalDate getDisplayedDateAfterWeekSimulation(LocalDate displayedDate) {
		return simulationDataManager.getDisplayedDateAfterWeekSimulation(displayedDate);
	}

	@Override
	public LocalDate getDisplayedDateAfterSeasonSimulation(LocalDate displayedDate) {
		return simulationDataManager.getDisplayedDateAfterSeasonSimulation(displayedDate);
	}

	@Override
	public LocalDate getNextGameDay(LocalDate startDate) {
		return simulationDataManager.getNextGameDay(startDate);
	}

	@Override
	public LocalDate getPreviousGameDay(LocalDate startDate) {
		return simulationDataManager.getPreviousGameDay(startDate);
	}

	@Override
	public LocalDate getMatchDisplayDate() {
		return simulationDataManager.getMatchDisplayDate();
	}

	@Override
	public LocalDate getWeekStartDate(LocalDate date) {
		return simulationDataManager.getWeekStartDate(date);
	}

	@Override
	public LocalDate getWeekDisplayDate(LocalDate weekStart) {
		return simulationDataManager.getWeekDisplayDate(weekStart);
	}

	@Override
	public LocalDate getPreviousWeekDisplayDate(LocalDate displayedDate) {
		return simulationDataManager.getPreviousWeekDisplayDate(displayedDate);
	}

	@Override
	public LocalDate getNextWeekDisplayDate(LocalDate displayedDate) {
		return simulationDataManager.getNextWeekDisplayDate(displayedDate);
	}

	@Override
	public String getWeekText(LocalDate displayedDate) {
		return simulationDataManager.getWeekText(displayedDate);
	}

	@Override
	public GameDay getGameDay(LocalDate date) {
		return simulationDataManager.getGameDay(date);
	}

	@Override
	public TreeMap<LocalDate, GameDay> getSeasonCalendar() {
		return simulationDataManager.getSeasonCalendar();
	}

	@Override
	public TreeMap<LocalDate, GameDay> getRegularSeasonCalendar() {
		return simulationDataManager.getRegularSeasonCalendar();
	}

	@Override
	public boolean isSeasonInitialized() {
		return simulationDataManager.isSeasonInitialized();
	}

	@Override
	public ArrayList<Team> getTeams() {
		return simulationDataManager.getTeams();
	}

	@Override
	public ArrayList<Team> getGlobalRanking() {
		return simulationDataManager.getGlobalRanking();
	}

	@Override
	public ArrayList<Team> getEastRanking() {
		return simulationDataManager.getEastRanking();
	}

	@Override
	public ArrayList<Team> getWestRanking() {
		return simulationDataManager.getWestRanking();
	}

	@Override
	public Team getTeamByName(String teamName) {
		return simulationDataManager.getTeamByName(teamName);
	}

	@Override
	public String getConferenceName(Team team) {
		return simulationDataManager.getConferenceName(team);
	}

	@Override
	public String getDivisionName(Team team) {
		return simulationDataManager.getDivisionName(team);
	}

	@Override
	public double getAverageNote(Team team) {
		return simulationDataManager.getAverageNote(team);
	}

	@Override
	public double getAveragePoints(Team team, boolean currentSeasonSelected) {
		return simulationDataManager.getAveragePoints(team, currentSeasonSelected);
	}

	@Override
	public double getAverageRebounds(Team team, boolean currentSeasonSelected) {
		return simulationDataManager.getAverageRebounds(team, currentSeasonSelected);
	}

	@Override
	public double getAverageAssists(Team team, boolean currentSeasonSelected) {
		return simulationDataManager.getAverageAssists(team, currentSeasonSelected);
	}

	@Override
	public String getTeamAbbreviation(String teamName) {
		return simulationDataManager.getTeamAbbreviation(teamName);
	}

	@Override
	public double getTeamCurrentPayroll(Team team) {
		return simulationDataManager.getTeamCurrentPayroll(team);
	}

	@Override
	public String getTeamFinancialPolicyLabel(Team team) {
		return simulationDataManager.getTeamFinancialPolicyLabel(team);
	}

	@Override
	public String getTeamMarketSizeLabel(Team team) {
		return simulationDataManager.getTeamMarketSizeLabel(team);
	}

	@Override
	public int getTeamCurrentWinStreak(Team team) {
		return simulationDataManager.getTeamCurrentWinStreak(team);
	}

	@Override
	public int getTeamCurrentLoseStreak(Team team) {
		return simulationDataManager.getTeamCurrentLoseStreak(team);
	}

	@Override
	public int getTeamMaxWinStreak(Team team) {
		return simulationDataManager.getTeamMaxWinStreak(team);
	}

	@Override
	public int getTeamMaxLoseStreak(Team team) {
		return simulationDataManager.getTeamMaxLoseStreak(team);
	}

	@Override
	public int getTeamNumberWin(Team team) {
		return simulationDataManager.getTeamNumberWin(team);
	}

	@Override
	public int getTeamNumberLose(Team team) {
		return simulationDataManager.getTeamNumberLose(team);
	}

	@Override
	public int getTeamNumberPlayedGames(Team team) {
		return simulationDataManager.getTeamNumberPlayedGames(team);
	}

	@Override
	public ArrayList<Boolean> getTeamLastGamesResults(Team team, int numberOfGames) {
		return simulationDataManager.getTeamLastGamesResults(team, numberOfGames);
	}

	@Override
	public GameStat getGameStat(Game game) {
		return simulationDataManager.getGameStat(game);
	}

	@Override
	public void displayCurrentSeason() {
		simulationDataManager.displayCurrentSeason();
	}

	@Override
	public void displayWeek(LocalDate startDate) {
		simulationDataManager.displayWeek(startDate);
	}

	@Override
	public void displayGameDay(LocalDate date) {
		simulationDataManager.displayGameDay(date);
	}

	@Override
	public boolean isLiveMatchAvailable(Game game) {
		return simulationDataManager.isLiveMatchAvailable(game);
	}

	@Override
	public void setLiveGame(Game game) {
		logger.debug("Setting live game " + getGameLabel(game));
		simulationDataManager.setLiveGame(game);
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
		simulationDataManager.startLiveMatch();
	}

	@Override
	public void pauseLiveMatch() {
		simulationDataManager.pauseLiveMatch();
	}

	@Override
	public void playCurrentLiveQuarter() {
		simulationDataManager.playCurrentLiveQuarter();
	}

	@Override
	public void resetLiveMatch() {
		simulationDataManager.resetLiveMatch();
	}

	@Override
	public void tickLiveMatch() {
		simulationDataManager.tickLiveMatch();
	}

	@Override
	public boolean isLiveMatchRunning() {
		return simulationDataManager.isLiveMatchRunning();
	}

	@Override
	public LiveMatchState getCurrentLiveState() {
		return simulationDataManager.getCurrentLiveState();
	}
}
