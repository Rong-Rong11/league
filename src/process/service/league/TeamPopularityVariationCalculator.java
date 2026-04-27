package process.service.league;

import org.apache.log4j.Logger;

import data.team.Team;
import data.team.finance.economicprofile.EconomicProfile;
import data.team.finance.mediamarket.MediaMarket;
import data.team.finance.transfer.TeamTransferStrategy;
import log.LoggerUtility;

public class TeamPopularityVariationCalculator {
	private static final Logger logger = LoggerUtility.getLogger(TeamPopularityVariationCalculator.class, "text");

	public double calculatePreSeasonVariation(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			logger.warn("Returning 0 preseason popularity variation because team or team finance is null");
			return 0.0;
		}

		double variation = 0.0;
		variation += calculateCommonPopularityBase(team);
		variation += calculatePreSeasonSpecificVariation(team);
		variation += calculateRandomVariation(0.8);

		logger.trace("Calculated preseason popularity variation " + variation + " for " + team.getName());
		return variation;
	}

	public double calculateMonthlyVariation(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			logger.warn("Returning 0 monthly popularity variation because team or team finance is null");
			return 0.0;
		}

		double variation = 0.0;
		variation += calculateCommonPopularityBase(team);
		variation += calculateMonthlySpecificVariation(team);
		variation += calculateRandomVariation(0.6);

		logger.trace("Calculated monthly popularity variation " + variation + " for " + team.getName());
		return variation;
	}

	private double calculateCommonPopularityBase(Team team) {
		double variation = 0.0;

		EconomicProfile economicProfile = team.getTeamFinance().getStructure().getEconomicProfile();
		MediaMarket mediaMarket = team.getTeamFinance().getStructure().getMediaMarket();

		if (team.hasStarPlayer()) {
			variation += 1.4;
		}

		variation += economicProfile.getFanLoyalty() - 0.5 * 1.5;
		variation += economicProfile.getHistoricalPrestige() - 0.5 * 1.2;
		variation += economicProfile.getCommercialAggressiveness() - 0.5 * 0.8;

		variation += mediaMarket.getFanBaseModifier() * 1.5;
		variation += mediaMarket.getPrestigeModifier() * 1.2;
		variation += mediaMarket.getBusinessOpportunityModifier() * 0.8;

		logger.trace("Calculated common popularity base " + variation + " for " + team.getName());
		return variation;
	}

	private double calculatePreSeasonSpecificVariation(Team team) {
		double variation = 0.0;

		MediaMarket mediaMarket = team.getTeamFinance().getStructure().getMediaMarket();
		TeamTransferStrategy strategy = team.getTeamFinance().getBehavior().getTeamTransferStrategy();

		if (team.hasStarPlayer()) {
			variation += 1.0;
		}

		if (strategy.isAllIn()) {
			variation += 1.5;
		} else if (strategy.isRebuild()) {
			variation -= 1.5;
		}

		double payroll = team.getTeamFinance().getCurrentPayroll();
		variation += (payroll / 200.0);
		variation += mediaMarket.getPrestigeModifier() * 1.2;
		variation += (Math.random() * 1.0) - 0.5;

		logger.trace("Calculated preseason specific popularity variation " + variation + " for " + team.getName());
		return variation;
	}

	private double calculateMonthlySpecificVariation(Team team) {
		double variation = 0.0;
		double performance = team.getTeamPerformance().getPerformanceRating();
		int winStreak = team.getTeamPerformance().getCurrentWinStreak();

		variation += (performance - 0.5) * 6.0;
		variation += Math.min(winStreak, 10) * 0.35;

		logger.trace("Calculated monthly specific popularity variation " + variation + " for " + team.getName());
		return variation;
	}

	private double calculateRandomVariation(double amplitude) {
		double variation = Math.random() * amplitude;
		logger.trace("Calculated random popularity variation " + variation + " with amplitude " + amplitude);
		return variation;
	}
}
