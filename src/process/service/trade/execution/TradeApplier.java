package process.service.trade.execution;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import data.player.Player;
import data.team.Team;
import log.LoggerUtility;
import process.utility.FinanceUtility;

public class TradeApplier {
	private static final Logger logger = LoggerUtility.getLogger(TradeApplier.class, "text");

	public void applyTrade(Team team, ArrayList<Player> players) {
		if (team == null || players == null) {
			logger.warn("Skipping trade application because team or players list is null");
			return;
		}

		logger.debug("Applying trade to " + team.getName() + " with " + players.size() + " players");

		HashMap<String, Player> currentPlayers = team.getCurrentPlayers();
		HashMap<String, Player> newPlayers = new HashMap<String, Player>();

		for (Player player : players) {
			if (player == null) {
				logger.warn("Skipping null player during trade application for " + team.getName());
				continue;
			}

			if (!currentPlayers.containsKey(player.getName())) {
				player.setTransfered(true);
				logger.trace("Marked player as transferred: " + player.getName());
			}

			newPlayers.put(player.getName(), player);
		}

		team.setCurrentPlayers(newPlayers);
		updateStarPlayer(team, players);
		FinanceUtility.updateTeamPayroll(team);
		team.getTeamFinance().incrementTransferMade();

		logger.debug("Trade applied to " + team.getName());
	}

	public static void updateStarPlayer(Team team, ArrayList<Player> players) {
		if (team == null || players == null) {
			logger.warn("Skipping star player update because team or players list is null");
			return;
		}

		for (Player player : players) {
			if (player == null) {
				continue;
			}

			if (!player.isStar()) {
				continue;
			}

			team.setStarPlayer(player);
			logger.trace("Updated star player for " + team.getName() + ": " + player.getName());
			return;
		}

		team.setStarPlayer(null);
		logger.trace("No star player found for " + team.getName());
	}
}
