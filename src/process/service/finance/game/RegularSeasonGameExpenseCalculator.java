package process.service.finance.game;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;

public class RegularSeasonGameExpenseCalculator extends GameExpenseCalculator {
	public RegularSeasonGameExpenseCalculator(GameStat gameStat) {
		super(gameStat);
	}

	@Override
	protected double getStadiumBonusRate(Game game, Team homeTeam, int attendees, double gamePopularity) {
		if (game.getGameContext().isRivalry()) {
			return 0.12;
		}
		if (attendees >= 18000) {
			return 0.06;
		}
		return 0.0;
	}

	@Override
	protected double getSecurityBonusRate(Game game, Team homeTeam, int attendees) {
		if (game.getGameContext().isRivalry()) {
			return 0.24;
		}
		if (attendees >= 18000) {
			return 0.12;
		}
		return 0.0;
	}

	@Override
	protected double getStaffBonusRate(Game game, Team homeTeam) {
		if (game.getGameContext().isRivalry()) {
			return 0.10;
		}
		return 0.0;
	}

	@Override
	protected double getTravelBonusRate(Game game) {
		return 0.0;
	}

	@Override
	protected double getLogisticBonusRate(Game game, Team homeTeam) {
		if (game.getGameContext().isRivalry()) {
			return 0.22;
		}
		return 0.0;
	}
}
