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
import config.FinanceConfiguration;
import data.finance.transfer.Trade;
import data.player.Player;
import data.team.Team;
import gui.panel.common.DashboardCard;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.PlayerDisplayUtil;
import gui.panel.mapPanel.effectifPanel.playerPanel.PlayerPortraitPanel;

public class TradeEntryPanel extends DashboardCard {
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);
	private static final int PORTRAIT_WIDTH = 42;
	private static final int PORTRAIT_HEIGHT = 30;
	private static final int TRADE_LINE_GAP = 8;
	private static final int CARD_HEIGHT = 102;

	private JLabel dateLabel;
	private JLabel partnerLabel;
	private PlayerPortraitPanel departurePortraitPanel;
	private PlayerPortraitPanel arrivalPortraitPanel;
	private JLabel departureLabel;
	private JLabel departureDetailLabel;
	private JLabel arrivalLabel;
	private JLabel arrivalDetailLabel;

	public TradeEntryPanel() {
		create();
		organize();
	}

	private void create() {
		dateLabel = new JLabel("-");
		partnerLabel = new JLabel("-");
		departurePortraitPanel = new PlayerPortraitPanel(null, PORTRAIT_WIDTH, PORTRAIT_HEIGHT);
		arrivalPortraitPanel = new PlayerPortraitPanel(null, PORTRAIT_WIDTH, PORTRAIT_HEIGHT);
		departureLabel = new JLabel("-");
		departureDetailLabel = new JLabel("-");
		arrivalLabel = new JLabel("-");
		arrivalDetailLabel = new JLabel("-");

		dateLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		partnerLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		departureLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		departureDetailLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
		arrivalLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		arrivalDetailLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
		applyTheme();
	}

	private void organize() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(10, CARD_HEIGHT));

		JPanel contentPanel = new JPanel();
		contentPanel.setOpaque(false);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.add(buildTradeHeaderPanel());
		contentPanel.add(Box.createVerticalStrut(5));
		contentPanel.add(buildTradePlayerLine(departurePortraitPanel, departureLabel, departureDetailLabel));
		contentPanel.add(Box.createVerticalStrut(4));
		contentPanel.add(buildTradePlayerLine(arrivalPortraitPanel, arrivalLabel, arrivalDetailLabel));

		add(contentPanel, BorderLayout.CENTER);
	}

	public void updateTrade(Trade trade, Team selectedTeam) {
		if (trade == null || selectedTeam == null) {
			departurePortraitPanel.setPlayer(null);
			arrivalPortraitPanel.setPlayer(null);
			dateLabel.setText("-");
			partnerLabel.setText("-");
			departureLabel.setText("-");
			departureDetailLabel.setText("-");
			arrivalLabel.setText("-");
			arrivalDetailLabel.setText("-");
			return;
		}

		boolean selectedTeamIsTeamA = selectedTeam.equals(trade.getTeamPlayerA());
		Team partnerTeam = selectedTeamIsTeamA ? trade.getTeamPlayerB() : trade.getTeamPlayerA();
		Player departurePlayer = selectedTeamIsTeamA ? trade.getPlayerA() : trade.getPlayerB();
		Player arrivalPlayer = selectedTeamIsTeamA ? trade.getPlayerB() : trade.getPlayerA();

		departurePortraitPanel.setPlayer(departurePlayer);
		arrivalPortraitPanel.setPlayer(arrivalPlayer);
		dateLabel.setText(formatTradeDate(trade.getDateOfTransfer()));
		partnerLabel.setText("Avec " + buildTeamName(partnerTeam));
		departureLabel.setText("Depart : " + buildPlayerName(departurePlayer));
		departureDetailLabel.setText(buildPlayerDetail(departurePlayer));
		arrivalLabel.setText("Arrivee : " + buildPlayerName(arrivalPlayer));
		arrivalDetailLabel.setText(buildPlayerDetail(arrivalPlayer));
	}

	private JPanel buildTradeHeaderPanel() {
		JPanel headerPanel = new JPanel(new BorderLayout(TRADE_LINE_GAP, 0));
		headerPanel.setOpaque(false);

		JPanel textPanel = new JPanel();
		textPanel.setOpaque(false);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		textPanel.add(dateLabel);
		textPanel.add(Box.createVerticalStrut(2));
		textPanel.add(partnerLabel);

		headerPanel.add(buildPortraitSpacer(), BorderLayout.WEST);
		headerPanel.add(textPanel, BorderLayout.CENTER);
		return headerPanel;
	}

	private JPanel buildPortraitSpacer() {
		JPanel spacer = new JPanel();
		spacer.setOpaque(false);
		spacer.setPreferredSize(new Dimension(PORTRAIT_WIDTH, PORTRAIT_HEIGHT));
		return spacer;
	}

	private JPanel buildTradePlayerLine(PlayerPortraitPanel portraitPanel, JLabel titleLabel, JLabel detailLabel) {
		JPanel linePanel = new JPanel(new BorderLayout(TRADE_LINE_GAP, 0));
		linePanel.setOpaque(false);

		JPanel portraitPanelContainer = new JPanel(new BorderLayout());
		portraitPanelContainer.setOpaque(false);
		portraitPanelContainer.add(portraitPanel, BorderLayout.NORTH);

		JPanel textPanel = new JPanel();
		textPanel.setOpaque(false);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		textPanel.add(titleLabel);
		textPanel.add(Box.createVerticalStrut(1));
		textPanel.add(detailLabel);

		linePanel.add(portraitPanelContainer, BorderLayout.WEST);
		linePanel.add(textPanel, BorderLayout.CENTER);
		return linePanel;
	}

	private String formatTradeDate(LocalDate tradeDate) {
		if (tradeDate == null
				|| tradeDate.equals(FinanceConfiguration.PRESEASON_TRADE)
				|| tradeDate.isBefore(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE)) {
			return "Pré-saison";
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

	private String buildPlayerDetail(Player player) {
		if (player == null) {
			return "-";
		}
		return "Poste : "
				+ player.getPosition()
				+ "  Salaire : "
				+ PlayerDisplayUtil.formatSalary(player.getSalary());
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
		if (departureDetailLabel != null) {
			departureDetailLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		}
		if (arrivalLabel != null) {
			arrivalLabel.setForeground(DashboardPanelUtil.POSITIVE_VALUE_COLOR);
		}
		if (arrivalDetailLabel != null) {
			arrivalDetailLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		}
	}
}
