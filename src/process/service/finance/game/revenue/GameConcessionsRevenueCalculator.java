package process.service.finance.game.revenue;

import org.apache.log4j.Logger;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.mediamarket.MediaMarket;
import log.LoggerUtility;

public class GameConcessionsRevenueCalculator {
	private static final Logger logger = LoggerUtility.getLogger(GameConcessionsRevenueCalculator.class, "text");

	private GameStat gameStat;
	private GameRevenueBonusProvider bonusProvider;

	public GameConcessionsRevenueCalculator(GameStat gameStat, GameRevenueBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
	}

	public void calculateConcessionsRevenue(Team homeTeam, int attendees, double popularityRate, Game game) {
		if (homeTeam == null) {
			logger.warn("Skipping concessions revenue calculation because home team is null");
			return;
		}
		if (game == null) {
			logger.warn("Skipping concessions revenue calculation because game is null");
			return;
		}
		if (gameStat == null) {
			logger.warn("Skipping concessions revenue calculation because game stat is null");
			return;
		}
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getStructure().getEconomicProfil();
		MediaMarket mediaMarket = homeTeam.getTeamFinance().getStructure().getMediaMarket();
		boolean rivalryGame = game.getGameContext().isRivalry();
		logger.trace("Calculating concessions revenue for "
				+ homeTeam.getName()
				+ " with attendees "
				+ attendees
				+ " and popularity rate "
				+ popularityRate);

		double purchaseRate = 0.72;
		double averageSpend = 21;
		logger.trace("Base concessions purchase rate is " + purchaseRate + " and average spend is " + averageSpend);

		if (economicProfil != null) {
			purchaseRate += economicProfil.getFanLoyalty() * 0.05;
			averageSpend *= (1 + economicProfil.getHistoricalPrestige() * 0.04);
			logger.trace("Applied economic profile concessions modifiers");
		}

		if (mediaMarket != null) {
			averageSpend *= (1 + mediaMarket.getBusinessOpportunityModifier() * 0.05);
			logger.trace("Applied media market concessions modifier");
		}

		if (popularityRate > 0.80) {
			logger.trace("Applying elite popularity concessions modifier");
			purchaseRate += 0.06;
			averageSpend *= 1.08;
		} else if (popularityRate > 0.65) {
			logger.trace("Applying high popularity concessions modifier");
			purchaseRate += 0.03;
			averageSpend *= 1.04;
		} else if (popularityRate < 0.40) {
			logger.trace("Applying low popularity concessions modifier");
			purchaseRate -= 0.03;
			averageSpend *= 0.96;
		}

		if (rivalryGame) {
			logger.trace("Applying rivalry concessions modifier");
			purchaseRate += 0.02;
			averageSpend *= 1.03;
		}

		averageSpend *= (1 + popularityRate * 0.03);
		double revenue = (attendees * purchaseRate * averageSpend) / 1000000;
		double bonusRate = bonusProvider.getConcessionsBonusRate(game, homeTeam, attendees, popularityRate);
		revenue *= (1 + bonusRate);
		logger.trace("Applied concessions bonus rate " + bonusRate);

		gameStat.getHomeFinance().setConcessionsRevenue(revenue);
		logger.debug("Calculated concessions revenue "
				+ revenue
				+ " for "
				+ homeTeam.getName()
				+ " with purchase rate "
				+ purchaseRate
				+ " and average spend "
				+ averageSpend);
	}
}
