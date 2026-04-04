package gui.panel.matchPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import data.finance.GameStat;
import data.finance.TeamGameFinance;
import data.sport.setup.Game;

public class MatchFinancePanel extends JPanel {
	private static final Color TITLE_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color SUBTITLE_COLOR = new Color(0x6D, 0x75, 0x83);
	private static final Color POSITIVE_COLOR = new Color(0x2F, 0x80, 0xA9);
	private static final Color NEGATIVE_COLOR = new Color(0xE0, 0x00, 0x00);
	private static final Color SEPARATOR_COLOR = new Color(225, 225, 225);

	private JLabel attendanceValueLabel;
	private JLabel attendanceInfoLabel;
	private JLabel ticketValueLabel;
	private JLabel merchandisingValueLabel;
	private JLabel concessionsValueLabel;
	private JLabel tvValueLabel;
	private JLabel travelValueLabel;
	private JLabel expenseValueLabel;
	private JLabel netValueLabel;

	public MatchFinancePanel() {
		super(new BorderLayout());
		setOpaque(false);

		JPanel content = new JPanel();
		content.setOpaque(false);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		content.add(buildAttendanceRow());
		content.add(buildMoneyRow("Billetterie", true));
		content.add(buildMoneyRow("Merchandising", true));
		content.add(buildMoneyRow("Concessions", true));
		content.add(buildMoneyRow("Droits TV", true));
		content.add(buildMoneyRow("Voyage equipe", false));
		content.add(buildMoneyRow("Charges match", false));

		add(content, BorderLayout.CENTER);
		add(buildNetPanel(), BorderLayout.SOUTH);

		showHiddenState();
	}

	public void showHiddenState() {
		attendanceValueLabel.setText("-");
		attendanceInfoLabel.setText("Capacite: -");
		setPositiveValue(ticketValueLabel, "-");
		setPositiveValue(merchandisingValueLabel, "-");
		setPositiveValue(concessionsValueLabel, "-");
		setPositiveValue(tvValueLabel, "-");
		setNegativeValue(travelValueLabel, "-");
		setNegativeValue(expenseValueLabel, "-");
		setPositiveValue(netValueLabel, "-");
	}

	public void showGameFinance(Game game, GameStat gameStat) {
		if (game == null || gameStat == null) {
			showHiddenState();
			return;
		}

		TeamGameFinance home = gameStat.getHomeFinance();
		TeamGameFinance away = gameStat.getAwayFinance();
		double homeRevenue = computeRevenue(home);
		double homeExpense = computeExpense(home);
		double awayRevenue = computeRevenue(away);
		double awayExpense = computeExpense(away);
		double totalExpense = homeExpense + awayExpense;
		double net = (homeRevenue + awayRevenue) - totalExpense;
		double travelCost = home.getTravelCosts() + away.getTravelCosts();
		int capacity = game.getGameContext().getHomeTeam().getStadium().getCapacity();
		int attendanceRate = capacity == 0 ? 0 : (int) Math.round((gameStat.getAttendees() * 100.0) / capacity);

		attendanceValueLabel.setText(String.valueOf(gameStat.getAttendees()));
		attendanceInfoLabel.setText("Capacite: " + attendanceRate + "%");
		setPositiveValue(ticketValueLabel, formatMoney(home.getTicketRevenue()));
		setPositiveValue(merchandisingValueLabel, formatMoney(home.getMerchRevenue()));
		setPositiveValue(concessionsValueLabel, formatMoney(home.getConcessionsRevenue()));
		setPositiveValue(tvValueLabel, formatMoney(home.getTvRevenue()));
		setNegativeValue(travelValueLabel, formatMoney(travelCost));
		setNegativeValue(expenseValueLabel, formatMoney(totalExpense));
		if (net >= 0) {
			setPositiveValue(netValueLabel, formatMoney(net));
		} else {
			setNegativeValue(netValueLabel, formatMoney(Math.abs(net)));
		}
	}

