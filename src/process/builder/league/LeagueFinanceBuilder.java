package process.builder.league;

import org.apache.log4j.Logger;

import config.CalendarConfiguration;
import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.finance.budget.income.Income;
import data.finance.budget.income.IncomeType;
import data.league.League;
import data.league.finance.LeagueFinance;
import log.LoggerUtility;
import process.utility.FinanceUtility;

public class LeagueFinanceBuilder {
	private static final Logger logger = LoggerUtility.getLogger(LeagueFinanceBuilder.class, "text");

	public static void buildFinanceLeague(League league) {
		if (league == null) {
			logger.warn("Skipping league finance build because league is null");
			return;
		}

		logger.info("Building league finance");
		double annualRevenueEstimate = FinanceConfiguration.INITIAL_LEAGUE_BUDGET;
		double openingTreasury = annualRevenueEstimate * 0.20;
		logger.debug("League finance uses annual revenue estimate "
				+ annualRevenueEstimate
				+ " and opening treasury "
				+ openingTreasury);

		Budget budget = new Budget(openingTreasury);
		logger.debug("Initializing league budget");
		FinanceUtility.initiateBudget(budget);

		logger.trace("Adding national TV income to league budget");
		FinanceUtility.addIncome(budget, new Income(IncomeType.NATIONAL_TV, openingTreasury * 0.55),
				0);
		logger.trace("Adding national sponsoring income to league budget");
		FinanceUtility.addIncome(budget,
				new Income(IncomeType.NATIONAL_SPONSORING, openingTreasury * 0.20), 0);
		logger.trace("Adding national merchandising income to league budget");
		FinanceUtility.addIncome(budget,
				new Income(IncomeType.NATIONAL_MERCHANDISING, openingTreasury * 0.10), 0);
		logger.trace("Adding other income to league budget");
		FinanceUtility.addIncome(budget, new Income(IncomeType.OTHER, openingTreasury * 0.15), 0);

		double salaryCap = (annualRevenueEstimate * FinanceConfiguration.PLAYER_SHARE)
				/ CalendarConfiguration.NUMBER_OF_TEAM;
		double luxuryTaxLine = salaryCap * FinanceConfiguration.LUXURYTAX_THRESHOLD_RATE;
		double minimumTeamSalary = salaryCap * FinanceConfiguration.MINIMUM_TEAM_SALARY_RATE;
		logger.debug("Calculated salary cap "
				+ salaryCap
				+ ", luxury tax line "
				+ luxuryTaxLine
				+ " and minimum team salary "
				+ minimumTeamSalary);

		LeagueFinance leagueFinance = new LeagueFinance(budget, salaryCap, luxuryTaxLine, minimumTeamSalary,
				FinanceConfiguration.INITIAL_LEAGUE_VALUE);
		league.setLeagueFinance(leagueFinance);
		logger.info("League finance built successfully");
	}
}
