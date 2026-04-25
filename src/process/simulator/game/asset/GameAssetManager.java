package process.simulator.game.asset;

import java.util.ArrayList;
import java.util.HashMap;

import data.player.Asset;
import data.player.Player;
import data.sport.play.action.ActionResult;
import data.team.Team;
import process.utility.PlayerUtility;
import process.visitor.actionresult.ActionResultVisitor;
import process.visitor.actionresult.AssetUpdateVisitor;

public class GameAssetManager {

	public HashMap<Player, Asset> createPlayerAssetMap(ArrayList<Player> players) {
		HashMap<Player, Asset> assets = new HashMap<Player, Asset>();
		for (Player player : players) {
			Asset asset = player.getCurrentSeasonAssets().getMinutesPlayedPerMatch() != 0
					? player.getCurrentSeasonAssets()
					: player.getPreSeasonAssets();
			assets.put(player, asset);
		}
		return assets;
	}

	public HashMap<Player, Asset> createNewAssets(ArrayList<Player> players) {
		HashMap<Player, Asset> teamPlayersNewAsset = new HashMap<Player, Asset>();
		for (Player player : players) {
			teamPlayersNewAsset.put(player, new Asset());
		}
		return teamPlayersNewAsset;
	}

	public void updateAssetAfterAction(ActionResult actionResult, HashMap<Player, Asset> playersNewAssets) {
		ActionResultVisitor<Void> assetUpdateVisitor = new AssetUpdateVisitor(playersNewAssets);
		actionResult.accept(assetUpdateVisitor);
	}

	public void updateCurrentSeasonAssets(Team team, HashMap<Player, Asset> playersNewAssets) {
		for (Player player : team.getCurrentPlayers().values()) {
			PlayerUtility.updateAsset(player, playersNewAssets.get(player));
		}
	}

	public void updateTrueShootingPercentages(Team homeTeam, Team awayTeam,
			HashMap<Player, Asset> playersNewAssets) {
		updateTrueShootingPercentages(homeTeam, playersNewAssets);
		updateTrueShootingPercentages(awayTeam, playersNewAssets);
	}

	private void updateTrueShootingPercentages(Team team, HashMap<Player, Asset> playersNewAssets) {
		for (Player player : team.getCurrentPlayers().values()) {
			Asset asset = playersNewAssets.get(player);
			double fieldGoalAttempts = asset.getTwoPointAttemptPerMatch() + asset.getThreePointAttemptPerMatch();
			double freeThrowAttempts = asset.getFreeThrowAttemptPerMatch();
			double denominator = 2 * (fieldGoalAttempts + 0.44 * freeThrowAttempts);
			if (denominator > 0) {
				asset.setTrueShootingPercentage(asset.getPointPerMatch() / denominator);
			}
		}
	}
}
