package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import data.team.Team;
import gui.panel.common.BuildBox;
import gui.panel.common.ButtonStyleUtil;
import gui.panel.common.DashboardTitleBanner;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.TeamMapPanel;
import gui.panel.common.ThemeAware;
import gui.panel.openningPanel.OpeningPolicyDetailPanel;
import gui.panel.openningPanel.OpeningTeamSelectionPanel;
import process.orchestrator.GUIInterface;

public class OpeningDashboard extends JPanel implements ThemeAware {

	private static final int IDEAL_DASHBOARD_SPACING = 16;
	private static final int IDEAL_DASHBOARD_HEADER_HEIGHT = 64;
	private static final int IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH = 500;
	private static final int IDEAL_DASHBOARD_TOP_CARD_HEIGHT = 335;
	private static final Color IDEAL_DASHBOARD_BACKGROUND_COLOR = DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR;

	private GUIInterface guiInterface;
	private ArrayList<Team> teams;
	private Team selectedTeam;
	private JButton continueButton;
	private JButton themeButton;
	private JButton randomPoliciesButton;
	private TeamMapPanel openingMapPanel;
	private OpeningTeamSelectionPanel teamSelectionPanel;
	private OpeningPolicyDetailPanel policyDetailPanel;

	public OpeningDashboard(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		create();
		organize();
		actions();
		selectDefaultTeam();
	}

