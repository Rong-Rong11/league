package gui.panel.mapPanel.effectifPanel.tradePanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.finance.transfer.Trade;
import data.team.Team;
import gui.panel.common.DashboardCard;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.mapPanel.effectifPanel.playerPanel.PlayerPortraitPanel;

public class TradeEntryPanel extends DashboardCard {
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
		contentPanel.add(TradeEntryPanelFactory.buildHeaderPanel(
				dateLabel, partnerLabel, PORTRAIT_WIDTH, PORTRAIT_HEIGHT, TRADE_LINE_GAP));
		contentPanel.add(Box.createVerticalStrut(5));
		contentPanel.add(TradeEntryPanelFactory.buildTradePlayerLine(
				departurePortraitPanel, departureLabel, departureDetailLabel, TRADE_LINE_GAP));
		contentPanel.add(Box.createVerticalStrut(4));
		contentPanel.add(TradeEntryPanelFactory.buildTradePlayerLine(
				arrivalPortraitPanel, arrivalLabel, arrivalDetailLabel, TRADE_LINE_GAP));

		add(contentPanel, BorderLayout.CENTER);
	}

	public void updateTrade(Trade trade, Team selectedTeam) {
		TradeEntryData tradeEntryData = TradeEntryData.fromTrade(trade, selectedTeam);
		departurePortraitPanel.setPlayer(tradeEntryData.getDeparturePlayer());
		arrivalPortraitPanel.setPlayer(tradeEntryData.getArrivalPlayer());
		dateLabel.setText(tradeEntryData.getDateText());
		partnerLabel.setText(tradeEntryData.getPartnerText());
		departureLabel.setText(tradeEntryData.getDepartureText());
		departureDetailLabel.setText(tradeEntryData.getDepartureDetailText());
		arrivalLabel.setText(tradeEntryData.getArrivalText());
		arrivalDetailLabel.setText(tradeEntryData.getArrivalDetailText());
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
