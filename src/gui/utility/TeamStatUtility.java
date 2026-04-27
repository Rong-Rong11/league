package gui.utility;

import java.util.ArrayList;

import data.league.League;
import data.team.Team;
import process.repository.TeamRepository;
import process.utility.TeamMetricsUtility;
import process.utility.TeamNameUtility;

public class TeamStatUtility {

	public static double getAverageNote(Team team) {
		return TeamMetricsUtility.getAverageNote(team);
	}

	public static double getAveragePoints(Team team, boolean currentSeasonSelected) {
		return TeamMetricsUtility.getAveragePoints(team, currentSeasonSelected);
	}

	public static double getAverageRebounds(Team team, boolean currentSeasonSelected) {
		return TeamMetricsUtility.getAverageRebounds(team, currentSeasonSelected);
	}

	public static double getAverageAssists(Team team, boolean currentSeasonSelected) {
		return TeamMetricsUtility.getAverageAssists(team, currentSeasonSelected);
	}

	public static ArrayList<Boolean> getLastResults(Team team, int numberOfGames) {
		return TeamMetricsUtility.getLastResults(team, numberOfGames);
	}

	public static int getBestWinStreak(Team team) {
		return TeamMetricsUtility.getBestWinStreak(team);
	}

	public static int getBestLoseStreak(Team team) {
		return TeamMetricsUtility.getBestLoseStreak(team);
	}

	public static Team findTeamByName(String teamName) {
		return TeamRepository.getInstance().getTeam(teamName);
	}

	public static String getConferenceName(Team team, League league) {
		return TeamNameUtility.getConferenceName(team);
	}

	public static String getDivisionName(Team team, League league) {
		return TeamNameUtility.getDivisionName(team);
	}
}
