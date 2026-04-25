package process.service.league;

import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.mediamarket.MediaMarket;
import data.team.finance.transfer.TeamTransferStrategy;

public class TeamPopularityVariationCalculator {

	public double calculatePreSeasonVariation(Team team) {
		double variation = 0.0;
		variation += calculateCommonPopularityBase(team);
		variation += calculatePreSeasonSpecificVariation(team);
		variation += calculateRandomVariation(0.8);
		return variation;
	}

	public double calculateMonthlyVariation(Team team) {
		double variation = 0.0;
		variation += calculateCommonPopularityBase(team);
		variation += calculateMonthlySpecificVariation(team);
		variation += calculateRandomVariation(0.6);
		return variation;
	}

	private double calculateCommonPopularityBase(Team team) {
		double variation = 0.0;

		EconomicProfil economicProfil = team.getTeamFinance().getStructure().getEconomicProfil();
		MediaMarket mediaMarket = team.getTeamFinance().getStructure().getMediaMarket();

		if (team.hasStarPlayer()) {
			variation += 1.4;
		}

		variation += economicProfil.getFanLoyalty() - 0.5 * 1.5;
		variation += economicProfil.getHistoricalPrestige() - 0.5 * 1.2;
		variation += economicProfil.getCommercialAggressiveness() - 0.5 * 0.8;

		variation += mediaMarket.getFanBaseModifier() * 1.5;
		variation += mediaMarket.getPrestigeModifier() * 1.2;
		variation += mediaMarket.getBusinessOpportunityModifier() * 0.8;

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

		return variation;
	}

	private double calculateMonthlySpecificVariation(Team team) {
		double variation = 0.0;
		double performance = team.getTeamPerformance().getPerformanceRating();
		int winStreak = team.getTeamPerformance().getCurrentWinStreak();
		variation += (performance - 0.5) * 6.0;
		variation += Math.min(winStreak, 10) * 0.35;

		return variation;
	}

	private double calculateRandomVariation(double amplitude) {
		return (Math.random() * amplitude);
	}
}
