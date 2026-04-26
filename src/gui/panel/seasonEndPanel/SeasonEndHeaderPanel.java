package gui.panel.seasonEndPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gui.panel.common.ButtonStyleUtil;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.ThemeAware;

public class SeasonEndHeaderPanel extends JPanel implements ThemeAware {
	private static final String OVERVIEW_PAGE = "overview";
	private static final String FINANCE_PAGE = "finance";
	private static final String PROFILE_PAGE = "profile";

	private final SeasonEndDataProvider dataProvider;
	private JButton overviewButton;
	private JButton financeButton;
	private JButton profileButton;
	private String selectedPage;

	public SeasonEndHeaderPanel(SeasonEndDataProvider dataProvider) {
		this.dataProvider = dataProvider;
		selectedPage = OVERVIEW_PAGE;
		create();
		organize();
	}

	private void create() {
		overviewButton = new RoundedButton("Vue d'ensemble");
		financeButton = new RoundedButton("Finances");
		profileButton = new RoundedButton("Profils & graphes");
	}

	private void organize() {
		removeAll();
		setLayout(new BorderLayout(18, 0));
		setOpaque(false);
		add(buildTitleBlock(), BorderLayout.CENTER);
		add(buildPageSelector(), BorderLayout.EAST);
	}

	private JPanel buildTitleBlock() {
		JPanel panel = new JPanel(new BorderLayout(14, 0));
		panel.setOpaque(false);

		JLabel iconLabel = new JLabel("F", SwingConstants.CENTER);
		iconLabel.setOpaque(true);
		iconLabel.setBackground(DashboardPanelUtil.NEUTRAL_ACCENT_COLOR);
		iconLabel.setForeground(DashboardPanelUtil.getPrimaryActionTextColor());
		iconLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		iconLabel.setPreferredSize(new Dimension(54, 54));

		JPanel textPanel = new JPanel();
		textPanel.setOpaque(false);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

		JLabel titleLabel = new JLabel("Simulation terminee", SwingConstants.CENTER);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
		titleLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);

		JLabel subtitleLabel = new JLabel(dataProvider.buildHeaderSubtitle(), SwingConstants.CENTER);
		subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		subtitleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		subtitleLabel.setAlignmentX(LEFT_ALIGNMENT);

		textPanel.add(Box.createVerticalStrut(4));
		textPanel.add(titleLabel);
		textPanel.add(Box.createVerticalStrut(3));
		textPanel.add(subtitleLabel);
		panel.add(iconLabel, BorderLayout.WEST);
		panel.add(textPanel, BorderLayout.CENTER);
		return panel;
	}

	private JPanel buildPageSelector() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
		stylePageSwitchButton(overviewButton, OVERVIEW_PAGE.equals(selectedPage));
		stylePageSwitchButton(financeButton, FINANCE_PAGE.equals(selectedPage));
		stylePageSwitchButton(profileButton, PROFILE_PAGE.equals(selectedPage));
		panel.add(overviewButton);
		panel.add(financeButton);
		panel.add(profileButton);
		return panel;
	}

	private void stylePageSwitchButton(JButton button, boolean selected) {
		ButtonStyleUtil.styleToggleButton(button);
		ButtonStyleUtil.setToggleButtonSelected(button, selected);
		button.setPreferredSize(new Dimension(150, 36));
	}

	public void setSelectedPage(String selectedPage) {
		this.selectedPage = selectedPage;
		organize();
		revalidate();
		repaint();
	}

	public void refresh() {
		organize();
		revalidate();
		repaint();
	}

	public JButton getOverviewButton() {
		return overviewButton;
	}

	public JButton getFinanceButton() {
		return financeButton;
	}

	public JButton getProfileButton() {
		return profileButton;
	}

	@Override
	public void applyTheme() {
		refresh();
	}
}
