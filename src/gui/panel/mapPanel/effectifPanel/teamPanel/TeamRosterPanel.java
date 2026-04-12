package gui.panel.mapPanel.effectifPanel.teamPanel;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import data.player.Player;
import data.team.Team;
import gui.panel.mapPanel.effectifPanel.playerPanel.PlayerRosterEntryPanel;
import gui.utility.PlayerStatUtility;

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
		ArrayList<Player> players;
		if (currentSeasonSelected) {
			players = new ArrayList<Player>(team.getCurrentPlayers().values());
		} else {
			players = new ArrayList<Player>(team.getFormerPlayers().values());
		}

		PlayerStatUtility.sortPlayersByDisplayedNote(players);

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

}
