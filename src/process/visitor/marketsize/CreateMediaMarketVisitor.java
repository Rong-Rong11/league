package process.visitor.marketsize;

import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;
import data.team.finance.mediamarket.MediaMarket;

public class CreateMediaMarketVisitor implements MarketSizeVisitor<Void> {

   private static final double BASE = 0.05;

   private MediaMarket mediaMarket;

   public CreateMediaMarketVisitor(MediaMarket mediaMarket) {
      super();
      this.mediaMarket = mediaMarket;
   }

   @Override
   public Void visit(LargeSize largeSize) {
      applyMultipliers(7.0 * Math.random());
      return null;
   }

   @Override
   public Void visit(MediumSize mediumSize) {
      applyMultipliers(5.0 * Math.random());
      return null;
   }

   @Override
   public Void visit(SmallSize smallSize) {
      applyMultipliers(3.0 * Math.random());
      return null;
   }

   private void applyMultipliers(double multiplier) {
      mediaMarket.setFanBaseModifier(BASE * multiplier);
      mediaMarket.setBusinessOpportunityModifier(BASE * multiplier);
      mediaMarket.setPrestigeModifier(BASE * multiplier * 0.6); // un peu moins fort
      mediaMarket.setPricingPowerModifier(BASE * multiplier);
   }
}