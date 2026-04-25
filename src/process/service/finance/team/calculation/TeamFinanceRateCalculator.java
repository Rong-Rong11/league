package process.service.finance.team.calculation;

import org.apache.log4j.Logger;

import config.FinanceConfiguration;
import data.league.League;
import data.team.Team;
import data.team.finance.marketsize.MarketSize;
import log.LoggerUtility;
import process.utility.CalendarUtility;
import process.visitor.marketsize.CalculateMonthlyTeamFinanceVisitor;

public class TeamFinanceRateCalculator {
	private static final Logger logger = LoggerUtility.getLogger(TeamFinanceRateCalculator.class, "text");

	private League league;

	public TeamFinanceRateCalculator(League league) {
		this.league = league;
		logger.debug("Team finance rate calculator initialized");
	}

	public double getOperationalPressureMultiplier(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			logger.warn("Using neutral operational pressure multiplier because team or team finance is null");
			return 1.0;
		}
		double remainingBudget = team.getTeamFinance().getBudget().getRemainingAmount();
		double payroll = team.getTeamFinance().getCurrentPayroll();
		double luxuryTaxLine = league.getLeagueFinance().getLeagueFinancialRules().getLuxuryTaxLine();

		double multiplier = 1.0;

		if (remainingBudget < 500) {
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

		logger.debug("Operational pressure multiplier for "
				+ team.getName()
				+ " is "
				+ multiplier
				+ " with remainingBudget="
				+ remainingBudget
				+ ", payrollPressure="
				+ payrollPressure);
		return multiplier;
	}

	public double getSeasonContextRevenueMultiplier(int month) {
		if (CalendarUtility.isImportantMonth(month)) {
			logger.debug("Season context revenue multiplier is 1.3 for important month " + month);
			return 1.3;
		}
		logger.debug("Season context revenue multiplier is 1.0 for month " + month);
		return 1.0;
	}

	public double getSeasonContextExpenseMultiplier(int month) {
		if (CalendarUtility.isImportantMonth(month)) {
			logger.debug("Season context expense multiplier is 1.13 for important month " + month);
			return 1.13;
		}
		logger.debug("Season context expense multiplier is 1.0 for month " + month);
		return 1.0;
	}

	public double getMarketMultiplier(MarketSize marketSize) {
		if (marketSize == null) {
			logger.warn("Using neutral market multiplier because market size is null");
			return 1.0;
		}
		double multiplier = marketSize.accept(new CalculateMonthlyTeamFinanceVisitor());
		logger.debug("Market multiplier for " + marketSize.getClass().getSimpleName() + " is " + multiplier);
		return multiplier;
	}

	public double getMonthlyLocalRevenueRate(Team team, int month, double monthAmplitude, double teamAmplitude) {
		if (team == null) {
			logger.warn("Using neutral monthly local revenue rate because team is null");
			return 1.0;
		}
		String teamKey = team.getName();
		double teamPhase = Math.abs(teamKey.hashCode() % 17) * 0.19;
		double monthWave = Math.sin((month * 1.35) + teamPhase);
		double secondWave = Math.cos((month * 0.72) + (teamPhase * 0.55));

		double rate = 1 + (monthWave * monthAmplitude) + (secondWave * teamAmplitude);
		logger.trace("Monthly local revenue waves for "
				+ team.getName()
				+ ": monthWave="
				+ monthWave
				+ ", secondWave="
				+ secondWave
				+ ", teamPhase="
				+ teamPhase);
		logger.debug("Monthly local revenue rate for " + team.getName() + " month " + month + " is " + rate);
		return rate;
	}

	public double getMonthlyExpenseRate(Team team, int month, double monthAmplitude, double teamAmplitude) {
		if (team == null) {
			logger.warn("Using neutral monthly expense rate because team is null");
			return 1.0;
		}
		String teamKey = team.getName();
		double teamPhase = Math.abs(teamKey.hashCode() % 19) * 0.21;
		double monthWave = Math.cos((month * 1.18) + teamPhase);
		double secondWave = Math.sin((month * 0.81) + (teamPhase * 0.48));

		double rate = 1 + (monthWave * monthAmplitude) + (secondWave * teamAmplitude);
		logger.trace("Monthly expense waves for "
				+ team.getName()
				+ ": monthWave="
				+ monthWave
				+ ", secondWave="
				+ secondWave
				+ ", teamPhase="
				+ teamPhase);
		logger.debug("Monthly expense rate for " + team.getName() + " month " + month + " is " + rate);
		return rate;
	}

	public double getSmallMarketRevenueBoost(MarketSize marketSize, double boost) {
		if (marketSize == null) {
			logger.warn("Using neutral small market revenue boost because market size is null");
			return 1.0;
		}

		double marketMultiplier = getMarketMultiplier(marketSize);
		if (marketMultiplier <= FinanceConfiguration.MARKET_SIZE_SMALL_MULTIPLIER) {
			logger.debug("Small market revenue boost is " + boost + " for " + marketSize.getClass().getSimpleName());
			return boost;
		}

		logger.debug("Small market revenue boost is 1.0 for " + marketSize.getClass().getSimpleName());
		return 1.0;
	}

	public double getSmallMarketCostFactor(MarketSize marketSize, double factor) {
		if (marketSize == null) {
			logger.warn("Using neutral small market cost factor because market size is null");
			return 1.0;
		}

		double marketMultiplier = getMarketMultiplier(marketSize);
		if (marketMultiplier <= FinanceConfiguration.MARKET_SIZE_SMALL_MULTIPLIER) {
			logger.debug("Small market cost factor is " + factor + " for " + marketSize.getClass().getSimpleName());
			return factor;
		}

		logger.debug("Small market cost factor is 1.0 for " + marketSize.getClass().getSimpleName());
		return 1.0;
	}
}
