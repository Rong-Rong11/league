package gui.panel.calendarPanel;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.calendar.GameDay;
import gui.panel.common.RoundedButton;
import gui.panel.common.RoundedPanel;
import data.sport.setup.Game;
import process.utility.TeamDisplayUtil;

public class WeekDayRowPanel extends RoundedPanel {
	private static final long serialVersionUID = 1L;
	private static final Color PANEL_BACKGROUND = Color.WHITE;
	private static final Color TITLE_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color SUBTITLE_COLOR = new Color(0x6D, 0x75, 0x83);
	private static final Color UPCOMING_COLOR = new Color(0xD2, 0x30, 0x30);
	private static final Color SIMULATED_COLOR = new Color(0x2F, 0x80, 0xA9);
	private static final Color MATCH_CHIP_COLOR = new Color(0xF3, 0xF5, 0xF8);
	private static final Font TITLE_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 18);
	private static final Font TEXT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
	private static final Font MATCH_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);
	private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("EEEE dd/MM");

	public WeekDayRowPanel(LocalDate day, GameDay gameDay, boolean displayed,
			ActionListener simulateAction, ActionListener detailAction) {
		super(24);
		create(day, gameDay, displayed, simulateAction, detailAction);
	}

	private void create(LocalDate day, GameDay gameDay, boolean displayed,
			ActionListener simulateAction, ActionListener detailAction) {
		setLayout(new BorderLayout(0, 14));
		setBackground(PANEL_BACKGROUND);
		setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

		JPanel infoPanel = buildInfoPanel(day, gameDay, displayed);
		JPanel matchesPanel = buildMatchesPanel(gameDay.getGames());
		JPanel actionsPanel = buildActionsPanel(simulateAction, detailAction);

		JPanel centerPanel = new JPanel(new BorderLayout(0, 12));
		centerPanel.setOpaque(false);
		centerPanel.add(infoPanel, BorderLayout.NORTH);
		centerPanel.add(matchesPanel, BorderLayout.CENTER);

		add(centerPanel, BorderLayout.CENTER);
		add(actionsPanel, BorderLayout.SOUTH);
	}

	private JPanel buildInfoPanel(LocalDate day, GameDay gameDay, boolean displayed) {
		JLabel dayTitle = new JLabel(DAY_FORMATTER.format(day));
		dayTitle.setFont(TITLE_FONT);
		dayTitle.setForeground(TITLE_COLOR);

		String detailText = gameDay.getGames().size() == 1 ? "1 match" : gameDay.getGames().size() + " matchs";
		JLabel dayDetail = new JLabel(detailText);
		dayDetail.setFont(TEXT_FONT);
		dayDetail.setForeground(SUBTITLE_COLOR);

		JLabel stateLabel = new JLabel(displayed ? "Simulé" : "À simuler");
		stateLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		stateLabel.setForeground(displayed ? SIMULATED_COLOR : UPCOMING_COLOR);

		JPanel titlePanel = new JPanel();
		titlePanel.setOpaque(false);
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
		titlePanel.add(dayTitle);
		titlePanel.add(Box.createVerticalStrut(4));
		titlePanel.add(dayDetail);

		JPanel infoPanel = new JPanel(new BorderLayout(16, 0));
		infoPanel.setOpaque(false);
		infoPanel.add(titlePanel, BorderLayout.WEST);
		infoPanel.add(stateLabel, BorderLayout.EAST);
		return infoPanel;
	}

	private JPanel buildMatchesPanel(ArrayList<Game> games) {
		JPanel matchesPanel = new JPanel(new GridLayout(0, 1, 0, 8));
		matchesPanel.setOpaque(false);

		int matchCount = Math.min(games.size(), 8);
		for (int i = 0; i < matchCount; i++) {
			matchesPanel.add(buildMatchChip(games.get(i)));
		}

		if (games.size() > matchCount) {
			JLabel moreLabel = new JLabel("+" + (games.size() - matchCount) + " autres matchs");
			moreLabel.setFont(TEXT_FONT);
			moreLabel.setForeground(SUBTITLE_COLOR);
			matchesPanel.add(moreLabel);
		}
		return matchesPanel;
	}

	private JPanel buildMatchChip(Game game) {
		RoundedPanel chip = new RoundedPanel(18);
		chip.setLayout(new BorderLayout());
		chip.setBackground(MATCH_CHIP_COLOR);
		chip.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

		String homeTeam = TeamDisplayUtil.getAbbreviation(game.getGameContext().getHomeTeam());
		String awayTeam = TeamDisplayUtil.getAbbreviation(game.getGameContext().getAwayTeam());
		String text = awayTeam + " @ " + homeTeam;
		if (game.isDisplayed()) {
			text += "  " + game.getAwayFinalScore() + "-" + game.getHomeFinalScore();
		}

		JLabel matchLabel = new JLabel(text);
		matchLabel.setFont(MATCH_FONT);
		matchLabel.setForeground(TITLE_COLOR);
		chip.add(matchLabel, BorderLayout.CENTER);
		return chip;
	}

	private JPanel buildActionsPanel(ActionListener simulateAction, ActionListener detailAction) {
		JButton simulateButton = new RoundedButton("Simuler");
		simulateButton.setFont(TEXT_FONT);
		simulateButton.setPreferredSize(new Dimension(96, 36));
		simulateButton.addActionListener(simulateAction);

		JButton detailButton = new RoundedButton("Détail");
		detailButton.setFont(TEXT_FONT);
		detailButton.setPreferredSize(new Dimension(96, 36));
		detailButton.addActionListener(detailAction);

		JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		actionsPanel.setOpaque(false);
		actionsPanel.add(simulateButton);
		actionsPanel.add(detailButton);
		return actionsPanel;
	}
}
