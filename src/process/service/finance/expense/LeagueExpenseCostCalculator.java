package process.service.finance.expense;

import org.apache.log4j.Logger;

import config.FinanceConfiguration;
import log.LoggerUtility;
import process.utility.CalendarUtility;

public class LeagueExpenseCostCalculator {
	private static final Logger logger = LoggerUtility.getLogger(LeagueExpenseCostCalculator.class, "text");

	private LeagueExpenseRateCalculator rateCalculator;
	private static final double LEAGUE_EXPENSE_PRESSURE = 1.10;

	public LeagueExpenseCostCalculator(LeagueExpenseRateCalculator rateCalculator) {
		this.rateCalculator = rateCalculator;
	}

	public double calculateAdministrativeCost() {
		double cost = FinanceConfiguration.LEAGUE_ADMINISTRATIVE_COST * 1.28 * LEAGUE_EXPENSE_PRESSURE;
		logger.debug("Calculated administrative league cost " + cost);
		return cost;
	}

	public double calculateMediaCost(int month) {
		double cost = FinanceConfiguration.LEAGUE_MEDIA_COST * 0.84 * LEAGUE_EXPENSE_PRESSURE;
		logger.trace("Base media league cost for month " + month + " is " + cost);
		if (CalendarUtility.isImportantMonth(month)) {
			logger.trace("Applying important month media cost multiplier for month " + month);
			cost *= 1.04;
		}
		cost *= rateCalculator.getImportantGamesExpenseRate(month, 0.010);
		cost *= rateCalculator.getPlayoffGamesExpenseRate(month, 0.015);
		cost *= rateCalculator.getPremiumGamesExpenseRate(month, 0.009);
		cost *= rateCalculator.getHighAttendanceExpenseRate(month, 0.006);
		cost *= rateCalculator.getStarRivalryExpenseRate(month, 0.010);
		cost *= rateCalculator.getActivePlayoffTeamsExpenseRate(month, 0.007);
		cost *= rateCalculator.getSeasonExpenseRate(month, 0.08);
		cost *= rateCalculator.getPopularitySeasonExpenseRate();
		cost *= rateCalculator.getControlledEconomicNoise(month, 0.055);
		logger.debug("Calculated media league cost " + cost + " for month " + month);
		return cost;
	}

	public double calculateMarketingCost(int month) {
		double cost = FinanceConfiguration.LEAGUE_MARKETING_COST * 1.10 * LEAGUE_EXPENSE_PRESSURE;
		logger.trace("Base marketing league cost for month " + month + " is " + cost);
		if (CalendarUtility.isImportantMonth(month)) {
			logger.trace("Applying important month marketing cost multiplier for month " + month);
			cost *= 1.05;
		}
		cost *= rateCalculator.getImportantGamesExpenseRate(month, 0.012);
		cost *= rateCalculator.getPlayoffGamesExpenseRate(month, 0.017);
		cost *= rateCalculator.getPremiumGamesExpenseRate(month, 0.011);
		cost *= rateCalculator.getHighAttendanceExpenseRate(month, 0.007);
		cost *= rateCalculator.getStarRivalryExpenseRate(month, 0.012);
		cost *= rateCalculator.getActivePlayoffTeamsExpenseRate(month, 0.008);
		cost *= rateCalculator.getSeasonExpenseRate(month, 0.10);
		cost *= rateCalculator.getPopularitySeasonExpenseRate();
		cost *= rateCalculator.getControlledEconomicNoise(month, 0.060);
		logger.debug("Calculated marketing league cost " + cost + " for month " + month);
		return cost;
	}

	public double calculateOfficiatingCost(int month) {
		double cost = FinanceConfiguration.LEAGUE_OFFICIATING_COST * 1.02 * LEAGUE_EXPENSE_PRESSURE;
		logger.trace("Base officiating league cost for month " + month + " is " + cost);
		if (CalendarUtility.isImportantMonth(month)) {
			logger.trace("Applying important month officiating cost multiplier for month " + month);
			cost *= 1.03;
		}
		cost *= rateCalculator.getImportantGamesExpenseRate(month, 0.007);
		cost *= rateCalculator.getPlayoffGamesExpenseRate(month, 0.010);
		cost *= rateCalculator.getPremiumGamesExpenseRate(month, 0.005);
		cost *= rateCalculator.getHighAttendanceExpenseRate(month, 0.004);
		cost *= rateCalculator.getStarRivalryExpenseRate(month, 0.006);
		cost *= rateCalculator.getActivePlayoffTeamsExpenseRate(month, 0.0045);
		cost *= rateCalculator.getSeasonExpenseRate(month, 0.08);
		cost *= rateCalculator.getControlledEconomicNoise(month, 0.045);
		logger.debug("Calculated officiating league cost " + cost + " for month " + month);
		return cost;
	}
}
