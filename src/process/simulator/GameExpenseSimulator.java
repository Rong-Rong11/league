package process.simulator;

import config.FinanceConfiguration;
import config.SimulationConfiguration;
import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.marketsize.MarketSize;
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
		double baseCosts = marketSize.accept(new CalculateStadiumCostVisitor());
		double attendanceFactor = attendees / 200000;
		baseCosts *= (1 + (attendanceFactor * 0.25));
		baseCosts *= (1 + (gamePopularity * 0.15));
		gameStat.getHomeFinance().setArenaCosts(baseCosts);
	}

	private void calculateSecurityCosts(int attendees) {
		double costPerFan = 5;
		double riskFactor = attendees > 15000 ? 1.3 : 1.0;
		double securityCost = (attendees * costPerFan * riskFactor) / 1000000;

		gameStat.getHomeFinance().setSecurityCosts(securityCost);
	}

	private void calculateStaffCosts() {
		double baseStaffCost = 0.15;
		double attendanceFactor = 1.0;
		if (gameStat.getAttendanceRate() > 0.9) {
			attendanceFactor = 1.2;
		}
		if (gameStat.getAttendanceRate() < 0.4) {
			attendanceFactor = 0.9;
		}
		double staffCost = baseStaffCost * attendanceFactor;
		gameStat.getHomeFinance().setStaffCosts(staffCost);

	}

	private void calculateAwayTravelCost(Game game) {
		double travelCost = 0;
		int typeGame = game.getGameContext().getTypeGame();
		if (typeGame == SimulationConfiguration.GAME_INTRA_DIVISION) {
			travelCost = FinanceConfiguration.BASE_TRAVEL_INTRA_DIVISION_COST;
		} else if (typeGame == SimulationConfiguration.GAME_INTRA_CONFERENCE) {
			travelCost = FinanceConfiguration.BASE_TRAVEL_INTRA_CONFERENCE_COST;
		} else {
			travelCost = FinanceConfiguration.BASE_TRAVEL_INTER_CONFERENCE_COST;
		}
		gameStat.getAwayFinance().setTravelCosts(travelCost);
	}

	private void calculateLogisticCosts(Game game) {
		double baseTransport = 0.05;
		double mediaSetup = 0.04;
		double equipment = 0.03;
		double rivalryFactor = (CalendarUtilitary.isRivalry(game.getGameContext()) ? 1.15 : 1.0);
		double logisticCost = (baseTransport + mediaSetup + equipment) * rivalryFactor;
		gameStat.getHomeFinance().setLogisticsCosts(logisticCost);
	}

}
