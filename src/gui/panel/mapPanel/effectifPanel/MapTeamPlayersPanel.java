package gui.panel.mapPanel.effectifPanel;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import data.player.Player;
import data.team.Team;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.ThemeAware;
import process.utility.PlayerStatUtility;

public class MapTeamPlayersPanel extends JPanel implements ThemeAware {
	private JLabel[] playerLabels;

	public MapTeamPlayersPanel() {
		create();
		organize();
		updateTeam(null, true);
	}

	private void create() {
		playerLabels = new JLabel[10];
		for (int i = 0; i < playerLabels.length; i++) {
			playerLabels[i] = new JLabel("-");
			playerLabels[i].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
		}
	}

	private void organize() {
		setOpaque(false);
		setLayout(new GridLayout(5, 2, 10, 10));
		for (int i = 0; i < playerLabels.length; i++) {
			add(playerLabels[i]);
		}
		applyTheme();
	}

	public void updateTeam(Team team, boolean currentSeasonSelected) {
		if (team == null) {
			for (int i = 0; i < playerLabels.length; i++) {
				playerLabels[i].setText("-");
			}
			return;
		}
		ArrayList<Player> players;
		if (currentSeasonSelected) {
			players = new ArrayList<Player>(team.getCurrentPlayers().values());
		} else {
			players = new ArrayList<Player>(team.getFormerPlayers().values());
		}
		PlayerStatUtility.sortPlayersByDisplayedNote(players);

		for (int i = 0; i < playerLabels.length; i++) {
			if (i < players.size()) {
				Player player = players.get(i);
				playerLabels[i]
						.setText((int) Math.round(PlayerStatUtility.getDisplayedNote(player)) + "  " + player.getName());
			} else {
				playerLabels[i].setText("-");
			}
		}
	}

	@Override
	public void applyTheme() {
		for (int i = 0; i < playerLabels.length; i++) {
			playerLabels[i].setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		}
	}

}
