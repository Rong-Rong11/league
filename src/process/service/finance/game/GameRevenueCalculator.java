package process.service.finance.game;

import java.time.LocalDate;

import data.finance.GameStat;
import data.league.League;
import data.sport.setup.Game;
import data.sport.setup.GameMoment;
import data.team.Stadium;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import process.utility.CalendarUtility;
import process.utility.FinanceUtility;
import process.visitor.gamemoment.GameMomentAttendanceBonusVisitor;
import process.visitor.marketsize.CalculateBaseTicketVisitor;

public abstract class GameRevenueCalculator {

	private League league;
	protected GameStat gameStat;

	public GameRevenueCalculator(League league, GameStat gameStat) {
		this.league = league;
		this.gameStat = gameStat;
	}

	public final void calculateGameRevenue(Game game, LocalDate date) {
		Team homeTeam = game.getGameContext().getHomeTeam();
		double popularityRate = calculatePopularityRate(game, date);
		Stadium stadium = homeTeam.getStadium();
		int capacity = stadium.getCapacity();
		double attendanceRate = calculateAttendanceRate(game, date, homeTeam, popularityRate);
		int attendees = calculateAttendees(capacity, attendanceRate);
		int ticketPrice = calculateTicketPrice(homeTeam, stadium, popularityRate, attendees, game);

		calculateTicketRevenue(attendees, ticketPrice, game);
		calculateConcessionsRevenue(homeTeam, attendees, popularityRate, game);
		calculateParkingRevenue(homeTeam, attendees, game);
		calculateTVRevenue(game);
		calculateMerchRevenue(homeTeam, popularityRate, attendees, game);
	}

	protected double calculatePopularityRate(Game game, LocalDate date) {
		Team homeTeam = game.getGameContext().getHomeTeam();

		double gamePopularity = CalendarUtility.popularityScoreGame(game, date);
		double gameScore = gamePopularity / 800;

		double performatingRate = (game.getGameContext().getHomeTeam().getTeamPerformance().getPerformanceRating()
				+ game.getGameContext().getAwayTeam().getTeamPerformance().getPerformanceRating()) / 2;

		double popularityRate = (gameScore * 0.5) + (performatingRate * 0.5);

		int winStreak = homeTeam.getTeamPerformance().getCurrentWinStreak();
		popularityRate += Math.min(winStreak, 10) * 0.015;

		popularityRate += getPopularityBonusRate(game, date, homeTeam);

		popularityRate = Math.max(0.2, Math.min(1.0, popularityRate));
		gameStat.setPopularity(popularityRate);
		return popularityRate;
	}

