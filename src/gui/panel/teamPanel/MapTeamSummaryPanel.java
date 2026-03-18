package gui.panel.teamPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.player.Player;
import data.team.Team;
import process.utilitary.FinanceUtilitary;

public class MapTeamSummaryPanel extends JPanel {
	public interface TeamSelectionListener {
		void onTeamSelected(String teamName);
	}

	private JComboBox<String> teamSelector;
	private JLabel teamNameLabel;
	private JLabel payrollLabel;
	private JLabel capacityLabel;
	private JLabel averageNoteLabel;
	private JButton openRosterButton;
	private TeamLogoPanel teamLogoPanel;
	private TeamSelectionListener teamSelectionListener;
	private boolean updatingSelector;

	public MapTeamSummaryPanel() {
		create();
		organize();
		updateTeam(null);
	}

	private void create() {
		teamSelector = new JComboBox<String>();
		teamNameLabel = new JLabel();
		payrollLabel = new JLabel();
		capacityLabel = new JLabel();
		averageNoteLabel = new JLabel();
		openRosterButton = new JButton("Voir l'effectif complet");
		teamLogoPanel = new TeamLogoPanel("", 56);

		teamNameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		teamNameLabel.setForeground(new Color(0x17, 0x31, 0x74));
		openRosterButton.setFocusPainted(false);
		teamSelector.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
	}

	private void organize() {
		setLayout(new BorderLayout(0, 12));
		setOpaque(false);

		JPanel headerPanel = new JPanel(new BorderLayout(12, 0));
		headerPanel.setOpaque(false);
		headerPanel.add(teamLogoPanel, BorderLayout.WEST);
		headerPanel.add(teamNameLabel, BorderLayout.CENTER);

		JPanel infoPanel = new JPanel();
		infoPanel.setOpaque(false);
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.add(teamSelector);
		infoPanel.add(Box.createVerticalStrut(10));
		infoPanel.add(buildInfoLabel("Budget annuel", payrollLabel));
		infoPanel.add(Box.createVerticalStrut(8));
		infoPanel.add(buildInfoLabel("Capacité salle", capacityLabel));
		infoPanel.add(Box.createVerticalStrut(8));
		infoPanel.add(buildInfoLabel("Note moyenne", averageNoteLabel));

		add(headerPanel, BorderLayout.NORTH);
		add(infoPanel, BorderLayout.CENTER);
		add(openRosterButton, BorderLayout.SOUTH);
	}

	private JPanel buildInfoLabel(String title, JLabel valueLabel) {
		JPanel row = new JPanel();
		row.setOpaque(false);
		row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		titleLabel.setForeground(new Color(110, 117, 131));
		valueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		valueLabel.setForeground(new Color(0x17, 0x31, 0x74));

		row.add(titleLabel);
		row.add(Box.createVerticalStrut(2));
		row.add(valueLabel);
		row.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
		return row;
	}

	public void updateTeam(Team team) {
		if (team == null) {
			teamLogoPanel.setTeamName("");
			teamNameLabel.setText("Aucune équipe");
			payrollLabel.setText("-");
			capacityLabel.setText("-");
			averageNoteLabel.setText("-");
			openRosterButton.setEnabled(false);
			return;
		}

		updatingSelector = true;
		teamSelector.setSelectedItem(team.getName());
		updatingSelector = false;
		FinanceUtilitary.updateTeamPayroll(team);
		teamLogoPanel.setTeamName(team.getName());
		teamNameLabel.setText(team.getName());
		payrollLabel.setText(formatSalary(team.getTeamFinance().getPayroll()));
		capacityLabel.setText(String.valueOf(team.getStadium().getCapacity()));
		averageNoteLabel.setText(formatOneDecimal(computeAverageNote(team)) + "/100");
		openRosterButton.setEnabled(true);
	}

	private double computeAverageNote(Team team) {
		double total = 0;
		int count = 0;
		for (Player player : team.getPlayers().values()) {
			total += getDisplayedNote(player);
			count++;
		}
		if (count == 0) {
			return 0;
		}
		return total / count;
	}

	private double getDisplayedNote(Player player) {
		if (player.getCurrentSeasonAssets().getNote() > 0) {
			return player.getCurrentSeasonAssets().getNote();
		}
		return player.getPreSeasonAssets().getNote();
	}

	private String formatOneDecimal(double value) {
		return String.format("%.1f", value);
	}

	private String formatSalary(double salary) {
		if (salary >= 1) {
			return "$" + formatOneDecimal(salary) + "M";
		}
		return "$" + Math.round(salary * 1000.0) + "K";
	}

	public JButton getOpenRosterButton() {
		return openRosterButton;
	}

	public void setTeamNames(String[] teamNames) {
		updatingSelector = true;
		teamSelector.removeAllItems();
		for (int i = 0; i < teamNames.length; i++) {
			teamSelector.addItem(teamNames[i]);
		}
		updatingSelector = false;
	}

	public void setTeamSelectionListener(TeamSelectionListener teamSelectionListener) {
		this.teamSelectionListener = teamSelectionListener;
		teamSelector.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (updatingSelector) {
					return;
				}
				Object selectedItem = teamSelector.getSelectedItem();
				if (selectedItem != null && MapTeamSummaryPanel.this.teamSelectionListener != null) {
					MapTeamSummaryPanel.this.teamSelectionListener.onTeamSelected(selectedItem.toString());
				}
			}
		});
	}
}
