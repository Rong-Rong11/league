/*
 * Decompiled with CFR 0.152.
 */
package process.repository;

import java.util.HashMap;

import data.player.Asset;
import data.player.Player;

public class CurrentSeasonAssetRepository {
    private HashMap<Player, Asset> currentSeasonAssets = new HashMap<>();
    private static CurrentSeasonAssetRepository instance = new CurrentSeasonAssetRepository();

    private CurrentSeasonAssetRepository() {
    }

    public static CurrentSeasonAssetRepository getInstance() {
        return instance;
    }

    public void register(Player player, Asset asset) {
        this.currentSeasonAssets.put(player, asset);
    }

    public Asset getCurrentSeasonAsset(Player player) {
        if (this.currentSeasonAssets.containsKey(player)) {
            return this.currentSeasonAssets.get(player);
        }
        return null;
    }
}
