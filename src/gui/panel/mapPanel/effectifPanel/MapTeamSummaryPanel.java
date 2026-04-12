package gui.panel.mapPanel.effectifPanel;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.team.Team;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.PlayerDisplayUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.ThemeAware;
import gui.panel.mapPanel.effectifPanel.teamPanel.TeamLogoPanel;
import process.orchestrator.GUIInterface;
import process.utility.TeamDisplayUtility;

public class MapTeamSummaryPanel extends JPanel implements ThemeAware {

	private final GUIInterface guiInterface;
	private JLabel teamNameLabel;
	private JLabel cityLabel;
	private JLabel conferenceLabel;
	private JLabel divisionLabel;
	private JLabel arenaLabel;
	private JLabel budgetLabel;
	private JLabel capacityLabel;
	private JLabel averageNoteLabel;
	private JLabel financialPolicyLabel;
	private JLabel marketSizeLabel;
	private JButton openRosterButton;
	private TeamLogoPanel teamLogoPanel;
	private JLabel[] infoTitleLabels;

	public MapTeamSummaryPanel(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		create();
		organize();
		updateTeam(null, true);
		applyTheme();
	}

	private void create() {
		teamNameLabel = new JLabel();
		cityLabel = new JLabel();
		conferenceLabel = new JLabel();
		divisionLabel = new JLabel();
		arenaLabel = new JLabel();
		budgetLabel = new JLabel();
		capacityLabel = new JLabel();
		averageNoteLabel = new JLabel();
		financialPolicyLabel = new JLabel();
		marketSizeLabel = new JLabel();
		openRosterButton = new RoundedButton("Voir l'effectif complet");
		teamLogoPanel = new TeamLogoPanel("", 56);
		teamLogoPanel.setTeamQueryInterface(guiInterface);

		teamNameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		teamNameLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		openRosterButton.setFocusPainted(false);
		openRosterButton.setBackground(DashboardPanelUtil.getPrimaryActionColor());
		openRosterButton.setForeground(DashboardPanelUtil.getPrimaryActionTextColor());
	}

	private void organize() {
		setLayout(new BorderLayout(0, 12));
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(8, 10, 10, 10));

		JPanel headerPanel = new JPanel(new BorderLayout(12, 0));
		headerPanel.setOpaque(false);
		headerPanel.add(teamLogoPanel, BorderLayout.WEST);
		headerPanel.add(teamNameLabel, BorderLayout.CENTER);

		JPanel infoPanel = new JPanel();
		infoPanel.setOpaque(false);
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.add(buildInfoLabel("Equipe", teamNameLabel));
		infoPanel.add(Box.createVerticalStrut(8));
		infoPanel.add(buildInfoLabel("Ville", cityLabel));
		infoPanel.add(Box.createVerticalStrut(8));
		infoPanel.add(buildInfoLabel("Conference", conferenceLabel));
		infoPanel.add(Box.createVerticalStrut(8));
		infoPanel.add(buildInfoLabel("Division", divisionLabel));
		infoPanel.add(Box.createVerticalStrut(8));
		infoPanel.add(buildInfoLabel("Arene", arenaLabel));
		infoPanel.add(Box.createVerticalStrut(8));
		infoPanel.add(buildInfoLabel("Budget annuel", budgetLabel));
		infoPanel.add(Box.createVerticalStrut(8));
		infoPanel.add(buildInfoLabel("Capacite salle", capacityLabel));
		infoPanel.add(Box.createVerticalStrut(8));
		infoPanel.add(buildInfoLabel("Note moyenne", averageNoteLabel));
		infoPanel.add(Box.createVerticalStrut(8));
		infoPanel.add(buildInfoLabel("Politique financiere", financialPolicyLabel));
		infoPanel.add(Box.createVerticalStrut(8));
		infoPanel.add(buildInfoLabel("Taille du marche", marketSizeLabel));

		add(headerPanel, BorderLayout.NORTH);
		add(infoPanel, BorderLayout.CENTER);
		add(openRosterButton, BorderLayout.SOUTH);
		openRosterButton.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
	}

	private JPanel buildInfoLabel(String title, JLabel valueLabel) {
		JPanel row = new JPanel();
		row.setOpaque(false);
		row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		valueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		storeInfoTitleLabel(titleLabel);

		row.add(titleLabel);
		row.add(Box.createVerticalStrut(2));
		row.add(valueLabel);
		row.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
		return row;
	}

	private void storeInfoTitleLabel(JLabel titleLabel) {
		if (infoTitleLabels == null) {
			infoTitleLabels = new JLabel[10];
		}
		for (int i = 0; i < infoTitleLabels.length; i++) {
			if (infoTitleLabels[i] == null) {
				infoTitleLabels[i] = titleLabel;
				return;
			}
		}
	}

	public void updateTeam(Team team, boolean currentSeasonSelected) {
		if (team == null) {
			showEmptyState();
			return;
		}
		showTeamState(team, currentSeasonSelected);
	}

	private void showEmptyState() {
		teamLogoPanel.setTeamName("");
		teamNameLabel.setText("Aucune equipe");
		cityLabel.setText("-");
		conferenceLabel.setText("-");
		divisionLabel.setText("-");
		arenaLabel.setText("-");
		budgetLabel.setText("-");
		capacityLabel.setText("-");
		averageNoteLabel.setText("-");
		financialPolicyLabel.setText("-");
		marketSizeLabel.setText("-");
		openRosterButton.setEnabled(false);
		revalidate();
		repaint();
	}

	private void showTeamState(Team team, boolean currentSeasonSelected) {
		teamLogoPanel.setTeamName(team.getName());
		teamNameLabel.setText(team.getName());
		cityLabel.setText(TeamDisplayUtility.getCityName(team));
		conferenceLabel.setText(guiInterface.getConferenceName(team));
		divisionLabel.setText(guiInterface.getDivisionName(team));
		arenaLabel.setText(team.getStadium().getName());

		if (currentSeasonSelected) {
			budgetLabel.setText(PlayerDisplayUtil.formatSalary(team.getTeamFinance().getBudget().getRemainingAmount()));
			averageNoteLabel.setText(PlayerDisplayUtil.formatOneDecimal(team.getCurrentPopularity()) + "/100");
		} else {
			budgetLabel.setText(PlayerDisplayUtil.formatSalary(team.getTeamFinance().getBudget().getInitialAmount()));
			averageNoteLabel.setText(PlayerDisplayUtil.formatOneDecimal(
					team.getFormerPopularity())
					+ "/100");
		}

		capacityLabel.setText(String.valueOf(team.getStadium().getCapacity()));
		financialPolicyLabel.setText(guiInterface.getTeamFinancialPolicyLabel(team));
		marketSizeLabel.setText(guiInterface.getTeamMarketSizeLabel(team));
		openRosterButton.setEnabled(true);
		revalidate();
		repaint();
	}

	public JButton getOpenRosterButton() {
		return openRosterButton;
	}

	@Override
	public void applyTheme() {
		teamNameLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		cityLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		conferenceLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		divisionLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		arenaLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		budgetLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		capacityLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		averageNoteLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		openRosterButton.setBackground(DashboardPanelUtil.getPrimaryActionColor());
		openRosterButton.setForeground(DashboardPanelUtil.getPrimaryActionTextColor());
		if (infoTitleLabels != null) {
			for (int i = 0; i < infoTitleLabels.length; i++) {
				if (infoTitleLabels[i] != null) {
					infoTitleLabels[i].setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
				}
			}
		}
	}
}
