package process.repositery;

import java.util.HashMap;

import data.player.Asset;
import data.player.Player;

public class CurrentSeasonAssetRepositery {
	private HashMap<Player, Asset> currentSeasonAssets = new HashMap<Player, Asset>();
	private static CurrentSeasonAssetRepositery instance = new CurrentSeasonAssetRepositery();

	private CurrentSeasonAssetRepositery() {

	}

	public static CurrentSeasonAssetRepositery getInstance() {
		return instance;
	}

	public void register(Player player, Asset asset) {
		currentSeasonAssets.put(player, asset);
	}

	public Asset getCurrentSeasonAsset(Player player) {
		if (currentSeasonAssets.containsKey(player)) {
			return currentSeasonAssets.get(player);
		}
		return null;
	}
}
