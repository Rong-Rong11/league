package gui.panel.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MonthNavigatorPanel extends JPanel implements ThemeAware {
	private final JButton previousButton;
	private final JButton nextButton;
	private final JLabel valueLabel;
	private List<Integer> availableMonths;
	private int selectedMonth;
	private Runnable changeListener;

	public MonthNavigatorPanel() {
		super(new BorderLayout(6, 0));
		setOpaque(false);

		availableMonths = new ArrayList<Integer>();
		availableMonths.add(1);
		selectedMonth = 1;

		previousButton = createArrowButton("<");
		nextButton = createArrowButton(">");
		valueLabel = new JLabel(buildMonthText(), JLabel.CENTER);
		valueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));

		previousButton.addActionListener(new PreviousAction());
		nextButton.addActionListener(new NextAction());

		add(previousButton, BorderLayout.WEST);
		add(valueLabel, BorderLayout.CENTER);
		add(nextButton, BorderLayout.EAST);
		applyTheme();
		refreshState();
	}

	private JButton createArrowButton(String text) {
		JButton button = new RoundedButton(text);
		button.setBackground(DashboardPanelUtil.POLICY_BALANCED_COLOR);
		button.setForeground(Color.WHITE);
		button.setPreferredSize(new Dimension(42, 32));
		return button;
	}

	public void setChangeListener(Runnable changeListener) {
		this.changeListener = changeListener;
	}

	public int getSelectedMonth() {
		return selectedMonth;
	}

	public void setAvailableMonths(List<Integer> months) {
		if (months == null || months.isEmpty()) {
			availableMonths = new ArrayList<Integer>();
			availableMonths.add(1);
		} else {
			availableMonths = new ArrayList<Integer>(months);
		}
		if (!availableMonths.contains(selectedMonth)) {
			selectedMonth = availableMonths.get(availableMonths.size() - 1);
		}
		refreshState();
	}

	private void move(int delta) {
		int index = availableMonths.indexOf(selectedMonth);
		int nextIndex = index + delta;
		if (nextIndex < 0 || nextIndex >= availableMonths.size()) {
			return;
		}
		selectedMonth = availableMonths.get(nextIndex);
		refreshState();
		if (changeListener != null) {
			changeListener.run();
		}
	}

	private void refreshState() {
		valueLabel.setText(buildMonthText());
		int index = availableMonths.indexOf(selectedMonth);
		previousButton.setEnabled(index > 0);
		nextButton.setEnabled(index < availableMonths.size() - 1);
	}

	private String buildMonthText() {
		return "M" + selectedMonth;
	}

	@Override
	public void applyTheme() {
		valueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		previousButton.setBackground(DashboardPanelUtil.POLICY_BALANCED_COLOR);
		nextButton.setBackground(DashboardPanelUtil.POLICY_BALANCED_COLOR);
		if (DashboardPanelUtil.isDarkMode()) {
			previousButton.setForeground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
			nextButton.setForeground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
		} else {
			previousButton.setForeground(Color.WHITE);
			nextButton.setForeground(Color.WHITE);
		}
	}

	private class PreviousAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			move(-1);
		}
	}

	private class NextAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			move(1);
		}
	}
}
