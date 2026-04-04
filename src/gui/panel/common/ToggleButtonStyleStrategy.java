package gui.panel.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

public class ToggleButtonStyleStrategy implements ButtonStyleStrategy {
	private static final Color ACTIVE_BUTTON_COLOR = new Color(0x17, 0x31, 0x74);

	@Override
	public void applyBaseStyle(JButton button) {
		button.setFocusPainted(false);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		button.setPreferredSize(new Dimension(96, 32));
	}

	@Override
	public void applySelectionStyle(JButton button, boolean selected) {
		if (selected) {
			button.setBackground(ACTIVE_BUTTON_COLOR);
			button.setForeground(Color.WHITE);
			return;
		}
		button.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
		button.setForeground(DashboardPanelUtil.BUTTON_TEXT_COLOR);
	}
}