	private void create() {
		teams = new ArrayList<Team>(guiInterface.getTeams());
		continueButton = new RoundedButton("Continuer");
		themeButton = new RoundedButton("Mode sombre");
		randomPoliciesButton = new RoundedButton();
		openingMapPanel = new TeamMapPanel();
		teamSelectionPanel = new OpeningTeamSelectionPanel();
		policyDetailPanel = new OpeningPolicyDetailPanel(guiInterface);

		randomPoliciesButton.setFocusPainted(false);
		ButtonStyleUtil.styleToggleButton(randomPoliciesButton);
		ButtonStyleUtil.setToggleButtonSelected(randomPoliciesButton, true);
		randomPoliciesButton.setIcon(createRandomIcon());
		randomPoliciesButton.setText("");
		randomPoliciesButton.setPreferredSize(new Dimension(44, 44));
		continueButton.setFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.BOLD, 16));
		continueButton.setPreferredSize(new Dimension(170, 56));
		continueButton.setBorder(BorderFactory.createEmptyBorder(14, 28, 14, 28));
		continueButton.setBackground(new Color(0x17, 0x31, 0x74));
		continueButton.setForeground(Color.WHITE);
		themeButton.setFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.BOLD, 14));
		themeButton.setPreferredSize(new Dimension(150, 44));
		applyTheme();
	}

	private ImageIcon createRandomIcon() {
		ImageIcon icon = new ImageIcon("resources/randomIcon.png");
		Image scaledImage = icon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
		return new ImageIcon(scaledImage);
	}

	private void organize() {
		setLayout(new BorderLayout());
		setBackground(IDEAL_DASHBOARD_BACKGROUND_COLOR);

		JPanel content = buildContentPanel();
		content.add(buildHeader(), BorderLayout.NORTH);
		content.add(buildBody(), BorderLayout.CENTER);
		content.add(buildFooter(), BorderLayout.SOUTH);
		add(content, BorderLayout.CENTER);
	}

	private JPanel buildContentPanel() {
		return DashboardPanelUtil.createContentPanel(IDEAL_DASHBOARD_SPACING);
	}

	private JPanel buildHeader() {
		JPanel header = new DashboardTitleBanner(
				"Creation de la ligue",
				"Definissez les politiques financieres des equipes");
		header.setPreferredSize(new Dimension(360, IDEAL_DASHBOARD_HEADER_HEIGHT));
		return header;
	}

	private JPanel buildBody() {
		JPanel body = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING));
		body.setOpaque(false);
		body.add(buildCenterColumn(), BorderLayout.CENTER);
		body.add(buildRightColumn(), BorderLayout.EAST);
		return body;
	}

	private JPanel buildCenterColumn() {
		JPanel mapContent = new JPanel(new BorderLayout(0, 8));
		mapContent.setOpaque(false);

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		topPanel.setOpaque(false);
		topPanel.setBorder(BorderFactory.createEmptyBorder(-8, 0, 0, 0));
		topPanel.add(randomPoliciesButton);

		mapContent.add(topPanel, BorderLayout.NORTH);
		mapContent.add(openingMapPanel, BorderLayout.CENTER);

		return new BuildBox(
				"LOCALISATION DES FRANCHISES",
				"Cliquez sur une ville",
				mapContent);
	}

	private JPanel buildRightColumn() {
		JPanel column = DashboardPanelUtil.createRightColumn(IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH, 12);

		JPanel topCard = new BuildBox(
				"EQUIPE SELECTIONNEE",
				"Equipe courante",
				teamSelectionPanel);
		topCard.setPreferredSize(new Dimension(IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH, IDEAL_DASHBOARD_TOP_CARD_HEIGHT));

		JPanel bottomCard = new BuildBox(
				"INFORMATIONS EQUIPE",
				"Informations generales",
				policyDetailPanel);

		column.add(topCard, BorderLayout.NORTH);
		column.add(bottomCard, BorderLayout.CENTER);

		return column;
	}

	private JPanel buildFooter() {
		JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		footer.setOpaque(false);

		footer.add(themeButton);
		footer.add(continueButton);

		return footer;
	}

	private void actions() {
		openingMapPanel.setTeamSelectionAction(new MapSelectionAction());
		randomPoliciesButton.addActionListener(new RandomPoliciesListener());
		teamSelectionPanel.getAmbitiousButton().addActionListener(new AmbitiousPolicyListener());
		teamSelectionPanel.getBalancedButton().addActionListener(new BalancedPolicyListener());
		teamSelectionPanel.getThriftyButton().addActionListener(new ThriftyPolicyListener());
		teamSelectionPanel.getLargeMarketButton().addActionListener(new LargeMarketSizeListener());
		teamSelectionPanel.getMediumMarketButton().addActionListener(new MediumMarketSizeListener());
		teamSelectionPanel.getSmallMarketButton().addActionListener(new SmallMarketSizeListener());
	}

	private void selectDefaultTeam() {
		if (teams.isEmpty()) {
			setSelectedTeam(null);
			return;
		}
		setSelectedTeam(teams.get(0));
	}

	private void setSelectedTeam(Team selectedTeam) {
		this.selectedTeam = selectedTeam;
		refreshSelectedTeamPanels();
	}

	private void refreshSelectedTeamPanels() {
		teamSelectionPanel.updateTeam(selectedTeam);
		if (selectedTeam != null) {
			teamSelectionPanel.setSelectedPolicy(selectedTeam.getTeamFinance().getFinancialProfil());
			teamSelectionPanel.setSelectedMarketSize(selectedTeam.getTeamFinance().getMarketSize());
		}
		policyDetailPanel.updateTeam(selectedTeam);
		if (selectedTeam == null) {
			openingMapPanel.setSelectedTeamName(null);
			return;
		}
		openingMapPanel.setSelectedTeamName(selectedTeam.getName());
	}

	private class MapSelectionAction implements Runnable {
		@Override
		public void run() {
			setSelectedTeam(guiInterface.getTeamByName(openingMapPanel.getSelectedTeamName()));
		}
	}

	private void applySelectedPolicy(String policyType) {
		if (selectedTeam == null) {
			return;
		}

		if (policyType.equals("ambitious")) {
			guiInterface.chooseAmbitiousPolicy(selectedTeam);
		} else if (policyType.equals("balanced")) {
			guiInterface.chooseBalancedPolicy(selectedTeam);
		} else if (policyType.equals("thrifty")) {
			guiInterface.chooseThriftyPolicy(selectedTeam);
		}

		refreshSelectedTeamPanels();
	}

	private void applySelectedMarketSize(String marketSizeType) {
		if (selectedTeam == null) {
			return;
		}

		if (marketSizeType.equals("large")) {
			guiInterface.chooseLargeMarketSize(selectedTeam);
		} else if (marketSizeType.equals("medium")) {
			guiInterface.chooseMediumMarketSize(selectedTeam);
		} else if (marketSizeType.equals("small")) {
			guiInterface.chooseSmallMarketSize(selectedTeam);
		}

		refreshSelectedTeamPanels();
	}

	private class AmbitiousPolicyListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			applySelectedPolicy("ambitious");
		}
	}

	private class BalancedPolicyListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			applySelectedPolicy("balanced");
		}
	}

	private class ThriftyPolicyListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			applySelectedPolicy("thrifty");

		}
	}

	private class LargeMarketSizeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			applySelectedMarketSize("large");
		}
	}

	private class MediumMarketSizeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			applySelectedMarketSize("medium");
		}
	}

	private class SmallMarketSizeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			applySelectedMarketSize("small");
		}
	}

	private class RandomPoliciesListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			guiInterface.randomFinance();
			refreshSelectedTeamPanels();
		}
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
		themeButton.setText(DashboardPanelUtil.isDarkMode() ? "Mode clair" : "Mode sombre");
		themeButton.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
		themeButton.setForeground(DashboardPanelUtil.BUTTON_TEXT_COLOR);
		DashboardPanelUtil.refreshChildrenTheme(this);
	}

	public boolean hasSelectedProfil() {
		return selectedTeam != null;
	}

	public void showSelectionWarning() {
		JOptionPane.showMessageDialog(
				this,
				"Selectionnez une equipe sur la carte avant de continuer.",
				"Selection requise",
				JOptionPane.WARNING_MESSAGE);
	}
}
