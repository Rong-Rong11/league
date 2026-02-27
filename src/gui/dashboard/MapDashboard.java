package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import gui.components.BuildBox;
import gui.components.SectionTitle;
/**
 * Dashboard dédié à la page Carte.
 */
	public class MapDashboard extends JPanel {
		private static final int IDEAL_DASHBOARD_SPACING = 16;
		private static final int IDEAL_DASHBOARD_HEADER_HEIGHT = 50;
		private static final int IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH = 270;
		private static final Color IDEAL_DASHBOARD_BACKGROUND_COLOR = new Color(247, 248, 250);

		public MapDashboard() {
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

		content.add(buildHeaderRow(), BorderLayout.NORTH);
		content.add(buildBody(), BorderLayout.CENTER);

		add(content, BorderLayout.CENTER);
	}

		private JPanel buildHeaderRow(){
			JPanel header = new SectionTitle("Carte des equipes", "Distribution geographique"); // dans le subtitle il faut mettre quelque choes qui change selon le jour selctionner 
			header.setPreferredSize(new Dimension(IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH, IDEAL_DASHBOARD_HEADER_HEIGHT));
			return header;
		}

		private JPanel buildBody() {
			JPanel body = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING));
			body.setOpaque(false);

			JPanel teamSelctCard = buildTeamSelctCard();

			JPanel mapCard = new BuildBox("LOCALISATION DES FRANCHISES", "", "CARTE");//! À changer le string par un jpanel quand on aura la fonctionnalité

			body.add(mapCard, BorderLayout.CENTER);
			body.add(teamSelctCard,BorderLayout.EAST);


			return body;
		}

		private JPanel buildTeamSelctCard(){
			JPanel column = new JPanel(new GridLayout(2, 1, 0, 12));
			column.setOpaque(false);
			
			column.add(new BuildBox("Détails de l'équipe", "Informations détaillées sur l'équipe sélectionnée", "INFOS ÉQUIPE"));//! À changer le string par un jpanel quand on aura la fonctionnalité
			column.add(new BuildBox("Joueur de l'équipe", "", "JOUEURS"));//! À changer le string par un jpanel quand on aura la fonctionnalité

			return column;
		}

}
