package gui.panel.matchPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import config.SimulationConfiguration;

public class MatchHeaderPanel extends JPanel {
	private static final Color TITLE_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color SUBTITLE_COLOR = new Color(0x6D, 0x75, 0x83);
	private static final DateTimeFormatter HEADER_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM");

	private JLabel dayNumberLabel;
	private JLabel subtitleLabel;

	public MatchHeaderPanel() {
		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JLabel titleLabel = new JLabel("SAISON RÉGULIÈRE");
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		titleLabel.setForeground(TITLE_COLOR);
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);

		dayNumberLabel = new JLabel("Jour -");
		dayNumberLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		dayNumberLabel.setForeground(TITLE_COLOR);
		dayNumberLabel.setAlignmentX(LEFT_ALIGNMENT);

		subtitleLabel = new JLabel("-");
		subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		subtitleLabel.setForeground(SUBTITLE_COLOR);
		subtitleLabel.setAlignmentX(LEFT_ALIGNMENT);

		add(titleLabel);
		add(Box.createVerticalStrut(3));
		add(dayNumberLabel);
		add(Box.createVerticalStrut(3));
		add(subtitleLabel);
		setPreferredSize(new Dimension(270, 78));
	}

	public void updateDate(LocalDate date) {
		if (date == null) {
			dayNumberLabel.setText("Jour -");
			subtitleLabel.setText("-");
			return;
		}
		long dayNumber = ChronoUnit.DAYS.between(SimulationConfiguration.REGULAR_SEASON_DEBUT_DATE, date) + 1;
		dayNumberLabel.setText("Jour " + dayNumber);
		subtitleLabel.setText(HEADER_DATE_FORMATTER.format(date));
	}
}
