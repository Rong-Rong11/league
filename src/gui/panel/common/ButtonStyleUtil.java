package gui.panel.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

public class ButtonStyleUtil {
	private static final Color ACTIVE_BUTTON_COLOR = new Color(0x2F, 0x80, 0xA9);
	private static final Color INACTIVE_BUTTON_COLOR = new Color(0xEC, 0xF0, 0xF4);
	private static final Color INACTIVE_TEXT_COLOR = new Color(0x4F, 0x5D, 0x75);

	public static void styleToggleButton(JButton button) {
		button.setFocusPainted(false);
		button.setOpaque(true);
		button.setContentAreaFilled(true);
		button.setBorderPainted(false);
		button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		button.setPreferredSize(new Dimension(96, 32));
	}

	public static void setToggleButtonSelected(JButton button, boolean selected) {
		if (selected) {
			button.setBackground(ACTIVE_BUTTON_COLOR);
			button.setForeground(Color.WHITE);
			return;
		}
		button.setBackground(INACTIVE_BUTTON_COLOR);
		button.setForeground(INACTIVE_TEXT_COLOR);
	}
}
