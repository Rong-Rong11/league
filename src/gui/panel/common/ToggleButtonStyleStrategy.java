package gui.panel.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

public class ToggleButtonStyleStrategy implements ButtonStyleStrategy {
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
			button.setBackground(DashboardPanelUtil.POLICY_BALANCED_COLOR);
			button.setForeground(DashboardPanelUtil.isDarkMode() ? DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR : Color.WHITE);
			return;
		}
		button.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
		button.setForeground(DashboardPanelUtil.BUTTON_TEXT_COLOR);
	}
}
