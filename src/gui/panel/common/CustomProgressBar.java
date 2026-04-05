package gui.panel.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class CustomProgressBar extends JPanel implements ThemeAware {
	private static final int DEFAULT_BAR_HEIGHT = 12;

	private int minimum;
	private int maximum;
	private int value;
	private int cornerRadius;
	private Color fillColor;

	public CustomProgressBar() {
		this(0, 100);
	}

	public CustomProgressBar(int minimum, int maximum) {
		this.minimum = minimum;
		this.maximum = maximum;
		cornerRadius = DEFAULT_BAR_HEIGHT;
		fillColor = new Color(0x17, 0x31, 0x74);
		setOpaque(false);
		setPreferredSize(new Dimension(260, DEFAULT_BAR_HEIGHT));
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
		repaint();
	}

	public void setMaximum(int maximum) {
		this.maximum = maximum;
		repaint();
	}

	public void setValue(int value) {
		this.value = Math.max(minimum, Math.min(value, maximum));
		repaint();
	}

	public int getValue() {
		return value;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
		repaint();
	}

	public void setCornerRadius(int cornerRadius) {
		this.cornerRadius = cornerRadius;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int width = getWidth();
		int height = getHeight();
		if (width <= 0 || height <= 0) {
			return;
		}

		g2.setColor(getTrackColor());
		g2.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);

		int range = maximum - minimum;
		if (range > 0 && value > minimum) {
			double ratio = (value - minimum) / (double) range;
			int filledWidth = (int) Math.round(width * ratio);
			if (filledWidth > 0) {
				g2.setColor(fillColor);
				g2.fillRoundRect(0, 0, filledWidth, height, cornerRadius, cornerRadius);
			}
		}
	}

	private Color getTrackColor() {
		if (DashboardPanelUtil.isDarkMode()) {
			return new Color(53, 58, 68);
		}
		return new Color(227, 232, 238);
	}

	@Override
	public void applyTheme() {
		repaint();
	}
}
