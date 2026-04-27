package process.visitor.actionresult;

import data.player.Player;
import data.sport.play.action.Block;
import data.sport.play.action.EndOfTime;
import data.sport.play.action.MissedShot;
import data.sport.play.action.PointScored;
import data.sport.play.action.Rebound;
import data.sport.play.action.Turnover;
import data.sport.setup.Game;

public class LiveActionTextVisitor
		implements ActionResultVisitor<String> {
	private Game game;
	private String homeTeamName;
	private String awayTeamName;

	public LiveActionTextVisitor(Game game, String homeTeamName, String awayTeamName) {
		this.game = game;
		this.homeTeamName = homeTeamName;
		this.awayTeamName = awayTeamName;
	}

	@Override
	public String visit(PointScored pointScored) {
		Player player = pointScored.getScorerPlayer();
		String teamName = this.isHomePlayer(player) ? this.homeTeamName : this.awayTeamName;
		return teamName + " - " + player.getName() + " +" + this.computeDisplayedPoints(pointScored);
	}

	@Override
	public String visit(MissedShot missedShot) {
		Player player = missedShot.getShooter();
		String teamName = this.isHomePlayer(player) ? this.homeTeamName : this.awayTeamName;
		String shotLabel = "tir";
		if (missedShot.getOffensiveAction() != null) {
			String actionName = missedShot.getOffensiveAction().getName();
			if ("threepoint".equals(actionName)) {
				shotLabel = "3 points";
			} else if ("twopoint".equals(actionName)) {
				shotLabel = "2 points";
			} else if ("fouldraw".equals(actionName)) {
				shotLabel = "lancer franc";
			}
		}
		return teamName + " - " + player.getName() + " rate un " + shotLabel;
	}

	@Override
	public String visit(Turnover turnover) {
		Player player = turnover.getInterceptedPlayer();
		String teamName = this.isHomePlayer(player) ? this.homeTeamName : this.awayTeamName;
		return teamName + " - Ballon perdu " + player.getName();
	}

	@Override
	public String visit(Block block) {
		Player player = block.getBlockingPlayer();
		String teamName = this.isHomePlayer(player) ? this.homeTeamName : this.awayTeamName;
		return teamName + " - Contre " + player.getName();
	}

	@Override
	public String visit(Rebound rebound) {
		Player player = rebound.getReboundPlayer();
		String teamName = this.isHomePlayer(player) ? this.homeTeamName : this.awayTeamName;
		return teamName + " - Rebond " + player.getName();
	}

	@Override
	public String visit(EndOfTime endOfTime) {
		return "Fin de p\u00e9riode";
	}

	private boolean isHomePlayer(Player player) {
		return this.game.getGameContext().getHomeTeam().getCurrentPlayers().containsKey(player.getName());
	}

	private int computeDisplayedPoints(PointScored pointScored) {
		if (pointScored.getOffensiveAction() == null) {
			return pointScored.getPointsScored();
		}
		String actionName = pointScored.getOffensiveAction().getName();
		if ("threepoint".equals(actionName)) {
			return 3;
		}
		if ("twopoint".equals(actionName)) {
			return 2;
		}
		return 1;
	}
}
