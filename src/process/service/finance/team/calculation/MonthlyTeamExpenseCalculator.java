package process.service.finance.team.calculation;

import org.apache.log4j.Logger;

import config.FinanceConfiguration;
import data.league.League;
import data.team.Team;
import data.team.finance.MonthlyTeamExpense;
import data.team.finance.TeamFinance;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.financialpolicy.FinancialPolicy;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import log.LoggerUtility;
import process.utility.FinanceUtility;
import process.visitor.financialprofil.AdministrativeCostMultiplierVisitor;
import process.visitor.financialprofil.MaintenanceCostMultiplierVisitor;
import process.visitor.financialprofil.StaffCostMultiplierVisitor;

public class MonthlyTeamExpenseCalculator {
	private static final Logger logger = LoggerUtility.getLogger(MonthlyTeamExpenseCalculator.class, "text");

	private League league;
	private TeamFinanceRateCalculator rateCalculator;

	public MonthlyTeamExpenseCalculator(League league, TeamFinanceRateCalculator rateCalculator) {
		this.league = league;
		this.rateCalculator = rateCalculator;
		logger.debug("Monthly team expense calculator initialized");
	}

	public MonthlyTeamExpense calculateExpense(Team team, int month, boolean fixedCostsOnly) {
		if (team == null || team.getTeamFinance() == null) {
			logger.warn("Skipping monthly team expense calculation because team or team finance is null");
			return new MonthlyTeamExpense(0.0, 0.0, 0.0, 0.0, 0.0);
		}
		logger.debug("Calculating monthly team expense for "
				+ team.getName()
				+ " month "
				+ month
				+ " fixedCostsOnly="
				+ fixedCostsOnly);
		TeamFinance teamFinance = team.getTeamFinance();
		MarketSize marketSize = teamFinance.getStructure().getMarketSize();
		MediaMarket mediaMarket = teamFinance.getStructure().getMediaMarket();
		EconomicProfil economicProfil = teamFinance.getStructure().getEconomicProfil();
		FinancialPolicy financialPolicy = teamFinance.getBehavior().getFinancialProfil();

		double marketMultiplier = rateCalculator.getMarketMultiplier(marketSize);
		double monthlyPayroll = team.getTeamFinance().getCurrentPayroll()
				/ FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS;
		double monthlyLuxuryTax = calculateMonthlyLuxuryTax(teamFinance);
		double stadiumMaintenance = calculateStadiumMaintenance(team, marketMultiplier, mediaMarket,
				economicProfil, financialPolicy);
		double staffCost = calculateStaffCost(team, marketMultiplier, economicProfil, financialPolicy);
		double administrativeCost = calculateAdministrativeCost(team, marketMultiplier, mediaMarket,
				economicProfil,
				financialPolicy);
		double seasonExpenseMultiplier = rateCalculator.getSeasonContextExpenseMultiplier(month);
		logger.trace("Base monthly expenses for "
				+ team.getName()
				+ ": payroll="
				+ monthlyPayroll
				+ ", luxuryTax="
				+ monthlyLuxuryTax
				+ ", stadiumMaintenance="
				+ stadiumMaintenance
				+ ", staff="
				+ staffCost
				+ ", administrative="
				+ administrativeCost
				+ ", seasonMultiplier="
				+ seasonExpenseMultiplier);

		stadiumMaintenance *= rateCalculator.getMonthlyExpenseRate(team, month, 0.180, 0.100);
		if (fixedCostsOnly) {
			staffCost *= rateCalculator.getMonthlyExpenseRate(team, month, 0.120, 0.070);
		} else {
			staffCost *= rateCalculator.getMonthlyExpenseRate(team, month, 0.090, 0.050);
		}
		administrativeCost *= rateCalculator.getMonthlyExpenseRate(team, month, 0.200, 0.120);

		stadiumMaintenance *= seasonExpenseMultiplier;
		staffCost *= seasonExpenseMultiplier;
		administrativeCost *= seasonExpenseMultiplier;
		logger.debug("Calculated monthly team expense for "
				+ team.getName()
				+ ": payroll="
				+ monthlyPayroll
				+ ", luxuryTax="
				+ monthlyLuxuryTax
				+ ", stadiumMaintenance="
				+ stadiumMaintenance
				+ ", staff="
				+ staffCost
				+ ", administrative="
				+ administrativeCost);

		return new MonthlyTeamExpense(monthlyPayroll, monthlyLuxuryTax, stadiumMaintenance, staffCost,
				administrativeCost);
	}

