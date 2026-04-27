package data.team.finance.financialpolicy;

import process.visitor.financialpolicy.FinancialPolicyVisitor;

public class ThriftyPolicy
		extends FinancialPolicy {
	public ThriftyPolicy() {
		super();
	}

	@Override
	public <F> F accept(FinancialPolicyVisitor<F> financialPolicyVisitor) {
		return financialPolicyVisitor.visit(this);
	}
}
