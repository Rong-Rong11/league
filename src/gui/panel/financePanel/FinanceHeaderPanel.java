package gui.panel.financePanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FinanceHeaderPanel extends JPanel {
	private static final Color TITLE_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color SUBTITLE_COLOR = new Color(0x6D, 0x75, 0x83);
	private static final Color ACTIVE_BUTTON_COLOR = new Color(0x2F, 0x80, 0xA9);
	private static final Color INACTIVE_BUTTON_COLOR = new Color(0xEC, 0xF0, 0xF4);
	private static final Color INACTIVE_TEXT_COLOR = new Color(0x4F, 0x5D, 0x75);

	private JButton leagueButton;
	private JButton teamsButton;

	public FinanceHeaderPanel() {
		super(new BorderLayout(16, 0));
		setOpaque(true);
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(225, 225, 225)),
				BorderFactory.createEmptyBorder(12, 16, 12, 16)));

		leagueButton = new JButton("Ligue");
		teamsButton = new JButton("Equipes");

		styleToggleButton(leagueButton);
		styleToggleButton(teamsButton);

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
		applySelectedState(leagueButton, leagueSelected);
		applySelectedState(teamsButton, !leagueSelected);
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

	private void styleToggleButton(JButton button) {
		button.setFocusPainted(false);
		button.setOpaque(true);
		button.setContentAreaFilled(true);
		button.setBorderPainted(false);
		button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		button.setPreferredSize(new Dimension(96, 32));
	}

	private void applySelectedState(JButton button, boolean selected) {
		if (selected) {
			button.setBackground(ACTIVE_BUTTON_COLOR);
			button.setForeground(Color.WHITE);
			return;
		}
		button.setBackground(INACTIVE_BUTTON_COLOR);
		button.setForeground(INACTIVE_TEXT_COLOR);
	}
}
