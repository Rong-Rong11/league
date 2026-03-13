package gui.panel.calendarPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class SeasonProgressBarPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Color TITLE_COLOR = new Color(0x1C, 0x2A, 0x4A);
	private static final Color SUBTITLE_COLOR = new Color(0x6D, 0x75, 0x83);
	private static final Color PROGRESS_BLUE = new Color(0x2D, 0x6A, 0xC8);
	private static final Font TITLE_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 14);
	private static final Font TEXT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
	private static final Font PERCENT_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 20);
	private static final Font COMPLETION_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 11);

	private final LocalDate startDate;
	private final LocalDate endDate;
	private LocalDate currentDate;
	private final JLabel daysLabel;
	private final JLabel percentageLabel;
	private final JProgressBar progressBar;
	private int completedDays;
	private int totalDays;

	public SeasonProgressBarPanel(LocalDate startDate, LocalDate endDate, LocalDate currentDate) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.currentDate = currentDate;
		this.completedDays = 0;
		this.totalDays = 0;

		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

		JLabel titleLabel = new JLabel("Progression de la saison");
		titleLabel.setFont(TITLE_FONT);
		titleLabel.setForeground(TITLE_COLOR);

		daysLabel = new JLabel("0 jour complete sur 0");
		daysLabel.setFont(TEXT_FONT);
		daysLabel.setForeground(SUBTITLE_COLOR);

		JPanel leftInfoPanel = new JPanel();
		leftInfoPanel.setOpaque(false);
		leftInfoPanel.setLayout(new BoxLayout(leftInfoPanel, BoxLayout.Y_AXIS));
		leftInfoPanel.add(titleLabel);
		leftInfoPanel.add(daysLabel);

		percentageLabel = new JLabel("0 %");
		percentageLabel.setFont(PERCENT_FONT);
		percentageLabel.setForeground(TITLE_COLOR);

		JLabel completionLabel = new JLabel("Completion");
		completionLabel.setFont(COMPLETION_FONT);
		completionLabel.setForeground(SUBTITLE_COLOR);

		JPanel rightInfoPanel = new JPanel();
		rightInfoPanel.setOpaque(false);
		rightInfoPanel.setLayout(new BoxLayout(rightInfoPanel, BoxLayout.Y_AXIS));
		rightInfoPanel.add(percentageLabel);
		rightInfoPanel.add(completionLabel);

		JPanel infoPanel = new JPanel(new BorderLayout());
		infoPanel.setOpaque(false);
		infoPanel.add(leftInfoPanel, BorderLayout.WEST);
		infoPanel.add(rightInfoPanel, BorderLayout.EAST);

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(false);
		progressBar.setForeground(PROGRESS_BLUE);
		progressBar.setBackground(new Color(0xDF, 0xE3, 0xEA));
		progressBar.setPreferredSize(new Dimension(260, 14));
		progressBar.setBorder(BorderFactory.createEmptyBorder());

		JPanel progressWrapper = new JPanel(new BorderLayout());
		progressWrapper.setOpaque(false);
		progressWrapper.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
		progressWrapper.add(progressBar, BorderLayout.CENTER);

		add(infoPanel, BorderLayout.NORTH);
		add(progressWrapper, BorderLayout.CENTER);

		refreshProgressValue();
	}

	public void setCurrentDate(LocalDate currentDate) {
		this.currentDate = currentDate;
		refreshProgressValue();
	}

	public void setProgress(int completedDays, int totalDays) {
		this.completedDays = Math.max(0, completedDays);
		this.totalDays = Math.max(0, totalDays);
		updateUIValues();
	}

	public void setProgressPercentage(double percentage) {
		int clamped = (int) Math.round(Math.max(0, Math.min(100, percentage)));
		progressBar.setValue(clamped);
		percentageLabel.setText(clamped + " %");
	}

	private void refreshProgressValue() {
		int seasonTotalDays = countDaysBetween(startDate, endDate);
		LocalDate clampedCurrentDate = currentDate;
		if (clampedCurrentDate.isBefore(startDate)) {
			clampedCurrentDate = startDate;
		}
		if (clampedCurrentDate.isAfter(endDate)) {
			clampedCurrentDate = endDate;
		}
		int seasonElapsedDays = countDaysBetween(startDate, clampedCurrentDate);
		setProgress(seasonElapsedDays, seasonTotalDays);
	}

	private void updateUIValues() {
		int percentage = 0;
		if (totalDays > 0) {
			percentage = (completedDays * 100) / totalDays;
		}

		daysLabel.setText(completedDays + " jours completes sur " + totalDays);
		percentageLabel.setText(percentage + " %");
		progressBar.setValue(percentage);
	}

	private int countDaysBetween(LocalDate fromDate, LocalDate toDate) {
		if (toDate.isBefore(fromDate)) {
			return 0;
		}

		int days = 0;
		LocalDate date = fromDate;
		while (date.isBefore(toDate)) {
			days++;
			date = date.plusDays(1);
		}
		return days;
	}
}
