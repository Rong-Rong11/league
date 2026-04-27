package process.utility;

import data.player.Asset;
import data.player.HealthStatus;
import data.player.Player;

public class PlayerUtility {
	private static final double CURRENT_SEASON_MINUTES_RELIABLE = 18.0;
	private static final double CURRENT_SEASON_MINUTES_PARTIAL = 8.0;

	public static double getPlayerAttackNote(Player player) {
		Asset asset = PlayerUtility.getWeightedAssets(player);
		double scoringComponent = Math.sqrt(Math.max(0.0, asset.getPointPerMatch() / 10.0));
		double passingComponent = Math.sqrt(Math.max(0.0, asset.getAssistPerMatch() / 3.0));
		double efficiencyComponent = asset.getTrueShootingPercentage();
		double attackNote = scoringComponent * 0.45 + passingComponent * 0.25 + efficiencyComponent * 0.3;
		attackNote = 0.55 + attackNote * 0.35;
		return Math.min(attackNote, 1.0);
	}

	public static double getPlayerDefenseNote(Player player) {
		Asset asset = PlayerUtility.getWeightedAssets(player);
		double stealComponent = Math.sqrt(Math.max(0.0, asset.getInterceptionPerMatch() / 1.0));
		double blockComponent = Math.sqrt(Math.max(0.0, asset.getBlockPerMatch() / 1.0));
		double defenseNote = stealComponent * 0.55 + blockComponent * 0.45;
		defenseNote = 0.5 + defenseNote * 0.3;
		return Math.min(defenseNote, 1.0);
	}

	public static Asset getReferenceOffensiveAsset(Player player) {
		Asset currentAsset = player.getCurrentSeasonAssets();
		Asset previousAsset = player.getPreSeasonAssets();
		double currentMinutes = currentAsset.getMinutesPlayedPerMatch();
		if (currentMinutes <= 0.0) {
			return previousAsset;
		}
		if (currentMinutes >= CURRENT_SEASON_MINUTES_RELIABLE) {
			return currentAsset;
		}
		double currentWeight;
		if (currentMinutes <= CURRENT_SEASON_MINUTES_PARTIAL) {
			currentWeight = 0.25;
		} else {
			currentWeight = 0.25 + ((currentMinutes - CURRENT_SEASON_MINUTES_PARTIAL)
					/ (CURRENT_SEASON_MINUTES_RELIABLE - CURRENT_SEASON_MINUTES_PARTIAL)) * 0.75;
		}
		return blendAssets(currentAsset, previousAsset, currentWeight);
	}

	private static Asset getWeightedAssets(Player player) {
		Asset currentAsset = player.getCurrentSeasonAssets();
		Asset preSeasonAsset = player.getPreSeasonAssets();
		double currentMinutes = currentAsset.getMinutesPlayedPerMatch();
		double previousMinutes = preSeasonAsset.getMinutesPlayedPerMatch();
		double totalMinutes = currentMinutes + previousMinutes;
		if (totalMinutes == 0.0) {
			return preSeasonAsset;
		}
		Asset weightedAsset = new Asset();
		weightedAsset.setPointPerMatch(
				(currentAsset.getPointPerMatch() * currentMinutes + preSeasonAsset.getPointPerMatch() * previousMinutes)
						/ totalMinutes);
		weightedAsset.setAssistPerMatch((currentAsset.getAssistPerMatch() * currentMinutes
				+ preSeasonAsset.getAssistPerMatch() * previousMinutes) / totalMinutes);
		weightedAsset.setInterceptionPerMatch((currentAsset.getInterceptionPerMatch() * currentMinutes
				+ preSeasonAsset.getInterceptionPerMatch() * previousMinutes) / totalMinutes);
		weightedAsset.setBlockPerMatch((currentAsset.getBlockPerMatch() * currentMinutes
				+ preSeasonAsset.getBlockPerMatch() * previousMinutes) / totalMinutes);
		weightedAsset.setTwoPointAttemptPerMatch((currentAsset.getTwoPointAttemptPerMatch() * currentMinutes
				+ preSeasonAsset.getTwoPointAttemptPerMatch() * previousMinutes) / totalMinutes);
		weightedAsset.setThreePointAttemptPerMatch((currentAsset.getThreePointAttemptPerMatch() * currentMinutes
				+ preSeasonAsset.getThreePointAttemptPerMatch() * previousMinutes) / totalMinutes);
		weightedAsset.setFreeThrowAttemptPerMatch((currentAsset.getFreeThrowAttemptPerMatch() * currentMinutes
				+ preSeasonAsset.getFreeThrowAttemptPerMatch() * previousMinutes) / totalMinutes);
		weightedAsset.setTrueShootingPercentage((currentAsset.getTrueShootingPercentage() * currentMinutes
				+ preSeasonAsset.getTrueShootingPercentage() * previousMinutes) / totalMinutes);
		weightedAsset.setMinutesPlayedPerMatch((currentAsset.getMinutesPlayedPerMatch() * currentMinutes
				+ preSeasonAsset.getMinutesPlayedPerMatch() * previousMinutes) / totalMinutes);
		return weightedAsset;
	}

