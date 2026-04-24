package process.service.finance.game.revenue;

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
	public double getPopularityBonusRate(Game game, LocalDate date, Team homeTeam) {
	  return playoffFinancialRules.getRoundPopularityBonusRate();
	}

	@Override
	public double getAttendanceBonusRate(Team homeTeam, double popularityRate) {
	  return playoffFinancialRules.getRoundAttendanceBonusRate();
	}

	@Override
	public double getTicketPriceBonusRate(Game game, Team homeTeam, int attendees, double popularityRate) {
	  return playoffFinancialRules.getRoundTicketBonusRate();
	}

	@Override
	public double getTicketRevenueBonusRate(Game game, int attendees, double ticketPrice) {
	  return 0.0;
	}

	@Override
	public double getConcessionsBonusRate(Game game, Team homeTeam, int attendees, double popularityRate) {
	  return playoffFinancialRules.getRoundConcessionsBonusRate();
	}

	@Override
	public double getParkingBonusRate(Game game, Team homeTeam, int attendees) {
	  return playoffFinancialRules.getRoundParkingBonusRate();
	}

	@Override
	public double getHomeTvBonusRate(Game game) {
	  return playoffFinancialRules.getRoundTvBonusRate();
	}

	@Override
	public double getAwayTvBonusRate(Game game) {
	  return playoffFinancialRules.getRoundTvBonusRate();
	}

	@Override
	public double getMerchBonusRate(Game game, Team homeTeam, int attendees, double popularityRate) {
	  return playoffFinancialRules.getRoundMerchBonusRate();
	}
}
