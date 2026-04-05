package gui.panel.common;

import java.awt.Color;

public class DarkThemePalette implements ThemePalette {
	@Override
	public Color getDashboardBackgroundColor() {
		return new Color(24, 26, 31);
	}

	@Override
	public Color getPanelSurfaceColor() {
		return new Color(34, 37, 43);
	}

	@Override
	public Color getButtonSurfaceColor() {
		return new Color(52, 57, 66);
	}

	@Override
	public Color getButtonTextColor() {
		return new Color(228, 233, 240);
	}

	@Override
	public Color getAccentRedColor() {
		return new Color(0xA6, 0x4D, 0x5A);
	}

	@Override
	public Color getTitleTextColor() {
		return new Color(238, 242, 247);
	}

	@Override
	public Color getSubtitleTextColor() {
		return new Color(172, 179, 189);
	}

	@Override
	public Color getBorderColor() {
		return new Color(58, 63, 72);
	}

	@Override
	public Color getPlaceholderBackgroundColor() {
		return new Color(52, 56, 64);
	}

	@Override
	public Color getSidebarBackgroundColor() {
		return new Color(29, 32, 38);
	}

	@Override
	public Color getSidebarTextColor() {
		return new Color(228, 233, 240);
	}
}
