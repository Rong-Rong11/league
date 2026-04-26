package process.service.trade.evaluation;

import java.time.LocalDate;

import org.apache.log4j.Logger;

import data.team.Team;
import data.team.finance.TeamFinance;
import data.team.finance.transfer.TeamTransferStrategy;
import log.LoggerUtility;
import process.visitor.teamtransfer.PreSeasonTradeSatisfactionVisitor;
import process.visitor.teamtransfer.SeasonTradeSatisfactionVisitor;

public class TradeSatisfactionEvaluator {
	private static final Logger logger = LoggerUtility.getLogger(TradeSatisfactionEvaluator.class, "text");

	private Team team;

	public TradeSatisfactionEvaluator(Team team) {
		this.team = team;
		if (team == null) {
			logger.warn("Trade satisfaction evaluator initialized with null team");
		}
	}

	public boolean isSatisfied(boolean season) {
		if (team == null || team.getTeamFinance() == null) {
			logger.warn("Returning true trade satisfaction because team or team finance is null");
			return true;
		}

		TeamFinance teamFinance = team.getTeamFinance();
		TeamTransferStrategy teamTransferStrategy = teamFinance.getBehavior().getTeamTransferStrategy();
		int transferMade = teamFinance.getTransferMade();

		if (season) {
			logger.trace("Evaluating season trade satisfaction for " + team.getName());
			SeasonTradeSatisfactionVisitor visitor = new SeasonTradeSatisfactionVisitor(transferMade,
					teamTransferStrategy.getSeasonIntent());
			return teamFinance.getBehavior().getTeamTransferStrategy().accept(visitor);
		}

		logger.trace("Evaluating preseason trade satisfaction for " + team.getName());
		PreSeasonTradeSatisfactionVisitor preSeasonTradeSatisfactionVisitor = new PreSeasonTradeSatisfactionVisitor(
				transferMade);
		return teamTransferStrategy.accept(preSeasonTradeSatisfactionVisitor);
	}

	public boolean shouldTryTrade(LocalDate date, LocalDate deadLine) {
		if (team == null || team.getTeamPerformance() == null || team.getTeamFinance() == null) {
			logger.warn("Returning false trade attempt decision because team, performance or finance is null");
			return false;
		}
		if (date == null || deadLine == null) {
			logger.warn("Returning false trade attempt decision because date or deadline is null");
			return false;
		}

		double performance = team.getTeamPerformance().getPerformanceRating();
		double deadlineFactor = 1.0;

		if (date.plusDays(15).isAfter(deadLine)) {
			deadlineFactor = 1.5;
		}

		boolean shouldTryTrade;

		if (performance < 0.4) {
			shouldTryTrade = Math.random() < (0.6 * deadlineFactor);
		} else if (performance > 0.7 && team.getTeamFinance().getBehavior().getTeamTransferStrategy().isAllIn()) {
			shouldTryTrade = Math.random() < (0.5 * deadlineFactor);
		} else {
			shouldTryTrade = Math.random() < (0.2 * deadlineFactor);
		}

		logger.trace("Trade attempt decision for " + team.getName()
				+ " | performance: " + performance
				+ ", deadline factor: " + deadlineFactor
				+ ", should try trade: " + shouldTryTrade);

		return shouldTryTrade;
	}
}
