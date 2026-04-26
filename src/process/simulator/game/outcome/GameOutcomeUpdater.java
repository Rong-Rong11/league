package process.simulator.game.outcome;

import java.util.ArrayList;

import config.GameConfiguration;
import data.player.Player;
import data.sport.play.action.ActionResult;
import data.sport.setup.Game;
import data.sport.setup.GameResult;
import data.team.Team;
import data.team.TeamPerformance;
import process.utility.TeamUtility;
import process.visitor.actionresult.GameResultVisitor;

public class GameOutcomeUpdater {

	public void recordAction(GameResult gameResult, ActionResult actionResult, ArrayList<Player> homeTeamPlayers,
			ArrayList<Player> awayTeamPlayers) {
		GameResultVisitor visitor = new GameResultVisitor(gameResult, homeTeamPlayers, awayTeamPlayers);
		actionResult.accept(visitor);
		gameResult.addActions(actionResult);
	}

	public boolean shouldChangePossession(ActionResult terminalAction) {
		return terminalAction.getName().equals(GameConfiguration.SCORED_ACTION)
				|| terminalAction.getName().equals(GameConfiguration.DEFENSIVE_REBOUND_ACTION)
				|| terminalAction.getName().equals(GameConfiguration.TURNOVER_ACTION);
	}

	public void updateFinalOutcome(Game game) {
		Team homeTeam = game.getGameContext().getHomeTeam();
		Team awayTeam = game.getGameContext().getAwayTeam();
		int totalHome = totalHome(game);
		int totalAway = totalAway(game);
		game.setHomeFinalScore(totalHome);
		game.setAwayFinalScore(totalAway);

		TeamPerformance homePerformance = homeTeam.getTeamPerformance();
		TeamPerformance awayPerformance = awayTeam.getTeamPerformance();
		homePerformance.incrementNmberPlayedGames();
		awayPerformance.incrementNmberPlayedGames();

		if (totalHome > totalAway) {
			updateWinner(game, homeTeam, awayTeam, totalHome - totalAway);
		} else if (totalAway > totalHome) {
			updateWinner(game, awayTeam, homeTeam, totalAway - totalHome);
		} else {
			TeamUtility.updatePerformanceRating(homeTeam, awayTeam, 0, 0, awayTeam.getCurrentPopularity());
			TeamUtility.updatePerformanceRating(awayTeam, homeTeam, 0, 0, homeTeam.getCurrentPopularity());
		}
	}

	private void updateWinner(Game game, Team winner, Team loser, int scoreDifference) {
		game.setWinner(winner);
		game.setLoser(loser);
		winner.getTeamPerformance().incrementNumberWin();
		loser.getTeamPerformance().incrementNumberLose();
		TeamUtility.updatePerformanceRating(winner, loser, 1, scoreDifference, loser.getCurrentPopularity());
		TeamUtility.updatePerformanceRating(loser, winner, -1, scoreDifference, winner.getCurrentPopularity());
		TeamUtility.updateStreak(winner, true);
		TeamUtility.updateStreak(loser, false);
	}

	private static int totalHome(Game game) {
		int totalHome = 0;
		for (GameResult gameResult : game.getQuarterResults()) {
			if (gameResult != null) {
				totalHome += gameResult.getScorehomeTeam();
			}
		}
		return totalHome;
	}

	private static int totalAway(Game game) {
		int totalAway = 0;
		for (GameResult gameResult : game.getQuarterResults()) {
			if (gameResult != null) {
				totalAway += gameResult.getScoreAwayTeam();
			}
		}
		return totalAway;
	}
}
