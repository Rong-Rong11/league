package gui.panel.matchPanel;
import config.CalendarConfiguration;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.RoundedPanel;
import gui.panel.common.ThemeAware;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class MatchHeaderPanel extends RoundedPanel implements ThemeAware {
	private static final Color NAVIGATION_BUTTON_COLOR = new Color(0x17, 0x31, 0x74);
	private static final DateTimeFormatter HEADER_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM");

	private JLabel titleLabel;
	private JLabel dayNumberLabel;
	private JLabel subtitleLabel;
	private JButton previousDayButton;
	private JButton nextDayButton;

	public MatchHeaderPanel() {
		super(24);
		setLayout(new BorderLayout());
		setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

		titleLabel = new JLabel("SAISON REGULIERE");
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);

		dayNumberLabel = new JLabel("Jour -");
		dayNumberLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
		dayNumberLabel.setAlignmentX(LEFT_ALIGNMENT);

		subtitleLabel = new JLabel("-");
		subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
		subtitleLabel.setAlignmentX(LEFT_ALIGNMENT);

		previousDayButton = new RoundedButton("<");
		nextDayButton = new RoundedButton(">");
		previousDayButton.setPreferredSize(new Dimension(42, 30));
		nextDayButton.setPreferredSize(new Dimension(42, 30));
		previousDayButton.setBackground(NAVIGATION_BUTTON_COLOR);
		nextDayButton.setBackground(NAVIGATION_BUTTON_COLOR);
		previousDayButton.setForeground(Color.WHITE);
		nextDayButton.setForeground(Color.WHITE);

		JPanel textPanel = new JPanel();
		textPanel.setOpaque(false);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		textPanel.add(titleLabel);
		textPanel.add(Box.createVerticalStrut(3));
		textPanel.add(dayNumberLabel);
		textPanel.add(Box.createVerticalStrut(3));
		textPanel.add(subtitleLabel);

		add(textPanel, BorderLayout.WEST);
		add(buildNavigationPanel(), BorderLayout.EAST);
		setPreferredSize(new Dimension(320, 90));
		applyTheme();
	}

	private JPanel buildNavigationPanel() {
		JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
		navigationPanel.setOpaque(false);
		navigationPanel.add(previousDayButton);
		navigationPanel.add(nextDayButton);
		return navigationPanel;
	}

	public void updateDate(LocalDate date) {
		if (date == null) {
			dayNumberLabel.setText("Jour -");
			subtitleLabel.setText("-");
			return;
		}
		long dayNumber = ChronoUnit.DAYS.between(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE, date) + 1;
		dayNumberLabel.setText("Jour " + dayNumber);
		subtitleLabel.setText(HEADER_DATE_FORMATTER.format(date));
	}

	public void setPreviousDayAction(ActionListener actionListener) {
		previousDayButton.addActionListener(actionListener);
	}

	public void setNextDayAction(ActionListener actionListener) {
		nextDayButton.addActionListener(actionListener);
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		titleLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		dayNumberLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		subtitleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
	}
}
