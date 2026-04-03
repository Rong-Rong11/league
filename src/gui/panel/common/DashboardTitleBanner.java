package gui.panel.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

public class DashboardTitleBanner extends RoundedPanel {
	private static final Color TITLE_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color SUBTITLE_COLOR = new Color(0x6D, 0x75, 0x83);

	public DashboardTitleBanner(String title, String subtitle) {
		super(24);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
		setPreferredSize(new Dimension(360, 64));

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		titleLabel.setForeground(TITLE_COLOR);
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);
		add(titleLabel);

		if (subtitle != null && !subtitle.isEmpty()) {
			add(Box.createVerticalStrut(3));
			JLabel subtitleLabel = new JLabel(subtitle);
			subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
			subtitleLabel.setForeground(SUBTITLE_COLOR);
			subtitleLabel.setAlignmentX(LEFT_ALIGNMENT);
			add(subtitleLabel);
		}
	}
}
