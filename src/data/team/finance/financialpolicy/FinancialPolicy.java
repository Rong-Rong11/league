package data.team.finance.financialpolicy;

import process.visitor.financialpolicy.FinancialProfilVisitor;

public abstract class FinancialPolicy {

	public FinancialPolicy() {

	}

	public abstract <F> F accept(FinancialProfilVisitor<F> var1);
}
