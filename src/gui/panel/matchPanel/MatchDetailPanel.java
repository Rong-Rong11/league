package gui.panel.matchPanel;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import data.finance.GameStat;
import data.sport.setup.Game;

public class MatchDetailPanel extends JPanel {
	private MatchResultPanel resultPanel;
	private MatchStatPanel statPanel;

	public MatchDetailPanel() {
		super(new BorderLayout(0, 16));
		setOpaque(false);

		JPanel content = new JPanel();
		content.setOpaque(false);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		resultPanel = new MatchResultPanel();
		statPanel = new MatchStatPanel();
		content.add(resultPanel);
		content.add(statPanel);
		add(content, BorderLayout.NORTH);
	}

	public void showHiddenState(Game game, String dayLabel) {
		resultPanel.showHiddenState(game, dayLabel);
		statPanel.showEmptyState();
	}

	public void showGame(Game game, String dayLabel, GameStat gameStat) {
		resultPanel.showGame(game, dayLabel);
		statPanel.showStats(game.getQuarterResults());
		statPanel.showAttendance(game, gameStat);
	}

	public void showEmptyState() {
		resultPanel.showEmptyState();
		statPanel.showEmptyState();
	}
}
