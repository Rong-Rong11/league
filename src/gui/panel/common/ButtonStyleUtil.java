package gui.panel.common;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

public class ButtonStyleUtil {
	private static final ButtonStyleStrategy TOGGLE_BUTTON_STYLE = new ToggleButtonStyleStrategy();

	public static void styleToggleButton(JButton button) {
		styleButton(button, TOGGLE_BUTTON_STYLE);
	}

	public static void setToggleButtonSelected(JButton button, boolean selected) {
		applySelectionStyle(button, selected, TOGGLE_BUTTON_STYLE);
	}

	public static void styleButton(JButton button, ButtonStyleStrategy strategy) {
		strategy.applyBaseStyle(button);
	}

	public static void applySelectionStyle(JButton button, boolean selected, ButtonStyleStrategy strategy) {
		strategy.applySelectionStyle(button, selected);
	}

	public static void styleActionButton(JButton button, int width, int height, int fontSize) {
		button.setFocusPainted(false);
		button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontSize));
		button.setPreferredSize(new Dimension(width, height));
	}

	public static void styleMenuButton(JButton button, int width, int height, int fontSize) {
		button.setFocusPainted(false);
		button.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, fontSize));
		button.setPreferredSize(new Dimension(width, height));
	}
}
