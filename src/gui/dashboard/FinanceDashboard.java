package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import gui.panel.common.BuildBox;
import gui.panel.common.SectionTitle;

public class FinanceDashboard extends JPanel {

	private static final int IDEAL_DASHBOARD_SPACING = 16;
	private static final int IDEAL_DASHBOARD_HEADER_HEIGHT = 50;
	private static final int IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH = 270;
	private static final int IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH = 340;
	private static final Color IDEAL_DASHBOARD_BACKGROUND_COLOR = new Color(247, 248, 250);

	public FinanceDashboard() {
		organize();
	}

	private void organize() {
		setLayout(new BorderLayout());
		setBackground(IDEAL_DASHBOARD_BACKGROUND_COLOR);

		JPanel content = buildContentPanel();
		content.add(buildHeader(), BorderLayout.NORTH);
		content.add(buildBody(), BorderLayout.CENTER);
		add(content, BorderLayout.CENTER);
	}

	private JPanel buildContentPanel() {
		JPanel content = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING));
		content.setOpaque(false);
		content.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING));
		return content;
	}

	private JPanel buildHeader() {
		JPanel header = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, 0));
		header.setOpaque(false);

		JPanel leftHeader = new SectionTitle("FINANCE DE LA LIGUE", "Vue d'ensemble consolidée");
		leftHeader.setPreferredSize(new Dimension(IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH, IDEAL_DASHBOARD_HEADER_HEIGHT));

		JPanel centerHeader = new SectionTitle("FINANCE PAR ÉQUIPE", "Distribution par club");

		JPanel rightHeader = new SectionTitle("ANALYSE DÉTAILLÉE", "Équipe sélectionnée");
		rightHeader.setPreferredSize(new Dimension(IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH, IDEAL_DASHBOARD_HEADER_HEIGHT));

		header.add(leftHeader, BorderLayout.WEST);
		header.add(centerHeader, BorderLayout.CENTER);
		header.add(rightHeader, BorderLayout.EAST);

		return header;
	}

	private JPanel buildBody() {
		JPanel body = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, 0));
		body.setOpaque(false);
		body.add(buildLeftColumn(), BorderLayout.WEST);
		body.add(buildCenterColumn(), BorderLayout.CENTER);
		body.add(buildRightColumn(), BorderLayout.EAST);
		return body;
	}

	private JPanel buildLeftColumn() {
		JPanel column = new JPanel(new GridLayout(3, 1, 0, 12));
		column.setOpaque(false);
		column.setPreferredSize(new Dimension(IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH, 10));

		column.add(new BuildBox("REVENUS TOTAUX", "Synthèse ligue", "REVENUS"));//! À changer le string par un jpanel quand on aura la fonctionnalité
		column.add(new BuildBox("DÉPENSES TOTALES", "Synthèse ligue", "DÉPENSES"));//! À changer le string par un jpanel quand on aura la fonctionnalité
		column.add(new BuildBox("RÉSULTAT NET", "Synthèse ligue", "RÉSULTAT"));//! À changer le string par un jpanel quand on aura la fonctionnalité

		return column;
	}

	private JPanel buildCenterColumn() {
		return new BuildBox("DISTRIBUTION PAR CLUB", "Zone principale", "DISTRIBUTION");//! À changer le string par un jpanel quand on aura la fonctionnalité
	}

	private JPanel buildRightColumn() {
		JPanel column = new JPanel(new GridLayout(2, 1, 0, 12));
		column.setOpaque(false);
		column.setPreferredSize(new Dimension(IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH, 10));

		column.add(new BuildBox("DISTRIBUTION - ÉQUIPE", "Équipe sélectionnée", "DISTRIBUTION"));//! À changer le string par un jpanel quand on aura la fonctionnalité
		column.add(new BuildBox("DÉPENSES", "Équipe sélectionnée", "DÉPENSES"));//! À changer le string par un jpanel quand on aura la fonctionnalité

		return column;
	}
}
