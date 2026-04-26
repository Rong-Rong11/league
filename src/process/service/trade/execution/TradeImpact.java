package process.service.trade.execution;

import org.apache.log4j.Logger;

import data.team.Team;
import log.LoggerUtility;
import process.utility.FinanceUtility;

public class TradeImpact {
	private static final Logger logger = LoggerUtility.getLogger(TradeImpact.class, "text");

	public void applyFinanceImpact(Team team, double impact, int month) {
		if (team == null) {
			logger.warn("Skipping finance impact because team is null");
			return;
		}

		logger.trace("Applying finance impact to " + team.getName()
				+ " | impact: " + impact
				+ ", month: " + month);

		FinanceUtility.updateTeamPayroll(team);
	}
}
