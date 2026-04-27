package test.unit;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import data.league.League;
import data.team.Team;
import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;
import data.team.finance.mediamarket.MediaMarket;
import process.service.finance.distribution.central.calculation.LeagueFinanceMetricsCalculator;
import test.support.TestSupport;

public class TestLeagueFinanceMetricsCalculator {

	private League league;
	private LeagueFinanceMetricsCalculator metricsCalculator;

	@Before
	public void setUp() {
		league = TestSupport.buildLeagueWithFinance();
		metricsCalculator = new LeagueFinanceMetricsCalculator();
	}

	@Test
	public void shouldCalculateMarketPowerIndexWithMarketSizeVisitor() {
		ArrayList<Team> teams = TestSupport.firstTeams(league, 3);
		teams.get(0).getTeamFinance().getStructure().setMarketSize(new SmallSize());
		teams.get(1).getTeamFinance().getStructure().setMarketSize(new MediumSize());
		teams.get(2).getTeamFinance().getStructure().setMarketSize(new LargeSize());

		assertEquals(1.0, metricsCalculator.calculateMarketPowerIndex(teams), 0.0001);
	}

	@Test
	public void shouldCountTeamsByMarketSize() {
		ArrayList<Team> teams = TestSupport.firstTeams(league, 4);
		teams.get(0).getTeamFinance().getStructure().setMarketSize(new SmallSize());
		teams.get(1).getTeamFinance().getStructure().setMarketSize(new SmallSize());
		teams.get(2).getTeamFinance().getStructure().setMarketSize(new MediumSize());
		teams.get(3).getTeamFinance().getStructure().setMarketSize(new LargeSize());

		assertEquals(2, metricsCalculator.countSmallMarketTeams(teams));
		assertEquals(1, metricsCalculator.countMediumMarketTeams(teams));
		assertEquals(1, metricsCalculator.countLargeMarketTeams(teams));
	}

	@Test
	public void shouldCalculateAverageMediaMarketMetrics() {
		ArrayList<Team> teams = TestSupport.firstTeams(league, 2);
		MediaMarket firstMediaMarket = teams.get(0).getTeamFinance().getStructure().getMediaMarket();
		firstMediaMarket.setFanBaseModifier(0.10);
		firstMediaMarket.setPrestigeModifier(0.20);
		firstMediaMarket.setPricingPowerModifier(0.30);
		MediaMarket secondMediaMarket = teams.get(1).getTeamFinance().getStructure().getMediaMarket();
		secondMediaMarket.setFanBaseModifier(0.30);
		secondMediaMarket.setPrestigeModifier(0.40);
		secondMediaMarket.setPricingPowerModifier(0.50);

		assertEquals(0.20, metricsCalculator.calculateAverageMediaFanBase(teams), 0.0001);
		assertEquals(0.30, metricsCalculator.calculateAverageMediaPrestige(teams), 0.0001);
		assertEquals(0.40, metricsCalculator.calculateAveragePricingPower(teams), 0.0001);
	}
}
