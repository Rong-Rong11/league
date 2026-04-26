package test.performance;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import data.league.League;
import process.service.finance.FinanceManager;
import process.service.finance.RevenueSharingManager;
import process.service.league.TeamPopularityUpdater;
import test.support.TestSupport;

public class TestLeagueServicePerformance {

	private static final double REVENUE_SHARING_MAX_MS = 200.0;
	private static final double UPDATE_BEFORE_SEASON_MAX_MS = 120.0;
	private static final double UPDATE_MONTHLY_POPULARITY_MAX_MS = 120.0;

	@Before
	public void setUp() {
		TestSupport.clearRepositories();
	}

	@Test
	public void shouldApplyRevenueSharingQuickly() {
		League league = TestSupport.buildLeagueWithFinance();
		FinanceManager financeManager = new FinanceManager(league);
		RevenueSharingManager revenueSharingManager = new RevenueSharingManager(league);
		financeManager.applyMonthlyFinance(1);

		long start = System.nanoTime();
		revenueSharingManager.applyRevenueSharing(1);
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		assertTrue(league.getLeagueFinance().getBudget().getRemainingAmount() > 0);

		TestSupport.assertBelow("revenueSharing", elapsedMs, REVENUE_SHARING_MAX_MS);
	}

	@Test
	public void shouldUpdateTeamPopularityBeforeSeasonQuickly() {
		League league = TestSupport.buildLeagueWithFinance();
		TeamPopularityUpdater popularityUpdater = new TeamPopularityUpdater();

		long start = System.nanoTime();
		popularityUpdater.updateBeforeSeason();
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		assertEquals(30, league.getAllTeam().size());

		TestSupport.assertBelow("updateBeforeSeasonPopularity", elapsedMs, UPDATE_BEFORE_SEASON_MAX_MS);
	}

	@Test
	public void shouldUpdateTeamPopularityMonthlyQuickly() {
		League league = TestSupport.buildLeagueWithFinance();
		TeamPopularityUpdater popularityUpdater = new TeamPopularityUpdater();
		popularityUpdater.updateBeforeSeason();

		long start = System.nanoTime();
		popularityUpdater.updateMonthlyPopularity();
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		assertEquals(30, league.getAllTeam().size());

		TestSupport.assertBelow("updateMonthlyPopularity", elapsedMs, UPDATE_MONTHLY_POPULARITY_MAX_MS);
	}
}
