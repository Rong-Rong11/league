package process.service.finance.team;

import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.finance.budget.expense.Expense;
import data.finance.budget.expense.ExpenseType;
import data.finance.budget.income.Income;
import data.finance.budget.income.IncomeType;
import data.league.League;
import data.team.Team;
import data.team.finance.TeamFinance;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.financialpolicy.FinancialPolicy;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import process.utility.CalendarUtility;
import process.utility.FinanceUtility;
import process.visitor.financialprofil.AdministrativeCostMultiplierVisitor;
import process.visitor.financialprofil.MaintenanceCostMultiplierVisitor;
import process.visitor.financialprofil.StaffCostMultiplierVisitor;
import process.visitor.marketsize.CalculateMonthlyTeamFinanceVisitor;

public abstract class AbstractMonthlyTeamFinanceCalculator {
	private League league;

	public AbstractMonthlyTeamFinanceCalculator(League league) {
		this.league = league;
	}

	public void applyMonthlyFinance(Team team, int month) {
		Budget budget = team.getTeamFinance().getBudget();
		TeamFinance teamFinance = team.getTeamFinance();
		MarketSize marketSize = teamFinance.getMarketSize();
		MediaMarket mediaMarket = teamFinance.getMediaMarket();
		EconomicProfil economicProfil = teamFinance.getEconomicProfil();
		FinancialPolicy financialPolicy = teamFinance.getFinancialProfil();
		double teamValueFactor = FinanceUtility.getNormalizedTeamValue(team);

		double marketMultiplier = getMarketMultiplier(marketSize);
		double popularityFactor = team.getCurrentPopularity() / 100.0;
		double starFactor = team.hasStarPlayer() ? 1.18 : 1.0;
		double performanceFactor = 0.82 + (team.getTeamPerformance().getPerformanceRating() * 0.38);

		double localSponsoring = 1.78 * marketMultiplier * popularityFactor * starFactor;
		double localMerchandising = 1.22 * marketMultiplier * popularityFactor * starFactor;
		double otherRevenue = 0.40 * marketMultiplier * performanceFactor;

		localSponsoring *= (0.92 + team.getTeamPerformance().getPerformanceRating() * 0.16);
		localMerchandising *= (0.88 + team.getTeamPerformance().getPerformanceRating() * 0.24);

		localSponsoring *= (1 + mediaMarket.getBusinessOpportunityModifier() * 0.35);
		localMerchandising *= (1 + mediaMarket.getPrestigeModifier() * 0.20);
		otherRevenue *= (1 + mediaMarket.getBusinessOpportunityModifier() * 0.10);

		localSponsoring *= (1 + economicProfil.getCommercialAggressiveness() * 0.30);
		localSponsoring *= (1 + economicProfil.getHistoricalPrestige() * 0.15);
		localSponsoring *= (1 + teamValueFactor * 0.25);
		localSponsoring *= getSmallMarketRevenueBoost(marketSize, 1.25);
		localSponsoring *= getLocalSponsoringMultiplier();
		localSponsoring *= getMonthlyLocalRevenueRate(team, month, 0.070, 0.040);

		localMerchandising *= (1 + economicProfil.getFanLoyalty() * 0.25);
		localMerchandising *= (1 + economicProfil.getHistoricalPrestige() * 0.20);
		localMerchandising *= (1 + teamValueFactor * 0.22);
		localMerchandising *= getSmallMarketRevenueBoost(marketSize, 1.30);
		localMerchandising *= getLocalMerchandisingMultiplier();
		localMerchandising *= getMonthlyLocalRevenueRate(team, month, 0.105, 0.055);

		otherRevenue *= (1 + economicProfil.getOwnerDeficitTolerance() * 0.08);
		otherRevenue *= (1 + teamValueFactor * 0.15);
		otherRevenue *= getSmallMarketRevenueBoost(marketSize, 1.20);
		otherRevenue *= getOtherRevenueMultiplier();
		otherRevenue *= getMonthlyLocalRevenueRate(team, month, 0.090, 0.050);

		double seasonContextMultiplier = getSeasonContextRevenueMultiplier(month);
		localSponsoring *= seasonContextMultiplier;
		localMerchandising *= seasonContextMultiplier;
		otherRevenue *= seasonContextMultiplier;

		double monthlyPayroll = team.getTeamFinance().getCurrentPayroll()
				/ FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS;
		double monthlyLuxuryTax = calculateMonthlyLuxuryTax(teamFinance);
		double stadiumMaintenance = calculateStadiumMaintenance(team, marketMultiplier, mediaMarket,
				economicProfil, financialPolicy);
		double staffCost = calculateStaffCost(team, marketMultiplier, economicProfil, financialPolicy);
		double administrativeCost = calculateAdministrativeCost(team, marketMultiplier, mediaMarket,
				economicProfil,
				financialPolicy);
		double seasonExpenseMultiplier = getSeasonContextExpenseMultiplier(month);

		stadiumMaintenance *= getMonthlyExpenseRate(team, month, 0.220, 0.130);
		staffCost *= getMonthlyExpenseRate(team, month, 0.280, 0.170);
		administrativeCost *= getMonthlyExpenseRate(team, month, 0.240, 0.145);

		stadiumMaintenance *= seasonExpenseMultiplier;
		staffCost *= seasonExpenseMultiplier;
		administrativeCost *= seasonExpenseMultiplier;

		FinanceUtility.addIncome(budget,
				new Income(IncomeType.LOCAL_SPONSORING, localSponsoring), month);
		FinanceUtility.addIncome(budget,
				new Income(IncomeType.LOCAL_MERCHANDISING, localMerchandising),
				month);
		FinanceUtility.addIncome(budget, new Income(IncomeType.OTHER, otherRevenue),
				month);

		applyFixedCosts(team, month, budget, teamFinance, marketMultiplier, mediaMarket, economicProfil,
				financialPolicy,
				monthlyPayroll, monthlyLuxuryTax, stadiumMaintenance, staffCost, administrativeCost);
	}

