package process.service.finance.game.revenue;

import java.time.LocalDate;

import data.sport.setup.Game;
import data.team.Team;

public interface GameRevenueBonusProvider {
	double getPopularityBonusRate(Game game, LocalDate date, Team homeTeam);

	double getAttendanceBonusRate(Team homeTeam, double popularityRate);

	double getTicketPriceBonusRate(Game game, Team homeTeam, int attendees, double popularityRate);

	double getTicketRevenueBonusRate(Game game, int attendees, double ticketPrice);

	double getConcessionsBonusRate(Game game, Team homeTeam, int attendees, double popularityRate);

	double getParkingBonusRate(Game game, Team homeTeam, int attendees);

	double getHomeTvBonusRate(Game game);

	double getAwayTvBonusRate(Game game);

	double getMerchBonusRate(Game game, Team homeTeam, int attendees, double popularityRate);
}
