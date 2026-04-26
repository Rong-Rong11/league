package process.service.ranking;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import data.league.Division;
import data.league.League;
import data.sport.setup.Game;
import data.team.Team;
import log.LoggerUtility;
import process.utility.TeamUtility;

public class RegularSeasonRankingCriteriaCalculator {
	private static final Logger logger = LoggerUtility.getLogger(RegularSeasonRankingCriteriaCalculator.class, "text");

	private final ArrayList<Game> simulatedGames;
	private final League league;

	public RegularSeasonRankingCriteriaCalculator(ArrayList<Game> simulatedGames, League league) {
		this.simulatedGames = simulatedGames;
		this.league = league;

		if (simulatedGames == null) {
			logger.warn("Regular season ranking criteria calculator initialized with null simulated games list");
		}
		if (league == null) {
			logger.warn("Regular season ranking criteria calculator initialized with null league");
		}
	}

	public double getWinRate(Team team) {
		if (team == null || team.getTeamPerformance() == null) {
			logger.warn("Returning 0 win rate because team or team performance is null");
			return 0.0;
		}

		int wins = team.getTeamPerformance().getNumberWin();
		int games = team.getTeamPerformance().getNumberPlayedGames();

		if (games == 0) {
			logger.trace("Returning 0 win rate for " + team.getName() + " because no games were played");
			return 0.0;
		}

		return (double) wins / games;
	}

	public int getHeadToHeadWins(Team teamA, Team teamB) {
		if (teamA == null || teamB == null || simulatedGames == null) {
			logger.warn("Returning 0 head-to-head wins because team or simulated games list is null");
			return 0;
		}

		int wins = 0;

		for (Game game : simulatedGames) {
			if (!isPlayed(game)) {
				continue;
			}

			boolean sameMatchup = (game.getGameContext().getHomeTeam().equals(teamA)
					&& game.getGameContext().getAwayTeam().equals(teamB))
					|| (game.getGameContext().getHomeTeam().equals(teamB)
							&& game.getGameContext().getAwayTeam().equals(teamA));

			if (!sameMatchup) {
				continue;
			}

			Team winner = getWinner(game);
			if (teamA.equals(winner)) {
				wins++;
			}
		}

		logger.trace("Calculated head-to-head wins for " + teamA.getName() + " against " + teamB.getName() + ": "
				+ wins);
		return wins;
	}

	public boolean isDivisionChampion(Team team) {
		if (team == null || league == null) {
			logger.warn("Returning false for division champion because team or league is null");
			return false;
		}

		Division division = TeamUtility.getDivisionOfTeam(league, team);

		if (division == null) {
			logger.warn("Returning false for division champion because division was not found for " + team.getName());
			return false;
		}

		for (Team otherTeam : division.getTeams().values()) {
			if (otherTeam.equals(team)) {
				continue;
			}

			int comparison = Double.compare(getWinRate(otherTeam), getWinRate(team));
			if (comparison > 0) {
				return false;
			}

			if (comparison == 0) {
				int headToHeadComparison = Integer.compare(
						getHeadToHeadWins(otherTeam, team),
						getHeadToHeadWins(team, otherTeam));

				if (headToHeadComparison > 0) {
					return false;
				}
			}
		}

		logger.trace(team.getName() + " is division champion");
		return true;
	}

	public boolean isSameDivision(Team teamA, Team teamB) {
		if (teamA == null || teamB == null || league == null) {
			logger.warn("Returning false for same division because team or league is null");
			return false;
		}

		Division divisionA = TeamUtility.getDivisionOfTeam(league, teamA);
		Division divisionB = TeamUtility.getDivisionOfTeam(league, teamB);

		if (divisionA == null || divisionB == null) {
			logger.trace("Returning false for same division because one division was not found");
			return false;
		}

		return divisionA.equals(divisionB);
	}

