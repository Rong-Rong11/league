package process.service.finance.game.revenue;

import org.apache.log4j.Logger;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.mediamarket.MediaMarket;
import log.LoggerUtility;

public class GameParkingRevenueCalculator {
	private static final Logger logger = LoggerUtility.getLogger(GameParkingRevenueCalculator.class, "text");

	private GameStat gameStat;
	private GameRevenueBonusProvider bonusProvider;

	public GameParkingRevenueCalculator(GameStat gameStat, GameRevenueBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
	}

	public void calculateParkingRevenue(Team homeTeam, int attendees, Game game) {
		if (homeTeam == null) {
			logger.warn("Skipping parking revenue calculation because home team is null");
			return;
		}
		if (game == null) {
			logger.warn("Skipping parking revenue calculation because game is null");
			return;
		}
		if (gameStat == null) {
			logger.warn("Skipping parking revenue calculation because game stat is null");
			return;
		}
		MediaMarket mediaMarket = homeTeam.getTeamFinance().getStructure().getMediaMarket();
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getStructure().getEconomicProfil();
		logger.trace("Calculating parking revenue for " + homeTeam.getName() + " with attendees " + attendees);

		double parkingRate = 0.35;
		double parkingPrice = 24;
		double peoplePerCar = 2.3;
		logger.trace("Base parking values: rate="
				+ parkingRate
				+ ", price="
				+ parkingPrice
				+ ", peoplePerCar="
				+ peoplePerCar);

		if (mediaMarket != null) {
			parkingPrice *= (1 + mediaMarket.getPricingPowerModifier() * 0.05);
			logger.trace("Applied media market parking price modifier");
		}

		if (economicProfil != null) {
			parkingRate += economicProfil.getFanLoyalty() * 0.015;
			logger.trace("Applied fan loyalty parking rate modifier");
		}

		double cars = attendees / peoplePerCar;
		double revenue = (cars * parkingRate * parkingPrice) / 1000000;
		double bonusRate = bonusProvider.getParkingBonusRate(game, homeTeam, attendees);
		revenue *= (1 + bonusRate);
		logger.trace("Applied parking bonus rate " + bonusRate);

		gameStat.getHomeFinance().setParkingRevenue(revenue);
		logger.debug("Calculated parking revenue "
				+ revenue
				+ " for "
				+ homeTeam.getName()
				+ " with cars "
				+ cars
				+ ", rate "
				+ parkingRate
				+ " and price "
				+ parkingPrice);
	}
}
