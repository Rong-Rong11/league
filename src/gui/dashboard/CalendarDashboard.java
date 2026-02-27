package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import gui.components.BuildBox;
import gui.components.SectionTitle;

public class CalendarDashboard extends JPanel {

	private static final int IDEAL_DASHBOARD_SPACING = 16;
	private static final int IDEAL_DASHBOARD_HEADER_HEIGHT = 50;
	private static final int IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH = 270;
	private static final int IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH = 340;
	private static final Color IDEAL_DASHBOARD_BACKGROUND_COLOR = new Color(247, 248, 250);

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

		JPanel progressCard = new BuildBox("PROGRESSION DE LA SAISON", "", "BARRE DE PROGRESSION");
		progressCard.setPreferredSize(new Dimension(10, 110));

		JPanel matchDaysCard = new BuildBox("JOURS DE MATCH", "", "LISTE DES JOURS");

		column.add(progressCard, BorderLayout.NORTH);
		column.add(matchDaysCard, BorderLayout.CENTER);
		return column;
	}

	private JPanel buildRightColumn() {
		JPanel column = new JPanel(new GridLayout(2, 1, 0, 12));
		column.setOpaque(false);

		JPanel actionsCard = new BuildBox("ACTIONS RAPIDES", "", "BOUTONS D'ACTION");
		JPanel infoCard = new BuildBox("INFORMATIONS SAISON", "", "STATISTIQUES SAISON");

		column.add(actionsCard);
		column.add(infoCard);
		return column;
	}
}