	protected int calculateTicketPrice(Team homeTeam, Stadium stadium, double popularityRate, int attendees,
			Game game) {
		MarketSize marketSize = homeTeam.getTeamFinance().getMarketSize();
		MediaMarket mediaMarket = homeTeam.getTeamFinance().getMediaMarket();
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();
		double teamValueFactor = FinanceUtility.getNormalizedTeamValue(homeTeam);

		double base = stadium.getTicketPrice();
		base = marketSize.accept(new CalculateBaseTicketVisitor());

		double popularityFactor = 1 + (popularityRate - 0.5) * 0.28;
		double price = base * popularityFactor;

		price *= (1 + mediaMarket.getPricingPowerModifier() * 0.09);
		price *= (1 + economicProfil.getHistoricalPrestige() * 0.04);
		price *= (1 - economicProfil.getPriceElasticity() * 0.18);
		price *= (1 + teamValueFactor * 0.05);
		price *= (1 + getTicketPriceBonusRate(game, homeTeam, attendees, popularityRate));

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

	protected int calculateAttendees(int capacity, double attendanceRate) {
		int attendees = (int) (capacity * attendanceRate);
		gameStat.setAttendees(attendees);
		return attendees;
	}

	protected double calculateAttendanceRate(Game game, LocalDate date, Team homeTeam, double popularityRate) {
		MediaMarket mediaMarket = homeTeam.getTeamFinance().getMediaMarket();
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();
		double teamValueFactor = FinanceUtility.getNormalizedTeamValue(homeTeam);

		double importantDayBonus = CalendarUtility.isImportantDay(date)
				|| CalendarUtility.isSpecialEvent(league.getRegularSeason(), date) ? 0.20 : 0.0;
		double gameTimeBonus = getGameTimeAttendanceBonus(game);

		double attendanceRate = (0.32
				+ (popularityRate * 0.58)
				+ importantDayBonus);

		attendanceRate += mediaMarket.getFanBaseModifier() * 0.12;
		attendanceRate += economicProfil.getFanLoyalty() * 0.16;
		attendanceRate += economicProfil.getHistoricalPrestige() * 0.06;
		attendanceRate += teamValueFactor * 0.05;
		attendanceRate += getAttendanceBonusRate(homeTeam, popularityRate);
		attendanceRate += gameTimeBonus;

		double financeBoost = (mediaMarket.getFanBaseModifier() * 0.40)
				+ (economicProfil.getFanLoyalty() * 0.35)
				+ (economicProfil.getHistoricalPrestige() * 0.15)
				+ (teamValueFactor * 0.10);

		if (popularityRate > 0.82) {
			attendanceRate += 0.10 + (financeBoost * 0.05);
		} else if (popularityRate > 0.70) {
			attendanceRate += 0.06 + (financeBoost * 0.03);
		} else if (popularityRate > 0.62) {
			attendanceRate += 0.05;
		} else if (popularityRate < 0.30) {
			attendanceRate -= 0.14 + ((1 - teamValueFactor) * 0.03);
		} else if (popularityRate < 0.35) {
			attendanceRate -= 0.10 + ((1 - mediaMarket.getFanBaseModifier()) * 0.02);
		} else if (popularityRate < 0.45) {
			attendanceRate -= 0.06;
		}

		double randomFactor = 0.88 + (Math.random() * 0.24);

		attendanceRate *= randomFactor;
		attendanceRate = Math.max(0.30, Math.min(1.00, attendanceRate));
		gameStat.setAttendanceRate(attendanceRate);
		return attendanceRate;
	}

	private double getGameTimeAttendanceBonus(Game game) {
		GameMoment gameMoment = game.getGameContext().getGameMoment();

		if (gameMoment == null) {
			return 0.0;
		}

		return gameMoment.accept(new GameMomentAttendanceBonusVisitor());
	}

	protected void calculateTicketRevenue(int attendees, double ticketPrice, Game game) {
		double ticketRevenue = (attendees * ticketPrice * 1.05) / 1000000;
		ticketRevenue *= (1 + getTicketRevenueBonusRate(game, attendees, ticketPrice));
		gameStat.getHomeFinance().setTicketRevenue(ticketRevenue);
	}

	protected void calculateConcessionsRevenue(Team homeTeam, int attendees, double popularityRate, Game game) {
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
		revenue *= (1 + getConcessionsBonusRate(game, homeTeam, attendees, popularityRate));

		gameStat.getHomeFinance().setConcessionsRevenue(revenue);
	}

	protected void calculateParkingRevenue(Team homeTeam, int attendees, Game game) {
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
		revenue *= (1 + getParkingBonusRate(game, homeTeam, attendees));

		gameStat.getHomeFinance().setParkingRevenue(revenue);
	}

	protected void calculateTVRevenue(Game game) {
		double leagueTVPerGame = 0.7;

		double homeShare = leagueTVPerGame * 0.6;
		double awayShare = leagueTVPerGame * 0.4;

		homeShare *= (1 + getHomeTvBonusRate(game));
		awayShare *= (1 + getAwayTvBonusRate(game));

		gameStat.getHomeFinance().setTvRevenue(homeShare);
		gameStat.getAwayFinance().setTvRevenue(awayShare);
	}

	protected void calculateMerchRevenue(Team homeTeam, double popularityRate, int attendees, Game game) {
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

		purchaseRate += teamValueFactor * 0.01;
		averageSpend *= (1 + teamValueFactor * 0.06);

		double revenue = (attendees * purchaseRate * averageSpend) / 1000000;
		revenue *= (1 + getMerchBonusRate(game, homeTeam, attendees, popularityRate));

		gameStat.getHomeFinance().setMerchRevenue(revenue);
	}

	protected abstract double getPopularityBonusRate(Game game, LocalDate date, Team homeTeam);

	protected abstract double getAttendanceBonusRate(Team homeTeam, double popularityRate);

	protected abstract double getTicketPriceBonusRate(Game game, Team homeTeam, int attendees, double popularityRate);

	protected abstract double getTicketRevenueBonusRate(Game game, int attendees, double ticketPrice);

	protected abstract double getConcessionsBonusRate(Game game, Team homeTeam, int attendees, double popularityRate);

	protected abstract double getParkingBonusRate(Game game, Team homeTeam, int attendees);

	protected abstract double getHomeTvBonusRate(Game game);

	protected abstract double getAwayTvBonusRate(Game game);

	protected abstract double getMerchBonusRate(Game game, Team homeTeam, int attendees, double popularityRate);
}
