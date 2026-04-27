package process.simulator.game.action;

import java.util.TreeMap;

import org.apache.log4j.Logger;

import config.GameConfiguration;
import data.player.Asset;
import data.player.Player;
import data.sport.play.OffensiveTry;
import log.LoggerUtility;
import process.utility.PlayerUtility;

public class ActionResolutionCalculator {
	private static final Logger logger = LoggerUtility.getLogger(ActionResolutionCalculator.class, "text");

	public ActionResolutionCalculator() {
	}

	public boolean isTurnover(Player attackingPlayer, Player defendingPlayer) {
		if (attackingPlayer == null || defendingPlayer == null) {
			logger.warn("Returning false turnover because attacking or defending player is null");
			return false;
		}

		double playerDefenseNote = Math.min(PlayerUtility.getPlayerDefenseNote(defendingPlayer), 2);
		double playerAttackNote = Math.min(PlayerUtility.getPlayerAttackNote(attackingPlayer), 2);

		double noteGap = Math.max(0, playerDefenseNote - playerAttackNote);
		double turnoverProbability = 0.05 + (noteGap * 0.06);
		turnoverProbability = Math.min(turnoverProbability, 0.12);

		return Math.random() < turnoverProbability;
	}

	public boolean isShotMade(Player attackingPlayer, OffensiveTry action, TreeMap<Double, Player> defensivePlayers) {
		if (attackingPlayer == null || action == null || defensivePlayers == null) {
			logger.warn("Returning false shot result because attacking player, action or defenders are null");
			return false;
		}

		Asset asset = attackingPlayer.getCurrentSeasonAssets().getMinutesPlayedPerMatch() > 0
				? attackingPlayer.getCurrentSeasonAssets()
				: attackingPlayer.getPreSeasonAssets();

		if (asset == null) {
			logger.warn("Returning false shot result because player asset is null");
			return false;
		}

		double trueShootingPercentage = asset.getTrueShootingPercentage();
		double shotProbability;

		if (action.getName().equals(GameConfiguration.THREEPOINT)) {
			shotProbability = GameConfiguration.THREEPOINT_PROBABILITY_SUCCESS;
		} else if (action.getName().equals(GameConfiguration.TWOPOINT)) {
			shotProbability = GameConfiguration.TWO_PROBABILITY_SUCCESS;
		} else {
			shotProbability = GameConfiguration.FOULDRAW_PROBABILITY_SUCESS;
		}

		shotProbability += (trueShootingPercentage * 0.15);

		double defenseNote = calculateDefensiveRating(defensivePlayers);
		shotProbability -= defenseNote * 0.006;
		shotProbability -= attackingPlayer.getHealthStatus().getFatigue() * 0.05;

		shotProbability = Math.max(0.18, Math.min(0.82, shotProbability));

		return Math.random() < shotProbability;
	}

	private double calculateDefensiveRating(TreeMap<Double, Player> defensivePlayers) {
		if (defensivePlayers.isEmpty()) {
			logger.warn("Returning 0 defensive rating because defensive players list is empty");
			return 0;
		}

		double sumOfNote = 0;
		double numberOfPlayer = 0;

		for (Player player : defensivePlayers.values()) {
			if (player == null) {
				continue;
			}
			sumOfNote += PlayerUtility.getPlayerDefenseNote(player);
			numberOfPlayer++;
		}

		if (numberOfPlayer == 0) {
			logger.warn("Returning 0 defensive rating because no valid players were found");
			return 0;
		}

		return sumOfNote / numberOfPlayer;
	}
}
