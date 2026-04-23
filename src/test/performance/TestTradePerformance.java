package test.performance;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import config.CalendarConfiguration;
import config.FinanceConfiguration;
import data.league.League;
import data.league.finance.LeagueFinancialRules;
import process.repository.TeamRepository;
import process.service.trade.PreSeasonTradeService;
import process.service.trade.RegularSeasonTradeService;
import test.support.TestSupport;

public class TestTradePerformance {

	private static final double PRESEASON_TRADES_MAX_MS = 250.0;
	private static final double REGULAR_SEASON_TRADES_MAX_MS = 250.0;
	private static final double MULTI_MONTH_TRADES_MAX_MS = 800.0;

	@Before
	public void setUp() {
		TestSupport.clearRepositories();
	}

	@Test
	public void shouldSimulatePreSeasonTradesQuickly() {
		League league = TestSupport.buildLeagueWithFinance();
		LeagueFinancialRules rules = league.getLeagueFinance().getLeagueFinancialRules();
		PreSeasonTradeService tradeService = new PreSeasonTradeService(rules.getSalaryCap(), rules.getLuxuryTaxLine());

		long start = System.nanoTime();
		tradeService.simulateTrade(FinanceConfiguration.PRESEASON_TRADE, 0);
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		assertEquals(30, TeamRepository.getInstance().getAllTeams().size());

		TestSupport.assertBelow("preSeasonTrades", elapsedMs, PRESEASON_TRADES_MAX_MS);
	}

	@Test
	public void shouldSimulateRegularSeasonTradesQuickly() {
		League league = TestSupport.buildLeagueWithFinance();
		LeagueFinancialRules rules = league.getLeagueFinance().getLeagueFinancialRules();
		RegularSeasonTradeService tradeService = new RegularSeasonTradeService(rules.getSalaryCap(),
				rules.getLuxuryTaxLine());

		long start = System.nanoTime();
		tradeService.simulateTrade(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE.plusDays(30), 2);
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		assertEquals(30, TeamRepository.getInstance().getAllTeams().size());

		TestSupport.assertBelow("regularSeasonTrades", elapsedMs, REGULAR_SEASON_TRADES_MAX_MS);
	}

	@Test
	public void shouldHandleSeveralTradeWindowsQuickly() {
		League league = TestSupport.buildLeagueWithFinance();
		LeagueFinancialRules rules = league.getLeagueFinance().getLeagueFinancialRules();
		RegularSeasonTradeService tradeService = new RegularSeasonTradeService(rules.getSalaryCap(),
				rules.getLuxuryTaxLine());

		long start = System.nanoTime();
		for (int month = 1; month <= 4; month++) {
			tradeService.simulateTrade(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE.plusDays(month * 20), month);
		}
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		assertEquals(30, TeamRepository.getInstance().getAllTeams().size());

		TestSupport.assertBelow("multiMonthTrades", elapsedMs, MULTI_MONTH_TRADES_MAX_MS);
	}
}