	public void applyMonthlyFixedCosts(Team team, int month) {
		Budget budget = team.getTeamFinance().getBudget();
		TeamFinance teamFinance = team.getTeamFinance();
		MarketSize marketSize = teamFinance.getMarketSize();
		MediaMarket mediaMarket = teamFinance.getMediaMarket();
		EconomicProfil economicProfil = teamFinance.getEconomicProfil();
		FinancialPolicy financialPolicy = teamFinance.getFinancialProfil();

		double marketMultiplier = getMarketMultiplier(marketSize);
		double monthlyPayroll = team.getTeamFinance().getCurrentPayroll()
				/ FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS;
		double monthlyLuxuryTax = calculateMonthlyLuxuryTax(teamFinance);
		double stadiumMaintenance = calculateStadiumMaintenance(team, marketMultiplier, mediaMarket,
				economicProfil, financialPolicy);
		double staffCost = calculateStaffCost(team, marketMultiplier, economicProfil, financialPolicy);
		double administrativeCost = calculateAdministrativeCost(team, marketMultiplier, mediaMarket,
				economicProfil,
				financialPolicy);
		double seasonExpenseMultiplier = getSeasonContextExpenseMultiplier(month);

		stadiumMaintenance *= getMonthlyExpenseRate(team, month, 0.220, 0.130);
		staffCost *= getMonthlyExpenseRate(team, month, 0.280, 0.170);
		administrativeCost *= getMonthlyExpenseRate(team, month, 0.240, 0.145);

		stadiumMaintenance *= seasonExpenseMultiplier;
		staffCost *= seasonExpenseMultiplier;
		administrativeCost *= seasonExpenseMultiplier;

		applyFixedCosts(team, month, budget, teamFinance, marketMultiplier, mediaMarket, economicProfil,
				financialPolicy,
				monthlyPayroll, monthlyLuxuryTax, stadiumMaintenance, staffCost, administrativeCost);
	}

