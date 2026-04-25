package process.service.trade.evaluation;

import java.time.LocalDate;

import data.team.Team;
import data.team.finance.TeamFinance;
import data.team.finance.transfer.TeamTransferStrategy;
import process.visitor.teamtransfer.PreSeasonTradeSatisfactionVisitor;
import process.visitor.teamtransfer.SeasonTradeSatisfactionVisitor;

public class TradeSatisfactionEvaluator {

	private Team team;

	public TradeSatisfactionEvaluator(Team team) {
		this.team = team;
	}

	public boolean isSatisfied(boolean season) {
		TeamFinance teamFinance = team.getTeamFinance();
		TeamTransferStrategy teamTransferStrategy = teamFinance.getBehavior().getTeamTransferStrategy();
		int transferMade = teamFinance.getTransferMade();
		if (season) {
			SeasonTradeSatisfactionVisitor visitor = new SeasonTradeSatisfactionVisitor(transferMade,
					teamTransferStrategy.getSeasonIntent());
			return teamFinance.getBehavior().getTeamTransferStrategy().accept(visitor);
		} else {
			PreSeasonTradeSatisfactionVisitor preSeasonTradeSatisfactionVisitor = new PreSeasonTradeSatisfactionVisitor(
					transferMade);
			return teamTransferStrategy.accept(preSeasonTradeSatisfactionVisitor);
		}
	}

	public boolean shouldTryTrade(LocalDate date, LocalDate deadLine) {
		double performance = team.getTeamPerformance().getPerformanceRating();
		double deadlineFactor = 1.0;

		if (date.plusDays(15).isAfter(deadLine)) {
			deadlineFactor = 1.5;
		}

		if (performance < 0.4) {
			return Math.random() < (0.6 * deadlineFactor);
		}
		if (performance > 0.7 && team.getTeamFinance().getBehavior().getTeamTransferStrategy().isAllIn()) {
			return Math.random() < (0.5 * deadlineFactor);
		}
		return Math.random() < (0.2 * deadlineFactor);
	}
}
