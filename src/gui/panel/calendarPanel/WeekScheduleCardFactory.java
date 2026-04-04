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
import gui.panel.common.DashboardPanelUtil;
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
	private static final Color DARK_AFTERNOON_COLOR = new Color(0x7A, 0x6A, 0x33);
	private static final Color DARK_EVENING_COLOR = new Color(0x2E, 0x53, 0x61);
	private static final Color DARK_NIGHT_COLOR = new Color(0x33, 0x2F, 0x7A);
	private static final Color DARK_DISPLAYED_AFTERNOON_COLOR = new Color(0x64, 0x57, 0x2A);
	private static final Color DARK_DISPLAYED_EVENING_COLOR = new Color(0x2A, 0x49, 0x55);
	private static final Color DARK_DISPLAYED_NIGHT_COLOR = new Color(0x2D, 0x2A, 0x67);

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
		matchupLabel.setForeground(getTitleColor(slotKey));
		return matchupLabel;
	}

	private JPanel buildCardContent(Game game, String slotKey) {
		JPanel content = new JPanel();
		content.setOpaque(false);
		content.setLayout(new javax.swing.BoxLayout(content, javax.swing.BoxLayout.Y_AXIS));
		if (game.isDisplayed()) {
			JLabel scoreLabel = new JLabel(game.getAwayFinalScore() + " - " + game.getHomeFinalScore());
			scoreLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
			scoreLabel.setForeground(getTitleColor(slotKey));
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
		teamsLabel.setForeground(getSubtitleColor(slotKey));
		return teamsLabel;
	}

	private Color getSlotColor(String slotKey) {
		if (DashboardPanelUtil.isDarkMode()) {
			if ("AFTERNOON".equals(slotKey)) {
				return DARK_AFTERNOON_COLOR;
			}
			if ("EVENING".equals(slotKey)) {
				return DARK_EVENING_COLOR;
			}
			return DARK_NIGHT_COLOR;
		}
		if ("AFTERNOON".equals(slotKey)) {
			return AFTERNOON_COLOR;
		}
		if ("EVENING".equals(slotKey)) {
			return EVENING_COLOR;
		}
		return NIGHT_COLOR;
	}

	private Color getCardColor(Game game, String slotKey) {
		if (DashboardPanelUtil.isDarkMode()) {
			if (!game.isDisplayed()) {
				return getSlotColor(slotKey);
			}
			if ("AFTERNOON".equals(slotKey)) {
				return DARK_DISPLAYED_AFTERNOON_COLOR;
			}
			if ("EVENING".equals(slotKey)) {
				return DARK_DISPLAYED_EVENING_COLOR;
			}
			return DARK_DISPLAYED_NIGHT_COLOR;
		}
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

	private Color getTitleColor(String slotKey) {
		if (DashboardPanelUtil.isDarkMode()) {
			return Color.WHITE;
		}
		if (isDarkSlot(slotKey)) {
			return Color.WHITE;
		}
		return TITLE_COLOR;
	}

	private Color getSubtitleColor(String slotKey) {
		if (DashboardPanelUtil.isDarkMode()) {
			return new Color(230, 234, 240);
		}
		if (isDarkSlot(slotKey)) {
			return Color.WHITE;
		}
		return SUBTITLE_COLOR;
	}
}
