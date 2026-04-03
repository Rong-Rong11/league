package process.orchestrator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

import config.CalendarConfiguration;
import data.calendar.GameDay;
import data.finance.GameStat;
import data.league.League;
import data.league.PlayoffRound;
import data.league.finance.LeagueFinancialRules;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.financialpolicy.AmbitiousPolicy;
import data.team.finance.financialpolicy.BalancedPolicy;
import data.team.finance.financialpolicy.ThriftyPolicy;
import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;
import process.builder.calendar.FirstRoundCalendarBuilder;
import process.builder.calendar.RegularSeasonCalendarBuilder;
import process.builder.league.LeagueBuilder;
import process.builder.league.PlayoffBuilder;
import process.repositery.TeamRepositery;
import process.service.finance.FinanceManager;
import process.service.game.GameManager;
import process.service.leaguetools.TeamPopularityUpdater;
import process.service.trade.PreSeasonTradeService;
import process.service.trade.RegularSeasonTradeService;
import process.service.trade.TradeService;
import process.utility.FinanceUtilitary;
import process.utility.TeamDisplayUtil;
import process.utility.TeamStatUtil;

//cerveau de la simulation 
public class SimulationManager implements GUIInterface {

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

	public SimulationManager() {
		league = leagueBuilder.build();
		FinanceUtilitary.updateFormerLeaguePayroll();
		playoffBuilder = new PlayoffBuilder(league);
		firstRoundCalendarBuilder = new FirstRoundCalendarBuilder(league);

		clock = new SimulationClock(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE);
		regularSeasonCalendarBuilder = new RegularSeasonCalendarBuilder(league);
		financeManager = new FinanceManager(league);
		gameManager = new GameManager(league, financeManager, regularSeasonCalendarBuilder, playoffBuilder,
				firstRoundCalendarBuilder);
		LeagueFinancialRules leagueFinancialRules = league.getLeagueFinance().getLeagueFinancialRules();
		preSeasonTradeService = new PreSeasonTradeService(leagueFinancialRules.getSalaryCap(),
				leagueFinancialRules.getLuxuryTaxLine());
		regularSeasonTradeService = new RegularSeasonTradeService(leagueFinancialRules.getSalaryCap(),
				leagueFinancialRules.getLuxuryTaxLine());
		playoffBuilder = new PlayoffBuilder(league);
		firstRoundCalendarBuilder = new FirstRoundCalendarBuilder(league);
	}

	// methddes pour la presaison
	// pour page de garde
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

	// méthode à utiliser pour lancer la saison
	@Override
	public void startSeason() {
		financeManager.initializeFinance();
		simulatePreSeasonTrade();
		teamPopularityUpdater.updateBeforeSeason();
		league.getReagularSeason().setNbaCalendar(regularSeasonCalendarBuilder.buildCalendar());
		league.getLeagueFinance().getBudget().getInitialAmount();
		clock.reset();
	}

	private void simulatePreSeasonTrade() {
		preSeasonTradeService.simulateTrade(config.FinanceConfiguration.PRESEASON_TRADE, 0);
	}

	// passe le prochain jour, méthode à utiliser pour la simulation et tout se fais
	// tous seul
	@Override
	public void simulateDay(LocalDate date) {
		clock.setDate(date);
		if (isRegularSeasonDate(date)) {
			gameManager.simulateRegularSeasonDay(date, clock.getCurrentMonth());
		}

		if (isPlayoffDate(date)) {
			gameManager.simulatePlayoffDay(date, clock.getCurrentMonth(), league.getPlayoff().getCurrentRound());
		}
		verifyTimeline();
	}

	private boolean isRegularSeasonDate(LocalDate date) {
		return !date.isAfter(getRegularSeasonEndDate());
	}

	private boolean isPlayoffDate(LocalDate date) {
		return !date.isBefore(league.getPlayoff().getDebutDate());
	}

