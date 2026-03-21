package gui.panel.common;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlaceholderPanel extends JPanel {

	private static final Color PLACEHOLDER_BACKGROUND = new Color(226, 226, 226);

	public PlaceholderPanel(String placeholderText) {
		setLayout(new BorderLayout());
		setBackground(PLACEHOLDER_BACKGROUND);
		setOpaque(true);
		add(new JLabel(placeholderText, JLabel.CENTER), BorderLayout.CENTER);
	}
}
