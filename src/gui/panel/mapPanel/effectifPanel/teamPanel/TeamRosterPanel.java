package gui.panel.mapPanel.effectifPanel.teamPanel;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import data.player.Player;
import data.team.Team;
import gui.panel.common.PlayerDisplayUtil;
import gui.panel.mapPanel.effectifPanel.playerPanel.PlayerRosterEntryPanel;

public class TeamRosterPanel extends JPanel {

	public TeamRosterPanel() {
		setOpaque(false);
		setLayout(new GridLayout(0, 3, 8, 6));
	}

	public void updateTeam(Team team, boolean currentSeasonSelected) {
		removeAll();
		if (team == null) {
			revalidate();
			repaint();
			return;
		}

		ArrayList<Player> players = new ArrayList<Player>(team.getPlayers().values());
		sortPlayersByNote(players);

		int rows = 8;
		int columns = (int) Math.ceil(players.size() / 8.0);
		if (columns <= 0) {
			columns = 1;
		}
		setLayout(new GridLayout(rows, columns, 8, 6));

		for (Player player : players) {
			PlayerRosterEntryPanel entryPanel = new PlayerRosterEntryPanel();
			entryPanel.updatePlayer(player, currentSeasonSelected);
			add(entryPanel);
		}

		revalidate();
		repaint();
	}

	private void sortPlayersByNote(ArrayList<Player> players) {
		for (int i = 0; i < players.size() - 1; i++) {
			for (int j = i + 1; j < players.size(); j++) {
				double firstNote = PlayerDisplayUtil.getDisplayedNote(players.get(i));
				double secondNote = PlayerDisplayUtil.getDisplayedNote(players.get(j));
				if (secondNote > firstNote) {
					Player currentPlayer = players.get(i);
					players.set(i, players.get(j));
					players.set(j, currentPlayer);
				}
			}
		}
	}
}
