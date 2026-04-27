package gui.panel.mapPanel.effectifPanel.tradePanel;

import data.finance.transfer.Trade;
import data.player.Player;
import data.team.Team;

public final class TradeEntryData {
	private final Player departurePlayer;
	private final Player arrivalPlayer;
	private final String dateText;
	private final String partnerText;
	private final String departureText;
	private final String departureDetailText;
	private final String arrivalText;
	private final String arrivalDetailText;

	private TradeEntryData(Player departurePlayer, Player arrivalPlayer, String dateText, String partnerText,
			String departureText, String departureDetailText, String arrivalText, String arrivalDetailText) {
		this.departurePlayer = departurePlayer;
		this.arrivalPlayer = arrivalPlayer;
		this.dateText = dateText;
		this.partnerText = partnerText;
		this.departureText = departureText;
		this.departureDetailText = departureDetailText;
		this.arrivalText = arrivalText;
		this.arrivalDetailText = arrivalDetailText;
	}

	public static TradeEntryData empty() {
		return new TradeEntryData(null, null, "-", "-", "-", "-", "-", "-");
	}

	public static TradeEntryData fromTrade(Trade trade, Team selectedTeam) {
		if (trade == null || selectedTeam == null) {
			return empty();
		}

		boolean selectedTeamIsTeamA = selectedTeam.equals(trade.getTeamPlayerA());
		Team partnerTeam = selectedTeamIsTeamA ? trade.getTeamPlayerB() : trade.getTeamPlayerA();
		Player departurePlayer = selectedTeamIsTeamA ? trade.getPlayerA() : trade.getPlayerB();
		Player arrivalPlayer = selectedTeamIsTeamA ? trade.getPlayerB() : trade.getPlayerA();

		return new TradeEntryData(
				departurePlayer,
				arrivalPlayer,
				TradeDataUtil.formatTradeDate(trade.getDateOfTransfer()),
				"Avec " + TradeDataUtil.buildTeamName(partnerTeam),
				"Depart : " + TradeDataUtil.buildPlayerName(departurePlayer),
				TradeDataUtil.buildPlayerDetail(departurePlayer),
				"Arrivee : " + TradeDataUtil.buildPlayerName(arrivalPlayer),
				TradeDataUtil.buildPlayerDetail(arrivalPlayer));
	}

	public Player getDeparturePlayer() {
		return departurePlayer;
	}

	public Player getArrivalPlayer() {
		return arrivalPlayer;
	}

	public String getDateText() {
		return dateText;
	}

	public String getPartnerText() {
		return partnerText;
	}

	public String getDepartureText() {
		return departureText;
	}

	public String getDepartureDetailText() {
		return departureDetailText;
	}

	public String getArrivalText() {
		return arrivalText;
	}

	public String getArrivalDetailText() {
		return arrivalDetailText;
	}
}
