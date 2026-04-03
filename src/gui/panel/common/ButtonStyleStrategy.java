package gui.panel.common;

import javax.swing.JButton;

public interface ButtonStyleStrategy {
	void applyBaseStyle(JButton button);

	void applySelectionStyle(JButton button, boolean selected);
}
