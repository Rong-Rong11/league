package process.manager;

import java.time.LocalDate;

import data.league.League;
import data.team.Team;
import data.team.finance.financialprofil.FinancialProfil;
import process.builder.CalendarBuilder;
import process.builder.LeagueBuilder;
import process.repositery.TeamRepositery;
import process.utilitary.FinanceUtilitary;
import process.utilitary.TeamUtilitary;
import process.utilitary.TransferStrategyUtilitary;

public class LeagueManager {
	private League league;

	private LeagueBuilder leagueBuilder = new LeagueBuilder();
	private CalendarBuilder calendarBuilder = new CalendarBuilder();

	private GameManager gameManager = null;
	private TradeManager tradeManager;

	private FinanceManager financeManager;

	public LeagueManager() {
		league = new League();
		financeManager = new FinanceManager(league);
		gameManager = new GameManager(league, financeManager);
		tradeManager = new TradeManager(league.getLeagueFinance().getSalaryCap());

	}

	public void buildLeague() {
		league = leagueBuilder.build();
		FinanceUtilitary.updateLeaguePayroll();
		gameManager.setLeague(league);
	}

	public void startSeason() {
		simulatePreSeasonTrade();
		buildRegularSeasonCalendar();
		league.getLeagueFinance().getBudget().getInitialAmount();
		financeManager.distributeCentralRevenue(0, 0, 0, 0);
	}

	private void simulatePreSeasonTrade() {
		tradeManager.simulatePreSeasonTrade();
	}

	private void buildRegularSeasonCalendar() {
		calendarBuilder.initialization(league);
		calendarBuilder.specialEventsPlacement(league.getReagularSeason());
		calendarBuilder.generateAllGames(league);
		calendarBuilder.generateRegulaSeasonCalendar(league);
	}

	public boolean simulateDay(LocalDate date, int month) {
		return gameManager.simulateDay(league, date, month);
	}

	public void randomFinancialProfil() {
		for (Team team : TeamRepositery.getInstance().getAllTeams()) {
			FinancialProfil financialProfil = TeamUtilitary.randomFinancialProfil();
			team.getTeamFinance().setFinancialProfil(financialProfil);
			team.getTeamFinance().setTeamTransferStrategy(
					TransferStrategyUtilitary.chooseTransferStrategy(financialProfil, team.getRival()));
		}
	}

	public League getLeague() {
		return league;
	}
}
