package data.team.finance.mediamarket;

public class MediaMarket {

   private double fanBaseModifier;
   private double businessOpportunityModifier;
   private double prestigeModifier;
   private double pricingPowerModifier;

   public MediaMarket() {
      this.fanBaseModifier = 0.1;
      this.businessOpportunityModifier = 0.1;
      this.prestigeModifier = 0.1;
      this.pricingPowerModifier = 0.1;
   }

   public double getFanBaseModifier() {
      return fanBaseModifier;
   }

   public void setFanBaseModifier(double fanBaseModifier) {
      this.fanBaseModifier = fanBaseModifier;
   }

   public double getBusinessOpportunityModifier() {
      return businessOpportunityModifier;
   }

   public void setBusinessOpportunityModifier(double businessOpportunityModifier) {
      this.businessOpportunityModifier = businessOpportunityModifier;
   }

   public double getPrestigeModifier() {
      return prestigeModifier;
   }

   public void setPrestigeModifier(double prestigeModifier) {
      this.prestigeModifier = prestigeModifier;
   }

   public double getPricingPowerModifier() {
      return pricingPowerModifier;
   }

   public void setPricingPowerModifier(double pricingPowerModifier) {
      this.pricingPowerModifier = pricingPowerModifier;
   }
}
