package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.team.Team;
import gui.panel.common.BuildBox;
import gui.panel.common.DashboardCard;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.PlayerDisplayUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.RoundedPanel;
import gui.panel.common.ThemeAware;
import gui.panel.mapPanel.effectifPanel.teamPanel.TeamLogoPanel;
import gui.panel.mapPanel.effectifPanel.teamPanel.TeamRosterPanel;
import process.orchestrator.interf.GUIInterface;

public class RosterDashboard extends JPanel implements ThemeAware {
	private static final int DASHBOARD_SPACING = 16;
	private static final Color BACKGROUND_COLOR = DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR;

	private Team selectedTeam;
	private GUIInterface guiInterface;
	private Runnable backToMapAction;
	private boolean currentSeasonSelected;

	private JButton backButton;
	private JButton currentSeasonButton;
	private JButton previousSeasonButton;
	private JLabel teamNameLabel;
	private JLabel subtitleLabel;
	private JPanel headerPanel;
	private TeamLogoPanel teamLogoPanel;
	private JLabel playersCountValueLabel;
	private JLabel payrollValueLabel;
	private JLabel averageNoteValueLabel;
	private JLabel averagePointsValueLabel;
	private TeamRosterPanel rosterPanel;

	public RosterDashboard(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		create();
		organize();
		actions();
		updateDashboard();
	}

	private void create() {
		currentSeasonSelected = true;
		backButton = new RoundedButton("Retour a la carte");
		currentSeasonButton = new RoundedButton("Saison actuelle");
		previousSeasonButton = new RoundedButton("Saison precedente");
		teamNameLabel = new JLabel("Effectif");
		subtitleLabel = new JLabel("-");
		teamLogoPanel = new TeamLogoPanel("", 56);
		teamLogoPanel.setTeamQueryInterface(guiInterface);
		playersCountValueLabel = new JLabel("-");
		payrollValueLabel = new JLabel("-");
		averageNoteValueLabel = new JLabel("-");
		averagePointsValueLabel = new JLabel("-");
		rosterPanel = new TeamRosterPanel();

		backButton.setFocusPainted(false);
		currentSeasonButton.setFocusPainted(false);
		previousSeasonButton.setFocusPainted(false);
		backButton.setAlignmentX(LEFT_ALIGNMENT);
		currentSeasonButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		previousSeasonButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		currentSeasonButton.setPreferredSize(new Dimension(170, 44));
		previousSeasonButton.setPreferredSize(new Dimension(170, 44));
		teamNameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		teamNameLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
		subtitleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		subtitleLabel.setAlignmentX(LEFT_ALIGNMENT);
	}

	private void organize() {
		setLayout(new BorderLayout());
		setBackground(BACKGROUND_COLOR);

		JPanel content = new JPanel(new BorderLayout(DASHBOARD_SPACING, DASHBOARD_SPACING));
		content.setOpaque(false);
		content.setBorder(BorderFactory.createEmptyBorder(
				DASHBOARD_SPACING, DASHBOARD_SPACING, DASHBOARD_SPACING, DASHBOARD_SPACING));

		content.add(buildHeader(), BorderLayout.NORTH);
		content.add(buildBody(), BorderLayout.CENTER);
		add(content, BorderLayout.CENTER);
	}

	private JPanel buildHeader() {
		headerPanel = new RoundedPanel(new BorderLayout(12, 0), 24);
		headerPanel.setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		headerPanel.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

		JPanel titlePanel = new JPanel();
		titlePanel.setOpaque(false);
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
		titlePanel.setAlignmentX(LEFT_ALIGNMENT);
		titlePanel.add(backButton);
		titlePanel.add(Box.createVerticalStrut(12));

		JPanel teamLine = new JPanel(new BorderLayout(12, 0));
		teamLine.setOpaque(false);
		teamLine.setAlignmentX(LEFT_ALIGNMENT);
		teamLine.add(teamLogoPanel, BorderLayout.WEST);
		teamLine.add(teamNameLabel, BorderLayout.CENTER);

		titlePanel.add(teamLine);
		titlePanel.add(Box.createVerticalStrut(6));
		titlePanel.add(subtitleLabel);

		headerPanel.add(titlePanel, BorderLayout.WEST);
		headerPanel.setPreferredSize(new java.awt.Dimension(420, 124));
		return headerPanel;
	}

	private JPanel buildBody() {
		JPanel body = new JPanel(new BorderLayout(0, DASHBOARD_SPACING));
		body.setOpaque(false);
		body.add(buildSeasonButtonsPanel(), BorderLayout.NORTH);
		body.add(buildCenterContentPanel(), BorderLayout.CENTER);
		return body;
	}

	private JPanel buildCenterContentPanel() {
		JPanel centerContentPanel = new JPanel(new BorderLayout(0, DASHBOARD_SPACING));
		centerContentPanel.setOpaque(false);
		centerContentPanel.add(buildSummaryPanel(), BorderLayout.NORTH);
		centerContentPanel.add(new BuildBox("LISTE DES JOUEURS", "Effectif complet", buildRosterContentPanel()),
				BorderLayout.CENTER);
		return centerContentPanel;
	}

	private JPanel buildSeasonButtonsPanel() {
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setOpaque(false);
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.add(currentSeasonButton);
		buttonsPanel.add(Box.createHorizontalStrut(8));
		buttonsPanel.add(previousSeasonButton);
		updateSeasonButtonsStyle();
		return buttonsPanel;
	}

