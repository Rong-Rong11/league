package process.builder.finance;

import data.finance.budget.Budget;
import data.team.Team;
import data.team.finance.marketsize.MarketSize;
import process.visitor.marketsize.CalculateInitialTeamValue;

public class TeamValueCalculator {

	public static double calculateInitialTeamValue(Team team, MarketSize marketSize, Budget budget) {
		double baseValue = 250.0;
		double popularityBonus = team.getFormerPopularity() * 2.0;
		double marketBonus = getMarketValueBonus(marketSize);
		double stadiumBonus = 25.0;

		return baseValue + budget.getRemainingAmount() + popularityBonus + marketBonus + stadiumBonus;
	}

	private static double getMarketValueBonus(MarketSize marketSize) {
		return marketSize.accept(new CalculateInitialTeamValue());
	}
}
