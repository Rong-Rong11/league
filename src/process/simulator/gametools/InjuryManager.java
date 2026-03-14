package process.simulator.gametools;

import java.util.HashMap;

import config.SimulationConfiguration;
import data.player.Asset;
import data.player.Injury;
import data.player.Player;

public class InjuryManager {

	public InjuryManager() {

	}

	public void simulateInjury(HashMap<Player, Asset> playersNewAssets, Player player, String typeAction) {
		Asset asset = playersNewAssets.get(player);
		double fatigueFactor = 1.0 + player.getHealthStatus().getFatigue();
		double minutesFactor = 1.0 + asset.getMinutesPlayedPerMatch();
		double typeActionFactor;
		switch (typeAction) {
			case SimulationConfiguration.FOULDRAW:
				typeActionFactor = 1.05;
				break;
			case SimulationConfiguration.DEFENSIVE_REBOUND_ACTION:
				typeActionFactor = 1.5;
				break;
			case SimulationConfiguration.OFFENSIVE_REBOUND_ACTION:
				typeActionFactor = 1.5;
				break;
			default:
				typeActionFactor = 1.0;
		}
		double injuryProbability = SimulationConfiguration.INJURY_PROBABILITY * fatigueFactor * minutesFactor
				* typeActionFactor;
		if (Math.random() < injuryProbability) {
			injurePlayer(player);
		}
	}

	private static void injurePlayer(Player player) {
		double random = Math.random();
		Injury injury;
		if (random < 0.7) {
			injury = new Injury(SimulationConfiguration.MINOR_INJURY, SimulationConfiguration.MINOR_INJURY_DURATION);
		} else if (random < 0.90) {
			injury = new Injury(SimulationConfiguration.MEDIUM_INJURY, SimulationConfiguration.MEDIUM_INJURY_DURATION);
		} else {
			injury = new Injury(SimulationConfiguration.SERIOUS_INJURY, SimulationConfiguration.SERIOUS_INJURY_DURATION);
		}
		player.getHealthStatus().setInjured(true);
		player.getHealthStatus().setInjury(injury);
	}

}
