package gui.panel.openningPanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.team.Team;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.LabelStyleUtil;
import gui.panel.common.ThemeAware;
import process.orchestrator.interf.GUIInterface;
import gui.utility.TeamDisplayUtility;

public class OpeningPolicyDetailPanel extends JPanel implements ThemeAware {
	private final GUIInterface guiInterface;

	private JLabel teamValueLabel;
	private JLabel cityValueLabel;
	private JLabel conferenceValueLabel;
	private JLabel divisionValueLabel;
	private JLabel[] titleLabels;

	public OpeningPolicyDetailPanel(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		setLayout(new BorderLayout());
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		JPanel infoPanel = new JPanel(new GridLayout(4, 1, 0, 0));
		infoPanel.setOpaque(false);

		teamValueLabel = createValueLabel();
		cityValueLabel = createValueLabel();
		conferenceValueLabel = createValueLabel();
		divisionValueLabel = createValueLabel();

		infoPanel.add(createRow("Equipe", teamValueLabel));
		infoPanel.add(createRow("Ville", cityValueLabel));
		infoPanel.add(createRow("Conference", conferenceValueLabel));
		infoPanel.add(createRow("Division", divisionValueLabel));

		add(infoPanel, BorderLayout.CENTER);

		updateTeam(null);
		applyTheme();
	}

	private JLabel createValueLabel() {
		JLabel valueLabel = new JLabel("Aucune information n'est disponible.");
		LabelStyleUtil.styleValueLabel(valueLabel, 16);
		return valueLabel;
	}

	private JPanel createRow(String title, JLabel valueLabel) {
		JPanel row = new JPanel(new GridLayout(2, 1, 0, 2));
		row.setOpaque(false);
		row.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, DashboardPanelUtil.BORDER_COLOR),
				BorderFactory.createEmptyBorder(0, 12, 0, 12)));

		JLabel titleLabel = new JLabel(title);
		LabelStyleUtil.styleSubtitleLabel(titleLabel, 12);
		storeTitleLabel(titleLabel);

		row.add(titleLabel);
		row.add(valueLabel);
		return row;
	}

	private void storeTitleLabel(JLabel titleLabel) {
		if (titleLabels == null) {
			titleLabels = new JLabel[4];
		}
		for (int i = 0; i < titleLabels.length; i++) {
			if (titleLabels[i] == null) {
				titleLabels[i] = titleLabel;
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
		teamValueLabel.setText("Aucune equipe n'est selectionnee.");
		cityValueLabel.setText("Selectionnez une franchise sur la carte.");
		conferenceValueLabel.setText("La conference apparaitra apres la selection.");
		divisionValueLabel.setText("La division apparaitra apres la selection.");
		LabelStyleUtil.styleSubtitleLabel(teamValueLabel, 12);
		LabelStyleUtil.styleSubtitleLabel(cityValueLabel, 12);
		LabelStyleUtil.styleSubtitleLabel(conferenceValueLabel, 12);
		LabelStyleUtil.styleSubtitleLabel(divisionValueLabel, 12);
	}

	private void showTeamState(Team team) {
		teamValueLabel.setText(TeamDisplayUtility.getShortName(team));
		cityValueLabel.setText(TeamDisplayUtility.getCityName(team));
		conferenceValueLabel.setText(TeamDisplayUtility.getConferenceLabel(guiInterface.getConferenceName(team)));
		divisionValueLabel.setText(guiInterface.getDivisionName(team));
	}

	@Override
	public void applyTheme() {
		if (teamValueLabel.getText().contains("Aucune") || cityValueLabel.getText().contains("Selectionnez")) {
			LabelStyleUtil.styleSubtitleLabel(teamValueLabel, 12);
			LabelStyleUtil.styleSubtitleLabel(cityValueLabel, 12);
			LabelStyleUtil.styleSubtitleLabel(conferenceValueLabel, 12);
			LabelStyleUtil.styleSubtitleLabel(divisionValueLabel, 12);
		} else {
			LabelStyleUtil.styleValueLabel(teamValueLabel, 16);
			LabelStyleUtil.styleValueLabel(cityValueLabel, 16);
			LabelStyleUtil.styleValueLabel(conferenceValueLabel, 16);
			LabelStyleUtil.styleValueLabel(divisionValueLabel, 16);
		}
		if (titleLabels != null) {
			for (int i = 0; i < titleLabels.length; i++) {
				if (titleLabels[i] != null) {
					LabelStyleUtil.styleSubtitleLabel(titleLabels[i], 12);
				}
			}
		}
	}

}
