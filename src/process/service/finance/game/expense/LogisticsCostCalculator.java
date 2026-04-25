package process.service.finance.game.expense;

import org.apache.log4j.Logger;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.mediamarket.MediaMarket;
import log.LoggerUtility;
import process.utility.CalendarUtility;

public class LogisticsCostCalculator {
	private static final Logger logger = LoggerUtility.getLogger(LogisticsCostCalculator.class, "text");

	private GameStat gameStat;
	private GameExpenseBonusProvider bonusProvider;

	public LogisticsCostCalculator(GameStat gameStat, GameExpenseBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
		logger.debug("Logistics cost calculator initialized");
	}

	public void calculateLogisticCosts(Game game) {
		if (game == null) {
			logger.warn("Skipping logistics cost calculation because game is null");
			return;
		}
		if (gameStat == null) {
			logger.warn("Skipping logistics cost calculation because game stat is null");
			return;
		}
		Team homeTeam = game.getGameContext().getHomeTeam();
		MediaMarket mediaMarket = homeTeam.getTeamFinance().getStructure().getMediaMarket();
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getStructure().getEconomicProfil();
		logger.trace("Calculating logistics costs for home team " + homeTeam.getName());

		double baseTransport = 0.06;
		double mediaSetup = 0.045;
		double equipment = 0.035;
		double baseLogisticCost = baseTransport + mediaSetup + equipment;
		logger.trace("Base logistics cost is " + baseLogisticCost);

		double modifier = 0.0;

		if (CalendarUtility.isRivalry(game.getGameContext())) {
			logger.trace("Applying rivalry logistics modifier");
			modifier += 0.15;
		}

		modifier += mediaMarket.getBusinessOpportunityModifier() * 0.08;
		modifier += economicProfil.getCommercialAggressiveness() * 0.10;
		modifier += economicProfil.getHistoricalPrestige() * 0.05;
		logger.trace("Logistics modifier after market and economic profile is " + modifier);

		double bonusRate = bonusProvider.getLogisticBonusRate(game, homeTeam);
		modifier += bonusRate;
		logger.trace("Applied logistics bonus rate " + bonusRate);

		double logisticCost = baseLogisticCost * (1 + modifier);
		gameStat.getHomeFinance().setLogisticsCosts(logisticCost);
		logger.debug("Calculated logistics cost "
				+ logisticCost
				+ " for "
				+ homeTeam.getName()
				+ " with modifier "
				+ modifier);
	}
}
