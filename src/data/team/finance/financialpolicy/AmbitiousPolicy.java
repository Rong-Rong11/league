package data.team.finance.financialpolicy;

import process.visitor.financialpolicy.FinancialPolicyVisitor;

public class AmbitiousPolicy
		extends FinancialPolicy {
	public AmbitiousPolicy() {
		super();
	}

	@Override
	public <F> F accept(FinancialPolicyVisitor<F> financialPolicyVisitor) {
		return financialPolicyVisitor.visit(this);
	}
}
