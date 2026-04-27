package process.service.finance.team.calculation;

import org.apache.log4j.Logger;

import data.team.Team;
import data.team.finance.MonthlyTeamRevenue;
import data.team.finance.TeamFinance;
import data.team.finance.economicprofile.EconomicProfile;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import log.LoggerUtility;
import process.service.finance.team.provider.MonthlyTeamFinanceMultiplierProvider;
import process.utility.FinanceUtility;

public class MonthlyTeamRevenueCalculator {
	private static final Logger logger = LoggerUtility.getLogger(MonthlyTeamRevenueCalculator.class, "text");

	private TeamFinanceRateCalculator rateCalculator;
	private MonthlyTeamFinanceMultiplierProvider multiplierProvider;

	public MonthlyTeamRevenueCalculator(TeamFinanceRateCalculator rateCalculator,
			MonthlyTeamFinanceMultiplierProvider multiplierProvider) {
		this.rateCalculator = rateCalculator;
		this.multiplierProvider = multiplierProvider;
	}

	public MonthlyTeamRevenue calculateRevenue(Team team, int month) {
		if (team == null || team.getTeamFinance() == null) {
			logger.warn("Skipping monthly team revenue calculation because team or team finance is null");
			return new MonthlyTeamRevenue(0.0, 0.0, 0.0);
		}
		logger.debug("Calculating monthly team revenue for " + team.getName() + " month " + month);
		TeamFinance teamFinance = team.getTeamFinance();
		MarketSize marketSize = teamFinance.getStructure().getMarketSize();
		MediaMarket mediaMarket = teamFinance.getStructure().getMediaMarket();
		EconomicProfile economicProfile = teamFinance.getStructure().getEconomicProfile();
		double teamValueFactor = FinanceUtility.getNormalizedTeamValue(team);

		double marketMultiplier = rateCalculator.getMarketMultiplier(marketSize);
		double popularityFactor = team.getCurrentPopularity() / 100.0;
		double starFactor = team.hasStarPlayer() ? 1.18 : 1.0;
		double performanceFactor = 0.82 + (team.getTeamPerformance().getPerformanceRating() * 0.38);
		logger.trace("Monthly revenue factors for "
				+ team.getName()
				+ ": marketMultiplier="
				+ marketMultiplier
				+ ", popularityFactor="
				+ popularityFactor
				+ ", starFactor="
				+ starFactor
				+ ", performanceFactor="
				+ performanceFactor
				+ ", teamValueFactor="
				+ teamValueFactor);

		double localSponsoring = 4.8 * marketMultiplier * popularityFactor * starFactor;
		double localMerchandising = 2 * marketMultiplier * popularityFactor * starFactor;
		double otherRevenue = 0.3 * marketMultiplier * performanceFactor;
		logger.trace("Base monthly revenues for "
				+ team.getName()
				+ ": sponsoring="
				+ localSponsoring
				+ ", merchandising="
				+ localMerchandising
				+ ", other="
				+ otherRevenue);

		localSponsoring *= (1 + team.getTeamPerformance().getPerformanceRating() * 0.30);
		localMerchandising *= (1. + team.getTeamPerformance().getPerformanceRating() * 0.22);

		localSponsoring *= (1 + mediaMarket.getBusinessOpportunityModifier() * 0.28);
		localMerchandising *= (1 + mediaMarket.getPrestigeModifier() * 0.16);
		otherRevenue *= (1 + mediaMarket.getBusinessOpportunityModifier() * 0.10);

		localSponsoring *= (1 + economicProfile.getCommercialAggressiveness() * 0.22);
		localSponsoring *= (1 + economicProfile.getHistoricalPrestige() * 0.10);
		localSponsoring *= (1 + teamValueFactor * 0.15);
		localSponsoring *= rateCalculator.getSmallMarketRevenueBoost(marketSize, 1.15);
		localSponsoring *= multiplierProvider.getLocalSponsoringMultiplier();
		localSponsoring *= rateCalculator.getMonthlyLocalRevenueRate(team, month, 0.070, 0.040);

		localMerchandising *= (1 + economicProfile.getFanLoyalty() * 0.24);
		localMerchandising *= (1 + economicProfile.getHistoricalPrestige() * 0.12);
		localMerchandising *= (1 + teamValueFactor * 0.14);
		localMerchandising *= rateCalculator.getSmallMarketRevenueBoost(marketSize, 1.16);
		localMerchandising *= multiplierProvider.getLocalMerchandisingMultiplier();
		localMerchandising *= rateCalculator.getMonthlyLocalRevenueRate(team, month, 0.105, 0.055);

		otherRevenue *= (1 + economicProfile.getOwnerDeficitTolerance() * 0.05);
		otherRevenue *= (1 + teamValueFactor * 0.08);
		otherRevenue *= rateCalculator.getSmallMarketRevenueBoost(marketSize, 1.10);
		otherRevenue *= multiplierProvider.getOtherRevenueMultiplier();
		otherRevenue *= rateCalculator.getMonthlyLocalRevenueRate(team, month, 0.080, 0.045);

		double seasonContextMultiplier = rateCalculator.getSeasonContextRevenueMultiplier(month);
		localSponsoring *= seasonContextMultiplier;
		localMerchandising *= seasonContextMultiplier;
		otherRevenue *= seasonContextMultiplier;
		logger.debug("Calculated monthly team revenue for "
				+ team.getName()
				+ ": sponsoring="
				+ localSponsoring
				+ ", merchandising="
				+ localMerchandising
				+ ", other="
				+ otherRevenue
				+ ", seasonContextMultiplier="
				+ seasonContextMultiplier);

		return new MonthlyTeamRevenue(localSponsoring, localMerchandising, otherRevenue);
	}
}
