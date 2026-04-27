package gui.panel.mapPanel.effectifPanel;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.team.Team;
import gui.panel.common.ButtonStyleUtil;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.LabelStyleUtil;
import gui.panel.common.PlayerDisplayUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.ThemeAware;
import gui.panel.mapPanel.effectifPanel.teamPanel.TeamLogoPanel;
import process.orchestrator.interfaces.GUIInterface;
import gui.utility.TeamDisplayUtility;

public class MapTeamSummaryPanel extends JPanel implements ThemeAware {

	private final GUIInterface guiInterface;
	private JLabel teamNameLabel;
	private JLabel cityLabel;
	private JLabel conferenceLabel;
	private JLabel divisionLabel;
	private JLabel arenaLabel;
	private JLabel budgetLabel;
	private JLabel capacityLabel;
	private JLabel averageNoteLabel;
	private JLabel financialPolicyLabel;
	private JLabel marketSizeLabel;
	private JButton openRosterButton;
	private TeamLogoPanel teamLogoPanel;
	private JLabel[] infoTitleLabels;

	public MapTeamSummaryPanel(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		create();
		organize();
		updateTeam(null, true);
		applyTheme();
	}

	private void create() {
		teamNameLabel = new JLabel();
		cityLabel = new JLabel();
		conferenceLabel = new JLabel();
		divisionLabel = new JLabel();
		arenaLabel = new JLabel();
		budgetLabel = new JLabel();
		capacityLabel = new JLabel();
		averageNoteLabel = new JLabel();
		financialPolicyLabel = new JLabel();
		marketSizeLabel = new JLabel();
		openRosterButton = new RoundedButton("Voir l'effectif complet");
		teamLogoPanel = new TeamLogoPanel("", 56);
		teamLogoPanel.setTeamQueryInterface(guiInterface);

		LabelStyleUtil.styleTitleLabel(teamNameLabel, 20);
		ButtonStyleUtil.styleActionButton(openRosterButton, 220, 44, 14);
		openRosterButton.setBackground(DashboardPanelUtil.getPrimaryActionColor());
		openRosterButton.setForeground(DashboardPanelUtil.getPrimaryActionTextColor());
	}

	private void organize() {
		setLayout(new BorderLayout(0, 12));
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(8, 10, 10, 10));

		JPanel headerPanel = new JPanel(new BorderLayout(12, 0));
		headerPanel.setOpaque(false);
		headerPanel.add(teamLogoPanel, BorderLayout.WEST);
		headerPanel.add(teamNameLabel, BorderLayout.CENTER);

		JPanel infoPanel = new JPanel();
		infoPanel.setOpaque(false);
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.add(buildInfoLabel("Equipe", teamNameLabel));
		infoPanel.add(Box.createVerticalStrut(8));
		infoPanel.add(buildInfoLabel("Ville", cityLabel));
		infoPanel.add(Box.createVerticalStrut(8));
		infoPanel.add(buildInfoLabel("Conference", conferenceLabel));
		infoPanel.add(Box.createVerticalStrut(8));
		infoPanel.add(buildInfoLabel("Division", divisionLabel));
		infoPanel.add(Box.createVerticalStrut(8));
		infoPanel.add(buildInfoLabel("Arene", arenaLabel));
		infoPanel.add(Box.createVerticalStrut(8));
		infoPanel.add(buildInfoLabel("Budget annuel", budgetLabel));
		infoPanel.add(Box.createVerticalStrut(8));
		infoPanel.add(buildInfoLabel("Capacite salle", capacityLabel));
		infoPanel.add(Box.createVerticalStrut(8));
		infoPanel.add(buildInfoLabel("Note moyenne", averageNoteLabel));
		infoPanel.add(Box.createVerticalStrut(8));
		infoPanel.add(buildInfoLabel("Politique financiere", financialPolicyLabel));
		infoPanel.add(Box.createVerticalStrut(8));
		infoPanel.add(buildInfoLabel("Taille du marche", marketSizeLabel));

