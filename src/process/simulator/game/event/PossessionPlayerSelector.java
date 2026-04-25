package process.simulator.game.event;

import java.util.ArrayList;
import java.util.TreeMap;

import data.player.Player;

public class PossessionPlayerSelector {

	public Player chooseAttackingPlayer(TreeMap<Double, Player> attackingPlayers) {
		double random = Math.random();
		for (Double key : attackingPlayers.keySet()) {
			if (random <= key) {
				return attackingPlayers.get(key);
			}
		}
		return attackingPlayers.lastEntry().getValue();
	}

	public Player chooseDefendingPlayer(TreeMap<Double, Player> defensivePlayers) {
		ArrayList<Player> players = new ArrayList<Player>(defensivePlayers.values());
		int randomIndex = (int) (Math.random() * players.size());
		return players.get(randomIndex);
	}
}
