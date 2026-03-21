package gui.panel.mapPanel.effectifPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import data.player.Player;
import data.team.Team;
import gui.panel.common.PlayerDisplayUtil;

public class MapTeamPlayersPanel extends JPanel {
	private JLabel[] playerLabels;

	public MapTeamPlayersPanel() {
		create();
		organize();
		updateTeam(null);
	}

	private void create() {
		playerLabels = new JLabel[10];
		for (int i = 0; i < playerLabels.length; i++) {
			playerLabels[i] = new JLabel("-");
			playerLabels[i].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
			playerLabels[i].setForeground(new Color(40, 40, 40));
		}
	}

	private void organize() {
		setOpaque(false);
		setLayout(new GridLayout(5, 2, 10, 10));
		for (int i = 0; i < playerLabels.length; i++) {
			add(playerLabels[i]);
		}
	}

	public void updateTeam(Team team) {
		if (team == null) {
			for (int i = 0; i < playerLabels.length; i++) {
				playerLabels[i].setText("-");
			}
			return;
		}

		ArrayList<Player> players = new ArrayList<Player>(team.getPlayers().values());
		sortPlayersByNote(players);

		for (int i = 0; i < playerLabels.length; i++) {
			if (i < players.size()) {
				Player player = players.get(i);
				playerLabels[i].setText((int) Math.round(PlayerDisplayUtil.getDisplayedNote(player)) + "  " + player.getName());
			} else {
				playerLabels[i].setText("-");
			}
		}
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
