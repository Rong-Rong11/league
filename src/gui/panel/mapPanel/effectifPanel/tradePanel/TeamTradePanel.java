package gui.panel.mapPanel.effectifPanel.tradePanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import data.finance.transfer.Trade;
import data.team.Team;
import gui.panel.common.PlaceholderPanel;

public class TeamTradePanel extends JPanel {
	private JPanel listPanel;

	public TeamTradePanel() {
		create();
		organize();
	}

	private void create() {
		setOpaque(false);
		setLayout(new BorderLayout());

		listPanel = new JPanel();
		listPanel.setOpaque(false);
		listPanel.setLayout(new GridLayout(1, 1, 8, 8));
	}

	private void organize() {
		add(listPanel, BorderLayout.CENTER);
	}

	public void updateTeam(Team team, ArrayList<Trade> trades) {
		listPanel.removeAll();

		if (team == null) {
			revalidate();
			repaint();
			return;
		}
		if (trades == null || trades.isEmpty()) {
			listPanel.setLayout(new GridLayout(1, 1, 8, 8));
			listPanel.add(new PlaceholderPanel("Aucun transfert n'a ete enregistre pour cette equipe."));
			revalidate();
			repaint();
			return;
		}

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
				return tradeB.getDateOfTransfer().compareTo(tradeA.getDateOfTransfer());
			}
		});
		listPanel.setLayout(buildTradeGridLayout(sortedTrades.size()));

		for (Trade trade : sortedTrades) {
			TradeEntryPanel tradeEntryPanel = new TradeEntryPanel();
			tradeEntryPanel.updateTrade(trade, team);
			listPanel.add(tradeEntryPanel);
		}

		revalidate();
		repaint();
	}

	private GridLayout buildTradeGridLayout(int tradeCount) {
		int columns = 1;
		if (tradeCount >= 6) {
			columns = 3;
		} else if (tradeCount >= 2) {
			columns = 2;
		}
		int rows = (int) Math.ceil(tradeCount / (double) columns);
		return new GridLayout(rows, columns, 8, 8);
	}
}
