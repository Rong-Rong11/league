package process.service.finance;

import org.apache.log4j.Logger;

import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.finance.budget.expense.Expense;
import data.finance.budget.expense.ExpenseType;
import data.finance.budget.income.Income;
import data.finance.budget.income.IncomeType;
import data.league.League;
import data.league.finance.LeagueRedistributionPolicy;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import log.LoggerUtility;
import process.repository.TeamRepository;
import process.utility.FinanceUtility;
import process.visitor.marketsize.CalculateMonthlyTeamFinanceVisitor;

public class RevenueSharingManager {
	private static final Logger logger = LoggerUtility.getLogger(RevenueSharingManager.class, "text");

	private TeamRepository teamRepositery = TeamRepository.getInstance();
	private League league;

	public RevenueSharingManager(League league) {
		this.league = league;
	}

	public void applyRevenueSharing(int month) {
		if (league == null || league.getLeagueFinance() == null) {
			logger.warn("Skipping revenue sharing because league or league finance is null");
			return;
		}
		logger.info("Applying revenue sharing for month " + month);
		LeagueRedistributionPolicy leagueRedistributionPolicy = league.getLeagueFinance()
				.getLeagueRedistributionPolicy();
		double leagueAverage = calculateLeagueLocalAverage(month);
		double redistributionRate = calculateEffectiveRedistributionRate(leagueRedistributionPolicy, month);
		logger.debug("Revenue sharing inputs: leagueAverage="
				+ leagueAverage
				+ ", redistributionRate="
				+ redistributionRate);

		double pool = collectFromRichTeams(leagueAverage, redistributionRate, month);
		logger.debug("Revenue sharing collected pool " + pool + " for month " + month);

		double leagueKeeps = pool * leagueRedistributionPolicy.getBaseLeagueRetentionRate();
		FinanceUtility.addIncome(
				league.getLeagueFinance().getBudget(),
				new Income(IncomeType.LEAGUE_KEEPS, leagueKeeps),
				month);
		logger.debug("League keeps " + leagueKeeps + " from revenue sharing pool");

		double remainingPool = pool - leagueKeeps;
		double equalSharePool = remainingPool * leagueRedistributionPolicy.getBaseEqualShareRate();
		double weightedSharePool = remainingPool * leagueRedistributionPolicy.getBaseWeightedShareRate();
		logger.debug("Revenue sharing pools: remaining="
				+ remainingPool
				+ ", equalShare="
				+ equalSharePool
				+ ", weightedShare="
				+ weightedSharePool);

		distributeEqualShare(equalSharePool, month);
		distributeToSmallTeams(leagueAverage, weightedSharePool, month);
		logger.info("Revenue sharing applied for month " + month);
	}

	private double calculateLeagueLocalAverage(int month) {
		double total = 0.0;
		int teamCount = teamRepositery.getAllTeams().size();
		if (teamCount == 0) {
			logger.warn("League local average is 0 because no teams are registered");
			return 0.0;
		}

		for (Team team : teamRepositery.getAllTeams()) {
			double adjustedLocalRevenue = calculateAdjustedLocalRevenue(team, month);
			logger.trace("Adjusted local revenue for "
					+ team.getName()
					+ " month "
					+ month
					+ " is "
					+ adjustedLocalRevenue);
			total += adjustedLocalRevenue;
		}

		double average = total / teamCount;
		logger.debug("Calculated league local average " + average + " for month " + month);
		return average;
	}

	private double collectFromRichTeams(double leagueAverage, double redistributionRate, int month) {
		double pool = 0.0;

		for (Team team : teamRepositery.getAllTeams()) {
			double localRevenue = getRegularSeasonRevenueBase(team, month);
			double contextFactor = calculateRevenueContextFactor(team);
			double adjustedLocalRevenue = calculateAdjustedLocalRevenue(team, month);
			Budget budget = team.getTeamFinance().getBudget();

			if (adjustedLocalRevenue > leagueAverage) {
				double expectedRevenueAtLeagueAverage = leagueAverage * contextFactor;
				double excess = localRevenue - expectedRevenueAtLeagueAverage;

				if (excess > 0) {
					double contribution = excess * redistributionRate;
					logger.trace("Collecting revenue sharing contribution "
							+ contribution
							+ " from "
							+ team.getName()
							+ " with excess "
							+ excess);

					FinanceUtility.addExpense(
							budget,
							new Expense(ExpenseType.REVENUE_SHARING_CONTRIBUTION,
									contribution),
							month);

					FinanceUtility.updateBudget(budget);
					pool += contribution;
				}
			}
		}

		logger.debug("Collected revenue sharing pool " + pool + " for month " + month);
		return pool;
	}

