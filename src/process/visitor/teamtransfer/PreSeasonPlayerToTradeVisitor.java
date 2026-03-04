package process.visitor.teamtransfer;

import java.util.TreeMap;

import data.player.Player;
import data.team.Team;
import data.team.finance.transfer.AllIn;
import data.team.finance.transfer.Balanced;
import data.team.finance.transfer.Rebuild;
import data.team.finance.transfer.SalaryDump;
import data.team.finance.transfer.SmallAdjust;
import data.team.finance.transfer.SuperstarBuild;
import process.utilitary.PlayerUtilitary;

public class PreSeasonPlayerToTradeVisitor implements TeamTransferVisitor<Player> {
	private Team team;

	public PreSeasonPlayerToTradeVisitor(Team team) {
		super();
		this.team = team;
	}

	@Override
	public Player visit(AllIn allIn) {
		TreeMap<Double, Player> sortedPlayers = new TreeMap<Double, Player>();
		for (Player player : team.getPlayers().values()) {
			sortedPlayers.put(PlayerUtilitary.getPlayerOverAllNote(player), player);
		}

		int compt = 0;
		for (Double key : sortedPlayers.descendingKeySet()) {
			if (compt < 3) {
				compt++;
				continue;
			}

			Player player = sortedPlayers.get(key);
			if (player.isTransfered()) {
				continue;
			}

			return player;
		}
		return sortedPlayers.get(sortedPlayers.lastKey());
	}

	@Override
	public Player visit(SuperstarBuild superstarBuild) {
		TreeMap<Double, Player> sortedPlayers = new TreeMap<Double, Player>();
		for (Player player : team.getPlayers().values()) {
			sortedPlayers.put(PlayerUtilitary.getPlayerOverAllNote(player), player);
		}
		int compt = 0;
		for (Double key : sortedPlayers.descendingKeySet()) {
			if (compt < 1) {
				compt++;
				continue;
			}

			Player player = sortedPlayers.get(key);
			if (player.isTransfered()) {
				continue;
			}
			return player;
		}
		return sortedPlayers.get(sortedPlayers.lastKey());
	}

	@Override
	public Player visit(SmallAdjust smallAdjust) {
		TreeMap<Double, Player> sortedPlayers = new TreeMap<Double, Player>();
		for (Player player : team.getPlayers().values()) {
			sortedPlayers.put(PlayerUtilitary.getPlayerOverAllNote(player), player);
		}

		int compt = 0;
		for (Double key : sortedPlayers.keySet()) {
			Player player = sortedPlayers.get(key);
			if (compt == 0) {
				compt++;
				continue;
			}
			if (player.isTransfered()) {
				continue;
			}
			return player;
		}
		return sortedPlayers.get(sortedPlayers.firstKey());
	}

	@Override
	public Player visit(Balanced balanced) {
		TreeMap<Double, Player> sortedPlayers = new TreeMap<Double, Player>();
		for (Player player : team.getPlayers().values()) {
			sortedPlayers.put(PlayerUtilitary.getPlayerOverAllNote(player), player);
		}

		int targetCompt = sortedPlayers.size() / 2;
		int forbiddenCompt = targetCompt + 3;
		int compt = 0;

		for (Double key : sortedPlayers.keySet()) {
			Player player = sortedPlayers.get(key);
			if (player.isTransfered()) {
				continue;
			}
			if ((compt >= targetCompt) && (compt < forbiddenCompt)) {
				return player;
			}
			compt++;
		}

		return sortedPlayers.get(sortedPlayers.firstKey());
	}

	@Override
	public Player visit(Rebuild rebuild) {
		TreeMap<Double, Player> sortedPlayers = new TreeMap<Double, Player>();
		for (Player player : team.getPlayers().values()) {
			sortedPlayers.put(PlayerUtilitary.getPlayerOverAllNote(player), player);
		}

		for (Double key : sortedPlayers.descendingKeySet()) {
			Player player = sortedPlayers.get(key);
			if (player.isTransfered()) {
				continue;
			}
			return player;
		}
		return sortedPlayers.get(sortedPlayers.lastKey());
	}

	@Override
	public Player visit(SalaryDump salaryDump) {
		TreeMap<Double, Player> sortedPlayers = new TreeMap<Double, Player>();
		for (Player player : team.getPlayers().values()) {
			sortedPlayers.put(player.getSalary(), player);
		}

		for (Double key : sortedPlayers.descendingKeySet()) {
			Player player = sortedPlayers.get(key);
			if (player.isTransfered()) {
				continue;
			}
			return player;
		}
		return sortedPlayers.get(sortedPlayers.lastKey());
	}
}
