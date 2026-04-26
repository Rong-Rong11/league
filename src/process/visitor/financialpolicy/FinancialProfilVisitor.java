/*
	* Decompiled with CFR 0.152.
	*/
package process.visitor.financialpolicy;

import data.team.finance.financialpolicy.AmbitiousPolicy;
import data.team.finance.financialpolicy.BalancedPolicy;
import data.team.finance.financialpolicy.ThriftyPolicy;

public interface FinancialProfilVisitor<F> {
	public F visit(ThriftyPolicy var1);

	public F visit(BalancedPolicy var1);

	public F visit(AmbitiousPolicy var1);
}
