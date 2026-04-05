package gui.panel.common;

import java.awt.Color;

public class LightThemePalette implements ThemePalette {
	@Override
	public Color getDashboardBackgroundColor() {
		return new Color(247, 248, 250);
	}

	@Override
	public Color getPanelSurfaceColor() {
		return Color.WHITE;
	}

	@Override
	public Color getButtonSurfaceColor() {
		return new Color(220, 226, 234);
	}

	@Override
	public Color getButtonTextColor() {
		return new Color(40, 40, 40);
	}

	@Override
	public Color getAccentRedColor() {
		return new Color(0xA6, 0x4D, 0x5A);
	}

	@Override
	public Color getTitleTextColor() {
		return new Color(0x17, 0x31, 0x74);
	}

	@Override
	public Color getSubtitleTextColor() {
		return new Color(0x6D, 0x75, 0x83);
	}

	@Override
	public Color getBorderColor() {
		return new Color(220, 224, 230);
	}

	@Override
	public Color getPlaceholderBackgroundColor() {
		return new Color(226, 226, 226);
	}

	@Override
	public Color getSidebarBackgroundColor() {
		return Color.WHITE;
	}

	@Override
	public Color getSidebarTextColor() {
		return new Color(40, 40, 40);
	}
}