	private void distributeEqualShare(double pool, int month) {
		if (pool <= 0) {
			logger.trace("Skipping equal share distribution because pool is non-positive");
			return;
		}
		int teamCount = teamRepositery.getAllTeams().size();
		if (teamCount == 0) {
			logger.warn("Skipping equal share distribution because no teams are registered");
			return;
		}
		double share = pool / teamCount;
		logger.debug("Distributing equal share " + share + " to " + teamCount + " teams for month " + month);
		for (Team team : teamRepositery.getAllTeams()) {
			Budget budget = team.getTeamFinance().getBudget();
			FinanceUtility.addIncome(
					budget,
					new Income(IncomeType.EQUAL_SHARE, share),
					month);
			FinanceUtility.updateBudget(budget);
		}
	}

	private void distributeToSmallTeams(double leagueAverage, double pool, int month) {
		if (pool <= 0) {
			logger.trace("Skipping weighted share distribution because pool is non-positive");
			return;
		}
		double totalNeed = 0;
		for (Team team : teamRepositery.getAllTeams()) {
			double adjustedLocalRevenue = calculateAdjustedLocalRevenue(team, month);
			if (adjustedLocalRevenue < leagueAverage) {
				totalNeed += calculateWeightedNeed(team, leagueAverage, adjustedLocalRevenue);
			}
		}
		if (totalNeed <= 0) {
			logger.trace("Skipping weighted share distribution because total need is non-positive");
			return;
		}
		logger.debug("Distributing weighted revenue sharing pool "
				+ pool
				+ " with total need "
				+ totalNeed
				+ " for month "
				+ month);
		for (Team team : teamRepositery.getAllTeams()) {
			double adjustedLocalRevenue = calculateAdjustedLocalRevenue(team, month);
			Budget budget = team.getTeamFinance().getBudget();

			if (adjustedLocalRevenue < leagueAverage) {
				double need = calculateWeightedNeed(team, leagueAverage, adjustedLocalRevenue);
				double share = (need / totalNeed) * pool;
				logger.trace("Distributing weighted revenue sharing share "
						+ share
						+ " to "
						+ team.getName()
						+ " with need "
						+ need);

				FinanceUtility.addIncome(
						budget,
						new Income(IncomeType.EQUAL_SHARE, share),
						month);

				FinanceUtility.updateBudget(budget);
			}
		}
	}

	private double calculateEffectiveRedistributionRate(LeagueRedistributionPolicy leagueRedistributionPolicy,
			int month) {
		double rate = leagueRedistributionPolicy.getBaseRedistributionRate();

		double leagueAveragePopularity = calculateLeagueAveragePopularity();
		double inequality = calculateRevenueInequality(month);
		logger.trace("Calculating effective redistribution rate with base="
				+ rate
				+ ", averagePopularity="
				+ leagueAveragePopularity
				+ ", inequality="
				+ inequality);

		if (leagueAveragePopularity < 65) {
			rate += 0.03;
		}

		if (inequality > 0.30) {
			rate += 0.05;
		}

		if (inequality < 0.15) {
			rate -= 0.03;
		}

		double effectiveRate = Math.max(
				leagueRedistributionPolicy.getMinimumRedistributionRate(),
				Math.min(leagueRedistributionPolicy.getMaximumRedistributionRate(), rate));
		logger.debug("Effective redistribution rate is " + effectiveRate + " for month " + month);
		return effectiveRate;
	}

	private double calculateLeagueAveragePopularity() {
		double total = 0.0;
		int teamCount = teamRepositery.getAllTeams().size();
		if (teamCount == 0) {
			logger.warn("League average popularity is 0 because no teams are registered");
			return 0.0;
		}

		for (Team team : teamRepositery.getAllTeams()) {
			total += team.getCurrentPopularity();
		}

		double averagePopularity = total / teamCount;
		logger.trace("League average popularity is " + averagePopularity);
		return averagePopularity;
	}

