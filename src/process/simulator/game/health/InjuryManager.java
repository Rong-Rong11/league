package process.simulator.game.health;

import java.util.HashMap;

import org.apache.log4j.Logger;

import config.GameConfiguration;
import config.HealthConfiguration;
import data.player.Asset;
import data.player.Injury;
import data.player.Player;
import log.LoggerUtility;

public class InjuryManager {
	private static final Logger logger = LoggerUtility.getLogger(InjuryManager.class, "text");

	public InjuryManager() {
	}

	public void simulateInjury(HashMap<Player, Asset> playersNewAssets, Player player, String typeAction) {
		if (playersNewAssets == null || player == null || player.getHealthStatus() == null) {
			logger.warn("Skipping injury simulation because player or assets map is null");
			return;
		}

		Asset asset = playersNewAssets.get(player);
		if (asset == null) {
			logger.warn("Skipping injury simulation because player asset is null");
			return;
		}

		double fatigueFactor = 1.0 + player.getHealthStatus().getFatigue();
		double minutesFactor = 1.0 + asset.getMinutesPlayedPerMatch();

		double typeActionFactor;
		switch (typeAction) {
			case GameConfiguration.FOULDRAW:
				typeActionFactor = 1.05;
				break;
			case GameConfiguration.DEFENSIVE_REBOUND_ACTION:
			case GameConfiguration.OFFENSIVE_REBOUND_ACTION:
				typeActionFactor = 1.5;
				break;
			default:
				typeActionFactor = 1.0;
		}

		double injuryProbability = HealthConfiguration.INJURY_PROBABILITY
				* fatigueFactor
				* minutesFactor
				* typeActionFactor;

		if (Math.random() < injuryProbability) {
			injurePlayer(player);
		}
	}

	private static void injurePlayer(Player player) {
		Logger logger = LoggerUtility.getLogger(InjuryManager.class, "text");

		if (player == null || player.getHealthStatus() == null) {
			logger.warn("Skipping player injury because player or health status is null");
			return;
		}

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

		logger.debug("Player injured: " + player.getName()
				+ " | type: " + injury.getInjuryType()
				+ " | duration: " + injury.getInjuryDuration());
	}
}
