package process.simulator.game.event;

import java.util.HashMap;

import config.GameConfiguration;
import data.player.Asset;
import data.player.Player;
import data.sport.play.OffensiveTry;
import process.utility.PlayerUtility;

public class OffensiveActionSelector {

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
}
