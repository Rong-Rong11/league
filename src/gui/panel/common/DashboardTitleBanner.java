package gui.panel.common;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

public class DashboardTitleBanner extends RoundedPanel implements ThemeAware {
	private JLabel titleLabel;
	private JLabel subtitleLabel;

	public DashboardTitleBanner(String title, String subtitle) {
		super(24);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
		setPreferredSize(new Dimension(360, 64));

		titleLabel = new JLabel(title);
		LabelStyleUtil.styleTitleLabel(titleLabel, 16);
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);
		add(titleLabel);

		if (subtitle != null && !subtitle.isEmpty()) {
			add(Box.createVerticalStrut(3));
			subtitleLabel = new JLabel(subtitle);
			LabelStyleUtil.styleSubtitleLabel(subtitleLabel, 12);
			subtitleLabel.setAlignmentX(LEFT_ALIGNMENT);
			add(subtitleLabel);
		}
		applyTheme();
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		LabelStyleUtil.styleTitleLabel(titleLabel, 16);
		if (subtitleLabel != null) {
			LabelStyleUtil.styleSubtitleLabel(subtitleLabel, 12);
		}
	}
}