	private double calculateRevenueInequality(int month) {
		double minRevenue = Double.MAX_VALUE;
		double maxRevenue = Double.MIN_VALUE;

		for (Team team : teamRepositery.getAllTeams()) {
			double adjustedLocalRevenue = calculateAdjustedLocalRevenue(team, month);
			if (adjustedLocalRevenue < minRevenue) {
				minRevenue = adjustedLocalRevenue;
			}
			if (adjustedLocalRevenue > maxRevenue) {
				maxRevenue = adjustedLocalRevenue;
			}
		}
		if (maxRevenue <= 0) {
			logger.trace("Revenue inequality is 0 because max revenue is non-positive");
			return 0.0;
		}
		double inequality = (maxRevenue - minRevenue) / maxRevenue;
		logger.trace("Revenue inequality is " + inequality + " with min=" + minRevenue + " and max=" + maxRevenue);
		return inequality;
	}

	private double calculateWeightedNeed(Team team, double leagueAverage, double adjustedLocalRevenue) {
		double baseNeed = leagueAverage - adjustedLocalRevenue;

		if (baseNeed <= 0) {
			return 0.0;
		}

		MarketSize marketSize = team.getTeamFinance().getStructure().getMarketSize();
		double multiplier = 1.0;

		if (marketSize != null) {
			double marketMultiplier = getMarketMultiplier(marketSize);

			if (marketMultiplier <= FinanceConfiguration.MARKET_SIZE_SMALL_MULTIPLIER) {
				multiplier = 1.60;
			} else if (marketMultiplier < 1.0) {
				multiplier = 1.22;
			} else if (marketMultiplier > 1.0) {
				multiplier = 0.88;
			}
		}

		double weightedNeed = baseNeed * multiplier;
		logger.trace("Weighted need for "
				+ team.getName()
				+ " is "
				+ weightedNeed
				+ " with multiplier "
				+ multiplier);
		return weightedNeed;
	}

	private double calculateAdjustedLocalRevenue(Team team, int month) {
		double localRevenue = getRegularSeasonRevenueBase(team, month);
		double contextFactor = calculateRevenueContextFactor(team);

		if (contextFactor <= 0) {
			return localRevenue;
		}

		double adjustedRevenue = localRevenue / contextFactor;
		logger.trace("Adjusted local revenue for "
				+ team.getName()
				+ " is "
				+ adjustedRevenue
				+ " from localRevenue="
				+ localRevenue
				+ " and contextFactor="
				+ contextFactor);
		return adjustedRevenue;
	}

	private double getRegularSeasonRevenueBase(Team team, int month) {
		return FinanceUtility.getTeamIncomeOfMonthForRegularSeason(team, month)
				+ FinanceUtility.getTeamIncomeOfMonthForBoth(team, month);
	}

	private double calculateRevenueContextFactor(Team team) {
		double factor = 0.75;
		double valueFactor = FinanceUtility.getNormalizedTeamValue(team);

		MarketSize marketSize = team.getTeamFinance().getStructure().getMarketSize();
		MediaMarket mediaMarket = team.getTeamFinance().getStructure().getMediaMarket();
		EconomicProfil economicProfil = team.getTeamFinance().getStructure().getEconomicProfil();

		factor *= getMarketMultiplier(marketSize);

		double mediaFactor = 0.0;
		double ecomonicFactor = 0.0;

		mediaFactor += mediaMarket.getBusinessOpportunityModifier() * 0.20;
		mediaFactor += mediaMarket.getPrestigeModifier() * 0.10;
		mediaFactor += mediaMarket.getFanBaseModifier() * 0.10;
		mediaFactor += mediaMarket.getPricingPowerModifier() * 0.10;

		ecomonicFactor += economicProfil.getFanLoyalty() * 0.15;
		ecomonicFactor += economicProfil.getCommercialAggressiveness() * 0.15;
		ecomonicFactor += economicProfil.getHistoricalPrestige() * 0.10;

		double combinedFactor = 1 + (mediaFactor * 0.6 + ecomonicFactor * 0.4);
		factor *= combinedFactor;
		factor *= (1 + valueFactor * 0.12);

		double contextFactor = Math.max(0.75, factor);
		logger.trace("Revenue context factor for " + team.getName() + " is " + contextFactor);
		return contextFactor;
	}

	private double getMarketMultiplier(MarketSize marketSize) {
		if (marketSize == null) {
			logger.warn("Using neutral market multiplier for revenue sharing because market size is null");
			return 1.0;
		}
		double multiplier = marketSize.accept(new CalculateMonthlyTeamFinanceVisitor());
		logger.trace("Revenue sharing market multiplier is " + multiplier);
		return multiplier;
	}
}