	public double getDivisionWinRate(Team team) {
		if (team == null || simulatedGames == null) {
			logger.warn("Returning 0 division win rate because team or simulated games list is null");
			return 0.0;
		}

		int wins = 0;
		int games = 0;

		for (Game game : simulatedGames) {
			if (!isPlayed(game)) {
				continue;
			}

			if (!involvesTeam(game, team)) {
				continue;
			}

			Team opponent = getOpponent(game, team);
			if (opponent == null || !isSameDivision(team, opponent)) {
				continue;
			}

			games++;

			Team winner = getWinner(game);
			if (team.equals(winner)) {
				wins++;
			}
		}

		if (games == 0) {
			logger.trace("Returning 0 division win rate for " + team.getName() + " because no division games were played");
			return 0.0;
		}

		return (double) wins / games;
	}

	public double getConferenceWinRate(Team team) {
		if (team == null || simulatedGames == null) {
			logger.warn("Returning 0 conference win rate because team or simulated games list is null");
			return 0.0;
		}

		int wins = 0;
		int games = 0;

		for (Game game : simulatedGames) {
			if (!isPlayed(game)) {
				continue;
			}

			if (!involvesTeam(game, team)) {
				continue;
			}

			Team opponent = getOpponent(game, team);
			if (opponent == null) {
				continue;
			}

			games++;

			Team winner = getWinner(game);
			if (team.equals(winner)) {
				wins++;
			}
		}

		if (games == 0) {
			logger.trace(
					"Returning 0 conference win rate for " + team.getName() + " because no conference games were played");
			return 0.0;
		}

		return (double) wins / games;
	}

	public int getPointDifferential(Team team) {
		if (team == null || simulatedGames == null) {
			logger.warn("Returning 0 point differential because team or simulated games list is null");
			return 0;
		}

		int pointsScored = 0;
		int pointsAllowed = 0;

		for (Game game : simulatedGames) {
			if (!isPlayed(game)) {
				continue;
			}

			if (game.getGameContext().getHomeTeam().equals(team)) {
				pointsScored += game.getHomeFinalScore();
				pointsAllowed += game.getAwayFinalScore();
			} else if (game.getGameContext().getAwayTeam().equals(team)) {
				pointsScored += game.getAwayFinalScore();
				pointsAllowed += game.getHomeFinalScore();
			}
		}

		int pointDifferential = pointsScored - pointsAllowed;
		logger.trace("Calculated point differential for " + team.getName() + ": " + pointDifferential);
		return pointDifferential;
	}

	private boolean involvesTeam(Game game, Team team) {
		if (game == null || game.getGameContext() == null || team == null) {
			logger.warn("Returning false for involves team because game, game context or team is null");
			return false;
		}

		return game.getGameContext().getHomeTeam().equals(team)
				|| game.getGameContext().getAwayTeam().equals(team);
	}

	private Team getOpponent(Game game, Team team) {
		if (game == null || game.getGameContext() == null || team == null) {
			logger.warn("Returning null opponent because game, game context or team is null");
			return null;
		}

		if (game.getGameContext().getHomeTeam().equals(team)) {
			return game.getGameContext().getAwayTeam();
		}

		if (game.getGameContext().getAwayTeam().equals(team)) {
			return game.getGameContext().getHomeTeam();
		}

		return null;
	}

	private Team getWinner(Game game) {
		if (!isPlayed(game)) {
			return null;
		}

		if (game.getHomeFinalScore() > game.getAwayFinalScore()) {
			return game.getGameContext().getHomeTeam();
		}

		if (game.getAwayFinalScore() > game.getHomeFinalScore()) {
			return game.getGameContext().getAwayTeam();
		}

		logger.trace("No winner found because game ended in a tie");
		return null;
	}

	private boolean isPlayed(Game game) {
		if (game == null) {
			logger.warn("Returning false for played game because game is null");
			return false;
		}

		return game.getHomeFinalScore() >= 0 && game.getAwayFinalScore() >= 0;
	}
}
