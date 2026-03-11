package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import config.SimulationConfiguration;
import gui.panel.calendarPanel.CalendarSimulationPanel;
import gui.panel.calendarPanel.SeasonProgressBarPanel;
import gui.panel.common.BuildBox;
import gui.panel.common.SectionTitle;

public class CalendarDashboard extends JPanel {

	private static final int IDEAL_DASHBOARD_SPACING = 16;
	private static final int IDEAL_DASHBOARD_HEADER_HEIGHT = 50;
	private static final int IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH = 270;
	private static final int IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH = 340;
	private static final Color IDEAL_DASHBOARD_BACKGROUND_COLOR = new Color(247, 248, 250);
	private static final Color SECONDARY_TEXT_COLOR = new Color(0x6D, 0x75, 0x83);
	private final CalendarSimulationPanel calendarSimulationPanel;
	private final SeasonProgressBarPanel seasonProgressBarPanel;


	public CalendarDashboard() {
		calendarSimulationPanel = new CalendarSimulationPanel();
		seasonProgressBarPanel = new SeasonProgressBarPanel(
				SimulationConfiguration.REGULAR_SEASON_DEBUT_DATE,
				SimulationConfiguration.REGULAR_SEASON_END_DATE,
				SimulationConfiguration.REGULAR_SEASON_DEBUT_DATE);
		calendarSimulationPanel.setSeasonProgressBarPanel(seasonProgressBarPanel);

		setLayout(new BorderLayout());
		setBackground(IDEAL_DASHBOARD_BACKGROUND_COLOR);

		JPanel content = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING));
		content.setOpaque(false);

		JPanel leftSpace = new JPanel();
		leftSpace.setPreferredSize(new Dimension(IDEAL_DASHBOARD_SPACING, 0));
		leftSpace.setOpaque(false);

		JPanel rightSpace = new JPanel();
		rightSpace.setPreferredSize(new Dimension(IDEAL_DASHBOARD_SPACING, 0));
		rightSpace.setOpaque(false);

		JPanel bottomSpace = new JPanel();
		bottomSpace.setPreferredSize(new Dimension(0, IDEAL_DASHBOARD_SPACING));
		bottomSpace.setOpaque(false);

		add(leftSpace, BorderLayout.WEST);
		add(rightSpace, BorderLayout.EAST);
		add(bottomSpace, BorderLayout.SOUTH);
		add(content, BorderLayout.CENTER);

		content.add(buildHeader(), BorderLayout.NORTH);
		content.add(buildBody(), BorderLayout.CENTER);
	}

	public void startSeason() {
		calendarSimulationPanel.getLeagueManager().startSeason();
		calendarSimulationPanel.loadSeasonState();
	}

	private JPanel buildHeader() {
		JPanel header = new SectionTitle("CALENDRIER DE LA SAISON", "Saison régulière");
		header.setPreferredSize(new Dimension(IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH, IDEAL_DASHBOARD_HEADER_HEIGHT));
		return header;
	}

	private JPanel buildBody() {
		JPanel body = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING));
		body.setOpaque(false);

		JPanel leftColumn = buildLeftColumn();
		JPanel rightColumn = buildRightColumn();
		rightColumn.setPreferredSize(new Dimension(IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH, 10));

		body.add(leftColumn, BorderLayout.CENTER);
		body.add(rightColumn, BorderLayout.EAST);
		return body;
	}

	private JPanel buildLeftColumn() {
		JPanel column = new JPanel(new BorderLayout(0, 12));
		column.setOpaque(false);

		JPanel progressCard = new BuildBox("PROGRESSION DE LA SAISON", "", buildSeasonProgressPanel());
		progressCard.setPreferredSize(new Dimension(10, 110));

		JPanel matchDaysCard = new BuildBox("JOURS DE MATCH", "", buildMatchDaysPanel());

		column.add(progressCard, BorderLayout.NORTH);
		column.add(matchDaysCard, BorderLayout.CENTER);
		return column;
	}

	private JPanel buildSeasonProgressPanel() {
		return seasonProgressBarPanel;
	}

	private JPanel buildRightColumn() {
		JPanel column = new JPanel(new GridLayout(2, 1, 0, 12));
		column.setOpaque(false);

		JPanel actionsCard = new BuildBox("ACTIONS RAPIDES", "", buildActionsPanel());
		JPanel infoCard = new BuildBox("INFORMATIONS SAISON", "", buildSeasonInfoPanel());

		column.add(actionsCard);
		column.add(infoCard);
		return column;
	}


	private JPanel buildMatchDaysPanel() {
		return calendarSimulationPanel;
	}

	private JPanel buildActionsPanel() {

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(12, 16, 16, 16));

		JButton simulateDay = new JButton("Simuler un jour");
		JButton simulateWeek = new JButton("Simuler une semaine");

		panel.add(simulateDay);
		panel.add(Box.createVerticalStrut(10));
		panel.add(simulateWeek);

		return panel;
	}

	private JPanel buildSeasonInfoPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(12, 16, 16, 16));

		JLabel gamesLabel = new JLabel("Matchs joués : 0 (placeholder)");
		JLabel teamsLabel = new JLabel("Équipes : 30 (placeholder)");

		gamesLabel.setForeground(SECONDARY_TEXT_COLOR);
		teamsLabel.setForeground(SECONDARY_TEXT_COLOR);

		panel.add(gamesLabel);
		panel.add(Box.createVerticalStrut(8));
		panel.add(teamsLabel);

		return panel;
	}
}
