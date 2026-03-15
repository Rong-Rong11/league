package gui.layout.strategy;

import java.awt.Color;

import javax.swing.JButton;

public class ButtonHighlightStrategy implements SidebarHighlightStrategy {
	private JButton activeButton;
	private JButton[] allButtons;
	private Color defaultColor;
	private Color activeColor;

	public ButtonHighlightStrategy(JButton activeButton, JButton[] allButtons, Color defaultColor, Color activeColor) {
		this.activeButton = activeButton;
		this.allButtons = allButtons;
		this.defaultColor = defaultColor;
		this.activeColor = activeColor;
	}

	@Override
	public void highlight() {
		for (int i = 0; i < allButtons.length; i++) {
			allButtons[i].setBackground(defaultColor);
		}
		activeButton.setBackground(activeColor);
	}
}
