package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import data.sport.setup.Game;
import gui.panel.common.BuildBox;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.matchPanel.liveMatchPanel.LiveActionsPanel;
import gui.panel.matchPanel.liveMatchPanel.LiveMatchHeaderPanel;
import gui.panel.matchPanel.liveMatchPanel.LiveTeamStatsPanel;
import process.orchestrator.GUIInterface;
import process.service.live.LiveMatchState;

public class LiveMatchDashboard extends JPanel implements Runnable {
	private static final int DASHBOARD_SPACING = 16;
	private static final int SIDE_COLUMN_WIDTH = 270;
	private static final int LIVE_ROWS = 10;
	private static final int CHRONO_SPEED = 200;
	private static final Color BACKGROUND_COLOR = new Color(247, 248, 250);

	private GUIInterface guiInterface;
	private Runnable backToMatchAction;
	private Thread liveThread;

	private LiveMatchHeaderPanel headerPanel;
	private LiveActionsPanel liveActionsPanel;
	private LiveTeamStatsPanel homeStatsPanel;
	private LiveTeamStatsPanel awayStatsPanel;

	public LiveMatchDashboard(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		create();
		organize();
		actions();
		renderState();
	}

	private void create() {
		headerPanel = new LiveMatchHeaderPanel();
		liveActionsPanel = new LiveActionsPanel(LIVE_ROWS);
		homeStatsPanel = new LiveTeamStatsPanel();
		awayStatsPanel = new LiveTeamStatsPanel();
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
		JPanel content = new JPanel(new BorderLayout(DASHBOARD_SPACING, DASHBOARD_SPACING));
		content.setOpaque(false);
		content.setBorder(BorderFactory.createEmptyBorder(
				DASHBOARD_SPACING, DASHBOARD_SPACING, DASHBOARD_SPACING, DASHBOARD_SPACING));
		return content;
	}

	private JPanel buildHeader() {
		return headerPanel;
	}

	private JPanel buildBody() {
		JPanel body = new JPanel(new BorderLayout(DASHBOARD_SPACING, 0));
		body.setOpaque(false);
		body.add(buildLeftColumn(), BorderLayout.WEST);
		body.add(buildCenterColumn(), BorderLayout.CENTER);
		body.add(buildRightColumn(), BorderLayout.EAST);
		return body;
	}

	private JPanel buildLeftColumn() {
		JPanel leftColumn = new JPanel(new BorderLayout());
		leftColumn.setOpaque(false);
		leftColumn.setPreferredSize(new Dimension(SIDE_COLUMN_WIDTH, 10));
		leftColumn.add(new BuildBox("ÉQUIPE DOMICILE", "Statistiques", homeStatsPanel), BorderLayout.CENTER);
		return leftColumn;
	}

	private JPanel buildCenterColumn() {
		return new BuildBox("ACTIONS EN DIRECT", "Déroulement du match", liveActionsPanel);
	}

	private JPanel buildRightColumn() {
		JPanel rightColumn = new JPanel(new BorderLayout());
		rightColumn.setOpaque(false);
		rightColumn.setPreferredSize(new Dimension(SIDE_COLUMN_WIDTH, 10));
		rightColumn.add(new BuildBox("ÉQUIPE EXTÉRIEUR", "Statistiques", awayStatsPanel), BorderLayout.CENTER);
		return rightColumn;
	}

	private void actions() {
		headerPanel.getBackButton().addActionListener(new BackAction());
		headerPanel.getPlayButton().addActionListener(new PlayAction());
		headerPanel.getNextQuarterButton().addActionListener(new PlayQuarterAction());
		headerPanel.getPauseButton().addActionListener(new PauseAction());
	}

	public void setBackToMatchAction(Runnable backToMatchAction) {
		this.backToMatchAction = backToMatchAction;
	}

	public void setGame(Game game) {
		stopLiveReading();
		guiInterface.setLiveGame(game);
		renderState();
	}

	private void startLiveReading() {
		guiInterface.startLiveMatch();
		if (!guiInterface.isLiveMatchRunning() || (liveThread != null && liveThread.isAlive())) {
			return;
		}
		liveThread = new Thread(this, "live-match-thread");
		liveThread.start();
	}

	private void stopLiveReading() {
		guiInterface.pauseLiveMatch();
		if (liveThread != null) {
			liveThread.interrupt();
			liveThread = null;
		}
	}

	public void run() {
		while (guiInterface.isLiveMatchRunning()) {
			try {
				Thread.sleep(CHRONO_SPEED);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			guiInterface.tickLiveMatch();
			SwingUtilities.invokeLater(new UpdateValuesRunnable());
			if (Thread.currentThread().isInterrupted()) {
				break;
			}
		}
		liveThread = null;
	}

	private void renderState() {
		LiveMatchState state = guiInterface.getCurrentLiveState();
		headerPanel.updateHeader(state.getHomeTeam(), state.getAwayTeam(), state.getHomePoints(), state.getAwayPoints(),
				state.getQuarterLabel(), state.getQuarterTimeText());
		homeStatsPanel.updateStats(state.getHomePoints(), state.getHomeRebounds(), state.getHomeAssists(),
				state.getHomeTurnovers(), state.getHomeFgPercent(), state.getHomeThreePercent(),
				state.getHomeBestPlayers());
		awayStatsPanel.updateStats(state.getAwayPoints(), state.getAwayRebounds(), state.getAwayAssists(),
				state.getAwayTurnovers(), state.getAwayFgPercent(), state.getAwayThreePercent(),
				state.getAwayBestPlayers());
		liveActionsPanel.updateRows(state.getDisplayedRows(), state.getCenterMessage());
	}

	private class UpdateValuesRunnable implements Runnable {
		@Override
		public void run() {
			renderState();
		}
	}

	private class BackAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			stopLiveReading();
			if (backToMatchAction != null) {
				backToMatchAction.run();
			}
		}
	}

	private class PlayAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			startLiveReading();
			renderState();
		}
	}

	private class PlayQuarterAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			stopLiveReading();
			guiInterface.playCurrentLiveQuarter();
			renderState();
		}
	}

	private class PauseAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			stopLiveReading();
		}
	}
}
