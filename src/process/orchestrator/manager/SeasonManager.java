package process.orchestrator.manager;

import java.time.LocalDate;
import java.util.ArrayList;

import config.CalendarConfiguration;
import data.calendar.GameDay;
import data.league.Playoff;
import data.sport.setup.PlayoffSeries;
import data.team.Team;
import process.builder.calendar.RegularSeasonCalendarBuilder;
import process.orchestrator.interfaces.SimulationClock;
import process.service.finance.FinanceManager;
import process.service.game.GameManager;
import process.service.league.TeamPopularityUpdater;
import process.service.trade.TradeService;
import process.utility.CalendarUtility;
import process.utility.FinanceUtility;

class SeasonManager {
	private final SimulationClock clock;
	private final FinanceManager financeManager;
	private final TeamPopularityUpdater teamPopularityUpdater;
	private final GameManager gameManager;
	private final TradeService preSeasonTradeService;
	private final TradeService regularSeasonTradeService;
	private final RegularSeasonCalendarBuilder regularSeasonCalendarBuilder;
	private final SimulationDataManager dataManager;
	private final PlayoffManager playoffManager;

	SeasonManager(SimulationClock clock, FinanceManager financeManager, TeamPopularityUpdater teamPopularityUpdater,
			GameManager gameManager, TradeService preSeasonTradeService, TradeService regularSeasonTradeService,
			RegularSeasonCalendarBuilder regularSeasonCalendarBuilder, SimulationDataManager dataManager,
			PlayoffManager playoffManager) {
		this.clock = clock;
		this.financeManager = financeManager;
		this.teamPopularityUpdater = teamPopularityUpdater;
		this.gameManager = gameManager;
		this.preSeasonTradeService = preSeasonTradeService;
		this.regularSeasonTradeService = regularSeasonTradeService;
		this.regularSeasonCalendarBuilder = regularSeasonCalendarBuilder;
		this.dataManager = dataManager;
		this.playoffManager = playoffManager;
	}

	void startSeason() {
		financeManager.initializeFinance();
		FinanceUtility.updateFormerLeaguePayroll();
		simulatePreSeasonTrade();
		teamPopularityUpdater.updateBeforeSeason();
		dataManager.getLeague().getRegularSeason().setNbaCalendar(regularSeasonCalendarBuilder.buildCalendar());
		dataManager.getLeague().getLeagueFinance().getBudget().getInitialAmount();
		clock.reset();
	}

	void simulateDay(LocalDate date) {
		clock.setDate(date);
		if (isRegularSeasonDate(date)) {
			gameManager.simulateRegularSeasonDay(date, clock.getCurrentMonth());
		}

		if (isPlayoffDate(date)) {
			gameManager.simulatePlayoffDay(date, clock.getCurrentMonth(), dataManager.getLeague().getPlayoff().getCurrentRound());
		}
		verifyTimeline();
	}

	void simulateWeek(LocalDate startDate) {
		if (!dataManager.isSeasonInitialized() || startDate == null) {
			return;
		}
		LocalDate weekStart = dataManager.getWeekStartDate(startDate);
		LocalDate weekEnd = weekStart.plusDays(6);
		for (LocalDate day = weekStart; !day.isAfter(weekEnd); day = day.plusDays(1)) {
			GameDay gameDay = dataManager.getGameDay(day);
			if (gameDay != null && !gameDay.isEmpty()) {
				simulateAndDisplayDay(day);
			}
		}
	}

	void simulateSeasonFrom(LocalDate startDate) {
		if (!dataManager.isSeasonInitialized() || startDate == null) {
			return;
		}
		for (LocalDate day : dataManager.getSeasonCalendar().keySet()) {
			if (!day.isBefore(startDate)) {
				simulateAndDisplayDay(day);
			}
		}
	}

	void simulateRegularSeason() {
		while (!clock.getCurrentDate().isAfter(CalendarConfiguration.REGULAR_SEASON_END_DATE)) {
			simulateAndDisplayDay(clock.getCurrentDate());
			if (clock.getCurrentDate().equals(CalendarConfiguration.REGULAR_SEASON_END_DATE)) {
				break;
			}
			clock.nextDay();
		}
	}

	private void simulateAndDisplayDay(LocalDate date) {
		if (!dataManager.isSeasonInitialized() || date == null) {
			return;
		}
		simulateDay(date);
		dataManager.displayGameDay(date);
	}

	private void simulatePreSeasonTrade() {
		preSeasonTradeService.simulateTrade(config.FinanceConfiguration.PRESEASON_TRADE, 0);
	}

	private boolean isRegularSeasonDate(LocalDate date) {
		return !date.isAfter(dataManager.getRegularSeasonEndDate());
	}

	private boolean isPlayoffDate(LocalDate date) {
		Playoff playoff = dataManager.getLeague().getPlayoff();
		return playoff != null && !date.isBefore(playoff.getDebutDate());
	}

	private void verifyTimeline() {
		if (clock.hasMonthChanged()) {
			newMonth(clock.refreshMonth());
		}
		if (clock.hasWeekChanged()) {
			newWeek(clock.getCurrentDate(), clock.refreshWeek());
		}
		if (clock.isRegularSeasonEnd()) {
			playoffManager.startPlayoffs(clock.getCurrentMonth());
		}
	}

	private void newMonth(int month) {
		teamPopularityUpdater.updateMonthlyPopularity();
		if (isRegularSeasonDate(clock.getCurrentDate())) {
			financeManager.applyMonthlyFinance(month);
			return;
		}

		financeManager.applyPlayoffMonthlyFinance(month, getActivePlayoffTeams());
	}

	private ArrayList<Team> getActivePlayoffTeams() {
		ArrayList<Team> activeTeams = new ArrayList<Team>();
		Playoff playoff = dataManager.getLeague().getPlayoff();
		if (playoff == null || playoff.getCurrentRound() == null) {
			return activeTeams;
		}
		for (PlayoffSeries series : CalendarUtility.getCurrentRoundSeries(playoff)) {
			if (series == null || series.isFinished()) {
				continue;
			}
			activeTeams.add(series.getHigherTeam());
			activeTeams.add(series.getLowerTeam());
		}
		return activeTeams;
	}

	private void newWeek(LocalDate date, int month) {
		regularSeasonTradeService.simulateTrade(date, month);
	}
}
