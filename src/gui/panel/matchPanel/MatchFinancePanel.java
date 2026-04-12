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
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.ThemeAware;

public class MatchFinancePanel extends JPanel implements ThemeAware {
	private static final Color TITLE_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color NEGATIVE_COLOR = DashboardPanelUtil.ACCENT_RED_COLOR;

	private JLabel attendanceValueLabel;
	private JLabel attendanceInfoLabel;
	private JLabel ticketValueLabel;
	private JLabel merchandisingValueLabel;
	private JLabel concessionsValueLabel;
	private JLabel tvValueLabel;
	private JLabel travelValueLabel;
	private JLabel expenseValueLabel;
	private JLabel netValueLabel;
	private JLabel attendanceTitleLabel;
	private JLabel[] moneyNameLabels;
	private JLabel netTitleLabel;
	private JPanel attendanceRowPanel;
	private JPanel[] moneyRowPanels;
	private JPanel netPanel;

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
		applyTheme();
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
		attendanceRowPanel = new JPanel(new BorderLayout());
		JPanel row = attendanceRowPanel;
		row.setOpaque(false);
		applySectionBorder(row, 1, 14);

		attendanceTitleLabel = buildNameLabel("Spectateurs");
		JPanel rightPanel = new JPanel(new GridLayout(2, 1));
		rightPanel.setOpaque(false);

		attendanceValueLabel = new JLabel("-", SwingConstants.RIGHT);
		attendanceValueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

		attendanceInfoLabel = new JLabel("Capacite: -", SwingConstants.RIGHT);
		attendanceInfoLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

		rightPanel.add(attendanceValueLabel);
		rightPanel.add(attendanceInfoLabel);

		row.add(attendanceTitleLabel, BorderLayout.WEST);
		row.add(rightPanel, BorderLayout.EAST);
		return row;
	}

	private JPanel buildMoneyRow(String name, boolean positive) {
		JPanel row = new JPanel(new BorderLayout());
		row.setOpaque(false);
		applySectionBorder(row, 1, 16);

		JLabel nameLabel = buildNameLabel(name);
		JLabel valueLabel = new JLabel("-", SwingConstants.RIGHT);
		valueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));

		if ("Billetterie".equals(name)) {
			storeMoneyRowPanel(0, row);
			storeMoneyNameLabel(0, nameLabel);
			ticketValueLabel = valueLabel;
		} else if ("Merchandising".equals(name)) {
			storeMoneyRowPanel(1, row);
			storeMoneyNameLabel(1, nameLabel);
			merchandisingValueLabel = valueLabel;
		} else if ("Concessions".equals(name)) {
			storeMoneyRowPanel(2, row);
			storeMoneyNameLabel(2, nameLabel);
			concessionsValueLabel = valueLabel;
		} else if ("Droits TV".equals(name)) {
			storeMoneyRowPanel(3, row);
			storeMoneyNameLabel(3, nameLabel);
			tvValueLabel = valueLabel;
		} else if ("Voyage equipe".equals(name)) {
			storeMoneyRowPanel(4, row);
			storeMoneyNameLabel(4, nameLabel);
			travelValueLabel = valueLabel;
		} else if ("Charges match".equals(name)) {
			storeMoneyRowPanel(5, row);
			storeMoneyNameLabel(5, nameLabel);
			expenseValueLabel = valueLabel;
		}

		row.add(nameLabel, BorderLayout.WEST);
		row.add(valueLabel, BorderLayout.EAST);
		return row;
	}

	private JPanel buildNetPanel() {
		netPanel = new JPanel(new BorderLayout());
		JPanel panel = netPanel;
		panel.setOpaque(false);
		applySectionBorder(panel, 2, 18);

		netTitleLabel = new JLabel("RESULTAT NET");
		netTitleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));

		netValueLabel = new JLabel("-", SwingConstants.RIGHT);
		netValueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

		panel.add(netTitleLabel, BorderLayout.WEST);
		panel.add(netValueLabel, BorderLayout.EAST);
		return panel;
	}

	private JLabel buildNameLabel(String text) {
		JLabel label = new JLabel(text);
		label.setForeground(new Color(90, 90, 90));
		label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		return label;
	}

	private void storeMoneyNameLabel(int index, JLabel label) {
		if (moneyNameLabels == null) {
			moneyNameLabels = new JLabel[6];
		}
		moneyNameLabels[index] = label;
	}

	private void storeMoneyRowPanel(int index, JPanel row) {
		if (moneyRowPanels == null) {
			moneyRowPanels = new JPanel[6];
		}
		moneyRowPanels[index] = row;
	}

	private void setPositiveValue(JLabel label, String value) {
		label.setForeground(getPositiveColor());
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

	@Override
	public void applyTheme() {
		applySectionBorder(attendanceRowPanel, 1, 14);
		if (moneyRowPanels != null) {
			for (int i = 0; i < moneyRowPanels.length; i++) {
				if (moneyRowPanels[i] != null) {
					applySectionBorder(moneyRowPanels[i], 1, 16);
				}
			}
		}
		if (netPanel != null) {
			applySectionBorder(netPanel, 2, 18);
		}
		attendanceTitleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		attendanceValueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		attendanceInfoLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		if (moneyNameLabels != null) {
			for (int i = 0; i < moneyNameLabels.length; i++) {
				if (moneyNameLabels[i] != null) {
					moneyNameLabels[i].setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
				}
			}
		}
		netTitleLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		setPositiveValue(ticketValueLabel, stripSign(ticketValueLabel.getText()));
		setPositiveValue(merchandisingValueLabel, stripSign(merchandisingValueLabel.getText()));
		setPositiveValue(concessionsValueLabel, stripSign(concessionsValueLabel.getText()));
		setPositiveValue(tvValueLabel, stripSign(tvValueLabel.getText()));
		setNegativeValue(travelValueLabel, stripSign(travelValueLabel.getText()));
		setNegativeValue(expenseValueLabel, stripSign(expenseValueLabel.getText()));
		if (netValueLabel.getText().startsWith("-")) {
			setNegativeValue(netValueLabel, stripSign(netValueLabel.getText()));
		} else {
			setPositiveValue(netValueLabel, stripSign(netValueLabel.getText()));
		}
	}

	private void applySectionBorder(JPanel panel, int topThickness, int verticalPadding) {
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(topThickness, 0, 0, 0, DashboardPanelUtil.BORDER_COLOR),
				BorderFactory.createEmptyBorder(verticalPadding, 10, verticalPadding, 10)));
	}

	private Color getPositiveColor() {
		if (DashboardPanelUtil.isDarkMode()) {
			return new Color(120, 156, 255);
		}
		return TITLE_COLOR;
	}

	private String stripSign(String text) {
		if (text == null || "-".equals(text)) {
			return "-";
		}
		if (text.startsWith("+") || text.startsWith("-")) {
			return text.substring(1);
		}
		return text;
	}
}
