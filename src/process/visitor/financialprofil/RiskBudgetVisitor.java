package process.visitor.financialprofil;

import data.finance.budget.Budget;
import data.team.finance.financialprofil.AmbitiousProfil;
import data.team.finance.financialprofil.BalancedProfil;
import data.team.finance.financialprofil.ThriftyProfil;

public class RiskBudgetVisitor implements FinancialProfilVisitor<Boolean>{
	
	private Budget budget ; 
	
	public RiskBudgetVisitor(Budget budget) {
		super();
		this.budget = budget;
	}

	@Override
	public Boolean visit(ThriftyProfil thriftyProfil) {
		// TODO Auto-generated method stub
		return budget.getRemainingAmount() < budget.getInitialAmount() * 0.95;
	}

	@Override
	public Boolean visit(BalancedProfil balancedProfil) {
		return budget.getRemainingAmount() < budget.getInitialAmount() * 0.8;
	}

	@Override
	public Boolean visit(AmbitiousProfil ambitiousProfil) {
		return budget.getRemainingAmount() < budget.getInitialAmount() * 0.6;
	}
	

}
