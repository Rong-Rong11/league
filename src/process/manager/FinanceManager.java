package process.manager;

import java.time.LocalDate;
import java.util.HashMap;

import config.FinanceConfiguration;
import data.finance.GameStat;
import data.finance.budget.Budget;
import data.finance.budget.Income;
import data.league.League;
import data.sport.setup.Game;
import data.team.Team;
import process.repositery.TeamRepositery;
import process.simulator.GameExpenseSimulator;
import process.simulator.GameRevenueSimulator;
import process.utilitary.FinanceUtilitary;

public class FinanceManager {
	private League league;
	private TeamRepositery teamRepositery = TeamRepositery.getInstance();
	private RevenueSharingManager revenueSharingManager;
	private HashMap<Game, GameStat> gameStats = new HashMap<Game, GameStat>();

	public FinanceManager(League league) {
		this.league = league;
		revenueSharingManager = new RevenueSharingManager(league);
	}

	public void applyRevenueSharing(int month) {
		revenueSharingManager.applyRevenueSharing(month);
	}

	public void distributeCentralRevenue(double tvRevenue, double globalSponsors, double merchandisingRevenue,
			int month) {
		Budget leagueBudget = league.getLeagueFinance().getBudget();
		double totalCentralRevenue = tvRevenue + globalSponsors + merchandisingRevenue;

		double leagueTVCut = tvRevenue * FinanceConfiguration.LEAGUE_OPERATING_RATE;
		double leagueSponsorsCut = globalSponsors * FinanceConfiguration.LEAGUE_OPERATING_RATE;
		double leagueMerchandisingCut = merchandisingRevenue * FinanceConfiguration.LEAGUE_OPERATING_RATE;
		FinanceUtilitary.addIncome(leagueBudget, new Income(FinanceConfiguration.INCOME_TYPE_NATIONAL_TV, leagueTVCut),
				month);
		FinanceUtilitary.addIncome(leagueBudget,
				new Income(FinanceConfiguration.INCOME_TYPE_NATIONAL_SPONSORING, leagueSponsorsCut), month);
		FinanceUtilitary.addIncome(leagueBudget,
				new Income(FinanceConfiguration.INCOME_TYPE_NATIONAL_MERCHANDISING, leagueMerchandisingCut), month);

		double distributableRevenue = totalCentralRevenue - (leagueTVCut + leagueMerchandisingCut + leagueSponsorsCut);

		double share = distributableRevenue / teamRepositery.getAllTeams().size();

		for (Team team : teamRepositery.getAllTeams()) {
			Budget budget = team.getTeamFinance().getBudget();
			FinanceUtilitary.addIncome(budget, new Income(FinanceConfiguration.INCOME_TYPE_CENTRAL_SHARE, share), month);
			FinanceUtilitary.updateBudget(budget);
		}
	}

	public void calculateGame(Game game, LocalDate date, int month) {
		GameStat gameStat = new GameStat(game);

		GameRevenueSimulator gameRevenueSimulator = new GameRevenueSimulator(gameStat);
		gameRevenueSimulator.calculateGameRevenue(game, date);
		FinanceUtilitary.addGameRevenue(game, gameStat, month);

		GameExpenseSimulator gameExpenseSimulator = new GameExpenseSimulator(gameStat);
		gameExpenseSimulator.calculateGameExpenses(game);
		FinanceUtilitary.addGameExpense(game, gameStat, month);

		gameStats.put(game, gameStat);
	}

}
