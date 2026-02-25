package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import gui.components.SectionTitle;
import gui.components.TitledCard;

public class MatchDashboard extends JPanel {

	private static final int IDEAL_DASHBOARD_SPACING = 16;
	private static final int IDEAL_DASHBOARD_HEADER_HEIGHT = 50;
	private static final int IDEAL_DASHBOARD_SIDE_COLUMN_WIDTH = 300;
	private static final Color IDEAL_DASHBOARD_BACKGROUND_COLOR = new Color(247, 248, 250);

	public MatchDashboard() {
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
		add(bottomSpace,BorderLayout.SOUTH) ;
		add(content, BorderLayout.CENTER);

		content.add(buildHeader(), BorderLayout.NORTH);
		content.add(buildBody(), BorderLayout.CENTER);
	}

	private JPanel buildHeader() {
		JPanel header = new SectionTitle("SAISON RÉGULIÈRE", ""); // dans le subtitle il faut mettre quelque choes qui change selon le jour selctionner 
		header.setPreferredSize(new Dimension( IDEAL_DASHBOARD_HEADER_HEIGHT,0));
		return header;
	}

	private JPanel buildBody() {
		JPanel body = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING));
		body.setOpaque(false);

		JPanel leftCard = new TitledCard("MATCHS DU JOUR", "Liste des rencontres");
		leftCard.setPreferredSize(new Dimension(IDEAL_DASHBOARD_SIDE_COLUMN_WIDTH, 10));

		JPanel centerCard = new TitledCard("MATCH SÉLECTIONNÉ", "Détails du match");

		JPanel rightCard = new TitledCard("FINANCES DU MATCH", "Revenus et dépenses");
		rightCard.setPreferredSize(new Dimension(IDEAL_DASHBOARD_SIDE_COLUMN_WIDTH, 10));

		body.add(leftCard, BorderLayout.WEST);
		body.add(centerCard, BorderLayout.CENTER);
		body.add(rightCard, BorderLayout.EAST);

		return body;
	}
}