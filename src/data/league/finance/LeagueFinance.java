package data.league.finance;

import data.finance.budget.Budget;

public class LeagueFinance {
    private Budget budget;
    private LeagueFinancialRules leagueFinancialRules;
    private LeagueRevenueModel leagueRevenueModel;
    private LeagueExpenseModel leagueExpenseModel;
    private LeagueRedistributionPolicy leagueRedistributionPolicy;

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

    // les sommes en millions

}
