package test.unit;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import config.FinanceConfiguration;
import data.finance.transfer.Trade;
import data.player.Player;
import data.team.Team;
import gui.panel.mapPanel.effectifPanel.tradePanel.TradeDataUtil;
import gui.panel.mapPanel.effectifPanel.tradePanel.TradeEntryData;
import test.support.TestSupport;

public class TestTradeDisplayData {

	private Team teamA;
	private Team teamB;
	private Player playerA;
	private Player playerB;

	@Before
	public void setUp() {
		ArrayList<Team> teams = TestSupport.firstTeams(TestSupport.buildLeagueWithFinance(), 2);
		teamA = teams.get(0);
		teamB = teams.get(1);
		playerA = teamA.getCurrentPlayers().values().iterator().next();
		playerB = teamB.getCurrentPlayers().values().iterator().next();
	}

	@Test
	public void shouldBuildTradeEntryFromSelectedTeamPerspective() {
		Trade trade = new Trade(playerA, teamA, playerB, teamB, LocalDate.of(2026, 1, 15));

		TradeEntryData tradeEntryData = TradeEntryData.fromTrade(trade, teamA);

		assertEquals(playerA, tradeEntryData.getDeparturePlayer());
		assertEquals(playerB, tradeEntryData.getArrivalPlayer());
		assertEquals("Avec " + teamB.getName(), tradeEntryData.getPartnerText());
		assertTrue(tradeEntryData.getDepartureText().contains(playerA.getName()));
		assertTrue(tradeEntryData.getArrivalText().contains(playerB.getName()));
	}

	@Test
	public void shouldBuildPreseasonTradeLabel() {
		Trade trade = new Trade(playerA, teamA, playerB, teamB, FinanceConfiguration.PRESEASON_TRADE);

		TradeEntryData tradeEntryData = TradeEntryData.fromTrade(trade, teamA);

		assertEquals("Pré-saison", tradeEntryData.getDateText());
	}

	@Test
	public void shouldSortTradesChronologically() {
		Trade laterTrade = new Trade(playerA, teamA, playerB, teamB, LocalDate.of(2026, 2, 1));
		Trade earlierTrade = new Trade(playerA, teamA, playerB, teamB, LocalDate.of(2026, 1, 1));
		ArrayList<Trade> trades = new ArrayList<Trade>();
		trades.add(laterTrade);
		trades.add(earlierTrade);

		ArrayList<Trade> sortedTrades = TradeDataUtil.sortTradesByDate(trades);

		assertEquals(earlierTrade, sortedTrades.get(0));
		assertEquals(laterTrade, sortedTrades.get(1));
	}
}
