package gui.panel.common;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class RoundedButton extends JButton implements ThemeAware {
	private static final int DEFAULT_RADIUS = 18;

	private final int cornerRadius;
	private boolean customBackgroundSet;
	private boolean customForegroundSet;

	public RoundedButton() {
		this("");
	}

	public RoundedButton(String text) {
		super(text);
		setOpaque(false);
		setContentAreaFilled(false);
		setFocusPainted(false);
		setBorderPainted(false);
		setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
		cornerRadius = DEFAULT_RADIUS;
		super.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
		super.setForeground(DashboardPanelUtil.BUTTON_TEXT_COLOR);
	}

	@Override
	public void setBackground(Color backgroundColor) {
		customBackgroundSet = true;
		super.setBackground(backgroundColor);
	}

	@Override
	public void setForeground(Color textColor) {
		customForegroundSet = true;
		super.setForeground(textColor);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (getModel().isPressed()) {
			g2.setColor(getBackground().darker());
		} else {
			g2.setColor(getBackground());
		}
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
		super.paintComponent(g);
	}

	@Override
	public void applyTheme() {
		if (!customBackgroundSet) {
			super.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
		}
		if (!customForegroundSet) {
			super.setForeground(DashboardPanelUtil.BUTTON_TEXT_COLOR);
		}
	}
}
