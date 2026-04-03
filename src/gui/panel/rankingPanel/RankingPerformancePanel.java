package gui.panel.rankingPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.panel.common.DashboardCard;

public class RankingPerformancePanel extends JPanel {
	private static final Color TITLE_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color SUBTITLE_COLOR = new Color(110, 117, 131);
	private static final Color BORDER_COLOR = new Color(229, 232, 238);

	public RankingPerformancePanel() {
		setLayout(new GridLayout(3, 1, 0, 12));
		setOpaque(false);

		add(createPerformanceCard("Leader", "Celtics", "45-12"));
		add(createPerformanceCard("Meilleure serie", "Bucks", "4 victoires"));
		add(createPerformanceCard("Derniere place", "Pistons", "18-39"));
	}

	private JPanel createPerformanceCard(String label, String teamName, String detail) {
		DashboardCard card = new DashboardCard();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
				BorderFactory.createEmptyBorder(12, 14, 12, 14)));

		JLabel labelValue = new JLabel(label);
		labelValue.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		labelValue.setForeground(SUBTITLE_COLOR);

		JLabel teamValue = new JLabel(teamName);
		teamValue.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		teamValue.setForeground(TITLE_COLOR);

		JLabel detailValue = new JLabel(detail);
		detailValue.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		detailValue.setForeground(SUBTITLE_COLOR);

		card.add(labelValue);
		card.add(teamValue);
		card.add(detailValue);
		return card;
	}
}
