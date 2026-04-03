package gui.panel.financePanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.panel.common.ButtonStyleUtil;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.RoundedPanel;

public class FinanceHeaderPanel extends RoundedPanel {
	private static final Color TITLE_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color SUBTITLE_COLOR = new Color(0x6D, 0x75, 0x83);

	private JButton leagueButton;
	private JButton teamsButton;

	public FinanceHeaderPanel() {
		super(new BorderLayout(16, 0));
		setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

		leagueButton = new RoundedButton("Ligue");
		teamsButton = new RoundedButton("Equipes");

		ButtonStyleUtil.styleToggleButton(leagueButton);
		ButtonStyleUtil.styleToggleButton(teamsButton);

		add(buildTitlePanel(), BorderLayout.WEST);
		add(buildActionsPanel(), BorderLayout.EAST);
	}

	public JButton getLeagueButton() {
		return leagueButton;
	}

	public JButton getTeamsButton() {
		return teamsButton;
	}

	public void setSelectedView(String selectedView) {
		boolean leagueSelected = "league".equals(selectedView);
		ButtonStyleUtil.setToggleButtonSelected(leagueButton, leagueSelected);
		ButtonStyleUtil.setToggleButtonSelected(teamsButton, !leagueSelected);
	}

	private JPanel buildTitlePanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JLabel titleLabel = new JLabel("FINANCE");
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		titleLabel.setForeground(TITLE_COLOR);
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);

		JLabel subtitleLabel = new JLabel("Vue ligue et equipes");
		subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		subtitleLabel.setForeground(SUBTITLE_COLOR);
		subtitleLabel.setAlignmentX(LEFT_ALIGNMENT);

		panel.add(titleLabel);
		panel.add(Box.createVerticalStrut(4));
		panel.add(subtitleLabel);
		return panel;
	}

	private JPanel buildActionsPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		panel.setOpaque(false);
		panel.add(leagueButton);
		panel.add(teamsButton);
		return panel;
	}
}
