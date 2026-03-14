package gui.panel.liveMatchPanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.panel.common.SectionTitle;

public class LiveTeamStatsPanel extends JPanel {
	private JLabel pointsLabel;
	private JLabel reboundsLabel;
	private JLabel assistsLabel;
	private JLabel turnoversLabel;
	private JLabel fgLabel;
	private JLabel threeLabel;
	private JLabel bestPlayersLabel;

	public LiveTeamStatsPanel() {
		super(new BorderLayout(0, 12));
		setOpaque(false);

		JPanel statsPanel = new JPanel(new GridLayout(6, 1, 0, 6));
		statsPanel.setOpaque(false);

		pointsLabel = new JLabel("Points : 0");
		reboundsLabel = new JLabel("Rebonds : 0");
		assistsLabel = new JLabel("Passes : 0");
		turnoversLabel = new JLabel("Turnovers : 0");
		fgLabel = new JLabel("FG% : 0%");
		threeLabel = new JLabel("3PT% : 0%");
		bestPlayersLabel = new JLabel("-");

		statsPanel.add(pointsLabel);
		statsPanel.add(reboundsLabel);
		statsPanel.add(assistsLabel);
		statsPanel.add(turnoversLabel);
		statsPanel.add(fgLabel);
		statsPanel.add(threeLabel);

		JPanel bestPlayersPanel = new JPanel(new BorderLayout());
		bestPlayersPanel.setOpaque(false);
		bestPlayersPanel.add(new SectionTitle("MEILLEURS JOUEURS", ""), BorderLayout.NORTH);
		bestPlayersPanel.add(bestPlayersLabel, BorderLayout.CENTER);

		add(statsPanel, BorderLayout.NORTH);
		add(bestPlayersPanel, BorderLayout.CENTER);
	}

	public void updateStats(int points, int rebounds, int assists, int turnovers, String fg, String three,
			String bestPlayers) {
		pointsLabel.setText("Points : " + points);
		reboundsLabel.setText("Rebonds : " + rebounds);
		assistsLabel.setText("Passes : " + assists);
		turnoversLabel.setText("Turnovers : " + turnovers);
		fgLabel.setText("FG% : " + fg);
		threeLabel.setText("3PT% : " + three);
		bestPlayersLabel.setText(bestPlayers);
	}
}
