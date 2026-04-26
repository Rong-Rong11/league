package process.simulator.game.event;

import java.util.HashMap;

import org.apache.log4j.Logger;

import config.GameConfiguration;
import data.player.Asset;
import data.player.Player;
import log.LoggerUtility;

public class ShotOutcomePlayerSelector {
	private static final Logger logger = LoggerUtility.getLogger(ShotOutcomePlayerSelector.class, "text");

	public Player chooseAssistPlayer(Player scorerPlayer, HashMap<Player, Asset> attackPlayersAssetsOfMatch) {
		if (scorerPlayer == null || attackPlayersAssetsOfMatch == null || attackPlayersAssetsOfMatch.isEmpty()) {
			logger.warn("Unable to choose assist player because scorer or attacking assets map is null or empty");
			return null;
		}

		HashMap<Player, Double> assistPlayers = new HashMap<Player, Double>();
		double totalWeight = 0;

		for (Player player : attackPlayersAssetsOfMatch.keySet()) {
			if (player == null || attackPlayersAssetsOfMatch.get(player) == null) {
				continue;
			}

			Asset asset = attackPlayersAssetsOfMatch.get(player);
			double assistProbability = GameConfiguration.ASSIST_PROBABILITY;

			if (player.getName().equals(scorerPlayer.getName())) {
				continue;
			}

			switch (player.getPosition()) {
				case GameConfiguration.PLAYER_POSITION_POINT_GUARD:
					assistProbability *= 1.4;
					break;
				case GameConfiguration.PLAYER_POSITION_SHOOTING_GUARD:
					assistProbability *= 1.2;
					break;
				case GameConfiguration.PLAYER_POSITION_SMALL_FORWARD:
					assistProbability *= 1.0;
					break;
				case GameConfiguration.PLAYER_POSITION_POWER_FORWARD:
					assistProbability *= 0.8;
					break;
				case GameConfiguration.PLAYER_POSITION_CENTER:
					assistProbability *= 0.6;
					break;
			}

			assistProbability += asset.getAssistPerMatch() / GameConfiguration.MAX_ASSIST_PER_MATCH;
			assistPlayers.put(player, assistProbability);
			totalWeight += assistProbability;
		}

		if (totalWeight <= 0 || assistPlayers.isEmpty()) {
			logger.warn("Unable to choose assist player because total assist weight is non-positive");
			return null;
		}

		double random = Math.random() * totalWeight;
		double cumulative = 0.0;
		Player lastPlayer = null;

		for (Player player : assistPlayers.keySet()) {
			lastPlayer = player;
			cumulative += assistPlayers.get(player);
			if (random <= cumulative) {
				return player;
			}
		}

		return lastPlayer;
	}

	public Player chooseBlockingPlayer(HashMap<Player, Asset> defensivePlayersAssetsOfMatch) {
		if (defensivePlayersAssetsOfMatch == null || defensivePlayersAssetsOfMatch.isEmpty()) {
			logger.warn("Unable to choose blocking player because defensive assets map is null or empty");
			return null;
		}

		HashMap<Player, Double> blockingPlayers = new HashMap<Player, Double>();
		double total = 0;

		for (Player player : defensivePlayersAssetsOfMatch.keySet()) {
			if (player == null || defensivePlayersAssetsOfMatch.get(player) == null) {
				continue;
			}

			Asset asset = defensivePlayersAssetsOfMatch.get(player);
			double blockProbability = GameConfiguration.BLOCK_PROBABILTY;

			switch (player.getPosition()) {
				case GameConfiguration.PLAYER_POSITION_POINT_GUARD:
					blockProbability *= 0.3;
					break;
				case GameConfiguration.PLAYER_POSITION_SHOOTING_GUARD:
					blockProbability *= 0.5;
					break;
				case GameConfiguration.PLAYER_POSITION_SMALL_FORWARD:
					blockProbability *= 0.7;
					break;
				case GameConfiguration.PLAYER_POSITION_POWER_FORWARD:
					blockProbability *= 1.0;
					break;
				case GameConfiguration.PLAYER_POSITION_CENTER:
					blockProbability *= 1.3;
					break;
			}

			blockProbability += asset.getBlockPerMatch() / GameConfiguration.MAX_BLOCK_PER_MATCH;
			blockingPlayers.put(player, blockProbability);
			total += blockProbability;
		}

		if (total <= 0 || blockingPlayers.isEmpty()) {
			logger.warn("Unable to choose blocking player because total block weight is non-positive");
			return null;
		}

		double random = Math.random() * total;
		double cumulative = 0;
		Player lastPlayer = null;

		for (Player player : blockingPlayers.keySet()) {
			lastPlayer = player;
			cumulative += blockingPlayers.get(player);
			if (random <= cumulative) {
				return player;
			}
		}

		return lastPlayer;
	}