	private void applyFixedCosts(Team team, int month, Budget budget, TeamFinance teamFinance,
			double marketMultiplier, MediaMarket mediaMarket, EconomicProfil economicProfil,
			FinancialPolicy financialPolicy, double monthlyPayroll, double monthlyLuxuryTax,
			double stadiumMaintenance, double staffCost, double administrativeCost) {
		FinanceUtility.addExpense(budget,
				new Expense(ExpenseType.PLAYER_SALARY, monthlyPayroll), month);
		FinanceUtility.addExpense(budget,
				new Expense(ExpenseType.LUXURY_TAX_PAID, monthlyLuxuryTax), month);
		FinanceUtility.addExpense(budget,
				new Expense(ExpenseType.MAINTENANCE_STADIUM_COST,
						stadiumMaintenance),
				month);
		FinanceUtility.addExpense(budget,
				new Expense(ExpenseType.STAFF_COST, staffCost),
				month);
		FinanceUtility.addExpense(budget,
				new Expense(ExpenseType.ADMINISTRATIVE_COST, administrativeCost),
				month);

		teamFinance.setLuxuryTaxPaid(teamFinance.getLuxuryTaxPaid() + monthlyLuxuryTax);
		FinanceUtility.updateBudget(budget);
		FinanceUtility.updateTeamValue(team);
	}

	private double calculateMonthlyLuxuryTax(TeamFinance teamFinance) {
		double luxuryTaxLine = league.getLeagueFinance().getLeagueFinancialRules().getLuxuryTaxLine();
		double seasonLuxuryTax = FinanceUtility.luxuryTaxPenalty(teamFinance.getCurrentPayroll(),
				luxuryTaxLine);
		return seasonLuxuryTax / FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS;
	}

	private double calculateStadiumMaintenance(Team team, double marketMultiplier, MediaMarket mediaMarket,
			EconomicProfil economicProfil, FinancialPolicy financialPolicy) {
		double capacityFactor = team.getStadium().getCapacity() / 20000.0;
		double maintenance = 2 * marketMultiplier * capacityFactor;

		maintenance *= (1 + mediaMarket.getBusinessOpportunityModifier() * 0.26);
		maintenance *= (1 + economicProfil.getHistoricalPrestige() * 0.15);
		maintenance *= 1.15;
		maintenance *= financialPolicy.accept(new MaintenanceCostMultiplierVisitor());
		maintenance *= getSmallMarketCostFactor(team.getTeamFinance().getMarketSize(), 0.88);

		return maintenance;
	}

	private double calculateStaffCost(Team team, double marketMultiplier, EconomicProfil economicProfil,
			FinancialPolicy financialPolicy) {
		int numberOfPlayers = team.getCurrentPlayers().size();
		double popularityFactor = 0.80 + (team.getCurrentPopularity() / 100.0);
		double staffCost = ((0.15 * numberOfPlayers) + 2) * marketMultiplier * popularityFactor;

		staffCost *= (1 + economicProfil.getFanLoyalty() * 0.25);
		staffCost *= (1 + economicProfil.getCommercialAggressiveness() * 0.40);
		staffCost *= 1.12;
		staffCost *= getSmallMarketCostFactor(team.getTeamFinance().getMarketSize(), 0.92);
		staffCost *= financialPolicy.accept(new StaffCostMultiplierVisitor());
		staffCost *= getOperationalPressureMultiplier(team);

		return staffCost;
	}

