package data.league.finance;

public class LeagueRedistributionPolicy {

   private double leagueRetentionRate; // part gardée par la ligue

   private double redistributionRate; // combien les riches donnent
   private double equalShareRate; // part distribuée equally
   private double weightedShareRate; // part distribuée selon besoin

   public LeagueRedistributionPolicy(double leagueRetentionRate,
         double redistributionRate,
         double equalShareRate,
         double weightedShareRate) {
      this.leagueRetentionRate = leagueRetentionRate;
      this.redistributionRate = redistributionRate;
      this.equalShareRate = equalShareRate;
      this.weightedShareRate = weightedShareRate;
   }

   public double getLeagueRetentionRate() {
      return leagueRetentionRate;
   }

   public double getRedistributionRate() {
      return redistributionRate;
   }

   public double getEqualShareRate() {
      return equalShareRate;
   }

   public double getWeightedShareRate() {
      return weightedShareRate;
   }
}
