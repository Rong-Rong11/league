package process.simulator.gametools;

import java.util.ArrayList;
import java.util.HashMap;

import data.player.Asset;
import data.player.HealthStatus;
import data.player.Player;
import data.team.Team;
import process.utility.PlayerUtilitary;

public class HealthManager {

	public HealthManager() {

	}

	public void initializeHealth(Team homeTeam, Team awayTeam) {
		initializeHealthTeam(homeTeam);
		initializeHealthTeam(awayTeam);
	}

	private void initializeHealthTeam(Team team) {
		for (Player player : team.getCurrentPlayers().values()) {
			HealthStatus healthStatus = player.getHealthStatus();
			healthStatus.setFatigue(0);
			healthStatus.getInjury().setInjuryDuration(healthStatus.getInjury().getInjuryDuration() - 1);
			if (healthStatus.getInjury().getInjuryDuration() <= 0) {
				healthStatus.setInjured(false);
			}
		}
	}

	public void updateFatigue(ArrayList<Player> homePlayers, ArrayList<Player> awayPlayers, int actionTimeSeconds) {
		double minutesPlayed = actionTimeSeconds / 60.0;
		for (Player homePlayer : homePlayers) {
			PlayerUtilitary.updateFatigue(minutesPlayed, homePlayer);
		}
		for (Player awayPlayer : awayPlayers) {
			PlayerUtilitary.updateFatigue(minutesPlayed, awayPlayer);
		}
	}

	public void updateRest(double restMinutes, Team homeTeam, Team awayTeam) {
		for (Player player : homeTeam.getCurrentPlayers().values()) {
			PlayerUtilitary.updateRest(restMinutes, player);
		}
		for (Player player : awayTeam.getCurrentPlayers().values()) {
			PlayerUtilitary.updateRest(restMinutes, player);
		}
	}

	public void addMinutesPlayed(ArrayList<Player> homePlayers, ArrayList<Player> awayPlayers,
			HashMap<Player, Asset> playersNewAssets, int actionTime) {
		double playedMinutes = actionTime / 60.0;
		for (Player player : homePlayers) {
			Asset asset = playersNewAssets.get(player);
			asset.setMinutesPlayedPerMatch(asset.getMinutesPlayedPerMatch() + playedMinutes);
		}
		for (Player player : awayPlayers) {
			Asset asset = playersNewAssets.get(player);
			asset.setMinutesPlayedPerMatch(asset.getMinutesPlayedPerMatch() + playedMinutes);
		}

	}

}
