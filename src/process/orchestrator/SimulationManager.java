package process.orchestrator;

import java.time.LocalDate;

import config.CalendarConfiguration;
import data.finance.GameStat;
import data.league.League;
import data.league.finance.LeagueFinancialRules;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.financialpolicy.AmbitiousPolicy;
import data.team.finance.financialpolicy.BalancedPolicy;
import data.team.finance.financialpolicy.ThriftyPolicy;
import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;
import process.builder.CalendarBuilder;
import process.builder.LeagueBuilder;
import process.builder.PlayoffBuilder;
import process.service.leaguetools.TeamPopularityUpdater;
import process.service.submanager.FinanceManager;
import process.service.submanager.GameManager;
import process.service.submanager.PreSeasonTradeService;
import process.service.submanager.RegularSeasonTradeService;
import process.service.submanager.TradeService;
import process.utilitary.FinanceUtilitary;

//cerveau de la simulation 
public class SimulationManager implements SimulationInterface {

	private League league;
	private LeagueBuilder leagueBuilder = new LeagueBuilder();
	private CalendarBuilder calendarBuilder;
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

		clock = new SimulationClock(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE);
		calendarBuilder = new CalendarBuilder(league);
		financeManager = new FinanceManager(league);
		gameManager = new GameManager(league, financeManager, calendarBuilder);
		LeagueFinancialRules leagueFinancialRules = league.getLeagueFinance().getLeagueFinancialRules();
		preSeasonTradeService = new PreSeasonTradeService(leagueFinancialRules.getSalaryCap(),
				leagueFinancialRules.getLuxuryTaxLine());
		regularSeasonTradeService = new RegularSeasonTradeService(leagueFinancialRules.getSalaryCap(),
				leagueFinancialRules.getLuxuryTaxLine());
		playoffBuilder = new PlayoffBuilder(league);
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
		league.getReagularSeason().setNbaCalendar(calendarBuilder.buildRegulaSeasonCalendar());
		league.getLeagueFinance().getBudget().getInitialAmount();
		clock.reset();
	}

	private void simulatePreSeasonTrade() {
		preSeasonTradeService.simulateTrade(config.FinanceConfiguration.PRESEASON_TRADE, 0);
	}

	// passe le prochain jour, méthode à utiliser pour la simulation et tout se fais
	// tous seul
	@Override
	public void simulateRegularSeasonDay(LocalDate date) {
		clock.setDate(date);
		gameManager.simulateRegularSeasonDay(date, clock.getCurrentMonth());
		verifyTimeline();
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
	}

	// simuler la fin de saison régulière ou fin playoff
	@Override
	public void simulateRegularSeason() {
		while (!clock.getCurrentDate().equals(CalendarConfiguration.REGULAR_SEASON_END_DATE)) {
			simulateRegularSeasonDay(clock.getCurrentDate());
			nextDay();
		}
		endRegularSeason();
	}

	private void nextDay() {
		clock.nextDay();
	}

	@Override
	public League getLeague() {
		return league;
	}

	@Override
	public LocalDate getCurrentDate() {
		return clock.getCurrentDate();
	}

	public GameStat getGameStat(Game game) {
		return financeManager.getGameStat(game);
	}

}
