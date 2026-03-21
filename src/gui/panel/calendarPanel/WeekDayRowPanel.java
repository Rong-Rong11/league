package gui.panel.calendarPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.calendar.GameDay;

public class WeekDayRowPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Font TITLE_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);
	private static final Font TEXT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 11);
	private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("EEEE dd/MM");

	public WeekDayRowPanel(LocalDate day, GameDay gameDay, boolean displayed,
			ActionListener simulateAction, ActionListener detailAction) {
		create(day, gameDay, displayed, simulateAction, detailAction);
	}

	private void create(LocalDate day, GameDay gameDay, boolean displayed,
			ActionListener simulateAction, ActionListener detailAction) {
		setLayout(new BorderLayout(12, 0));
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));

		JPanel infoPanel = buildInfoPanel(day, gameDay);
		JPanel actionsPanel = buildActionsPanel(displayed, simulateAction, detailAction);

		add(infoPanel, BorderLayout.CENTER);
		add(actionsPanel, BorderLayout.EAST);
	}

	private JPanel buildInfoPanel(LocalDate day, GameDay gameDay) {
		JLabel dayTitle = new JLabel(DAY_FORMATTER.format(day));
		dayTitle.setFont(TITLE_FONT);

		String detailText = gameDay.getGames().size() == 1 ? "1 match" : gameDay.getGames().size() + " matchs";
		JLabel dayDetail = new JLabel(detailText);
		dayDetail.setFont(TEXT_FONT);

		JPanel infoPanel = new JPanel();
		infoPanel.setOpaque(false);
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.add(dayTitle);
		infoPanel.add(dayDetail);
		return infoPanel;
	}

	private JPanel buildActionsPanel(boolean displayed, ActionListener simulateAction, ActionListener detailAction) {
		JButton simulateButton = new JButton("Simuler");
		simulateButton.setFont(TEXT_FONT);
		simulateButton.addActionListener(simulateAction);

		JButton detailButton = new JButton("Détail");
		detailButton.setFont(TEXT_FONT);
		detailButton.addActionListener(detailAction);

		JLabel stateLabel = new JLabel(displayed ? "Simulé" : "À simuler");
		stateLabel.setFont(TEXT_FONT);

		JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		actionsPanel.setOpaque(false);
		actionsPanel.add(stateLabel);
		actionsPanel.add(simulateButton);
		actionsPanel.add(detailButton);
		return actionsPanel;
	}
}
