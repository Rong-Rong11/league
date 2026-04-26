package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.jfree.data.category.DefaultCategoryDataset;

import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.finance.budget.expense.Expense;
import data.finance.budget.income.Income;
import data.league.League;
import data.league.Playoff;
import data.sport.setup.PlayoffSeries;
import data.team.Team;
import data.team.finance.TeamFinance;
import gui.panel.common.BuildBox;
import gui.panel.common.ButtonStyleUtil;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.RoundedPanel;
import gui.panel.common.ThemeAware;
import gui.panel.financePanel.FinanceDataUtil;
import gui.panel.financePanel.FinanceViewFactory;
import gui.utility.TeamDisplayUtility;
import process.orchestrator.interf.GUIInterface;

public class SeasonEndDashboard extends JPanel implements RefreshableDashboard, ThemeAware {
	private static final int GAP = 16;
	private static final int LIST_LIMIT = 5;
	private static final String OVERVIEW_PAGE = "overview";
	private static final String FINANCE_PAGE = "finance";
	private static final String PROFILE_PAGE = "profile";

	private final GUIInterface guiInterface;
	private final CardLayout pageLayout;
	private final JPanel pagePanel;

	private JButton overviewButton;
	private JButton financePageButton;
	private JButton profilePageButton;
	private JButton reviewRankingButton;
	private JButton openFinanceButton;
	private String selectedPage;

	public SeasonEndDashboard(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		pageLayout = new CardLayout();
		pagePanel = new JPanel(pageLayout);
		selectedPage = OVERVIEW_PAGE;
		create();
		organize();
	}

	private void create() {
		overviewButton = new RoundedButton("Vue d'ensemble");
		financePageButton = new RoundedButton("Finances");
		profilePageButton = new RoundedButton("Profils & graphes");
		reviewRankingButton = new RoundedButton("Revoir le classement");
		openFinanceButton = new RoundedButton("Voir finances");
		actions();
		applyButtonStyle();
	}

	private void organize() {
		removeAll();
		setLayout(new BorderLayout(0, GAP));
		setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
		setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

		add(buildHeader(), BorderLayout.NORTH);
		add(buildBody(), BorderLayout.CENTER);
		add(buildFooter(), BorderLayout.SOUTH);

		refreshPageContent();
	}

	private JPanel buildHeader() {
		JPanel wrapper = new JPanel(new BorderLayout(0, 14));
		wrapper.setOpaque(false);
		wrapper.add(buildTitleBlock(), BorderLayout.NORTH);
		wrapper.add(buildPageSelector(), BorderLayout.SOUTH);
		return wrapper;
	}

	private JPanel buildTitleBlock() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JLabel iconLabel = new JLabel("F", SwingConstants.CENTER);
		iconLabel.setOpaque(true);
		iconLabel.setBackground(DashboardPanelUtil.NEUTRAL_ACCENT_COLOR);
		iconLabel.setForeground(DashboardPanelUtil.getPrimaryActionTextColor());
		iconLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		iconLabel.setPreferredSize(new Dimension(68, 68));
		iconLabel.setMaximumSize(new Dimension(68, 68));
		iconLabel.setAlignmentX(CENTER_ALIGNMENT);

