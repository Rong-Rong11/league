package process.service.finance.game;

import config.FinanceConfiguration;
import config.GameConfiguration;
import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import process.utility.CalendarUtility;
import process.visitor.marketsize.CalculateStadiumCostVisitor;

public abstract class GameExpenseCalculator {

	protected GameStat gameStat;

	public GameExpenseCalculator(GameStat gameStat) {
		this.gameStat = gameStat;
	}

	public final void calculateGameExpenses(Game game) {
		Team homeTeam = game.getGameContext().getHomeTeam();
		Team awayTeam = game.getGameContext().getAwayTeam();
		double gamePopularity = gameStat.getPopularity();
		int attendees = gameStat.getAttendees();

		calculateStadiumCosts(homeTeam, attendees, gamePopularity, game);
		calculateStaffCosts(homeTeam, game);
		calculateSecurityCosts(homeTeam, attendees, game);
		calculateLogisticCosts(game);
		calculateAwayTravelCost(awayTeam, game);
	}

	protected void calculateStadiumCosts(Team homeTeam, int attendees, double gamePopularity, Game game) {
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

		modifier += getStadiumBonusRate(game, homeTeam, attendees, gamePopularity);

		double arenaCost = baseCosts * (1 + modifier);
		gameStat.getHomeFinance().setArenaCosts(arenaCost);
	}

	protected void calculateSecurityCosts(Team homeTeam, int attendees, Game game) {
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();

		double costPerFan = 5.5;
		double modifier = 0.0;

		if (attendees > 15000) {
			modifier += 0.30;
		}

		if (economicProfil.getFanLoyalty() > 0.5) {
			modifier += 0.05;
		}
		modifier += economicProfil.getFanLoyalty() * 0.18;
		modifier += getSecurityBonusRate(game, homeTeam, attendees);
		if (homeTeam.hasStarPlayer()) {
			modifier += 0.1;
		}

		double securityCost = (attendees * costPerFan * (1 + modifier)) / 1000000.0;
		gameStat.getHomeFinance().setSecurityCosts(securityCost);
	}

	protected void calculateStaffCosts(Team homeTeam, Game game) {
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();

		double baseStaffCost = 0.17;
		double modifier = 0.0;
		double attendanceRate = gameStat.getAttendanceRate();

		if (attendanceRate > 0.9) {
			modifier += 0.20;
		} else if (attendanceRate < 0.6) {
			modifier -= 0.10;
		}

		modifier += economicProfil.getFanLoyalty() * 0.05;
		modifier += getStaffBonusRate(game, homeTeam);

		double staffCost = baseStaffCost * (1 + modifier);
		gameStat.getHomeFinance().setStaffCosts(staffCost);
	}

	protected void calculateAwayTravelCost(Team awayTeam, Game game) {
		double travelCost;
		int typeGame = game.getGameContext().getTypeGame();
		EconomicProfil economicProfil = awayTeam.getTeamFinance().getEconomicProfil();

		if (typeGame == GameConfiguration.GAME_INTRA_DIVISION) {
			travelCost = FinanceConfiguration.BASE_TRAVEL_INTRA_DIVISION_COST;
		} else if (typeGame == GameConfiguration.GAME_INTRA_CONFERENCE) {
			travelCost = FinanceConfiguration.BASE_TRAVEL_INTRA_CONFERENCE_COST;
		} else {
			travelCost = FinanceConfiguration.BASE_TRAVEL_INTER_CONFERENCE_COST;
		}

		double bonusRate = getTravelBonusRate(game);
		travelCost = travelCost * (1 + bonusRate);
		travelCost *= (1 + economicProfil.getFanLoyalty() * 0.32);

		gameStat.getAwayFinance().setTravelCosts(travelCost);
	}

	protected void calculateLogisticCosts(Game game) {
		Team homeTeam = game.getGameContext().getHomeTeam();
		MediaMarket mediaMarket = homeTeam.getTeamFinance().getMediaMarket();
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();

		double baseTransport = 0.06;
		double mediaSetup = 0.045;
		double equipment = 0.035;
		double baseLogisticCost = baseTransport + mediaSetup + equipment;

		double modifier = 0.0;

		if (CalendarUtility.isRivalry(game.getGameContext())) {
			modifier += 0.15;
		}

		modifier += mediaMarket.getBusinessOpportunityModifier() * 0.08;
		modifier += economicProfil.getCommercialAggressiveness() * 0.10;
		modifier += economicProfil.getHistoricalPrestige() * 0.05;

		modifier += getLogisticBonusRate(game, homeTeam);

		double logisticCost = baseLogisticCost * (1 + modifier);
		gameStat.getHomeFinance().setLogisticsCosts(logisticCost);
	}

	protected abstract double getStadiumBonusRate(Game game, Team homeTeam, int attendees, double gamePopularity);

	protected abstract double getSecurityBonusRate(Game game, Team homeTeam, int attendees);

	protected abstract double getStaffBonusRate(Game game, Team homeTeam);

	protected abstract double getTravelBonusRate(Game game);

	protected abstract double getLogisticBonusRate(Game game, Team homeTeam);
}
