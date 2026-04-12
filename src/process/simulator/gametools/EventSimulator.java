package process.simulator.gametools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import config.GameConfiguration;
import data.player.Asset;
import data.player.Player;
import data.sport.play.OffensiveTry;
import process.utility.PlayerUtility;

public class EventSimulator {

	public EventSimulator() {

	}

	public Player chooseAttackingPlayer(TreeMap<Double, Player> attackingPlayers) {
		double random = Math.random();
		for (Double key : attackingPlayers.keySet()) {
			if (random <= key) {
				return attackingPlayers.get(key);
			}
		}
		return attackingPlayers.lastEntry().getValue();
	}

	public Player chooseDefendingPlayer(TreeMap<Double, Player> defensivePlayers) {
		ArrayList<Player> players = new ArrayList<Player>(defensivePlayers.values());
		int randomIndex = (int) (Math.random() * players.size());
		return players.get(randomIndex);
	}

	public OffensiveTry chooseOffensiveAction(Player attackingPlayer,
			HashMap<Player, Asset> attackPlayersAssetsOfMatch) {
		Asset seasonAsset = attackPlayersAssetsOfMatch.get(attackingPlayer);
		Asset referenceAsset = PlayerUtility.getReferenceOffensiveAsset(attackingPlayer);
		double scoringVolume = smoothRatio(referenceAsset.getPointPerMatch(), 28.0);
		double creationVolume = smoothRatio(referenceAsset.getAssistPerMatch(), 8.0);
		double reboundPresence = smoothRatio(referenceAsset.getReboundPerMatch(), 12.0);
		double efficiency = smoothRatio(referenceAsset.getTrueShootingPercentage(), 0.55);
		double ballSecurityPenalty = smoothRatio(referenceAsset.getLostBallPerMatch(),
				GameConfiguration.MAX_TURNOVER_PER_MATCH);
		double currentRhythm = seasonAsset.getMinutesPlayedPerMatch() > 0
				? smoothRatio(seasonAsset.getPointPerMatch(), Math.max(seasonAsset.getMinutesPlayedPerMatch(), 1.0))
				: smoothRatio(referenceAsset.getPointPerMatch(), Math.max(referenceAsset.getMinutesPlayedPerMatch(), 1.0));

		double threePointWeight = GameConfiguration.THREEPOINT_PROBABILITY
				* (0.85 + scoringVolume * 0.45 + efficiency * 0.55 + currentRhythm * 0.12);
		double twoPointWeight = GameConfiguration.TWOPOINT_PROBABILITY
				* (0.90 + scoringVolume * 0.50 + reboundPresence * 0.20 + currentRhythm * 0.10);
		double foulDrawWeight = GameConfiguration.FOULDRAW_PROBABILITY
				* (0.80 + scoringVolume * 0.35 + creationVolume * 0.18 - ballSecurityPenalty * 0.15);

		switch (attackingPlayer.getPosition()) {
			case GameConfiguration.PLAYER_POSITION_CENTER:
				threePointWeight *= 0.45;
				twoPointWeight *= 1.50;
				foulDrawWeight *= 1.25;
				break;

			case GameConfiguration.PLAYER_POSITION_POINT_GUARD:
				threePointWeight *= 1.25;
				twoPointWeight *= 0.88;
				foulDrawWeight *= 1.12;
				break;

			case GameConfiguration.PLAYER_POSITION_POWER_FORWARD:
				threePointWeight *= 0.78;
				twoPointWeight *= 1.38;
				foulDrawWeight *= 1.18;
				break;

			case GameConfiguration.PLAYER_POSITION_SHOOTING_GUARD:
				threePointWeight *= 1.38;
				twoPointWeight *= 0.95;
				foulDrawWeight *= 1.02;
				break;

			case GameConfiguration.PLAYER_POSITION_SMALL_FORWARD:
				threePointWeight *= 1.08;
				twoPointWeight *= 1.15;
				foulDrawWeight *= 1.10;
				break;
		}
		double total = threePointWeight + foulDrawWeight + twoPointWeight;

		double random = Math.random() * total;
		if (random < threePointWeight) {
			return new OffensiveTry(GameConfiguration.THREEPOINT);
		}
		if (random < foulDrawWeight + threePointWeight) {
			return new OffensiveTry(GameConfiguration.FOULDRAW);
		}
		return new OffensiveTry(GameConfiguration.TWOPOINT);

	}

