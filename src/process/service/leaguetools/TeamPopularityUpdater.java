package process.service.leaguetools;

import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.mediamarket.MediaMarket;
import data.team.finance.transfer.TeamTransferStrategy;
import process.repositery.TeamRepositery;

public class TeamPopularityUpdater {
   private TeamRepositery teamRepositery = TeamRepositery.getInstance();

   public TeamPopularityUpdater() {

   }

   public void updateBeforeSeason() {
      for (Team team : teamRepositery.getAllTeams()) {
         updateTeamBeforeSeason(team);
      }
   }

   public void updateMonthlyPopularity() {
      for (Team team : teamRepositery.getAllTeams()) {
         updateTeamMonthlyPopularity(team);
      }
   }

   // a varier car sinon on tous la meme note
   private void updateTeamBeforeSeason(Team team) {
      double currentPopularity = team.getFormerPopularity();
      double variation = 0.0;

      variation += calculateCommonPopularityBase(team);
      variation += calculatePreSeasonSpecificVariation(team);
      variation += calculateRandomVariation(0.8);

      double newPopularity = clampPopularity(currentPopularity + variation);
      team.setFormerPopularity(newPopularity);
      team.setCurrentPopularity(newPopularity);
   }

   private void updateTeamMonthlyPopularity(Team team) {
      double currentPopularity = team.getCurrentPopularity();
      double variation = 0.0;

      variation += calculateCommonPopularityBase(team);
      variation += calculateMonthlySpecificVariation(team);
      variation += calculateRandomVariation(0.6);

      double newPopularity = currentPopularity + (variation * 0.4);
      newPopularity = clampPopularity(newPopularity);
      team.setCurrentPopularity(newPopularity);
   }

   private double calculateCommonPopularityBase(Team team) {
      double variation = 0.0;

      EconomicProfil economicProfil = team.getTeamFinance().getEconomicProfil();
      MediaMarket mediaMarket = team.getTeamFinance().getMediaMarket();

      if (team.hasStarPlayer()) {
         variation += 1.0;
      }

      variation += (economicProfil.getFanLoyalty() - 0.5) * 1.5;
      variation += (economicProfil.getHistoricalPrestige() - 0.5) * 1.2;
      variation += (economicProfil.getCommercialAggressiveness() - 0.5) * 0.8;

      variation += mediaMarket.getFanBaseModifier() * 1.5;
      variation += mediaMarket.getPrestigeModifier() * 1.2;
      variation += mediaMarket.getBusinessOpportunityModifier() * 0.8;

      return variation;
   }

   private double calculatePreSeasonSpecificVariation(Team team) {
      double variation = 0.0;

      EconomicProfil economicProfil = team.getTeamFinance().getEconomicProfil();
      MediaMarket mediaMarket = team.getTeamFinance().getMediaMarket();
      TeamTransferStrategy strategy = team.getTeamFinance().getTeamTransferStrategy();
      if (team.hasStarPlayer()) {
         variation += 1.0;
      }
      if (strategy.isAllIn()) {
         variation += 1.5;
      } else if (strategy.isRebuild()) {
         variation -= 1.5;
      }
      double payroll = team.getTeamFinance().getCurrentPayroll();
      variation += (payroll / 200.0);
      variation += mediaMarket.getPrestigeModifier() * 1.2;
      variation += (Math.random() * 1.0) - 0.5;

      return variation;
   }

   private double calculateMonthlySpecificVariation(Team team) {
      double variation = 0.0;
      double performance = team.getTeamPerformance().getPerformanceRating();
      int winStreak = team.getTeamPerformance().getCurrentWinStreak();
      variation += (performance - 0.5) * 4.0;
      variation += Math.min(winStreak, 8) * 0.25;

      return variation;
   }

   private double calculateRandomVariation(double amplitude) {
      return (Math.random() * amplitude) - (amplitude / 2.0);
   }

   private double clampPopularity(double popularity) {
      return Math.max(20.0, Math.min(100.0, popularity));
   }
}