	private double calculateMonthlyLuxuryTax(TeamFinance teamFinance) {
		double luxuryTaxLine = league.getLeagueFinance().getLeagueFinancialRules().getLuxuryTaxLine();
		double seasonLuxuryTax = FinanceUtility.luxuryTaxPenalty(teamFinance.getCurrentPayroll(),
				luxuryTaxLine);
		double monthlyLuxuryTax = seasonLuxuryTax / FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS;
		logger.trace("Calculated monthly luxury tax " + monthlyLuxuryTax + " from season luxury tax " + seasonLuxuryTax);
		return monthlyLuxuryTax;
	}

	private double calculateStadiumMaintenance(Team team, double marketMultiplier, MediaMarket mediaMarket,
			EconomicProfil economicProfil, FinancialPolicy financialPolicy) {
		double capacityFactor = team.getStadium().getCapacity() / 20000.0;
		double maintenance = 3 * marketMultiplier * capacityFactor;
		logger.trace("Base stadium maintenance for " + team.getName() + " is " + maintenance);

		maintenance *= (1 + mediaMarket.getBusinessOpportunityModifier() * 0.26);
		maintenance *= (1 + economicProfil.getHistoricalPrestige() * 0.15);
		maintenance *= 1.15;
		maintenance *= financialPolicy.accept(new MaintenanceCostMultiplierVisitor());
		maintenance *= rateCalculator.getSmallMarketCostFactor(team.getTeamFinance().getStructure().getMarketSize(),
				0.88);

		logger.trace("Calculated stadium maintenance " + maintenance + " for " + team.getName());
		return maintenance;
	}

	private double calculateStaffCost(Team team, double marketMultiplier, EconomicProfil economicProfil,
			FinancialPolicy financialPolicy) {
		int numberOfPlayers = team.getCurrentPlayers().size();
		double popularityFactor = (team.getCurrentPopularity() / 100.0);
		double staffCost = ((0.06 * numberOfPlayers) + 6) * marketMultiplier * popularityFactor;
		logger.trace("Base staff cost for "
				+ team.getName()
				+ " is "
				+ staffCost
				+ " with "
				+ numberOfPlayers
				+ " players");

		staffCost *= (1 + economicProfil.getFanLoyalty() * 0.23);
		staffCost *= (1 + economicProfil.getCommercialAggressiveness() * 0.1);
		staffCost *= rateCalculator.getSmallMarketCostFactor(team.getTeamFinance().getStructure().getMarketSize(), 0.75);
		if (team.hasStarPlayer()) {
			logger.trace("Applying star player staff cost multiplier for " + team.getName());
			staffCost *= 1.10;
		}
		staffCost *= financialPolicy.accept(new StaffCostMultiplierVisitor());
		staffCost *= rateCalculator.getOperationalPressureMultiplier(team);

		logger.trace("Calculated staff cost " + staffCost + " for " + team.getName());
		return staffCost;
	}

	private double calculateAdministrativeCost(Team team, double marketMultiplier, MediaMarket mediaMarket,
			EconomicProfil economicProfil, FinancialPolicy financialPolicy) {
		double administrativeCost = 3 * (1 + marketMultiplier);
		logger.trace("Base administrative cost for " + team.getName() + " is " + administrativeCost);

		administrativeCost *= (1 + mediaMarket.getBusinessOpportunityModifier() * 0.10);
		administrativeCost *= (1 + economicProfil.getCommercialAggressiveness() * 0.10);
		administrativeCost *= 1.10;
		administrativeCost *= financialPolicy.accept(new AdministrativeCostMultiplierVisitor());
		administrativeCost *= rateCalculator
				.getSmallMarketCostFactor(team.getTeamFinance().getStructure().getMarketSize(), 0.90);
		administrativeCost *= rateCalculator.getOperationalPressureMultiplier(team);

		logger.trace("Calculated administrative cost " + administrativeCost + " for " + team.getName());
		return administrativeCost;
	}
}
