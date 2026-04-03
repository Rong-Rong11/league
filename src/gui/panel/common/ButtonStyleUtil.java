package gui.panel.common;

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
}
