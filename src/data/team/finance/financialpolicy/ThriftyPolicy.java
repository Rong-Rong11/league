/*
	* Decompiled with CFR 0.152.
	*/
package data.team.finance.financialpolicy;

import process.visitor.financialprofil.FinancialProfilVisitor;

public class ThriftyPolicy
		extends FinancialPolicy {
	public ThriftyPolicy() {
		super();
	}

	@Override
	public <F> F accept(FinancialProfilVisitor<F> financialProfilVisitor) {
		return financialProfilVisitor.visit(this);
	}
}
