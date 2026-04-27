package process.utility;

import java.util.ArrayList;

import data.player.Asset;
import data.player.Player;
import data.sport.setup.Game;
import data.team.Team;

public final class TeamMetricsUtility {

	private TeamMetricsUtility() {
	}

	public static double getAverageNote(Team team) {
		double total = 0.0;
		int count = 0;
		for (Player player : team.getCurrentPlayers().values()) {
			total += getDisplayedNote(player);
			count++;
		}
		if (count == 0) {
			return 0.0;
		}
		return total / count;
	}

	public static double getAveragePoints(Team team, boolean currentSeasonSelected) {
		double total = 0.0;
		int count = 0;
		for (Player player : team.getCurrentPlayers().values()) {
			total += getDisplayedAssets(player, currentSeasonSelected).getPointPerMatch();
			count++;
		}
		if (count == 0) {
			return 0.0;
		}
		return total / count;
	}

	public static double getAverageRebounds(Team team, boolean currentSeasonSelected) {
		double total = 0.0;
		int count = 0;
		for (Player player : team.getCurrentPlayers().values()) {
			total += getDisplayedAssets(player, currentSeasonSelected).getReboundPerMatch();
			count++;
		}
		if (count == 0) {
			return 0.0;
		}
		return total / count;
	}

	public static double getAverageAssists(Team team, boolean currentSeasonSelected) {
		double total = 0.0;
		int count = 0;
		for (Player player : team.getCurrentPlayers().values()) {
			total += getDisplayedAssets(player, currentSeasonSelected).getAssistPerMatch();
			count++;
		}
		if (count == 0) {
			return 0.0;
		}
		return total / count;
	}

	public static ArrayList<Boolean> getLastResults(Team team, int numberOfGames) {
		ArrayList<Boolean> results = new ArrayList<Boolean>();
		if (team == null || team.getSchedule() == null || numberOfGames <= 0) {
			return results;
		}
		for (Game game : team.getSchedule().getScheduledGames().descendingMap().values()) {
			if (game.getWinner() == null) {
				continue;
			}
			results.add(game.getWinner().equals(team));
			if (results.size() == numberOfGames) {
				break;
			}
		}
		return results;
	}

	public static int getBestWinStreak(Team team) {
		if (team == null || team.getTeamPerformance() == null) {
			return 0;
		}
		return Math.max(team.getTeamPerformance().getCurrentWinStreak(), team.getTeamPerformance().getMaxWinsStreak());
	}

	public static int getBestLoseStreak(Team team) {
		if (team == null || team.getTeamPerformance() == null) {
			return 0;
		}
		return Math.max(team.getTeamPerformance().getCurrentLoseStreak(), team.getTeamPerformance().getMaxLoseStreak());
	}

	private static Asset getDisplayedAssets(Player player, boolean currentSeasonSelected) {
		if (!currentSeasonSelected) {
			return player.getPreSeasonAssets();
		}
		if (player.getCurrentSeasonAssets().getMinutesPlayedPerMatch() > 0) {
			return player.getCurrentSeasonAssets();
		}
		return player.getPreSeasonAssets();
	}

	private static double getDisplayedNote(Player player) {
		if (player.getCurrentSeasonAssets().getNote() > 0) {
			return player.getCurrentSeasonAssets().getNote();
		}
		return player.getPreSeasonAssets().getNote();
	}
}
