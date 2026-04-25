package process.service.finance.game.revenue;

import java.time.LocalDate;

import org.apache.log4j.Logger;

import data.finance.GameStat;
import data.league.League;
import data.sport.setup.Game;
import data.team.Stadium;
import data.team.Team;
import log.LoggerUtility;

public abstract class GameRevenueCalculator implements GameRevenueBonusProvider {
	private static final Logger logger = LoggerUtility.getLogger(GameRevenueCalculator.class, "text");

	private GamePopularityCalculator popularityCalculator;
	private GameAttendanceCalculator attendanceCalculator;
	private GameTicketPriceCalculator ticketPriceCalculator;
	private GameTicketRevenueCalculator ticketRevenueCalculator;
	private GameConcessionsRevenueCalculator concessionsRevenueCalculator;
	private GameParkingRevenueCalculator parkingRevenueCalculator;
	private GameTvRevenueCalculator tvRevenueCalculator;
	private GameMerchRevenueCalculator merchRevenueCalculator;

	public GameRevenueCalculator(League league, GameStat gameStat) {
		this.popularityCalculator = new GamePopularityCalculator(gameStat, this);
		this.attendanceCalculator = new GameAttendanceCalculator(league, gameStat, this);
		this.ticketPriceCalculator = new GameTicketPriceCalculator(gameStat, this);
		this.ticketRevenueCalculator = new GameTicketRevenueCalculator(gameStat, this);
		this.concessionsRevenueCalculator = new GameConcessionsRevenueCalculator(gameStat, this);
		this.parkingRevenueCalculator = new GameParkingRevenueCalculator(gameStat, this);
		this.tvRevenueCalculator = new GameTvRevenueCalculator(gameStat, this);
		this.merchRevenueCalculator = new GameMerchRevenueCalculator(gameStat, this);
	}

	public final void calculateGameRevenue(Game game, LocalDate date) {
		if (game == null) {
			logger.warn("Skipping game revenue calculation because game is null");
			return;
		}
		if (date == null) {
			logger.warn("Skipping game revenue calculation because date is null");
			return;
		}
		Team homeTeam = game.getGameContext().getHomeTeam();
		logger.debug("Calculating game revenue for " + homeTeam.getName() + " on " + date);
		double popularityRate = popularityCalculator.calculatePopularityRate(game, date);
		Stadium stadium = homeTeam.getStadium();
		int capacity = stadium.getCapacity();
		double attendanceRate = attendanceCalculator.calculateAttendanceRate(game, date, homeTeam, popularityRate);
		int attendees = attendanceCalculator.calculateAttendees(capacity, attendanceRate);
		int ticketPrice = ticketPriceCalculator.calculateTicketPrice(homeTeam, stadium, popularityRate, attendees,
				game);
		logger.debug("Game revenue inputs for "
				+ homeTeam.getName()
				+ ": popularityRate="
				+ popularityRate
				+ ", attendanceRate="
				+ attendanceRate
				+ ", attendees="
				+ attendees
				+ ", ticketPrice="
				+ ticketPrice);

		logger.trace("Calculating ticket revenue");
		ticketRevenueCalculator.calculateTicketRevenue(attendees, ticketPrice, game);
		logger.trace("Calculating concessions revenue");
		concessionsRevenueCalculator.calculateConcessionsRevenue(homeTeam, attendees, popularityRate, game);
		logger.trace("Calculating parking revenue");
		parkingRevenueCalculator.calculateParkingRevenue(homeTeam, attendees, game);
		logger.trace("Calculating TV revenue");
		tvRevenueCalculator.calculateTVRevenue(game);
		logger.trace("Calculating merch revenue");
		merchRevenueCalculator.calculateMerchRevenue(homeTeam, popularityRate, attendees, game);
		logger.debug("Game revenue calculated for " + homeTeam.getName() + " on " + date);
	}
}
