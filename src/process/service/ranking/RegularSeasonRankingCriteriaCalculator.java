package process.service.ranking;

import java.util.ArrayList;

import data.league.Division;
import data.league.League;
import data.sport.setup.Game;
import data.team.Team;
import process.utility.TeamUtility;

public class RegularSeasonRankingCriteriaCalculator {

	private final ArrayList<Game> simulatedGames;
	private final League league;

	public RegularSeasonRankingCriteriaCalculator(ArrayList<Game> simulatedGames, League league) {
		this.simulatedGames = simulatedGames;
		this.league = league;
	}

	public double getWinRate(Team team) {
		int wins = team.getTeamPerformance().getNumberWin();
		int games = team.getTeamPerformance().getNumberPlayedGames();

		if (games == 0) {
			return 0.0;
		}

		return (double) wins / games;
	}

	public int getHeadToHeadWins(Team teamA, Team teamB) {
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

		return wins;
	}

	public boolean isDivisionChampion(Team team) {
		Division division = TeamUtility.getDivisionOfTeam(league, team);

		if (division == null) {
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

		return true;
	}

	public boolean isSameDivision(Team teamA, Team teamB) {
		Division divisionA = TeamUtility.getDivisionOfTeam(league, teamA);
		Division divisionB = TeamUtility.getDivisionOfTeam(league, teamB);

		if (divisionA == null || divisionB == null) {
			return false;
		}

		return divisionA.equals(divisionB);
	}

	public double getDivisionWinRate(Team team) {
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
			return 0.0;
		}

		return (double) wins / games;
	}

	public double getConferenceWinRate(Team team) {
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
			return 0.0;
		}

		return (double) wins / games;
	}

	public int getPointDifferential(Team team) {
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

		return pointsScored - pointsAllowed;
	}

	private boolean involvesTeam(Game game, Team team) {
		return game.getGameContext().getHomeTeam().equals(team)
				|| game.getGameContext().getAwayTeam().equals(team);
	}

	private Team getOpponent(Game game, Team team) {
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

		return null;
	}

	private boolean isPlayed(Game game) {
		return game.getHomeFinalScore() >= 0 && game.getAwayFinalScore() >= 0;
	}
}