	private JPanel buildSummaryPanel() {
		JPanel summaryPanel = new JPanel(new GridLayout(1, 4, DASHBOARD_SPACING, 0));
		summaryPanel.setOpaque(false);
		summaryPanel.add(buildMetricCard("Joueurs", playersCountValueLabel));
		summaryPanel.add(buildMetricCard("Masse salariale", payrollValueLabel));
		summaryPanel.add(buildMetricCard("Note moyenne", averageNoteValueLabel));
		summaryPanel.add(buildMetricCard("PPG moyen", averagePointsValueLabel));
		return summaryPanel;
	}

	private JPanel buildMetricCard(String title, JLabel valueLabel) {
		DashboardCard card = new DashboardCard();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		titleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		valueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
		valueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);

		card.add(titleLabel);
		card.add(Box.createVerticalStrut(4));
		card.add(valueLabel);
		return card;
	}

	private JPanel buildRosterContentPanel() {
		JPanel container = new JPanel(new BorderLayout());
		container.setOpaque(false);
		container.add(rosterPanel, BorderLayout.CENTER);
		return container;
	}

	private void actions() {
		backButton.addActionListener(new BackToMapListener());
		currentSeasonButton.addActionListener(new CurrentSeasonListener());
		previousSeasonButton.addActionListener(new PreviousSeasonListener());
	}

	public void setSelectedTeam(Team team) {
		selectedTeam = team;
		updateDashboard();
	}

	public void setBackToMapAction(Runnable backToMapAction) {
		this.backToMapAction = backToMapAction;
	}

	private void updateDashboard() {
		updateSeasonButtonsStyle();
		if (selectedTeam == null) {
			showEmptyState();
			return;
		}
		showTeamState();
	}

	private void showEmptyState() {
		teamLogoPanel.setTeamName("");
		teamNameLabel.setText("Effectif");
		subtitleLabel.setText("-");
		playersCountValueLabel.setText("-");
		payrollValueLabel.setText("-");
		averageNoteValueLabel.setText("-");
		averagePointsValueLabel.setText("-");
		rosterPanel.updateTeam(null, currentSeasonSelected);
	}

	private void showTeamState() {
		teamLogoPanel.setTeamName(selectedTeam.getName());
		teamNameLabel.setText(selectedTeam.getName());
		subtitleLabel.setText("Effectif complet");

		if (currentSeasonSelected) {
			playersCountValueLabel.setText(String.valueOf(selectedTeam.getCurrentPlayers().size()));
			payrollValueLabel.setText(PlayerDisplayUtil.formatSalary(selectedTeam.getTeamFinance().getCurrentPayroll()));
			averageNoteValueLabel
					.setText(PlayerDisplayUtil.formatOneDecimal(selectedTeam.getCurrentPopularity()) + "/100");
		} else {
			playersCountValueLabel.setText(String.valueOf(selectedTeam.getFormerPlayers().size()));
			payrollValueLabel.setText(PlayerDisplayUtil.formatSalary(selectedTeam.getTeamFinance().getFormerPayroll()));
			averageNoteValueLabel
					.setText(PlayerDisplayUtil.formatOneDecimal(
							selectedTeam.getFormerPopularity())
							+ "/100");
		}

		averagePointsValueLabel.setText(
				PlayerDisplayUtil.formatOneDecimal(
						guiInterface.getAveragePoints(selectedTeam, currentSeasonSelected)));

		rosterPanel.updateTeam(selectedTeam, currentSeasonSelected);
	}

	private void updateSeasonButtonsStyle() {
		Color activeBackground = DashboardPanelUtil.getPrimaryActionColor();
		Color inactiveBackground = DashboardPanelUtil.BUTTON_SURFACE_COLOR;
		Color activeForeground = DashboardPanelUtil.getPrimaryActionTextColor();
		Color inactiveForeground = DashboardPanelUtil.BUTTON_TEXT_COLOR;

		currentSeasonButton.setOpaque(false);
		previousSeasonButton.setOpaque(false);
		currentSeasonButton.setContentAreaFilled(false);
		previousSeasonButton.setContentAreaFilled(false);
		currentSeasonButton.setBorderPainted(false);
		previousSeasonButton.setBorderPainted(false);

		if (currentSeasonSelected) {
			currentSeasonButton.setBackground(activeBackground);
			currentSeasonButton.setForeground(activeForeground);
			previousSeasonButton.setBackground(inactiveBackground);
			previousSeasonButton.setForeground(inactiveForeground);
		} else {
			currentSeasonButton.setBackground(inactiveBackground);
			currentSeasonButton.setForeground(inactiveForeground);
			previousSeasonButton.setBackground(activeBackground);
			previousSeasonButton.setForeground(activeForeground);
		}
	}

	private class BackToMapListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (backToMapAction != null) {
				backToMapAction.run();
			}
		}
	}

	private class CurrentSeasonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			currentSeasonSelected = true;
			updateDashboard();
		}
	}

	private class PreviousSeasonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			currentSeasonSelected = false;
			updateDashboard();
		}
	}

	public void refreshSelectedTeam() {
		updateDashboard();
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
		if (headerPanel != null) {
			headerPanel.setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		}
		backButton.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
		backButton.setForeground(DashboardPanelUtil.BUTTON_TEXT_COLOR);
		teamNameLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		subtitleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		playersCountValueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		payrollValueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		averageNoteValueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		averagePointsValueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		updateSeasonButtonsStyle();
		DashboardPanelUtil.refreshChildrenTheme(this);
	}

}