	public Player chooseRebounder(HashMap<Player, Asset> attackPlayersAssetsOfMatch,
			HashMap<Player, Asset> defensivePlayersAssetsOfMatch) {
		if (attackPlayersAssetsOfMatch == null || defensivePlayersAssetsOfMatch == null) {
			logger.warn("Unable to choose rebounder because attacking or defensive assets map is null");
			return null;
		}

		boolean offensiveRebound = Math.random() < GameConfiguration.OFFENSIVE_REBOUND_PROBABILITY;

		if (offensiveRebound) {
			return chooseOffensiveRebounder(attackPlayersAssetsOfMatch);
		}

		return chooseDefensiveRebounder(defensivePlayersAssetsOfMatch);
	}

	private Player chooseOffensiveRebounder(HashMap<Player, Asset> attackPlayersAssetsOfMatch) {
		if (attackPlayersAssetsOfMatch == null || attackPlayersAssetsOfMatch.isEmpty()) {
			logger.warn("Unable to choose offensive rebounder because attacking assets map is null or empty");
			return null;
		}

		HashMap<Player, Double> offensiveRebounderPlayers = new HashMap<Player, Double>();
		double total = 0;

		for (Player player : attackPlayersAssetsOfMatch.keySet()) {
			if (player == null || attackPlayersAssetsOfMatch.get(player) == null) {
				continue;
			}

			Asset asset = attackPlayersAssetsOfMatch.get(player);
			double reboundProbability = GameConfiguration.OFFENSIVE_REBOUND_PROBABILITY;

			switch (player.getPosition()) {
				case GameConfiguration.PLAYER_POSITION_POINT_GUARD:
					reboundProbability *= 0.6;
					break;
				case GameConfiguration.PLAYER_POSITION_SHOOTING_GUARD:
					reboundProbability *= 0.8;
					break;
				case GameConfiguration.PLAYER_POSITION_SMALL_FORWARD:
					reboundProbability *= 1.0;
					break;
				case GameConfiguration.PLAYER_POSITION_POWER_FORWARD:
					reboundProbability *= 1.2;
					break;
				case GameConfiguration.PLAYER_POSITION_CENTER:
					reboundProbability *= 1.4;
					break;
			}

			reboundProbability += asset.getReboundPerMatch() / GameConfiguration.MAX_REBOUND_PER_MATCH;
			offensiveRebounderPlayers.put(player, reboundProbability);
			total += reboundProbability;
		}

		if (total <= 0 || offensiveRebounderPlayers.isEmpty()) {
			logger.warn("Unable to choose offensive rebounder because total rebound weight is non-positive");
			return null;
		}

		double random = Math.random() * total;
		double cumulative = 0;
		Player lastPlayer = null;

		for (Player player : offensiveRebounderPlayers.keySet()) {
			lastPlayer = player;
			cumulative += offensiveRebounderPlayers.get(player);
			if (random <= cumulative) {
				return player;
			}
		}

		return lastPlayer;
	}

	private Player chooseDefensiveRebounder(HashMap<Player, Asset> defensivePlayersAssetsOfMatch) {
		if (defensivePlayersAssetsOfMatch == null || defensivePlayersAssetsOfMatch.isEmpty()) {
			logger.warn("Unable to choose defensive rebounder because defensive assets map is null or empty");
			return null;
		}

		HashMap<Player, Double> defensiveRebounderPlayers = new HashMap<Player, Double>();
		double total = 0;

		for (Player player : defensivePlayersAssetsOfMatch.keySet()) {
			if (player == null || defensivePlayersAssetsOfMatch.get(player) == null) {
				continue;
			}

			Asset asset = defensivePlayersAssetsOfMatch.get(player);
			double reboundProbability = GameConfiguration.DEFENSIVE_REBOUND_PROBABILITY;

			switch (player.getPosition()) {
				case GameConfiguration.PLAYER_POSITION_POINT_GUARD:
					reboundProbability *= 0.6;
					break;
				case GameConfiguration.PLAYER_POSITION_SHOOTING_GUARD:
					reboundProbability *= 0.8;
					break;
				case GameConfiguration.PLAYER_POSITION_SMALL_FORWARD:
					reboundProbability *= 1.0;
					break;
				case GameConfiguration.PLAYER_POSITION_POWER_FORWARD:
					reboundProbability *= 1.2;
					break;
				case GameConfiguration.PLAYER_POSITION_CENTER:
					reboundProbability *= 1.4;
					break;
			}

			reboundProbability += asset.getReboundPerMatch() / GameConfiguration.MAX_REBOUND_PER_MATCH;
			defensiveRebounderPlayers.put(player, reboundProbability);
			total += reboundProbability;
		}

		if (total <= 0 || defensiveRebounderPlayers.isEmpty()) {
			logger.warn("Unable to choose defensive rebounder because total rebound weight is non-positive");
			return null;
		}

		double random = Math.random() * total;
		double cumulative = 0;
		Player lastPlayer = null;

		for (Player player : defensiveRebounderPlayers.keySet()) {
			lastPlayer = player;
			cumulative += defensiveRebounderPlayers.get(player);
			if (random <= cumulative) {
				return player;
			}
		}

		return lastPlayer;
	}
}
