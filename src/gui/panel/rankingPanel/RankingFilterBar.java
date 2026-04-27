package gui.panel.rankingPanel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import gui.panel.common.ButtonStyleUtil;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.RoundedButton;

public class RankingFilterBar extends JPanel {
	private final JPanel modeFilterPanel;
	private final JButton globalButton;
	private final JButton eastButton;
	private final JButton westButton;
	private final JButton simulatePlayoffRoundButton;
	private final JButton regularSeasonButton;
	private final JButton playoffsButton;

	public RankingFilterBar() {
		super(new BorderLayout());
		setOpaque(false);

		modeFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		modeFilterPanel.setOpaque(false);
		globalButton = createFilterButton("Global", true);
		eastButton = createFilterButton("Est", false);
		westButton = createFilterButton("Ouest", false);
		simulatePlayoffRoundButton = new RoundedButton("Simuler le tour");
		modeFilterPanel.add(globalButton);
		modeFilterPanel.add(eastButton);
		modeFilterPanel.add(westButton);
		modeFilterPanel.add(simulatePlayoffRoundButton);

		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		rightPanel.setOpaque(false);
		regularSeasonButton = createFilterButton("S.R", true);
		playoffsButton = createFilterButton("Playoffs", false);
		rightPanel.add(regularSeasonButton);
		rightPanel.add(playoffsButton);

		add(modeFilterPanel, BorderLayout.WEST);
		add(rightPanel, BorderLayout.EAST);
	}

	private JButton createFilterButton(String text, boolean selected) {
		JButton button = new RoundedButton(text);
		ButtonStyleUtil.styleToggleButton(button);
		ButtonStyleUtil.setToggleButtonSelected(button, selected);
		return button;
	}

	public void registerModeActions(ActionListener globalAction, ActionListener eastAction, ActionListener westAction) {
		globalButton.addActionListener(globalAction);
		eastButton.addActionListener(eastAction);
		westButton.addActionListener(westAction);
	}

	public void registerSeasonActions(ActionListener regularAction, ActionListener playoffsAction,
			ActionListener simulateAction) {
		regularSeasonButton.addActionListener(regularAction);
		playoffsButton.addActionListener(playoffsAction);
		simulatePlayoffRoundButton.addActionListener(simulateAction);
	}

	public void updateModeSelection(String selectedMode) {
		styleFilterButton(globalButton, RankingTablePanel.GLOBAL_MODE.equals(selectedMode));
		styleFilterButton(eastButton, RankingTablePanel.EAST_MODE.equals(selectedMode));
		styleFilterButton(westButton, RankingTablePanel.WEST_MODE.equals(selectedMode));
	}

	public void updateSeasonSelection(String selectedSeason, boolean playoffsStarted, boolean playoffsFinished) {
		styleFilterButton(regularSeasonButton, RankingTablePanel.REGULAR_SEASON.equals(selectedSeason));
		styleFilterButton(playoffsButton, RankingTablePanel.PLAYOFFS.equals(selectedSeason));

		boolean playoffsSelected = RankingTablePanel.PLAYOFFS.equals(selectedSeason);
		globalButton.setVisible(!playoffsSelected);
		eastButton.setVisible(!playoffsSelected);
		westButton.setVisible(!playoffsSelected);
		simulatePlayoffRoundButton.setVisible(playoffsSelected);

		updatePlayoffRoundButton(playoffsStarted, playoffsFinished);
	}

	private void updatePlayoffRoundButton(boolean playoffsStarted, boolean playoffsFinished) {
		ButtonStyleUtil.styleActionButton(simulatePlayoffRoundButton, 190, 44, 15);
		boolean enabled = playoffsStarted && !playoffsFinished;
		simulatePlayoffRoundButton.setEnabled(enabled);
		simulatePlayoffRoundButton.setText(playoffsFinished ? "Playoffs termines" : "Simuler le tour");
		if (enabled) {
			simulatePlayoffRoundButton.setBackground(DashboardPanelUtil.getPrimaryActionColor());
			simulatePlayoffRoundButton.setForeground(DashboardPanelUtil.getPrimaryActionTextColor());
			return;
		}
		simulatePlayoffRoundButton.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
		simulatePlayoffRoundButton.setForeground(DashboardPanelUtil.BUTTON_TEXT_COLOR);
	}

	private void styleFilterButton(JButton button, boolean selected) {
		ButtonStyleUtil.styleToggleButton(button);
		ButtonStyleUtil.setToggleButtonSelected(button, selected);
	}
}
