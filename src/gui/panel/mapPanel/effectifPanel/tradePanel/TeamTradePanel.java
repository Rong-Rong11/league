package gui.panel.mapPanel.effectifPanel.tradePanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
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
		listPanel.setLayout(new BorderLayout());
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
			listPanel.add(new PlaceholderPanel("Aucun transfert n'a ete enregistre pour cette equipe."),
					BorderLayout.NORTH);
			revalidate();
			repaint();
			return;
		}

		ArrayList<Trade> sortedTrades = TradeDataUtil.sortTradesByDate(trades);
		int columns = TradeDataUtil.getTradeColumnCount(sortedTrades.size());
		JPanel columnsPanel = new JPanel(new GridLayout(1, columns, 8, 0));
		columnsPanel.setOpaque(false);
		ArrayList<JPanel> columnPanels = buildColumnPanels(columnsPanel, columns);

		for (int i = 0; i < sortedTrades.size(); i++) {
			Trade trade = sortedTrades.get(i);
			TradeEntryPanel tradeEntryPanel = new TradeEntryPanel();
			tradeEntryPanel.updateTrade(trade, team);
			JPanel columnPanel = columnPanels.get(i % columns);
			columnPanel.add(tradeEntryPanel);
			columnPanel.add(Box.createVerticalStrut(8));
		}
		listPanel.add(columnsPanel, BorderLayout.NORTH);

		revalidate();
		repaint();
	}

	private ArrayList<JPanel> buildColumnPanels(JPanel columnsPanel, int columns) {
		ArrayList<JPanel> columnPanels = new ArrayList<JPanel>();
		for (int i = 0; i < columns; i++) {
			JPanel columnPanel = new JPanel();
			columnPanel.setOpaque(false);
			columnPanel.setLayout(new BoxLayout(columnPanel, BoxLayout.Y_AXIS));
			columnPanels.add(columnPanel);
			columnsPanel.add(columnPanel);
		}
		return columnPanels;
	}
}
