package gui.panel.matchPanel.liveMatchPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.ThemeAware;

public class LiveActionsPanel extends JPanel implements ThemeAware {
	private JLabel[] actionRows;
	private JLabel messageLabel;

	public LiveActionsPanel(int liveRows) {
		super(new BorderLayout(0, 12));
		setOpaque(false);

		JPanel rowsPanel = new JPanel(new GridLayout(liveRows, 1, 0, 8));
		rowsPanel.setOpaque(false);
		actionRows = new JLabel[liveRows];
		for (int i = 0; i < liveRows; i++) {
			actionRows[i] = createRowLabel();
			rowsPanel.add(actionRows[i]);
		}

		messageLabel = new JLabel("", JLabel.CENTER);
		messageLabel.setForeground(new Color(0x6D, 0x75, 0x83));
		messageLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

		add(rowsPanel, BorderLayout.NORTH);
		add(messageLabel, BorderLayout.CENTER);
		applyTheme();
	}

	public void updateRows(String[] rows, String message) {
		for (int i = 0; i < actionRows.length; i++) {
			actionRows[i].setText(rows[i] == null ? " " : rows[i]);
		}
		messageLabel.setText(message == null ? "" : message);
		messageLabel.setVisible(message != null && !message.isEmpty());
	}

	private JLabel createRowLabel() {
		JLabel row = new JLabel(" ");
		row.setOpaque(true);
		row.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
		return row;
	}

	@Override
	public void applyTheme() {
		messageLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		for (int i = 0; i < actionRows.length; i++) {
			actionRows[i].setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
			actionRows[i].setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		}
	}
}
