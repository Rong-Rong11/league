package test.unit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import config.FinanceConfiguration;
import data.league.League;
import data.team.Team;
import data.team.finance.TeamFinance;
import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;
import process.builder.league.LeagueBuilder;
import process.repository.DivisionRepository;
import process.repository.PlayerRepository;
import process.repository.TeamRepository;
import process.service.finance.initialization.FinanceInitializer;

public class TestFinanceBuilder {

private League league;

@Before
public void setUp() {
	PlayerRepository.getInstance().clear();
	TeamRepository.getInstance().clear();
	DivisionRepository.getInstance().clear();
	league = new LeagueBuilder().build();
	new FinanceInitializer().initializeFinance();
}

@Test
public void shouldSetFinanceForAllTeams() {
	for (Team team : league.getAllTeam()) {
		TeamFinance teamFinance = team.getTeamFinance();
		assertNotNull(teamFinance);
		assertNotNull(teamFinance.getBudget());
		assertNotNull(teamFinance.getStructure().getEconomicProfil());
		assertNotNull(teamFinance.getStructure().getMediaMarket());
		assertNotNull(teamFinance.getStructure().getMarketSize());
		assertNotNull(teamFinance.getBehavior().getFinancialProfil());
	}
}

@Test
public void shouldBuildInitialBudget() {
	for (Team team : league.getAllTeam()) {
		TeamFinance teamFinance = team.getTeamFinance();
		assertNotNull(teamFinance);
		assertNotNull(teamFinance.getBudget());
		assertTrue(teamFinance.getBudget().getInitialAmount() > 0);
		assertEquals(teamFinance.getBudget().getInitialAmount(),
			teamFinance.getBudget().getRemainingAmount(), 0.001);
		assertNotNull(teamFinance.getBudget().getMonthlyIncomes());
		assertNotNull(teamFinance.getBudget().getMonthlyExpenses());
	}
}

@Test
public void shouldInitMonthlyBudgetMaps() {
	for (Team team : league.getAllTeam()) {
		assertEquals(FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS,
			team.getTeamFinance().getBudget().getMonthlyIncomes().size());
		assertEquals(FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS,
			team.getTeamFinance().getBudget().getMonthlyExpenses().size());

		for (int month = 0; month < FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS; month++) {
			assertTrue(team.getTeamFinance().getBudget().getMonthlyIncomes().containsKey(month));
			assertTrue(team.getTeamFinance().getBudget().getMonthlyExpenses().containsKey(month));
			assertNotNull(team.getTeamFinance().getBudget().getIncomesForMonth(month));
			assertNotNull(team.getTeamFinance().getBudget().getExpensesForMonth(month));
			assertTrue(team.getTeamFinance().getBudget().getIncomesForMonth(month).isEmpty());
			assertTrue(team.getTeamFinance().getBudget().getExpensesForMonth(month).isEmpty());
		}
	}
}

@Test
public void shouldSetTeamValue() {
	for (Team team : league.getAllTeam()) {
		TeamFinance teamFinance = team.getTeamFinance();
		assertTrue(teamFinance.getTeamValue() > 0);
		assertTrue(teamFinance.getTeamValue() > teamFinance.getBudget().getRemainingAmount());
	}
}

@Test
public void shouldMatchTeamValueFormula() {
	for (Team team : league.getAllTeam()) {
		double expectedTeamValue = 250.0
			+ team.getTeamFinance().getBudget().getRemainingAmount()
			+ (team.getFormerPopularity() * 2.0)
			+ getExpectedMarketValueBonus(team)
			+ 25.0;

		assertEquals(expectedTeamValue, team.getTeamFinance().getTeamValue(), 0.001);
	}
}

@Test
public void shouldInitStadiumCapacityAndTicketPrice() {
	for (Team team : league.getAllTeam()) {
		assertNotNull(team.getStadium());
		assertTrue(team.getStadium().getCapacity() > 0);
		assertTrue(team.getStadium().getTicketPrice() > 0);
	}
}

@Test
public void shouldKeepStadiumCapacityInRange() {
	for (Team team : league.getAllTeam()) {
		int capacity = team.getStadium().getCapacity();

		if (team.getTeamFinance().getStructure().getMarketSize() instanceof LargeSize) {
			assertTrue(capacity >= 40000);
			assertTrue(capacity < 80000);
		} else if (team.getTeamFinance().getStructure().getMarketSize() instanceof MediumSize) {
			assertTrue(capacity >= 15000);
			assertTrue(capacity < 35000);
		} else if (team.getTeamFinance().getStructure().getMarketSize() instanceof SmallSize) {
			assertTrue(capacity >= 5000);
			assertTrue(capacity < 10000);
		} else {
			fail("Unknown market size for team " + team.getName());
		}
	}
}

@Test
public void shouldInitMediaMarketAndEconomicProfile() {
	for (Team team : league.getAllTeam()) {
		TeamFinance teamFinance = team.getTeamFinance();
		assertNotNull(teamFinance.getStructure().getMediaMarket());
		assertNotNull(teamFinance.getStructure().getEconomicProfil());

		assertTrue(teamFinance.getStructure().getMediaMarket().getFanBaseModifier() > 0);
		assertTrue(teamFinance.getStructure().getMediaMarket().getBusinessOpportunityModifier() > 0);
		assertTrue(teamFinance.getStructure().getMediaMarket().getPrestigeModifier() > 0);
		assertTrue(teamFinance.getStructure().getMediaMarket().getPricingPowerModifier() > 0);

		assertTrue(teamFinance.getStructure().getEconomicProfil().getFanLoyalty() > 0);
		assertTrue(teamFinance.getStructure().getEconomicProfil().getFanLoyalty() <= 1);
		assertTrue(teamFinance.getStructure().getEconomicProfil().getPriceElasticity() > 0);
		assertTrue(teamFinance.getStructure().getEconomicProfil().getPriceElasticity() <= 1);
		assertTrue(teamFinance.getStructure().getEconomicProfil().getCommercialAggressiveness() > 0);
		assertTrue(teamFinance.getStructure().getEconomicProfil().getCommercialAggressiveness() <= 1);
		assertTrue(teamFinance.getStructure().getEconomicProfil().getHistoricalPrestige() > 0);
		assertTrue(teamFinance.getStructure().getEconomicProfil().getHistoricalPrestige() <= 1);
		assertTrue(teamFinance.getStructure().getEconomicProfil().getOwnerDeficitTolerance() > 0);
		assertTrue(teamFinance.getStructure().getEconomicProfil().getOwnerDeficitTolerance() <= 1);
	}
}

@Test
public void shouldMatchMediaMarketRelationships() {
	for (Team team : league.getAllTeam()) {
		double fanBaseModifier = team.getTeamFinance().getStructure().getMediaMarket().getFanBaseModifier();
		double businessOpportunityModifier = team.getTeamFinance().getStructure().getMediaMarket().getBusinessOpportunityModifier();
		double prestigeModifier = team.getTeamFinance().getStructure().getMediaMarket().getPrestigeModifier();
		double pricingPowerModifier = team.getTeamFinance().getStructure().getMediaMarket().getPricingPowerModifier();

		assertEquals(fanBaseModifier, businessOpportunityModifier, 0.0001);
		assertEquals(fanBaseModifier, pricingPowerModifier, 0.0001);
		assertEquals(fanBaseModifier * 0.6, prestigeModifier, 0.0001);
	}
}

@Test
public void shouldKeepEconomicProfileValuesInRange() {
	for (Team team : league.getAllTeam()) {
		double popularity = team.getFormerPopularity();
		double expectedHistoricalPrestigeMin = interval(0.25 + popularity / 100.0 * 0.6);
		double expectedHistoricalPrestigeMax = expectedHistoricalPrestigeMin;

		double expectedFanLoyaltyMin = interval(0.35 + popularity / 100.0 * 0.35 + expectedHistoricalPrestigeMin * 0.25);
		double expectedFanLoyaltyMax = expectedFanLoyaltyMin;

		double expectedPriceElasticityMin = interval(
			0.78 - expectedFanLoyaltyMax * 0.32 - expectedHistoricalPrestigeMax * 0.22);
		double expectedPriceElasticityMax = expectedPriceElasticityMin;

		double expectedCommercialAggressivenessMin = interval(0.45
			+ getExpectedBusinessOpportunityModifierMin(team) * 1.15);
		double expectedCommercialAggressivenessMax = interval(0.45
			+ getExpectedBusinessOpportunityModifierMax(team) * 1.15);

		double expectedOwnerDeficitToleranceMin = 0.6;

		assertTrue(team.getTeamFinance().getStructure().getEconomicProfil().getHistoricalPrestige() >= expectedHistoricalPrestigeMin);
		assertTrue(team.getTeamFinance().getStructure().getEconomicProfil().getHistoricalPrestige() <= expectedHistoricalPrestigeMax);

		assertTrue(team.getTeamFinance().getStructure().getEconomicProfil().getFanLoyalty() >= expectedFanLoyaltyMin);
		assertTrue(team.getTeamFinance().getStructure().getEconomicProfil().getFanLoyalty() <= expectedFanLoyaltyMax);

		assertTrue(team.getTeamFinance().getStructure().getEconomicProfil().getPriceElasticity() >= expectedPriceElasticityMin);
		assertTrue(team.getTeamFinance().getStructure().getEconomicProfil().getPriceElasticity() <= expectedPriceElasticityMax);

		assertTrue(team.getTeamFinance().getStructure().getEconomicProfil()
			.getCommercialAggressiveness() >= expectedCommercialAggressivenessMin);
		assertTrue(team.getTeamFinance().getStructure().getEconomicProfil()
			.getCommercialAggressiveness() <= expectedCommercialAggressivenessMax);

		assertEquals(expectedOwnerDeficitToleranceMin,
			team.getTeamFinance().getStructure().getEconomicProfil().getOwnerDeficitTolerance(), 0.0001);
	}
}

@Test
public void shouldKeepBudgetInExpectedRange() {
	for (Team team : league.getAllTeam()) {
		double popularity = team.getFormerPopularity();
		double initialBudget = team.getTeamFinance().getBudget().getInitialAmount();
		double baseBudget = getExpectedBaseBudget(popularity);
		double marketMultiplier = getExpectedBudgetMarketMultiplier(team);
		double popularityFactor = 0.85 + popularity / 100.0 * 0.3;
		double prestigeFactorMin = 0.85 + team.getTeamFinance().getStructure().getEconomicProfil().getHistoricalPrestige() * 0.3;
		double prestigeFactorMax = prestigeFactorMin;
		double ownerFactor = 0.7 + team.getTeamFinance().getStructure().getEconomicProfil().getOwnerDeficitTolerance() * 0.6;
		double mediaFactor = 1.25;

		double commercialFactorMin = 0.9 + getExpectedCommercialAggressivenessMin(team) * 0.2;
		double commercialFactorMax = 0.9 + getExpectedCommercialAggressivenessMax(team) * 0.2;

		double expectedMinBudget = baseBudget * marketMultiplier * popularityFactor * prestigeFactorMin
			* mediaFactor * commercialFactorMin * ownerFactor;
		double expectedMaxBudget = baseBudget * marketMultiplier * popularityFactor * prestigeFactorMax
			* mediaFactor * commercialFactorMax * ownerFactor;

		assertTrue(initialBudget >= expectedMinBudget);
		assertTrue(initialBudget <= expectedMaxBudget);
	}
}

@Test
public void shouldSetTicketPriceByMarketSize() {
	for (Team team : league.getAllTeam()) {
		double ticketPrice = team.getStadium().getTicketPrice();

		if (team.getTeamFinance().getStructure().getMarketSize() instanceof LargeSize) {
			assertEquals(58.5, ticketPrice, 0.001);
		} else if (team.getTeamFinance().getStructure().getMarketSize() instanceof MediumSize) {
			assertEquals(45.0, ticketPrice, 0.001);
		} else if (team.getTeamFinance().getStructure().getMarketSize() instanceof SmallSize) {
			assertEquals(45.0, ticketPrice, 0.001);
		}
	}
}

@Test
public void shouldSetTicketPriceFromConfigMultipliers() {
	for (Team team : league.getAllTeam()) {
		double ticketPrice = team.getStadium().getTicketPrice();

		if (team.getTeamFinance().getStructure().getMarketSize() instanceof LargeSize) {
			assertEquals(FinanceConfiguration.BASE_TICKET_PRICE * FinanceConfiguration.MARKET_SIZE_LARGE_MULTIPLIER,
				ticketPrice, 0.001);
		} else if (team.getTeamFinance().getStructure().getMarketSize() instanceof MediumSize) {
			assertEquals(FinanceConfiguration.BASE_TICKET_PRICE * FinanceConfiguration.MARKET_SIZE_MEDIUM_MULTIPLIER,
				ticketPrice, 0.001);
		} else if (team.getTeamFinance().getStructure().getMarketSize() instanceof SmallSize) {
			assertEquals(FinanceConfiguration.BASE_TICKET_PRICE * FinanceConfiguration.MARKET_SIZE_SMALL_MULTIPLIER,
				ticketPrice, 0.001);
		}
	}
}

private double getExpectedMarketValueBonus(Team team) {
	if (team.getTeamFinance().getStructure().getMarketSize() instanceof LargeSize) {
		return 140.0;
	}
	if (team.getTeamFinance().getStructure().getMarketSize() instanceof MediumSize) {
		return 80.0;
	}
	if (team.getTeamFinance().getStructure().getMarketSize() instanceof SmallSize) {
		return 40.0;
	}
	return 0.0;
}

private double getExpectedBusinessOpportunityModifierMax(Team team) {
	if (team.getTeamFinance().getStructure().getMarketSize() instanceof LargeSize) {
		return 0.56;
	}
	if (team.getTeamFinance().getStructure().getMarketSize() instanceof MediumSize) {
		return 0.40;
	}
	if (team.getTeamFinance().getStructure().getMarketSize() instanceof SmallSize) {
		return 0.24;
	}
	return 0.0;
}

private double getExpectedBusinessOpportunityModifierMin(Team team) {
	return 0.0;
}

private double getExpectedBaseBudget(double popularity) {
	double baseBudget = FinanceConfiguration.BASE_TEAM_BUDGET;

	if (popularity <= 70) {
		return baseBudget * 1.1;
	} else if (popularity <= 80) {
		return baseBudget * 1.3;
	} else if (popularity <= 90) {
		return baseBudget * 1.45;
	}
	return baseBudget * 1.6;
}

private double getExpectedBudgetMarketMultiplier(Team team) {
	if (team.getTeamFinance().getStructure().getMarketSize() instanceof LargeSize) {
		return 1.6;
	}
	if (team.getTeamFinance().getStructure().getMarketSize() instanceof MediumSize) {
		return 1.4;
	}
	if (team.getTeamFinance().getStructure().getMarketSize() instanceof SmallSize) {
		return 1.1;
	}
	return 0.0;
}

private double getExpectedCommercialAggressivenessMax(Team team) {
	return interval(0.45 + getExpectedBusinessOpportunityModifierMax(team) * 1.15);
}

private double getExpectedCommercialAggressivenessMin(Team team) {
	return interval(0.45 + getExpectedBusinessOpportunityModifierMin(team) * 1.15);
}

private double interval(double value) {
	return Math.max(0.0, Math.min(1.0, value));
}
}
