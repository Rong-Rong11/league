package process.repository;

import java.util.ArrayList;
import java.util.HashMap;

import data.player.Player;

public class PlayerRepository {
	private HashMap<String, Player> players = new HashMap<>();
	private static PlayerRepository instance = new PlayerRepository();

	private PlayerRepository() {
	}

	public static PlayerRepository getInstance() {
		return instance;
	}

	public void register(String playerName, Player player) {
		this.players.put(playerName, player);
	}

	public Player getPlayer(String playerName) {
		if (this.players.containsKey(playerName)) {
			return this.players.get(playerName);
		}
		return null;
	}

	public ArrayList<Player> getAllPlayers() {
		return new ArrayList<Player>(this.players.values());
	}

	public void clear() {
		this.players.clear();
	}
}
