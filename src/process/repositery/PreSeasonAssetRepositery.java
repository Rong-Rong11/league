package process.repositery;

import java.util.HashMap;

import data.player.Asset;
import data.player.Player;

public class PreSeasonAssetRepositery {
	
	private HashMap<Player, Asset> preSeasonAssets = new HashMap<Player, Asset>() ; 
	private static PreSeasonAssetRepositery instance = new PreSeasonAssetRepositery() ; 
	
	private PreSeasonAssetRepositery() {
		
	}
	
	public static PreSeasonAssetRepositery getInstance() {
		return instance ; 
	}
	
	public void register(Player player, Asset asset) {
		preSeasonAssets.put(player, asset) ; 
	}
	
	public Asset getPreSeasonAsset(Player player) {
		if(preSeasonAssets.containsKey(player)) {
			return preSeasonAssets.get(player) ; 
		}
		return null ; 
	}
}
