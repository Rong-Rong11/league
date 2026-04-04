package gui.panel.common;

public class DashboardCard extends RoundedPanel implements ThemeAware {

	public DashboardCard() {
		super(24);
		applyTheme();
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
	}
}
