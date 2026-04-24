package process.builder.finance;

import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.financialpolicy.FinancialPolicy;
import data.team.finance.mediamarket.MediaMarket;
import data.team.finance.transfer.TeamTransferStrategy;

public class EconomicProfileBuilder {

	public static void build(EconomicProfil economicProfil, double teamPopularity,
			MediaMarket mediaMarket,
			FinancialPolicy financialProfil,
			TeamTransferStrategy transferStrategy) {

		double historicalPrestige = interval(0.25 + teamPopularity / 100.0 * 0.6);

		double fanLoyalty = interval(
				0.35 + teamPopularity / 100.0 * 0.35 + historicalPrestige * 0.25);

		double priceElasticity = interval(
				0.78 - fanLoyalty * 0.32 - historicalPrestige * 0.22);

		double commercialAggressiveness = interval(
				0.45 + mediaMarket.getBusinessOpportunityModifier() * 1.15);

		double ownerDeficitTolerance = interval(
				0.4 + getFinancialModifier(financialProfil));

		economicProfil.setFanLoyalty(fanLoyalty);
		economicProfil.setPriceElasticity(priceElasticity);
		economicProfil.setCommercialAggressiveness(commercialAggressiveness);
		economicProfil.setHistoricalPrestige(historicalPrestige);
		economicProfil.setOwnerDeficitTolerance(ownerDeficitTolerance);
	}

	private static double getFinancialModifier(FinancialPolicy financialProfil) {
		return 0.2; // simple version (a ameliorer plus tard)
	}

	private static double interval(double value) {
		return Math.max(0.0, Math.min(1.0, value));
	}
}
