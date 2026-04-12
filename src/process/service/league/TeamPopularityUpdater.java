package process.service.league;

import data.league.PlayoffRound;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.mediamarket.MediaMarket;
import data.team.finance.transfer.TeamTransferStrategy;
import process.repository.TeamRepository;

public class TeamPopularityUpdater {
   private TeamRepository teamRepositery = TeamRepository.getInstance();

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

   public void applyPlayoffQualificationBonus(Team team) {
      double bonus = 2.0;
      double newPopularity = clampPopularity(team.getCurrentPopularity() + bonus);
      team.setCurrentPopularity(newPopularity);
   }

   public void applyPlayoffRoundBonus(Team team, PlayoffRound round) {
      double bonus = getPlayoffRoundPopularityBonus(round);
      double newPopularity = clampPopularity(team.getCurrentPopularity() + bonus);
      team.setCurrentPopularity(newPopularity);
   }

   public void applyMissedPlayoffPenalty(Team team) {
      double penalty = calculateMissedPlayoffPenalty(team);
      double newPopularity = clampPopularity(team.getCurrentPopularity() - penalty);
      team.setCurrentPopularity(newPopularity);
   }

   // à varier car sinon on tous la même note
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

      variation += economicProfil.getFanLoyalty() - 0.5 * 1.5;
      variation += economicProfil.getHistoricalPrestige() - 0.5 * 1.2;
      variation += economicProfil.getCommercialAggressiveness() - 0.5 * 0.8;

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
      return (Math.random() * amplitude);
   }

   private double getPlayoffRoundPopularityBonus(PlayoffRound round) {
      if (round == null) {
         return 0.0;
      }

      switch (round) {
         case FIRST_ROUND:
            return 2.5;
         case CONFERENCE_SEMIFINALS:
            return 3.5;
         case CONFERENCE_FINALS:
            return 5.0;
         case NBA_FINALS:
            return 7.0;
         default:
            return 0.0;
      }
   }

   private double calculateMissedPlayoffPenalty(Team team) {
      EconomicProfil economicProfil = team.getTeamFinance().getEconomicProfil();
      MediaMarket mediaMarket = team.getTeamFinance().getMediaMarket();
      double currentPopularity = team.getCurrentPopularity();

      if (currentPopularity < 65) {
         return 0.0;
      }

      double penalty = 0.0;

      penalty += (currentPopularity - 65) * 0.08;
      penalty += economicProfil.getHistoricalPrestige() * 1.5;
      penalty += economicProfil.getCommercialAggressiveness() * 1.0;
      penalty += mediaMarket.getPrestigeModifier() * 1.2;
      penalty += mediaMarket.getFanBaseModifier() * 1.0;
      penalty += mediaMarket.getBusinessOpportunityModifier() * 0.8;
      penalty -= economicProfil.getFanLoyalty() * 1.2;

      return Math.max(0.0, penalty);
   }

   private double clampPopularity(double popularity) {
      return Math.max(20.0, Math.min(100.0, popularity));
   }
}
