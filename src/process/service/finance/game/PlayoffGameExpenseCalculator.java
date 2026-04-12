package process.service.finance.game;

import data.finance.GameStat;
import data.league.PlayoffRound;
import data.sport.setup.Game;
import data.team.Team;
import process.service.finance.playoff.PlayoffFinancialRules;

public class PlayoffGameExpenseCalculator extends GameExpenseCalculator {
   private PlayoffRound round;
   private PlayoffFinancialRules playoffFinancialRules;

   public PlayoffGameExpenseCalculator(GameStat gameStat, PlayoffRound round) {
      super(gameStat);
      this.round = round;
      this.playoffFinancialRules = new PlayoffFinancialRules(round);
   }

   @Override
   protected double getStadiumBonusRate(Game game, Team homeTeam, int attendees, double gamePopularity) {
      return playoffFinancialRules.getRoundStadiumCostBonusRate();
   }

   @Override
   protected double getSecurityBonusRate(Game game, Team homeTeam, int attendees) {
      return playoffFinancialRules.getRoundSecurityBonusRate();
   }

   @Override
   protected double getStaffBonusRate(Game game, Team homeTeam) {
      return playoffFinancialRules.getRoundStaffCostBonusRate();
   }

   @Override
   protected double getTravelBonusRate(Game game) {
      return playoffFinancialRules.getRoundTravelBonusRate();
   }

   @Override
   protected double getLogisticBonusRate(Game game, Team homeTeam) {
      return playoffFinancialRules.getRoundLogisticsBonusRate();
   }
}
