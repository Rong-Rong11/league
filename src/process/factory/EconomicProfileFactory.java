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

            double historicalPrestige = clamp(0.2 + teamPopularity / 100.0 * 0.5);

            double fanLoyalty = clamp(
                        0.3 + teamPopularity / 100.0 * 0.3 + historicalPrestige * 0.2);

            double priceElasticity = clamp(
                        0.8 - fanLoyalty * 0.3 - historicalPrestige * 0.2);

            double commercialAggressiveness = clamp(
                        0.4 + mediaMarket.getBusinessOpportunityModifier());

            double ownerDeficitTolerance = clamp(
                        0.4 + getFinancialModifier(financialProfil));

            economicProfil.setFanLoyalty(fanLoyalty);
            economicProfil.setPriceElasticity(priceElasticity);
            economicProfil.setCommercialAggressiveness(commercialAggressiveness);
            economicProfil.setOwnerDeficitTolerance(ownerDeficitTolerance);
      }

      private static double getFinancialModifier(FinancialPolicy financialProfil) {
            return 0.2; // simple version (à améliorer plus tard)
      }

      private static double clamp(double value) {
            return Math.max(0.0, Math.min(1.0, value));
      }
}
