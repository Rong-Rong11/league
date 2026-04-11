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

	@Override
	public Color getRevenueColor() {
		return new Color(0x5B, 0xA2, 0xFF);
	}

	@Override
	public Color getExpenseColor() {
		return new Color(0xFF, 0x6F, 0x6F);
	}

	@Override
	public Color getPositiveValueColor() {
		return new Color(0x54, 0xC5, 0x85);
	}

	@Override
	public Color getNegativeValueColor() {
		return new Color(0xFF, 0x7C, 0x7C);
	}

	@Override
	public Color getNeutralAccentColor() {
		return new Color(0xE0, 0xB1, 0x5E);
	}

	@Override
	public Color getPolicyThriftyColor() {
		return new Color(0x58, 0xD1, 0x97);
	}

	@Override
	public Color getPolicyBalancedColor() {
		return new Color(0x73, 0xB5, 0xFF);
	}

	@Override
	public Color getPolicyAmbitiousColor() {
		return new Color(0xFF, 0xA5, 0x4B);
	}

	@Override
	public Color getStrategyRebuildColor() {
		return new Color(0xB1, 0x87, 0xFF);
	}

	@Override
	public Color getStrategyAllInColor() {
		return new Color(0xFF, 0x7A, 0x7A);
	}

	@Override
	public Color getStrategyBalancedColor() {
		return new Color(0x5E, 0xD2, 0xD8);
	}

	@Override
	public Color getMarketSmallColor() {
		return new Color(0xFF, 0xB2, 0x5E);
	}

	@Override
	public Color getMarketMediumColor() {
		return new Color(0x83, 0xB8, 0xFF);
	}

	@Override
	public Color getMarketLargeColor() {
		return new Color(0xC0, 0x9A, 0xFF);
	}
}
