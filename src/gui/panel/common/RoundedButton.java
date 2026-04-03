package gui.panel.common;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class RoundedButton extends JButton {
	private static final int DEFAULT_RADIUS = 18;

	private final int cornerRadius;

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
}
