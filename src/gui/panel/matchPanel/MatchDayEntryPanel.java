package gui.panel.matchPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import data.sport.setup.Game;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.ThemeAware;
import process.utility.TeamDisplayUtility;

public class MatchDayEntryPanel extends JPanel implements ThemeAware {
	private static final Color TITLE_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color TEXT_COLOR = new Color(90, 90, 90);
	private static final Color UPCOMING_COLOR = DashboardPanelUtil.ACCENT_RED_COLOR;

	private JPanel centerPanel;
	private JPanel textPanel;
	private JButton detailButton;
	private JLabel teamLabel;
	private JLabel opponentLabel;
	private JLabel statusLabel;
	private JLabel[] scoreLabels;
	private int rowIndex;

	public MatchDayEntryPanel(final Game game, boolean displayed, int index,
			final MatchDayListPanel.MatchSelectionListener matchSelectionListener) {
		super(new BorderLayout(10, 0));
		rowIndex = index;
		setOpaque(true);
		setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		applyRowBorder();

		centerPanel = new JPanel(new BorderLayout(8, 0));
		centerPanel.setOpaque(true);
		centerPanel.setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);

		textPanel = new JPanel();
		textPanel.setOpaque(true);
		textPanel.setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

		teamLabel = createTeamLabel(TeamDisplayUtility.getShortName(game.getGameContext().getHomeTeam()));
		opponentLabel = createOpponentLabel(TeamDisplayUtility.getShortName(game.getGameContext().getAwayTeam()));
		statusLabel = createStatusLabel(displayed ? "Termine" : "A venir", displayed);
		textPanel.add(teamLabel);
		textPanel.add(opponentLabel);
		textPanel.add(statusLabel);
		centerPanel.add(textPanel, BorderLayout.CENTER);

		if (displayed) {
			JPanel scorePanel = new JPanel();
			scorePanel.setOpaque(false);
			scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
			scorePanel.setPreferredSize(new Dimension(40, 30));
			scoreLabels = new JLabel[] { createScoreLabel(game.getHomeFinalScore()),
					createScoreLabel(game.getAwayFinalScore()) };
			scorePanel.add(scoreLabels[0]);
			scorePanel.add(scoreLabels[1]);
			centerPanel.add(scorePanel, BorderLayout.EAST);
		}

		centerPanel.addMouseListener(new SelectMatchMouseListener(game, matchSelectionListener));

		detailButton = new RoundedButton(">");
		detailButton.setFocusPainted(false);
		detailButton.setBorderPainted(false);
		detailButton.setContentAreaFilled(false);
		detailButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 28));
		detailButton.addActionListener(new DetailButtonListener(game, matchSelectionListener));

		add(centerPanel, BorderLayout.CENTER);
		add(detailButton, BorderLayout.EAST);
		applyTheme();
	}

	private JLabel createTeamLabel(String text) {
		JLabel label = new JLabel(text);
		label.setForeground(TITLE_COLOR);
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		return label;
	}

	private JLabel createOpponentLabel(String text) {
		JLabel label = new JLabel(text);
		label.setForeground(TEXT_COLOR);
		label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
		return label;
	}

	private JLabel createStatusLabel(String text, boolean displayed) {
		JLabel label = new JLabel(text);
		label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
		label.setForeground(displayed ? TEXT_COLOR : UPCOMING_COLOR);
		return label;
	}

	private JLabel createScoreLabel(int score) {
		JLabel label = new JLabel(String.valueOf(score), SwingConstants.RIGHT);
		label.setForeground(TITLE_COLOR);
		label.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setPreferredSize(new Dimension(46, 18));
		label.setMinimumSize(new Dimension(46, 18));
		label.setMaximumSize(new Dimension(46, 18));
		label.setAlignmentX(RIGHT_ALIGNMENT);
		return label;
	}

	private class SelectMatchMouseListener implements MouseListener {
		private Game game;
		private MatchDayListPanel.MatchSelectionListener matchSelectionListener;

		private SelectMatchMouseListener(Game game, MatchDayListPanel.MatchSelectionListener matchSelectionListener) {
			this.game = game;
			this.matchSelectionListener = matchSelectionListener;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (matchSelectionListener != null) {
				matchSelectionListener.onMatchSelected(game);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
	}

	private class DetailButtonListener implements ActionListener {
		private Game game;
		private MatchDayListPanel.MatchSelectionListener matchSelectionListener;

		private DetailButtonListener(Game game, MatchDayListPanel.MatchSelectionListener matchSelectionListener) {
			this.game = game;
			this.matchSelectionListener = matchSelectionListener;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (matchSelectionListener != null) {
				matchSelectionListener.onMatchDetail(game);
			}
		}
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		applyRowBorder();
		if (centerPanel != null) {
			centerPanel.setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		}
		if (textPanel != null) {
			textPanel.setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		}
		if (teamLabel != null) {
			teamLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		}
		if (opponentLabel != null) {
			opponentLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		}
		if (statusLabel != null) {
			if ("A venir".equals(statusLabel.getText())) {
				statusLabel.setForeground(DashboardPanelUtil.ACCENT_RED_COLOR);
			} else {
				statusLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
			}
		}
		if (scoreLabels != null) {
			for (int i = 0; i < scoreLabels.length; i++) {
				scoreLabels[i].setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
			}
		}
		if (detailButton != null) {
			detailButton.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
			detailButton.setForeground(DashboardPanelUtil.BUTTON_TEXT_COLOR);
		}
	}

	private void applyRowBorder() {
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(rowIndex == 0 ? 1 : 0, 0, 1, 0, DashboardPanelUtil.BORDER_COLOR),
				BorderFactory.createEmptyBorder(8, 10, 8, 10)));
	}
}
