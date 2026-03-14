package process.simulator.gametools;

import java.util.TreeMap;

import config.GameConfiguration;
import data.player.Asset;
import data.player.Player;
import data.sport.play.OffensiveTry;
import process.utilitary.PlayerUtilitary;

public class ActionSimulator {
	
	public ActionSimulator() {
		
	}
	
	public boolean effectiveTurnover(Player attackingPlayer, Player defendingPlayer) {
		double playerDefenseNote = Math.min(PlayerUtilitary.getPlayerDefenseNote(defendingPlayer), 2);
		double playerAttackNote = Math.min(PlayerUtilitary.getPlayerAttackNote(attackingPlayer), 2);

		if (playerDefenseNote > playerAttackNote) {
			if (Math.random() < 0.7) {
				return true;
			}
		}
		return false;
	}
	
	public boolean simulateShot(Player attackingPlayer, OffensiveTry action,TreeMap<Double, Player> defensivePlayers) {
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
		shotProbability += (trueShootingPercentage * 0.5);

		double defenseNote = defensingPlayersNote(defensivePlayers);
		shotProbability -= defenseNote * 0.05;

		shotProbability -= attackingPlayer.getHealthStatus().getFatigue();

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
