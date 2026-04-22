package data.league.finance;

import java.util.HashMap;

import data.finance.MonthlyCentralRevenueData;
import data.finance.budget.Budget;

public class LeagueFinance {
	private double leagueValue = 0;
	private Budget budget;
	private LeagueFinancialRules leagueFinancialRules;
	private LeagueRedistributionPolicy leagueRedistributionPolicy = new LeagueRedistributionPolicy();
	private HashMap<Integer, MonthlyCentralRevenueData> monthlyCentralRevenueHistory = new HashMap<>();

	public LeagueFinance(Budget budget, double salaryCap, double luxuryTaxLine, double minimumTeamSalary,
			double leagueValue) {
		super();
		this.budget = budget;
		this.leagueValue = leagueValue;
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

	public double getLeagueValue() {
		return leagueValue;
	}

	public void setLeagueValue(double leagueValue) {
		this.leagueValue = leagueValue;
	}

	public HashMap<Integer, MonthlyCentralRevenueData> getMonthlyCentralRevenueHistory() {
		return monthlyCentralRevenueHistory;
	}

	// les sommes en millions

}
