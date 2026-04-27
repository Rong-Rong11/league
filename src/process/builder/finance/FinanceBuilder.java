package process.builder.finance;

import org.apache.log4j.Logger;

import data.finance.budget.Budget;
import data.team.Stadium;
import data.team.Team;
import data.team.finance.TeamFinance;
import data.team.finance.economicprofile.EconomicProfile;
import data.team.finance.financialpolicy.FinancialPolicy;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import log.LoggerUtility;
import process.utility.FinanceUtility;

public class FinanceBuilder {
	private static final Logger logger = LoggerUtility.getLogger(FinanceBuilder.class, "text");

	// deja un marketSize et un profil financier au moment de l'appel car choisi en
	// random
	public static TeamFinance buildTeamFinance(Team team) {
		if (team == null) {
			logger.warn("Skipping team finance build because team is null");
			return null;
		}
		if (team.getTeamFinance() == null) {
			logger.warn("Skipping team finance build because team finance is null for team " + team.getName());
			return null;
		}

		Budget budget = team.getTeamFinance().getBudget();
		TeamFinance teamFinance = team.getTeamFinance();

		EconomicProfile economicProfile = teamFinance.getStructure().getEconomicProfile();
		MarketSize marketSize = teamFinance.getStructure().getMarketSize();
		MediaMarket mediaMarket = teamFinance.getStructure().getMediaMarket();
		FinancialPolicy financialPolicy = teamFinance.getBehavior().getFinancialPolicy();

		if (budget == null) {
			logger.warn("Skipping team finance build because budget is null for team " + team.getName());
			return teamFinance;
		}
		if (marketSize == null) {
			logger.warn("Skipping team finance build because market size is null for team " + team.getName());
			return teamFinance;
		}
		if (economicProfile == null) {
			logger.warn("Skipping team finance build because economic profile is null for team " + team.getName());
			return teamFinance;
		}
		if (mediaMarket == null) {
			logger.warn("Skipping team finance build because media market is null for team " + team.getName());
			return teamFinance;
		}
		if (financialPolicy == null) {
			logger.warn("Skipping team finance build because financial policy is null for team " + team.getName());
			return teamFinance;
		}

		double popularity = team.getFormerPopularity();
		Stadium stadium = team.getStadium();
		logger.info("Building finance data for team " + team.getName());
		logger.debug("Finance build uses popularity "
				+ popularity
				+ ", market size "
				+ marketSize.getClass().getSimpleName()
				+ " and policy "
				+ financialPolicy.getClass().getSimpleName());

		logger.debug("Creating media market configuration");
		MediaMarketBuilder.createMediaMarket(mediaMarket, marketSize);
		logger.debug("Building economic profile");
		EconomicProfileBuilder.build(economicProfile, popularity, mediaMarket, financialPolicy,
				teamFinance.getBehavior().getTeamTransferStrategy());

		logger.debug("Calculating initial budget");
		BudgetBuilder.calculateInitialBudget(budget, marketSize, economicProfile, popularity);
		logger.debug("Calculating initial team value");
		teamFinance.setTeamValue(TeamValueCalculator.calculateInitialTeamValue(team, marketSize, budget));
		logger.debug("Initializing budget state");
		FinanceUtility.initiateBudget(budget);
		logger.debug("Updating team payroll");
		FinanceUtility.updateTeamPayroll(team);
		logger.debug("Configuring stadium finance");
		StadiumFinanceBuilder.configureStadium(stadium, marketSize);
		logger.debug("Team finance built with team value "
				+ teamFinance.getTeamValue()
				+ " and remaining budget "
				+ budget.getRemainingAmount());
		logger.info("Team finance build completed for " + team.getName());

		return teamFinance;
	}
}
