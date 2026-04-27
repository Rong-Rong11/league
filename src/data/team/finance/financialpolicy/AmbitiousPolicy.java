package data.team.finance.financialpolicy;

import process.visitor.financialpolicy.FinancialProfilVisitor;

public class AmbitiousPolicy
		extends FinancialPolicy {
	public AmbitiousPolicy() {
		super();
	}

	@Override
	public <F> F accept(FinancialProfilVisitor<F> financialProfilVisitor) {
		return financialProfilVisitor.visit(this);
	}
}
