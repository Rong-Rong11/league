package process.simulator.game.event;

import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import data.player.Player;
import log.LoggerUtility;

public class PossessionPlayerSelector {
	private static final Logger logger = LoggerUtility.getLogger(PossessionPlayerSelector.class, "text");

	public Player chooseAttackingPlayer(TreeMap<Double, Player> attackingPlayers) {
		if (attackingPlayers == null || attackingPlayers.isEmpty()) {
			logger.warn("Unable to select attacking player because attacking players map is null or empty");
			return null;
		}

		double random = Math.random();

		for (Double key : attackingPlayers.keySet()) {
			if (random <= key) {
				return attackingPlayers.get(key);
			}
		}

		return attackingPlayers.lastEntry().getValue();
	}

	public Player chooseDefendingPlayer(TreeMap<Double, Player> defensivePlayers) {
		if (defensivePlayers == null || defensivePlayers.isEmpty()) {
			logger.warn("Unable to select defending player because defensive players map is null or empty");
			return null;
		}

		ArrayList<Player> players = new ArrayList<Player>(defensivePlayers.values());
		int randomIndex = (int) (Math.random() * players.size());

		return players.get(randomIndex);
	}
}
