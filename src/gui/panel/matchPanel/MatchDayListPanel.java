package gui.panel.matchPanel;
import config.CalendarConfiguration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.calendar.GameDay;
import data.sport.setup.Game;

public class MatchDayListPanel extends JPanel {
	private static final Color SEPARATOR_COLOR = new Color(225, 225, 225);

	public interface MatchSelectionListener {
		void onMatchSelected(Game game);
		void onMatchDetail(Game game);
	}

	private JPanel gamesColumn;
	private MatchSelectionListener matchSelectionListener;

	public MatchDayListPanel() {
		super(new BorderLayout());
		gamesColumn = new JPanel();
		gamesColumn.setOpaque(false);
		gamesColumn.setLayout(new GridLayout(config.CalendarConfiguration.MAX_GAMES_PER_DAY, 1, 0, 0));
		add(gamesColumn, BorderLayout.CENTER);
	}

	public void setMatchSelectionListener(MatchSelectionListener matchSelectionListener) {
		this.matchSelectionListener = matchSelectionListener;
	}

	public void showGameDay(GameDay gameDay) {
		gamesColumn.removeAll();

		if (gameDay == null || gameDay.getGames().isEmpty()) {
			gamesColumn.add(new JLabel("Aucun match aujourd'hui"));
			revalidate();
			repaint();
			return;
		}

		for (int i = 0; i < gameDay.getGames().size(); i++) {
			gamesColumn.add(new MatchDayEntryPanel(gameDay.getGames().get(i), gameDay.getGames().get(i).isDisplayed(), i,
					matchSelectionListener));
		}
		for (int i = gameDay.getGames().size(); i < config.CalendarConfiguration.MAX_GAMES_PER_DAY; i++) {
			gamesColumn.add(buildEmptyRow(i));
		}

		revalidate();
		repaint();
	}

	private JPanel buildEmptyRow(int index) {
		JPanel emptyRow = new JPanel();
		emptyRow.setOpaque(true);
		emptyRow.setBackground(Color.WHITE);
		emptyRow.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(index == 0 ? 1 : 0, 0, 1, 0, SEPARATOR_COLOR),
				BorderFactory.createEmptyBorder(6, 8, 6, 8)));
		return emptyRow;
	}
}
