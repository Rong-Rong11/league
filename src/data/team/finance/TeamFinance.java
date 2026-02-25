package data.team.finance;

import data.finance.budget.Budget;

public class TeamFinance {
	
	private FinancialProfil financialProfil ; 
	private Budget budget ; 
	private double payroll ; 
	private MarketSize marketSize ; 
	private double luxuryTaxPaid ;
	
	public TeamFinance(FinancialProfil financialProfil, Budget budget, MarketSize marketSize) {
		super();
		this.financialProfil = financialProfil;
		this.budget = budget;
		this.payroll = 0 ; 
		this.marketSize = marketSize;
		this.luxuryTaxPaid = 0 ;
	}

	public FinancialProfil getFinancialProfil() {
		return financialProfil;
	} 
	
	
	
	
}
