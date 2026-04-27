package data.team.finance;

import data.finance.budget.Budget;
import data.team.finance.financialpolicy.FinancialPolicy;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.transfer.TeamTransferStrategy;

public class TeamFinance {
	private double teamValue;
	private TeamFinanceStructure structure;
	private TeamFinanceBehavior behavior;

	private Budget budget;
	private double formerPayroll;
	private double currentPayroll;
	private double luxuryTaxPaid;
	private int transferMade = 0;

	public TeamFinance(FinancialPolicy financialPolicy, Budget budget, MarketSize marketSize,
			TeamTransferStrategy teamTransferStrategy) {
		this.structure = new TeamFinanceStructure(marketSize);
		this.behavior = new TeamFinanceBehavior(financialPolicy, teamTransferStrategy);
		this.budget = budget;
		this.formerPayroll = 0.0;
		this.currentPayroll = 0;
		this.luxuryTaxPaid = 0.0;
		this.transferMade = 0;
		teamValue = 0.0;
	}

	public TeamFinanceStructure getStructure() {
		return structure;
	}

	public void setStructure(TeamFinanceStructure structure) {
		this.structure = structure;
	}

	public TeamFinanceBehavior getBehavior() {
		return behavior;
	}

	public void setBehavior(TeamFinanceBehavior behavior) {
		this.behavior = behavior;
	}

	public void incrementTransferMade() {
		transferMade++;
	}

	public Budget getBudget() {
		return this.budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

	public int getTransferMade() {
		return this.transferMade;
	}

	public double getLuxuryTaxPaid() {
		return this.luxuryTaxPaid;
	}

	public void setLuxuryTaxPaid(double luxuryTaxPaid) {
		this.luxuryTaxPaid = luxuryTaxPaid;
	}

	public void setTransferMade(int transferMade) {
		this.transferMade = transferMade;
	}

	public double getFormerPayroll() {
		return formerPayroll;
	}

	public void setFormerPayroll(double formerPayroll) {
		this.formerPayroll = formerPayroll;
	}

	public double getCurrentPayroll() {
		return currentPayroll;
	}

	public void setCurrentPayroll(double currentPayroll) {
		this.currentPayroll = currentPayroll;
	}

	public double getTeamValue() {
		return teamValue;
	}

	public void setTeamValue(double teamValue) {
		this.teamValue = teamValue;
	}

}
