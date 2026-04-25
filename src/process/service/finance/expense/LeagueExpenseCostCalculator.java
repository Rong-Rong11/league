package process.service.finance.expense;

import org.apache.log4j.Logger;

import config.FinanceConfiguration;
import log.LoggerUtility;
import process.utility.CalendarUtility;

public class LeagueExpenseCostCalculator {
	private static final Logger logger = LoggerUtility.getLogger(LeagueExpenseCostCalculator.class, "text");

	private LeagueExpenseRateCalculator rateCalculator;

	public LeagueExpenseCostCalculator(LeagueExpenseRateCalculator rateCalculator) {
		this.rateCalculator = rateCalculator;
		logger.debug("League expense cost calculator initialized");
	}

	public double calculateAdministrativeCost() {
		double cost = FinanceConfiguration.LEAGUE_ADMINISTRATIVE_COST * 1.6;
		logger.debug("Calculated administrative league cost " + cost);
		return cost;
	}

	public double calculateMediaCost(int month) {
		double cost = FinanceConfiguration.LEAGUE_MEDIA_COST * 1.2;
		logger.trace("Base media league cost for month " + month + " is " + cost);
		if (CalendarUtility.isImportantMonth(month)) {
			logger.trace("Applying important month media cost multiplier for month " + month);
			cost *= 1.22;
		}
		cost *= rateCalculator.getImportantGamesExpenseRate(month, 0.035);
		cost *= rateCalculator.getPlayoffGamesExpenseRate(month, 0.060);
		cost *= rateCalculator.getActivePlayoffTeamsExpenseRate(month, 0.022);
		cost *= rateCalculator.getSeasonExpenseRate(month, 0.15);
		cost *= rateCalculator.getPopularitySeasonExpenseRate();
		cost *= rateCalculator.getControlledEconomicNoise(month, 0.075);
		logger.debug("Calculated media league cost " + cost + " for month " + month);
		return cost;
	}

	public double calculateMarketingCost(int month) {
		double cost = FinanceConfiguration.LEAGUE_MARKETING_COST * 1.8;
		logger.trace("Base marketing league cost for month " + month + " is " + cost);
		if (CalendarUtility.isImportantMonth(month)) {
			logger.trace("Applying important month marketing cost multiplier for month " + month);
			cost *= 1.28;
		}
		cost *= rateCalculator.getImportantGamesExpenseRate(month, 0.042);
		cost *= rateCalculator.getPlayoffGamesExpenseRate(month, 0.070);
		cost *= rateCalculator.getActivePlayoffTeamsExpenseRate(month, 0.028);
		cost *= rateCalculator.getSeasonExpenseRate(month, 0.22);
		cost *= rateCalculator.getPopularitySeasonExpenseRate();
		cost *= rateCalculator.getControlledEconomicNoise(month, 0.085);
		logger.debug("Calculated marketing league cost " + cost + " for month " + month);
		return cost;
	}

	public double calculateOfficiatingCost(int month) {
		double cost = FinanceConfiguration.LEAGUE_OFFICIATING_COST * 1.7;
		logger.trace("Base officiating league cost for month " + month + " is " + cost);
		if (CalendarUtility.isImportantMonth(month)) {
			logger.trace("Applying important month officiating cost multiplier for month " + month);
			cost *= 1.18;
		}
		cost *= rateCalculator.getImportantGamesExpenseRate(month, 0.014);
		cost *= rateCalculator.getPlayoffGamesExpenseRate(month, 0.016);
		cost *= rateCalculator.getActivePlayoffTeamsExpenseRate(month, 0.0080);
		cost *= rateCalculator.getSeasonExpenseRate(month, 0.18);
		cost *= rateCalculator.getControlledEconomicNoise(month, 0.12);
		logger.debug("Calculated officiating league cost " + cost + " for month " + month);
		return cost;
	}
}
