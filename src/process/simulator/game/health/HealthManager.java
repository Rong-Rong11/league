package process.simulator.game.health;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import data.player.Asset;
import data.player.HealthStatus;
import data.player.Player;
import data.team.Team;
import log.LoggerUtility;
import process.utility.PlayerUtility;

public class HealthManager {
	private static final Logger logger = LoggerUtility.getLogger(HealthManager.class, "text");

	public HealthManager() {
	}

	public void initializeHealth(Team homeTeam, Team awayTeam) {
		if (homeTeam == null || awayTeam == null) {
			logger.warn("Skipping health initialization because home team or away team is null");
			return;
		}

		initializeHealthTeam(homeTeam);
		initializeHealthTeam(awayTeam);
	}

	private void initializeHealthTeam(Team team) {
		if (team == null) {
			logger.warn("Skipping team health initialization because team is null");
			return;
		}

		for (Player player : team.getCurrentPlayers().values()) {
			if (player == null || player.getHealthStatus() == null) {
				continue;
			}

			HealthStatus healthStatus = player.getHealthStatus();
			healthStatus.setFatigue(0);
			healthStatus.getInjury().setInjuryDuration(healthStatus.getInjury().getInjuryDuration() - 1);

			if (healthStatus.getInjury().getInjuryDuration() <= 0) {
				healthStatus.setInjured(false);
			}
		}
	}

	public void updateFatigue(ArrayList<Player> homePlayers, ArrayList<Player> awayPlayers, int actionTimeSeconds) {
		if (homePlayers == null || awayPlayers == null) {
			logger.warn("Skipping fatigue update because home players or away players list is null");
			return;
		}

		double minutesPlayed = actionTimeSeconds / 60.0;

		for (Player homePlayer : homePlayers) {
			if (homePlayer == null) {
				continue;
			}
			PlayerUtility.updateFatigue(minutesPlayed, homePlayer);
		}

		for (Player awayPlayer : awayPlayers) {
			if (awayPlayer == null) {
				continue;
			}
			PlayerUtility.updateFatigue(minutesPlayed, awayPlayer);
		}
	}

	public void updateRest(double restMinutes, Team homeTeam, Team awayTeam) {
		if (homeTeam == null || awayTeam == null) {
			logger.warn("Skipping rest update because home team or away team is null");
			return;
		}

		for (Player player : homeTeam.getCurrentPlayers().values()) {
			if (player == null) {
				continue;
			}
			PlayerUtility.updateRest(restMinutes, player);
		}

		for (Player player : awayTeam.getCurrentPlayers().values()) {
			if (player == null) {
				continue;
			}
			PlayerUtility.updateRest(restMinutes, player);
		}
	}

	public void addMinutesPlayed(ArrayList<Player> homePlayers, ArrayList<Player> awayPlayers,
			HashMap<Player, Asset> playersNewAssets, int actionTime) {
		if (homePlayers == null || awayPlayers == null || playersNewAssets == null) {
			logger.warn("Skipping minutes played update because players list or assets map is null");
			return;
		}

		double playedMinutes = actionTime / 60.0;

		for (Player player : homePlayers) {
			if (player == null) {
				continue;
			}

			Asset asset = playersNewAssets.get(player);
			if (asset == null) {
				continue;
			}

			asset.setMinutesPlayedPerMatch(asset.getMinutesPlayedPerMatch() + playedMinutes);
		}

		for (Player player : awayPlayers) {
			if (player == null) {
				continue;
			}

			Asset asset = playersNewAssets.get(player);
			if (asset == null) {
				continue;
			}

			asset.setMinutesPlayedPerMatch(asset.getMinutesPlayedPerMatch() + playedMinutes);
		}
	}
}
