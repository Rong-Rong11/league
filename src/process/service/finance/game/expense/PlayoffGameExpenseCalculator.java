package process.service.finance.game.expense;

import data.finance.GameStat;
import data.league.PlayoffRound;
import data.sport.setup.Game;
import data.team.Team;
import process.service.finance.playoff.PlayoffFinancialRules;

public class PlayoffGameExpenseCalculator extends GameExpenseCalculator {

	private PlayoffFinancialRules playoffFinancialRules;

	public PlayoffGameExpenseCalculator(GameStat gameStat, PlayoffRound round) {
		super(gameStat);

		this.playoffFinancialRules = new PlayoffFinancialRules(round);
	}

	@Override
	public double getStadiumBonusRate(Game game, Team homeTeam, int attendees, double gamePopularity) {
		return playoffFinancialRules.getRoundStadiumCostBonusRate();
	}

	@Override
	public double getSecurityBonusRate(Game game, Team homeTeam, int attendees) {
		return playoffFinancialRules.getRoundSecurityBonusRate();
	}

	@Override
	public double getStaffBonusRate(Game game, Team homeTeam) {
		return playoffFinancialRules.getRoundStaffCostBonusRate();
	}

	@Override
	public double getTravelBonusRate(Game game) {
		return playoffFinancialRules.getRoundTravelBonusRate();
	}

	@Override
	public double getLogisticBonusRate(Game game, Team homeTeam) {
		return playoffFinancialRules.getRoundLogisticsBonusRate();
	}
}
