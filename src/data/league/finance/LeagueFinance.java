package data.league.finance;

import data.finance.budget.Budget;

public class LeagueFinance {
	private Budget budget;
	private LeagueFinancialRules leagueFinancialRules;
	private LeagueRedistributionPolicy leagueRedistributionPolicy = new LeagueRedistributionPolicy();

	public LeagueFinance(Budget budget, double salaryCap, double luxuryTaxLine, double minimumTeamSalary) {
		super();
		this.budget = budget;
		leagueFinancialRules = new LeagueFinancialRules(salaryCap, luxuryTaxLine, minimumTeamSalary);
	}

	public Budget getBudget() {
		return budget;
	}

	public LeagueFinancialRules getLeagueFinancialRules() {
		return leagueFinancialRules;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

	public void setLeagueFinancialRules(LeagueFinancialRules leagueFinancialRules) {
		this.leagueFinancialRules = leagueFinancialRules;
	}

	public LeagueRedistributionPolicy getLeagueRedistributionPolicy() {
		return leagueRedistributionPolicy;
	}

	public void setLeagueRedistributionPolicy(LeagueRedistributionPolicy leagueRedistributionPolicy) {
		this.leagueRedistributionPolicy = leagueRedistributionPolicy;
	}

	// les sommes en millions

}
