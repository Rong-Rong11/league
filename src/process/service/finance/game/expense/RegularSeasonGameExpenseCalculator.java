package process.service.finance.game.expense;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;

public class RegularSeasonGameExpenseCalculator extends GameExpenseCalculator {
	public RegularSeasonGameExpenseCalculator(GameStat gameStat) {
		super(gameStat);
	}

	@Override
	public double getStadiumBonusRate(Game game, Team homeTeam, int attendees, double gamePopularity) {
		if (game.getGameContext().isRivalry()) {
			return 0.16;
		}
		if (attendees >= 18000) {
			return 0.08;
		}
		return 0.0;
	}

	@Override
	public double getSecurityBonusRate(Game game, Team homeTeam, int attendees) {
		if (game.getGameContext().isRivalry()) {
			return 0.30;
		}
		if (attendees >= 18000) {
			return 0.16;
		}
		return 0.0;
	}

	@Override
	public double getStaffBonusRate(Game game, Team homeTeam) {
		if (game.getGameContext().isRivalry()) {
			return 0.13;
		}
		return 0.0;
	}

	@Override
	public double getTravelBonusRate(Game game) {
		return 0.0;
	}

	@Override
	public double getLogisticBonusRate(Game game, Team homeTeam) {
		if (game.getGameContext().isRivalry()) {
			return 0.27;
		}
		return 0.0;
	}
}
