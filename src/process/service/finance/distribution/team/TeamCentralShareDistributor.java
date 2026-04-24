package process.service.finance.distribution.team;

import data.finance.budget.Budget;
import data.finance.budget.income.Income;
import data.finance.budget.income.IncomeType;
import data.team.Team;
import process.repository.TeamRepository;
import process.utility.FinanceUtility;

public class TeamCentralShareDistributor {
	private TeamRepository teamRepositery = TeamRepository.getInstance();
	private CentralRevenueShareScorer shareScorer = new CentralRevenueShareScorer();

	public void distributeTvShare(double tvRevenue, int month) {
		double equalPart = tvRevenue * 0.90;
		double weightedPart = tvRevenue * 0.10;
		distributeEqualShare(equalPart, IncomeType.CENTRAL_SHARE, month);
		distributeWeightedShare(weightedPart, month, CentralRevenueShareScorer.TV_SHARE_TYPE);
	}

	public void distributeNationalSponsoringShare(double sponsoringRevenue, int month) {
		double equalPart = sponsoringRevenue * 0.80;
		double weightedPart = sponsoringRevenue * 0.20;
		distributeEqualShare(equalPart, IncomeType.CENTRAL_SHARE, month);
		distributeWeightedShare(weightedPart, month, CentralRevenueShareScorer.SPONSORING_SHARE_TYPE);
	}

	public void distributeMerchandisingShare(double merchandisingRevenue, int month) {
		double equalPart = merchandisingRevenue * 0.7;
		double weightedPart = merchandisingRevenue * 0.3;
		distributeEqualShare(equalPart, IncomeType.CENTRAL_SHARE, month);
		distributeWeightedShare(weightedPart, month, CentralRevenueShareScorer.MERCHANDISING_SHARE_TYPE);
	}

	private void distributeEqualShare(double amount, IncomeType incomeType, int month) {
		double share = amount / teamRepositery.getAllTeams().size();

		for (Team team : teamRepositery.getAllTeams()) {
			Budget budget = team.getTeamFinance().getBudget();
			FinanceUtility.addIncome(budget, new Income(incomeType, share), month);
			FinanceUtility.updateBudget(budget);
		}
	}

	private void distributeWeightedShare(double weightedPart, int month, String shareType) {
		double totalScore = 0.0;
		for (Team team : teamRepositery.getAllTeams()) {
			totalScore += shareScorer.calculateShareScore(team, shareType);
		}

		if (totalScore <= 0) {
			distributeEqualShare(weightedPart, IncomeType.CENTRAL_SHARE, month);
			return;
		}

		for (Team team : teamRepositery.getAllTeams()) {
			double score = shareScorer.calculateShareScore(team, shareType);
			double share = weightedPart * (score / totalScore);

			Budget budget = team.getTeamFinance().getBudget();
			FinanceUtility.addIncome(budget, new Income(IncomeType.CENTRAL_SHARE, share),
					month);
			FinanceUtility.updateBudget(budget);
		}
	}
}
