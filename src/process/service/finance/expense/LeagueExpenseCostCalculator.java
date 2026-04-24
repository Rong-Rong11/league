package process.service.finance.expense;

import config.FinanceConfiguration;
import process.utility.CalendarUtility;

public class LeagueExpenseCostCalculator {

	private LeagueExpenseRateCalculator rateCalculator;

	public LeagueExpenseCostCalculator(LeagueExpenseRateCalculator rateCalculator) {
		this.rateCalculator = rateCalculator;
	}

	public double calculateAdministrativeCost() {
		return FinanceConfiguration.LEAGUE_ADMINISTRATIVE_COST * 1.6;
	}

	public double calculateMediaCost(int month) {
		double cost = FinanceConfiguration.LEAGUE_MEDIA_COST * 1.2;
		if (CalendarUtility.isImportantMonth(month)) {
			cost *= 1.22;
		}
		cost *= rateCalculator.getImportantGamesExpenseRate(month, 0.035);
		cost *= rateCalculator.getPlayoffGamesExpenseRate(month, 0.060);
		cost *= rateCalculator.getActivePlayoffTeamsExpenseRate(month, 0.022);
		cost *= rateCalculator.getSeasonExpenseRate(month, 0.15);
		cost *= rateCalculator.getPopularitySeasonExpenseRate();
		cost *= rateCalculator.getControlledEconomicNoise(month, 0.075);
		return cost;
	}

	public double calculateMarketingCost(int month) {
		double cost = FinanceConfiguration.LEAGUE_MARKETING_COST * 1.8;
		if (CalendarUtility.isImportantMonth(month)) {
			cost *= 1.28;
		}
		cost *= rateCalculator.getImportantGamesExpenseRate(month, 0.042);
		cost *= rateCalculator.getPlayoffGamesExpenseRate(month, 0.070);
		cost *= rateCalculator.getActivePlayoffTeamsExpenseRate(month, 0.028);
		cost *= rateCalculator.getSeasonExpenseRate(month, 0.22);
		cost *= rateCalculator.getPopularitySeasonExpenseRate();
		cost *= rateCalculator.getControlledEconomicNoise(month, 0.085);
		return cost;
	}

	public double calculateOfficiatingCost(int month) {
		double cost = FinanceConfiguration.LEAGUE_OFFICIATING_COST * 1.7;
		if (CalendarUtility.isImportantMonth(month)) {
			cost *= 1.18;
		}
		cost *= rateCalculator.getImportantGamesExpenseRate(month, 0.014);
		cost *= rateCalculator.getPlayoffGamesExpenseRate(month, 0.016);
		cost *= rateCalculator.getActivePlayoffTeamsExpenseRate(month, 0.0080);
		cost *= rateCalculator.getSeasonExpenseRate(month, 0.18);
		cost *= rateCalculator.getControlledEconomicNoise(month, 0.12);
		return cost;
	}
}
