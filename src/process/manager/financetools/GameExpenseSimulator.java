package process.manager.financetools;

import config.FinanceConfiguration;
import config.GameConfiguration;
import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import process.utilitary.CalendarUtilitary;
import process.visitor.marketsize.CalculateStadiumCostVisitor;

public class GameExpenseSimulator {
	private GameStat gameStat;

	public GameExpenseSimulator(GameStat gameStat) {
		super();
		this.gameStat = gameStat;
	}

	public void calculateGameExpenses(Game game) {
		Team homeTeam = game.getGameContext().getHomeTeam();
		double gamePopularity = gameStat.getPopularity();

		int attendees = gameStat.getAttendees();

		calculateStadiumCosts(homeTeam, attendees, gamePopularity);
		calculateStaffCosts();
		calculateSecurityCosts(attendees);
		calculateLogisticCosts(game);
		calculateAwayTravelCost(game);

	}

	private void calculateStadiumCosts(Team homeTeam, int attendees, double gamePopularity) {
		MarketSize marketSize = homeTeam.getTeamFinance().getMarketSize();
		MediaMarket mediaMarket = homeTeam.getTeamFinance().getMediaMarket();
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();

		double baseCosts = marketSize.accept(new CalculateStadiumCostVisitor());

		double attendanceFactor = ((double) attendees) / 20000.0;
		baseCosts *= (1 + (attendanceFactor * 0.25));
		baseCosts *= (1 + (gamePopularity * 0.15));

		baseCosts *= (1 + mediaMarket.getBusinessOpportunityModifier() * 0.10);

		baseCosts *= (1 + economicProfil.getFanLoyalty() * 0.05);
		baseCosts *= (1 + economicProfil.getHistoricalPrestige() * 0.05);

		gameStat.getHomeFinance().setArenaCosts(baseCosts);
	}

	private void calculateSecurityCosts(int attendees) {
		Team homeTeam = gameStat.getGame().getGameContext().getHomeTeam();
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();

		double costPerFan = 5;
		double riskFactor = attendees > 15000 ? 1.3 : 1.0;

		if (economicProfil.getFanLoyalty() < 0.4) {
			riskFactor *= 1.05;
		}

		double securityCost = (attendees * costPerFan * riskFactor) / 1000000;
		gameStat.getHomeFinance().setSecurityCosts(securityCost);
	}

	private void calculateStaffCosts() {
		Team homeTeam = gameStat.getGame().getGameContext().getHomeTeam();
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();

		double baseStaffCost = 0.15;
		double attendanceFactor = 1.0;

		if (gameStat.getAttendanceRate() > 0.9) {
			attendanceFactor = 1.2;
		}
		if (gameStat.getAttendanceRate() < 0.4) {
			attendanceFactor = 0.9;
		}

		double staffCost = baseStaffCost * attendanceFactor;

		if (economicProfil != null) {
			staffCost *= (1 + economicProfil.getFanLoyalty() * 0.05);
		}

		gameStat.getHomeFinance().setStaffCosts(staffCost);
	}

	private void calculateAwayTravelCost(Game game) {
		double travelCost = 0;
		int typeGame = game.getGameContext().getTypeGame();
		if (typeGame == GameConfiguration.GAME_INTRA_DIVISION) {
			travelCost = FinanceConfiguration.BASE_TRAVEL_INTRA_DIVISION_COST;
		} else if (typeGame == GameConfiguration.GAME_INTRA_CONFERENCE) {
			travelCost = FinanceConfiguration.BASE_TRAVEL_INTRA_CONFERENCE_COST;
		} else {
			travelCost = FinanceConfiguration.BASE_TRAVEL_INTER_CONFERENCE_COST;
		}
		gameStat.getAwayFinance().setTravelCosts(travelCost);
	}

	private void calculateLogisticCosts(Game game) {
		Team homeTeam = game.getGameContext().getHomeTeam();
		MediaMarket mediaMarket = homeTeam.getTeamFinance().getMediaMarket();
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();

		double baseTransport = 0.05;
		double mediaSetup = 0.04;
		double equipment = 0.03;
		double rivalryFactor = (CalendarUtilitary.isRivalry(game.getGameContext()) ? 1.15 : 1.0);

		double logisticCost = (baseTransport + mediaSetup + equipment) * rivalryFactor;

		if (mediaMarket != null) {
			logisticCost *= (1 + mediaMarket.getBusinessOpportunityModifier() * 0.08);
		}

		if (economicProfil != null) {
			logisticCost *= (1 + economicProfil.getCommercialAggressiveness() * 0.10);
			logisticCost *= (1 + economicProfil.getHistoricalPrestige() * 0.05);
		}

		gameStat.getHomeFinance().setLogisticsCosts(logisticCost);
	}

}
