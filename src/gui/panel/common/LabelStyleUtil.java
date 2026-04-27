package gui.panel.common;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class LabelStyleUtil {
	public static void styleTitleLabel(JLabel label, int fontSize) {
		styleLabel(label, Font.BOLD, fontSize, DashboardPanelUtil.TITLE_TEXT_COLOR);
	}

	public static void styleSubtitleLabel(JLabel label, int fontSize) {
		styleLabel(label, Font.PLAIN, fontSize, DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
	}

	public static void styleValueLabel(JLabel label, int fontSize) {
		styleValueLabel(label, fontSize, DashboardPanelUtil.TITLE_TEXT_COLOR);
	}

	public static void styleValueLabel(JLabel label, int fontSize, Color color) {
		styleLabel(label, Font.BOLD, fontSize, color);
	}

	public static void styleMutedLabel(JLabel label, int fontSize) {
		styleLabel(label, Font.PLAIN, fontSize, DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
	}

	public static void styleAccentLabel(JLabel label, int fontSize, Color color) {
		styleLabel(label, Font.BOLD, fontSize, color);
	}

	private static void styleLabel(JLabel label, int fontStyle, int fontSize, Color color) {
		label.setFont(new Font(Font.SANS_SERIF, fontStyle, fontSize));
		label.setForeground(color);
	}
}
