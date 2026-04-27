package process.service.finance.game.expense;

import org.apache.log4j.Logger;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.economicprofile.EconomicProfile;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import log.LoggerUtility;
import process.visitor.marketsize.CalculateStadiumCostVisitor;

public class StadiumCostCalculator {
	private static final Logger logger = LoggerUtility.getLogger(StadiumCostCalculator.class, "text");

	private GameStat gameStat;
	private GameExpenseBonusProvider bonusProvider;

	public StadiumCostCalculator(GameStat gameStat, GameExpenseBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
	}

	public void calculateStadiumCosts(Team homeTeam, int attendees, double gamePopularity, Game game) {
		if (homeTeam == null) {
			logger.warn("Skipping stadium cost calculation because home team is null");
			return;
		}
		if (gameStat == null) {
			logger.warn("Skipping stadium cost calculation because game stat is null");
			return;
		}
		MarketSize marketSize = homeTeam.getTeamFinance().getStructure().getMarketSize();
		MediaMarket mediaMarket = homeTeam.getTeamFinance().getStructure().getMediaMarket();
		EconomicProfile economicProfile = homeTeam.getTeamFinance().getStructure().getEconomicProfile();
		logger.trace("Calculating stadium costs for " + homeTeam.getName());

		double baseCosts = marketSize.accept(new CalculateStadiumCostVisitor());
		logger.trace("Base stadium cost is " + baseCosts + " for market size "
				+ marketSize.getClass().getSimpleName());

		double attendanceFactor = ((double) attendees) / 20000.0;
		double modifier = 0.0;

		modifier += attendanceFactor * 0.25;
		modifier += gamePopularity * 0.15;
		modifier += mediaMarket.getBusinessOpportunityModifier() * 0.10;
		modifier += economicProfile.getFanLoyalty() * 0.05;
		modifier += economicProfile.getHistoricalPrestige() * 0.05;
		logger.trace("Stadium modifier after attendance, popularity and market factors is " + modifier);

		double bonusRate = bonusProvider.getStadiumBonusRate(game, homeTeam, attendees, gamePopularity);
		modifier += bonusRate;
		logger.trace("Applied stadium bonus rate " + bonusRate);

		double arenaCost = baseCosts * (1 + modifier);
		gameStat.getHomeFinance().setArenaCosts(arenaCost);
		logger.debug("Calculated stadium cost "
				+ arenaCost
				+ " for "
				+ homeTeam.getName()
				+ " with modifier "
				+ modifier);
	}
}
