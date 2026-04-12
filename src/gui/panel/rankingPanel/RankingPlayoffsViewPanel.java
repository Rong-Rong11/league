package gui.panel.rankingPanel;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import gui.panel.common.PlaceholderPanel;
import gui.panel.common.ThemeAware;

public class RankingPlayoffsViewPanel extends JPanel implements ThemeAware {

	public RankingPlayoffsViewPanel() {
		setLayout(new BorderLayout());
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		add(new PlaceholderPanel("Playoffs a venir"), BorderLayout.CENTER);
	}

	@Override
	public void applyTheme() {
		revalidate();
		repaint();
	}
}
