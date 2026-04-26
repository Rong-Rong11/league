package process.simulator.game.asset;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import data.player.Asset;
import data.player.Player;
import data.sport.play.action.ActionResult;
import data.team.Team;
import log.LoggerUtility;
import process.utility.PlayerUtility;
import process.visitor.actionresult.ActionResultVisitor;
import process.visitor.actionresult.AssetUpdateVisitor;

public class GameAssetManager {
	private static final Logger logger = LoggerUtility.getLogger(GameAssetManager.class, "text");

	public HashMap<Player, Asset> createPlayerAssetMap(ArrayList<Player> players) {
		HashMap<Player, Asset> assets = new HashMap<Player, Asset>();

		if (players == null) {
			logger.warn("Returning empty asset map because players list is null");
			return assets;
		}

		for (Player player : players) {
			if (player == null) {
				continue;
			}

			Asset asset = player.getCurrentSeasonAssets().getMinutesPlayedPerMatch() != 0
					? player.getCurrentSeasonAssets()
					: player.getPreSeasonAssets();

			assets.put(player, asset);
		}

		return assets;
	}

	public HashMap<Player, Asset> createNewAssets(ArrayList<Player> players) {
		HashMap<Player, Asset> teamPlayersNewAsset = new HashMap<Player, Asset>();

		if (players == null) {
			logger.warn("Returning empty new asset map because players list is null");
			return teamPlayersNewAsset;
		}

		for (Player player : players) {
			if (player == null) {
				continue;
			}
			teamPlayersNewAsset.put(player, new Asset());
		}

		return teamPlayersNewAsset;
	}

	public void updateAssetAfterAction(ActionResult actionResult, HashMap<Player, Asset> playersNewAssets) {
		if (actionResult == null || playersNewAssets == null) {
			logger.warn("Skipping asset update after action because action result or assets map is null");
			return;
		}

		ActionResultVisitor<Void> assetUpdateVisitor = new AssetUpdateVisitor(playersNewAssets);
		actionResult.accept(assetUpdateVisitor);
	}

	public void updateCurrentSeasonAssets(Team team, HashMap<Player, Asset> playersNewAssets) {
		if (team == null || playersNewAssets == null) {
			logger.warn("Skipping current season asset update because team or assets map is null");
			return;
		}

		for (Player player : team.getCurrentPlayers().values()) {
			if (player == null) {
				continue;
			}
			PlayerUtility.updateAsset(player, playersNewAssets.get(player));
		}
	}

	public void updateTrueShootingPercentages(Team homeTeam, Team awayTeam,
			HashMap<Player, Asset> playersNewAssets) {

		if (homeTeam == null || awayTeam == null || playersNewAssets == null) {
			logger.warn("Skipping true shooting update because team or assets map is null");
			return;
		}

		updateTrueShootingPercentages(homeTeam, playersNewAssets);
		updateTrueShootingPercentages(awayTeam, playersNewAssets);
	}

	private void updateTrueShootingPercentages(Team team, HashMap<Player, Asset> playersNewAssets) {
		if (team == null || playersNewAssets == null) {
			logger.warn("Skipping true shooting update because team or assets map is null");
			return;
		}

		for (Player player : team.getCurrentPlayers().values()) {
			if (player == null) {
				continue;
			}

			Asset asset = playersNewAssets.get(player);
			if (asset == null) {
				continue;
			}

			double fieldGoalAttempts = asset.getTwoPointAttemptPerMatch() + asset.getThreePointAttemptPerMatch();
			double freeThrowAttempts = asset.getFreeThrowAttemptPerMatch();
			double denominator = 2 * (fieldGoalAttempts + 0.44 * freeThrowAttempts);

			if (denominator > 0) {
				asset.setTrueShootingPercentage(asset.getPointPerMatch() / denominator);
			}
		}
	}
}
