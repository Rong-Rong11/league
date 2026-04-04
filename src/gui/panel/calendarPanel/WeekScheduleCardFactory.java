package gui.panel.calendarPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.calendar.GameDay;
import data.sport.setup.Game;
import gui.panel.common.RoundedPanel;
import process.utility.TeamDisplayUtil;

public class WeekScheduleCardFactory {
	private static final Color TITLE_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color SUBTITLE_COLOR = new Color(0x6D, 0x75, 0x83);
	private static final Color AFTERNOON_COLOR = new Color(0xF8, 0xE9, 0x9A);
	private static final Color EVENING_COLOR = new Color(0xC8, 0xEE, 0xF6);
	private static final Color NIGHT_COLOR = new Color(0x4D, 0x46, 0xF0);
	private static final Color DISPLAYED_AFTERNOON_COLOR = new Color(0xF2, 0xE4, 0xB8);
	private static final Color DISPLAYED_EVENING_COLOR = new Color(0xD9, 0xEC, 0xF0);
	private static final Color DISPLAYED_NIGHT_COLOR = new Color(0x8C, 0x88, 0xE8);

	public ArrayList<Game> getGamesForSlot(GameDay gameDay, String slotKey) {
		ArrayList<Game> slotGames = new ArrayList<Game>();
		for (Game game : gameDay.getGames()) {
			if (matchesSlot(game, slotKey)) {
				slotGames.add(game);
			}
		}
		return slotGames;
	}

	public JPanel buildMatchCard(Game game, String slotKey) {
		RoundedPanel card = new RoundedPanel(14);
		card.setLayout(new BorderLayout(0, 4));
		card.setBackground(getCardColor(game, slotKey));
		card.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
		card.add(buildMatchupLabel(game, slotKey), BorderLayout.NORTH);
		card.add(buildCardContent(game, slotKey), BorderLayout.CENTER);
		return card;
	}

	private boolean matchesSlot(Game game, String slotKey) {
		return slotKey.equals(game.getGameContext().getGameMoment().getSlotKey());
	}

	private JLabel buildMatchupLabel(Game game, String slotKey) {
		String awayTeam = TeamDisplayUtil.getAbbreviation(game.getGameContext().getAwayTeam());
		String homeTeam = TeamDisplayUtil.getAbbreviation(game.getGameContext().getHomeTeam());
		JLabel matchupLabel = new JLabel(awayTeam + " vs " + homeTeam);
		matchupLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
		matchupLabel.setForeground(isDarkSlot(slotKey) && !game.isDisplayed() ? Color.WHITE : TITLE_COLOR);
		return matchupLabel;
	}

	private JPanel buildCardContent(Game game, String slotKey) {
		JPanel content = new JPanel();
		content.setOpaque(false);
		content.setLayout(new javax.swing.BoxLayout(content, javax.swing.BoxLayout.Y_AXIS));
		if (game.isDisplayed()) {
			JLabel scoreLabel = new JLabel(game.getAwayFinalScore() + " - " + game.getHomeFinalScore());
			scoreLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
			scoreLabel.setForeground(TITLE_COLOR);
			content.add(scoreLabel);
		}
		content.add(buildTeamsLabel(game, slotKey));
		return content;
	}

	private JLabel buildTeamsLabel(Game game, String slotKey) {
		String detailText = TeamDisplayUtil.getShortName(game.getGameContext().getAwayTeam()) + " vs "
				+ TeamDisplayUtil.getShortName(game.getGameContext().getHomeTeam());
		JLabel teamsLabel = new JLabel(detailText);
		teamsLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		teamsLabel.setForeground(isDarkSlot(slotKey) && !game.isDisplayed() ? Color.WHITE : SUBTITLE_COLOR);
		return teamsLabel;
	}

	private Color getSlotColor(String slotKey) {
		if ("AFTERNOON".equals(slotKey)) {
			return AFTERNOON_COLOR;
		}
		if ("EVENING".equals(slotKey)) {
			return EVENING_COLOR;
		}
		return NIGHT_COLOR;
	}

	private Color getCardColor(Game game, String slotKey) {
		if (!game.isDisplayed()) {
			return getSlotColor(slotKey);
		}
		if ("AFTERNOON".equals(slotKey)) {
			return DISPLAYED_AFTERNOON_COLOR;
		}
		if ("EVENING".equals(slotKey)) {
			return DISPLAYED_EVENING_COLOR;
		}
		return DISPLAYED_NIGHT_COLOR;
	}

	private boolean isDarkSlot(String slotKey) {
		return "NIGHT".equals(slotKey);
	}
}
