package process.service.finance.game;

import java.time.LocalDate;

import data.finance.GameStat;
import data.league.League;
import data.league.PlayoffRound;
import data.sport.setup.Game;
import data.team.Team;
import process.service.finance.playoff.PlayoffFinancialRules;

public class PlayoffGameRevenueCalculator extends GameRevenueCalculator {

   private PlayoffFinancialRules playoffFinancialRules;

   public PlayoffGameRevenueCalculator(League league, GameStat gameStat, PlayoffRound round) {
      super(league, gameStat);
      this.playoffFinancialRules = new PlayoffFinancialRules(round);
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
      return playoffFinancialRules.getRoundTicketBonusRate();
   }

   @Override
   protected double getTicketRevenueBonusRate(Game game, int attendees, double ticketPrice) {
      return 0.0;
   }

   @Override
   protected double getConcessionsBonusRate(Game game, Team homeTeam, int attendees, double popularityRate) {
      return playoffFinancialRules.getRoundConcessionsBonusRate();
   }

   @Override
   protected double getParkingBonusRate(Game game, Team homeTeam, int attendees) {
      return playoffFinancialRules.getRoundParkingBonusRate();
   }

   @Override
   protected double getHomeTvBonusRate(Game game) {
      return playoffFinancialRules.getRoundTvBonusRate();
   }

   @Override
   protected double getAwayTvBonusRate(Game game) {
      return playoffFinancialRules.getRoundTvBonusRate();
   }

   @Override
   protected double getMerchBonusRate(Game game, Team homeTeam, int attendees, double popularityRate) {
      return playoffFinancialRules.getRoundMerchBonusRate();
   }
}
