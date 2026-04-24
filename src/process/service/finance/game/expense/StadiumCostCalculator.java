package process.service.finance.game.expense;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import process.visitor.marketsize.CalculateStadiumCostVisitor;

public class StadiumCostCalculator {

	private GameStat gameStat;
	private GameExpenseBonusProvider bonusProvider;

	public StadiumCostCalculator(GameStat gameStat, GameExpenseBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
	}

	public void calculateStadiumCosts(Team homeTeam, int attendees, double gamePopularity, Game game) {
		MarketSize marketSize = homeTeam.getTeamFinance().getMarketSize();
		MediaMarket mediaMarket = homeTeam.getTeamFinance().getMediaMarket();
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();

		double baseCosts = marketSize.accept(new CalculateStadiumCostVisitor());

		double attendanceFactor = ((double) attendees) / 20000.0;
		double modifier = 0.0;

		modifier += attendanceFactor * 0.25;
		modifier += gamePopularity * 0.15;
		modifier += mediaMarket.getBusinessOpportunityModifier() * 0.10;
		modifier += economicProfil.getFanLoyalty() * 0.05;
		modifier += economicProfil.getHistoricalPrestige() * 0.05;

		modifier += bonusProvider.getStadiumBonusRate(game, homeTeam, attendees, gamePopularity);

		double arenaCost = baseCosts * (1 + modifier);
		gameStat.getHomeFinance().setArenaCosts(arenaCost);
	}
}
