package gui.panel.common;

import java.awt.BorderLayout;

public class TitledCard extends DashboardCard {

	public TitledCard(String title, String subtitle) {
		setLayout(new BorderLayout());
		add(new SectionTitle(title, subtitle), BorderLayout.NORTH);
	}
}
