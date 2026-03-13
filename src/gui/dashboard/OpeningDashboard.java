package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import gui.panel.common.BuildBox;
import gui.panel.common.SectionTitle;

public class OpeningDashboard extends JPanel {

	private static final int IDEAL_DASHBOARD_SPACING = 16;
	private static final int IDEAL_DASHBOARD_HEADER_HEIGHT = 50;
	private static final int IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH = 420;
	private static final int IDEAL_DASHBOARD_TOP_CARD_HEIGHT = 220;
	private static final Color IDEAL_DASHBOARD_BACKGROUND_COLOR = new Color(247, 248, 250);

	private JButton continueButton;

	public OpeningDashboard() {
		setLayout(new BorderLayout());
		setBackground(IDEAL_DASHBOARD_BACKGROUND_COLOR);

		JPanel content = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING));
		content.setOpaque(false);
		content.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING));

		content.add(buildHeaderRow(), BorderLayout.NORTH);
		content.add(buildBody(), BorderLayout.CENTER);
		content.add(buildFooter(), BorderLayout.SOUTH);

		add(content, BorderLayout.CENTER);
	}

	private JPanel buildHeaderRow() {
		JPanel header = new SectionTitle(
			"Creation de la ligue",
			"Definissez les politiques financieres des equipes"
		);
		header.setPreferredSize(new Dimension(360, IDEAL_DASHBOARD_HEADER_HEIGHT));
		return header;
	}

	private JPanel buildBody() {
		JPanel body = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING));
		body.setOpaque(false);

		JPanel mapCard = new BuildBox(
			"LOCALISATION DES FRANCHISES",
			"Cliquez sur une ville",
			"CARTE"
		);

		JPanel rightColumn = buildRightColumn();
		rightColumn.setPreferredSize(new Dimension(IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH, 10));

		body.add(mapCard, BorderLayout.CENTER);
		body.add(rightColumn, BorderLayout.EAST);

		return body;
	}

	private JPanel buildRightColumn() {
		JPanel column = new JPanel(new BorderLayout(0, 12));
		column.setOpaque(false);

		JPanel topCard = new BuildBox(
			"EQUIPE SELECTIONNEE",
			"Equipe courante",
			"EQUIPE / POLITIQUE"
		);
		topCard.setPreferredSize(new Dimension(IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH, IDEAL_DASHBOARD_TOP_CARD_HEIGHT));

		JPanel bottomCard = new BuildBox(
			"POLITIQUE FINANCIERE",
			"Informations generales",
			"DETAILS"
		);

		column.add(topCard, BorderLayout.NORTH);
		column.add(bottomCard, BorderLayout.CENTER);

		return column;
	}

	private JPanel buildFooter() {
		JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		footer.setOpaque(false);

		continueButton = new JButton("Continuer");
		footer.add(continueButton);

		return footer;
	}

	public JButton getContinueButton() {
		return continueButton;
	}

	public boolean hasSelectedProfil() {
		return true;
	}

	public void showSelectionWarning() {
	}
}
