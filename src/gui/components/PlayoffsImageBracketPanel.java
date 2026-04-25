package gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PlayoffsImageBracketPanel extends JPanel {
	private static final int REFERENCE_IMAGE_WIDTH = 1343;
	private static final int REFERENCE_IMAGE_HEIGHT = 1171;
	private static final int LABEL_WIDTH = 120;
	private static final int LABEL_HEIGHT = 24;
	private static final int CHAMPION_LABEL_WIDTH = 130;
	private static final int CHAMPION_LABEL_HEIGHT = 28;
	private static final String IMAGE_PATH = "src/resources/playoffs_bracket_empty.png";
	private static final Color MAIN_BLUE = new Color(0x17, 0x31, 0x74);
	private static final Color SECONDARY_TEXT = new Color(0x6D, 0x75, 0x83);

	private Image bracketImage;
	private int imageWidth = REFERENCE_IMAGE_WIDTH;
	private int imageHeight = REFERENCE_IMAGE_HEIGHT;
	private HashMap<String, JLabel> labels = new HashMap<String, JLabel>();
	private HashMap<String, BracketPosition> positions = new HashMap<String, BracketPosition>();

	public PlayoffsImageBracketPanel() {
		setLayout(null);
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(REFERENCE_IMAGE_WIDTH, REFERENCE_IMAGE_HEIGHT));
		loadImage();
		createLabels();
	}

	private void loadImage() {
		ImageIcon icon = new ImageIcon(IMAGE_PATH);
		if (icon.getIconWidth() > 0) {
			bracketImage = icon.getImage();
			imageWidth = icon.getIconWidth();
			imageHeight = icon.getIconHeight();
			setPreferredSize(new Dimension(imageWidth, imageHeight));
		}
	}

	private void createLabels() {
		addColumn("a", 125, new int[] { 54, 117, 187, 251, 324, 390, 454, 521, 629, 695, 760, 828, 901, 965,
				1031, 1096 }, LABEL_WIDTH, LABEL_HEIGHT);
		addColumn("b", 430, new int[] { 100, 231, 369, 503, 673, 810, 947, 1078 }, LABEL_WIDTH, LABEL_HEIGHT);
		addColumn("c", 721, new int[] { 182, 453, 756, 1026 }, LABEL_WIDTH, LABEL_HEIGHT);
		addColumn("d", 1020, new int[] { 317, 896 }, LABEL_WIDTH, LABEL_HEIGHT);
		addPosition("e1", 1254, 588, CHAMPION_LABEL_WIDTH, CHAMPION_LABEL_HEIGHT);
	}

	private void addColumn(String column, int centerX, int[] yPositions, int width, int height) {
		for (int i = 0; i < yPositions.length; i++) {
			addPosition(column + (i + 1), centerX, yPositions[i], width, height);
		}
	}

	private void addPosition(String position, int centerX, int centerY, int width, int height) {
		JLabel label = createLabel(position);
		positions.put(position, new BracketPosition(centerX, centerY, width, height));
		applyLabelBounds(label, centerX, centerY, width, height);
		labels.put(position, label);
		add(label);
	}

	private void applyLabelBounds(JLabel label, int baseCenterX, int baseCenterY, int baseWidth, int baseHeight) {
		double scaleX = imageWidth / (double) REFERENCE_IMAGE_WIDTH;
		double scaleY = imageHeight / (double) REFERENCE_IMAGE_HEIGHT;
		int width = (int) Math.round(baseWidth * scaleX);
		int height = (int) Math.round(baseHeight * scaleY);
		int centerX = (int) Math.round(baseCenterX * scaleX);
		int centerY = (int) Math.round(baseCenterY * scaleY);
		label.setBounds(centerX - width / 2, centerY - height / 2, width, height);
	}

	private JLabel createLabel(String position) {
		JLabel label = new JLabel("", SwingConstants.CENTER);
		label.setOpaque(false);
		label.setFont(new Font(Font.SANS_SERIF, "e1".equals(position) ? Font.BOLD : Font.PLAIN,
				"e1".equals(position) ? 16 : 14));
		label.setForeground(MAIN_BLUE);
		return label;
	}

	public void setTeamName(String position, String shortName) {
		JLabel label = labels.get(position);
		if (label == null) {
			return;
		}
		label.setText(shortName == null ? "" : shortName);
		label.setForeground(shortName == null || shortName.equals("") ? SECONDARY_TEXT : MAIN_BLUE);
	}

	public void clearPosition(String position) {
		setTeamName(position, "");
	}

	public void refreshFromPlayoffsData(Map<String, String> positions) {
		for (String key : labels.keySet()) {
			clearPosition(key);
		}
		if (positions == null) {
			repaint();
			return;
		}
		for (String key : positions.keySet()) {
			setTeamName(key, positions.get(key));
		}
		repaint();
	}

	private void refreshLabelBounds() {
		for (String key : labels.keySet()) {
			BracketPosition position = positions.get(key);
			if (position != null) {
				applyLabelBounds(labels.get(key), position.centerX, position.centerY, position.width, position.height);
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (bracketImage != null) {
			refreshLabelBounds();
			g.drawImage(bracketImage, 0, 0, imageWidth, imageHeight, this);
			return;
		}
		g.setColor(new Color(0xF7, 0xF8, 0xFA));
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(SECONDARY_TEXT);
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		g.drawString("Ajoutez l'image src/resources/playoffs_bracket_empty.png pour afficher l'arbre.", 260, 640);
	}

	private static class BracketPosition {
		private int centerX;
		private int centerY;
		private int width;
		private int height;

		private BracketPosition(int centerX, int centerY, int width, int height) {
			this.centerX = centerX;
			this.centerY = centerY;
			this.width = width;
			this.height = height;
		}
	}
}
