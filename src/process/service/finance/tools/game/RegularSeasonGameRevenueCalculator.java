package process.service.finance.tools.game;

import java.time.LocalDate;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;

public class RegularSeasonGameRevenueCalculator extends GameRevenueCalculator {

   public RegularSeasonGameRevenueCalculator(GameStat gameStat) {
      super(gameStat);
   }

   @Override
   protected double getPopularityBonusRate(Game game, LocalDate date, Team homeTeam) {
      return 0.0;
   }

   @Override
   protected double getAttendanceBonusRate(Team homeTeam, double popularityRate) {
      return 0.0;
   }

   @Override
   protected double getTicketPriceBonusRate(Game game, Team homeTeam, int attendees, double popularityRate) {
      return 0.0;
   }

   @Override
   protected double getTicketRevenueBonusRate(Game game, int attendees, double ticketPrice) {
      return 0.0;
   }

   @Override
   protected double getConcessionsBonusRate(Game game, Team homeTeam, int attendees, double popularityRate) {
      return 0.0;
   }

   @Override
   protected double getParkingBonusRate(Game game, Team homeTeam, int attendees) {
      return 0.0;
   }

   @Override
   protected double getHomeTvBonusRate(Game game) {
      return 0.0;
   }

   @Override
   protected double getAwayTvBonusRate(Game game) {
      return 0.0;
   }

   @Override
   protected double getMerchBonusRate(Game game, Team homeTeam, int attendees, double popularityRate) {
      return 0.0;
   }
}
