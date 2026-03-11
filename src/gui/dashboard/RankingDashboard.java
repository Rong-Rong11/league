package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import gui.panel.common.BuildBox;
import gui.panel.common.SectionTitle;

/**
 * Dashboard dédié à la page Classement.
 */
public class RankingDashboard extends JPanel {

	private static final int IDEAL_DASHBOARD_SPACING = 16;
	private static final int IDEAL_DASHBOARD_HEADER_HEIGHT = 50;
	private static final int IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH = 340;
	private static final int IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH = 300;
	private static final Color IDEAL_DASHBOARD_BACKGROUND_COLOR = new Color(247, 248, 250);

	public RankingDashboard() {
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
		JPanel header = new SectionTitle("CLASSEMENT GÉNÉRAL", "Conférence Est - Saison régulière");
		header.setPreferredSize(new Dimension(IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH, IDEAL_DASHBOARD_HEADER_HEIGHT));
		return header;
	}

	private JPanel buildBody() {
		JPanel body = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING));
		body.setOpaque(false);

		JPanel rankingCard = new BuildBox("CLASSEMENT COMPLET", "12 équipes", "TABLEAU CLASSEMENT");//! À changer le string par un jpanel quand on aura la fonctionnalité
		JPanel sideColumn = buildSideColumn();
		sideColumn.setPreferredSize(new Dimension(IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH, 10));

		body.add(rankingCard, BorderLayout.CENTER);
		body.add(sideColumn, BorderLayout.EAST);

		return body;
	}

	private JPanel buildSideColumn() {
		JPanel column = new JPanel(new GridLayout(2, 1, 0, 12));
		column.setOpaque(false);

		column.add(new BuildBox("ZONE PLAYOFFS", "Équipes qualifiées", "PLAYOFFS"));//! À changer le string par un jpanel quand on aura la fonctionnalité
		column.add(new BuildBox("PERFORMANCES", "Forme récente", "STATISTIQUES"));//! À changer le string par un jpanel quand on aura la fonctionnalité

		return column;
	}
}
