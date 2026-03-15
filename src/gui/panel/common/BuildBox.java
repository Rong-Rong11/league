package gui.panel.common;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class BuildBox extends DashboardCard {

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
		add(new PlaceholderPanel(placeholderText), BorderLayout.CENTER);
	}
}
