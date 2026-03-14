package process.manager;

import java.time.LocalDate;

import data.league.League;
import data.sport.setup.Game;
import data.team.Team;
import process.manager.financetools.CentralRevenueDistributor;
import process.manager.financetools.GameFinanceProcessor;
import process.manager.financetools.MonthlyTeamFinanceCalculator;
import process.manager.financetools.RevenueSharingManager;
import process.repositery.TeamRepositery;

public class FinanceManager {
	private TeamRepositery teamRepositery = TeamRepositery.getInstance();
	private RevenueSharingManager revenueSharingManager;
	private MonthlyTeamFinanceCalculator monthlyTeamFinanceCalculator;
	private CentralRevenueDistributor centralRevenueDistributor;
	private GameFinanceProcessor gameFinanceProcessor;

	public FinanceManager(League league) {
		revenueSharingManager = new RevenueSharingManager(league);
		monthlyTeamFinanceCalculator = new MonthlyTeamFinanceCalculator();
		centralRevenueDistributor = new CentralRevenueDistributor(league);
		gameFinanceProcessor = new GameFinanceProcessor();
	}

	public void applyMonthlyFinance(int month) {
		applyMonthlyFinanceForAllTeams(month);
		distributeMonthlyCentralRevenue(month);
		applyRevenueSharing(month);
	}

	private void distributeMonthlyCentralRevenue(int month) {
		centralRevenueDistributor.distributeMonthlyCentralRevenue(month);
	}
		
	private void applyRevenueSharing(int month) {
		revenueSharingManager.applyRevenueSharing(month);
	}

	public void calculateGame(Game game, LocalDate date, int month) {
		gameFinanceProcessor.calculateGame(game, date, month);
	}

	private void applyMonthlyFinanceForTeam(Team team, int month) {
		monthlyTeamFinanceCalculator.applyMonthlyFinance(team, month);
	}

	private void applyMonthlyFinanceForAllTeams(int month) {
		for (Team team : teamRepositery.getAllTeams()) {
			applyMonthlyFinanceForTeam(team, month);
		}
	}

}