	private JPanel buildAttendanceRow() {
		JPanel row = new JPanel(new BorderLayout());
		row.setOpaque(false);
		row.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, SEPARATOR_COLOR),
				BorderFactory.createEmptyBorder(14, 10, 14, 10)));

		JLabel nameLabel = buildNameLabel("Spectateurs");
		JPanel rightPanel = new JPanel(new GridLayout(2, 1));
		rightPanel.setOpaque(false);

		attendanceValueLabel = new JLabel("-", SwingConstants.RIGHT);
		attendanceValueLabel.setForeground(TITLE_COLOR);
		attendanceValueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

		attendanceInfoLabel = new JLabel("Capacite: -", SwingConstants.RIGHT);
		attendanceInfoLabel.setForeground(SUBTITLE_COLOR);
		attendanceInfoLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

		rightPanel.add(attendanceValueLabel);
		rightPanel.add(attendanceInfoLabel);

		row.add(nameLabel, BorderLayout.WEST);
		row.add(rightPanel, BorderLayout.EAST);
		return row;
	}

	private JPanel buildMoneyRow(String name, boolean positive) {
		JPanel row = new JPanel(new BorderLayout());
		row.setOpaque(false);
		row.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, SEPARATOR_COLOR),
				BorderFactory.createEmptyBorder(16, 10, 16, 10)));

		JLabel nameLabel = buildNameLabel(name);
		JLabel valueLabel = new JLabel("-", SwingConstants.RIGHT);
		valueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));

		if (positive) {
			valueLabel.setForeground(POSITIVE_COLOR);
		} else {
			valueLabel.setForeground(NEGATIVE_COLOR);
		}

		if ("Billetterie".equals(name)) {
			ticketValueLabel = valueLabel;
		} else if ("Merchandising".equals(name)) {
			merchandisingValueLabel = valueLabel;
		} else if ("Concessions".equals(name)) {
			concessionsValueLabel = valueLabel;
		} else if ("Droits TV".equals(name)) {
			tvValueLabel = valueLabel;
		} else if ("Voyage equipe".equals(name)) {
			travelValueLabel = valueLabel;
		} else if ("Charges match".equals(name)) {
			expenseValueLabel = valueLabel;
		}

		row.add(nameLabel, BorderLayout.WEST);
		row.add(valueLabel, BorderLayout.EAST);
		return row;
	}

	private JPanel buildNetPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(2, 0, 0, 0, SEPARATOR_COLOR),
				BorderFactory.createEmptyBorder(18, 10, 18, 10)));

		JLabel label = new JLabel("RESULTAT NET");
		label.setForeground(TITLE_COLOR);
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));

		netValueLabel = new JLabel("-", SwingConstants.RIGHT);
		netValueLabel.setForeground(POSITIVE_COLOR);
		netValueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

		panel.add(label, BorderLayout.WEST);
		panel.add(netValueLabel, BorderLayout.EAST);
		return panel;
	}

	private JLabel buildNameLabel(String text) {
		JLabel label = new JLabel(text);
		label.setForeground(new Color(90, 90, 90));
		label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		return label;
	}

	private void setPositiveValue(JLabel label, String value) {
		label.setForeground(POSITIVE_COLOR);
		if ("-".equals(value)) {
			label.setText("-");
			return;
		}
		label.setText("+" + value);
	}

	private void setNegativeValue(JLabel label, String value) {
		label.setForeground(NEGATIVE_COLOR);
		if ("-".equals(value)) {
			label.setText("-");
			return;
		}
		label.setText("-" + value);
	}

	private double computeRevenue(TeamGameFinance teamGameFinance) {
		return teamGameFinance.getTicketRevenue()
				+ teamGameFinance.getConcessionsRevenue()
				+ teamGameFinance.getMerchRevenue()
				+ teamGameFinance.getTvRevenue()
				+ teamGameFinance.getParkingRevenue();
	}

	private double computeExpense(TeamGameFinance teamGameFinance) {
		return teamGameFinance.getArenaCosts()
				+ teamGameFinance.getStaffCosts()
				+ teamGameFinance.getSecurityCosts()
				+ teamGameFinance.getLogisticsCosts()
				+ teamGameFinance.getTravelCosts();
	}

	private String formatMoney(double amount) {
		return String.format("%.0fKEUR", amount * 1000);
	}
}
