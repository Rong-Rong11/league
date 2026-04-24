package process.service.finance.game.revenue;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.mediamarket.MediaMarket;
import process.utility.FinanceUtility;

public class GameMerchRevenueCalculator {

	private GameStat gameStat;
	private GameRevenueBonusProvider bonusProvider;

	public GameMerchRevenueCalculator(GameStat gameStat, GameRevenueBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
	}

	public void calculateMerchRevenue(Team homeTeam, double popularityRate, int attendees, Game game) {
		MediaMarket mediaMarket = homeTeam.getTeamFinance().getMediaMarket();
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();
		double teamValueFactor = FinanceUtility.getNormalizedTeamValue(homeTeam);
		boolean rivalryGame = game.getGameContext().isRivalry();

		double purchaseRate = 0.030 + (popularityRate * 0.040);
		double averageSpend = 42;

		if (economicProfil != null) {
			purchaseRate += economicProfil.getFanLoyalty() * 0.008;
			purchaseRate += economicProfil.getHistoricalPrestige() * 0.012;
			averageSpend *= (1 + economicProfil.getHistoricalPrestige() * 0.05);
		}

		if (mediaMarket != null) {
			purchaseRate += mediaMarket.getPrestigeModifier() * 0.005;
			averageSpend *= (1 + mediaMarket.getBusinessOpportunityModifier() * 0.04);
		}

		if (popularityRate > 0.82) {
			purchaseRate += 0.018;
			averageSpend *= 1.12;
		} else if (popularityRate > 0.70) {
			purchaseRate += 0.010;
			averageSpend *= 1.06;
		} else if (popularityRate < 0.40) {
			purchaseRate -= 0.006;
			averageSpend *= 0.95;
		}

		if (rivalryGame) {
			purchaseRate += 0.006;
			averageSpend *= 1.05;
		}

		if (homeTeam.hasStarPlayer()) {
			purchaseRate += 0.012;
			averageSpend *= 1.08;
		}

		purchaseRate += teamValueFactor * 0.01;
		averageSpend *= (1 + teamValueFactor * 0.06);

		double revenue = (attendees * purchaseRate * averageSpend) / 1000000;
		revenue *= (1 + bonusProvider.getMerchBonusRate(game, homeTeam, attendees, popularityRate));

		gameStat.getHomeFinance().setMerchRevenue(revenue);
	}
}
