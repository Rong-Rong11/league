package gui.panel.common;

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
		LabelStyleUtil.styleTitleLabel(titleLabel, 15);
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);
		add(titleLabel);

		if (subtitle != null && !subtitle.isEmpty()) {
			add(Box.createVerticalStrut(3));
			subtitleLabel = new JLabel(subtitle);
			LabelStyleUtil.styleSubtitleLabel(subtitleLabel, 13);
			subtitleLabel.setAlignmentX(LEFT_ALIGNMENT);
			add(subtitleLabel);
		}
		applyTheme();
	}

	@Override
	public void applyTheme() {
		LabelStyleUtil.styleTitleLabel(titleLabel, 15);
		if (subtitleLabel != null) {
			LabelStyleUtil.styleSubtitleLabel(subtitleLabel, 13);
		}
	}
}
