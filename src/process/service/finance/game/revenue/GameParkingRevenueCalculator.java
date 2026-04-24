package process.service.finance.game.revenue;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.mediamarket.MediaMarket;

public class GameParkingRevenueCalculator {

	private GameStat gameStat;
	private GameRevenueBonusProvider bonusProvider;

	public GameParkingRevenueCalculator(GameStat gameStat, GameRevenueBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
	}

	public void calculateParkingRevenue(Team homeTeam, int attendees, Game game) {
		MediaMarket mediaMarket = homeTeam.getTeamFinance().getMediaMarket();
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();

		double parkingRate = 0.35;
		double parkingPrice = 24;
		double peoplePerCar = 2.3;

		if (mediaMarket != null) {
			parkingPrice *= (1 + mediaMarket.getPricingPowerModifier() * 0.05);
		}

		if (economicProfil != null) {
			parkingRate += economicProfil.getFanLoyalty() * 0.015;
		}

		double cars = attendees / peoplePerCar;
		double revenue = (cars * parkingRate * parkingPrice) / 1000000;
		revenue *= (1 + bonusProvider.getParkingBonusRate(game, homeTeam, attendees));

		gameStat.getHomeFinance().setParkingRevenue(revenue);
	}
}
