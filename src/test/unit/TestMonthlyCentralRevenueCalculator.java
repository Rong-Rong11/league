package test.unit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import data.league.League;
import data.league.finance.CentralRevenueProfile;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.marketsize.SmallSize;
import data.team.finance.mediamarket.MediaMarket;
import process.service.finance.distribution.central.calculation.MonthlyCentralRevenueCalculator;
import test.support.TestSupport;

public class TestMonthlyCentralRevenueCalculator {

	private League league;
	private MonthlyCentralRevenueCalculator calculator;
	private CentralRevenueProfile neutralProfile;

	@Before
	public void setUp() {
		league = TestSupport.buildLeagueWithFinance();
		calculator = new MonthlyCentralRevenueCalculator(league);
		neutralProfile = new CentralRevenueProfile(1.0, 1.0, 1.0);
	}

	@Test
	public void shouldIncreaseTvRevenueWithStrongerLeagueMarketEconomicAndMediaProfile() {
		applyLeagueProfile(new SmallSize(), 0.25, 0.25, 0.25, 0.08, 0.06, 0.08, 0.08);
		double lowRevenue = calculator.calculateNationalTvRevenue(neutralProfile, 2);

		applyLeagueProfile(new LargeSize(), 0.75, 0.75, 0.75, 0.38, 0.24, 0.38, 0.38);
		double highRevenue = calculator.calculateNationalTvRevenue(neutralProfile, 2);

		assertTrue(highRevenue > lowRevenue);
	}

	@Test
	public void shouldIncreaseSponsoringRevenueWithStrongerLeagueMarketEconomicAndMediaProfile() {
		applyLeagueProfile(new SmallSize(), 0.25, 0.25, 0.25, 0.08, 0.06, 0.08, 0.08);
		double lowRevenue = calculator.calculateNationalSponsoringRevenue(neutralProfile, 2);

		applyLeagueProfile(new LargeSize(), 0.75, 0.75, 0.75, 0.38, 0.24, 0.38, 0.38);
		double highRevenue = calculator.calculateNationalSponsoringRevenue(neutralProfile, 2);

		assertTrue(highRevenue > lowRevenue);
	}

	@Test
	public void shouldIncreaseMerchandisingRevenueWithStrongerLeagueMarketEconomicAndMediaProfile() {
		applyLeagueProfile(new SmallSize(), 0.25, 0.25, 0.25, 0.08, 0.06, 0.08, 0.08);
		double lowRevenue = calculator.calculateNationalMerchandisingRevenue(neutralProfile, 2);

		applyLeagueProfile(new LargeSize(), 0.75, 0.75, 0.75, 0.38, 0.24, 0.38, 0.38);
		double highRevenue = calculator.calculateNationalMerchandisingRevenue(neutralProfile, 2);

		assertTrue(highRevenue > lowRevenue);
	}

	private void applyLeagueProfile(MarketSize marketSize, double fanLoyalty,
			double commercialAggressiveness, double historicalPrestige, double fanBaseModifier,
			double mediaPrestigeModifier, double businessOpportunityModifier, double pricingPowerModifier) {
		for (Team team : league.getAllTeam()) {
			team.getTeamFinance().getStructure().setMarketSize(marketSize);
			EconomicProfil economicProfil = team.getTeamFinance().getStructure().getEconomicProfil();
			economicProfil.setFanLoyalty(fanLoyalty);
			economicProfil.setCommercialAggressiveness(commercialAggressiveness);
			economicProfil.setHistoricalPrestige(historicalPrestige);

			MediaMarket mediaMarket = team.getTeamFinance().getStructure().getMediaMarket();
			mediaMarket.setFanBaseModifier(fanBaseModifier);
			mediaMarket.setPrestigeModifier(mediaPrestigeModifier);
			mediaMarket.setBusinessOpportunityModifier(businessOpportunityModifier);
			mediaMarket.setPricingPowerModifier(pricingPowerModifier);
		}
	}
}
