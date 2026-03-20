package gui.panel.common;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

public class ButtonStyleUtil {

	public static void styleToggleButton(JButton button) {
		button.setFocusPainted(false);
		button.setOpaque(true);
		button.setContentAreaFilled(true);
		button.setBorderPainted(false);
		button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		button.setPreferredSize(new Dimension(96, 32));
	}
}
