package process.builder.finance;

import org.apache.log4j.Logger;

import data.team.finance.economicprofile.EconomicProfile;
import data.team.finance.financialpolicy.FinancialPolicy;
import data.team.finance.mediamarket.MediaMarket;
import data.team.finance.transfer.TeamTransferStrategy;
import log.LoggerUtility;

public class EconomicProfileBuilder {
	private static final Logger logger = LoggerUtility.getLogger(EconomicProfileBuilder.class, "text");

	public static void build(EconomicProfile economicProfile, double teamPopularity,
			MediaMarket mediaMarket,
			FinancialPolicy financialPolicy,
			TeamTransferStrategy transferStrategy) {
		if (economicProfile == null) {
			logger.warn("Skipping economic profile build because economic profile is null");
			return;
		}
		if (mediaMarket == null) {
			logger.warn("Skipping economic profile build because media market is null");
			return;
		}
		if (financialPolicy == null) {
			logger.warn("Skipping economic profile build because financial policy is null");
			return;
		}
		if (transferStrategy == null) {
			logger.warn("Building economic profile with null transfer strategy");
		}

		logger.info("Building economic profile");
		logger.debug("Economic profile build with popularity "
				+ teamPopularity
				+ ", media market "
				+ mediaMarket.getClass().getSimpleName()
				+ " and policy "
				+ financialPolicy.getClass().getSimpleName());

		double historicalPrestige = interval(0.25 + teamPopularity / 100.0 * 0.6);

		double fanLoyalty = interval(
				0.35 + teamPopularity / 100.0 * 0.35 + historicalPrestige * 0.25);

		double priceElasticity = interval(
				0.78 - fanLoyalty * 0.32 - historicalPrestige * 0.22);

		double commercialAggressiveness = interval(
				0.45 + mediaMarket.getBusinessOpportunityModifier() * 1.15);

		double ownerDeficitTolerance = interval(
				0.4 + getFinancialModifier(financialPolicy));

		economicProfile.setFanLoyalty(fanLoyalty);
		economicProfile.setPriceElasticity(priceElasticity);
		economicProfile.setCommercialAggressiveness(commercialAggressiveness);
		economicProfile.setHistoricalPrestige(historicalPrestige);
		economicProfile.setOwnerDeficitTolerance(ownerDeficitTolerance);
		logger.debug("Economic profile built with fan loyalty "
				+ fanLoyalty
				+ ", price elasticity "
				+ priceElasticity
				+ ", commercial aggressiveness "
				+ commercialAggressiveness
				+ ", historical prestige "
				+ historicalPrestige
				+ " and owner deficit tolerance "
				+ ownerDeficitTolerance);
		logger.info("Economic profile build completed");
	}

	private static double getFinancialModifier(FinancialPolicy financialPolicy) {
		logger.trace("Applying financial modifier for " + financialPolicy.getClass().getSimpleName());
		return 0.2; // simple version (a ameliorer plus tard)
	}

	private static double interval(double value) {
		return Math.max(0.0, Math.min(1.0, value));
	}
}
