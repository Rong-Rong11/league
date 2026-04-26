package test.unit;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import config.FinanceConfiguration;
import data.calendar.GameDay;
import data.finance.GameStat;
import data.finance.budget.Budget;
import data.finance.budget.expense.ExpenseType;
import data.league.League;
import data.league.PlayoffRound;
import data.sport.setup.Game;
import data.sport.setup.PlayoffSeries;
import data.team.Team;
import process.service.finance.FinanceManager;
import process.service.finance.expense.LeagueExpenseCalculator;
import test.support.TestSupport;

public class TestLeagueExpenseCalculator {

	private League league;
	private LeagueExpenseCalculator calculator;
	private Budget leagueBudget;
	private Team homeTeam;
	private Team awayTeam;

	@Before
	public void setUp() {
		league = TestSupport.buildLeagueWithFinance();
		calculator = new LeagueExpenseCalculator(league);
		leagueBudget = league.getLeagueFinance().getBudget();
		TestSupport.resetBudget(leagueBudget);
		ArrayList<Team> teams = TestSupport.firstTeams(league, 2);
		homeTeam = teams.get(0);
		awayTeam = teams.get(1);
	}

	@Test
	public void shouldApplyMonthlyLeagueExpensesAndUpdateBudget() {
		calculator.applyMonthlyExpenses(2);

		assertTrue(leagueBudget.getExpensesForMonth(2).containsKey(ExpenseType.ADMINISTRATIVE_COST.name()));
		assertTrue(leagueBudget.getExpensesForMonth(2).containsKey(ExpenseType.MEDIA_COST.name()));
		assertTrue(leagueBudget.getExpensesForMonth(2).containsKey(ExpenseType.MARKETING_COST.name()));
		assertTrue(leagueBudget.getExpensesForMonth(2).containsKey(ExpenseType.OFFICIATING_COST.name()));

		double total = leagueBudget.getExpensesForMonth(2).get(ExpenseType.ADMINISTRATIVE_COST.name()).getAmount()
				+ leagueBudget.getExpensesForMonth(2).get(ExpenseType.MEDIA_COST.name()).getAmount()
				+ leagueBudget.getExpensesForMonth(2).get(ExpenseType.MARKETING_COST.name()).getAmount()
				+ leagueBudget.getExpensesForMonth(2).get(ExpenseType.OFFICIATING_COST.name()).getAmount();
		assertEquals(leagueBudget.getInitialAmount() - total, leagueBudget.getRemainingAmount(), 0.0001);
	}

	@Test
	public void shouldCalculateExpectedLeagueExpensesForNormalMonthWithoutGames() {
		calculator.applyMonthlyExpenses(2);

		assertEquals(FinanceConfiguration.LEAGUE_ADMINISTRATIVE_COST * 1.6,
				leagueBudget.getExpensesForMonth(2).get(ExpenseType.ADMINISTRATIVE_COST.name()).getAmount(), 0.0001);
		assertEquals(expectedMediaCost(2, false, 0, 0),
				leagueBudget.getExpensesForMonth(2).get(ExpenseType.MEDIA_COST.name()).getAmount(), 0.0001);
		assertEquals(expectedMarketingCost(2, false, 0, 0),
				leagueBudget.getExpensesForMonth(2).get(ExpenseType.MARKETING_COST.name()).getAmount(), 0.0001);
		assertEquals(expectedOfficiatingCost(2, false, 0, 0),
				leagueBudget.getExpensesForMonth(2).get(ExpenseType.OFFICIATING_COST.name()).getAmount(), 0.0001);
	}

	@Test
	public void shouldApplyHigherImportantMonthCostsThanNormalMonth() {
		calculator.applyMonthlyExpenses(1);
		double importantMonthMedia = leagueBudget.getExpensesForMonth(1).get(ExpenseType.MEDIA_COST.name()).getAmount();
		double importantMonthMarketing = leagueBudget.getExpensesForMonth(1).get(ExpenseType.MARKETING_COST.name())
				.getAmount();
		double importantMonthOfficiating = leagueBudget.getExpensesForMonth(1).get(ExpenseType.OFFICIATING_COST.name())
				.getAmount();

		TestSupport.resetBudget(leagueBudget);
		calculator.applyMonthlyExpenses(2);

		assertTrue(
				importantMonthMedia > leagueBudget.getExpensesForMonth(2).get(ExpenseType.MEDIA_COST.name()).getAmount());
		assertTrue(importantMonthMarketing > leagueBudget.getExpensesForMonth(2).get(ExpenseType.MARKETING_COST.name())
				.getAmount());
		assertTrue(
				importantMonthOfficiating > leagueBudget.getExpensesForMonth(2).get(ExpenseType.OFFICIATING_COST.name())
						.getAmount());
	}

