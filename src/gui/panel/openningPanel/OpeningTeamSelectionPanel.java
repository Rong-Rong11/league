package gui.panel.openningPanel;

import data.team.Team;
import data.team.finance.financialpolicy.AmbitiousPolicy;
import data.team.finance.financialpolicy.BalancedPolicy;
import data.team.finance.financialpolicy.FinancialPolicy;
import data.team.finance.financialpolicy.ThriftyPolicy;
import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;
import gui.panel.common.ButtonStyleUtil;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.ThemeAware;
import gui.panel.mapPanel.effectifPanel.teamPanel.TeamLogoPanel;
import process.utility.TeamDisplayUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OpeningTeamSelectionPanel extends JPanel implements ThemeAware {

	private static final Color HEADER_BACKGROUND = new Color(0x17, 0x31, 0x74);
	private static final Color TITLE_COLOR = new Color(110, 117, 131);
	private static final Dimension SELECTION_BUTTON_SIZE = new Dimension(156, 56);

	private TeamLogoPanel logoPanel;
	private JLabel cityLabel;
	private JLabel teamLabel;
	private JButton ambitiousButton;
	private JButton balancedButton;
	private JButton thriftyButton;
	private JButton largeMarketButton;
	private JButton mediumMarketButton;
	private JButton smallMarketButton;
	private FinancialPolicy selectedPolicy;
	private MarketSize selectedMarketSize;
	private JLabel[] sectionTitleLabels;

	public OpeningTeamSelectionPanel() {
		create();
		organize();
		updateTeam(null);
	}

	private void create() {
		logoPanel = new TeamLogoPanel("", 48);
		cityLabel = new JLabel("-");
		teamLabel = new JLabel("-");
		ambitiousButton = new RoundedButton("Ambitieux");
		balancedButton = new RoundedButton("Equilibre");
		thriftyButton = new RoundedButton("Economique");
		largeMarketButton = new RoundedButton("Grand");
		mediumMarketButton = new RoundedButton("Moyen");
		smallMarketButton = new RoundedButton("Petit");

		cityLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		cityLabel.setForeground(Color.WHITE);
		teamLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		teamLabel.setForeground(Color.WHITE);

		ButtonStyleUtil.styleToggleButton(ambitiousButton);
		ButtonStyleUtil.styleToggleButton(balancedButton);
		ButtonStyleUtil.styleToggleButton(thriftyButton);
		ButtonStyleUtil.styleToggleButton(largeMarketButton);
		ButtonStyleUtil.styleToggleButton(mediumMarketButton);
		ButtonStyleUtil.styleToggleButton(smallMarketButton);
		enlargeSelectionButtons();
		applyTheme();
	}

	private void enlargeSelectionButtons() {
		applySelectionButtonSize(ambitiousButton);
		applySelectionButtonSize(balancedButton);
		applySelectionButtonSize(thriftyButton);
		applySelectionButtonSize(largeMarketButton);
		applySelectionButtonSize(mediumMarketButton);
		applySelectionButtonSize(smallMarketButton);
	}

	private void applySelectionButtonSize(JButton button) {
		button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		button.setPreferredSize(SELECTION_BUTTON_SIZE);
		button.setMinimumSize(SELECTION_BUTTON_SIZE);
		button.setBorder(BorderFactory.createEmptyBorder(12, 22, 12, 22));
	}

	private void organize() {
		setLayout(new BorderLayout(0, 12));
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

		JPanel headerPanel = new JPanel(new BorderLayout(12, 0));
		headerPanel.setOpaque(true);
		headerPanel.setBackground(HEADER_BACKGROUND);
		headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		headerPanel.setLayout(new BorderLayout(12, 0));
		headerPanel.add(logoPanel, BorderLayout.WEST);

		JPanel namePanel = new JPanel(new GridLayout(2, 1, 0, 2));
		namePanel.setOpaque(false);
		namePanel.add(cityLabel);
		namePanel.add(teamLabel);
		headerPanel.add(namePanel, BorderLayout.CENTER);

		JPanel contentPanel = new JPanel();
		contentPanel.setOpaque(false);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		JPanel policyPanel = buildButtonSection(
				"POLITIQUE FINANCIERE",
				ambitiousButton,
				balancedButton,
				thriftyButton);
		JPanel marketPanel = buildButtonSection(
				"TAILLE DU MARCHE",
				largeMarketButton,
				mediumMarketButton,
				smallMarketButton);

		contentPanel.add(policyPanel);
		contentPanel.add(Box.createVerticalStrut(8));
		contentPanel.add(marketPanel);

		add(headerPanel, BorderLayout.NORTH);
		add(contentPanel, BorderLayout.CENTER);
	}

	private JPanel buildButtonSection(String title, JButton firstButton, JButton secondButton, JButton thirdButton) {
		JPanel sectionPanel = new JPanel(new BorderLayout(0, 6));
		sectionPanel.setOpaque(false);

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
		storeSectionTitleLabel(titleLabel);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 12, 0));
		buttonPanel.setOpaque(false);
		buttonPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 56));
		buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
		buttonPanel.add(firstButton);
		buttonPanel.add(secondButton);
		buttonPanel.add(thirdButton);

		sectionPanel.add(titleLabel, BorderLayout.NORTH);
		sectionPanel.add(buttonPanel, BorderLayout.CENTER);
		return sectionPanel;
	}

	private void storeSectionTitleLabel(JLabel titleLabel) {
		if (sectionTitleLabels == null) {
			sectionTitleLabels = new JLabel[2];
		}
		for (int i = 0; i < sectionTitleLabels.length; i++) {
			if (sectionTitleLabels[i] == null) {
				sectionTitleLabels[i] = titleLabel;
				return;
			}
		}
	}

	public void updateTeam(Team team) {
		if (team == null) {
			showEmptyState();
			return;
		}
		showTeamState(team);
	}

	private void showEmptyState() {
		logoPanel.setTeamName("");
		cityLabel.setText("Aucune");
		teamLabel.setText("selection");
		setButtonsEnabled(false);
		selectedPolicy = null;
		selectedMarketSize = null;
		refreshPolicyButtons();
		refreshMarketSizeButtons();
	}

	private void showTeamState(Team team) {
		logoPanel.setTeamName(team.getName());
		cityLabel.setText(TeamDisplayUtil.getCityName(team));
		teamLabel.setText(TeamDisplayUtil.getShortName(team));
		setButtonsEnabled(true);
		selectedPolicy = team.getTeamFinance().getFinancialProfil();
		selectedMarketSize = team.getTeamFinance().getMarketSize();
		refreshPolicyButtons();
		refreshMarketSizeButtons();
	}

	public void setSelectedPolicy(FinancialPolicy selectedPolicy) {
		this.selectedPolicy = selectedPolicy;
		refreshPolicyButtons();
	}

	public void setSelectedMarketSize(MarketSize selectedMarketSize) {
		this.selectedMarketSize = selectedMarketSize;
		refreshMarketSizeButtons();
	}

	private void setButtonsEnabled(boolean enabled) {
		ambitiousButton.setEnabled(enabled);
		balancedButton.setEnabled(enabled);
		thriftyButton.setEnabled(enabled);
		largeMarketButton.setEnabled(enabled);
		mediumMarketButton.setEnabled(enabled);
		smallMarketButton.setEnabled(enabled);
	}

	private void refreshPolicyButtons() {
		ButtonStyleUtil.setToggleButtonSelected(ambitiousButton, selectedPolicy instanceof AmbitiousPolicy);
		ButtonStyleUtil.setToggleButtonSelected(balancedButton, selectedPolicy instanceof BalancedPolicy);
		ButtonStyleUtil.setToggleButtonSelected(thriftyButton, selectedPolicy instanceof ThriftyPolicy);
	}

	private void refreshMarketSizeButtons() {
		ButtonStyleUtil.setToggleButtonSelected(largeMarketButton, selectedMarketSize instanceof LargeSize);
		ButtonStyleUtil.setToggleButtonSelected(mediumMarketButton, selectedMarketSize instanceof MediumSize);
		ButtonStyleUtil.setToggleButtonSelected(smallMarketButton, selectedMarketSize instanceof SmallSize);
	}

	public JButton getAmbitiousButton() {
		return ambitiousButton;
	}

	public JButton getBalancedButton() {
		return balancedButton;
	}

	public JButton getThriftyButton() {
		return thriftyButton;
	}

	public JButton getLargeMarketButton() {
		return largeMarketButton;
	}

	public JButton getMediumMarketButton() {
		return mediumMarketButton;
	}

	public JButton getSmallMarketButton() {
		return smallMarketButton;
	}

	@Override
	public void applyTheme() {
		if (sectionTitleLabels != null) {
			for (int i = 0; i < sectionTitleLabels.length; i++) {
				if (sectionTitleLabels[i] != null) {
					sectionTitleLabels[i].setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
				}
			}
		}
		refreshPolicyButtons();
		refreshMarketSizeButtons();
	}
}
