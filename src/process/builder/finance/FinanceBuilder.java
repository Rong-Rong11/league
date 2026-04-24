package process.builder.finance;

import data.finance.budget.Budget;
import data.team.Stadium;
import data.team.Team;
import data.team.finance.TeamFinance;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.financialpolicy.FinancialPolicy;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import process.utility.FinanceUtility;

public class FinanceBuilder {

	// deja un marketSize et un profil financier au moment de l'appel car choisi en
	// random
	public static TeamFinance buildTeamFinance(Team team) {
		Budget budget = team.getTeamFinance().getBudget();
		TeamFinance teamFinance = team.getTeamFinance();

		EconomicProfil economicProfil = teamFinance.getEconomicProfil();
		MarketSize marketSize = teamFinance.getMarketSize();
		MediaMarket mediaMarket = teamFinance.getMediaMarket();
		FinancialPolicy financialProfil = teamFinance.getFinancialProfil();

		double popularity = team.getFormerPopularity();
		Stadium stadium = team.getStadium();

		MediaMarketBuilder.createMediaMarket(mediaMarket, marketSize);
		EconomicProfileBuilder.build(economicProfil, popularity, mediaMarket, financialProfil,
				teamFinance.getTeamTransferStrategy());

		BudgetBuilder.calculateInitialBudget(budget, marketSize, economicProfil, popularity);
		teamFinance.setTeamValue(TeamValueCalculator.calculateInitialTeamValue(team, marketSize, budget));
		FinanceUtility.initiateBudget(budget);
		FinanceUtility.updateTeamPayroll(team);
		StadiumFinanceBuilder.configureStadium(stadium, marketSize);

		return teamFinance;
	}
}
