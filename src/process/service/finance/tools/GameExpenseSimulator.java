package process.service.finance.tools;

import config.FinanceConfiguration;
import config.GameConfiguration;
import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import process.utility.CalendarUtilitary;
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
		calculateStaffCosts(homeTeam);
		calculateSecurityCosts(homeTeam, attendees);
		calculateLogisticCosts(game);
		calculateAwayTravelCost(game);
	}

	private void calculateStadiumCosts(Team homeTeam, int attendees, double gamePopularity) {
		MarketSize marketSize = homeTeam.getTeamFinance().getMarketSize();
		MediaMarket mediaMarket = homeTeam.getTeamFinance().getMediaMarket();
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();

		double baseCosts = marketSize.accept(new CalculateStadiumCostVisitor());

		double attendanceFactor = ((double) attendees) / 20000.0;
		double modifier = 0.0;

		modifier += attendanceFactor * 0.25;
		modifier += gamePopularity * 0.15;

		modifier += mediaMarket.getBusinessOpportunityModifier() * 0.10;

		modifier += economicProfil.getFanLoyalty() * 0.05;
		modifier += economicProfil.getHistoricalPrestige() * 0.05;

		double arenaCost = baseCosts * (1 + modifier);
		gameStat.getHomeFinance().setArenaCosts(arenaCost);
	}

	private void calculateSecurityCosts(Team homeTeam, int attendees) {
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();

		double costPerFan = 5.0;
		double modifier = 0.0;

		if (attendees > 15000) {
			modifier += 0.30;
		}

		if (economicProfil.getFanLoyalty() > 0.5) {
			modifier += 0.05;
		}

		double securityCost = (attendees * costPerFan * (1 + modifier)) / 1000000.0;
		gameStat.getHomeFinance().setSecurityCosts(securityCost);
	}

	private void calculateStaffCosts(Team homeTeam) {
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();

		double baseStaffCost = 0.15;
		double modifier = 0.0;
		double attendanceRate = gameStat.getAttendanceRate();

		if (attendanceRate > 0.9) {
			modifier += 0.20;
		} else if (attendanceRate < 0.6) {
			modifier -= 0.10;
		}

		modifier += economicProfil.getFanLoyalty() * 0.05;

		double staffCost = baseStaffCost * (1 + modifier);
		gameStat.getHomeFinance().setStaffCosts(staffCost);
	}

	private void calculateAwayTravelCost(Game game) {
		double travelCost;
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
		double baseLogisticCost = baseTransport + mediaSetup + equipment;

		double modifier = 0.0;

		if (CalendarUtilitary.isRivalry(game.getGameContext())) {
			modifier += 0.15;
		}
		modifier += mediaMarket.getBusinessOpportunityModifier() * 0.08;

		modifier += economicProfil.getCommercialAggressiveness() * 0.10;
		modifier += economicProfil.getHistoricalPrestige() * 0.05;

		double logisticCost = baseLogisticCost * (1 + modifier);
		gameStat.getHomeFinance().setLogisticsCosts(logisticCost);
	}
}
