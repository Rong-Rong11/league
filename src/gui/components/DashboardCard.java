package gui.components;

import java.awt.Color;

import javax.swing.JPanel;

/**
 * Composant carte simple réutilisable pour les dashboards.
 */
public class DashboardCard extends JPanel {

	private static final Color CARD_BACKGROUND = Color.WHITE;

	public DashboardCard() {
		setBackground(CARD_BACKGROUND);
		setOpaque(true);
	}
}
