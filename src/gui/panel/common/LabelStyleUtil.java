package gui.panel.common;

import java.awt.Font;

import javax.swing.JLabel;

public class LabelStyleUtil {
	public static void styleTitleLabel(JLabel label, int fontSize) {
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontSize));
		label.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
	}

	public static void styleSubtitleLabel(JLabel label, int fontSize) {
		label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, fontSize));
		label.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
	}

	public static void styleValueLabel(JLabel label, int fontSize) {
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontSize));
		label.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
	}
}
