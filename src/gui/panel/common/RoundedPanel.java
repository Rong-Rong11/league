package gui.panel.common;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class RoundedPanel extends JPanel {
	private final int cornerRadius;

	public RoundedPanel() {
		this(20);
	}

	public RoundedPanel(int radius) {
		setOpaque(false);
		cornerRadius = radius;
	}

	public RoundedPanel(LayoutManager layout) {
		this(layout, 20);
	}

	public RoundedPanel(LayoutManager layout, int radius) {
		super(layout);
		setOpaque(false);
		cornerRadius = radius;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(getBackground());
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
		super.paintComponent(g);
	}
}
