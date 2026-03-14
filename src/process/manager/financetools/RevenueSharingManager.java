package process.manager.financetools;

import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.finance.budget.Expense;
import data.finance.budget.Income;
import data.league.League;
import data.team.Team;
import process.repositery.TeamRepositery;
import process.utilitary.FinanceUtilitary;

public class RevenueSharingManager {
	private TeamRepositery teamRepositery = TeamRepositery.getInstance();
	private League league;

	public RevenueSharingManager(League league) {
		this.league = league;
	}

	public void applyRevenueSharing(int month) {
		double leagueAverage = calculateLeagueLocalAverage(month);
		double pool = collectFromRichTeams(leagueAverage, month);
		double leagueKeeps = pool * 0.05;
		FinanceUtilitary.addIncome(league.getLeagueFinance().getBudget(),
				new Income(FinanceConfiguration.INCOME_TYPE_REVENUE_SHARING, leagueKeeps), month);
		double remainingPool = pool - leagueKeeps;

		distributeToSmallTeams(leagueAverage, remainingPool, month);

	}

	private double calculateLeagueLocalAverage(int month) {
		double total = 0;
		for (Team team : teamRepositery.getAllTeams()) {
			double localRevenue = FinanceUtilitary.getTeamLocalRevenueOfMonth(team, month);
			Budget budget = team.getTeamFinance().getBudget();
			total += localRevenue;
		}
		return total / teamRepositery.getAllTeams().size();
	}

	private double collectFromRichTeams(double leagueAverage, int month) {
		double pool = 0;
		for (Team team : teamRepositery.getAllTeams()) {
			double localRevenue = FinanceUtilitary.getTeamLocalRevenueOfMonth(team, month);
			Budget budget = team.getTeamFinance().getBudget();

			if (localRevenue > leagueAverage) {
				double excess = localRevenue - leagueAverage;
				double contribution = excess * FinanceConfiguration.REVENUE_SHARING_REDISTRIBUTION_RATE;
				FinanceUtilitary.addExpense(budget,
						new Expense(FinanceConfiguration.EXPENSE_TYPE_REVENUE_SHARING, contribution), month);

				FinanceUtilitary.updateBudget(budget);
				pool += contribution;
			}
		}
		return pool;
	}

	private void distributeToSmallTeams(double leagueAverage, double pool, int month) {
		double totalNeed = 0;
		for (Team team : teamRepositery.getAllTeams()) {
			double localRevenue = FinanceUtilitary.getTeamLocalRevenueOfMonth(team, month);
			Budget budget = team.getTeamFinance().getBudget();
			if (localRevenue < leagueAverage) {
				totalNeed += (leagueAverage - localRevenue);
			}
		}
		if (totalNeed == 0) {
			return;
		}
		for (Team team : teamRepositery.getAllTeams()) {
			double localRevenue = FinanceUtilitary.getTeamLocalRevenueOfMonth(team, month);
			Budget budget = team.getTeamFinance().getBudget();
			if (localRevenue < leagueAverage && pool >= 0) {
				double need = leagueAverage - localRevenue;
				double share = (need / totalNeed) * pool;
				pool -= share;
				FinanceUtilitary.addIncome(budget, new Income(FinanceConfiguration.INCOME_TYPE_REVENUE_SHARING, share),
						month);
				FinanceUtilitary.updateBudget(budget);
			}
		}
	}

}
