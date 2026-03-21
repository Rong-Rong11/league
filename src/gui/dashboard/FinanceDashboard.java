package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import gui.panel.common.BuildBox;
import gui.panel.financePanel.FinanceHeaderPanel;

public class FinanceDashboard extends JPanel {

	private static final int IDEAL_DASHBOARD_SPACING = 16;
	private static final int IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH = 340;
	private static final Color IDEAL_DASHBOARD_BACKGROUND_COLOR = new Color(247, 248, 250);
	private static final String LEAGUE_VIEW = "league";
	private static final String TEAM_VIEW = "team";

	private FinanceHeaderPanel headerPanel;
	private JPanel centerContentPanel;
	private String selectedView;

	public FinanceDashboard() {
		selectedView = LEAGUE_VIEW;
		create();
		organize();
		actions();
		refreshView();
	}

	private void create() {
		headerPanel = new FinanceHeaderPanel();
		centerContentPanel = new JPanel(new BorderLayout());
		centerContentPanel.setOpaque(false);
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
		return headerPanel;
	}

	private JPanel buildBody() {
		JPanel body = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, 0));
		body.setOpaque(false);
		body.add(buildCenterColumn(), BorderLayout.CENTER);
		body.add(buildRightColumn(), BorderLayout.EAST);
		return body;
	}

	private JPanel buildCenterColumn() {
		JPanel centerColumn = new JPanel(new BorderLayout());
		centerColumn.setOpaque(false);
		centerColumn.add(centerContentPanel, BorderLayout.CENTER);
		return centerColumn;
	}

	private JPanel buildRightColumn() {
		JPanel column = new JPanel(new GridLayout(2, 1, 0, 12));
		column.setOpaque(false);
		column.setPreferredSize(new Dimension(IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH, 10));

		column.add(new BuildBox("DISTRIBUTION - ÉQUIPE", "Équipe sélectionnée", "DISTRIBUTION"));//! À changer le string par un jpanel quand on aura la fonctionnalité
		column.add(new BuildBox("DÉPENSES", "Équipe sélectionnée", "DÉPENSES"));//! À changer le string par un jpanel quand on aura la fonctionnalité

		return column;
	}

	private void actions() {
		headerPanel.getLeagueButton().addActionListener(new ShowLeagueViewAction());
		headerPanel.getTeamsButton().addActionListener(new ShowTeamViewAction());
	}

	private void switchView(String view) {
		if (view == null || view.equals(selectedView)) {
			return;
		}
		selectedView = view;
		refreshView();
	}

	private void refreshView() {
		headerPanel.setSelectedView(selectedView);
		centerContentPanel.removeAll();
		centerContentPanel.add(buildMainContentPanel(), BorderLayout.CENTER);
		centerContentPanel.revalidate();
		centerContentPanel.repaint();
		repaint();
	}

	private JPanel buildMainContentPanel() {
		if (LEAGUE_VIEW.equals(selectedView)) {
			return new BuildBox("FINANCE DE LA LIGUE", "Vue consolidee", "LIGUE");
		}
		return new BuildBox("DISTRIBUTION PAR CLUB", "Vue par equipe", "DISTRIBUTION");
	}

	private class ShowLeagueViewAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			switchView(LEAGUE_VIEW);
		}
	}

	private class ShowTeamViewAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			switchView(TEAM_VIEW);
		}
	}
}
