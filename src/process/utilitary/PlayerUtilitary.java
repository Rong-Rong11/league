package process.utilitary;

import config.SimulationConfiguration;
import data.player.Asset;
import data.player.HealthStatus;
import data.player.Player;

public class PlayerUtilitary {

	public static double getPlayerAttackNote(Player player) {
		double scoringRatio;
		double assistRatio;
		double efficiency;
		double note;
		Asset assets = getWeightedAssets(player);
		scoringRatio = assets.getPointPerMatch() / SimulationConfiguration.AVERAGE_POINTS_PER_MATCH;
		assistRatio = assets.getAssistPerMatch() / SimulationConfiguration.AVERAGE_ASSIST_PER_MATCH;
		efficiency = assets.getTrueShootingPercentage();

		note = (scoringRatio * 0.5) + (assistRatio * 0.3) + (efficiency * 0.2);
		return Math.min(note, 1);
	}

	public static double getPlayerDefenseNote(Player player) {
		double interceptionRatio;
		double blockRatio;
		double note;
		Asset asset = getWeightedAssets(player);
		interceptionRatio = asset.getInterceptionPerMatch() / SimulationConfiguration.AVERAGE_INTERCEPTION_PER_MATCH;
		blockRatio = asset.getBlockPerMatch() / SimulationConfiguration.AVERAGE_BLOCK_PER_MATCH;

		note = (interceptionRatio * 0.6) + (blockRatio * 0.4);
		return Math.min(note, 1);
	}

	private static Asset getWeightedAssets(Player player) {
		Asset season = player.getCurrentSeasonAssets();
		Asset previous = player.getPreSeasonAssets();

		double seasonMinutes = season.getMinutesPlayedPerMatch();
		double previousMinutes = previous.getMinutesPlayedPerMatch();
		double totalMinutes = seasonMinutes + previousMinutes;

		if (totalMinutes == 0) {
			return previous;
		}

		Asset result = new Asset();

		result.setPointPerMatch(
				(season.getPointPerMatch() * seasonMinutes
						+ previous.getPointPerMatch() * previousMinutes) /
						totalMinutes);

		result.setAssistPerMatch(
				(season.getAssistPerMatch() * seasonMinutes
						+ previous.getAssistPerMatch() * previousMinutes)
						/ totalMinutes);

		result.setInterceptionPerMatch(
				(season.getInterceptionPerMatch() * seasonMinutes
						+ previous.getInterceptionPerMatch() * previousMinutes)
						/ totalMinutes);

		result.setBlockPerMatch(
				(season.getBlockPerMatch() * seasonMinutes
						+ previous.getBlockPerMatch() * previousMinutes) /
						totalMinutes);

		result.setTrueShootingPercentage(
				(season.getTrueShootingPercentage() * seasonMinutes
						+ previous.getTrueShootingPercentage() * previousMinutes)
						/ totalMinutes);

		result.setMinutesPlayedPerMatch(
				(season.getMinutesPlayedPerMatch() * seasonMinutes +
						previous.getMinutesPlayedPerMatch() * previousMinutes) / totalMinutes);

		return result;
	}

	public static double getPlayerOverAllNote(Player player) {
		double attackNote = getPlayerAttackNote(player);
		double defenseNote = getPlayerDefenseNote(player);

		double performanceNote = (attackNote * 0.6) + (defenseNote * 0.4);
		double lastSeasonNote = player.getPreSeasonAssets().getNote();

		return (performanceNote * 0.7) + (lastSeasonNote * 0.3);
	}

	public static void updateFatigue(int minutesPlayed, Player player) {
		HealthStatus healthStatus = player.getHealthStatus();
		double fatigue = healthStatus.getFatigue();
		fatigue += (0.02 * minutesPlayed);
		if (fatigue > 1) {
			fatigue = 1;
		}
		healthStatus.setFatigue(fatigue);
		player.setHealthStatus(healthStatus);
	}

	public static void updateRest(int restMinutes, Player player) {
		HealthStatus healthStatus = player.getHealthStatus();
		double fatigue = healthStatus.getFatigue();
		fatigue -= 0.02 * restMinutes;
		if (fatigue < 0) {
			fatigue = 0;
		}
		healthStatus.setFatigue(fatigue);
		player.setHealthStatus(healthStatus);
	}

	public static void updateAsset(Player player, Asset matchAsset) {
		Asset seasonAsset = player.getCurrentSeasonAssets();

		if (matchAsset.getMinutesPlayedPerMatch() == 0) {
			return;
		}

		double seasonMinutes = seasonAsset.getMinutesPlayedPerMatch();
		double matchMinutes = matchAsset.getMinutesPlayedPerMatch();
		double totalMinutes = seasonMinutes + matchMinutes;

		seasonAsset.setPointPerMatch(
				(seasonAsset.getPointPerMatch() * seasonMinutes
				+ matchAsset.getPointPerMatch() * matchMinutes)
						/ totalMinutes);

		seasonAsset.setReboundPerMatch(
				(seasonAsset.getReboundPerMatch() * seasonMinutes
				+ matchAsset.getReboundPerMatch() * matchMinutes)
						/ totalMinutes);

		seasonAsset.setAssistPerMatch(
				(seasonAsset.getAssistPerMatch() * seasonMinutes
				+ matchAsset.getAssistPerMatch() * matchMinutes)
				/ totalMinutes);

		seasonAsset.setInterceptionPerMatch(
				(seasonAsset.getInterceptionPerMatch() * seasonMinutes
				+ matchAsset.getInterceptionPerMatch() * matchMinutes)
				/ totalMinutes);

		seasonAsset.setBlockPerMatch(
				(seasonAsset.getBlockPerMatch() * seasonMinutes
				+ matchAsset.getBlockPerMatch() * matchMinutes)
				/ totalMinutes);

		seasonAsset.setLostBallPerMatch(
				(seasonAsset.getLostBallPerMatch() * seasonMinutes
				+ matchAsset.getLostBallPerMatch() * matchMinutes)
				/ totalMinutes);

		seasonAsset.setMinutesPlayedPerMatch(
				(seasonAsset.getMinutesPlayedPerMatch() * seasonMinutes
				+ matchAsset.getMinutesPlayedPerMatch() * matchMinutes)
				/ totalMinutes);
	}
}
