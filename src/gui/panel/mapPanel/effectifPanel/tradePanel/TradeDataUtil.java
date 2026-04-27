package gui.panel.mapPanel.effectifPanel.tradePanel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import config.CalendarConfiguration;
import config.FinanceConfiguration;
import data.finance.transfer.Trade;
import data.player.Player;
import data.team.Team;
import gui.panel.common.PlayerDisplayUtil;

public final class TradeDataUtil {
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);

	private TradeDataUtil() {
	}

	public static ArrayList<Trade> sortTradesByDate(List<Trade> trades) {
		ArrayList<Trade> sortedTrades = new ArrayList<Trade>(trades);
		Collections.sort(sortedTrades, new Comparator<Trade>() {
			@Override
			public int compare(Trade tradeA, Trade tradeB) {
				if (tradeA == null || tradeA.getDateOfTransfer() == null) {
					return 1;
				}
				if (tradeB == null || tradeB.getDateOfTransfer() == null) {
					return -1;
				}
				return tradeA.getDateOfTransfer().compareTo(tradeB.getDateOfTransfer());
			}
		});
		return sortedTrades;
	}

	public static int getTradeColumnCount(int tradeCount) {
		if (tradeCount >= 8) {
			return 3;
		}
		if (tradeCount >= 5) {
			return 2;
		}
		return 1;
	}

	public static String formatTradeDate(LocalDate tradeDate) {
		if (tradeDate == null
				|| tradeDate.equals(FinanceConfiguration.PRESEASON_TRADE)
				|| tradeDate.isBefore(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE)) {
			return "Pré-saison";
		}
		return DATE_FORMATTER.format(tradeDate);
	}

	public static String buildTeamName(Team team) {
		return team == null ? "-" : team.getName();
	}

	public static String buildPlayerName(Player player) {
		return player == null ? "-" : player.getName();
	}

	public static String buildPlayerDetail(Player player) {
		if (player == null) {
			return "-";
		}
		return "Poste : " + player.getPosition() + "  Salaire : " + PlayerDisplayUtil.formatSalary(player.getSalary());
	}
}
