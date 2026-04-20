/*
	* Decompiled with CFR 0.152.
	*/
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

	public LiveActionTextVisitor(Game game, String string, String string2) {
		this.game = game;
		this.homeTeamName = string;
		this.awayTeamName = string2;
	}

	@Override
	public String visit(PointScored pointScored) {
		Player player = pointScored.getScorerPlayer();
		String string = this.isHomePlayer(player) ? this.homeTeamName : this.awayTeamName;
		return string + " - " + player.getName() + " +" + this.computeDisplayedPoints(pointScored);
	}

	@Override
	public String visit(MissedShot missedShot) {
		Player player = missedShot.getShooter();
		String string = this.isHomePlayer(player) ? this.homeTeamName : this.awayTeamName;
		String string2 = "tir";
		if (missedShot.getOffensiveAction() != null) {
			String string3 = missedShot.getOffensiveAction().getName();
			if ("threepoint".equals(string3)) {
				string2 = "3 points";
			} else if ("twopoint".equals(string3)) {
				string2 = "2 points";
			} else if ("fouldraw".equals(string3)) {
				string2 = "lancer franc";
			}
		}
		return string + " - " + player.getName() + " rate un " + string2;
	}

	@Override
	public String visit(Turnover turnover) {
		Player player = turnover.getInterceptedPlayer();
		String string = this.isHomePlayer(player) ? this.homeTeamName : this.awayTeamName;
		return string + " - Ballon perdu " + player.getName();
	}

	@Override
	public String visit(Block block) {
		Player player = block.getBlockingPlayer();
		String string = this.isHomePlayer(player) ? this.homeTeamName : this.awayTeamName;
		return string + " - Contre " + player.getName();
	}

	@Override
	public String visit(Rebound rebound) {
		Player player = rebound.getReboundPlayer();
		String string = this.isHomePlayer(player) ? this.homeTeamName : this.awayTeamName;
		return string + " - Rebond " + player.getName();
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
		String string = pointScored.getOffensiveAction().getName();
		if ("threepoint".equals(string)) {
			return 3;
		}
		if ("twopoint".equals(string)) {
			return 2;
		}
		return 1;
	}
}