		JLabel titleLabel = new JLabel("Simulation terminee", SwingConstants.CENTER);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 34));
		titleLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		titleLabel.setAlignmentX(CENTER_ALIGNMENT);

		JLabel subtitleLabel = new JLabel(buildHeaderSubtitle(), SwingConstants.CENTER);
		subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		subtitleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		subtitleLabel.setAlignmentX(CENTER_ALIGNMENT);

		panel.add(iconLabel);
		panel.add(Box.createVerticalStrut(14));
		panel.add(titleLabel);
		panel.add(Box.createVerticalStrut(6));
		panel.add(subtitleLabel);
		return panel;
	}

	private JPanel buildPageSelector() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		panel.setOpaque(false);
		stylePageSwitchButton(overviewButton, OVERVIEW_PAGE.equals(selectedPage));
		stylePageSwitchButton(financePageButton, FINANCE_PAGE.equals(selectedPage));
		stylePageSwitchButton(profilePageButton, PROFILE_PAGE.equals(selectedPage));
		panel.add(overviewButton);
		panel.add(financePageButton);
		panel.add(profilePageButton);
		return panel;
	}

	private JPanel buildBody() {
		JPanel body = new JPanel(new BorderLayout());
		body.setOpaque(false);
		pagePanel.setOpaque(false);
		body.add(pagePanel, BorderLayout.CENTER);
		return body;
	}

	private JPanel buildFooter() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
		panel.setOpaque(false);
		panel.add(reviewRankingButton);
		panel.add(openFinanceButton);
		return panel;
	}

	private void refreshPageContent() {
		pagePanel.removeAll();
		pagePanel.add(buildOverviewPage(), OVERVIEW_PAGE);
		pagePanel.add(buildFinancePage(), FINANCE_PAGE);
		pagePanel.add(buildProfilePage(), PROFILE_PAGE);
		pageLayout.show(pagePanel, selectedPage);
		pagePanel.revalidate();
		pagePanel.repaint();
	}

	private JPanel buildOverviewPage() {
		JPanel page = new JPanel(new BorderLayout(0, GAP));
		page.setOpaque(false);
		page.add(buildKeyStatsPanel(), BorderLayout.NORTH);

		JPanel center = new JPanel(new GridLayout(1, 2, GAP, 0));
		center.setOpaque(false);
		center.add(new BuildBox("BILAN SPORTIF", "Classement et playoffs", buildSportPanel()));
		center.add(new BuildBox("SYNTHESE SAISON", "Resultats finaux", buildSeasonSummaryPanel()));
		page.add(center, BorderLayout.CENTER);
		return page;
	}

	private JPanel buildFinancePage() {
		JPanel page = new JPanel(new BorderLayout(0, GAP));
		page.setOpaque(false);

		JPanel top = new JPanel(new GridLayout(1, 2, GAP, 0));
		top.setOpaque(false);
		top.add(new BuildBox("LIGUE", "Budget et resultat global", buildLeagueFinancePanel()));
		top.add(new BuildBox("CLUBS", "Top et bottom nets", buildTeamFinancePanel()));
		page.add(top, BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 3, GAP, 0));
		charts.setOpaque(false);
		charts.add(new BuildBox("HISTORIQUE", "Revenus, depenses et net",
				FinanceViewFactory.financeLineChart(buildLeagueHistoryDataset(), DashboardPanelUtil.REVENUE_COLOR)));
		charts.add(new BuildBox("TOP 5 NETS", "Equipes les plus rentables",
				FinanceViewFactory.financeBarChart(buildTeamNetDataset(true), DashboardPanelUtil.POSITIVE_VALUE_COLOR)));
		charts.add(new BuildBox("BOTTOM 5 NETS", "Equipes les moins rentables",
				FinanceViewFactory.financeBarChart(buildTeamNetDataset(false), DashboardPanelUtil.EXPENSE_COLOR)));
		page.add(charts, BorderLayout.CENTER);
		return page;
	}

	private JPanel buildProfilePage() {
		JPanel page = new JPanel(new BorderLayout(0, GAP));
		page.setOpaque(false);

		JPanel top = new JPanel(new GridLayout(1, 3, GAP, 0));
		top.setOpaque(false);
		top.add(new BuildBox("PROFILS DES CLUBS", "Meilleurs et pires nets", buildTeamProfilePanel()));
		top.add(new BuildBox("TAILLE DU MARCHE", "Repartition des equipes", buildMarketSummaryPanel()));
		top.add(new BuildBox("POLITIQUE FINANCIERE", "Repartition des equipes", buildPolicySummaryPanel()));
		page.add(top, BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 3, GAP, 0));
		charts.setOpaque(false);
		charts.add(new BuildBox("LIGUE", "Revenus contre depenses",
				FinanceViewFactory.financeBarChart(buildLeagueTotalDataset(), DashboardPanelUtil.REVENUE_COLOR)));
		charts.add(new BuildBox("MARCHES", "Nombre d'equipes",
				FinanceViewFactory.financeBarChart(buildCountDataset(countByMarket(), "Marche"),
						DashboardPanelUtil.POLICY_BALANCED_COLOR)));
		charts.add(new BuildBox("POLITIQUES", "Nombre d'equipes",
				FinanceViewFactory.financeBarChart(buildCountDataset(countByPolicy(), "Politique"),
						DashboardPanelUtil.NEUTRAL_ACCENT_COLOR)));
		page.add(charts, BorderLayout.CENTER);
		return page;
	}

	private JPanel buildKeyStatsPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 6, GAP, 0));
		panel.setOpaque(false);
		ArrayList<Team> ranking = guiInterface.getGlobalRanking();
		Team bestTeam = ranking.isEmpty() ? null : ranking.get(0);
		Team bestAttack = getBestAttack(ranking);
		Team richestTeam = getRichestTeam(guiInterface.getTeams());
		Team bestNetTeam = getBestNetTeam();
		Team worstNetTeam = getWorstNetTeam();

		panel.add(buildStatCard("Champion NBA", safeText(guiInterface.getPlayoffChampionName()), "Playoffs termines",
				DashboardPanelUtil.NEUTRAL_ACCENT_COLOR));
		panel.add(buildStatCard("Meilleure equipe", TeamDisplayUtility.getShortName(bestTeam), buildRecordText(bestTeam),
				DashboardPanelUtil.REVENUE_COLOR));
		panel.add(buildStatCard("Meilleure attaque", TeamDisplayUtility.getShortName(bestAttack),
				formatOneDecimal(guiInterface.getAveragePoints(bestAttack, true)) + " pts/match",
				DashboardPanelUtil.POSITIVE_VALUE_COLOR));
		panel.add(buildStatCard("Plus gros budget", TeamDisplayUtility.getShortName(richestTeam),
				formatMoney(getRemainingBudget(richestTeam)), DashboardPanelUtil.POLICY_BALANCED_COLOR));
		panel.add(buildStatCard("Meilleur net", TeamDisplayUtility.getShortName(bestNetTeam),
				formatMoney(getTotalTeamNet(bestNetTeam)), DashboardPanelUtil.POSITIVE_VALUE_COLOR));
		panel.add(buildStatCard("Pire net", TeamDisplayUtility.getShortName(worstNetTeam),
				formatMoney(getTotalTeamNet(worstNetTeam)), DashboardPanelUtil.EXPENSE_COLOR));
		return panel;
	}

	private JPanel buildSportPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 2, GAP, 0));
		panel.setOpaque(false);
		panel.add(buildTopRankingPanel());
		panel.add(buildPlayoffSummaryPanel());
		return panel;
	}

	private JPanel buildSeasonSummaryPanel() {
		JPanel panel = buildListPanel();
		panel.add(buildInfoRow("Champion", safeText(guiInterface.getPlayoffChampionName()),
				DashboardPanelUtil.NEUTRAL_ACCENT_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildInfoRow("Finaliste", TeamDisplayUtility.getShortName(getFinalist()),
				DashboardPanelUtil.POLICY_BALANCED_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildInfoRow("Series joue es", String.valueOf(guiInterface.getPlayoffSeriesCount()),
				DashboardPanelUtil.REVENUE_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildInfoRow("Matchs playoffs", String.valueOf(countPlayoffGames()),
				DashboardPanelUtil.POSITIVE_VALUE_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildInfoRow("Finale NBA", buildFinalsScoreText(), DashboardPanelUtil.TITLE_TEXT_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildInfoRow("Matchs totaux", String.valueOf(countTotalGames()),
				DashboardPanelUtil.STRATEGY_REBUILD_COLOR));
		return panel;
	}

	private JPanel buildLeagueFinancePanel() {
		JPanel panel = buildListPanel();
		League league = guiInterface.getLeague();
		Budget leagueBudget = getLeagueBudget();
		double totalNet = getTotalNet(leagueBudget);
		panel.add(buildInfoRow("Budget restant", formatMoney(getRemainingBudget(leagueBudget)),
				DashboardPanelUtil.POLICY_BALANCED_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildInfoRow("Valeur ligue",
				league == null || league.getLeagueFinance() == null ? "-" : formatMoney(league.getLeagueFinance().getLeagueValue()),
				DashboardPanelUtil.NEUTRAL_ACCENT_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildInfoRow("Revenus totaux", formatMoney(getTotalIncome(leagueBudget)),
				DashboardPanelUtil.REVENUE_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildInfoRow("Depenses totales", formatMoney(getTotalExpense(leagueBudget)),
				DashboardPanelUtil.EXPENSE_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildInfoRow("Net total", formatMoney(totalNet),
				DashboardPanelUtil.getValueColorForAmount(totalNet)));
		return panel;
	}

	private JPanel buildTeamFinancePanel() {
		JPanel panel = new JPanel(new GridLayout(1, 2, GAP, 0));
		panel.setOpaque(false);

		JPanel topPanel = buildListPanel();
		topPanel.add(buildSectionLabel("Top nets"));
		addTeamNetRows(topPanel, getTeamsSortedByNet(), 0, Math.min(LIST_LIMIT, getTeamsSortedByNet().size()));

		JPanel bottomPanel = buildListPanel();
		bottomPanel.add(buildSectionLabel("Bottom nets"));
		List<Team> teamsByNet = getTeamsSortedByNet();
		int bottomStart = Math.max(0, teamsByNet.size() - LIST_LIMIT);
		addTeamNetRows(bottomPanel, teamsByNet, bottomStart, teamsByNet.size());

		panel.add(topPanel);
		panel.add(bottomPanel);
		return panel;
	}

	private JPanel buildTopRankingPanel() {
		JPanel panel = buildListPanel();
		ArrayList<Team> ranking = guiInterface.getGlobalRanking();
		for (int index = 0; index < 8 && index < ranking.size(); index++) {
			Team team = ranking.get(index);
			panel.add(buildInfoRow((index + 1) + ". " + TeamDisplayUtility.getShortName(team), buildRecordText(team),
					getRankColor(index + 1)));
			if (index < 7) {
				panel.add(Box.createVerticalStrut(8));
			}
		}
		return panel;
	}

	private JPanel buildPlayoffSummaryPanel() {
		JPanel panel = buildListPanel();
		panel.add(buildInfoRow("Champion", safeText(guiInterface.getPlayoffChampionName()),
				DashboardPanelUtil.NEUTRAL_ACCENT_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildInfoRow("Finaliste", TeamDisplayUtility.getShortName(getFinalist()),
				DashboardPanelUtil.POLICY_BALANCED_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildInfoRow("Finale", buildFinalsScoreText(), DashboardPanelUtil.TITLE_TEXT_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildInfoRow("Series jouees", String.valueOf(guiInterface.getPlayoffSeriesCount()),
				DashboardPanelUtil.REVENUE_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildInfoRow("Matchs playoffs", String.valueOf(countPlayoffGames()),
				DashboardPanelUtil.POSITIVE_VALUE_COLOR));
		return panel;
	}

	private JPanel buildTeamProfilePanel() {
		JPanel panel = buildListPanel();
		List<Team> teamsByNet = getTeamsSortedByNet();
		panel.add(buildSectionLabel("Meilleurs nets"));
		panel.add(Box.createVerticalStrut(8));
		for (int i = 0; i < LIST_LIMIT && i < teamsByNet.size(); i++) {
			addProfileRow(panel, teamsByNet.get(i), DashboardPanelUtil.POSITIVE_VALUE_COLOR);
		}
		if (!teamsByNet.isEmpty()) {
			panel.add(Box.createVerticalStrut(14));
			panel.add(buildSectionLabel("Pires nets"));
			panel.add(Box.createVerticalStrut(8));
			int start = Math.max(0, teamsByNet.size() - LIST_LIMIT);
			for (int i = start; i < teamsByNet.size(); i++) {
				addProfileRow(panel, teamsByNet.get(i), DashboardPanelUtil.EXPENSE_COLOR);
			}
		}
		return panel;
	}

	private JPanel buildMarketSummaryPanel() {
		return buildCountPanel(countByMarket(), DashboardPanelUtil.POLICY_BALANCED_COLOR);
	}

	private JPanel buildPolicySummaryPanel() {
		return buildCountPanel(countByPolicy(), DashboardPanelUtil.NEUTRAL_ACCENT_COLOR);
	}

	private JPanel buildCountPanel(Map<String, Integer> counts, Color color) {
		JPanel panel = buildListPanel();
		for (String key : counts.keySet()) {
			panel.add(buildInfoRow(key, String.valueOf(counts.get(key)), color));
			panel.add(Box.createVerticalStrut(10));
		}
		return panel;
	}

	private RoundedPanel buildStatCard(String title, String value, String subtitle, Color valueColor) {
		RoundedPanel card = new RoundedPanel(new BorderLayout(), 16);
		card.setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(DashboardPanelUtil.BORDER_COLOR, 1),
				BorderFactory.createEmptyBorder(14, 14, 14, 14)));

		JPanel textPanel = new JPanel();
		textPanel.setOpaque(false);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		titleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		JLabel valueLabel = new JLabel(value);
		valueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		valueLabel.setForeground(valueColor);
		JLabel subtitleLabel = new JLabel(subtitle);
		subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		subtitleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		textPanel.add(titleLabel);
		textPanel.add(Box.createVerticalStrut(9));
		textPanel.add(valueLabel);
		textPanel.add(Box.createVerticalStrut(4));
		textPanel.add(subtitleLabel);
		card.add(textPanel, BorderLayout.CENTER);
		return card;
	}

	private JPanel buildListPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
		return panel;
	}

	private JPanel buildInfoRow(String title, String value, Color valueColor) {
		JPanel row = new JPanel(new BorderLayout(10, 0));
		row.setOpaque(false);
		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		titleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		JLabel valueLabel = new JLabel(value);
		valueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		valueLabel.setForeground(valueColor);
		row.add(titleLabel, BorderLayout.CENTER);
		row.add(valueLabel, BorderLayout.EAST);
		return row;
	}

	private JLabel buildSectionLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		label.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		return label;
	}

	private void addTeamNetRows(JPanel panel, List<Team> teams, int start, int end) {
		for (int i = start; i < end && i < teams.size(); i++) {
			Team team = teams.get(i);
			double net = getTotalTeamNet(team);
			panel.add(buildInfoRow(TeamDisplayUtility.getShortName(team), formatMoney(net),
					DashboardPanelUtil.getValueColorForAmount(net)));
			if (i < end - 1 && i < teams.size() - 1) {
				panel.add(Box.createVerticalStrut(8));
			}
		}
	}

	private void addProfileRow(JPanel panel, Team team, Color accentColor) {
		String title = TeamDisplayUtility.getShortName(team) + " | " + formatMoney(getTotalTeamNet(team));
		String details = getMarketLabel(team) + " | " + getPolicyLabel(team) + " | " + getStrategyLabel(team)
				+ " | Budget " + formatMoney(getRemainingBudget(team))
				+ " | Payroll " + formatMoney(getCurrentPayroll(team));
		panel.add(buildInfoRow(title, details, accentColor));
		panel.add(Box.createVerticalStrut(8));
	}

	private DefaultCategoryDataset buildLeagueHistoryDataset() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Budget budget = getLeagueBudget();
		for (int month = 1; month <= lastFinanceMonth(); month++) {
			String label = "M" + month;
			dataset.addValue(toMillions(getIncomeForMonth(budget, month)), "Revenus", label);
			dataset.addValue(toMillions(getExpenseForMonth(budget, month)), "Depenses", label);
			dataset.addValue(toMillions(getNetForMonth(budget, month)), "Net", label);
		}
		return dataset;
	}

	private DefaultCategoryDataset buildTeamNetDataset(boolean top) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		List<Team> teams = new ArrayList<Team>(getTeamsSortedByNet());
		if (!top) {
			Collections.reverse(teams);
		}
		int limit = Math.min(LIST_LIMIT, teams.size());
		for (int i = 0; i < limit; i++) {
			Team team = teams.get(i);
			dataset.addValue(toMillions(getTotalTeamNet(team)), top ? "Top nets" : "Bottom nets",
					TeamDisplayUtility.getShortName(team));
		}
		return dataset;
	}

	private DefaultCategoryDataset buildLeagueTotalDataset() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Budget budget = getLeagueBudget();
		dataset.addValue(toMillions(getTotalIncome(budget)), "Revenus", "Revenus");
		dataset.addValue(toMillions(getTotalExpense(budget)), "Depenses", "Depenses");
		dataset.addValue(toMillions(getTotalNet(budget)), "Net", "Net");
		return dataset;
	}

	private DefaultCategoryDataset buildCountDataset(Map<String, Integer> counts, String rowName) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (String key : counts.keySet()) {
			dataset.addValue(counts.get(key), rowName, key);
		}
		return dataset;
	}

	private List<Team> getTeamsSortedByNet() {
		ArrayList<Team> teams = new ArrayList<Team>(guiInterface.getTeams());
		Collections.sort(teams, new Comparator<Team>() {
			@Override
			public int compare(Team first, Team second) {
				return Double.compare(getTotalTeamNet(second), getTotalTeamNet(first));
			}
		});
		return teams;
	}

	private Team getBestAttack(ArrayList<Team> teams) {
		Team bestTeam = null;
		double bestScore = -1.0;
		for (Team team : teams) {
			double score = guiInterface.getAveragePoints(team, true);
			if (score > bestScore) {
				bestScore = score;
				bestTeam = team;
			}
		}
		return bestTeam;
	}

	private Team getRichestTeam(ArrayList<Team> teams) {
		Team bestTeam = null;
		double bestBudget = Double.NEGATIVE_INFINITY;
		for (Team team : teams) {
			double budget = getRemainingBudget(team);
			if (budget > bestBudget) {
				bestBudget = budget;
				bestTeam = team;
			}
		}
		return bestTeam;
	}

	private Team getBestNetTeam() {
		List<Team> teams = getTeamsSortedByNet();
		return teams.isEmpty() ? null : teams.get(0);
	}

	private Team getWorstNetTeam() {
		List<Team> teams = getTeamsSortedByNet();
		return teams.isEmpty() ? null : teams.get(teams.size() - 1);
	}

	private Team getFinalist() {
		PlayoffSeries finals = getFinalsSeries();
		if (finals == null || !finals.isFinished()) {
			return null;
		}
		if (finals.getHigherTeamWins() > finals.getLowerTeamWins()) {
			return finals.getLowerTeam();
		}
		return finals.getHigherTeam();
	}

	private PlayoffSeries getFinalsSeries() {
		Playoff playoff = getPlayoff();
		if (playoff == null || playoff.getNbaFinals().isEmpty()) {
			return null;
		}
		return playoff.getNbaFinals().get(0);
	}

	private String buildFinalsScoreText() {
		PlayoffSeries finals = getFinalsSeries();
		if (finals == null) {
			return "-";
		}
		return TeamDisplayUtility.getShortName(finals.getHigherTeam()) + " "
				+ finals.getHigherTeamWins() + "-"
				+ finals.getLowerTeamWins() + " "
				+ TeamDisplayUtility.getShortName(finals.getLowerTeam());
	}

	private int countTotalGames() {
		int regularSeasonGames = 0;
		for (Team team : guiInterface.getTeams()) {
			regularSeasonGames += guiInterface.getTeamNumberPlayedGames(team);
		}
		return regularSeasonGames / 2 + countPlayoffGames();
	}

	private int countPlayoffGames() {
		int count = 0;
		for (PlayoffSeries series : getAllPlayoffSeries()) {
			count += series.getNumberPlayedGames();
		}
		return count;
	}

	private ArrayList<PlayoffSeries> getAllPlayoffSeries() {
		ArrayList<PlayoffSeries> series = new ArrayList<PlayoffSeries>();
		Playoff playoff = getPlayoff();
		if (playoff == null) {
			return series;
		}
		series.addAll(playoff.getEastFirstRound());
		series.addAll(playoff.getWestFirstRound());
		series.addAll(playoff.getEastConferenceSemis());
		series.addAll(playoff.getWestConferenceSemis());
		series.addAll(playoff.getEastConferenceFinals());
		series.addAll(playoff.getWestConferenceFinals());
		series.addAll(playoff.getNbaFinals());
		return series;
	}

	private Playoff getPlayoff() {
		League league = guiInterface.getLeague();
		return league == null ? null : league.getPlayoff();
	}

	private Budget getLeagueBudget() {
		League league = guiInterface.getLeague();
		if (league == null || league.getLeagueFinance() == null) {
			return null;
		}
		return league.getLeagueFinance().getBudget();
	}

	private double getTotalTeamNet(Team team) {
		return getTotalNet(getTeamBudget(team));
	}

	private double getTotalNet(Budget budget) {
		double total = 0.0;
		if (budget != null) {
			for (int month = 1; month <= lastFinanceMonth(); month++) {
				total += getNetForMonth(budget, month);
			}
		}
		return total;
	}

	private double getTotalIncome(Budget budget) {
		double total = 0.0;
		if (budget != null) {
			for (int month = 1; month <= lastFinanceMonth(); month++) {
				total += getIncomeForMonth(budget, month);
			}
		}
		return total;
	}

	private double getTotalExpense(Budget budget) {
		double total = 0.0;
		if (budget != null) {
			for (int month = 1; month <= lastFinanceMonth(); month++) {
				total += getExpenseForMonth(budget, month);
			}
		}
		return total;
	}

	private double getIncomeForMonth(Budget budget, int month) {
		double total = 0.0;
		if (budget != null && budget.getIncomesForMonth(month) != null) {
			for (Income income : budget.getIncomesForMonth(month).values()) {
				total += income.getAmount();
			}
		}
		return total;
	}

	private double getExpenseForMonth(Budget budget, int month) {
		double total = 0.0;
		if (budget != null && budget.getExpensesForMonth(month) != null) {
			for (Expense expense : budget.getExpensesForMonth(month).values()) {
				total += expense.getAmount();
			}
		}
		return total;
	}

	private double getNetForMonth(Budget budget, int month) {
		return budget == null ? 0.0 : budget.getNetForMonth(month);
	}

	private double getRemainingBudget(Team team) {
		return getRemainingBudget(getTeamBudget(team));
	}

	private double getRemainingBudget(Budget budget) {
		return budget == null ? 0.0 : budget.getRemainingAmount();
	}

	private Budget getTeamBudget(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			return null;
		}
		return team.getTeamFinance().getBudget();
	}

	private double getCurrentPayroll(Team team) {
		return team == null || team.getTeamFinance() == null ? 0.0 : team.getTeamFinance().getCurrentPayroll();
	}

	private Map<String, Integer> countByMarket() {
		Map<String, Integer> counts = new LinkedHashMap<String, Integer>();
		counts.put("Petit marche", 0);
		counts.put("Marche moyen", 0);
		counts.put("Grand marche", 0);
		for (Team team : guiInterface.getTeams()) {
			increment(counts, getMarketLabel(team));
		}
		return counts;
	}

	private Map<String, Integer> countByPolicy() {
		Map<String, Integer> counts = new LinkedHashMap<String, Integer>();
		counts.put("Politique econome", 0);
		counts.put("Politique equilibree", 0);
		counts.put("Politique ambitieuse", 0);
		for (Team team : guiInterface.getTeams()) {
			increment(counts, getPolicyLabel(team));
		}
		return counts;
	}

	private void increment(Map<String, Integer> counts, String key) {
		String safeKey = key == null || key.equals("-") ? "Inconnu" : key;
		if (!counts.containsKey(safeKey)) {
			counts.put(safeKey, 0);
		}
		counts.put(safeKey, counts.get(safeKey) + 1);
	}

	private String getMarketLabel(Team team) {
		TeamFinance finance = team == null ? null : team.getTeamFinance();
		if (finance == null || finance.getStructure() == null) {
			return "-";
		}
		return FinanceDataUtil.formatMarket(finance.getStructure().getMarketSize());
	}

	private String getPolicyLabel(Team team) {
		TeamFinance finance = team == null ? null : team.getTeamFinance();
		if (finance == null || finance.getBehavior() == null) {
			return "-";
		}
		return FinanceDataUtil.formatPolicy(finance.getBehavior().getFinancialProfil());
	}

	private String getStrategyLabel(Team team) {
		TeamFinance finance = team == null ? null : team.getTeamFinance();
		if (finance == null || finance.getBehavior() == null) {
			return "-";
		}
		return FinanceDataUtil.formatStrategy(finance.getBehavior().getTeamTransferStrategy());
	}

	private int lastFinanceMonth() {
		return Math.max(1, FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS - 1);
	}

	private double toMillions(double value) {
		return value / 1000000.0;
	}

	private String buildRecordText(Team team) {
		if (team == null) {
			return "-";
		}
		int wins = guiInterface.getTeamNumberWin(team);
		int losses = guiInterface.getTeamNumberLose(team);
		int games = wins + losses;
		if (games == 0) {
			return wins + "-" + losses;
		}
		double pct = wins * 100.0 / games;
		return wins + "-" + losses + " (" + formatOneDecimal(pct) + "%)";
	}

	private Color getRankColor(int rank) {
		if (rank == 1) {
			return DashboardPanelUtil.NEUTRAL_ACCENT_COLOR;
		}
		if (rank <= 3) {
			return DashboardPanelUtil.POLICY_BALANCED_COLOR;
		}
		return DashboardPanelUtil.REVENUE_COLOR;
	}

	private String buildHeaderSubtitle() {
		String champion = guiInterface.getPlayoffChampionName();
		String finalist = TeamDisplayUtility.getShortName(getFinalist());
		if (champion == null || champion.equals("")) {
			return "Bilan final de la saison sur plusieurs vues.";
		}
		if (finalist == null || finalist.equals("-")) {
			return "Champion NBA : " + champion + ". Navigation sans scroll.";
		}
		return "Champion NBA : " + champion + " contre " + finalist + ". Navigation sans scroll.";
	}

	private String formatMoney(double value) {
		return FinanceDataUtil.formatMoney(value);
	}

	private String formatOneDecimal(double value) {
		return String.format(java.util.Locale.US, "%.1f", value);
	}

	private String safeText(String text) {
		return text == null || text.equals("") ? "-" : text;
	}

	private void actions() {
		overviewButton.addActionListener(new SwitchPageAction(OVERVIEW_PAGE));
		financePageButton.addActionListener(new SwitchPageAction(FINANCE_PAGE));
		profilePageButton.addActionListener(new SwitchPageAction(PROFILE_PAGE));
	}

	private void stylePageSwitchButton(JButton button, boolean selected) {
		ButtonStyleUtil.styleToggleButton(button);
		ButtonStyleUtil.setToggleButtonSelected(button, selected);
		button.setPreferredSize(new Dimension(170, 42));
	}

	private void styleButton(JButton button, Color background, Color foreground) {
		button.setPreferredSize(new Dimension(250, 52));
		button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
		button.setBackground(background);
		button.setForeground(foreground);
		button.setFocusPainted(false);
	}

	private void applyButtonStyle() {
		styleButton(reviewRankingButton, DashboardPanelUtil.BUTTON_SURFACE_COLOR, DashboardPanelUtil.BUTTON_TEXT_COLOR);
		styleButton(openFinanceButton, DashboardPanelUtil.getPrimaryActionColor(),
				DashboardPanelUtil.getPrimaryActionTextColor());
	}

	public JButton getReviewRankingButton() {
		return reviewRankingButton;
	}

	public JButton getOpenFinanceButton() {
		return openFinanceButton;
	}

	@Override
	public void refresh() {
		applyButtonStyle();
		organize();
		revalidate();
		repaint();
	}

	@Override
	public void applyTheme() {
		refresh();
	}

	private class SwitchPageAction implements ActionListener {
		private final String page;

		private SwitchPageAction(String page) {
			this.page = page;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			selectedPage = page;
			refresh();
		}
	}
}
