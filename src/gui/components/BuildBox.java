package gui.components;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class BuildBox extends DashboardCard {

	private static final Color PLACEHOLDER_BACKGROUND = new Color(226, 226, 226);

	public BuildBox(String title, String subtitle, JPanel content) {
		setLayout(new BorderLayout());

		JPanel titlePart = new SectionTitle(title, subtitle);
		add(titlePart, BorderLayout.NORTH);

		add(content, BorderLayout.CENTER);
	}

	public BuildBox(String title, String subtitle, String placeholderText) {
		setLayout(new BorderLayout());

		JPanel titlePart = new SectionTitle(title, subtitle);
		add(titlePart, BorderLayout.NORTH);

		JPanel placeholderPart = new JPanel(new BorderLayout());
		placeholderPart.setBackground(PLACEHOLDER_BACKGROUND);
		placeholderPart.setOpaque(true);
		placeholderPart.add(new JLabel(placeholderText, JLabel.CENTER), BorderLayout.CENTER);

		add(placeholderPart, BorderLayout.CENTER);
	}
}
