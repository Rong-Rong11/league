package process.simulator.gametools;

import config.GameConfiguration;

import java.util.TreeMap;

import data.player.Asset;
import data.player.Player;
import data.sport.play.OffensiveTry;
import process.utility.PlayerUtilitary;

public class ActionSimulator {

	public ActionSimulator() {

	}

	public boolean effectiveTurnover(Player attackingPlayer, Player defendingPlayer) {
		double playerDefenseNote = Math.min(PlayerUtilitary.getPlayerDefenseNote(defendingPlayer), 2);
		double playerAttackNote = Math.min(PlayerUtilitary.getPlayerAttackNote(attackingPlayer), 2);
		double noteGap = Math.max(0, playerDefenseNote - playerAttackNote);
		double turnoverProbability = 0.05 + (noteGap * 0.06);
		turnoverProbability = Math.min(turnoverProbability, 0.12);
		return Math.random() < turnoverProbability;
	}

	public boolean simulateShot(Player attackingPlayer, OffensiveTry action, TreeMap<Double, Player> defensivePlayers) {
		Asset asset = attackingPlayer.getCurrentSeasonAssets().getMinutesPlayedPerMatch() > 0
				? attackingPlayer.getCurrentSeasonAssets()
				: attackingPlayer.getPreSeasonAssets();
		double trueShootingPercentage = asset.getTrueShootingPercentage();
		double shotProbability;
		if (action.getName().equals(GameConfiguration.THREEPOINT)) {
			shotProbability = GameConfiguration.THREEPOINT_PROBABILITY_SUCCESS;
		} else if (action.getName().equals(GameConfiguration.TWOPOINT)) {
			shotProbability = GameConfiguration.TWO_PROBABILITY_SUCCESS;
		} else {
			shotProbability = GameConfiguration.FOULDRAW_PROBABILITY_SUCESS;
		}
		shotProbability += (trueShootingPercentage * 0.30);
		double defenseNote = defensingPlayersNote(defensivePlayers);
		shotProbability -= defenseNote * 0.006;
		shotProbability -= attackingPlayer.getHealthStatus().getFatigue() * 0.05;
		shotProbability = Math.max(0.18, Math.min(0.82, shotProbability));
		return Math.random() < shotProbability;

	}

	private double defensingPlayersNote(TreeMap<Double, Player> defensivePlayers) {
		double sumOfNote = 0;
		double numberOfPlayer = 0;
		double note;
		for (Player player : defensivePlayers.values()) {
			sumOfNote += PlayerUtilitary.getPlayerDefenseNote(player);
			numberOfPlayer++;
		}
		note = sumOfNote / numberOfPlayer;
		return note;
	}
}
