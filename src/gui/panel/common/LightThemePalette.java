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

	@Override
	public Color getRevenueColor() {
		return new Color(0x2C, 0x6B, 0xD9);
	}

	@Override
	public Color getExpenseColor() {
		return new Color(0xD0, 0x55, 0x55);
	}

	@Override
	public Color getPositiveValueColor() {
		return new Color(0x2E, 0x8B, 0x57);
	}

	@Override
	public Color getNegativeValueColor() {
		return new Color(0xC0, 0x3F, 0x3F);
	}

	@Override
	public Color getNeutralAccentColor() {
		return new Color(0xC4, 0x8A, 0x32);
	}

	@Override
	public Color getPolicyThriftyColor() {
		return new Color(0x2F, 0x8F, 0x66);
	}

	@Override
	public Color getPolicyBalancedColor() {
		return new Color(0x2F, 0x6F, 0xC1);
	}

	@Override
	public Color getPolicyAmbitiousColor() {
		return new Color(0xCC, 0x7A, 0x2A);
	}

	@Override
	public Color getStrategyRebuildColor() {
		return new Color(0x8A, 0x58, 0xC7);
	}

	@Override
	public Color getStrategyAllInColor() {
		return new Color(0xC5, 0x3D, 0x3D);
	}

	@Override
	public Color getStrategyBalancedColor() {
		return new Color(0x2B, 0x8D, 0x95);
	}

	@Override
	public Color getMarketSmallColor() {
		return new Color(0xC2, 0x7A, 0x2E);
	}

	@Override
	public Color getMarketMediumColor() {
		return new Color(0x4F, 0x7E, 0xC8);
	}

	@Override
	public Color getMarketLargeColor() {
		return new Color(0x6C, 0x57, 0xC8);
	}
}
