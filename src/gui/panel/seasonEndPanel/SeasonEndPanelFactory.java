package gui.panel.seasonEndPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.team.Team;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.RoundedPanel;
import gui.utility.TeamDisplayUtility;

public final class SeasonEndPanelFactory {
	public static final int GAP = 12;
	public static final int LIST_LIMIT = 5;
	public static final int PROFILE_LIST_LIMIT = 3;

	private SeasonEndPanelFactory() {
	}

	public static RoundedPanel buildStatCard(String title, String value, String subtitle, Color valueColor) {
		RoundedPanel card = new RoundedPanel(new BorderLayout(), 16);
		card.setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(DashboardPanelUtil.BORDER_COLOR, 1),
				BorderFactory.createEmptyBorder(14, 14, 14, 14)));

		JPanel textPanel = new JPanel();
		textPanel.setOpaque(false);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		titleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		JLabel valueLabel = new JLabel(value);
		valueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		valueLabel.setForeground(valueColor);
		JLabel subtitleLabel = new JLabel(subtitle);
		subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		subtitleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		textPanel.add(titleLabel);
		textPanel.add(javax.swing.Box.createVerticalStrut(9));
		textPanel.add(valueLabel);
		textPanel.add(javax.swing.Box.createVerticalStrut(4));
		textPanel.add(subtitleLabel);
		card.add(textPanel, BorderLayout.CENTER);
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
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		titleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		JLabel valueLabel = new JLabel(value);
		valueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		valueLabel.setForeground(valueColor);
		row.add(titleLabel, BorderLayout.CENTER);
		row.add(valueLabel, BorderLayout.EAST);
		return row;
	}

	public static JLabel buildSectionLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		label.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		return label;
	}

	public static JPanel buildProfileRow(Team team, SeasonEndDataProvider dataProvider, Color accentColor) {
		JPanel row = new JPanel();
		row.setOpaque(false);
		row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));

		JLabel titleLabel = new JLabel(TeamDisplayUtility.getShortName(team) + " | "
				+ dataProvider.formatMoney(dataProvider.getTotalTeamNet(team)));
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
		titleLabel.setForeground(accentColor);

		JLabel profileLabel = new JLabel(dataProvider.getMarketLabel(team) + " | "
				+ dataProvider.getPolicyLabel(team) + " | " + dataProvider.getStrategyLabel(team));
		profileLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		profileLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);

		JLabel moneyLabel = new JLabel("Budget " + dataProvider.formatMoney(dataProvider.getRemainingBudget(team))
				+ " | Payroll " + dataProvider.formatMoney(dataProvider.getCurrentPayroll(team)));
		moneyLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		moneyLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);

		row.add(titleLabel);
		row.add(profileLabel);
		row.add(moneyLabel);
		return row;
	}
}
