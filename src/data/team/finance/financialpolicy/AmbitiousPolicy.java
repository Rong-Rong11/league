package data.team.finance.financialpolicy;

import process.visitor.financialprofil.FinancialProfilVisitor;

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
