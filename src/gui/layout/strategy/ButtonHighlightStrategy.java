package gui.layout.strategy;

import java.awt.Color;

import javax.swing.JButton;

public class ButtonHighlightStrategy implements SidebarHighlightStrategy {
	private JButton activeButton;
	private JButton[] allButtons;
	private Color defaultColor;
	private Color activeColor;
	private Color defaultTextColor;
	private Color activeTextColor;

	public ButtonHighlightStrategy(JButton activeButton, JButton[] allButtons, Color defaultColor, Color activeColor,
			Color defaultTextColor, Color activeTextColor) {
		this.activeButton = activeButton;
		this.allButtons = allButtons;
		this.defaultColor = defaultColor;
		this.activeColor = activeColor;
		this.defaultTextColor = defaultTextColor;
		this.activeTextColor = activeTextColor;
	}

	@Override
	public void highlight() {
		for (int i = 0; i < allButtons.length; i++) {
			allButtons[i].setBackground(defaultColor);
			allButtons[i].setForeground(defaultTextColor);
		}
		activeButton.setBackground(activeColor);
		activeButton.setForeground(activeTextColor);
	}
}
