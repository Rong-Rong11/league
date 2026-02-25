package process.repositery;

import java.util.ArrayList;
import java.util.HashMap;

import data.player.Player;

public class PlayerRepositery {
	private HashMap<String, Player> players = new HashMap<String, Player>() ; 
	private static PlayerRepositery instance = new PlayerRepositery() ; 
	
	private PlayerRepositery() {
		
	}
	
	public static PlayerRepositery getInstance() {
		return instance ; 
	}
	
	public void register(String name, Player player) {
		players.put(name, player) ; 
	}
	
	public Player getPlayer(String name) {
		if(players.containsKey(name)) {
			return players.get(name) ; 
		}
		return null ; 
	}
	
	public ArrayList<Player> getAllPlayers () {
		return new ArrayList<Player>(players.values()) ; 
	}
	
}
