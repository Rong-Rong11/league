package process.visitor.actionresult;

import config.GameConfiguration;
import data.player.Player;
import data.sport.play.action.Block;
import data.sport.play.action.EndOfTime;
import data.sport.play.action.MissedShot;
import data.sport.play.action.PointScored;
import data.sport.play.action.Rebound;
import data.sport.play.action.Turnover;
import data.sport.setup.Game;

public class LiveActionTextVisitor implements ActionResultVisitor<String>{
	
	private Game game ; 
	private String homeTeamName ; 
	private String awayTeamName; 
	
	
	public LiveActionTextVisitor(Game game, String homeTeamName, String awayTeamName) {
		super();
		this.game = game;
		this.homeTeamName = homeTeamName;
		this.awayTeamName = awayTeamName;
	}

	@Override
	public String visit(PointScored pointScored) {
		Player scorer = pointScored.getScorerPlayer();
		String team = isHomePlayer(scorer) ? homeTeamName : awayTeamName;
		return team + " - " + scorer.getName() + " +" + computeDisplayedPoints(pointScored);
	}

	@Override
	public String visit(MissedShot missedShot) {
		Player shooter = missedShot.getShooter();
		String team = isHomePlayer(shooter) ? homeTeamName : awayTeamName;
			String shotLabel = "tir";
			if (missedShot.getOffensiveAction() != null) {
				String shotType = missedShot.getOffensiveAction().getName();
				if (GameConfiguration.THREEPOINT.equals(shotType)) {
					shotLabel = "3 points";
				} else if (GameConfiguration.TWOPOINT.equals(shotType)) {
					shotLabel = "2 points";
				} else if (GameConfiguration.FOULDRAW.equals(shotType)) {
					shotLabel = "lancer franc";
				}
			}
			return team + " - " + shooter.getName() + " rate un " + shotLabel;
		}

		@Override
		public String visit(Turnover turnover) {
			Player intercepted = turnover.getInterceptedPlayer();
			String team = isHomePlayer(intercepted) ? homeTeamName : awayTeamName;
			return team + " - Ballon perdu " + intercepted.getName();
		}

		@Override
		public String visit(Block block) {
			Player blocker = block.getBlockingPlayer();
			String team = isHomePlayer(blocker) ? homeTeamName : awayTeamName;
			return team + " - Contre " + blocker.getName();
		}

		@Override
	public String visit(Rebound rebound) {
		Player reboundPlayer = rebound.getReboundPlayer();
		String team = isHomePlayer(reboundPlayer) ? homeTeamName : awayTeamName;
		return team + " - Rebond " + reboundPlayer.getName();
	}

	@Override
	public String visit(EndOfTime endOfTime) {
		return "Fin de période";
	}
	
	private boolean isHomePlayer(Player player) {
		return game.getGameContext().getHomeTeam().getPlayers().containsKey(player.getName());
	}
	
	private int computeDisplayedPoints(PointScored pointScored) {
		if (pointScored.getOffensiveAction() == null) {
			return pointScored.getPointsScored();
		}
		String offensiveName = pointScored.getOffensiveAction().getName();
		if (GameConfiguration.THREEPOINT.equals(offensiveName)) {
			return 3;
		}
		if (GameConfiguration.TWOPOINT.equals(offensiveName)) {
			return 2;
		}
		return 1;
	}

}
