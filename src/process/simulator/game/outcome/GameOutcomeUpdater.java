package process.simulator.game.outcome;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import config.GameConfiguration;
import data.player.Player;
import data.sport.play.action.ActionResult;
import data.sport.setup.Game;
import data.sport.setup.GameResult;
import data.team.Team;
import data.team.TeamPerformance;
import log.LoggerUtility;
import process.utility.TeamUtility;
import process.visitor.actionresult.GameResultVisitor;

public class GameOutcomeUpdater {
	private static final Logger logger = LoggerUtility.getLogger(GameOutcomeUpdater.class, "text");

	public void recordAction(GameResult gameResult, ActionResult actionResult, ArrayList<Player> homeTeamPlayers,
			ArrayList<Player> awayTeamPlayers) {
		if (gameResult == null || actionResult == null || homeTeamPlayers == null || awayTeamPlayers == null) {
			logger.warn("Skipping action record because game result, action result or players list is null");
			return;
		}

		GameResultVisitor visitor = new GameResultVisitor(gameResult, homeTeamPlayers, awayTeamPlayers);
		actionResult.accept(visitor);
		gameResult.addActions(actionResult);
	}

	public boolean shouldChangePossession(ActionResult terminalAction) {
		if (terminalAction == null) {
			logger.warn("Returning false possession change because terminal action is null");
			return false;
		}

		return terminalAction.getName().equals(GameConfiguration.SCORED_ACTION)
				|| terminalAction.getName().equals(GameConfiguration.DEFENSIVE_REBOUND_ACTION)
				|| terminalAction.getName().equals(GameConfiguration.TURNOVER_ACTION);
	}

	public void updateFinalOutcome(Game game) {
		if (game == null || game.getGameContext() == null) {
			logger.warn("Skipping final outcome update because game or game context is null");
			return;
		}

		Team homeTeam = game.getGameContext().getHomeTeam();
		Team awayTeam = game.getGameContext().getAwayTeam();

		if (homeTeam == null || awayTeam == null) {
			logger.warn("Skipping final outcome update because home team or away team is null");
			return;
		}

		int totalHome = totalHome(game);
		int totalAway = totalAway(game);

		game.setHomeFinalScore(totalHome);
		game.setAwayFinalScore(totalAway);

		TeamPerformance homePerformance = homeTeam.getTeamPerformance();
		TeamPerformance awayPerformance = awayTeam.getTeamPerformance();

		if (homePerformance == null || awayPerformance == null) {
			logger.warn("Skipping performance update because home or away performance is null");
			return;
		}

		homePerformance.incrementNmberPlayedGames();
		awayPerformance.incrementNmberPlayedGames();

		if (totalHome > totalAway) {
			updateWinner(game, homeTeam, awayTeam, totalHome - totalAway);
		} else if (totalAway > totalHome) {
			updateWinner(game, awayTeam, homeTeam, totalAway - totalHome);
		} else {
			logger.debug("Game ended in a tie between " + homeTeam.getName() + " and " + awayTeam.getName());

			TeamUtility.updatePerformanceRating(homeTeam, awayTeam, 0, 0, awayTeam.getCurrentPopularity());
			TeamUtility.updatePerformanceRating(awayTeam, homeTeam, 0, 0, homeTeam.getCurrentPopularity());
		}

		logger.debug("Final outcome updated: " + homeTeam.getName() + " " + totalHome
				+ " - " + totalAway + " " + awayTeam.getName());
	}

	private void updateWinner(Game game, Team winner, Team loser, int scoreDifference) {
		if (game == null || winner == null || loser == null) {
			logger.warn("Skipping winner update because game, winner or loser is null");
			return;
		}

		game.setWinner(winner);
		game.setLoser(loser);

		winner.getTeamPerformance().incrementNumberWin();
		loser.getTeamPerformance().incrementNumberLose();

		TeamUtility.updatePerformanceRating(winner, loser, 1, scoreDifference, loser.getCurrentPopularity());
		TeamUtility.updatePerformanceRating(loser, winner, -1, scoreDifference, winner.getCurrentPopularity());
		TeamUtility.updateStreak(winner, true);
		TeamUtility.updateStreak(loser, false);

		logger.debug("Winner updated: " + winner.getName() + " defeated " + loser.getName()
				+ " by " + scoreDifference + " points");
	}

	private static int totalHome(Game game) {
		if (game == null || game.getQuarterResults() == null) {
			logger.warn("Returning 0 home total because game or quarter results are null");
			return 0;
		}

		int totalHome = 0;

		for (GameResult gameResult : game.getQuarterResults()) {
			if (gameResult != null) {
				totalHome += gameResult.getScorehomeTeam();
			}
		}

		return totalHome;
	}

	private static int totalAway(Game game) {
		if (game == null || game.getQuarterResults() == null) {
			logger.warn("Returning 0 away total because game or quarter results are null");
			return 0;
		}

		int totalAway = 0;

		for (GameResult gameResult : game.getQuarterResults()) {
			if (gameResult != null) {
				totalAway += gameResult.getScoreAwayTeam();
			}
		}

		return totalAway;
	}
}
