package test.performance;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import data.league.League;
import data.sport.setup.Game;
import process.service.finance.FinanceManager;
import test.support.TestSupport;

public class TestFinancePerformance {

	private static final double APPLY_MONTHLY_FINANCE_MAX_MS = 1500.0;
	private static final double SIX_MONTHS_FINANCE_MAX_MS = 4000.0;
	private static final double FIFTY_GAME_FINANCE_MAX_MS = 3000.0;
	private static final double THREE_FULL_FINANCE_CYCLES_MAX_MS = 3000.0;

	@Before
	public void setUp() {
		TestSupport.clearRepositories();
	}

	@Test
	public void shouldApplyMonthlyFinanceQuickly() {
		League league = TestSupport.buildLeagueWithFinance();
		FinanceManager financeManager = new FinanceManager(league);

		long start = System.nanoTime();
		financeManager.applyMonthlyFinance(1);
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		assertTrue(league.getLeagueFinance().getBudget().getRemainingAmount() > 0);

		TestSupport.assertBelow("applyMonthlyFinance", elapsedMs, APPLY_MONTHLY_FINANCE_MAX_MS);
	}

	@Test
	public void shouldApplySixMonthsOfFinanceQuickly() {
		League league = TestSupport.buildLeagueWithFinance();
		FinanceManager financeManager = new FinanceManager(league);

		long start = System.nanoTime();
		for (int month = 1; month <= 6; month++) {
			financeManager.applyMonthlyFinance(month);
		}
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		assertTrue(league.getLeagueFinance().getBudget().getRemainingAmount() > 0);

		TestSupport.assertBelow("sixMonthsFinance", elapsedMs, SIX_MONTHS_FINANCE_MAX_MS);
	}

	@Test
	public void shouldCalculateRegularSeasonGameFinanceQuickly() {
		League league = TestSupport.buildLeagueWithFinance();
		FinanceManager financeManager = new FinanceManager(league);

		long start = System.nanoTime();
		for (int index = 0; index < 50; index++) {
			Game game = TestSupport.createInterConferenceGame(league, index % 10, (index % 10) + 1);
			financeManager.calculateRegularSeasonGame(game, java.time.LocalDate.of(2025, 10, 21), 1);
			assertTrue(financeManager.getGameStat(game) != null);
		}
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		TestSupport.assertBelow("fiftyGameFinance", elapsedMs, FIFTY_GAME_FINANCE_MAX_MS);
	}

	@Test
	public void shouldApplySeveralFullFinanceCyclesQuickly() {
		long start = System.nanoTime();
		double remainingBudget = 0.0;

		for (int cycle = 0; cycle < 3; cycle++) {
			League league = TestSupport.buildLeagueWithFinance();
			FinanceManager financeManager = new FinanceManager(league);

			for (int month = 1; month <= 12; month++) {
				financeManager.applyMonthlyFinance(month);
			}

			remainingBudget += league.getLeagueFinance().getBudget().getRemainingAmount();
		}
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		assertTrue(remainingBudget > 0.0);

		TestSupport.assertBelow("threeFullFinanceCycles", elapsedMs, THREE_FULL_FINANCE_CYCLES_MAX_MS);
	}
}