	private double calculateAdministrativeCost(Team team, double marketMultiplier, MediaMarket mediaMarket,
			EconomicProfil economicProfil, FinancialPolicy financialPolicy) {
		double administrativeCost = 2 * marketMultiplier;

		administrativeCost *= (1 + mediaMarket.getBusinessOpportunityModifier() * 0.10);
		administrativeCost *= (1 + economicProfil.getCommercialAggressiveness() * 0.10);
		administrativeCost *= 1.10;
		administrativeCost *= financialPolicy.accept(new AdministrativeCostMultiplierVisitor());
		administrativeCost *= getSmallMarketCostFactor(team.getTeamFinance().getMarketSize(), 0.90);
		administrativeCost *= getOperationalPressureMultiplier(team);

		return administrativeCost;
	}

	private double getOperationalPressureMultiplier(Team team) {
		double remainingBudget = team.getTeamFinance().getBudget().getRemainingAmount();
		double payroll = team.getTeamFinance().getCurrentPayroll();
		double luxuryTaxLine = league.getLeagueFinance().getLeagueFinancialRules().getLuxuryTaxLine();

		double multiplier = 1.0;

		if (remainingBudget < 420) {
			multiplier *= 0.82;
		} else if (remainingBudget < 520) {
			multiplier *= 0.90;
		} else if (remainingBudget < 620) {
			multiplier *= 0.96;
		}

		double payrollPressure = payroll / luxuryTaxLine;
		if (payrollPressure > 1.20) {
			multiplier *= 0.90;
		} else if (payrollPressure > 1.08) {
			multiplier *= 0.95;
		}

		return multiplier;
	}

	private double getSeasonContextRevenueMultiplier(int month) {
		if (CalendarUtility.isImportantMonth(month)) {
			return 1.04;
		}
		return 1.0;
	}

	private double getSeasonContextExpenseMultiplier(int month) {
		if (CalendarUtility.isImportantMonth(month)) {
			return 1.05;
		}
		return 1.0;
	}

	private double getMarketMultiplier(MarketSize marketSize) {
		return marketSize.accept(new CalculateMonthlyTeamFinanceVisitor());
	}

	private double getMonthlyLocalRevenueRate(Team team, int month, double monthAmplitude, double teamAmplitude) {
		String teamKey = team.getName() == null ? "" : team.getName();
		double teamPhase = Math.abs(teamKey.hashCode() % 17) * 0.19;
		double monthWave = Math.sin((month * 1.35) + teamPhase);
		double secondWave = Math.cos((month * 0.72) + (teamPhase * 0.55));

		return 1 + (monthWave * monthAmplitude) + (secondWave * teamAmplitude);
	}

	private double getMonthlyExpenseRate(Team team, int month, double monthAmplitude, double teamAmplitude) {
		String teamKey = team.getName() == null ? "" : team.getName();
		double teamPhase = Math.abs(teamKey.hashCode() % 19) * 0.21;
		double monthWave = Math.cos((month * 1.18) + teamPhase);
		double secondWave = Math.sin((month * 0.81) + (teamPhase * 0.48));

		return 1 + (monthWave * monthAmplitude) + (secondWave * teamAmplitude);
	}

	private double getSmallMarketRevenueBoost(MarketSize marketSize, double boost) {
		if (marketSize == null) {
			return 1.0;
		}

		double marketMultiplier = getMarketMultiplier(marketSize);
		if (marketMultiplier <= FinanceConfiguration.MARKET_SIZE_SMALL_MULTIPLIER) {
			return boost;
		}

		return 1.0;
	}

	private double getSmallMarketCostFactor(MarketSize marketSize, double factor) {
		if (marketSize == null) {
			return 1.0;
		}

		double marketMultiplier = getMarketMultiplier(marketSize);
		if (marketMultiplier <= FinanceConfiguration.MARKET_SIZE_SMALL_MULTIPLIER) {
			return factor;
		}

		return 1.0;
	}

	protected abstract double getLocalSponsoringMultiplier();

	protected abstract double getLocalMerchandisingMultiplier();

	protected abstract double getOtherRevenueMultiplier();
}
