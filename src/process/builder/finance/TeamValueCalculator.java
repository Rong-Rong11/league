package process.builder.finance;

import org.apache.log4j.Logger;

import data.finance.budget.Budget;
import data.team.Team;
import data.team.finance.marketsize.MarketSize;
import log.LoggerUtility;
import process.visitor.marketsize.CalculateInitialTeamValue;

public class TeamValueCalculator {
	private static final Logger logger = LoggerUtility.getLogger(TeamValueCalculator.class, "text");

	public static double calculateInitialTeamValue(Team team, MarketSize marketSize, Budget budget) {
		if (team == null) {
			logger.warn("Skipping initial team value calculation because team is null");
			return 0.0;
		}
		if (marketSize == null) {
			logger.warn("Skipping initial team value calculation because market size is null");
			return 0.0;
		}
		if (budget == null) {
			logger.warn("Skipping initial team value calculation because budget is null");
			return 0.0;
		}

		logger.debug("Calculating initial team value for " + team.getName());
		double baseValue = 250.0;
		double popularityBonus = team.getFormerPopularity() * 2.0;
		double marketBonus = getMarketValueBonus(marketSize);
		double stadiumBonus = 25.0;
		double budgetAmount = budget.getRemainingAmount();
		logger.trace("Team value components: base="
				+ baseValue
				+ ", budget="
				+ budgetAmount
				+ ", popularityBonus="
				+ popularityBonus
				+ ", marketBonus="
				+ marketBonus
				+ ", stadiumBonus="
				+ stadiumBonus);

		double teamValue = baseValue + budgetAmount + popularityBonus + marketBonus + stadiumBonus;
		logger.debug("Initial team value calculated at " + teamValue);
		return teamValue;
	}

	private static double getMarketValueBonus(MarketSize marketSize) {
		if (marketSize == null) {
			logger.warn("Skipping market value bonus calculation because market size is null");
			return 0.0;
		}

		logger.trace("Calculating market value bonus for " + marketSize.getClass().getSimpleName());
		return marketSize.accept(new CalculateInitialTeamValue());
	}
}
