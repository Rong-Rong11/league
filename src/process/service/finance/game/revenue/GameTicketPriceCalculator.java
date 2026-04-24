package process.service.finance.game.revenue;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Stadium;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import process.utility.FinanceUtility;
import process.visitor.marketsize.CalculateBaseTicketVisitor;

public class GameTicketPriceCalculator {

	private GameStat gameStat;
	private GameRevenueBonusProvider bonusProvider;

	public GameTicketPriceCalculator(GameStat gameStat, GameRevenueBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
	}

	public int calculateTicketPrice(Team homeTeam, Stadium stadium, double popularityRate, int attendees, Game game) {
		MarketSize marketSize = homeTeam.getTeamFinance().getMarketSize();
		MediaMarket mediaMarket = homeTeam.getTeamFinance().getMediaMarket();
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();
		double teamValueFactor = FinanceUtility.getNormalizedTeamValue(homeTeam);

		double base = stadium.getTicketPrice();
		base = marketSize.accept(new CalculateBaseTicketVisitor());

		double popularityFactor = 1 + (popularityRate - 0.5) * 0.28;
		double price = base * popularityFactor;

		if (homeTeam.hasStarPlayer()) {
			price *= 1.06;
		}

		price *= (1 + mediaMarket.getPricingPowerModifier() * 0.09);
		price *= (1 + economicProfil.getHistoricalPrestige() * 0.04);
		price *= (1 - economicProfil.getPriceElasticity() * 0.18);
		price *= (1 + teamValueFactor * 0.05);
		price *= (1 + bonusProvider.getTicketPriceBonusRate(game, homeTeam, attendees, popularityRate));

		if (stadium.getCapacity() > 0) {
			double occupancyRate = (double) attendees / stadium.getCapacity();
			if (occupancyRate > 0.9) {
				price *= 1.03;
			}
		}

		int newPrice = (int) Math.max(5, Math.round(price));
		gameStat.setTicketPrice(newPrice);
		return newPrice;
	}
}