		add(headerPanel, BorderLayout.NORTH);
		add(infoPanel, BorderLayout.CENTER);
		add(openRosterButton, BorderLayout.SOUTH);
		openRosterButton.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
	}

	private JPanel buildInfoLabel(String title, JLabel valueLabel) {
		JPanel row = new JPanel();
		row.setOpaque(false);
		row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));

		JLabel titleLabel = new JLabel(title);
		LabelStyleUtil.styleSubtitleLabel(titleLabel, 12);
		LabelStyleUtil.styleValueLabel(valueLabel, 18);
		storeInfoTitleLabel(titleLabel);

		row.add(titleLabel);
		row.add(Box.createVerticalStrut(2));
		row.add(valueLabel);
		row.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
		return row;
	}

	private void storeInfoTitleLabel(JLabel titleLabel) {
		if (infoTitleLabels == null) {
			infoTitleLabels = new JLabel[10];
		}
		for (int i = 0; i < infoTitleLabels.length; i++) {
			if (infoTitleLabels[i] == null) {
				infoTitleLabels[i] = titleLabel;
				return;
			}
		}
	}

	public void updateTeam(Team team, boolean currentSeasonSelected) {
		if (team == null) {
			showEmptyState();
			return;
		}
		showTeamState(team, currentSeasonSelected);
	}

	private void showEmptyState() {
		teamLogoPanel.setTeamName("");
		teamNameLabel.setText("Aucune equipe n'est selectionnee.");
		cityLabel.setText("Selectionnez une franchise sur la carte.");
		conferenceLabel.setText("La conference sera affichee apres la selection.");
		divisionLabel.setText("La division sera affichee apres la selection.");
		arenaLabel.setText("L'arene sera affichee apres la selection.");
		budgetLabel.setText("Le budget sera affiche apres la selection.");
		capacityLabel.setText("La capacite sera affichee apres la selection.");
		averageNoteLabel.setText("La note moyenne sera affichee apres la selection.");
		financialPolicyLabel.setText("La politique financiere sera affichee apres la selection.");
		marketSizeLabel.setText("La taille du marche sera affichee apres la selection.");
		openRosterButton.setEnabled(false);
		revalidate();
		repaint();
	}

	private void showTeamState(Team team, boolean currentSeasonSelected) {
		teamLogoPanel.setTeamName(team.getName());
		teamNameLabel.setText(team.getName());
		cityLabel.setText(TeamDisplayUtility.getCityName(team));
		conferenceLabel.setText(guiInterface.getConferenceName(team));
		divisionLabel.setText(guiInterface.getDivisionName(team));
		arenaLabel.setText(team.getStadium().getName());

		if (currentSeasonSelected) {
			budgetLabel.setText(PlayerDisplayUtil.formatSalary(team.getTeamFinance().getBudget().getRemainingAmount()));
			averageNoteLabel.setText(PlayerDisplayUtil.formatOneDecimal(team.getCurrentPopularity()) + "/100");
		} else {
			budgetLabel.setText(PlayerDisplayUtil.formatSalary(team.getTeamFinance().getBudget().getInitialAmount()));
			averageNoteLabel.setText(PlayerDisplayUtil.formatOneDecimal(
					team.getFormerPopularity())
					+ "/100");
		}

		capacityLabel.setText(String.valueOf(team.getStadium().getCapacity()));
		financialPolicyLabel.setText(guiInterface.getTeamFinancialPolicyLabel(team));
		marketSizeLabel.setText(guiInterface.getTeamMarketSizeLabel(team));
		openRosterButton.setEnabled(true);
		revalidate();
		repaint();
	}

	public JButton getOpenRosterButton() {
		return openRosterButton;
	}

	@Override
	public void applyTheme() {
		LabelStyleUtil.styleTitleLabel(teamNameLabel, 20);
		applyInfoValueStyle(cityLabel);
		applyInfoValueStyle(conferenceLabel);
		applyInfoValueStyle(divisionLabel);
		applyInfoValueStyle(arenaLabel);
		applyInfoValueStyle(budgetLabel);
		applyInfoValueStyle(capacityLabel);
		applyInfoValueStyle(averageNoteLabel);
		applyInfoValueStyle(financialPolicyLabel);
		applyInfoValueStyle(marketSizeLabel);
		openRosterButton.setBackground(DashboardPanelUtil.getPrimaryActionColor());
		openRosterButton.setForeground(DashboardPanelUtil.getPrimaryActionTextColor());
		if (infoTitleLabels != null) {
			for (int i = 0; i < infoTitleLabels.length; i++) {
				if (infoTitleLabels[i] != null) {
					LabelStyleUtil.styleSubtitleLabel(infoTitleLabels[i], 12);
				}
			}
		}
	}

	private void applyInfoValueStyle(JLabel label) {
		if (isPlaceholderText(label.getText())) {
			LabelStyleUtil.styleSubtitleLabel(label, 13);
			return;
		}
		LabelStyleUtil.styleValueLabel(label, 18);
	}

	private boolean isPlaceholderText(String text) {
		return text != null
				&& (text.startsWith("Aucune")
						|| text.startsWith("Selectionnez")
						|| text.startsWith("La ")
						|| text.startsWith("Le ")
						|| text.startsWith("L'"));
	}
}
