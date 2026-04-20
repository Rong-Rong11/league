/*
	* Decompiled with CFR 0.152.
	*/
package process.utility;

import data.player.Asset;
import data.player.HealthStatus;
import data.player.Player;

public class PlayerUtility {
	private static final double CURRENT_SEASON_MINUTES_RELIABLE = 18.0;
	private static final double CURRENT_SEASON_MINUTES_PARTIAL = 8.0;

	public static double getPlayerAttackNote(Player player) {
		Asset asset = PlayerUtility.getWeightedAssets(player);
		double d = Math.sqrt(Math.max(0.0, asset.getPointPerMatch() / 10.0));
		double d2 = Math.sqrt(Math.max(0.0, asset.getAssistPerMatch() / 3.0));
		double d3 = asset.getTrueShootingPercentage();
		double d4 = d * 0.45 + d2 * 0.25 + d3 * 0.3;
		d4 = 0.55 + d4 * 0.35;
		return Math.min(d4, 1.0);
	}

	public static double getPlayerDefenseNote(Player player) {
		Asset asset = PlayerUtility.getWeightedAssets(player);
		double d = Math.sqrt(Math.max(0.0, asset.getInterceptionPerMatch() / 1.0));
		double d2 = Math.sqrt(Math.max(0.0, asset.getBlockPerMatch() / 1.0));
		double d3 = d * 0.55 + d2 * 0.45;
		d3 = 0.5 + d3 * 0.3;
		return Math.min(d3, 1.0);
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
		double d;
		Asset asset = player.getCurrentSeasonAssets();
		Asset asset2 = player.getPreSeasonAssets();
		double d2 = asset.getMinutesPlayedPerMatch();
		double d3 = d2 + (d = asset2.getMinutesPlayedPerMatch());
		if (d3 == 0.0) {
			return asset2;
		}
		Asset asset3 = new Asset();
		asset3.setPointPerMatch((asset.getPointPerMatch() * d2 + asset2.getPointPerMatch() * d) / d3);
		asset3.setAssistPerMatch((asset.getAssistPerMatch() * d2 + asset2.getAssistPerMatch() * d) / d3);
		asset3.setInterceptionPerMatch(
				(asset.getInterceptionPerMatch() * d2 + asset2.getInterceptionPerMatch() * d) / d3);
		asset3.setBlockPerMatch((asset.getBlockPerMatch() * d2 + asset2.getBlockPerMatch() * d) / d3);
		asset3.setTwoPointAttemptPerMatch(
				(asset.getTwoPointAttemptPerMatch() * d2 + asset2.getTwoPointAttemptPerMatch() * d) / d3);
		asset3.setThreePointAttemptPerMatch(
				(asset.getThreePointAttemptPerMatch() * d2 + asset2.getThreePointAttemptPerMatch() * d) / d3);
		asset3.setFreeThrowAttemptPerMatch(
				(asset.getFreeThrowAttemptPerMatch() * d2 + asset2.getFreeThrowAttemptPerMatch() * d) / d3);
		asset3.setTrueShootingPercentage(
				(asset.getTrueShootingPercentage() * d2 + asset2.getTrueShootingPercentage() * d) / d3);
		asset3.setMinutesPlayedPerMatch(
				(asset.getMinutesPlayedPerMatch() * d2 + asset2.getMinutesPlayedPerMatch() * d) / d3);
		return asset3;
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
		double d = PlayerUtility.getPlayerAttackNote(player);
		double d2 = PlayerUtility.getPlayerDefenseNote(player);
		double d3 = d * 0.6 + d2 * 0.4;
		double d4 = player.getPreSeasonAssets().getNote();
		return d3 * 0.7 + d4 * 0.3;
	}

	public static void updateFatigue(double d, Player player) {
		HealthStatus healthStatus = player.getHealthStatus();
		double d2 = healthStatus.getFatigue();
		if ((d2 += 0.02 * d) > 1.0) {
			d2 = 1.0;
		}
		healthStatus.setFatigue(d2);
		player.setHealthStatus(healthStatus);
	}

	public static void updateRest(double d, Player player) {
		HealthStatus healthStatus = player.getHealthStatus();
		double d2 = healthStatus.getFatigue();
		if ((d2 -= 0.02 * d) < 0.0) {
			d2 = 0.0;
		}
		healthStatus.setFatigue(d2);
		player.setHealthStatus(healthStatus);
	}

	public static void updateAsset(Player player, Asset asset) {
		Asset asset2 = player.getCurrentSeasonAssets();
		if (asset.getMinutesPlayedPerMatch() == 0.0) {
			return;
		}
		double d = asset2.getMinutesPlayedPerMatch();
		double d2 = asset.getMinutesPlayedPerMatch();
		double d3 = d + d2;
		asset2.setPointPerMatch((asset2.getPointPerMatch() * d + asset.getPointPerMatch() * d2) / d3);
		asset2.setReboundPerMatch((asset2.getReboundPerMatch() * d + asset.getReboundPerMatch() * d2) / d3);
		asset2.setAssistPerMatch((asset2.getAssistPerMatch() * d + asset.getAssistPerMatch() * d2) / d3);
		asset2.setInterceptionPerMatch(
				(asset2.getInterceptionPerMatch() * d + asset.getInterceptionPerMatch() * d2) / d3);
		asset2.setBlockPerMatch((asset2.getBlockPerMatch() * d + asset.getBlockPerMatch() * d2) / d3);
		asset2.setLostBallPerMatch((asset2.getLostBallPerMatch() * d + asset.getLostBallPerMatch() * d2) / d3);
		asset2.setTwoPointAttemptPerMatch(
				(asset2.getTwoPointAttemptPerMatch() * d + asset.getTwoPointAttemptPerMatch() * d2) / d3);
		asset2.setThreePointAttemptPerMatch(
				(asset2.getThreePointAttemptPerMatch() * d + asset.getThreePointAttemptPerMatch() * d2) / d3);
		asset2.setFreeThrowAttemptPerMatch(
				(asset2.getFreeThrowAttemptPerMatch() * d + asset.getFreeThrowAttemptPerMatch() * d2) / d3);
		asset2.setTrueShootingPercentage(
				(asset2.getTrueShootingPercentage() * d + asset.getTrueShootingPercentage() * d2) / d3);
		asset2.setMinutesPlayedPerMatch(
				(asset2.getMinutesPlayedPerMatch() * d + asset.getMinutesPlayedPerMatch() * d2) / d3);
	}
}
