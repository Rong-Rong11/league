package data.league.finance;

public class LeagueRedistributionPolicy {

   private double baseLeagueRetentionRate = 0.05;
   private double baseRedistributionRate = 0.25;
   private double baseEqualShareRate = 0.4;
   private double baseWeightedShareRate = 0.6;
   private double minimumRedistributionRate = 0.15;
   private double maximumRedistributionRate = 0.4;

   public LeagueRedistributionPolicy() {
   }

   public double getBaseLeagueRetentionRate() {
      return baseLeagueRetentionRate;
   }

   public void setBaseLeagueRetentionRate(double baseLeagueRetentionRate) {
      this.baseLeagueRetentionRate = baseLeagueRetentionRate;
   }

   public double getBaseRedistributionRate() {
      return baseRedistributionRate;
   }

   public void setBaseRedistributionRate(double baseRedistributionRate) {
      this.baseRedistributionRate = baseRedistributionRate;
   }

   public double getBaseEqualShareRate() {
      return baseEqualShareRate;
   }

   public void setBaseEqualShareRate(double baseEqualShareRate) {
      this.baseEqualShareRate = baseEqualShareRate;
   }

   public double getBaseWeightedShareRate() {
      return baseWeightedShareRate;
   }

   public void setBaseWeightedShareRate(double baseWeightedShareRate) {
      this.baseWeightedShareRate = baseWeightedShareRate;
   }

   public double getMinimumRedistributionRate() {
      return minimumRedistributionRate;
   }

   public void setMinimumRedistributionRate(double minimumRedistributionRate) {
      this.minimumRedistributionRate = minimumRedistributionRate;
   }

   public double getMaximumRedistributionRate() {
      return maximumRedistributionRate;
   }

   public void setMaximumRedistributionRate(double maximumRedistributionRate) {
      this.maximumRedistributionRate = maximumRedistributionRate;
   }

}
