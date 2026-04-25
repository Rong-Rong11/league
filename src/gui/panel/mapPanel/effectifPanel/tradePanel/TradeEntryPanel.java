package gui.panel.mapPanel.effectifPanel.tradePanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import config.CalendarConfiguration;
import data.finance.transfer.Trade;
import data.player.Player;
import data.team.Team;
import gui.panel.common.DashboardCard;
import gui.panel.common.DashboardPanelUtil;

public class TradeEntryPanel extends DashboardCard {
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);

	private JLabel dateLabel;
	private JLabel partnerLabel;
	private JLabel departureLabel;
	private JLabel arrivalLabel;

	public TradeEntryPanel() {
		create();
		organize();
	}

	private void create() {
		dateLabel = new JLabel("-");
		partnerLabel = new JLabel("-");
		departureLabel = new JLabel("-");
		arrivalLabel = new JLabel("-");

		dateLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		partnerLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		departureLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		arrivalLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		applyTheme();
	}

	private void organize() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(10, 92));

		JPanel contentPanel = new JPanel();
		contentPanel.setOpaque(false);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.add(dateLabel);
		contentPanel.add(Box.createVerticalStrut(4));
		contentPanel.add(partnerLabel);
		contentPanel.add(Box.createVerticalStrut(8));
		contentPanel.add(departureLabel);
		contentPanel.add(Box.createVerticalStrut(3));
		contentPanel.add(arrivalLabel);

		add(contentPanel, BorderLayout.CENTER);
	}

	public void updateTrade(Trade trade, Team selectedTeam) {
		if (trade == null || selectedTeam == null) {
			dateLabel.setText("-");
			partnerLabel.setText("-");
			departureLabel.setText("-");
			arrivalLabel.setText("-");
			return;
		}

		boolean selectedTeamIsTeamA = selectedTeam.equals(trade.getTeamPlayerA());
		Team partnerTeam = selectedTeamIsTeamA ? trade.getTeamPlayerB() : trade.getTeamPlayerA();
		Player departurePlayer = selectedTeamIsTeamA ? trade.getPlayerA() : trade.getPlayerB();
		Player arrivalPlayer = selectedTeamIsTeamA ? trade.getPlayerB() : trade.getPlayerA();

		dateLabel.setText(formatTradeDate(trade.getDateOfTransfer()));
		partnerLabel.setText("Avec " + buildTeamName(partnerTeam));
		departureLabel.setText("Depart : " + buildPlayerName(departurePlayer));
		arrivalLabel.setText("Arrivee : " + buildPlayerName(arrivalPlayer));
	}

	private String formatTradeDate(LocalDate tradeDate) {
		if (tradeDate == null || tradeDate.isBefore(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE)) {
			return "Pre-saison";
		}
		return DATE_FORMATTER.format(tradeDate);
	}

	private String buildTeamName(Team team) {
		if (team == null) {
			return "-";
		}
		return team.getName();
	}

	private String buildPlayerName(Player player) {
		if (player == null) {
			return "-";
		}
		return player.getName();
	}

	@Override
	public void applyTheme() {
		super.applyTheme();
		if (dateLabel != null) {
			dateLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		}
		if (partnerLabel != null) {
			partnerLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		}
		if (departureLabel != null) {
			departureLabel.setForeground(DashboardPanelUtil.EXPENSE_COLOR);
		}
		if (arrivalLabel != null) {
			arrivalLabel.setForeground(DashboardPanelUtil.POSITIVE_VALUE_COLOR);
		}
	}
}
