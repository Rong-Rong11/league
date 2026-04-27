package data.team.finance.financialpolicy;

import process.visitor.financialpolicy.FinancialPolicyVisitor;

public class BalancedPolicy
		extends FinancialPolicy {
	public BalancedPolicy() {
		super();
	}

	@Override
	public <F> F accept(FinancialPolicyVisitor<F> financialPolicyVisitor) {
		return financialPolicyVisitor.visit(this);
	}
}