	private static Asset blendAssets(Asset currentAsset, Asset previousAsset, double currentWeight) {
		double boundedCurrentWeight = Math.max(0.0, Math.min(1.0, currentWeight));
		double previousWeight = 1.0 - boundedCurrentWeight;
		Asset blendedAsset = new Asset();
		blendedAsset.setNote(currentAsset.getNote() * boundedCurrentWeight + previousAsset.getNote() * previousWeight);
		blendedAsset.setMinutesPlayedPerMatch(currentAsset.getMinutesPlayedPerMatch() * boundedCurrentWeight
				+ previousAsset.getMinutesPlayedPerMatch() * previousWeight);
		blendedAsset.setPointPerMatch(currentAsset.getPointPerMatch() * boundedCurrentWeight
				+ previousAsset.getPointPerMatch() * previousWeight);
		blendedAsset.setReboundPerMatch(currentAsset.getReboundPerMatch() * boundedCurrentWeight
				+ previousAsset.getReboundPerMatch() * previousWeight);
		blendedAsset.setAssistPerMatch(currentAsset.getAssistPerMatch() * boundedCurrentWeight
				+ previousAsset.getAssistPerMatch() * previousWeight);
		blendedAsset.setInterceptionPerMatch(currentAsset.getInterceptionPerMatch() * boundedCurrentWeight
				+ previousAsset.getInterceptionPerMatch() * previousWeight);
		blendedAsset.setBlockPerMatch(currentAsset.getBlockPerMatch() * boundedCurrentWeight
				+ previousAsset.getBlockPerMatch() * previousWeight);
		blendedAsset.setLostBallPerMatch(currentAsset.getLostBallPerMatch() * boundedCurrentWeight
				+ previousAsset.getLostBallPerMatch() * previousWeight);
		blendedAsset.setTwoPointAttemptPerMatch(currentAsset.getTwoPointAttemptPerMatch() * boundedCurrentWeight
				+ previousAsset.getTwoPointAttemptPerMatch() * previousWeight);
		blendedAsset.setThreePointAttemptPerMatch(currentAsset.getThreePointAttemptPerMatch() * boundedCurrentWeight
				+ previousAsset.getThreePointAttemptPerMatch() * previousWeight);
		blendedAsset.setFreeThrowAttemptPerMatch(currentAsset.getFreeThrowAttemptPerMatch() * boundedCurrentWeight
				+ previousAsset.getFreeThrowAttemptPerMatch() * previousWeight);
		blendedAsset.setTrueShootingPercentage(currentAsset.getTrueShootingPercentage() * boundedCurrentWeight
				+ previousAsset.getTrueShootingPercentage() * previousWeight);
		return blendedAsset;
	}

