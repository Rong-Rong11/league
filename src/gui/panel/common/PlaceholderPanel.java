package gui.panel.common;

import java.awt.BorderLayout;
import javax.swing.JLabel;

public class PlaceholderPanel extends RoundedPanel implements ThemeAware {
	private JLabel placeholderLabel;

	public PlaceholderPanel(String placeholderText) {
		super(20);
		setLayout(new BorderLayout());
		placeholderLabel = new JLabel(placeholderText, JLabel.CENTER);
		add(placeholderLabel, BorderLayout.CENTER);
		applyTheme();
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.PLACEHOLDER_BACKGROUND_COLOR);
		placeholderLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
	}
}
