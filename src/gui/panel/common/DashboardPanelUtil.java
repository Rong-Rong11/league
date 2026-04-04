package gui.panel.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class DashboardPanelUtil {
	public static Color DASHBOARD_BACKGROUND_COLOR = new Color(247, 248, 250);
	public static Color PANEL_SURFACE_COLOR = Color.WHITE;
	public static Color BUTTON_SURFACE_COLOR = new Color(220, 226, 234);
	public static Color BUTTON_TEXT_COLOR = new Color(40, 40, 40);
	public static final java.awt.Color ACCENT_RED_COLOR = new java.awt.Color(0xA6, 0x4D, 0x5A);
	public static Color TITLE_TEXT_COLOR = new Color(0x17, 0x31, 0x74);
	public static Color SUBTITLE_TEXT_COLOR = new Color(0x6D, 0x75, 0x83);
	public static Color BORDER_COLOR = new Color(220, 224, 230);
	public static Color PLACEHOLDER_BACKGROUND_COLOR = new Color(226, 226, 226);
	public static Color SIDEBAR_BACKGROUND_COLOR = Color.WHITE;
	public static Color SIDEBAR_TEXT_COLOR = new Color(40, 40, 40);
	private static boolean darkMode;

	static {
		setDarkMode(false);
	}

	public static void toggleDarkMode() {
		setDarkMode(!darkMode);
	}

	public static void setDarkMode(boolean darkEnabled) {
		darkMode = darkEnabled;
		if (darkMode) {
			DASHBOARD_BACKGROUND_COLOR = new Color(24, 26, 31);
			PANEL_SURFACE_COLOR = new Color(34, 37, 43);
			BUTTON_SURFACE_COLOR = new Color(52, 57, 66);
			BUTTON_TEXT_COLOR = new Color(228, 233, 240);
			TITLE_TEXT_COLOR = new Color(238, 242, 247);
			SUBTITLE_TEXT_COLOR = new Color(172, 179, 189);
			BORDER_COLOR = new Color(58, 63, 72);
			PLACEHOLDER_BACKGROUND_COLOR = new Color(52, 56, 64);
			SIDEBAR_BACKGROUND_COLOR = new Color(29, 32, 38);
			SIDEBAR_TEXT_COLOR = new Color(228, 233, 240);
			return;
		}
		DASHBOARD_BACKGROUND_COLOR = new Color(247, 248, 250);
		PANEL_SURFACE_COLOR = Color.WHITE;
		BUTTON_SURFACE_COLOR = new Color(220, 226, 234);
		BUTTON_TEXT_COLOR = new Color(40, 40, 40);
		TITLE_TEXT_COLOR = new Color(0x17, 0x31, 0x74);
		SUBTITLE_TEXT_COLOR = new Color(0x6D, 0x75, 0x83);
		BORDER_COLOR = new Color(220, 224, 230);
		PLACEHOLDER_BACKGROUND_COLOR = new Color(226, 226, 226);
		SIDEBAR_BACKGROUND_COLOR = Color.WHITE;
		SIDEBAR_TEXT_COLOR = new Color(40, 40, 40);
	}

	public static boolean isDarkMode() {
		return darkMode;
	}

	public static void refreshTheme(Component component) {
		if (component instanceof ThemeAware) {
			((ThemeAware) component).applyTheme();
		}
		if (component instanceof Container) {
			Component[] subComponents = ((Container) component).getComponents();
			for (int i = 0; i < subComponents.length; i++) {
				refreshTheme(subComponents[i]);
			}
		}
		component.repaint();
	}

	public static void refreshChildrenTheme(Container container) {
		Component[] subComponents = container.getComponents();
		for (int i = 0; i < subComponents.length; i++) {
			refreshTheme(subComponents[i]);
		}
	}

	public static JPanel createContentPanel(int spacing) {
		JPanel content = new JPanel(new BorderLayout(spacing, spacing));
		content.setOpaque(false);
		content.setBorder(BorderFactory.createEmptyBorder(0, spacing, spacing, spacing));
		return content;
	}

	public static JPanel createBodyPanel(int horizontalSpacing, int verticalSpacing) {
		JPanel body = new JPanel(new BorderLayout(horizontalSpacing, verticalSpacing));
		body.setOpaque(false);
		return body;
	}

	public static JPanel createGridColumn(int rows, int columns, int horizontalGap, int verticalGap, int width) {
		JPanel column = new JPanel(new GridLayout(rows, columns, horizontalGap, verticalGap));
		column.setOpaque(false);
		column.setPreferredSize(new Dimension(width, 10));
		return column;
	}

	public static JPanel createRightColumn(int width, int spacing) {
		JPanel column = new JPanel(new BorderLayout(0, spacing));
		column.setOpaque(false);
		column.setPreferredSize(new Dimension(width, 10));
		return column;
	}
}
