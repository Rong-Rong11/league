package data.team.finance;

import data.team.finance.financialpolicy.FinancialPolicy;
import data.team.finance.transfer.TeamTransferStrategy;

public class TeamFinanceBehavior {

	private FinancialPolicy financialPolicy;
	private TeamTransferStrategy teamTransferStrategy;

	public TeamFinanceBehavior(FinancialPolicy financialPolicy, TeamTransferStrategy teamTransferStrategy) {
		this.financialPolicy = financialPolicy;
		this.teamTransferStrategy = teamTransferStrategy;
	}

	public FinancialPolicy getFinancialPolicy() {
		return financialPolicy;
	}

	public void setFinancialPolicy(FinancialPolicy financialPolicy) {
		this.financialPolicy = financialPolicy;
	}

	public TeamTransferStrategy getTeamTransferStrategy() {
		return teamTransferStrategy;
	}

	public void setTeamTransferStrategy(TeamTransferStrategy teamTransferStrategy) {
		this.teamTransferStrategy = teamTransferStrategy;
	}
}
