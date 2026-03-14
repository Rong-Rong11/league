package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import config.CalendarConfiguration;
import data.league.RegularSeason;
import gui.components.BuildBox;
import gui.components.SectionTitle;

public class CalendarDashboard extends JPanel {

	private static final int IDEAL_DASHBOARD_SPACING = 16;
	private static final int IDEAL_DASHBOARD_HEADER_HEIGHT = 50;
	private static final int IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH = 270;
	private static final int IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH = 340;
	private static final Color IDEAL_DASHBOARD_BACKGROUND_COLOR = new Color(247, 248, 250);
	private static final Color PRIMARY_TEXT_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color SECONDARY_TEXT_COLOR = new Color(0x6D, 0x75, 0x83);
	private static final Color PROGRESS_BACKGROUND_COLOR = new Color(234, 237, 242);
	private static final Color PROGRESS_FOREGROUND_COLOR = new Color(0x14, 0x3A, 0x8C);

	public CalendarDashboard() {
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

	private JPanel buildRightColumn() {
		JPanel column = new JPanel(new GridLayout(2, 1, 0, 12));
		column.setOpaque(false);

		JPanel actionsCard = new BuildBox("ACTIONS RAPIDES", "", buildActionsPanel());
		JPanel infoCard = new BuildBox("INFORMATIONS SAISON", "", buildSeasonInfoPanel());

		column.add(actionsCard);
		column.add(infoCard);
		return column;
	}

	private JPanel buildSeasonProgressPanel() {
		RegularSeason regularSeason = new RegularSeason(
				CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE,
				CalendarConfiguration.REGULAR_SEASON_END_DATE);
		LocalDate currentDate = LocalDate.now();


		JPanel panel = new JPanel(new BorderLayout(12, 0));
		panel.setBackground(Color.WHITE);
		panel.setBorder(new EmptyBorder(12, 16, 16, 16));

		JPanel textPanel = new JPanel();
		textPanel.setOpaque(false);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

		JLabel daysLabel = new JLabel(" jours completes sur 0" );
		daysLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
		daysLabel.setForeground(SECONDARY_TEXT_COLOR);
		daysLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JProgressBar progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setForeground(PROGRESS_FOREGROUND_COLOR);
		progressBar.setBackground(PROGRESS_BACKGROUND_COLOR);
		progressBar.setBorderPainted(false);
		progressBar.setStringPainted(false);
		progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);

		textPanel.add(daysLabel);
		textPanel.add(Box.createVerticalStrut(16));
		textPanel.add(progressBar);

		JPanel valuePanel = new JPanel();
		valuePanel.setOpaque(false);
		valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.Y_AXIS));

		JLabel percentLabel = new JLabel(0 + "%");
		percentLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
		percentLabel.setForeground(PRIMARY_TEXT_COLOR);
		percentLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

		JLabel completionLabel = new JLabel("Completion");
		completionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
		completionLabel.setForeground(SECONDARY_TEXT_COLOR);
		completionLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

		valuePanel.add(percentLabel);
		valuePanel.add(Box.createVerticalStrut(4));
		valuePanel.add(completionLabel);

		panel.add(textPanel, BorderLayout.CENTER);
		panel.add(valuePanel, BorderLayout.EAST);
		return panel;
	}

	private JPanel buildMatchDaysPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(new EmptyBorder(12, 16, 16, 16));

		for (int i = 1; i <= 5; i++) {

			JLabel dayLabel = new JLabel("Jour de match " + i);
			dayLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
			dayLabel.setForeground(SECONDARY_TEXT_COLOR);
			dayLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

			panel.add(dayLabel);
			panel.add(Box.createVerticalStrut(8));
		}

		return panel;
	}

	private JPanel buildActionsPanel() {

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(new EmptyBorder(12, 16, 16, 16));

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
		panel.setBorder(new EmptyBorder(12, 16, 16, 16));
	
		JLabel gamesLabel = new JLabel("Matchs joués : 0");
		JLabel teamsLabel = new JLabel("Équipes : 30");
	
		gamesLabel.setForeground(SECONDARY_TEXT_COLOR);
		teamsLabel.setForeground(SECONDARY_TEXT_COLOR);
	
		panel.add(gamesLabel);
		panel.add(Box.createVerticalStrut(8));
		panel.add(teamsLabel);
	
		return panel;
	}
}
