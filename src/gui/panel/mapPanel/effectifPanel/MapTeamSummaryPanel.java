package gui.panel.mapPanel.effectifPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.team.Team;
import gui.panel.common.PlayerDisplayUtil;
import gui.panel.mapPanel.effectifPanel.teamPanel.TeamLogoPanel;
import process.utilitary.FinanceUtilitary;
import process.utilitary.TeamStatUtil;

public class MapTeamSummaryPanel extends JPanel {
	private JLabel teamNameLabel;
	private JLabel payrollLabel;
	private JLabel capacityLabel;
	private JLabel averageNoteLabel;
	private JButton openRosterButton;
	private TeamLogoPanel teamLogoPanel;

	public MapTeamSummaryPanel() {
		create();
		organize();
		updateTeam(null);
	}

	private void create() {
		teamNameLabel = new JLabel();
		payrollLabel = new JLabel();
		capacityLabel = new JLabel();
		averageNoteLabel = new JLabel();
		openRosterButton = new JButton("Voir l'effectif complet");
		teamLogoPanel = new TeamLogoPanel("", 56);

		teamNameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		teamNameLabel.setForeground(new Color(0x17, 0x31, 0x74));
		openRosterButton.setFocusPainted(false);
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
			showEmptyState();
			return;
		}
		showTeamState(team);
	}

	private void showEmptyState() {
		teamLogoPanel.setTeamName("");
		teamNameLabel.setText("Aucune équipe");
		payrollLabel.setText("-");
		capacityLabel.setText("-");
		averageNoteLabel.setText("-");
		openRosterButton.setEnabled(false);
	}

	private void showTeamState(Team team) {
		teamLogoPanel.setTeamName(team.getName());
		teamNameLabel.setText(team.getName());
		FinanceUtilitary.updateTeamPayroll(team);
		payrollLabel.setText(PlayerDisplayUtil.formatSalary(team.getTeamFinance().getPayroll()));
		capacityLabel.setText(String.valueOf(team.getStadium().getCapacity()));
		averageNoteLabel.setText(PlayerDisplayUtil.formatOneDecimal(TeamStatUtil.getAverageNote(team)) + "/100");
		openRosterButton.setEnabled(true);
	}

	public JButton getOpenRosterButton() {
		return openRosterButton;
	}
}