	public static double getPlayerOverAllNote(Player player) {
		double attackNote = PlayerUtility.getPlayerAttackNote(player);
		double defenseNote = PlayerUtility.getPlayerDefenseNote(player);
		double blendedSeasonNote = attackNote * 0.6 + defenseNote * 0.4;
		double preSeasonNote = player.getPreSeasonAssets().getNote();
		return blendedSeasonNote * 0.7 + preSeasonNote * 0.3;
	}

	public static void updateFatigue(double activityFactor, Player player) {
		HealthStatus healthStatus = player.getHealthStatus();
		double fatigue = healthStatus.getFatigue();
		if ((fatigue += 0.02 * activityFactor) > 1.0) {
			fatigue = 1.0;
		}
		healthStatus.setFatigue(fatigue);
		player.setHealthStatus(healthStatus);
	}

	public static void updateRest(double recoveryFactor, Player player) {
		HealthStatus healthStatus = player.getHealthStatus();
		double fatigue = healthStatus.getFatigue();
		if ((fatigue -= 0.02 * recoveryFactor) < 0.0) {
			fatigue = 0.0;
		}
		healthStatus.setFatigue(fatigue);
		player.setHealthStatus(healthStatus);
	}

	public static void updateAsset(Player player, Asset asset) {
		Asset currentSeasonAsset = player.getCurrentSeasonAssets();
		if (asset.getMinutesPlayedPerMatch() == 0.0) {
			return;
		}
		double existingMinutes = currentSeasonAsset.getMinutesPlayedPerMatch();
		double newMinutes = asset.getMinutesPlayedPerMatch();
		double totalMinutes = existingMinutes + newMinutes;
		currentSeasonAsset.setPointPerMatch(
				(currentSeasonAsset.getPointPerMatch() * existingMinutes + asset.getPointPerMatch() * newMinutes)
						/ totalMinutes);
		currentSeasonAsset.setReboundPerMatch(
				(currentSeasonAsset.getReboundPerMatch() * existingMinutes + asset.getReboundPerMatch() * newMinutes)
						/ totalMinutes);
		currentSeasonAsset.setAssistPerMatch(
				(currentSeasonAsset.getAssistPerMatch() * existingMinutes + asset.getAssistPerMatch() * newMinutes)
						/ totalMinutes);
		currentSeasonAsset.setInterceptionPerMatch((currentSeasonAsset.getInterceptionPerMatch() * existingMinutes
				+ asset.getInterceptionPerMatch() * newMinutes) / totalMinutes);
		currentSeasonAsset.setBlockPerMatch(
				(currentSeasonAsset.getBlockPerMatch() * existingMinutes + asset.getBlockPerMatch() * newMinutes)
						/ totalMinutes);
		currentSeasonAsset.setLostBallPerMatch(
				(currentSeasonAsset.getLostBallPerMatch() * existingMinutes + asset.getLostBallPerMatch() * newMinutes)
						/ totalMinutes);
		currentSeasonAsset.setTwoPointAttemptPerMatch((currentSeasonAsset.getTwoPointAttemptPerMatch() * existingMinutes
				+ asset.getTwoPointAttemptPerMatch() * newMinutes) / totalMinutes);
		currentSeasonAsset.setThreePointAttemptPerMatch(
				(currentSeasonAsset.getThreePointAttemptPerMatch() * existingMinutes
						+ asset.getThreePointAttemptPerMatch() * newMinutes) / totalMinutes);
		currentSeasonAsset.setFreeThrowAttemptPerMatch(
				(currentSeasonAsset.getFreeThrowAttemptPerMatch() * existingMinutes
						+ asset.getFreeThrowAttemptPerMatch() * newMinutes) / totalMinutes);
		currentSeasonAsset.setTrueShootingPercentage((currentSeasonAsset.getTrueShootingPercentage() * existingMinutes
				+ asset.getTrueShootingPercentage() * newMinutes) / totalMinutes);
		currentSeasonAsset.setMinutesPlayedPerMatch(
				(currentSeasonAsset.getMinutesPlayedPerMatch() * existingMinutes + asset.getMinutesPlayedPerMatch() * newMinutes)
						/ totalMinutes);
	}
}
