package gui.panel.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class DashboardPanelUtil {
	private static final ThemePalette LIGHT_THEME_PALETTE = new LightThemePalette();
	private static final ThemePalette DARK_THEME_PALETTE = new DarkThemePalette();

	public static Color DASHBOARD_BACKGROUND_COLOR = new Color(247, 248, 250);
	public static Color PANEL_SURFACE_COLOR = Color.WHITE;
	public static Color BUTTON_SURFACE_COLOR = new Color(220, 226, 234);
	public static Color BUTTON_TEXT_COLOR = new Color(40, 40, 40);
	public static Color ACCENT_RED_COLOR = new Color(0xA6, 0x4D, 0x5A);
	public static Color TITLE_TEXT_COLOR = new Color(0x17, 0x31, 0x74);
	public static Color SUBTITLE_TEXT_COLOR = new Color(0x6D, 0x75, 0x83);
	public static Color BORDER_COLOR = new Color(220, 224, 230);
	public static Color PLACEHOLDER_BACKGROUND_COLOR = new Color(226, 226, 226);
	public static Color SIDEBAR_BACKGROUND_COLOR = Color.WHITE;
	public static Color SIDEBAR_TEXT_COLOR = new Color(40, 40, 40);
	public static Color REVENUE_COLOR = new Color(0x2C, 0x6B, 0xD9);
	public static Color EXPENSE_COLOR = new Color(0xD0, 0x55, 0x55);
	public static Color POSITIVE_VALUE_COLOR = new Color(0x2E, 0x8B, 0x57);
	public static Color NEGATIVE_VALUE_COLOR = new Color(0xC0, 0x3F, 0x3F);
	public static Color NEUTRAL_ACCENT_COLOR = new Color(0xC4, 0x8A, 0x32);
	public static Color POLICY_THRIFTY_COLOR = new Color(0x2F, 0x8F, 0x66);
	public static Color POLICY_BALANCED_COLOR = new Color(0x2F, 0x6F, 0xC1);
	public static Color POLICY_AMBITIOUS_COLOR = new Color(0xC5, 0x3D, 0x3D);
	public static Color STRATEGY_REBUILD_COLOR = new Color(0x8A, 0x58, 0xC7);
	public static Color STRATEGY_ALL_IN_COLOR = new Color(0xC5, 0x3D, 0x3D);
	public static Color STRATEGY_BALANCED_COLOR = new Color(0x2B, 0x8D, 0x95);
	public static Color MARKET_SMALL_COLOR = new Color(0xC2, 0x7A, 0x2E);
	public static Color MARKET_MEDIUM_COLOR = new Color(0x4F, 0x7E, 0xC8);
	public static Color MARKET_LARGE_COLOR = new Color(0x6C, 0x57, 0xC8);
	public static Color ON_ACCENT_TEXT_COLOR = Color.WHITE;
	private static boolean darkMode;
	private static ThemePalette currentPalette;

	static {
		setDarkMode(false);
	}

	public static void toggleDarkMode() {
		setDarkMode(!darkMode);
	}

	public static void setDarkMode(boolean darkEnabled) {
		darkMode = darkEnabled;
		if (darkMode) {
			currentPalette = DARK_THEME_PALETTE;
		} else {
			currentPalette = LIGHT_THEME_PALETTE;
		}
		applyPalette(currentPalette);
	}

	public static boolean isDarkMode() {
		return darkMode;
	}

	public static ThemePalette getCurrentPalette() {
		return currentPalette;
	}

	private static void applyPalette(ThemePalette palette) {
		DASHBOARD_BACKGROUND_COLOR = palette.getDashboardBackgroundColor();
		PANEL_SURFACE_COLOR = palette.getPanelSurfaceColor();
		BUTTON_SURFACE_COLOR = palette.getButtonSurfaceColor();
		BUTTON_TEXT_COLOR = palette.getButtonTextColor();
		ACCENT_RED_COLOR = palette.getAccentRedColor();
		TITLE_TEXT_COLOR = palette.getTitleTextColor();
		SUBTITLE_TEXT_COLOR = palette.getSubtitleTextColor();
		BORDER_COLOR = palette.getBorderColor();
		PLACEHOLDER_BACKGROUND_COLOR = palette.getPlaceholderBackgroundColor();
		SIDEBAR_BACKGROUND_COLOR = palette.getSidebarBackgroundColor();
		SIDEBAR_TEXT_COLOR = palette.getSidebarTextColor();
		REVENUE_COLOR = palette.getRevenueColor();
		EXPENSE_COLOR = palette.getExpenseColor();
		POSITIVE_VALUE_COLOR = palette.getPositiveValueColor();
		NEGATIVE_VALUE_COLOR = palette.getNegativeValueColor();
		NEUTRAL_ACCENT_COLOR = palette.getNeutralAccentColor();
		POLICY_THRIFTY_COLOR = palette.getPolicyThriftyColor();
		POLICY_BALANCED_COLOR = palette.getPolicyBalancedColor();
		POLICY_AMBITIOUS_COLOR = palette.getPolicyAmbitiousColor();
		STRATEGY_REBUILD_COLOR = palette.getStrategyRebuildColor();
		STRATEGY_ALL_IN_COLOR = palette.getStrategyAllInColor();
		STRATEGY_BALANCED_COLOR = palette.getStrategyBalancedColor();
		MARKET_SMALL_COLOR = palette.getMarketSmallColor();
		MARKET_MEDIUM_COLOR = palette.getMarketMediumColor();
		MARKET_LARGE_COLOR = palette.getMarketLargeColor();
	}

	public static Color getValueColorForAmount(double value) {
		if (value > 0.0) {
			return POSITIVE_VALUE_COLOR;
		}
		if (value < 0.0) {
			return NEGATIVE_VALUE_COLOR;
		}
		return TITLE_TEXT_COLOR;
	}

	public static Color getPrimaryActionColor() {
		return POLICY_BALANCED_COLOR;
	}

	public static Color getPrimaryActionTextColor() {
		if (isDarkMode()) {
			return DASHBOARD_BACKGROUND_COLOR;
		}
		return ON_ACCENT_TEXT_COLOR;
	}

	public static Color getNavigationButtonColor() {
		return POLICY_BALANCED_COLOR;
	}

	public static Color getHeaderAccentColor() {
		return POLICY_BALANCED_COLOR;
	}

	public static Color getProgressFillColor() {
		return NEUTRAL_ACCENT_COLOR;
	}

	public static Color getProgressTrackColor() {
		if (isDarkMode()) {
			return new Color(53, 58, 68);
		}
		return new Color(227, 232, 238);
	}

	public static Color getCalendarGridBorderColor() {
		if (isDarkMode()) {
			return new Color(58, 63, 72);
		}
		return BORDER_COLOR;
	}

	public static Color getCalendarHeaderBackgroundColor() {
		return getHeaderAccentColor();
	}

	public static Color getCalendarCellBackgroundColor() {
		if (isDarkMode()) {
			return PANEL_SURFACE_COLOR;
		}
		return Color.WHITE;
	}

	public static Color getCalendarDisplayedDayBackgroundColor() {
		if (isDarkMode()) {
			return new Color(42, 46, 54);
		}
		return new Color(245, 247, 250);
	}

	public static Color getCalendarOtherMonthBackgroundColor() {
		if (isDarkMode()) {
			return new Color(30, 33, 39);
		}
		return new Color(245, 246, 248);
	}

	public static Color getCalendarOutsideMonthTextColor() {
		if (isDarkMode()) {
			return new Color(111, 118, 128);
		}
		return new Color(180, 185, 193);
	}

	public static Color getCalendarMatchChipColor() {
		if (isDarkMode()) {
			return new Color(47, 53, 62);
		}
		return new Color(236, 242, 250);
	}

	public static Color getCalendarPlayoffDayBackgroundColor() {
		if (isDarkMode()) {
			return new Color(45, 38, 31);
		}
		return new Color(255, 247, 232);
	}

	public static Color getCalendarDisplayedPlayoffDayBackgroundColor() {
		if (isDarkMode()) {
			return new Color(55, 43, 33);
		}
		return new Color(250, 235, 211);
	}

	public static Color getCalendarPlayoffMatchChipColor() {
		if (isDarkMode()) {
			return new Color(111, 70, 43);
		}
		return new Color(255, 214, 153);
	}

	public static Color getCurrentDayBackgroundColor() {
		if (isDarkMode()) {
			return new Color(48, 54, 66);
		}
		return new Color(0xE8, 0xF2, 0xFF);
	}

	public static Color getCurrentDayBorderColor() {
		if (isDarkMode()) {
			return ON_ACCENT_TEXT_COLOR;
		}
		return POLICY_BALANCED_COLOR;
	}

	public static Color getCalendarSlotBaseColor(String slotKey) {
		if (isDarkMode()) {
			if ("AFTERNOON".equals(slotKey)) {
				return new Color(0x7A, 0x6A, 0x33);
			}
			if ("EVENING".equals(slotKey)) {
				return new Color(0x2E, 0x53, 0x61);
			}
			return new Color(0x33, 0x2F, 0x7A);
		}
		if ("AFTERNOON".equals(slotKey)) {
			return new Color(0xF8, 0xE9, 0x9A);
		}
		if ("EVENING".equals(slotKey)) {
			return new Color(0xC8, 0xEE, 0xF6);
		}
		return new Color(0x4D, 0x46, 0xF0);
	}

	public static Color getCalendarPlayoffSlotBaseColor() {
		if (isDarkMode()) {
			return new Color(0x6D, 0x45, 0x2A);
		}
		return new Color(0xFF, 0xD0, 0x8A);
	}

	public static Color getCalendarPlayoffSlotDisplayedColor() {
		if (isDarkMode()) {
			return new Color(0x5C, 0x39, 0x24);
		}
		return new Color(0xF2, 0xBC, 0x73);
	}

	public static Color getCalendarSlotDisplayedColor(String slotKey) {
		if (isDarkMode()) {
			if ("AFTERNOON".equals(slotKey)) {
				return new Color(0x64, 0x57, 0x2A);
			}
			if ("EVENING".equals(slotKey)) {
				return new Color(0x2A, 0x49, 0x55);
			}
			return new Color(0x2D, 0x2A, 0x67);
		}
		if ("AFTERNOON".equals(slotKey)) {
			return new Color(0xF2, 0xE4, 0xB8);
		}
		if ("EVENING".equals(slotKey)) {
			return new Color(0xD9, 0xEC, 0xF0);
		}
		return new Color(0x8C, 0x88, 0xE8);
	}

	public static Color getCalendarSlotTitleColor(String slotKey) {
		if (isDarkMode() || "NIGHT".equals(slotKey)) {
			return ON_ACCENT_TEXT_COLOR;
		}
		return TITLE_TEXT_COLOR;
	}

	public static Color getCalendarSlotSubtitleColor(String slotKey) {
		if (isDarkMode()) {
			return new Color(230, 234, 240);
		}
		if ("NIGHT".equals(slotKey)) {
			return ON_ACCENT_TEXT_COLOR;
		}
		return SUBTITLE_TEXT_COLOR;
	}

	public static Color getMapPointColor() {
		return EXPENSE_COLOR;
	}

	public static Color getSelectedMapPointColor() {
		return REVENUE_COLOR;
	}

	public static Color getFinancialPolicyColor(String policyName) {
		if (policyName == null) {
			return TITLE_TEXT_COLOR;
		}
		String lowerName = policyName.toLowerCase();
		if (lowerName.contains("thrifty")) {
			return POLICY_THRIFTY_COLOR;
		}
		if (lowerName.contains("ambitious")) {
			return POLICY_AMBITIOUS_COLOR;
		}
		if (lowerName.contains("balanced")) {
			return POLICY_BALANCED_COLOR;
		}
		return TITLE_TEXT_COLOR;
	}

	public static Color getTransferStrategyColor(String strategyName) {
		if (strategyName == null) {
			return TITLE_TEXT_COLOR;
		}
		String lowerName = strategyName.toLowerCase();
		if (lowerName.contains("rebuild")) {
			return STRATEGY_REBUILD_COLOR;
		}
		if (lowerName.contains("all in") || lowerName.contains("allin") || lowerName.contains("superstar")) {
			return STRATEGY_ALL_IN_COLOR;
		}
		if (lowerName.contains("balanced") || lowerName.contains("small adjust") || lowerName.contains("salary dump")) {
			return STRATEGY_BALANCED_COLOR;
		}
		return TITLE_TEXT_COLOR;
	}

	public static Color getMarketColor(String marketName) {
		if (marketName == null) {
			return TITLE_TEXT_COLOR;
		}
		String lowerName = marketName.toLowerCase();
		if (lowerName.contains("small")) {
			return MARKET_SMALL_COLOR;
		}
		if (lowerName.contains("medium")) {
			return MARKET_MEDIUM_COLOR;
		}
		if (lowerName.contains("large")) {
			return MARKET_LARGE_COLOR;
		}
		return TITLE_TEXT_COLOR;
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

	public static Border createSurfaceBorder(int padding) {
		return createSurfaceBorder(padding, padding);
	}

	public static Border createSurfaceBorder(int verticalPadding, int horizontalPadding) {
		return BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(BORDER_COLOR, 1),
				BorderFactory.createEmptyBorder(verticalPadding, horizontalPadding, verticalPadding, horizontalPadding));
	}

	public static void applySurfaceCard(JComponent component, int padding) {
		applySurfaceCard(component, padding, padding);
	}

	public static void applySurfaceCard(JComponent component, int verticalPadding, int horizontalPadding) {
		component.setBackground(PANEL_SURFACE_COLOR);
		component.setBorder(createSurfaceBorder(verticalPadding, horizontalPadding));
	}
}
