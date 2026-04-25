package process.service.finance.game.revenue;

import org.apache.log4j.Logger;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Stadium;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import log.LoggerUtility;
import process.utility.FinanceUtility;
import process.visitor.marketsize.CalculateBaseTicketVisitor;

public class GameTicketPriceCalculator {
	private static final Logger logger = LoggerUtility.getLogger(GameTicketPriceCalculator.class, "text");

	private GameStat gameStat;
	private GameRevenueBonusProvider bonusProvider;

	public GameTicketPriceCalculator(GameStat gameStat, GameRevenueBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
		logger.debug("Game ticket price calculator initialized");
	}

	public int calculateTicketPrice(Team homeTeam, Stadium stadium, double popularityRate, int attendees, Game game) {
		if (homeTeam == null) {
			logger.warn("Skipping ticket price calculation because home team is null");
			return 0;
		}
		if (stadium == null) {
			logger.warn("Skipping ticket price calculation because stadium is null");
			return 0;
		}
		if (gameStat == null) {
			logger.warn("Skipping ticket price calculation because game stat is null");
			return 0;
		}
		MarketSize marketSize = homeTeam.getTeamFinance().getStructure().getMarketSize();
		MediaMarket mediaMarket = homeTeam.getTeamFinance().getStructure().getMediaMarket();
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getStructure().getEconomicProfil();
		double teamValueFactor = FinanceUtility.getNormalizedTeamValue(homeTeam);
		logger.trace("Calculating ticket price for "
				+ homeTeam.getName()
				+ " with popularity rate "
				+ popularityRate
				+ " and attendees "
				+ attendees);

		double base = stadium.getTicketPrice();
		base = marketSize.accept(new CalculateBaseTicketVisitor());
		logger.trace("Base ticket price is " + base + " for market size " + marketSize.getClass().getSimpleName());

		double popularityFactor = 1 + (popularityRate - 0.5) * 0.28;
		double price = base * popularityFactor;
		logger.trace("Applied popularity ticket price factor " + popularityFactor);

		if (homeTeam.hasStarPlayer()) {
			logger.trace("Applying star player ticket price modifier for " + homeTeam.getName());
			price *= 1.06;
		}

		price *= (1 + mediaMarket.getPricingPowerModifier() * 0.09);
		price *= (1 + economicProfil.getHistoricalPrestige() * 0.04);
		price *= (1 - economicProfil.getPriceElasticity() * 0.18);
		price *= (1 + teamValueFactor * 0.05);
		logger.trace("Ticket price after market and economic modifiers is " + price);
		double bonusRate = bonusProvider.getTicketPriceBonusRate(game, homeTeam, attendees, popularityRate);
		price *= (1 + bonusRate);
		logger.trace("Applied ticket price bonus rate " + bonusRate);

		if (stadium.getCapacity() > 0) {
			double occupancyRate = (double) attendees / stadium.getCapacity();
			if (occupancyRate > 0.9) {
				logger.trace("Applying high occupancy ticket price modifier with occupancy rate " + occupancyRate);
				price *= 1.03;
			}
		}

		int newPrice = (int) Math.max(5, Math.round(price));
		gameStat.setTicketPrice(newPrice);
		logger.debug("Calculated ticket price " + newPrice + " for " + homeTeam.getName());
		return newPrice;
	}
}
