package data.league;

import data.finance.budget.Budget;


public class LeagueFinance {
	private Budget budget ; 
	public static double salaryCap ; 
	public static double luxuryTaxLine ; 
	public static double minimumTeamSalary ;
	
	public LeagueFinance(Budget budget, double salaryCap, double luxuryTaxLine, double minimumTeamSalary) {
		super();
		this.budget = budget;
		this.salaryCap = salaryCap;
		this.luxuryTaxLine = luxuryTaxLine;
		this.minimumTeamSalary = minimumTeamSalary;
	}

	public Budget getBudget() {
		return budget;
	}

	public double getSalaryCap() {
		return salaryCap;
	}

	public double getLuxuryTaxLine() {
		return luxuryTaxLine;
	}

	public double getMinimumTeamSalary() {
		return minimumTeamSalary;
	} 
	
	
	
	
	
	//les sommes en millions 
	
	
	
	
}
