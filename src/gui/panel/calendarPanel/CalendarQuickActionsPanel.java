package gui.panel.calendarPanel;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class CalendarQuickActionsPanel extends JPanel {

	public CalendarQuickActionsPanel(ActionListener simulateDayAction, ActionListener simulateWeekAction,
			ActionListener simulateSeasonAction) {
		setBackground(Color.WHITE);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(12, 16, 16, 16));

		JButton simulateDayButton = new JButton("Simuler un jour");
		JButton simulateWeekButton = new JButton("Simuler une semaine");
		JButton simulateSeasonButton = new JButton("Simuler toute la saison");

		simulateDayButton.addActionListener(simulateDayAction);
		simulateWeekButton.addActionListener(simulateWeekAction);
		simulateSeasonButton.addActionListener(simulateSeasonAction);

		add(simulateDayButton);
		add(Box.createVerticalStrut(10));
		add(simulateWeekButton);
		add(Box.createVerticalStrut(10));
		add(simulateSeasonButton);
	}
}
