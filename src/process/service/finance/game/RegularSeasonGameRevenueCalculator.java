package process.service.finance.game;

import java.time.LocalDate;

import data.finance.GameStat;
import data.league.League;
import data.sport.setup.Game;
import data.team.Team;
import process.utility.CalendarUtility;

public class RegularSeasonGameRevenueCalculator extends GameRevenueCalculator {

	public RegularSeasonGameRevenueCalculator(League league, GameStat gameStat) {
		super(league, gameStat);
	}

	@Override
	protected double getPopularityBonusRate(Game game, LocalDate date, Team homeTeam) {
	  double score = CalendarUtility.popularityScoreGame(game, date);
	  if (game.getGameContext().isRivalry() || score >= 120) {
		 return 0.10;
	  }
	  if (score >= 95) {
		 return 0.05;
	  }
	  return 0.0;
	}

	@Override
	protected double getAttendanceBonusRate(Team homeTeam, double popularityRate) {
		if (popularityRate >= 0.85) {
			return 0.04;
		}
		if (popularityRate >= 0.75) {
			return 0.02;
		}
		return 0.0;
	}

	@Override
	protected double getTicketPriceBonusRate(Game game, Team homeTeam, int attendees, double popularityRate) {
	  if (game.getGameContext().isRivalry()) {
		 return 0.10;
	  }
	  if (popularityRate >= 0.85) {
		 return 0.06;
	  }
	  if (popularityRate >= 0.75) {
		 return 0.03;
	  }
	  return 0.0;
	}

	@Override
	protected double getTicketRevenueBonusRate(Game game, int attendees, double ticketPrice) {
	  if (attendees >= 18000) {
		 return 0.06;
	  }
	  if (attendees >= 15000) {
		 return 0.03;
	  }
	  return 0.0;
	}

	@Override
	protected double getConcessionsBonusRate(Game game, Team homeTeam, int attendees, double popularityRate) {
	  if (game.getGameContext().isRivalry()) {
		 return 0.08;
	  }
	  if (popularityRate >= 0.8) {
		 return 0.05;
	  }
	  return 0.0;
	}

	@Override
	protected double getParkingBonusRate(Game game, Team homeTeam, int attendees) {
	  if (game.getGameContext().isRivalry()) {
		 return 0.05;
	  }
	  if (attendees >= 17000) {
		 return 0.03;
	  }
	  return 0.0;
	}

	@Override
	protected double getHomeTvBonusRate(Game game) {
	  if (game.getGameContext().isRivalry()) {
		 return 0.16;
	  }
	  return 0.0;
	}

	@Override
	protected double getAwayTvBonusRate(Game game) {
	  if (game.getGameContext().isRivalry()) {
		 return 0.12;
	  }
	  return 0.0;
	}

	@Override
	protected double getMerchBonusRate(Game game, Team homeTeam, int attendees, double popularityRate) {
	  if (game.getGameContext().isRivalry()) {
		 return 0.12;
	  }
	  if (popularityRate >= 0.8) {
		 return 0.07;
	  }
	  return 0.0;
	}
}
