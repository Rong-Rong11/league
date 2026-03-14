package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import config.SimulationConfiguration;
import data.player.Player;
import data.sport.play.action.ActionResult;
import data.sport.play.action.Block;
import data.sport.play.action.EndOfTime;
import data.sport.play.action.PointScored;
import data.sport.play.action.Rebound;
import data.sport.play.action.Turnover;
import data.sport.setup.Game;
import data.sport.setup.GameResult;
import gui.panel.common.BuildBox;
import gui.panel.common.SectionTitle;
import process.manager.LeagueManager;
import process.manager.LiveMatchStatistics;
import process.visitor.actionresult.ActionResultVisitor;

public class LiveMatchDashboard extends JPanel {
	private static final int IDEAL_DASHBOARD_SPACING = 16;
	private static final int IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH = 270;
	private static final int IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH = 270;
	private static final Color IDEAL_DASHBOARD_BACKGROUND_COLOR = new Color(247, 248, 250);
	private static final int LIVE_ROWS = 10;
	private static final int LIVE_DELAY_MS = 700;

	private Runnable backToMatchAction;
	private LeagueManager leagueManager;
	private LocalDate gameDate;

	private Game game;
	private String homeTeamName;
	private String awayTeamName;

	private ArrayList<LiveMatchStatistics.LiveAction> liveActions;
	private int liveActionIndex;
	private Thread liveThread;
	private boolean isLiveRunning;

	private LiveMatchStatistics liveMatchStatistics;

	private JButton backButton;
	private JButton playButton;
	private JButton nextQuarterButton;
	private JButton pauseButton;

	private JLabel scoreLabel;
	private JLabel quarterTimeLabel;
	private JLabel statusLabel;
	private JLabel[] actionRows;

	private JLabel homePointsLabel;
	private JLabel homeReboundsLabel;
	private JLabel homeAssistsLabel;
	private JLabel homeTurnoversLabel;
	private JLabel homeFgLabel;
	private JLabel homeThreeLabel;
	private JLabel homeBestPlayersLabel;

	private JLabel awayPointsLabel;
	private JLabel awayReboundsLabel;
	private JLabel awayAssistsLabel;
	private JLabel awayTurnoversLabel;
	private JLabel awayFgLabel;
	private JLabel awayThreeLabel;
	private JLabel awayBestPlayersLabel;

	public LiveMatchDashboard() {
		create();
		organize();
		actions();
		resetLiveState();
		updateLiveDashboard();
	}

	private void create() {
		homeTeamName = "HOME";
		awayTeamName = "AWAY";

		liveActions = new ArrayList<LiveMatchStatistics.LiveAction>();
		liveActionIndex = 0;
		liveMatchStatistics = new LiveMatchStatistics();

		backButton = new JButton("Retour");
		playButton = new JButton("Play");
		nextQuarterButton = new JButton("Jouer le quart");
		pauseButton = new JButton("Pause");

		scoreLabel = new JLabel(homeTeamName + " 0 - 0 " + awayTeamName, JLabel.CENTER);
		quarterTimeLabel = new JLabel("Q1 12:00", JLabel.CENTER);
		statusLabel = new JLabel("Aucun match selectionne", JLabel.CENTER);
	}

