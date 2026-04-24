package process.builder.league;

import config.CalendarConfiguration;
import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.finance.budget.income.Income;
import data.finance.budget.income.IncomeType;
import data.league.League;
import data.league.finance.LeagueFinance;
import process.utility.FinanceUtility;

public class LeagueFinanceBuilder {

	public static void buildFinanceLeague(League league) {
		double annualRevenueEstimate = FinanceConfiguration.INITIAL_LEAGUE_BUDGET;
		double openingTreasury = annualRevenueEstimate * 0.20;

		Budget budget = new Budget(openingTreasury);
		FinanceUtility.initiateBudget(budget);

		FinanceUtility.addIncome(budget, new Income(IncomeType.NATIONAL_TV, openingTreasury * 0.55),
				0);
		FinanceUtility.addIncome(budget,
				new Income(IncomeType.NATIONAL_SPONSORING, openingTreasury * 0.20), 0);
		FinanceUtility.addIncome(budget,
				new Income(IncomeType.NATIONAL_MERCHANDISING, openingTreasury * 0.10), 0);
		FinanceUtility.addIncome(budget, new Income(IncomeType.OTHER, openingTreasury * 0.15), 0);

		double salaryCap = (annualRevenueEstimate * FinanceConfiguration.PLAYER_SHARE)
				/ CalendarConfiguration.NUMBER_OF_TEAM;
		double luxuryTaxLine = salaryCap * FinanceConfiguration.LUXURYTAX_THRESHOLD_RATE;
		double minimumTeamSalary = salaryCap * FinanceConfiguration.MINIMUM_TEAM_SALARY_RATE;

		LeagueFinance leagueFinance = new LeagueFinance(budget, salaryCap, luxuryTaxLine, minimumTeamSalary,
				FinanceConfiguration.INITIAL_LEAGUE_VALUE);
		league.setLeagueFinance(leagueFinance);
	}
}
