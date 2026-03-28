/*
 * Decompiled with CFR 0.152.
 */
package process.repositery;

import data.player.Asset;
import data.player.Player;
import java.util.HashMap;

public class PreSeasonAssetRepositery {
    private HashMap<Player, Asset> preSeasonAssets = new HashMap<>();
    private static PreSeasonAssetRepositery instance = new PreSeasonAssetRepositery();

    private PreSeasonAssetRepositery() {
    }

    public static PreSeasonAssetRepositery getInstance() {
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
}
