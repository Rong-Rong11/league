package gui.panel.calendarPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import data.calendar.GameDay;
import data.sport.setup.Game;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.RoundedPanel;
import gui.panel.common.ThemeAware;
import process.orchestrator.GUIInterface;

public class WeekScheduleGridPanel extends RoundedPanel implements ThemeAware {
	private static final long serialVersionUID = 1L;
	private static final Color GRID_BORDER_COLOR = new Color(220, 224, 230);
	private static final Color TITLE_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color CURRENT_DAY_BACKGROUND = new Color(0xE8, 0xF2, 0xFF);
	private static final Color CURRENT_DAY_BORDER = new Color(0x17, 0x31, 0x74);
	private static final Font TEXT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 11);
	private static final Font SLOT_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);
	private static final Font DAY_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 16);
	private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("EEE d/M", Locale.FRENCH);
	private static final String[] SLOT_KEYS = { "AFTERNOON", "EVENING", "NIGHT" };
	private static final String[] SLOT_LABELS = { "Apres-midi", "Soiree", "Night" };

	private final GUIInterface guiInterface;
	private final LocalDate weekStart;
	private final LocalDate indicatorDay;
	private final OpenDayAction openDayAction;
	private final DaySimulationAction daySimulationAction;
	private final WeekScheduleCardFactory cardFactory = new WeekScheduleCardFactory();

	public WeekScheduleGridPanel(GUIInterface guiInterface, LocalDate weekStart, LocalDate indicatorDay,
			OpenDayAction openDayAction, DaySimulationAction daySimulationAction) {
		super(18);
		this.guiInterface = guiInterface;
		this.weekStart = weekStart;
		this.indicatorDay = indicatorDay;
		this.openDayAction = openDayAction;
		this.daySimulationAction = daySimulationAction;
		setLayout(new BorderLayout());
		setBackground(getCellBackground());
		setBorder(BorderFactory.createLineBorder(getGridBorderColor(), 1));
		add(buildGrid(), BorderLayout.CENTER);
	}

	private JPanel buildGrid() {
		JPanel grid = new JPanel();
		grid.setOpaque(true);
		grid.setBackground(getGridBorderColor());
		grid.setLayout(new javax.swing.BoxLayout(grid, javax.swing.BoxLayout.Y_AXIS));
		grid.add(buildHeaderRow());
		for (int slotIndex = 0; slotIndex < SLOT_KEYS.length; slotIndex++) {
			grid.add(buildSlotRow(slotIndex));
		}
		return grid;
	}

	private JPanel buildHeaderRow() {
		JPanel row = new JPanel(new GridLayout(1, 8, 0, 0));
		row.setOpaque(false);
		row.add(buildTopLeftCell());
		for (int offset = 0; offset < 7; offset++) {
			row.add(buildDayHeaderCell(weekStart.plusDays(offset)));
		}
		return row;
	}

	private JPanel buildSlotRow(int slotIndex) {
		JPanel row = new JPanel(new GridLayout(1, 8, 0, 0));
		row.setOpaque(false);
		String slotKey = SLOT_KEYS[slotIndex];
		int rowHeight = buildSlotRowHeight(slotKey);
		row.add(buildSlotLabelCell(SLOT_LABELS[slotIndex], rowHeight));
		for (int offset = 0; offset < 7; offset++) {
			row.add(buildSlotCell(weekStart.plusDays(offset), slotKey, rowHeight));
		}
		return row;
	}

	private int buildSlotRowHeight(String slotKey) {
		int maxGames = 0;
		for (int offset = 0; offset < 7; offset++) {
			GameDay gameDay = guiInterface.getGameDay(weekStart.plusDays(offset));
			if (gameDay != null && !gameDay.isEmpty()) {
				maxGames = Math.max(maxGames, cardFactory.getGamesForSlot(gameDay, slotKey).size());
			}
		}
		if (maxGames == 0) {
			return 88;
		}
		return 28 + maxGames * 68;
	}

	private JPanel buildTopLeftCell() {
		JPanel cell = buildGridCell(getCellBackground(), 76);
		cell.add(new JLabel("Creneau", SwingConstants.CENTER), BorderLayout.CENTER);
		return cell;
	}

	private JPanel buildDayHeaderCell(LocalDate day) {
		boolean currentDay = day.equals(indicatorDay);
		JPanel cell = buildGridCell(currentDay ? getCurrentDayBackground() : getCellBackground(), 76);
		if (currentDay) {
			cell.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, getCurrentDayBorderColor()));
		}
		if (isOutsideSeason(day)) {
			return cell;
		}

		GameDay gameDay = guiInterface.getGameDay(day);
		boolean hasGames = gameDay != null && !gameDay.isEmpty();
		boolean displayed = hasGames && gameDay.isDisplayed();
		cell.add(buildDayTextPanel(day), BorderLayout.CENTER);
		cell.add(buildDayActionsPanel(gameDay, day, hasGames, displayed), BorderLayout.SOUTH);
		return cell;
	}

	private JPanel buildDayTextPanel(LocalDate day) {
		JLabel titleLabel = new JLabel(DAY_FORMATTER.format(day), SwingConstants.CENTER);
		titleLabel.setFont(DAY_FONT);
		titleLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		JPanel textPanel = new JPanel(new GridLayout(1, 1));
		textPanel.setOpaque(false);
		textPanel.add(titleLabel);
		return textPanel;
	}

	private JPanel buildDayActionsPanel(GameDay gameDay, LocalDate day, boolean hasGames, boolean displayed) {
		JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
		actionsPanel.setOpaque(false);
		if (!hasGames) {
			return actionsPanel;
		}

		actionsPanel.add(buildActionButton("Detail", 82, new DetailDayListener(gameDay, day)));

		if (!displayed) {
			actionsPanel.add(buildActionButton("Simuler", 86, new SimulateDayListener(day)));
		}
		return actionsPanel;
	}

	private JPanel buildSlotLabelCell(String text, int preferredHeight) {
		JPanel cell = buildGridCell(getCellBackground(), preferredHeight);
		JLabel label = new JLabel(text, SwingConstants.CENTER);
		label.setFont(SLOT_FONT);
		label.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		cell.add(label, BorderLayout.CENTER);
		return cell;
	}

	private JPanel buildSlotCell(LocalDate day, String slotKey, int preferredHeight) {
		JPanel cell = buildGridCell(getCellBackground(), preferredHeight);
		GameDay gameDay = guiInterface.getGameDay(day);
		if (isOutsideSeason(day) || gameDay == null || gameDay.isEmpty()) {
			return cell;
		}

		JPanel content = new JPanel();
		content.setOpaque(false);
		content.setLayout(new javax.swing.BoxLayout(content, javax.swing.BoxLayout.Y_AXIS));
		java.util.ArrayList<Game> slotGames = cardFactory.getGamesForSlot(gameDay, slotKey);
		for (Game game : slotGames) {
			content.add(cardFactory.buildMatchCard(game, slotKey));
			content.add(javax.swing.Box.createVerticalStrut(6));
		}
		if (!slotGames.isEmpty()) {
			content.remove(content.getComponentCount() - 1);
		}
		cell.add(content, BorderLayout.CENTER);
		return cell;
	}

	private JPanel buildGridCell(Color background, int preferredHeight) {
		JPanel cell = new JPanel(new BorderLayout());
		cell.setOpaque(true);
		cell.setBackground(background);
		cell.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, getGridBorderColor()));
		cell.setPreferredSize(new Dimension(10, preferredHeight));
		return cell;
	}

	private JButton buildActionButton(String text, int width, ActionListener actionListener) {
		JButton button = new RoundedButton(text);
		button.setFont(TEXT_FONT);
		button.setPreferredSize(new Dimension(width, 28));
		button.addActionListener(actionListener);
		return button;
	}

	private boolean isOutsideSeason(LocalDate day) {
		return day.isBefore(guiInterface.getRegularSeasonStartDate()) || day.isAfter(guiInterface.getRegularSeasonEndDate());
	}

	private Color getGridBorderColor() {
		return DashboardPanelUtil.isDarkMode() ? new Color(58, 63, 72) : GRID_BORDER_COLOR;
	}

	private Color getCellBackground() {
		return DashboardPanelUtil.isDarkMode() ? new Color(34, 37, 43) : Color.WHITE;
	}

	private Color getCurrentDayBackground() {
		return DashboardPanelUtil.isDarkMode() ? new Color(48, 54, 66) : CURRENT_DAY_BACKGROUND;
	}

	private Color getCurrentDayBorderColor() {
		return DashboardPanelUtil.isDarkMode() ? Color.WHITE : CURRENT_DAY_BORDER;
	}

	public interface OpenDayAction {
		void open(GameDay gameDay, LocalDate day);
	}

	public interface DaySimulationAction {
		void simulate(LocalDate day);
	}

	private class DetailDayListener implements ActionListener {
		private final GameDay gameDay;
		private final LocalDate day;

		private DetailDayListener(GameDay gameDay, LocalDate day) {
			this.gameDay = gameDay;
			this.day = day;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			openDayAction.open(gameDay, day);
		}
	}

	private class SimulateDayListener implements ActionListener {
		private final LocalDate day;

		private SimulateDayListener(LocalDate day) {
			this.day = day;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			daySimulationAction.simulate(day);
		}
	}

	@Override
	public void applyTheme() {
		setBackground(getCellBackground());
	}
}
