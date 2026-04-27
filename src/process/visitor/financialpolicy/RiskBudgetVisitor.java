package process.visitor.financialpolicy;

import data.finance.budget.Budget;
import data.team.finance.financialpolicy.AmbitiousPolicy;
import data.team.finance.financialpolicy.BalancedPolicy;
import data.team.finance.financialpolicy.ThriftyPolicy;

public class RiskBudgetVisitor implements FinancialPolicyVisitor<Boolean> {

	private Budget budget;

	public RiskBudgetVisitor(Budget budget) {
		super();
		this.budget = budget;
	}

	@Override
	public Boolean visit(ThriftyPolicy thriftyProfil) {
		return budget.getRemainingAmount() < budget.getInitialAmount() * 0.95;
	}

	@Override
	public Boolean visit(BalancedPolicy balancedProfil) {
		return budget.getRemainingAmount() < budget.getInitialAmount() * 0.8;
	}

	@Override
	public Boolean visit(AmbitiousPolicy ambitiousProfil) {
		return budget.getRemainingAmount() < budget.getInitialAmount() * 0.5;
	}

}
