package process.visitor.financialpolicy;

import data.team.finance.financialpolicy.AmbitiousPolicy;
import data.team.finance.financialpolicy.BalancedPolicy;
import data.team.finance.financialpolicy.ThriftyPolicy;

public interface FinancialPolicyVisitor<F> {
	public F visit(ThriftyPolicy policy);

	public F visit(BalancedPolicy policy);

	public F visit(AmbitiousPolicy policy);
}
