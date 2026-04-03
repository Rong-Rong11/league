package gui.panel.matchPanel;
import config.CalendarConfiguration;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.RoundedPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;


public class MatchHeaderPanel extends RoundedPanel {
	private static final Color TITLE_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color SUBTITLE_COLOR = new Color(0x6D, 0x75, 0x83);
	private static final DateTimeFormatter HEADER_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM");

	private JLabel dayNumberLabel;
	private JLabel subtitleLabel;

	public MatchHeaderPanel() {
		super(24);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

		JLabel titleLabel = new JLabel("SAISON RÉGULIÈRE");
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		titleLabel.setForeground(TITLE_COLOR);
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);

		dayNumberLabel = new JLabel("Jour -");
		dayNumberLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
		dayNumberLabel.setForeground(TITLE_COLOR);
		dayNumberLabel.setAlignmentX(LEFT_ALIGNMENT);

		subtitleLabel = new JLabel("-");
		subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
		subtitleLabel.setForeground(SUBTITLE_COLOR);
		subtitleLabel.setAlignmentX(LEFT_ALIGNMENT);

		add(titleLabel);
		add(Box.createVerticalStrut(3));
		add(dayNumberLabel);
		add(Box.createVerticalStrut(3));
		add(subtitleLabel);
		setPreferredSize(new Dimension(320, 74));
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
}
