package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import gui.panel.common.BuildBox;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.SectionTitle;
import gui.panel.common.ThemeAware;
import gui.panel.rankingPanel.RankingPerformancePanel;
import gui.panel.rankingPanel.RankingTablePanel;
import process.orchestrator.GUIInterface;

/**
 * Dashboard dedie a la page Classement.
 */
public class RankingDashboard extends JPanel implements ThemeAware {
	private static final int IDEAL_DASHBOARD_SPACING = 16;
	private static final int IDEAL_DASHBOARD_HEADER_HEIGHT = 50;
	private static final int IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH = 250;
	private static final int IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH = 300;
	private static final Color IDEAL_DASHBOARD_BACKGROUND_COLOR = DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR;

	private GUIInterface guiInterface;
	private RankingTablePanel rankingTablePanel;
	private RankingPerformancePanel rankingPerformancePanel;

	public RankingDashboard(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		create();
		organize();
	}

	private void create() {
		rankingTablePanel = new RankingTablePanel(guiInterface);
		rankingPerformancePanel = new RankingPerformancePanel(guiInterface);
	}

	public void refreshRanking() {
		rankingTablePanel.refreshRanking();
		rankingPerformancePanel.refreshPerformance();
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
		return DashboardPanelUtil.createContentPanel(IDEAL_DASHBOARD_SPACING);
	}

	private JPanel buildHeader() {
		JPanel header = new SectionTitle("CLASSEMENT GÉNÉRAL", "");
		header.setPreferredSize(new Dimension(IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH, IDEAL_DASHBOARD_HEADER_HEIGHT));
		return header;
	}

	private JPanel buildBody() {
		JPanel body = DashboardPanelUtil.createBodyPanel(IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING);
		body.add(buildCenterColumn(), BorderLayout.CENTER);
		body.add(buildRightColumn(), BorderLayout.EAST);
		return body;
	}

	private JPanel buildCenterColumn() {
		return new BuildBox("", "", rankingTablePanel);
	}

	private JPanel buildRightColumn() {
		JPanel column = DashboardPanelUtil.createGridColumn(1, 1, 0, 12, IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH);
		column.add(new BuildBox("PERFORMANCES", "Forme récente", rankingPerformancePanel));

		return column;
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
		DashboardPanelUtil.refreshChildrenTheme(this);
	}
}
