package gui.panel.seasonEndPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.team.Team;
import gui.panel.common.DashboardCard;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.LabelStyleUtil;
import gui.panel.common.RoundedPanel;
import gui.utility.TeamDisplayUtility;

public final class SeasonEndPanelFactory {
	public static final int GAP = 12;
	public static final int LIST_LIMIT = 5;
	public static final int PROFILE_LIST_LIMIT = 5;

	private SeasonEndPanelFactory() {
	}

	public static RoundedPanel buildStatCard(String title, String value, String subtitle, Color valueColor) {
		RoundedPanel card = new RoundedPanel(new BorderLayout(), 16);
		DashboardPanelUtil.applySurfaceCard(card, 14);

		JPanel textPanel = new JPanel();
		textPanel.setOpaque(false);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		JLabel titleLabel = new JLabel(title);
		LabelStyleUtil.styleSubtitleLabel(titleLabel, 14);
		JLabel valueLabel = new JLabel(value);
		LabelStyleUtil.styleValueLabel(valueLabel, 18, valueColor);
		JLabel subtitleLabel = new JLabel(subtitle);
		LabelStyleUtil.styleSubtitleLabel(subtitleLabel, 12);
		textPanel.add(titleLabel);
		textPanel.add(javax.swing.Box.createVerticalStrut(9));
		textPanel.add(valueLabel);
		textPanel.add(javax.swing.Box.createVerticalStrut(4));
		textPanel.add(subtitleLabel);
		card.add(textPanel, BorderLayout.CENTER);
		return card;
	}

	public static DashboardCard buildInfoBox(String title, String subtitle, JPanel content, final String infoTitle,
			final String infoMessage) {
		DashboardCard card = new DashboardCard();
		card.setLayout(new BorderLayout());

		JPanel header = new JPanel(new BorderLayout(10, 0));
		header.setOpaque(false);
		header.setBorder(BorderFactory.createEmptyBorder(14, 16, 10, 16));

		JPanel titlePanel = new JPanel();
		titlePanel.setOpaque(false);
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

		JLabel titleLabel = new JLabel(title);
		LabelStyleUtil.styleTitleLabel(titleLabel, 16);
		JLabel subtitleLabel = new JLabel(subtitle);
		LabelStyleUtil.styleSubtitleLabel(subtitleLabel, 13);

		titlePanel.add(titleLabel);
		titlePanel.add(subtitleLabel);

		header.add(titlePanel, BorderLayout.CENTER);
		card.add(header, BorderLayout.NORTH);
		card.add(content, BorderLayout.CENTER);
		return card;
	}

	public static JPanel buildListPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
		return panel;
	}

	public static JPanel buildCompactListPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
		return panel;
	}

	public static JPanel buildInfoRow(String title, String value, Color valueColor) {
		JPanel row = new JPanel(new BorderLayout(10, 0));
		row.setOpaque(false);
		JLabel titleLabel = new JLabel(title);
		LabelStyleUtil.styleSubtitleLabel(titleLabel, 12);
		JLabel valueLabel = new JLabel(value);
		LabelStyleUtil.styleValueLabel(valueLabel, 12, valueColor);
		row.add(titleLabel, BorderLayout.CENTER);
		row.add(valueLabel, BorderLayout.EAST);
		row.setMaximumSize(new Dimension(Integer.MAX_VALUE, row.getPreferredSize().height));
		return row;
	}

	public static JLabel buildSectionLabel(String text) {
		JLabel label = new JLabel(text);
		LabelStyleUtil.styleTitleLabel(label, 12);
		return label;
	}

	public static JPanel buildProfileRow(Team team, SeasonEndDataProvider dataProvider, Color accentColor) {
		JPanel row = new JPanel();
		row.setOpaque(false);
		row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));

		JLabel titleLabel = new JLabel(TeamDisplayUtility.getShortName(team) + " | "
				+ dataProvider.formatMoney(dataProvider.getTotalTeamNet(team)));
		LabelStyleUtil.styleAccentLabel(titleLabel, 11, accentColor);

		JLabel profileLabel = new JLabel(dataProvider.getMarketLabel(team) + " | "
				+ dataProvider.getPolicyLabel(team) + " | " + dataProvider.getStrategyLabel(team));
		LabelStyleUtil.styleSubtitleLabel(profileLabel, 10);

		JLabel moneyLabel = new JLabel("Budget " + dataProvider.formatMoney(dataProvider.getRemainingBudget(team))
				+ " | Payroll " + dataProvider.formatMoney(dataProvider.getCurrentPayroll(team)));
		LabelStyleUtil.styleSubtitleLabel(moneyLabel, 10);

		row.add(titleLabel);
		row.add(profileLabel);
		row.add(moneyLabel);
		row.setMaximumSize(new Dimension(Integer.MAX_VALUE, row.getPreferredSize().height));
		return row;
	}
}
