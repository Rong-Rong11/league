package gui.panel.openningPanel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.team.Team;
import gui.panel.common.DashboardPanelUtil;
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
		JLabel valueLabel = new JLabel("-");
		valueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		valueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		return valueLabel;
	}

	private JPanel createRow(String title, JLabel valueLabel) {
		JPanel row = new JPanel(new GridLayout(2, 1, 0, 2));
		row.setOpaque(false);
		row.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, DashboardPanelUtil.BORDER_COLOR),
				BorderFactory.createEmptyBorder(0, 12, 0, 12)));

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
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
		teamValueLabel.setText("-");
		cityValueLabel.setText("-");
		conferenceValueLabel.setText("-");
		divisionValueLabel.setText("-");
	}

	private void showTeamState(Team team) {
		teamValueLabel.setText(TeamDisplayUtility.getShortName(team));
		cityValueLabel.setText(TeamDisplayUtility.getCityName(team));
		conferenceValueLabel.setText(TeamDisplayUtility.getConferenceLabel(guiInterface.getConferenceName(team)));
		divisionValueLabel.setText(guiInterface.getDivisionName(team));
	}

	@Override
	public void applyTheme() {
		teamValueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		cityValueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		conferenceValueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		divisionValueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		if (titleLabels != null) {
			for (int i = 0; i < titleLabels.length; i++) {
				if (titleLabels[i] != null) {
					titleLabels[i].setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
				}
			}
		}
	}

}
