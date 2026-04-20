package gui.layout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gui.layout.strategy.ButtonHighlightStrategy;
import gui.layout.strategy.SidebarHighlightStrategy;
import gui.panel.common.ButtonStyleUtil;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.LabelStyleUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.ThemeAware;

public class SidebarPanel extends JPanel implements ThemeAware {
	private JButton matchButton = new RoundedButton("Match");
	private JButton calendarButton = new RoundedButton("Calendrier");
	private JButton rankingButton = new RoundedButton("Classement");
	private JButton financeButton = new RoundedButton("Finance");
	private JButton mapButton = new RoundedButton("Carte");
	private JButton themeButton = new RoundedButton("Mode sombre");
	private JButton exitButton = new RoundedButton("Quitter");
	private JLabel titleLabel;
	private JLabel subtitleLabel;
	private Map<String, SidebarHighlightStrategy> highlightStrategies = new HashMap<String, SidebarHighlightStrategy>();
	private JButton[] menuButtons;
	private String activeSection;
	private JPanel topSectionPanel;
	private JPanel menuSectionPanel;
	private JPanel bottomSectionPanel;

	public SidebarPanel() {
		create();
		organize();
	}

	private void create() {
		menuButtons = new JButton[] { matchButton, calendarButton, rankingButton, financeButton, mapButton };
		applySidebarIcons();
		initializeHighlightStrategies();
		applyTheme();
	}

