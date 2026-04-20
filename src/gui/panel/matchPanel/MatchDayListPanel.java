package gui.panel.matchPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import data.calendar.GameDay;
import data.sport.setup.Game;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.LabelStyleUtil;
import gui.panel.common.ThemeAware;

public class MatchDayListPanel extends JPanel implements ThemeAware {

	public interface MatchSelectionListener {
		void onMatchSelected(Game game);
		void onMatchDetail(Game game);
	}

	private JPanel gamesColumn;
	private MatchSelectionListener matchSelectionListener;
	private JLabel emptyStateLabel;

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
			showMessage("Aucun match programme pour cette date.");
			return;
		}

		for (int i = 0; i < gameDay.getGames().size(); i++) {
			gamesColumn.add(new MatchDayEntryPanel(gameDay.getGames().get(i), gameDay.getGames().get(i).isDisplayed(), i,
					matchSelectionListener));
		}
		for (int i = gameDay.getGames().size(); i < config.CalendarConfiguration.MAX_GAMES_PER_DAY; i++) {
			gamesColumn.add(buildEmptyRow(i));
		}

		gamesColumn.revalidate();
		repaint();
	}

	public void showSeasonNotStartedState() {
		showMessage("Lancez la saison pour afficher les matchs du calendrier.");
	}

	private void showMessage(String text) {
		gamesColumn.removeAll();
		emptyStateLabel = new JLabel(text, SwingConstants.CENTER);
		LabelStyleUtil.styleSubtitleLabel(emptyStateLabel, 13);
		emptyStateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		gamesColumn.add(emptyStateLabel);
		gamesColumn.revalidate();
		repaint();
	}

	private JPanel buildEmptyRow(int index) {
		JPanel emptyRow = new JPanel();
		emptyRow.setOpaque(true);
		emptyRow.setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		emptyRow.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(index == 0 ? 1 : 0, 0, 1, 0, DashboardPanelUtil.BORDER_COLOR),
				BorderFactory.createEmptyBorder(6, 8, 6, 8)));
		return emptyRow;
	}

	@Override
	public void applyTheme() {
		if (emptyStateLabel != null) {
			LabelStyleUtil.styleSubtitleLabel(emptyStateLabel, 13);
		}
		repaint();
	}
}
