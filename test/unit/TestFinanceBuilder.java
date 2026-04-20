package unit;

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
public void shouldSetTeamFinanceForAllTeams() {
	for (Team team : league.getAllTeam()) {
		TeamFinance teamFinance = team.getTeamFinance();
		assertNotNull(teamFinance);
		assertNotNull(teamFinance.getBudget());
		assertNotNull(teamFinance.getEconomicProfil());
		assertNotNull(teamFinance.getMediaMarket());
		assertNotNull(teamFinance.getMarketSize());
		assertNotNull(teamFinance.getFinancialProfil());
	}
}

@Test
public void shouldBuildInitialBudgetForTeamFinance() {
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
public void shouldInitializeBudgetMonthlyMapsForEachFinancialMonth() {
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
public void shouldCalculateTeamValueDuringFinanceBuild() {
	for (Team team : league.getAllTeam()) {
		TeamFinance teamFinance = team.getTeamFinance();
		assertTrue(teamFinance.getTeamValue() > 0);
		assertTrue(teamFinance.getTeamValue() > teamFinance.getBudget().getRemainingAmount());
	}
}

@Test
public void shouldCalculateExpectedTeamValueFormulaDuringFinanceBuild() {
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
public void shouldInitializeStadiumCapacityAndTicketPrice() {
	for (Team team : league.getAllTeam()) {
		assertNotNull(team.getStadium());
		assertTrue(team.getStadium().getCapacity() > 0);
		assertTrue(team.getStadium().getTicketPrice() > 0);
	}
}

@Test
public void shouldSetStadiumCapacityWithinExpectedRangeForMarketSize() {
	for (Team team : league.getAllTeam()) {
		int capacity = team.getStadium().getCapacity();

		if (team.getTeamFinance().getMarketSize() instanceof LargeSize) {
			assertTrue(capacity >= 40000);
			assertTrue(capacity < 80000);
		} else if (team.getTeamFinance().getMarketSize() instanceof MediumSize) {
			assertTrue(capacity >= 15000);
			assertTrue(capacity < 35000);
		} else if (team.getTeamFinance().getMarketSize() instanceof SmallSize) {
			assertTrue(capacity >= 5000);
			assertTrue(capacity < 10000);
		} else {
			fail("Unknown market size for team " + team.getName());
		}
	}
}

@Test
public void shouldInitializeMediaMarketAndEconomicProfile() {
	for (Team team : league.getAllTeam()) {
		TeamFinance teamFinance = team.getTeamFinance();
		assertNotNull(teamFinance.getMediaMarket());
		assertNotNull(teamFinance.getEconomicProfil());

		assertTrue(teamFinance.getMediaMarket().getFanBaseModifier() > 0);
		assertTrue(teamFinance.getMediaMarket().getBusinessOpportunityModifier() > 0);
		assertTrue(teamFinance.getMediaMarket().getPrestigeModifier() > 0);
		assertTrue(teamFinance.getMediaMarket().getPricingPowerModifier() > 0);

		assertTrue(teamFinance.getEconomicProfil().getFanLoyalty() > 0);
		assertTrue(teamFinance.getEconomicProfil().getFanLoyalty() < 1);
		assertTrue(teamFinance.getEconomicProfil().getPriceElasticity() > 0);
		assertTrue(teamFinance.getEconomicProfil().getPriceElasticity() < 1);
		assertTrue(teamFinance.getEconomicProfil().getCommercialAggressiveness() > 0);
		assertTrue(teamFinance.getEconomicProfil().getCommercialAggressiveness() < 1);
		assertTrue(teamFinance.getEconomicProfil().getHistoricalPrestige() > 0);
		assertTrue(teamFinance.getEconomicProfil().getHistoricalPrestige() < 1);
		assertTrue(teamFinance.getEconomicProfil().getOwnerDeficitTolerance() > 0);
		assertTrue(teamFinance.getEconomicProfil().getOwnerDeficitTolerance() < 1);
	}
}

@Test
public void shouldSetMediaMarketModifiersWithExpectedRelationships() {
	for (Team team : league.getAllTeam()) {
		double fanBaseModifier = team.getTeamFinance().getMediaMarket().getFanBaseModifier();
		double businessOpportunityModifier = team.getTeamFinance().getMediaMarket().getBusinessOpportunityModifier();
		double prestigeModifier = team.getTeamFinance().getMediaMarket().getPrestigeModifier();
		double pricingPowerModifier = team.getTeamFinance().getMediaMarket().getPricingPowerModifier();

		assertEquals(fanBaseModifier, businessOpportunityModifier, 0.0001);
		assertEquals(fanBaseModifier, pricingPowerModifier, 0.0001);
		assertEquals(fanBaseModifier * 0.6, prestigeModifier, 0.0001);
	}
}

@Test
public void shouldCalculateExpectedEconomicProfileValues() {
	for (Team team : league.getAllTeam()) {
		double popularity = team.getFormerPopularity();
		double expectedHistoricalPrestigeMin = 0.2;
		double expectedHistoricalPrestigeMax = interval(0.2 + popularity / 100.0 * 0.5);

		double expectedFanLoyaltyMin = interval(0.3 + popularity / 100.0 * 0.3 + expectedHistoricalPrestigeMin * 0.2);
		double expectedFanLoyaltyMax = interval(0.3 + popularity / 100.0 * 0.3 + expectedHistoricalPrestigeMax * 0.2);

		double expectedPriceElasticityMin = interval(
			0.8 - expectedFanLoyaltyMax * 0.3 - expectedHistoricalPrestigeMax * 0.2);
		double expectedPriceElasticityMax = interval(
			0.8 - expectedFanLoyaltyMin * 0.3 - expectedHistoricalPrestigeMin * 0.2);

		double expectedCommercialAggressivenessMin = 0.4;
		double expectedCommercialAggressivenessMax = interval(0.4
			+ getExpectedBusinessOpportunityModifierMax(team));

		double expectedOwnerDeficitToleranceMin = 0.6;
		double expectedOwnerDeficitToleranceMax = 0.6;

		assertTrue(team.getTeamFinance().getEconomicProfil().getHistoricalPrestige() >= expectedHistoricalPrestigeMin);
		assertTrue(team.getTeamFinance().getEconomicProfil().getHistoricalPrestige() <= expectedHistoricalPrestigeMax);

		assertTrue(team.getTeamFinance().getEconomicProfil().getFanLoyalty() >= expectedFanLoyaltyMin);
		assertTrue(team.getTeamFinance().getEconomicProfil().getFanLoyalty() <= expectedFanLoyaltyMax);

		assertTrue(team.getTeamFinance().getEconomicProfil().getPriceElasticity() >= expectedPriceElasticityMin);
		assertTrue(team.getTeamFinance().getEconomicProfil().getPriceElasticity() <= expectedPriceElasticityMax);

		assertTrue(team.getTeamFinance().getEconomicProfil()
			.getCommercialAggressiveness() >= expectedCommercialAggressivenessMin);
		assertTrue(team.getTeamFinance().getEconomicProfil()
			.getCommercialAggressiveness() <= expectedCommercialAggressivenessMax);

		assertEquals(expectedOwnerDeficitToleranceMin,
			team.getTeamFinance().getEconomicProfil().getOwnerDeficitTolerance(), 0.0001);
	}
}

@Test
public void shouldKeepBudgetWithinExpectedRangeForTeamMarketSize() {
	for (Team team : league.getAllTeam()) {
		double popularity = team.getFormerPopularity();
		double initialBudget = team.getTeamFinance().getBudget().getInitialAmount();
		double baseBudget = getExpectedBaseBudget(popularity);
		double marketMultiplier = getExpectedBudgetMarketMultiplier(team);
		double popularityFactor = 0.85 + popularity / 100.0 * 0.3;
		double prestigeFactor = 1.0;
		double ownerFactor = 1.06;
		double mediaFactor = 1.25;

		double commercialFactorMin = 0.9 + 0.4 * 0.2;
		double commercialFactorMax = 0.9 + getExpectedCommercialAggressivenessMax(team) * 0.2;

		double expectedMinBudget = baseBudget * marketMultiplier * popularityFactor * prestigeFactor
			* mediaFactor * commercialFactorMin * ownerFactor;
		double expectedMaxBudget = baseBudget * marketMultiplier * popularityFactor * prestigeFactor
			* mediaFactor * commercialFactorMax * ownerFactor;

		assertTrue(initialBudget >= expectedMinBudget);
		assertTrue(initialBudget <= expectedMaxBudget);
	}
}

@Test
public void shouldSetTicketPriceAccordingToMarketSize() {
	for (Team team : league.getAllTeam()) {
		double ticketPrice = team.getStadium().getTicketPrice();

		if (team.getTeamFinance().getMarketSize() instanceof LargeSize) {
			assertEquals(58.5, ticketPrice, 0.001);
		} else if (team.getTeamFinance().getMarketSize() instanceof MediumSize) {
			assertEquals(45.0, ticketPrice, 0.001);
		} else if (team.getTeamFinance().getMarketSize() instanceof SmallSize) {
			assertEquals(31.5, ticketPrice, 0.001);
		}
	}
}

@Test
public void shouldSetTicketPriceFromFinanceConfigurationMultipliers() {
	for (Team team : league.getAllTeam()) {
		double ticketPrice = team.getStadium().getTicketPrice();

		if (team.getTeamFinance().getMarketSize() instanceof LargeSize) {
			assertEquals(FinanceConfiguration.BASE_TICKET_PRICE * FinanceConfiguration.MARKET_SIZE_LARGE_MULTIPLIER,
				ticketPrice, 0.001);
		} else if (team.getTeamFinance().getMarketSize() instanceof MediumSize) {
			assertEquals(FinanceConfiguration.BASE_TICKET_PRICE * FinanceConfiguration.MARKET_SIZE_MEDIUM_MULTIPLIER,
				ticketPrice, 0.001);
		} else if (team.getTeamFinance().getMarketSize() instanceof SmallSize) {
			assertEquals(FinanceConfiguration.BASE_TICKET_PRICE * FinanceConfiguration.MARKET_SIZE_SMALL_MULTIPLIER,
				ticketPrice, 0.001);
		}
	}
}

private double getExpectedMarketValueBonus(Team team) {
	if (team.getTeamFinance().getMarketSize() instanceof LargeSize) {
		return 140.0;
	}
	if (team.getTeamFinance().getMarketSize() instanceof MediumSize) {
		return 80.0;
	}
	if (team.getTeamFinance().getMarketSize() instanceof SmallSize) {
		return 40.0;
	}
	return 0.0;
}

private double getExpectedBusinessOpportunityModifierMax(Team team) {
	if (team.getTeamFinance().getMarketSize() instanceof LargeSize) {
		return 0.2;
	}
	if (team.getTeamFinance().getMarketSize() instanceof MediumSize) {
		return 0.1;
	}
	if (team.getTeamFinance().getMarketSize() instanceof SmallSize) {
		return 0.05;
	}
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
	if (team.getTeamFinance().getMarketSize() instanceof LargeSize) {
		return 1.2;
	}
	if (team.getTeamFinance().getMarketSize() instanceof MediumSize) {
		return 1.0;
	}
	if (team.getTeamFinance().getMarketSize() instanceof SmallSize) {
		return 0.8;
	}
	return 0.0;
}

private double getExpectedCommercialAggressivenessMax(Team team) {
	if (team.getTeamFinance().getMarketSize() instanceof LargeSize) {
		return 0.6;
	}
	if (team.getTeamFinance().getMarketSize() instanceof MediumSize) {
		return 0.5;
	}
	if (team.getTeamFinance().getMarketSize() instanceof SmallSize) {
		return 0.45;
	}
	return 0.0;
}

private double interval(double value) {
	return Math.max(0.0, Math.min(1.0, value));
}
}