	private double smoothRatio(double value, double scale) {
		if (scale <= 0) {
			return 0;
		}
		return value / (value + scale);
	}

	public Player chooseAssistPlayer(Player scorerPlayer, HashMap<Player, Asset> attackPlayersAssetsOfMatch) {
		HashMap<Player, Double> assistPlayers = new HashMap<Player, Double>();
		double totalWeight = 0;

		for (Player player : attackPlayersAssetsOfMatch.keySet()) {
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
		double random = Math.random() * totalWeight;
		double cumulative = 0.0;
		Player lastPlayer = null;
		for (Player player : assistPlayers.keySet()) {
			lastPlayer = player;
			cumulative += assistPlayers.get(player) / totalWeight;
			if (random <= cumulative) {
				return player;
			}
		}
		return lastPlayer;
	}

	public Player chooseBlockingPlayer(HashMap<Player, Asset> defensivePlayersAssetsOfMatch) {
		HashMap<Player, Double> blockingPlayers = new HashMap<Player, Double>();
		double total = 0;

		for (Player player : defensivePlayersAssetsOfMatch.keySet()) {
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

			blockProbability += (asset.getBlockPerMatch() / GameConfiguration.MAX_BLOCK_PER_MATCH);
			blockingPlayers.put(player, blockProbability);
			total += blockProbability;
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
		boolean offensiveRebound = Math.random() < GameConfiguration.OFFENSIVE_REBOUND_PROBABILITY;
		if (offensiveRebound) {
			return chooseOffensiveRebounder(attackPlayersAssetsOfMatch);
		}
		return chooseDefensiveRebounder(defensivePlayersAssetsOfMatch);
	}

	private Player chooseOffensiveRebounder(HashMap<Player, Asset> attackPlayersAssetsOfMatch) {
		HashMap<Player, Double> offensiveRebounderPLayers = new HashMap<Player, Double>();
		double total = 0;
		for (Player player : attackPlayersAssetsOfMatch.keySet()) {

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

			reboundProbability += (asset.getReboundPerMatch() / GameConfiguration.MAX_REBOUND_PER_MATCH);
			offensiveRebounderPLayers.put(player, reboundProbability);
			total += reboundProbability;
		}
		double random = Math.random() * total;
		double cumulative = 0;
		Player lastPlayer = null;
		for (Player player : offensiveRebounderPLayers.keySet()) {
			lastPlayer = player;
			cumulative += offensiveRebounderPLayers.get(player);
			if (random <= cumulative) {
				return player;
			}
		}
		return lastPlayer;
	}

	private Player chooseDefensiveRebounder(HashMap<Player, Asset> defensivePlayersAssetsOfMatch) {
		HashMap<Player, Double> defensiveRebounderPLayers = new HashMap<Player, Double>();
		double total = 0;
		for (Player player : defensivePlayersAssetsOfMatch.keySet()) {

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
			reboundProbability += (asset.getReboundPerMatch() / GameConfiguration.MAX_REBOUND_PER_MATCH);
			defensiveRebounderPLayers.put(player, reboundProbability);
			total += reboundProbability;
		}
		double random = Math.random() * total;
		double cumulative = 0;
		Player lastPlayer = null;
		for (Player player : defensiveRebounderPLayers.keySet()) {
			cumulative += defensiveRebounderPLayers.get(player);
			if (random <= cumulative) {
				return player;
			}
		}
		return lastPlayer;
	}

}