	private void verifyTimeline() {
		if (clock.hasMonthChanged()) {
			newMonth(clock.refreshMonth());
		}
		if (clock.hasWeekChanged()) {
			newWeek(clock.getCurrentDate(), clock.refreshWeek());
		}
		if (clock.isRegularSeasonEnd()) {
			endRegularSeason();
		}
	}

	private void newMonth(int month) {
		teamPopularityUpdater.updateMonthlyPopularity();
		financeManager.applyMonthlyFinance(month);
	}

	private void newWeek(LocalDate date, int month) {
		regularSeasonTradeService.simulateTrade(date, month);
	}

	@Override
	public void endRegularSeason() {
		league.setPlayoff(playoffBuilder.buldFirstRoundPlayoffs());
		league.getPlayoff().setCurrentRound(PlayoffRound.FIRST_ROUND);
		league.getPlayoff().setNbaCalendar(firstRoundCalendarBuilder.buildCalendar());
	}

	// simuler la fin de saison régulière ou fin playoff
	@Override
	public void simulateRegularSeason() {
		while (!clock.getCurrentDate().equals(CalendarConfiguration.REGULAR_SEASON_END_DATE)) {
			simulateDay(clock.getCurrentDate());
			nextDay();
		}
		endRegularSeason();
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
		return league.getReagularSeason().getDebutDate();
	}

	@Override
	public LocalDate getRegularSeasonEndDate() {
		return league.getReagularSeason().getEndDate();
	}

	@Override
	public GameDay getGameDay(LocalDate date) {
		if (!isSeasonInitialized() || date == null) {
			return null;
		}
		return league.getReagularSeason().getNbaCalendar().getCalendar().get(date);
	}

	@Override
	public TreeMap<LocalDate, GameDay> getSeasonCalendar() {
		if (!isSeasonInitialized()) {
			return new TreeMap<LocalDate, GameDay>();
		}
		return new TreeMap<LocalDate, GameDay>(league.getReagularSeason().getNbaCalendar().getCalendar());
	}

	@Override
	public boolean isSeasonInitialized() {
		return league != null
				&& league.getReagularSeason() != null
				&& league.getReagularSeason().getNbaCalendar() != null
				&& !league.getReagularSeason().getNbaCalendar().getCalendar().isEmpty();
	}

	@Override
	public ArrayList<Team> getTeams() {
		return new ArrayList<Team>(TeamRepositery.getInstance().getAllTeams());
	}

	@Override
	public Team getTeamByName(String teamName) {
		return TeamRepositery.getInstance().getTeam(teamName);
	}

	@Override
	public String getConferenceName(Team team) {
		return TeamStatUtil.getConferenceName(team, league);
	}

	@Override
	public String getDivisionName(Team team) {
		return TeamStatUtil.getDivisionName(team, league);
	}

	@Override
	public double getAverageNote(Team team) {
		return TeamStatUtil.getAverageNote(team);
	}

	@Override
	public double getAveragePoints(Team team, boolean currentSeasonSelected) {
		return TeamStatUtil.getAveragePoints(team, currentSeasonSelected);
	}

	@Override
	public String getTeamAbbreviation(String teamName) {
		return TeamDisplayUtil.getAbbreviation(getTeamByName(teamName));
	}

	@Override
	public double getTeamCurrentPayroll(Team team) {
		return financeManager.getTeamCurrentPayroll(team);
	}

	@Override
	public GameStat getGameStat(Game game) {
		return financeManager.getGameStat(game);
	}

	@Override
	public void displayCurrentSeason() {
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
			return;
		}
		for (int offset = 0; offset < 7; offset++) {
			displayGameDay(startDate.plusDays(offset));
		}
	}

	@Override
	public void displayGameDay(LocalDate date) {
		GameDay gameDay = getGameDay(date);
		if (gameDay == null) {
			return;
		}
		gameDay.setDisplayed(true);
		for (Game game : gameDay.getGames()) {
			game.setDisplayed(true);
		}
	}

}
