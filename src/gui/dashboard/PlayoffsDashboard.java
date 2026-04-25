package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import gui.components.PlayoffsImageBracketPanel;
import gui.panel.common.RoundedButton;
import gui.panel.common.RoundedPanel;
import process.orchestrator.interf.GUIInterface;

public class PlayoffsDashboard extends JPanel implements RefreshableDashboard {
	private static final Color BACKGROUND = new Color(0xF7, 0xF8, 0xFA);
	private static final Color MAIN_BLUE = new Color(0x17, 0x31, 0x74);
	private static final Color SECONDARY_TEXT = new Color(0x6D, 0x75, 0x83);
	private static final Color CARD_BORDER = new Color(0xDC, 0xE0, 0xE6);

	private GUIInterface guiInterface;
	private PlayoffsImageBracketPanel bracketPanel;
	private JLabel roundValueLabel;
	private JLabel remainingValueLabel;
	private JLabel lastWinnerValueLabel;
	private JLabel championValueLabel;

	public PlayoffsDashboard(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		create();
		organize();
		actions();
		refresh();
	}

	private void create() {
		bracketPanel = new PlayoffsImageBracketPanel();
		roundValueLabel = createSummaryValueLabel();
		remainingValueLabel = createSummaryValueLabel();
		lastWinnerValueLabel = createSummaryValueLabel();
		championValueLabel = createSummaryValueLabel();
	}

	private void organize() {
		setLayout(new BorderLayout(18, 18));
		setBackground(BACKGROUND);
		setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
		add(buildHeader(), BorderLayout.NORTH);
		add(buildBody(), BorderLayout.CENTER);
	}

	private JPanel buildHeader() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);

		JPanel textPanel = new JPanel();
		textPanel.setOpaque(false);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		JLabel titleLabel = new JLabel("Playoffs");
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 34));
		titleLabel.setForeground(MAIN_BLUE);
		JLabel subtitleLabel = new JLabel("Suivez la progression des equipes qualifiees jusqu'au champion final");
		subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		subtitleLabel.setForeground(SECONDARY_TEXT);
		textPanel.add(titleLabel);
		textPanel.add(Box.createVerticalStrut(6));
		textPanel.add(subtitleLabel);

		panel.add(textPanel, BorderLayout.CENTER);
		panel.add(buildActionsPanel(), BorderLayout.EAST);
		return panel;
	}

	private JPanel buildActionsPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		panel.setOpaque(false);
		JButton nextMatchButton = buildActionButton("Simuler prochain match");
		JButton nextRoundButton = buildActionButton("Simuler prochain tour");
		JButton allButton = buildActionButton("Simuler tous les playoffs");
		nextMatchButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				guiInterface.simulateNextPlayoffMatch();
				refresh();
			}
		});
		nextRoundButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				guiInterface.simulateNextPlayoffRound();
				refresh();
			}
		});
		allButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				guiInterface.simulateAllPlayoffs();
				refresh();
			}
		});
		panel.add(nextMatchButton);
		panel.add(nextRoundButton);
		panel.add(allButton);
		return panel;
	}

	private JButton buildActionButton(String text) {
		JButton button = new RoundedButton(text);
		button.setPreferredSize(new Dimension(210, 44));
		button.setBackground(MAIN_BLUE);
		button.setForeground(Color.WHITE);
		button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		button.setFocusPainted(false);
		return button;
	}

	private JPanel buildBody() {
		JPanel panel = new JPanel(new BorderLayout(18, 0));
		panel.setOpaque(false);

		JScrollPane scrollPane = new JScrollPane(bracketPanel);
		scrollPane.setBorder(BorderFactory.createLineBorder(CARD_BORDER, 1));
		scrollPane.getViewport().setBackground(Color.WHITE);
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(buildSummaryPanel(), BorderLayout.EAST);
		return panel;
	}

	private JPanel buildSummaryPanel() {
		RoundedPanel panel = new RoundedPanel(new GridLayout(4, 1, 0, 16), 18);
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(CARD_BORDER, 1),
				BorderFactory.createEmptyBorder(22, 22, 22, 22)));
		panel.setPreferredSize(new Dimension(280, 0));
		panel.add(buildSummaryItem("Tour actuel", roundValueLabel));
		panel.add(buildSummaryItem("Matchs restants", remainingValueLabel));
		panel.add(buildSummaryItem("Dernier vainqueur", lastWinnerValueLabel));
		panel.add(buildSummaryItem("Champion", championValueLabel));
		return panel;
	}

	private JPanel buildSummaryItem(String title, JLabel valueLabel) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		titleLabel.setForeground(SECONDARY_TEXT);
		panel.add(titleLabel);
		panel.add(Box.createVerticalStrut(6));
		panel.add(valueLabel);
		return panel;
	}

	private JLabel createSummaryValueLabel() {
		JLabel label = new JLabel("-");
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
		label.setForeground(MAIN_BLUE);
		return label;
	}

	private String getRoundLabel() {
		if (guiInterface.getCurrentPlayoffRound() == null) {
			return "A venir";
		}
		switch (guiInterface.getCurrentPlayoffRound()) {
		case FIRST_ROUND:
			return "Premier tour";
		case CONFERENCE_SEMIFINALS:
			return "Demi-finales";
		case CONFERENCE_FINALS:
			return "Finales conf.";
		case NBA_FINALS:
			return "Finales NBA";
		case FINISHED:
			return "Termine";
		default:
			return guiInterface.getCurrentPlayoffRound().name();
		}
	}

	private void actions() {
	}

	@Override
	public void refresh() {
		bracketPanel.refreshFromPlayoffsData(guiInterface.getPlayoffPositionMap());
		roundValueLabel.setText(getRoundLabel());
		remainingValueLabel.setText(String.valueOf(guiInterface.getRemainingPlayoffGames()));
		lastWinnerValueLabel.setText(guiInterface.getLastPlayoffWinnerName());
		championValueLabel.setText(guiInterface.getPlayoffChampion() == null ? "-"
				: guiInterface.getPlayoffChampion().getAbbreviation());
		revalidate();
		repaint();
	}
}
