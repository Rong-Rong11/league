package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import config.CalendarConfiguration;
import data.calendar.GameDay;
import data.finance.GameStat;
import data.sport.setup.Game;
import gui.panel.common.BuildBox;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.ThemeAware;
import gui.panel.matchPanel.MatchDayListPanel;
import gui.panel.matchPanel.MatchDayListPanel.MatchSelectionListener;
import gui.panel.matchPanel.MatchDetailPanel;
import gui.panel.matchPanel.MatchFinancePanel;
import gui.panel.matchPanel.MatchHeaderPanel;
import process.orchestrator.interfaces.GUIInterface;

public class MatchDashboard extends JPanel implements ThemeAware, RefreshableDashboard {
	private static final int DASHBOARD_SPACING = 16;
	private static final int LEFT_COLUMN_WIDTH = 270;
	private static final int RIGHT_COLUMN_WIDTH = 300;
	private static final Color BACKGROUND_COLOR = DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR;

	private GUIInterface guiInterface;
	private LocalDate selectedDate;
	private Game selectedGame;
	private GameDay selectedGameDay;

	private MatchHeaderPanel headerPanel;
	private MatchDayListPanel matchDayListPanel;
	private MatchDetailPanel matchDetailPanel;
	private MatchFinancePanel matchFinancePanel;

	private Runnable openLiveMatchAction;

	public MatchDashboard(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		selectedDate = CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE;
		create();
		organize();
		actions();
		loadGamesOfDay(selectedDate);
	}

	private void create() {
		headerPanel = new MatchHeaderPanel();
		matchDayListPanel = new MatchDayListPanel();
		matchDetailPanel = new MatchDetailPanel();
		matchFinancePanel = new MatchFinancePanel();
	}

	private void organize() {
		setLayout(new BorderLayout());
		setBackground(BACKGROUND_COLOR);

		JPanel content = buildContentPanel();
		content.add(buildHeader(), BorderLayout.NORTH);
		content.add(buildBody(), BorderLayout.CENTER);
		add(content, BorderLayout.CENTER);
	}

	private JPanel buildContentPanel() {
		return DashboardPanelUtil.createContentPanel(DASHBOARD_SPACING);
	}

	private JPanel buildHeader() {
		return headerPanel;
	}

	private JPanel buildBody() {
		JPanel body = DashboardPanelUtil.createBodyPanel(DASHBOARD_SPACING, 0);
		body.add(buildLeftColumn(), BorderLayout.WEST);
		body.add(buildCenterColumn(), BorderLayout.CENTER);
		body.add(buildRightColumn(), BorderLayout.EAST);
		return body;
	}

	private JPanel buildLeftColumn() {
		JPanel leftColumn = new BuildBox("MATCHS DU JOUR", "Rencontres de la journee", matchDayListPanel);
		leftColumn.setPreferredSize(new Dimension(LEFT_COLUMN_WIDTH, 10));
		return leftColumn;
	}

	private JPanel buildCenterColumn() {
		return new BuildBox("MATCH SELECTIONNE", "Score et statistiques", matchDetailPanel);
	}

	private JPanel buildRightColumn() {
		JPanel rightColumn = new BuildBox("FINANCES DU MATCH", "Revenus et depenses", matchFinancePanel);
		rightColumn.setPreferredSize(new Dimension(RIGHT_COLUMN_WIDTH, 10));
		return rightColumn;
	}

	private void actions() {
		matchDayListPanel.setMatchSelectionListener(new DashboardMatchSelectionListener());
		headerPanel.setPreviousDayAction(new PreviousDayAction());
		headerPanel.setNextDayAction(new NextDayAction());
	}

	public LocalDate getSelectedDate() {
		return selectedDate;
	}

	public Game getSelectedGame() {
		return selectedGame;
	}

	public void setOpenLiveMatchAction(Runnable openLiveMatchAction) {
		this.openLiveMatchAction = openLiveMatchAction;
	}

	public void loadGamesOfDay(LocalDate date) {
		if (date == null) {
			resetSelectedGame();
			return;
		}
		selectedDate = date;
		if (!guiInterface.isSeasonInitialized()) {
			headerPanel.updateDate(date);
			selectedGameDay = null;
			matchDayListPanel.showSeasonNotStartedState();
			resetSelectedGame();
			return;
		}
		GameDay gameDay = guiInterface.getGameDay(date);
		showGameDay(gameDay, date);
	}

