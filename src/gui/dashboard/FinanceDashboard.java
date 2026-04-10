package gui.dashboard;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.ThemeAware;
import gui.panel.financePanel.CompareFinanceViewPanel;
import gui.panel.financePanel.FinanceHeaderPanel;
import gui.panel.financePanel.LeagueFinanceViewPanel;
import gui.panel.financePanel.TeamFinanceViewPanel;
import process.orchestrator.GUIInterface;

public class FinanceDashboard extends JPanel implements ThemeAware {

	private static final int DASHBOARD_SPACING = 16;
	private static final String LEAGUE_VIEW = "league";
	private static final String TEAM_VIEW = "team";
	private static final String COMPARE_VIEW = "compare";

	private final FinanceHeaderPanel headerPanel;
	private final JPanel centerContentPanel;
	private final LeagueFinanceViewPanel leagueViewPanel;
	private final TeamFinanceViewPanel teamViewPanel;
	private final CompareFinanceViewPanel compareViewPanel;

	private String selectedView;

	public FinanceDashboard(GUIInterface guiInterface) {
		selectedView = LEAGUE_VIEW;
		headerPanel = new FinanceHeaderPanel();
		centerContentPanel = new JPanel(new BorderLayout());
		centerContentPanel.setOpaque(false);
		leagueViewPanel = new LeagueFinanceViewPanel(guiInterface);
		teamViewPanel = new TeamFinanceViewPanel(guiInterface);
		compareViewPanel = new CompareFinanceViewPanel(guiInterface);

		organize();
		actions();
		refreshData();
	}

	private void organize() {
		setLayout(new BorderLayout());
		setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);

		JPanel content = DashboardPanelUtil.createContentPanel(DASHBOARD_SPACING);
		content.add(headerPanel, BorderLayout.NORTH);
		content.add(centerContentPanel, BorderLayout.CENTER);
		add(content, BorderLayout.CENTER);
	}

	private void actions() {
		headerPanel.getLeagueButton().addActionListener(e -> switchView(LEAGUE_VIEW));
		headerPanel.getTeamsButton().addActionListener(e -> switchView(TEAM_VIEW));
		headerPanel.getCompareButton().addActionListener(e -> switchView(COMPARE_VIEW));
	}

	private void switchView(String view) {
		if (view == null || view.equals(selectedView)) {
			return;
		}
		selectedView = view;
		refreshView();
	}

	public void refreshData() {
		leagueViewPanel.refreshData();
		teamViewPanel.refreshData();
		compareViewPanel.refreshData();
		refreshView();
	}

	private void refreshView() {
		headerPanel.setSelectedView(selectedView);
		centerContentPanel.removeAll();
		JPanel currentViewPanel = getCurrentViewPanel();
		centerContentPanel.add(currentViewPanel, BorderLayout.CENTER);
		DashboardPanelUtil.refreshTheme(currentViewPanel);
		centerContentPanel.revalidate();
		centerContentPanel.repaint();
	}

	private JPanel getCurrentViewPanel() {
		if (TEAM_VIEW.equals(selectedView)) {
			return teamViewPanel;
		}
		if (COMPARE_VIEW.equals(selectedView)) {
			return compareViewPanel;
		}
		return leagueViewPanel;
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
		headerPanel.applyTheme();
		leagueViewPanel.applyTheme();
		teamViewPanel.applyTheme();
		compareViewPanel.applyTheme();
		refreshData();
		DashboardPanelUtil.refreshChildrenTheme(this);
	}
}
