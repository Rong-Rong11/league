package gui.panel.matchPanel;

import data.sport.setup.Game;
import process.utility.TeamDisplayUtil;

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

import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.RoundedButton;

public class MatchDayEntryPanel extends JPanel {
	private static final Color TITLE_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color TEXT_COLOR = new Color(90, 90, 90);
	private static final Color UPCOMING_COLOR = new Color(0x2F, 0x80, 0xA9);
	private static final Color SEPARATOR_COLOR = new Color(225, 225, 225);

	public MatchDayEntryPanel(final Game game, boolean displayed, int index,
			final MatchDayListPanel.MatchSelectionListener matchSelectionListener) {
		super(new BorderLayout(10, 0));
		setOpaque(true);
		setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(index == 0 ? 1 : 0, 0, 1, 0, SEPARATOR_COLOR),
				BorderFactory.createEmptyBorder(8, 10, 8, 10)));

		JPanel centerPanel = new JPanel(new BorderLayout(8, 0));
		centerPanel.setOpaque(true);
		centerPanel.setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);

		JPanel textPanel = new JPanel();
		textPanel.setOpaque(true);
		textPanel.setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

		textPanel.add(createTeamLabel(TeamDisplayUtil.getShortName(game.getGameContext().getHomeTeam())));
		textPanel.add(createOpponentLabel(TeamDisplayUtil.getShortName(game.getGameContext().getAwayTeam())));
		textPanel.add(createStatusLabel(displayed ? "Termine" : "A venir", displayed));
		centerPanel.add(textPanel, BorderLayout.CENTER);

		if (displayed) {
			JPanel scorePanel = new JPanel();
			scorePanel.setOpaque(false);
			scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
			scorePanel.setPreferredSize(new Dimension(40, 30));
			scorePanel.add(createScoreLabel(game.getHomeFinalScore()));
			scorePanel.add(createScoreLabel(game.getAwayFinalScore()));
			centerPanel.add(scorePanel, BorderLayout.EAST);
		}

		centerPanel.addMouseListener(new SelectMatchMouseListener(game, matchSelectionListener));

		JButton detailButton = new RoundedButton(">");
		detailButton.setFocusPainted(false);
		detailButton.setBorderPainted(false);
		detailButton.setContentAreaFilled(false);
		detailButton.setForeground(TEXT_COLOR);
		detailButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 28));
		detailButton.addActionListener(new DetailButtonListener(game, matchSelectionListener));

		add(centerPanel, BorderLayout.CENTER);
		add(detailButton, BorderLayout.EAST);
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
}
