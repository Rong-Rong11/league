package data.team.finance.financialpolicy;

import process.visitor.financialpolicy.FinancialPolicyVisitor;

public abstract class FinancialPolicy {

	public FinancialPolicy() {

	}

	public abstract <F> F accept(FinancialPolicyVisitor<F> var1);
}
