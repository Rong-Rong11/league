package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.ThemeAware;
import gui.panel.seasonEndPanel.SeasonEndDataProvider;
import gui.panel.seasonEndPanel.SeasonEndFinancePanel;
import gui.panel.seasonEndPanel.SeasonEndHeaderPanel;
import gui.panel.seasonEndPanel.SeasonEndOverviewPanel;
import gui.panel.seasonEndPanel.SeasonEndProfilePanel;
import process.orchestrator.interfaces.GUIInterface;

public class SeasonEndDashboard extends JPanel implements RefreshableDashboard, ThemeAware {
	private static final String OVERVIEW_PAGE = "overview";
	private static final String FINANCE_PAGE = "finance";
	private static final String PROFILE_PAGE = "profile";

	private final GUIInterface guiInterface;
	private final CardLayout pageLayout;
	private final JPanel pagePanel;
	private final SeasonEndDataProvider dataProvider;

	private SeasonEndHeaderPanel headerPanel;
	private JButton reviewRankingButton;
	private JButton openFinanceButton;
	private String selectedPage;

	public SeasonEndDashboard(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		this.dataProvider = new SeasonEndDataProvider(guiInterface);
		pageLayout = new CardLayout();
		pagePanel = new JPanel(pageLayout);
		selectedPage = OVERVIEW_PAGE;
		create();
		organize();
	}

	private void create() {
		headerPanel = new SeasonEndHeaderPanel(dataProvider);
		reviewRankingButton = new RoundedButton("Revoir le classement");
		openFinanceButton = new RoundedButton("Voir finances");
		actions();
		applyButtonStyle();
	}

	private void organize() {
		removeAll();
		setLayout(new BorderLayout(0, 10));
		setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
		setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

		add(headerPanel, BorderLayout.NORTH);
		add(buildBody(), BorderLayout.CENTER);
		add(buildFooter(), BorderLayout.SOUTH);
		refreshPageContent();
	}

	private JPanel buildBody() {
		JPanel body = new JPanel(new BorderLayout());
		body.setOpaque(false);
		pagePanel.setOpaque(false);
		body.add(pagePanel, BorderLayout.CENTER);
		return body;
	}

	private JPanel buildFooter() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
		panel.setOpaque(false);
		panel.setPreferredSize(new Dimension(0, 48));
		panel.add(reviewRankingButton);
		panel.add(openFinanceButton);
		return panel;
	}

	private void refreshPageContent() {
		headerPanel.setSelectedPage(selectedPage);
		pagePanel.removeAll();
		pagePanel.add(new SeasonEndOverviewPanel(dataProvider), OVERVIEW_PAGE);
		pagePanel.add(new SeasonEndFinancePanel(dataProvider), FINANCE_PAGE);
		pagePanel.add(new SeasonEndProfilePanel(dataProvider), PROFILE_PAGE);
		pageLayout.show(pagePanel, selectedPage);
		pagePanel.revalidate();
		pagePanel.repaint();
	}

	private void actions() {
		headerPanel.getOverviewButton().addActionListener(new SwitchPageAction(OVERVIEW_PAGE));
		headerPanel.getFinanceButton().addActionListener(new SwitchPageAction(FINANCE_PAGE));
		headerPanel.getProfileButton().addActionListener(new SwitchPageAction(PROFILE_PAGE));
	}

	private void styleButton(JButton button, Color background, Color foreground) {
		button.setPreferredSize(new Dimension(230, 42));
		button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		button.setBackground(background);
		button.setForeground(foreground);
		button.setFocusPainted(false);
	}

	private void applyButtonStyle() {
		styleButton(reviewRankingButton, DashboardPanelUtil.BUTTON_SURFACE_COLOR, DashboardPanelUtil.BUTTON_TEXT_COLOR);
		styleButton(openFinanceButton, DashboardPanelUtil.getPrimaryActionColor(),
				DashboardPanelUtil.getPrimaryActionTextColor());
	}

	public JButton getReviewRankingButton() {
		return reviewRankingButton;
	}

	public JButton getOpenFinanceButton() {
		return openFinanceButton;
	}

	@Override
	public void refresh() {
		headerPanel.refresh();
		applyButtonStyle();
		organize();
		revalidate();
		repaint();
	}

	@Override
	public void applyTheme() {
		refresh();
	}

	private class SwitchPageAction implements ActionListener {
		private final String page;

		private SwitchPageAction(String page) {
			this.page = page;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			selectedPage = page;
			refreshPageContent();
		}
	}
}
