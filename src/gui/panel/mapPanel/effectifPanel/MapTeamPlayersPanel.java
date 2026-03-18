package gui.panel.mapPanel.effectifPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JLabel;
import javax.swing.JPanel;

import data.player.Player;
import data.team.Team;

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
		Collections.sort(players, new Comparator<Player>() {
			@Override
			public int compare(Player a, Player b) {
				return Double.compare(getDisplayedNote(b), getDisplayedNote(a));
			}
		});

		for (int i = 0; i < playerLabels.length; i++) {
			if (i < players.size()) {
				Player player = players.get(i);
				playerLabels[i].setText((int) Math.round(getDisplayedNote(player)) + "  " + player.getName());
			} else {
				playerLabels[i].setText("-");
			}
		}
	}

	private double getDisplayedNote(Player player) {
		if (player.getCurrentSeasonAssets().getNote() > 0) {
			return player.getCurrentSeasonAssets().getNote();
		}
		return player.getPreSeasonAssets().getNote();
	}
}