	public void showGameDay(GameDay gameDay, LocalDate date) {
		selectedDate = date;
		selectedGameDay = gameDay;
		headerPanel.updateDate(date);
		matchDayListPanel.showGameDay(gameDay);

		if (gameDay == null || gameDay.getGames().isEmpty()) {
			resetSelectedGame();
			return;
		}

		updateSelectedGame(gameDay.getGames().get(0));
		repaint();
	}

	public void refreshSelectedGame() {
		if (guiInterface.isSeasonInitialized()) {
			LocalDate matchDisplayDate = guiInterface.getMatchDisplayDate();
			if (matchDisplayDate != null && !matchDisplayDate.equals(selectedDate)) {
				loadGamesOfDay(matchDisplayDate);
				return;
			}
		}
		if (selectedGameDay != null) {
			matchDayListPanel.showGameDay(selectedGameDay);
		}
		if (selectedGame != null) {
			updateSelectedGame(selectedGame);
		}
	}

	@Override
	public void refresh() {
		refreshSelectedGame();
	}

	private void updateSelectedGame(Game game) {
		selectedGame = game;
		if (selectedGame == null || !selectedGame.isDisplayed()) {
			matchDetailPanel.showHiddenState(game, buildDayNumberText());
			matchFinancePanel.showHiddenState();
			return;
		}

		GameStat gameStat = guiInterface.getGameStat(game);
		matchDetailPanel.showGame(game, buildDayNumberText(), gameStat);
		matchFinancePanel.showGameFinance(game, gameStat);
	}

	private void resetSelectedGame() {
		selectedGameDay = null;
		selectedGame = null;
		matchDetailPanel.showEmptyState();
		matchFinancePanel.showHiddenState();
	}

	private String buildDayNumberText() {
		if (selectedDate == null) {
			return "Jour -";
		}
		long dayNumber = ChronoUnit.DAYS.between(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE, selectedDate) + 1;
		return "Jour " + dayNumber;
	}

	private void showAdjacentGameDay(LocalDate date) {
		if (date != null) {
			loadGamesOfDay(date);
		}
	}

	private class DashboardMatchSelectionListener implements MatchSelectionListener {
		@Override
		public void onMatchSelected(Game game) {
			updateSelectedGame(game);
		}

		@Override
		public void onMatchDetail(Game game) {
			updateSelectedGame(game);
			if (!guiInterface.isLiveMatchAvailable(game)) {
				int choice = JOptionPane.showOptionDialog(
						MatchDashboard.this,
						"Ce match n'est pas encore simule. Simulez d'abord la journee complete pour acceder au live match.",
						"Live match indisponible",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE,
						null,
						new Object[] { "Simuler la journee", "Annuler" },
						"Simuler la journee");
				if (choice != 0) {
					return;
				}
				boolean liveMatchAvailable = guiInterface.makeLiveMatchAvailable(game, selectedDate);
				showGameDay(guiInterface.getGameDay(selectedDate), selectedDate);
				updateSelectedGame(game);
				if (!liveMatchAvailable) {
					JOptionPane.showMessageDialog(MatchDashboard.this,
							"Le live match reste indisponible apres la simulation de cette journee.",
							"Live match indisponible", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
			if (openLiveMatchAction != null) {
				openLiveMatchAction.run();
			}
		}
	}

	private class PreviousDayAction implements java.awt.event.ActionListener {
		@Override
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (!guiInterface.isSeasonInitialized() || selectedDate == null) {
				return;
			}
			showAdjacentGameDay(guiInterface.getPreviousGameDay(selectedDate.minusDays(1)));
		}
	}

	private class NextDayAction implements java.awt.event.ActionListener {
		@Override
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (!guiInterface.isSeasonInitialized() || selectedDate == null) {
				return;
			}
			showAdjacentGameDay(guiInterface.getNextGameDay(selectedDate.plusDays(1)));
		}
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
		DashboardPanelUtil.refreshChildrenTheme(this);
	}
}
