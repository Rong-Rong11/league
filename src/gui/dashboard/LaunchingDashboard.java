package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gui.panel.common.ButtonStyleUtil;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.LabelStyleUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.ThemeAware;

public class LaunchingDashboard extends JPanel implements ThemeAware {

	private JLabel badgeLabel;
	private JLabel titleLabel;
	private JLabel subtitleLabel;
	private JLabel footerLabel;
	private JPanel heroPanel;
	private JPanel featuresPanel;
	private JButton continueButton;
	private JButton infoButton;
	private JButton themeButton;

	public LaunchingDashboard() {
		create();
		organize();
		actions();
		applyTheme();
	}

	private void create() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(60, 110, 55, 110));

		badgeLabel = new JLabel("SAISON NBA - MANAGEMENT - FINANCES", JLabel.CENTER);
		badgeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

		titleLabel = new JLabel("NBA League Simulator", JLabel.CENTER);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 54));

		subtitleLabel = new JLabel(
				"Construis ta ligue, configure ton equipe et mene ta franchise au titre",
				JLabel.CENTER);
		subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));

		footerLabel = new JLabel(
				"Calendrier - Classements - Playoffs - Finances - Evolution des equipes",
				JLabel.CENTER);
		footerLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));

		heroPanel = new JPanel(new BorderLayout(0, 22));
		heroPanel.setPreferredSize(new Dimension(900, 620));

		featuresPanel = new JPanel(new GridLayout(1, 3, 22, 0));
		featuresPanel.setOpaque(false);
		featuresPanel.add(createFeatureCard("Simulation",
				"Avance journee par journee et suis les resultats de toute la ligue."));
		featuresPanel.add(createFeatureCard("Analyse",
				"Observe le classement, les series, les statistiques et les tendances."));
		featuresPanel.add(createFeatureCard("Finances",
				"Configure les profils economiques et mesure l impact sur les franchises."));

		continueButton = new RoundedButton("Continuer");
		ButtonStyleUtil.styleActionButton(continueButton, 360, 78, 24);

		infoButton = new RoundedButton("Comment fonctionne la simulation ?");
		ButtonStyleUtil.styleActionButton(infoButton, 285, 38, 13);

		themeButton = new RoundedButton("Mode sombre");
		ButtonStyleUtil.styleActionButton(themeButton, 160, 38, 13);
	}

	private JPanel createFeatureCard(String title, String text) {
		JPanel card = new JPanel(new BorderLayout(0, 12));
		card.setBorder(BorderFactory.createEmptyBorder(24, 22, 24, 22));

		JLabel titleLabel = new JLabel(title, JLabel.CENTER);
		LabelStyleUtil.styleTitleLabel(titleLabel, 20);

		JLabel textLabel = new JLabel(
				"<html><div style='text-align:center; width:210px;'>" + text + "</div></html>",
				JLabel.CENTER);
		LabelStyleUtil.styleSubtitleLabel(textLabel, 13);

		card.add(titleLabel, BorderLayout.NORTH);
		card.add(textLabel, BorderLayout.CENTER);
		return card;
	}

	private void organize() {
		JPanel headerPanel = new JPanel(new GridLayout(4, 1, 0, 10));
		headerPanel.setOpaque(false);
		headerPanel.add(badgeLabel);
		headerPanel.add(titleLabel);
		headerPanel.add(subtitleLabel);
		headerPanel.add(footerLabel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

		continueButton.setAlignmentX(CENTER_ALIGNMENT);
		infoButton.setAlignmentX(CENTER_ALIGNMENT);
		themeButton.setAlignmentX(CENTER_ALIGNMENT);

		buttonPanel.add(continueButton);
		buttonPanel.add(Box.createVerticalStrut(12));
		buttonPanel.add(infoButton);
		buttonPanel.add(Box.createVerticalStrut(8));
		buttonPanel.add(themeButton);

		heroPanel.add(headerPanel, BorderLayout.NORTH);
		heroPanel.add(featuresPanel, BorderLayout.CENTER);
		heroPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(heroPanel, BorderLayout.CENTER);
	}

	private void actions() {
		infoButton.addActionListener(new InfoAction());
	}

	public JButton getContinueButton() {
		return continueButton;
	}

	public JButton getThemeButton() {
		return themeButton;
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);

		heroPanel.setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		heroPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(DashboardPanelUtil.BORDER_COLOR),
				BorderFactory.createEmptyBorder(30, 40, 30, 40)));

		badgeLabel.setForeground(DashboardPanelUtil.NEUTRAL_ACCENT_COLOR);
		titleLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		subtitleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		footerLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);

		for (int i = 0; i < featuresPanel.getComponentCount(); i++) {
			JPanel card = (JPanel) featuresPanel.getComponent(i);
			card.setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
			card.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(DashboardPanelUtil.BORDER_COLOR),
					BorderFactory.createEmptyBorder(24, 22, 24, 22)));
		}

		ButtonStyleUtil.styleActionButton(continueButton, 360, 78, 24);
		continueButton.setBackground(DashboardPanelUtil.getPrimaryActionColor());
		continueButton.setForeground(DashboardPanelUtil.getPrimaryActionTextColor());

		ButtonStyleUtil.styleActionButton(infoButton, 285, 38, 13);

		themeButton.setText(DashboardPanelUtil.isDarkMode() ? "Mode clair" : "Mode sombre");
		ButtonStyleUtil.styleActionButton(themeButton, 160, 38, 13);
		themeButton.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
		themeButton.setForeground(DashboardPanelUtil.BUTTON_TEXT_COLOR);

		revalidate();
		repaint();
	}

	private class InfoAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(
					LaunchingDashboard.this,
					"Avant de lancer la simulation, vous pouvez selectionner une equipe directement sur la carte "
							+ "et configurer ses parametres financiers, comme la taille du marche ou le profil economique.\n\n"
							+ "Une fois la configuration terminee, vous pouvez lancer la simulation et suivre une saison complete de NBA.\n\n"
							+ "Vous pourrez ensuite :\n\n"
							+ "- simuler les journees de matchs\n"
							+ "- suivre l evolution du classement\n"
							+ "- consulter les statistiques des equipes et des joueurs\n"
							+ "- observer l impact financier de la saison\n"
							+ "- naviguer entre les differentes vues de l application\n\n"
							+ "Chaque action fait progresser la saison et met a jour les informations en temps reel.",
					"Fonctionnement de la simulation",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
}