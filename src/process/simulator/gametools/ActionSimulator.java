package process.simulator.gametools;

import java.util.TreeMap;

import config.SimulationConfiguration;
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
		double noteGap = Math.max(0, playerDefenseNote - playerAttackNote);
		double turnoverProbability = 0.08 + (noteGap * 0.18);
		return Math.random() < turnoverProbability;
	}
	
	public boolean simulateShot(Player attackingPlayer, OffensiveTry action,TreeMap<Double, Player> defensivePlayers) {
		Asset asset = attackingPlayer.getCurrentSeasonAssets().getMinutesPlayedPerMatch() > 0
				? attackingPlayer.getCurrentSeasonAssets()
				: attackingPlayer.getPreSeasonAssets();
		double trueShootingPercentage = asset.getTrueShootingPercentage();
		double shotProbability;
		if (action.getName().equals(SimulationConfiguration.THREEPOINT)) {
			shotProbability = SimulationConfiguration.THREEPOINT_PROBABILITY_SUCCESS;
		} else if (action.getName().equals(SimulationConfiguration.TWOPOINT)) {
			shotProbability = SimulationConfiguration.TWO_PROBABILITY_SUCCESS;
		} else {
			shotProbability = SimulationConfiguration.FOULDRAW_PROBABILITY_SUCESS;
		}
		shotProbability += (trueShootingPercentage * 0.35);

		double defenseNote = defensingPlayersNote(defensivePlayers);
		shotProbability -= defenseNote * 0.02;

		shotProbability -= attackingPlayer.getHealthStatus().getFatigue() * 0.15;

		shotProbability = Math.max(0.08, Math.min(0.9, shotProbability));

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
