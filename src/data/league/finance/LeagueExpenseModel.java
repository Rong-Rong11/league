package data.league.finance;

public class LeagueExpenseModel {

   private double administrativeCost;
   private double mediaCost;
   private double marketingRate;
   private double officiatingCostPerGame;

   public LeagueExpenseModel(double administrativeCost,
         double mediaCost,
         double marketingRate,
         double officiatingCostPerGame) {
      this.administrativeCost = administrativeCost;
      this.mediaCost = mediaCost;
      this.marketingRate = marketingRate;
      this.officiatingCostPerGame = officiatingCostPerGame;
   }

   public double getAdministrativeCost() {
      return administrativeCost;
   }

   public double getMediaCost() {
      return mediaCost;
   }

   public double getMarketingRate() {
      return marketingRate;
   }

   public double getOfficiatingCostPerGame() {
      return officiatingCostPerGame;
   }
}
