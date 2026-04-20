package process.visitor.financialprofil;

import data.team.finance.financialpolicy.AmbitiousPolicy;
import data.team.finance.financialpolicy.BalancedPolicy;
import data.team.finance.financialpolicy.ThriftyPolicy;

public class StaffCostMultiplierVisitor implements FinancialProfilVisitor<Double> {

	@Override
	public Double visit(ThriftyPolicy thriftyPolicy) {
	  // TODO Auto-generated method stub
	  return 0.9;
	}

	@Override
	public Double visit(BalancedPolicy balancedPolicy) {
	  // TODO Auto-generated method stub
	  return 1.05;
	}

	@Override
	public Double visit(AmbitiousPolicy ambitiousPolicy) {
	  // TODO Auto-generated method stub
	  return 1.2;
	}

}
