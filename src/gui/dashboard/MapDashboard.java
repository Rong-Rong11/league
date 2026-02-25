package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import gui.components.SectionTitle;
import gui.components.TitledCard;
/**
 * Dashboard dédié à la page Finance.
 */
public class MapDashboard extends JPanel {
	private static final int IDEAL_DASHBOARD_SPACING = 16;
	private static final int IDEAL_DASHBOARD_HEADER_HEIGHT = 50;
	private static final int IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH = 270;
	private static final int IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH = 340;
	private static final Color IDEAL_DASHBOARD_BACKGROUND_COLOR = new Color(247, 248, 250);

	public MapDashboard(){
		setLayout(new BorderLayout());
		setBackground(IDEAL_DASHBOARD_BACKGROUND_COLOR);

		JPanel content = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING));
		content.setOpaque(false);

		JPanel rightSpace = new JPanel();
		rightSpace.setPreferredSize(new Dimension(IDEAL_DASHBOARD_SPACING,0));
		rightSpace.setOpaque(false);

		JPanel leftSpace = new JPanel ();
		leftSpace.setPreferredSize(new Dimension(IDEAL_DASHBOARD_SPACING,0));
		leftSpace.setOpaque(false);

		JPanel bottomSpace = new JPanel();
		bottomSpace.setPreferredSize(new Dimension(0,IDEAL_DASHBOARD_SPACING));
		bottomSpace.setOpaque(false);
		
		add(leftSpace, BorderLayout.WEST);
		add(rightSpace, BorderLayout.EAST);
		add(content, BorderLayout.CENTER);
		add(bottomSpace, BorderLayout.SOUTH); 

		add(buildHeaderRow(),BorderLayout.NORTH);
	}

		private JPanel buildHeaderRow(){
		JPanel header = new SectionTitle("Carte des equipes", "Distribution geographique"); // dans le subtitle il faut mettre quelque choes qui change selon le jour selctionner 
		header.setPreferredSize(new Dimension(10, IDEAL_DASHBOARD_HEADER_HEIGHT));
		return header;
		}

}

