package gui.panel.mapPanel.effectifPanel.teamPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.ThemeAware;
import process.orchestrator.interfaces.TeamGetterInterface;

public class TeamLogoPanel extends JPanel implements ThemeAware {
	private static final String LOGO_FOLDER_PATH = "src/resources/nba_logos/";
	private static final int DEFAULT_LOGO_SIZE = 64;

	private JLabel logoLabel;
	private String teamName;
	private int logoSize;
	private TeamGetterInterface teamQueryInterface;

	public TeamLogoPanel() {
		this("", DEFAULT_LOGO_SIZE);
	}

	public TeamLogoPanel(String teamName, int logoSize) {
		this.teamName = teamName;
		this.logoSize = logoSize;
		create();
		organize();
		updateLogo();
	}

	private void create() {
		logoLabel = new JLabel("", JLabel.CENTER);
		logoLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
	}

	private void organize() {
		setLayout(new BorderLayout());
		setOpaque(false);
		setPreferredSize(new Dimension(logoSize, logoSize));
		add(logoLabel, BorderLayout.CENTER);
		applyTheme();
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
		updateLogo();
	}

	public void setTeamQueryInterface(TeamGetterInterface teamQueryInterface) {
		this.teamQueryInterface = teamQueryInterface;
		updateLogo();
	}

	private void updateLogo() {
		File logoFile = new File(LOGO_FOLDER_PATH + buildFileName(teamName));
		if (!logoFile.exists()) {
			showFallbackLabel();
			return;
		}

		ImageIcon icon = new ImageIcon(logoFile.getPath());
		if (icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
			showFallbackLabel();
			return;
		}
		Image scaledImage = icon.getImage().getScaledInstance(logoSize, logoSize, Image.SCALE_SMOOTH);
		logoLabel.setText("");
		logoLabel.setIcon(new ImageIcon(scaledImage));
	}

	private void showFallbackLabel() {
		logoLabel.setIcon(null);
		logoLabel.setText(buildAbbreviation(teamName));
	}

	private String buildFileName(String teamName) {
		if (teamName == null || teamName.isEmpty()) {
			return "";
		}
		return teamName.replace(" ", "_") + ".png";
	}

	private String buildAbbreviation(String teamName) {
		if (teamQueryInterface != null && teamName != null && !teamName.equals("")) {
			return teamQueryInterface.getTeamAbbreviation(teamName);
		}
		if (teamName == null || teamName.equals("")) {
			return "---";
		}
		String[] nameParts = teamName.split(" ");
		if (nameParts.length == 1) {
			return nameParts[0].substring(0, Math.min(3, nameParts[0].length())).toUpperCase();
		}
		StringBuilder abbreviation = new StringBuilder();
		for (String namePart : nameParts) {
			if (!namePart.isEmpty()) {
				abbreviation.append(Character.toUpperCase(namePart.charAt(0)));
			}
		}
		return abbreviation.length() == 0 ? "---" : abbreviation.toString();
	}

	@Override
	public void applyTheme() {
		logoLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
	}
}
