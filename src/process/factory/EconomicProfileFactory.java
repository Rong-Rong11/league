package process.factory;

import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.financialpolicy.FinancialPolicy;
import data.team.finance.mediamarket.MediaMarket;
import data.team.finance.transfer.TeamTransferStrategy;

public class EconomicProfileFactory {
      public static void create(EconomicProfil economicProfil, double teamPopularity,
                  MediaMarket mediaMarket,
                  FinancialPolicy financialProfil,
                  TeamTransferStrategy transferStrategy) {

            double historicalPrestige = interval(0.2 + teamPopularity / 100.0 * 0.5);

            double fanLoyalty = interval(
                        0.3 + teamPopularity / 100.0 * 0.3 + historicalPrestige * 0.2);

            double priceElasticity = interval(
                        0.8 - fanLoyalty * 0.3 - historicalPrestige * 0.2);

            double commercialAggressiveness = interval(
                        0.4 + mediaMarket.getBusinessOpportunityModifier());

            double ownerDeficitTolerance = interval(
                        0.4 + getFinancialModifier(financialProfil));

            economicProfil.setFanLoyalty(fanLoyalty);
            economicProfil.setPriceElasticity(priceElasticity);
            economicProfil.setCommercialAggressiveness(commercialAggressiveness);
            economicProfil.setOwnerDeficitTolerance(ownerDeficitTolerance);
      }

      private static double getFinancialModifier(FinancialPolicy financialProfil) {
            return 0.2; // simple version (à améliorer plus tard)
      }

      private static double interval(double value) {
            return Math.max(0.0, Math.min(1.0, value));
      }
}