	@Test
	public void shouldIncreaseLeagueExpensesWhenPlayoffActivityExists() {
		Game playoffGame = TestSupport.createInterConferenceGame(homeTeam, awayTeam);
		GameDay playoffDay = new GameDay(java.time.LocalDate.of(2026, 5, 1));
		playoffDay.addGame(playoffGame);
		TestSupport.setPlayoffCalendar(league, playoffDay);
		PlayoffSeries series = new PlayoffSeries(homeTeam, awayTeam);
		league.getPlayoff().setCurrentRound(PlayoffRound.FIRST_ROUND);
		league.getPlayoff().getEastFirstRound().clear();
		league.getPlayoff().getEastFirstRound().add(series);

		TestSupport.resetBudget(leagueBudget);
		calculator.applyMonthlyExpenses(8);
		double playoffMedia = leagueBudget.getExpensesForMonth(8).get(ExpenseType.MEDIA_COST.name()).getAmount();
		double playoffMarketing = leagueBudget.getExpensesForMonth(8).get(ExpenseType.MARKETING_COST.name()).getAmount();

		league.getPlayoff().setCurrentRound(null);
		league.getPlayoff().getEastFirstRound().clear();
		TestSupport.setPlayoffCalendar(league);
		TestSupport.resetBudget(leagueBudget);
		calculator.applyMonthlyExpenses(8);

		assertTrue(playoffMedia > leagueBudget.getExpensesForMonth(8).get(ExpenseType.MEDIA_COST.name()).getAmount());
		assertTrue(
				playoffMarketing > leagueBudget.getExpensesForMonth(8).get(ExpenseType.MARKETING_COST.name()).getAmount());
	}

	@Test
	public void shouldCountHighAttendanceImportantGamesWhenFinanceManagerProvidesGameStats() {
		homeTeam.setCurrentPopularity(5.0);
		awayTeam.setCurrentPopularity(5.0);
		Game regularGame = TestSupport.createInterConferenceGame(homeTeam, awayTeam);
		homeTeam.setRival("same");
		awayTeam.setRival("same");
		Game importantGame = new Game(
				new data.sport.setup.GameContext(homeTeam, awayTeam, config.GameConfiguration.GAME_INTRA_DIVISION));
		GameDay gameDay = new GameDay(java.time.LocalDate.of(2025, 10, 25));
		gameDay.addGame(regularGame);
		gameDay.addGame(importantGame);
		TestSupport.setRegularSeasonCalendar(league, gameDay);

		FinanceManager financeManager = new FinanceManager(league) {
			@Override
			public GameStat getGameStat(Game game) {
				GameStat gameStat = new GameStat(game);
				if (game == importantGame) {
					gameStat.setAttendanceRate(0.90);
				} else {
					gameStat.setAttendanceRate(0.50);
				}
				return gameStat;
			}
		};
		calculator.setFinanceManager(financeManager);
		TestSupport.resetBudget(leagueBudget);

		calculator.applyMonthlyExpenses(1);
		double boostedMediaCost = leagueBudget.getExpensesForMonth(1).get(ExpenseType.MEDIA_COST.name()).getAmount();

		LeagueExpenseCalculator calculatorWithoutFinanceManager = new LeagueExpenseCalculator(league);
		TestSupport.resetBudget(leagueBudget);
		calculatorWithoutFinanceManager.applyMonthlyExpenses(1);
		double baseMediaCost = leagueBudget.getExpensesForMonth(1).get(ExpenseType.MEDIA_COST.name()).getAmount();

		assertTrue(boostedMediaCost > baseMediaCost);
	}

	private double expectedMediaCost(int month, boolean playoffMonth, int importantGames, int activePlayoffTeams) {
		double cost = FinanceConfiguration.LEAGUE_MEDIA_COST * 1.2;
		if (month == 1 || month == 6 || month == 10) {
			cost *= 1.22;
		}
		cost *= 1 + (importantGames * 0.035);
		cost *= playoffMonth ? 1.15 : 1.0;
		cost *= 1 + (activePlayoffTeams * 0.022);
		cost *= playoffMonth ? 1.15 : ((month == 1 || month == 6 || month == 10) ? 1.06 : 1.0);
		cost *= 1 + (Math.cos((month * 1.41) + (importantGames * 0.13) + (activePlayoffTeams * 0.17)) * 0.075);
		return cost;
	}

	private double expectedMarketingCost(int month, boolean playoffMonth, int importantGames, int activePlayoffTeams) {
		double cost = FinanceConfiguration.LEAGUE_MARKETING_COST * 1.8;
		if (month == 1 || month == 6 || month == 10) {
			cost *= 1.28;
		}
		cost *= 1 + (importantGames * 0.042);
		cost *= playoffMonth ? 1.18 : 1.0;
		cost *= 1 + (activePlayoffTeams * 0.028);
		cost *= playoffMonth ? 1.22 : ((month == 1 || month == 6 || month == 10) ? 1.06 : 1.0);
		cost *= 1 + (Math.cos((month * 1.41) + (importantGames * 0.13) + (activePlayoffTeams * 0.17)) * 0.085);
		return cost;
	}

	private double expectedOfficiatingCost(int month, boolean playoffMonth, int importantGames, int activePlayoffTeams) {
		double cost = FinanceConfiguration.LEAGUE_OFFICIATING_COST * 1.7;
		if (month == 1 || month == 6 || month == 10) {
			cost *= 1.18;
		}
		cost *= 1 + (importantGames * 0.014);
		cost *= playoffMonth ? 1.16 : 1.0;
		cost *= 1 + (activePlayoffTeams * 0.008);
		cost *= playoffMonth ? 1.18 : ((month == 1 || month == 6 || month == 10) ? 1.06 : 1.0);
		cost *= 1 + (Math.cos((month * 1.41) + (importantGames * 0.13) + (activePlayoffTeams * 0.17)) * 0.12);
		return cost;
	}
}
