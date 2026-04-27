package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.ThemeAware;

public class LoadingDashboard extends JPanel implements ThemeAware {
	private static final String DEFAULT_SUBTITLE = "Preparation du calendrier, des matchs et des finances...";
	private static final int STEP_DELAY_MS = 80;
	private static final int FINAL_DELAY_MS = 120;

	private JLabel titleLabel;
	private JLabel subtitleLabel;
	private JProgressBar progressBar;

	public LoadingDashboard() {
		create();
		organize();
		applyTheme();
	}

	private void create() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(120, 220, 120, 220));

		titleLabel = new JLabel("Chargement de la saison", JLabel.CENTER);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 38));

		subtitleLabel = new JLabel(DEFAULT_SUBTITLE, JLabel.CENTER);
		subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

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
		Thread loadingThread = new Thread(new LoadingSequenceWorker(handler), "loading-sequence");
		loadingThread.start();
	}

	public interface LoadingSequenceHandler {
		void initializeSeason();

		void loadMatches();

		void finishLoading();
	}

	private class LoadingSequenceWorker implements Runnable {
		private LoadingSequenceHandler handler;

		private LoadingSequenceWorker(LoadingSequenceHandler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			try {
				updateProgress(0, DEFAULT_SUBTITLE);
				advanceStep(8, "Preparation de la saison...");
				advanceStep(18, "Verification des donnees...");
				advanceStep(30, "Organisation de la saison...");

				updateProgress(42, "Initialisation du calendrier et des finances...");
				runSeasonInitialization();
				advanceStep(58, "Calendrier pret...");
				advanceStep(70, "Preparation des tableaux de bord...");

				updateProgress(82, "Chargement des matchs et des tableaux de bord...");
				runMatchLoading();
				advanceStep(90, "Chargement du premier jour...");
				advanceStep(96, "Finalisation de l ouverture...");
				updateProgress(100, "Ouverture de la simulation...");
				pause(FINAL_DELAY_MS);
				runFinishLoading();
			} catch (InterruptedException exception) {
				Thread.currentThread().interrupt();
			}
		}

		private void runSeasonInitialization() {
			if (handler != null) {
				handler.initializeSeason();
			}
		}

		private void runMatchLoading() {
			runOnUiThread(new MatchLoadingTask(handler));
		}

		private void runFinishLoading() {
			runOnUiThread(new FinishLoadingTask(handler));
		}
	}

	private void advanceStep(int progress, String text) throws InterruptedException {
		updateProgress(progress, text);
		pause(STEP_DELAY_MS);
	}

	private void updateProgress(final int progress, final String text) {
		runOnUiThread(new ProgressUpdateTask(progress, text));
	}

	private void runOnUiThread(Runnable action) {
		if (action == null) {
			return;
		}
		if (SwingUtilities.isEventDispatchThread()) {
			action.run();
			return;
		}
		SwingUtilities.invokeLater(action);
	}

	private void pause(int delayMs) throws InterruptedException {
		Thread.sleep(delayMs);
	}

	private class ProgressUpdateTask implements Runnable {
		private int progress;
		private String text;

		private ProgressUpdateTask(int progress, String text) {
			this.progress = progress;
			this.text = text;
		}

		@Override
		public void run() {
			setProgress(progress, text);
		}
	}

	private class MatchLoadingTask implements Runnable {
		private LoadingSequenceHandler handler;

		private MatchLoadingTask(LoadingSequenceHandler handler) {
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
		titleLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		subtitleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		progressBar.setForeground(DashboardPanelUtil.getPrimaryActionColor());
		progressBar.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
	}
}