	private void organize() {
		setLayout(new BorderLayout());
		setBackground(IDEAL_DASHBOARD_BACKGROUND_COLOR);

		JPanel content = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING));
		content.setOpaque(false);

		JPanel leftSpace = new JPanel();
		leftSpace.setPreferredSize(new Dimension(IDEAL_DASHBOARD_SPACING, 0));
		leftSpace.setOpaque(false);

		JPanel rightSpace = new JPanel();
		rightSpace.setPreferredSize(new Dimension(IDEAL_DASHBOARD_SPACING, 0));
		rightSpace.setOpaque(false);

		JPanel topSpace = new JPanel();
		topSpace.setPreferredSize(new Dimension(0, IDEAL_DASHBOARD_SPACING));
		topSpace.setOpaque(false);

		JPanel bottomSpace = new JPanel();
		bottomSpace.setPreferredSize(new Dimension(0, IDEAL_DASHBOARD_SPACING));
		bottomSpace.setOpaque(false);

		add(leftSpace, BorderLayout.WEST);
		add(rightSpace, BorderLayout.EAST);
		add(topSpace, BorderLayout.NORTH);
		add(bottomSpace, BorderLayout.SOUTH);
		add(content, BorderLayout.CENTER);

		content.add(buildHeader(), BorderLayout.NORTH);
		content.add(buildBody(), BorderLayout.CENTER);
	}

	private void actions() {
		backButton.addActionListener(new BackAction());
		playButton.addActionListener(new PlayAction());
		nextQuarterButton.addActionListener(new NextQuarterAction());
		pauseButton.addActionListener(new PauseAction());
		isLiveRunning = false;
		liveThread = null;
	}

	public void setBackToMatchAction(Runnable backToMatchAction) {
		this.backToMatchAction = backToMatchAction;
	}

	public void setSimulationContext(LeagueManager leagueManager, LocalDate gameDate) {
		this.leagueManager = leagueManager;
		this.gameDate = gameDate;
	}

	public void setGame(Game game) {
		stopLiveThread();
		this.game = game;
		if (game == null) {
			homeTeamName = "HOME";
			awayTeamName = "AWAY";
		} else {
			homeTeamName = game.getGameContext().getHomeTeam().getName();
			awayTeamName = game.getGameContext().getAwayTeam().getName();
		}

		buildLiveActions();
		resetLiveState();
		updateLiveDashboard();
	}

	private JPanel buildHeader() {
		JPanel header = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, 0));
		header.setOpaque(false);

		JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		leftHeader.setOpaque(false);
		leftHeader.add(backButton);

		JPanel centerHeader = new JPanel(new GridLayout(2, 1, 0, 2));
		centerHeader.setOpaque(false);
		centerHeader.add(scoreLabel);
		centerHeader.add(statusLabel);

		JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		rightHeader.setOpaque(false);
		rightHeader.add(playButton);
		rightHeader.add(nextQuarterButton);
		rightHeader.add(pauseButton);

		header.add(leftHeader, BorderLayout.WEST);
		header.add(centerHeader, BorderLayout.CENTER);
		header.add(rightHeader, BorderLayout.EAST);

		return header;
	}

	private JPanel buildBody() {
		JPanel body = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, 0));
		body.setOpaque(false);

		JPanel leftCard = new BuildBox("ÉQUIPE DOMICILE", "Statistiques", buildTeamStatsPanel(true));
		leftCard.setPreferredSize(new Dimension(IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH, 10));

		JPanel centerCard = new BuildBox("ACTIONS EN DIRECT", "Dernières actions du match", buildLiveActionsPanel());

		JPanel rightCard = new BuildBox("ÉQUIPE EXTÉRIEUR", "Statistiques", buildTeamStatsPanel(false));
		rightCard.setPreferredSize(new Dimension(IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH, 10));

		body.add(leftCard, BorderLayout.WEST);
		body.add(centerCard, BorderLayout.CENTER);
		body.add(rightCard, BorderLayout.EAST);

		return body;
	}

	private JPanel buildLiveActionsPanel() {
		JPanel panel = new JPanel(new GridLayout(LIVE_ROWS, 1, 0, 8));
		panel.setOpaque(false);
		actionRows = new JLabel[LIVE_ROWS];
		for (int i = 0; i < LIVE_ROWS; i++) {
			JLabel row = new JLabel(" ");
			row.setOpaque(true);
			row.setBackground(new Color(236, 240, 245));
			actionRows[i] = row;
			panel.add(row);
		}
		return panel;
	}

	private JPanel buildTeamStatsPanel(boolean home) {
		JPanel panel = new JPanel(new BorderLayout(0, 12));
		panel.setOpaque(false);

		JPanel top = new JPanel(new GridLayout(6, 1, 0, 6));
		top.setOpaque(false);

		JPanel bestPlayers = new JPanel(new BorderLayout());
		bestPlayers.setOpaque(false);
		bestPlayers.add(new SectionTitle("MEILLEURS JOUEURS", ""), BorderLayout.NORTH);

		if (home) {
			homePointsLabel = new JLabel("Points : 0");
			homeReboundsLabel = new JLabel("Rebonds : 0");
			homeAssistsLabel = new JLabel("Passes : 0");
			homeTurnoversLabel = new JLabel("Turnovers : 0");
			homeFgLabel = new JLabel("FG% : 0");
			homeThreeLabel = new JLabel("3PT% : 0");
			homeBestPlayersLabel = new JLabel("-");

			top.add(homePointsLabel);
			top.add(homeReboundsLabel);
			top.add(homeAssistsLabel);
			top.add(homeTurnoversLabel);
			top.add(homeFgLabel);
			top.add(homeThreeLabel);
			bestPlayers.add(homeBestPlayersLabel, BorderLayout.CENTER);
		} else {
			awayPointsLabel = new JLabel("Points : 0");
			awayReboundsLabel = new JLabel("Rebonds : 0");
			awayAssistsLabel = new JLabel("Passes : 0");
			awayTurnoversLabel = new JLabel("Turnovers : 0");
			awayFgLabel = new JLabel("FG% : 0");
			awayThreeLabel = new JLabel("3PT% : 0");
			awayBestPlayersLabel = new JLabel("-");

			top.add(awayPointsLabel);
			top.add(awayReboundsLabel);
			top.add(awayAssistsLabel);
			top.add(awayTurnoversLabel);
			top.add(awayFgLabel);
			top.add(awayThreeLabel);
			bestPlayers.add(awayBestPlayersLabel, BorderLayout.CENTER);
		}

		panel.add(top, BorderLayout.NORTH);
		panel.add(bestPlayers, BorderLayout.CENTER);
		return panel;
	}

	private void buildLiveActions() {
		liveActions.clear();
		if (game == null || game.getQuarterResults() == null) {
			return;
		}
		GameResult[] quarterResults = game.getQuarterResults();
		for (int i = 0; i < quarterResults.length; i++) {
			if (quarterResults[i] == null) {
				continue;
			}
			if (quarterResults[i].getActions() == null) {
				continue;
			}
			for (ActionResult action : quarterResults[i].getActions()) {
				liveActions.add(new LiveMatchStatistics.LiveAction(i + 1, action));
			}
		}
	}

	private void playNextAction() {
		if (liveActionIndex >= liveActions.size()) {
			revealGameDay();
			stopLiveThread();
			return;
		}
		LiveMatchStatistics.LiveAction liveAction = liveActions.get(liveActionIndex);
		liveMatchStatistics.applyAction(liveAction.getAction(), new LiveMatchStatistics.HomePlayerChecker() {
			@Override
			public boolean isHomePlayer(Player player) {
				return LiveMatchDashboard.this.isHomePlayer(player);
			}
		});
		liveActionIndex++;
		updateLiveDashboard();
		if (liveActionIndex >= liveActions.size()) {
			revealGameDay();
		}
	}

	private void updateLiveDashboard() {
		scoreLabel.setText(homeTeamName + " " + liveMatchStatistics.getHomePoints() + " - "
				+ liveMatchStatistics.getAwayPoints() + " " + awayTeamName);
		quarterTimeLabel.setText(buildQuarterTimeText());
		statusLabel.setText(buildStatusText());

		homePointsLabel.setText("Points : " + liveMatchStatistics.getHomePoints());
		homeReboundsLabel.setText("Rebonds : " + liveMatchStatistics.getHomeRebounds());
		homeAssistsLabel.setText("Passes : " + liveMatchStatistics.getHomeAssists());
		homeTurnoversLabel.setText("Turnovers : " + liveMatchStatistics.getHomeTurnovers());
		homeFgLabel.setText("FG% : " + liveMatchStatistics.getHomeFgPercent());
		homeThreeLabel.setText("3PT% : " + liveMatchStatistics.getHomeThreePercent());
		homeBestPlayersLabel.setText(liveMatchStatistics.getHomeBestPlayersText());

		awayPointsLabel.setText("Points : " + liveMatchStatistics.getAwayPoints());
		awayReboundsLabel.setText("Rebonds : " + liveMatchStatistics.getAwayRebounds());
		awayAssistsLabel.setText("Passes : " + liveMatchStatistics.getAwayAssists());
		awayTurnoversLabel.setText("Turnovers : " + liveMatchStatistics.getAwayTurnovers());
		awayFgLabel.setText("FG% : " + liveMatchStatistics.getAwayFgPercent());
		awayThreeLabel.setText("3PT% : " + liveMatchStatistics.getAwayThreePercent());
		awayBestPlayersLabel.setText(liveMatchStatistics.getAwayBestPlayersText());

		refreshLiveRows();
	}

	private String buildQuarterTimeText() {
		if (liveActionIndex <= 0 || liveActions.isEmpty()) {
			return "Q1 12:00";
		}
		LiveMatchStatistics.LiveAction current = liveActions.get(liveActionIndex - 1);
		int quarter = current.getQuarter();
		int remaining = SimulationConfiguration.QUARTER_DURATION - current.getAction().getActionTime();
		if (remaining < 0) {
			remaining = 0;
		}
		int min = remaining / 60;
		int sec = remaining % 60;
		return "Q" + quarter + " " + String.format("%d:%02d", min, sec);
	}

	private String buildStatusText() {
		if (game == null) {
			return "Aucun match selectionne";
		}
		if (!isReplayAvailable()) {
			return "Match non encore simule";
		}
		if (liveActionIndex >= liveActions.size()) {
			return "Lecture terminee";
		}
		return buildQuarterTimeText();
	}

	private void refreshLiveRows() {
		for (int i = 0; i < LIVE_ROWS; i++) {
			int actionIndex = liveActionIndex - LIVE_ROWS + i;
			if (actionIndex < 0 || actionIndex >= liveActionIndex) {
				actionRows[i].setText(" ");
			} else {
				actionRows[i].setText(buildActionLabel(liveActions.get(actionIndex)));
			}
		}
	}

	private String buildActionLabel(LiveMatchStatistics.LiveAction liveAction) {
		ActionResult action = liveAction.getAction();
		int remaining = SimulationConfiguration.QUARTER_DURATION - action.getActionTime();
		if (remaining < 0) {
			remaining = 0;
		}
		int min = remaining / 60;
		int sec = remaining % 60;
		return "Q" + liveAction.getQuarter() + " " + String.format("%d:%02d", min, sec) + " - "
				+ action.accept(new LiveActionTextVisitor());
	}

	private void startLiveThread() {
		if (isLiveRunning) {
			return;
		}
		isLiveRunning = true;
		liveThread = new Thread(new LiveRunner());
		liveThread.start();
	}

	private void stopLiveThread() {
		isLiveRunning = false;
		if (liveThread != null) {
			liveThread.interrupt();
		}
	}

	private void moveToNextQuarter() {
		if (!isReplayAvailable() || liveActionIndex >= liveActions.size()) {
			return;
		}
		int currentQuarter = liveActionIndex == 0 ? 1 : liveActions.get(liveActionIndex - 1).getQuarter();
		while (liveActionIndex < liveActions.size() && liveActions.get(liveActionIndex).getQuarter() == currentQuarter) {
			playNextAction();
		}
	}

	private void resetLiveState() {
		liveActionIndex = 0;
		liveMatchStatistics.reset();
	}

	private boolean isHomePlayer(Player player) {
		if (game == null || player == null) {
			return false;
		}
		return game.getGameContext().getHomeTeam().getPlayers().containsKey(player.getName());
	}

	private boolean isGameSimulated(Game game) {
		if (game == null || game.getQuarterResults() == null || game.getQuarterResults().length == 0) {
			return false;
		}
		for (GameResult quarterResult : game.getQuarterResults()) {
			if (quarterResult == null || quarterResult.getActions() == null || quarterResult.getActions().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	private boolean isReplayAvailable() {
		return isGameSimulated(game) && !liveActions.isEmpty();
	}

	private void revealGameDay() {
		if (leagueManager == null || gameDate == null) {
			return;
		}
		data.calendar.GameDay gameDay = leagueManager.getLeague().getReagularSeason().getCalendar().getCalendar().get(gameDate);
		if (gameDay != null) {
			gameDay.setDisplayed(true);
		}
	}

	private class BackAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			stopLiveThread();
			if (backToMatchAction != null) {
				backToMatchAction.run();
			}
		}
	}

	private class PlayAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isReplayAvailable()) {
				updateLiveDashboard();
				return;
			}
			if (liveActionIndex > liveActions.size()) {
				resetLiveState();
			}
			updateLiveDashboard();
			startLiveThread();
		}
	}

	private class PauseAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			stopLiveThread();
		}
	}

	private class NextQuarterAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			moveToNextQuarter();
		}
	}

	private class LiveRunner implements Runnable {
		@Override
		public void run() {
			while (isLiveRunning) {
				try {
					Thread.sleep(LIVE_DELAY_MS);
				} catch (InterruptedException e) {
					return;
				}
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (isLiveRunning) {
							playNextAction();
						}
					}
				});
			}
		}
	}

	private class LiveActionTextVisitor implements ActionResultVisitor<String> {
		@Override
		public String visit(PointScored pointScored) {
			Player scorer = pointScored.getScorerPlayer();
			String team = isHomePlayer(scorer) ? homeTeamName : awayTeamName;
			return team + " - " + scorer.getName() + " + " + computeDisplayedPoints(pointScored) + " pts";
		}

		@Override
		public String visit(Turnover turnover) {
			Player intercepted = turnover.getInterceptedPlayer();
			String team = isHomePlayer(intercepted) ? homeTeamName : awayTeamName;
			return team + " - Ballon perdu " + intercepted.getName();
		}

		@Override
		public String visit(Block block) {
			Player blocker = block.getBlockingPlayer();
			String team = isHomePlayer(blocker) ? homeTeamName : awayTeamName;
			return team + " - Contre " + blocker.getName();
		}

		@Override
		public String visit(Rebound rebound) {
			Player reboundPlayer = rebound.getReboundPlayer();
			String team = isHomePlayer(reboundPlayer) ? homeTeamName : awayTeamName;
			return team + " - Rebond " + reboundPlayer.getName();
		}

		@Override
		public String visit(EndOfTime endOfTime) {
			return "Fin de période";
		}
	}

	private int computeDisplayedPoints(PointScored pointScored) {
		if (pointScored.getOffensiveAction() == null) {
			return pointScored.getPointsScored();
		}
		String offensiveName = pointScored.getOffensiveAction().getName();
		if (SimulationConfiguration.THREEPOINT.equals(offensiveName)) {
			return 3;
		}
		if (SimulationConfiguration.TWOPOINT.equals(offensiveName)) {
			return 2;
		}
		return 1;
	}
}
