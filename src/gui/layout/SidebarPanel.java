package gui.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
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
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.ThemeAware;

public class SidebarPanel extends JPanel implements ThemeAware {
	private static final Color ACTIVE_BUTTON_BACKGROUND_COLOR = new Color(0x17, 0x31, 0x74);

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

	public SidebarPanel() {
		create();
		organize();
	}

	private void create() {
		menuButtons = new JButton[] { matchButton, calendarButton, rankingButton, financeButton, mapButton };
		initializeHighlightStrategies();
		applyTheme();
	}

	private void organize() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(240, 0));
		setBackground(DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR);

		add(buildTopSection(), BorderLayout.NORTH);
		add(buildMenuSection(), BorderLayout.CENTER);
		add(buildBottomSection(), BorderLayout.SOUTH);
	}

	private JPanel buildTopSection() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		ImageIcon logoIcon = new ImageIcon("img/logo.png");
		JLabel logoLabel = new JLabel(logoIcon);
		logoLabel.setAlignmentX(CENTER_ALIGNMENT);

		titleLabel = new JLabel("NBA League");
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		titleLabel.setAlignmentX(CENTER_ALIGNMENT);

		subtitleLabel = new JLabel("Management");
		subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
		subtitleLabel.setAlignmentX(CENTER_ALIGNMENT);

		panel.add(logoLabel);
		panel.add(Box.createVerticalStrut(10));
		panel.add(titleLabel);
		panel.add(subtitleLabel);

		return panel;
	}

	private JPanel buildMenuSection() {
		JPanel panel = new JPanel();
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
		JPanel panel = new JPanel(new BorderLayout());
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
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(false);

		button.setBackground(DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR);
		button.setForeground(DashboardPanelUtil.SIDEBAR_TEXT_COLOR);
		button.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

		button.setPreferredSize(new Dimension(200, 50));
		button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		button.setMargin(new Insets(0, 20, 0, 10));
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
				matchButton, menuButtons, DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR, ACTIVE_BUTTON_BACKGROUND_COLOR,
				DashboardPanelUtil.SIDEBAR_TEXT_COLOR, Color.WHITE));
		highlightStrategies.put("calendar", new ButtonHighlightStrategy(
				calendarButton, menuButtons, DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR, ACTIVE_BUTTON_BACKGROUND_COLOR,
				DashboardPanelUtil.SIDEBAR_TEXT_COLOR, Color.WHITE));
		highlightStrategies.put("ranking", new ButtonHighlightStrategy(
				rankingButton, menuButtons, DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR, ACTIVE_BUTTON_BACKGROUND_COLOR,
				DashboardPanelUtil.SIDEBAR_TEXT_COLOR, Color.WHITE));
		highlightStrategies.put("finance", new ButtonHighlightStrategy(
				financeButton, menuButtons, DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR, ACTIVE_BUTTON_BACKGROUND_COLOR,
				DashboardPanelUtil.SIDEBAR_TEXT_COLOR, Color.WHITE));
		highlightStrategies.put("map", new ButtonHighlightStrategy(
				mapButton, menuButtons, DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR, ACTIVE_BUTTON_BACKGROUND_COLOR,
				DashboardPanelUtil.SIDEBAR_TEXT_COLOR, Color.WHITE));
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

		activeButton.setBackground(ACTIVE_BUTTON_BACKGROUND_COLOR);
		activeButton.setForeground(Color.WHITE);
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

	@Override
	public void applyTheme() {
		removeAll();
		organize();
		setBackground(DashboardPanelUtil.SIDEBAR_BACKGROUND_COLOR);
		if (titleLabel != null) {
			titleLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		}
		if (subtitleLabel != null) {
			subtitleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		}
		themeButton.setText(DashboardPanelUtil.isDarkMode() ? "Mode clair" : "Mode sombre");
		themeButton.setBackground(new Color(0x17, 0x31, 0x74));
		themeButton.setForeground(Color.WHITE);
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
