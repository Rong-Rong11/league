package gui.panel.common;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;

public class PlaceholderPanel extends RoundedPanel {

	private static final Color PLACEHOLDER_BACKGROUND = new Color(226, 226, 226);

	public PlaceholderPanel(String placeholderText) {
		super(20);
		setLayout(new BorderLayout());
		setBackground(PLACEHOLDER_BACKGROUND);
		add(new JLabel(placeholderText, JLabel.CENTER), BorderLayout.CENTER);
	}
}
