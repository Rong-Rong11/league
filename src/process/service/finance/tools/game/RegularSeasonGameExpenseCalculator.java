package process.service.finance.tools.game;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;

public class RegularSeasonGameExpenseCalculator extends GameExpenseCalculator {
   public RegularSeasonGameExpenseCalculator(GameStat gameStat) {
      super(gameStat);
   }

   @Override
   protected double getStadiumBonusRate(Game game, Team homeTeam, int attendees, double gamePopularity) {
      return 0.0;
   }

   @Override
   protected double getSecurityBonusRate(Game game, Team homeTeam, int attendees) {
      return 0.0;
   }

   @Override
   protected double getStaffBonusRate(Game game, Team homeTeam) {
      return 0.0;
   }

   @Override
   protected double getTravelBonusRate(Game game) {
      return 0.0;
   }

   @Override
   protected double getLogisticBonusRate(Game game, Team homeTeam) {
      return 0.0;
   }
}
