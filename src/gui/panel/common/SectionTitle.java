package gui.panel.common;

import java.awt.Color;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SectionTitle extends JPanel {

	private static final Color TITLE_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color SUBTITLE_COLOR = new Color(0x6D, 0x75, 0x83);

	public SectionTitle(String title, String subtitle) {
		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		titleLabel.setForeground(TITLE_COLOR);
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);
		add(titleLabel);

		if (subtitle != null && !subtitle.isEmpty()) {
			add(Box.createVerticalStrut(3));
			JLabel subtitleLabel = new JLabel(subtitle);
			subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
			subtitleLabel.setForeground(SUBTITLE_COLOR);
			subtitleLabel.setAlignmentX(LEFT_ALIGNMENT);
			add(subtitleLabel);
		}
	}
}
