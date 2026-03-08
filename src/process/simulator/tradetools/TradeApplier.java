package process.simulator.tradetools;

import java.util.ArrayList;
import java.util.HashMap;

import data.player.Player;
import data.team.Team;

public class TradeApplier {
	
	public TradeApplier() {
		
	}
	public void applyTrade(Team team, ArrayList<Player> teamIncoming) {
		HashMap<String, Player> oldTeam = team.getPlayers();
		HashMap<String, Player> updatedTeam = new HashMap<String, Player>();

		for (Player player : teamIncoming) {
			if (!oldTeam.containsKey(player.getName())) {
				player.setTransfered(true);
			}
			updatedTeam.put(player.getName(), player);
		}
		team.setPlayers(updatedTeam);
		updateStarPlayer(team, teamIncoming);
		team.getTeamFinance().incrementTransferMade();
	}

	public static void updateStarPlayer(Team team, ArrayList<Player> teamIncoming) {
		for (Player player : teamIncoming) {
			if (player.isStar()) {
				team.setStarPlayer(player);
				return;
			}
		}
		team.setStarPlayer(null);
	}
}
