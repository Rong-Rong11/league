package process.visitor.financialprofil;

import data.team.finance.financialpolicy.AmbitiousPolicy;
import data.team.finance.financialpolicy.BalancedPolicy;
import data.team.finance.financialpolicy.ThriftyPolicy;

public class MaintenanceCostMultiplierVisitor implements FinancialProfilVisitor<Double> {

	@Override
	public Double visit(ThriftyPolicy thriftyPolicy) {
		return 0.92;
	}

	@Override
	public Double visit(BalancedPolicy balancedPolicy) {
		return 1.00;
	}

	@Override
	public Double visit(AmbitiousPolicy ambitiousPolicy) {
		return 1.16;
	}
}
