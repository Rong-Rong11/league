package process.service.finance.game.revenue;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.mediamarket.MediaMarket;

public class GameConcessionsRevenueCalculator {

	private GameStat gameStat;
	private GameRevenueBonusProvider bonusProvider;

	public GameConcessionsRevenueCalculator(GameStat gameStat, GameRevenueBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
	}

	public void calculateConcessionsRevenue(Team homeTeam, int attendees, double popularityRate, Game game) {
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();
		MediaMarket mediaMarket = homeTeam.getTeamFinance().getMediaMarket();
		boolean rivalryGame = game.getGameContext().isRivalry();

		double purchaseRate = 0.72;
		double averageSpend = 21;

		if (economicProfil != null) {
			purchaseRate += economicProfil.getFanLoyalty() * 0.05;
			averageSpend *= (1 + economicProfil.getHistoricalPrestige() * 0.04);
		}

		if (mediaMarket != null) {
			averageSpend *= (1 + mediaMarket.getBusinessOpportunityModifier() * 0.05);
		}

		if (popularityRate > 0.80) {
			purchaseRate += 0.06;
			averageSpend *= 1.08;
		} else if (popularityRate > 0.65) {
			purchaseRate += 0.03;
			averageSpend *= 1.04;
		} else if (popularityRate < 0.40) {
			purchaseRate -= 0.03;
			averageSpend *= 0.96;
		}

		if (rivalryGame) {
			purchaseRate += 0.02;
			averageSpend *= 1.03;
		}

		averageSpend *= (1 + popularityRate * 0.03);
		double revenue = (attendees * purchaseRate * averageSpend) / 1000000;
		revenue *= (1 + bonusProvider.getConcessionsBonusRate(game, homeTeam, attendees, popularityRate));

		gameStat.getHomeFinance().setConcessionsRevenue(revenue);
	}
}
