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
		return DashboardPanelUtil.getCalendarSlotBaseColor(slotKey);
	}

	private Color getCardColor(Game game, String slotKey) {
		if (!game.isDisplayed()) {
			return getSlotColor(slotKey);
		}
		return DashboardPanelUtil.getCalendarSlotDisplayedColor(slotKey);
	}

	private boolean isDarkSlot(String slotKey) {
		return "NIGHT".equals(slotKey);
	}

	private Color getTitleColor(String slotKey) {
		return DashboardPanelUtil.getCalendarSlotTitleColor(slotKey);
	}

	private Color getSubtitleColor(String slotKey) {
		return DashboardPanelUtil.getCalendarSlotSubtitleColor(slotKey);
	}
}
