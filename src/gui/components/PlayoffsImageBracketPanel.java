package gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.ThemeAware;

public class PlayoffsImageBracketPanel extends JPanel implements ThemeAware {
	private static final int REFERENCE_IMAGE_WIDTH = 1343;
	private static final int REFERENCE_IMAGE_HEIGHT = 1171;
	private static final int LABEL_WIDTH = 120;
	private static final int LABEL_HEIGHT = 24;
	private static final int CHAMPION_LABEL_WIDTH = 130;
	private static final int CHAMPION_LABEL_HEIGHT = 28;
	private static final String LIGHT_IMAGE_PATH = "src/resources/playoffs_bracket_empty.png";
	private static final String DARK_IMAGE_PATH = "src/resources/playoffs_bracket_empty_dark.png";

	private Image bracketImage;
	private String currentImagePath;
	private int imageWidth = REFERENCE_IMAGE_WIDTH;
	private int imageHeight = REFERENCE_IMAGE_HEIGHT;
	private int drawWidth = REFERENCE_IMAGE_WIDTH;
	private int drawHeight = REFERENCE_IMAGE_HEIGHT;
	private int drawX = 0;
	private int drawY = 0;
	private HashMap<String, JLabel> labels = new HashMap<String, JLabel>();
	private HashMap<String, BracketPosition> positions = new HashMap<String, BracketPosition>();

	public PlayoffsImageBracketPanel() {
		setLayout(null);
		setBackground(getImageBackgroundColor());
		setPreferredSize(new Dimension(980, 820));
		loadImage();
		createLabels();
	}

	private void loadImage() {
		String imagePath = getImagePath();
		if (imagePath.equals(currentImagePath) && bracketImage != null) {
			return;
		}
		ImageIcon icon = new ImageIcon(imagePath);
		if (icon.getIconWidth() <= 0) {
			imagePath = LIGHT_IMAGE_PATH;
			icon = new ImageIcon(imagePath);
		}
		if (icon.getIconWidth() > 0) {
			bracketImage = icon.getImage();
			imageWidth = icon.getIconWidth();
			imageHeight = icon.getIconHeight();
			currentImagePath = imagePath;
		}
	}

	private String getImagePath() {
		if (DashboardPanelUtil.isDarkMode()) {
			return DARK_IMAGE_PATH;
		}
		return LIGHT_IMAGE_PATH;
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
		double scaleX = drawWidth / (double) REFERENCE_IMAGE_WIDTH;
		double scaleY = drawHeight / (double) REFERENCE_IMAGE_HEIGHT;
		int width = Math.max(40, (int) Math.round(baseWidth * scaleX));
		int height = Math.max(16, (int) Math.round(baseHeight * scaleY));
		int centerX = drawX + (int) Math.round(baseCenterX * scaleX);
		int centerY = drawY + (int) Math.round(baseCenterY * scaleY);
		label.setBounds(centerX - width / 2, centerY - height / 2, width, height);
		label.setFont(new Font(Font.SANS_SERIF, label.getText() != null && label.getText().length() > 3 ? Font.PLAIN
				: Font.BOLD, Math.max(9, (int) Math.round(14 * Math.min(scaleX, scaleY)))));
	}

	private JLabel createLabel(String position) {
		JLabel label = new JLabel("", SwingConstants.CENTER);
		label.setOpaque(false);
		label.setFont(new Font(Font.SANS_SERIF, "e1".equals(position) ? Font.BOLD : Font.PLAIN,
				"e1".equals(position) ? 16 : 14));
		label.setForeground(getFilledLabelColor());
		return label;
	}

	public void setTeamName(String position, String shortName) {
		JLabel label = labels.get(position);
		if (label == null) {
			return;
		}
		label.setText(shortName == null ? "" : shortName);
		label.setForeground(shortName == null || shortName.equals("") ? getEmptyLabelColor() : getFilledLabelColor());
	}

	public void refreshFromPlayoffsData(Map<String, String> positions) {
		loadImage();
		for (String key : labels.keySet()) {
			setTeamName(key, "");
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

	@Override
	public void applyTheme() {
		setBackground(getImageBackgroundColor());
		loadImage();
		refreshLabelColors();
		repaint();
	}

	private void refreshLabelColors() {
		for (String key : labels.keySet()) {
			JLabel label = labels.get(key);
			label.setForeground(label.getText() == null || label.getText().equals("")
					? getEmptyLabelColor()
					: getFilledLabelColor());
		}
	}

	private Color getFilledLabelColor() {
		return DashboardPanelUtil.TITLE_TEXT_COLOR;
	}

	private Color getEmptyLabelColor() {
		return DashboardPanelUtil.SUBTITLE_TEXT_COLOR;
	}

	private Color getImageBackgroundColor() {
		if (DashboardPanelUtil.isDarkMode()) {
			return new Color(0x08, 0x0F, 0x1C);
		}
		return DashboardPanelUtil.PANEL_SURFACE_COLOR;
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
			updateDrawBounds();
			refreshLabelBounds();
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(bracketImage, drawX, drawY, drawWidth, drawHeight, this);
			return;
		}
		g.setColor(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(getEmptyLabelColor());
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		g.drawString("Ajoutez l'image de l'arbre dans src/resources pour afficher les playoffs.", 260, 640);
	}

	private void updateDrawBounds() {
		int availableWidth = Math.max(1, getWidth());
		int availableHeight = Math.max(1, getHeight());
		double scale = Math.min(availableWidth / (double) imageWidth, availableHeight / (double) imageHeight);
		drawWidth = (int) Math.round(imageWidth * scale);
		drawHeight = (int) Math.round(imageHeight * scale);
		drawX = (availableWidth - drawWidth) / 2;
		drawY = (availableHeight - drawHeight) / 2;
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
