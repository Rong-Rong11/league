package process.service.finance.distribution.team;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import data.finance.budget.Budget;
import data.finance.budget.income.Income;
import data.finance.budget.income.IncomeType;
import data.team.Team;
import log.LoggerUtility;
import process.repository.TeamRepository;
import process.utility.FinanceUtility;

public class TeamCentralShareDistributor {
	private static final Logger logger = LoggerUtility.getLogger(TeamCentralShareDistributor.class, "text");

	private TeamRepository teamRepositery = TeamRepository.getInstance();
	private CentralRevenueShareScorer shareScorer = new CentralRevenueShareScorer();

	public void distributeTvShare(double tvRevenue, int month) {
		logger.debug("Distributing TV central share " + tvRevenue + " for month " + month);
		double equalPart = tvRevenue * 0.90;
		double weightedPart = tvRevenue * 0.10;
		logger.trace("TV central share split: equalPart=" + equalPart + ", weightedPart=" + weightedPart);
		distributeEqualShare(equalPart, IncomeType.CENTRAL_SHARE, month);
		distributeWeightedShare(weightedPart, month, CentralRevenueShareScorer.TV_SHARE_TYPE);
	}

	public void distributeNationalSponsoringShare(double sponsoringRevenue, int month) {
		logger.debug("Distributing national sponsoring central share " + sponsoringRevenue + " for month " + month);
		double equalPart = sponsoringRevenue * 0.80;
		double weightedPart = sponsoringRevenue * 0.20;
		logger.trace("Sponsoring central share split: equalPart=" + equalPart + ", weightedPart=" + weightedPart);
		distributeEqualShare(equalPart, IncomeType.CENTRAL_SHARE, month);
		distributeWeightedShare(weightedPart, month, CentralRevenueShareScorer.SPONSORING_SHARE_TYPE);
	}

	public void distributeMerchandisingShare(double merchandisingRevenue, int month) {
		logger.debug("Distributing merchandising central share " + merchandisingRevenue + " for month " + month);
		double equalPart = merchandisingRevenue * 0.7;
		double weightedPart = merchandisingRevenue * 0.3;
		logger.trace("Merchandising central share split: equalPart=" + equalPart + ", weightedPart=" + weightedPart);
		distributeEqualShare(equalPart, IncomeType.CENTRAL_SHARE, month);
		distributeWeightedShare(weightedPart, month, CentralRevenueShareScorer.MERCHANDISING_SHARE_TYPE);
	}

	private void distributeEqualShare(double amount, IncomeType incomeType, int month) {
		ArrayList<Team> teams = teamRepositery.getAllTeams();
		if (teams.isEmpty()) {
			logger.warn("Skipping equal central share distribution because no teams are registered");
			return;
		}
		double share = amount / teams.size();
		logger.debug("Distributing equal central share "
				+ share
				+ " to "
				+ teams.size()
				+ " teams for month "
				+ month);

		for (Team team : teams) {
			Budget budget = team.getTeamFinance().getBudget();
			logger.trace("Adding equal central share " + share + " to " + team.getName());
			FinanceUtility.addIncome(budget, new Income(incomeType, share), month);
			FinanceUtility.updateBudget(budget);
		}
	}

	private void distributeWeightedShare(double weightedPart, int month, String shareType) {
		ArrayList<Team> teams = teamRepositery.getAllTeams();
		if (teams.isEmpty()) {
			logger.warn("Skipping weighted central share distribution because no teams are registered");
			return;
		}
		logger.debug("Distributing weighted central share "
				+ weightedPart
				+ " for type "
				+ shareType
				+ " in month "
				+ month);
		double totalScore = 0.0;
		for (Team team : teams) {
			totalScore += shareScorer.calculateShareScore(team, shareType);
		}
		logger.debug("Total weighted central share score is " + totalScore + " for type " + shareType);

		if (totalScore <= 0) {
			logger.warn("Weighted central share score is non-positive, falling back to equal distribution");
			distributeEqualShare(weightedPart, IncomeType.CENTRAL_SHARE, month);
			return;
		}

		for (Team team : teams) {
			double score = shareScorer.calculateShareScore(team, shareType);
			double share = weightedPart * (score / totalScore);

			Budget budget = team.getTeamFinance().getBudget();
			logger.trace("Adding weighted central share "
					+ share
					+ " to "
					+ team.getName()
					+ " with score "
					+ score);
			FinanceUtility.addIncome(budget, new Income(IncomeType.CENTRAL_SHARE, share),
					month);
			FinanceUtility.updateBudget(budget);
		}
	}
}
