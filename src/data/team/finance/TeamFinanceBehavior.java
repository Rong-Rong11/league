package data.team.finance;

import data.team.finance.financialpolicy.FinancialPolicy;
import data.team.finance.transfer.TeamTransferStrategy;

public class TeamFinanceBehavior {

	private FinancialPolicy financialProfil;
	private TeamTransferStrategy teamTransferStrategy;

	public TeamFinanceBehavior(FinancialPolicy financialProfil, TeamTransferStrategy teamTransferStrategy) {
		this.financialProfil = financialProfil;
		this.teamTransferStrategy = teamTransferStrategy;
	}

	public FinancialPolicy getFinancialProfil() {
		return financialProfil;
	}

	public void setFinancialProfil(FinancialPolicy financialProfil) {
		this.financialProfil = financialProfil;
	}

	public TeamTransferStrategy getTeamTransferStrategy() {
		return teamTransferStrategy;
	}

	public void setTeamTransferStrategy(TeamTransferStrategy teamTransferStrategy) {
		this.teamTransferStrategy = teamTransferStrategy;
	}
}
