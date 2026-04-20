package gui.panel.financePanel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.panel.common.ButtonStyleUtil;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.LabelStyleUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.RoundedPanel;
import gui.panel.common.ThemeAware;

public class FinanceHeaderPanel extends RoundedPanel implements ThemeAware {
	private JButton leagueButton;
	private JButton teamsButton;
	private JLabel titleLabel;
	private JLabel subtitleLabel;

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
		boolean teamSelected = "team".equals(selectedView);
		ButtonStyleUtil.setToggleButtonSelected(leagueButton, leagueSelected);
		ButtonStyleUtil.setToggleButtonSelected(teamsButton, teamSelected);
	}

	private JPanel buildTitlePanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		titleLabel = new JLabel("FINANCE");
		LabelStyleUtil.styleTitleLabel(titleLabel, 24);
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);

		subtitleLabel = new JLabel("Vue ligue et equipes");
		LabelStyleUtil.styleSubtitleLabel(subtitleLabel, 12);
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

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		LabelStyleUtil.styleTitleLabel(titleLabel, 24);
		LabelStyleUtil.styleSubtitleLabel(subtitleLabel, 12);
	}
}
