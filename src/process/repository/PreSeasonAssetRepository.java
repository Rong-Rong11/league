package process.repository;

import java.util.HashMap;

import data.player.Asset;
import data.player.Player;

public class PreSeasonAssetRepository {
	private HashMap<Player, Asset> preSeasonAssets = new HashMap<>();
	private static PreSeasonAssetRepository instance = new PreSeasonAssetRepository();

	private PreSeasonAssetRepository() {
	}

	public static PreSeasonAssetRepository getInstance() {
		return instance;
	}

	public void register(Player player, Asset asset) {
		this.preSeasonAssets.put(player, asset);
	}

	public Asset getPreSeasonAsset(Player player) {
		if (this.preSeasonAssets.containsKey(player)) {
			return this.preSeasonAssets.get(player);
		}
		return null;
	}

	public void clear() {
		this.preSeasonAssets.clear();
	}
}
