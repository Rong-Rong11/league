package process.service.finance.game.expense;

import data.sport.setup.Game;
import data.team.Team;

public interface GameExpenseBonusProvider {
	double getStadiumBonusRate(Game game, Team homeTeam, int attendees, double gamePopularity);

	double getSecurityBonusRate(Game game, Team homeTeam, int attendees);

	double getStaffBonusRate(Game game, Team homeTeam);

	double getTravelBonusRate(Game game);

	double getLogisticBonusRate(Game game, Team homeTeam);
}
