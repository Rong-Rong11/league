package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.LabelStyleUtil;
import gui.panel.common.ThemeAware;

public class LoadingDashboard extends JPanel implements ThemeAware {
	private static final String DEFAULT_SUBTITLE = "Preparation du calendrier, des matchs et des finances...";
	private static final int STEP_DELAY_MS = 12;
	private static final double MIN_PROGRESS_STEP = 0.75;
	private static final double PROGRESS_CATCHUP_FACTOR = 0.28;

	private JLabel titleLabel;
	private JLabel subtitleLabel;
	private JProgressBar progressBar;
	private Timer loadingTimer;
	private LoadingPhase[] loadingPhases;
	private int loadingPhaseIndex;
	private double currentProgress;

	public LoadingDashboard() {
		create();
		organize();
		applyTheme();
	}

	private void create() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(120, 220, 120, 220));

		titleLabel = new JLabel("Chargement de la saison", JLabel.CENTER);
		LabelStyleUtil.styleTitleLabel(titleLabel, 38);

		subtitleLabel = new JLabel(DEFAULT_SUBTITLE, JLabel.CENTER);
		LabelStyleUtil.styleSubtitleLabel(subtitleLabel, 16);

		progressBar = new JProgressBar(0, 100);
		progressBar.setStringPainted(true);
		progressBar.setValue(0);
	}

	private void organize() {
		JPanel centerPanel = new JPanel(new BorderLayout(0, 28));
		centerPanel.setOpaque(false);

		JPanel textPanel = new JPanel(new BorderLayout(0, 12));
		textPanel.setOpaque(false);
		textPanel.add(titleLabel, BorderLayout.NORTH);
		textPanel.add(subtitleLabel, BorderLayout.CENTER);

		centerPanel.add(textPanel, BorderLayout.NORTH);
		centerPanel.add(progressBar, BorderLayout.CENTER);

		add(centerPanel, BorderLayout.CENTER);
	}

	public void reset() {
		setStatusText(DEFAULT_SUBTITLE);
		setProgressValue(0);
	}

	public void setProgress(int value, String text) {
		setStatusText(text);
		setProgressValue(value);
	}

	public void setProgressValue(int value) {
		progressBar.setValue(value);
		progressBar.setString(value + "%");
	}

	public void setStatusText(String text) {
		subtitleLabel.setText(text);
	}

	public int getProgressValue() {
		return progressBar.getValue();
	}

	public void startLoadingSequence(LoadingSequenceHandler handler) {
		stopTimer();
		reset();
		loadingPhases = createSteps(handler);
		loadingPhaseIndex = 0;
		currentProgress = 0;
		loadingTimer = new Timer(STEP_DELAY_MS, new LoadingTimerAction());
		loadingTimer.start();
	}

	public interface LoadingSequenceHandler {
		void initializeSeason();

		void loadMatches();

		void finishLoading();
	}

	private LoadingPhase[] createSteps(LoadingSequenceHandler handler) {
		return new LoadingPhase[] {
				new LoadingPhase(12, "Preparation de la saison...", null),
				new LoadingPhase(26, "Verification des donnees...", null),
				new LoadingPhase(40, "Organisation de la saison...", null),
				new LoadingPhase(55, "Initialisation du calendrier et des finances...", new InitializeSeasonTask(handler)),
				new LoadingPhase(68, "Calendrier pret...", null),
				new LoadingPhase(80, "Preparation des tableaux de bord...", null),
				new LoadingPhase(92, "Chargement des matchs et des tableaux de bord...", new LoadMatchesTask(handler)),
				new LoadingPhase(97, "Chargement du premier jour...", null),
				new LoadingPhase(100, "Ouverture de la simulation...", new FinishLoadingTask(handler))
		};
	}

	private void runStep() {
		if (loadingPhases == null || loadingPhaseIndex >= loadingPhases.length) {
			stopTimer();
			return;
		}

		LoadingPhase phase = loadingPhases[loadingPhaseIndex];
		currentProgress = computeNextProgress(currentProgress, phase.targetProgress);
		setProgress((int) Math.round(currentProgress), phase.text);

		if (currentProgress + 0.001 < phase.targetProgress) {
			return;
		}

		currentProgress = phase.targetProgress;
		setProgress(phase.targetProgress, phase.text);
		if (phase.action != null) {
			phase.action.run();
		}

		loadingPhaseIndex++;
		if (loadingPhaseIndex >= loadingPhases.length) {
			stopTimer();
		}
	}

	private double computeNextProgress(double currentValue, int targetValue) {
		double remaining = targetValue - currentValue;
		if (remaining <= 0) {
			return targetValue;
		}
		double step = Math.max(MIN_PROGRESS_STEP, remaining * PROGRESS_CATCHUP_FACTOR);
		return Math.min(currentValue + step, targetValue);
	}

	private void stopTimer() {
		if (loadingTimer != null) {
			loadingTimer.stop();
			loadingTimer = null;
		}
	}

	private class LoadingTimerAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			runStep();
		}
	}

	private class LoadingPhase {
		private int targetProgress;
		private String text;
		private Runnable action;

		private LoadingPhase(int targetProgress, String text, Runnable action) {
			this.targetProgress = targetProgress;
			this.text = text;
			this.action = action;
		}
	}

	private class InitializeSeasonTask implements Runnable {
		private LoadingSequenceHandler handler;

		private InitializeSeasonTask(LoadingSequenceHandler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			if (handler != null) {
				handler.initializeSeason();
			}
		}
	}

	private class LoadMatchesTask implements Runnable {
		private LoadingSequenceHandler handler;

		private LoadMatchesTask(LoadingSequenceHandler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			if (handler != null) {
				handler.loadMatches();
			}
		}
	}

	private class FinishLoadingTask implements Runnable {
		private LoadingSequenceHandler handler;

		private FinishLoadingTask(LoadingSequenceHandler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			if (handler != null) {
				handler.finishLoading();
			}
		}
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
		LabelStyleUtil.styleTitleLabel(titleLabel, 38);
		LabelStyleUtil.styleSubtitleLabel(subtitleLabel, 16);
		progressBar.setForeground(DashboardPanelUtil.getPrimaryActionColor());
		progressBar.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
	}
}
