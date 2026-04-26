package process.visitor.financialpolicy;

import data.team.finance.financialpolicy.AmbitiousPolicy;
import data.team.finance.financialpolicy.BalancedPolicy;
import data.team.finance.financialpolicy.ThriftyPolicy;

public class AdministrativeCostMultiplierVisitor implements FinancialProfilVisitor<Double> {

	@Override
	public Double visit(ThriftyPolicy thriftyPolicy) {
		return 0.80;
	}

	@Override
	public Double visit(BalancedPolicy balancedPolicy) {
		return 1.1;
	}

	@Override
	public Double visit(AmbitiousPolicy ambitiousPolicy) {
		return 1.40;
	}
}
