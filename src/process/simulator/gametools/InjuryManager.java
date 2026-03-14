package process.simulator.gametools;

import java.util.HashMap;

import config.GameConfiguration;
import config.HealthConfiguration;
import data.player.Asset;
import data.player.Injury;
import data.player.Player;

public class InjuryManager {
	
	public InjuryManager() {
		
	}
	
	public void simulateInjury(HashMap<Player, Asset> playersNewAssets,Player player, String typeAction) {
		Asset asset = playersNewAssets.get(player);
		double fatigueFactor = 1.0 + player.getHealthStatus().getFatigue();
		double minutesFactor = 1.0 + asset.getMinutesPlayedPerMatch();
		double typeActionFactor;
		switch (typeAction) {
			case GameConfiguration.FOULDRAW:
				typeActionFactor = 1.05;
				break;
			case GameConfiguration.DEFENSIVE_REBOUND_ACTION:
				typeActionFactor = 1.5;
				break;
			case GameConfiguration.OFFENSIVE_REBOUND_ACTION:
				typeActionFactor = 1.5;
				break;
			default:
				typeActionFactor = 1.0;
		}
		double injuryProbability = HealthConfiguration.INJURY_PROBABILITY * fatigueFactor * minutesFactor
				* typeActionFactor;
		if (Math.random() < injuryProbability) {
			injurePlayer(player);
		}
	}
	
	private static void injurePlayer(Player player) {
		double random = Math.random();
		Injury injury;
		if (random < 0.7) {
			injury = new Injury(HealthConfiguration.MINOR_INJURY, HealthConfiguration.MINOR_INJURY_DURATION);
		} else if (random < 0.90) {
			injury = new Injury(HealthConfiguration.MEDIUM_INJURY, HealthConfiguration.MEDIUM_INJURY_DURATION);
		} else {
			injury = new Injury(HealthConfiguration.SERIOUS_INJURY, HealthConfiguration.SERIOUS_INJURY_DURATION);
		}
		player.getHealthStatus().setInjured(true);
		player.getHealthStatus().setInjury(injury);
	}
	
	
}
