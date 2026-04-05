package gui.panel.common;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SectionTitle extends JPanel implements ThemeAware {
	private JLabel titleLabel;
	private JLabel subtitleLabel;

	public SectionTitle(String title, String subtitle) {
		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(14, 16, 10, 16));

		titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);
		add(titleLabel);

		if (subtitle != null && !subtitle.isEmpty()) {
			add(Box.createVerticalStrut(3));
			subtitleLabel = new JLabel(subtitle);
			subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
			subtitleLabel.setAlignmentX(LEFT_ALIGNMENT);
			add(subtitleLabel);
		}
		applyTheme();
	}

	@Override
	public void applyTheme() {
		titleLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		if (subtitleLabel != null) {
			subtitleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		}
	}
}
