package gui.panel.rankingPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RankingTablePanel extends JPanel {
	private static final Color HEADER_BACKGROUND = new Color(245, 247, 250);
	private static final Color HEADER_TEXT_COLOR = new Color(110, 117, 131);
	private static final Color PRIMARY_TEXT_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color PRIMARY_ACCENT = new Color(0x37, 0x84, 0xB3);
	private static final Color MUTED_TEXT_COLOR = new Color(90, 90, 90);
	private static final Color BORDER_COLOR = new Color(229, 232, 238);
	private static final Color WIN_BACKGROUND = new Color(232, 244, 251);
	private static final Color LOSS_BACKGROUND = new Color(252, 236, 236);

	public RankingTablePanel() {
		setLayout(new BorderLayout(0, 12));
		setOpaque(false);

		add(buildTopBar(), BorderLayout.NORTH);
		add(buildTableContent(), BorderLayout.CENTER);
	}

	private JPanel buildTopBar() {
		JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		topBar.setOpaque(false);
		topBar.add(createFilterButton("Saison reguliere", true));
		topBar.add(createFilterButton("Playoffs", false));
		topBar.add(createFilterButton("Saison precedente", false));
		return topBar;
	}

	private JButton createFilterButton(String text, boolean selected) {
		JButton button = new JButton(text);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setOpaque(true);
		button.setFont(new Font(Font.SANS_SERIF, selected ? Font.BOLD : Font.PLAIN, 12));
		button.setBackground(selected ? PRIMARY_ACCENT : HEADER_BACKGROUND);
		button.setForeground(selected ? Color.WHITE : MUTED_TEXT_COLOR);
		return button;
	}

	private JPanel buildTableContent() {
		JPanel content = new JPanel(new BorderLayout(0, 0));
		content.setOpaque(false);
		content.add(buildHeaderRow(), BorderLayout.NORTH);
		content.add(buildRowsPanel(), BorderLayout.CENTER);
		return content;
	}

	private JPanel buildHeaderRow() {
		JPanel header = new JPanel(new GridLayout(1, 7, 12, 0));
		header.setOpaque(true);
		header.setBackground(HEADER_BACKGROUND);
		header.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 1, 0, BORDER_COLOR),
				BorderFactory.createEmptyBorder(10, 16, 10, 16)));

		header.add(createHeaderLabel("RANG"));
		header.add(createHeaderLabel("EQUIPE"));
		header.add(createHeaderLabel("V"));
		header.add(createHeaderLabel("D"));
		header.add(createHeaderLabel("POINTS"));
		header.add(createHeaderLabel("% VICT"));
		header.add(createHeaderLabel("FORME"));
		return header;
	}

	private JLabel createHeaderLabel(String text) {
		JLabel label = new JLabel(text);
		label.setForeground(HEADER_TEXT_COLOR);
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		return label;
	}

	private JPanel buildRowsPanel() {
		JPanel rowsPanel = new JPanel();
		rowsPanel.setOpaque(false);
		rowsPanel.setLayout(new BoxLayout(rowsPanel, BoxLayout.Y_AXIS));

		rowsPanel.add(createRow(1, "Celtics", 45, 12, 90, "78.9%", new String[] { "W", "W", "W", "L", "W" }));
		rowsPanel.add(createRow(2, "Bucks", 42, 15, 84, "73.7%", new String[] { "W", "W", "L", "W", "W" }));
		rowsPanel.add(createRow(3, "Heat", 39, 18, 78, "68.4%", new String[] { "L", "W", "W", "W", "L" }));
		rowsPanel.add(createRow(4, "Cavaliers", 38, 19, 76, "66.7%", new String[] { "W", "L", "W", "W", "W" }));
		rowsPanel.add(createRow(5, "76ers", 36, 21, 72, "63.2%", new String[] { "L", "L", "W", "W", "L" }));
		rowsPanel.add(createRow(6, "Knicks", 34, 23, 68, "59.6%", new String[] { "W", "L", "L", "W", "L" }));
		rowsPanel.add(createRow(7, "Hawks", 32, 25, 64, "56.1%", new String[] { "L", "W", "W", "L", "W" }));
		rowsPanel.add(createRow(8, "Bulls", 31, 26, 62, "54.4%", new String[] { "W", "W", "L", "L", "W" }));
		rowsPanel.add(createRow(9, "Raptors", 28, 29, 56, "49.1%", new String[] { "L", "L", "W", "L", "L" }));
		rowsPanel.add(createRow(10, "Wizards", 25, 32, 50, "43.9%", new String[] { "L", "W", "L", "L", "L" }));
		rowsPanel.add(createRow(11, "Hornets", 22, 35, 44, "38.6%", new String[] { "L", "L", "L", "W", "L" }));
		rowsPanel.add(createRow(12, "Pistons", 18, 39, 36, "31.6%", new String[] { "L", "L", "L", "L", "W" }));

		return rowsPanel;
	}

	private JPanel createRow(int rank, String teamName, int wins, int losses, int points, String percentage,
			String[] form) {
		JPanel row = new JPanel(new GridLayout(1, 7, 12, 0));
		row.setOpaque(true);
		row.setBackground(Color.WHITE);
		row.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
				BorderFactory.createEmptyBorder(12, 16, 12, 16)));

		row.add(createValueLabel(String.valueOf(rank), true));
		row.add(createValueLabel(teamName, true));
		row.add(createValueLabel(String.valueOf(wins), false));
		row.add(createValueLabel(String.valueOf(losses), false));
		row.add(createValueLabel(String.valueOf(points), true));
		row.add(createValueLabel(percentage, true));
		row.add(buildFormPanel(form));

		return row;
	}

	private JLabel createValueLabel(String text, boolean accented) {
		JLabel label = new JLabel(text);
		label.setForeground(accented ? PRIMARY_TEXT_COLOR : MUTED_TEXT_COLOR);
		label.setFont(new Font(Font.SANS_SERIF, accented ? Font.BOLD : Font.PLAIN, 13));
		return label;
	}

	private JPanel buildFormPanel(String[] form) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
		panel.setOpaque(false);
		for (String result : form) {
			panel.add(createFormBadge(result));
		}
		return panel;
	}

	private JPanel createFormBadge(String result) {
		JPanel badge = new JPanel(new BorderLayout());
		badge.setOpaque(true);
		badge.setBackground("W".equals(result) ? WIN_BACKGROUND : LOSS_BACKGROUND);
		badge.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
		badge.setPreferredSize(new Dimension(26, 22));

		JLabel label = new JLabel(result, JLabel.CENTER);
		label.setForeground("W".equals(result) ? PRIMARY_ACCENT : new Color(0xD0, 0x55, 0x55));
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
		badge.add(label, BorderLayout.CENTER);
		return badge;
	}
}
