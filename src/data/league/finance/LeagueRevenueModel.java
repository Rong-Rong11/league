package data.league.finance;

public class LeagueRevenueModel {

   private double baseTvRevenue;
   private double baseSponsoringRevenue;
   private double baseMerchRevenue;

   private double popularityWeight;
   private double performanceWeight;
   private double starPowerWeight;
   private double prestigeWeight;

   public LeagueRevenueModel(double baseTvRevenue,
         double baseSponsoringRevenue,
         double baseMerchRevenue,
         double popularityWeight,
         double performanceWeight,
         double starPowerWeight,
         double prestigeWeight) {

      this.baseTvRevenue = baseTvRevenue;
      this.baseSponsoringRevenue = baseSponsoringRevenue;
      this.baseMerchRevenue = baseMerchRevenue;
      this.popularityWeight = popularityWeight;
      this.performanceWeight = performanceWeight;
      this.starPowerWeight = starPowerWeight;
      this.prestigeWeight = prestigeWeight;
   }

   public double getBaseTvRevenue() {
      return baseTvRevenue;
   }

   public double getBaseSponsoringRevenue() {
      return baseSponsoringRevenue;
   }

   public double getBaseMerchRevenue() {
      return baseMerchRevenue;
   }

   public double getPopularityWeight() {
      return popularityWeight;
   }

   public double getPerformanceWeight() {
      return performanceWeight;
   }

   public double getStarPowerWeight() {
      return starPowerWeight;
   }

   public double getPrestigeWeight() {
      return prestigeWeight;
   }
}
