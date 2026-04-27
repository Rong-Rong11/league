package process.service.finance.game.revenue;

import org.apache.log4j.Logger;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.economicprofile.EconomicProfile;
import data.team.finance.mediamarket.MediaMarket;
import log.LoggerUtility;
import process.utility.FinanceUtility;

public class GameMerchRevenueCalculator {
	private static final Logger logger = LoggerUtility.getLogger(GameMerchRevenueCalculator.class, "text");

	private GameStat gameStat;
	private GameRevenueBonusProvider bonusProvider;

	public GameMerchRevenueCalculator(GameStat gameStat, GameRevenueBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
	}

	public void calculateMerchRevenue(Team homeTeam, double popularityRate, int attendees, Game game) {
		if (homeTeam == null) {
			logger.warn("Skipping merch revenue calculation because home team is null");
			return;
		}
		if (game == null) {
			logger.warn("Skipping merch revenue calculation because game is null");
			return;
		}
		if (gameStat == null) {
			logger.warn("Skipping merch revenue calculation because game stat is null");
			return;
		}
		MediaMarket mediaMarket = homeTeam.getTeamFinance().getStructure().getMediaMarket();
		EconomicProfile economicProfile = homeTeam.getTeamFinance().getStructure().getEconomicProfile();
		double teamValueFactor = FinanceUtility.getNormalizedTeamValue(homeTeam);
		boolean rivalryGame = game.getGameContext().isRivalry();
		logger.trace("Calculating merch revenue for "
				+ homeTeam.getName()
				+ " with attendees "
				+ attendees
				+ " and popularity rate "
				+ popularityRate);

		double purchaseRate = 0.030 + (popularityRate * 0.040);
		double averageSpend = 42;
		logger.trace("Base merch purchase rate is " + purchaseRate + " and average spend is " + averageSpend);

		if (economicProfile != null) {
			purchaseRate += economicProfile.getFanLoyalty() * 0.008;
			purchaseRate += economicProfile.getHistoricalPrestige() * 0.012;
			averageSpend *= (1 + economicProfile.getHistoricalPrestige() * 0.05);
			logger.trace("Applied economic profile merch modifiers");
		}

		if (mediaMarket != null) {
			purchaseRate += mediaMarket.getPrestigeModifier() * 0.005;
			averageSpend *= (1 + mediaMarket.getBusinessOpportunityModifier() * 0.04);
			logger.trace("Applied media market merch modifiers");
		}

		if (popularityRate > 0.82) {
			logger.trace("Applying elite popularity merch modifier");
			purchaseRate += 0.018;
			averageSpend *= 1.12;
		} else if (popularityRate > 0.70) {
			logger.trace("Applying high popularity merch modifier");
			purchaseRate += 0.010;
			averageSpend *= 1.06;
		} else if (popularityRate < 0.40) {
			logger.trace("Applying low popularity merch modifier");
			purchaseRate -= 0.006;
			averageSpend *= 0.95;
		}

		if (rivalryGame) {
			logger.trace("Applying rivalry merch modifier");
			purchaseRate += 0.006;
			averageSpend *= 1.05;
		}

		if (homeTeam.hasStarPlayer()) {
			logger.trace("Applying star player merch modifier for " + homeTeam.getName());
			purchaseRate += 0.012;
			averageSpend *= 1.08;
		}

		purchaseRate += teamValueFactor * 0.01;
		averageSpend *= (1 + teamValueFactor * 0.06);
		logger.trace("Applied team value merch modifier with factor " + teamValueFactor);

		double revenue = (attendees * purchaseRate * averageSpend) / 1000000;
		double bonusRate = bonusProvider.getMerchBonusRate(game, homeTeam, attendees, popularityRate);
		revenue *= (1 + bonusRate);
		logger.trace("Applied merch bonus rate " + bonusRate);

		gameStat.getHomeFinance().setMerchRevenue(revenue);
		logger.debug("Calculated merch revenue "
				+ revenue
				+ " for "
				+ homeTeam.getName()
				+ " with purchase rate "
				+ purchaseRate
				+ " and average spend "
				+ averageSpend);
	}
}
