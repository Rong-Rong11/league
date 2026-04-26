/*
	* Decompiled with CFR 0.152.
	*/
package data.team.finance.financialpolicy;

import process.visitor.financialpolicy.FinancialProfilVisitor;

public class BalancedPolicy
		extends FinancialPolicy {
	public BalancedPolicy() {
		super();
	}

	@Override
	public <F> F accept(FinancialProfilVisitor<F> financialProfilVisitor) {
		return financialProfilVisitor.visit(this);
	}
}