	private void organize() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(300, 0));
		setBackground(DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR);

		add(buildTopSection(), BorderLayout.NORTH);
		add(buildMenuSection(), BorderLayout.CENTER);
		add(buildBottomSection(), BorderLayout.SOUTH);
	}

	private JPanel buildTopSection() {
		topSectionPanel = new JPanel();
		JPanel panel = topSectionPanel;
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel logoLabel = new JLabel();
		logoLabel.setAlignmentX(CENTER_ALIGNMENT);

		titleLabel = new JLabel("NBA League");
		LabelStyleUtil.styleTitleLabel(titleLabel, 30);
		titleLabel.setAlignmentX(CENTER_ALIGNMENT);

		subtitleLabel = new JLabel("Management");
		LabelStyleUtil.styleSubtitleLabel(subtitleLabel, 18);
		subtitleLabel.setAlignmentX(CENTER_ALIGNMENT);

		panel.add(logoLabel);
		panel.add(Box.createVerticalStrut(10));
		panel.add(titleLabel);
		panel.add(subtitleLabel);

		return panel;
	}

	private JPanel buildMenuSection() {
		menuSectionPanel = new JPanel();
		JPanel panel = menuSectionPanel;
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR);

		configureMenuButton(matchButton);
		configureMenuButton(calendarButton);
		configureMenuButton(rankingButton);
		configureMenuButton(financeButton);
		configureMenuButton(mapButton);

		matchButton.addActionListener(new HighlightAction(matchButton));
		calendarButton.addActionListener(new HighlightAction(calendarButton));
		rankingButton.addActionListener(new HighlightAction(rankingButton));
		financeButton.addActionListener(new HighlightAction(financeButton));
		mapButton.addActionListener(new HighlightAction(mapButton));

		highlightActiveButton(matchButton);

		panel.add(matchButton);
		panel.add(calendarButton);
		panel.add(rankingButton);
		panel.add(financeButton);
		panel.add(mapButton);

		panel.add(Box.createVerticalGlue());

		return panel;
	}

	private JPanel buildBottomSection() {
		bottomSectionPanel = new JPanel(new BorderLayout());
		JPanel panel = bottomSectionPanel;
		panel.setBackground(DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR);

		configureMenuButton(themeButton);
		configureMenuButton(exitButton);
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setOpaque(false);
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		buttonsPanel.add(themeButton);
		buttonsPanel.add(Box.createVerticalStrut(8));
		buttonsPanel.add(exitButton);
		panel.add(buttonsPanel, BorderLayout.SOUTH);

		return panel;
	}

	private void configureMenuButton(JButton button) {
		ButtonStyleUtil.styleMenuButton(button, 260, 74, 22);
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setHorizontalTextPosition(SwingConstants.RIGHT);
		button.setIconTextGap(12);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(false);
		button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 74));
		button.setMargin(new Insets(0, 28, 0, 14));
	}

	private void applySidebarIcons() {
		matchButton.setIcon(loadSidebarIcon("resources/logo_dash/match.png"));
		calendarButton.setIcon(loadSidebarIcon("resources/logo_dash/calendar.png"));
		rankingButton.setIcon(loadSidebarIcon("resources/logo_dash/classement.png"));
		financeButton.setIcon(loadSidebarIcon("resources/logo_dash/finance.png"));
		mapButton.setIcon(loadSidebarIcon("resources/logo_dash/map.png"));
		themeButton.setIcon(loadSidebarIcon(
				DashboardPanelUtil.isDarkMode() ? "resources/logo_dash/sun_mode_icon.png"
						: "resources/logo_dash/moon_mode_icon.png"));
		exitButton.setIcon(loadSidebarIcon(
				DashboardPanelUtil.isDarkMode() ? "resources/logo_dash/exit_dark.png"
						: "resources/logo_dash/exit_light.png"));
	}

	private ImageIcon loadSidebarIcon(String path) {
		return loadSidebarIcon(path, 28);
	}

	private ImageIcon loadSidebarIcon(String path, int size) {
		if (path == null) {
			return null;
		}
		ImageIcon icon = new ImageIcon(path);
		if (icon.getIconWidth() <= 0) {
			return null;
		}
		Image scaledImage = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
		return new ImageIcon(scaledImage);
	}

	private class HighlightAction implements ActionListener {
		private JButton button;

		public HighlightAction(JButton button) {
			this.button = button;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			highlightActiveButton(button);
		}
	}

	private void initializeHighlightStrategies() {
		highlightStrategies.put("match", new ButtonHighlightStrategy(
				matchButton, menuButtons, DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR, DashboardPanelUtil.getNavigationButtonColor(),
				DashboardPanelUtil.SIDEBAR_TEXT_COLOR, DashboardPanelUtil.getPrimaryActionTextColor()));
		highlightStrategies.put("calendar", new ButtonHighlightStrategy(
				calendarButton, menuButtons, DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR, DashboardPanelUtil.getNavigationButtonColor(),
				DashboardPanelUtil.SIDEBAR_TEXT_COLOR, DashboardPanelUtil.getPrimaryActionTextColor()));
		highlightStrategies.put("ranking", new ButtonHighlightStrategy(
				rankingButton, menuButtons, DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR, DashboardPanelUtil.getNavigationButtonColor(),
				DashboardPanelUtil.SIDEBAR_TEXT_COLOR, DashboardPanelUtil.getPrimaryActionTextColor()));
		highlightStrategies.put("finance", new ButtonHighlightStrategy(
				financeButton, menuButtons, DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR, DashboardPanelUtil.getNavigationButtonColor(),
				DashboardPanelUtil.SIDEBAR_TEXT_COLOR, DashboardPanelUtil.getPrimaryActionTextColor()));
		highlightStrategies.put("map", new ButtonHighlightStrategy(
				mapButton, menuButtons, DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR, DashboardPanelUtil.getNavigationButtonColor(),
				DashboardPanelUtil.SIDEBAR_TEXT_COLOR, DashboardPanelUtil.getPrimaryActionTextColor()));
	}

	private void highlightActiveButton(JButton activeButton) {
		JButton[] buttons = {
				matchButton,
				calendarButton,
				rankingButton,
				financeButton,
				mapButton
		};

		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setBackground(DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR);
			buttons[i].setForeground(DashboardPanelUtil.SIDEBAR_TEXT_COLOR);
		}

		activeButton.setBackground(DashboardPanelUtil.getNavigationButtonColor());
		activeButton.setForeground(DashboardPanelUtil.getPrimaryActionTextColor());
	}

	public void setActiveSection(String sectionName) {
		activeSection = sectionName;
		SidebarHighlightStrategy strategy = highlightStrategies.get(sectionName);
		if (strategy != null) {
			strategy.highlight();
		}
	}

	public JButton getThemeButton() {
		return themeButton;
	}

	public JButton getMatchButton() {
		return matchButton;
	}

	public JButton getCalendarButton() {
		return calendarButton;
	}

	public JButton getRankingButton() {
		return rankingButton;
	}

	public JButton getFinanceButton() {
		return financeButton;
	}

	public JButton getMapButton() {
		return mapButton;
	}

	public JButton getExitButton() {
		return exitButton;
	}

	public String getActiveSection() {
		return activeSection;
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR);
		if (topSectionPanel != null) {
			topSectionPanel.setBackground(DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR);
		}
		if (menuSectionPanel != null) {
			menuSectionPanel.setBackground(DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR);
		}
		if (bottomSectionPanel != null) {
			bottomSectionPanel.setBackground(DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR);
		}
		applySidebarIcons();
		if (titleLabel != null) {
			LabelStyleUtil.styleTitleLabel(titleLabel, 30);
			titleLabel.setAlignmentX(CENTER_ALIGNMENT);
		}
		if (subtitleLabel != null) {
			LabelStyleUtil.styleSubtitleLabel(subtitleLabel, 18);
			subtitleLabel.setAlignmentX(CENTER_ALIGNMENT);
		}
		themeButton.setText(DashboardPanelUtil.isDarkMode() ? "Mode clair" : "Mode sombre");
		themeButton.setBackground(DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR);
		themeButton.setForeground(DashboardPanelUtil.SIDEBAR_TEXT_COLOR);
		exitButton.setBackground(DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR);
		exitButton.setForeground(DashboardPanelUtil.SIDEBAR_TEXT_COLOR);
		initializeHighlightStrategies();
		if (activeSection != null) {
			setActiveSection(activeSection);
		} else {
			highlightActiveButton(matchButton);
		}
		revalidate();
		repaint();
	}
}
