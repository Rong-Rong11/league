package gui.panel.calendarPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.calendar.GameDay;
import data.sport.setup.Game;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.RoundedPanel;
import gui.utility.TeamDisplayUtility;
import process.orchestrator.interfaces.GUIInterface;
import process.visitor.gamemoment.GameMomentSlotKeyVisitor;

public class WeekScheduleCardFactory {
	private GUIInterface guiInterface;

	public WeekScheduleCardFactory(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
	}

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
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(getBorderColor(game, slotKey), 1),
				BorderFactory.createEmptyBorder(6, 8, 6, 8)));
		card.setPreferredSize(new Dimension(120, 54));
		card.setMinimumSize(new Dimension(110, 54));
		card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
		card.add(buildMatchupLabel(game, slotKey), BorderLayout.NORTH);
		card.add(buildCardContent(game, slotKey), BorderLayout.CENTER);
		return card;
	}

	private boolean matchesSlot(Game game, String slotKey) {
		String gameSlot = game.getGameContext().getGameMoment().accept(new GameMomentSlotKeyVisitor());
		if (gameSlot == null) {
			return false;
		}
		return slotKey.equalsIgnoreCase(gameSlot);
	}

	private JLabel buildMatchupLabel(Game game, String slotKey) {
		String awayTeam = TeamDisplayUtility.getAbbreviation(game.getGameContext().getAwayTeam());
		String homeTeam = TeamDisplayUtility.getAbbreviation(game.getGameContext().getHomeTeam());
		JLabel matchupLabel = new JLabel(buildMatchText(game, awayTeam, homeTeam));
		matchupLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
		matchupLabel.setForeground(getTitleColor(slotKey));
		return matchupLabel;
	}

	private String buildMatchText(Game game, String awayTeam, String homeTeam) {
		if (game.getPlayoffRound() == null || guiInterface == null) {
			return awayTeam + " vs " + homeTeam;
		}
		String bestOfLabel = guiInterface.getPlayoffGameLabel(game);
		if (bestOfLabel == null || bestOfLabel.equals("")) {
			return awayTeam + "-" + homeTeam;
		}
		return bestOfLabel + " " + awayTeam + "-" + homeTeam;
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
		} else {
			JLabel pendingLabel = new JLabel("A jouer");
			pendingLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
			pendingLabel.setForeground(getSubtitleColor(slotKey));
			content.add(pendingLabel);
		}
		content.add(buildTeamsLabel(game, slotKey));
		return content;
	}

	private JLabel buildTeamsLabel(Game game, String slotKey) {
		String detailText = TeamDisplayUtility.getShortName(game.getGameContext().getAwayTeam()) + " vs "
				+ TeamDisplayUtility.getShortName(game.getGameContext().getHomeTeam());
		JLabel teamsLabel = new JLabel(detailText);
		teamsLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		teamsLabel.setForeground(getSubtitleColor(slotKey));
		return teamsLabel;
	}

	private Color getSlotColor(String slotKey) {
		return DashboardPanelUtil.getCalendarSlotBaseColor(slotKey);
	}

	private Color getCardColor(Game game, String slotKey) {
		if (game.getPlayoffRound() != null) {
			if (game.isDisplayed()) {
				return DashboardPanelUtil.getCalendarPlayoffSlotDisplayedColor();
			}
			return DashboardPanelUtil.getCalendarPlayoffSlotBaseColor();
		}
		if (!game.isDisplayed()) {
			return getSlotColor(slotKey);
		}
		return DashboardPanelUtil.getCalendarSlotDisplayedColor(slotKey);
	}

	private Color getBorderColor(Game game, String slotKey) {
		if (game.getPlayoffRound() != null) {
			return DashboardPanelUtil.EXPENSE_COLOR;
		}
		if ("AFTERNOON".equals(slotKey)) {
			return DashboardPanelUtil.NEUTRAL_ACCENT_COLOR;
		}
		if ("EVENING".equals(slotKey)) {
			return DashboardPanelUtil.REVENUE_COLOR;
		}
		return DashboardPanelUtil.POLICY_BALANCED_COLOR;
	}

	private Color getTitleColor(String slotKey) {
		return DashboardPanelUtil.getCalendarSlotTitleColor(slotKey);
	}

	private Color getSubtitleColor(String slotKey) {
		return DashboardPanelUtil.getCalendarSlotSubtitleColor(slotKey);
	}
}
