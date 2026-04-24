package process.service.finance.game.revenue;

import java.time.LocalDate;

import data.finance.GameStat;
import data.league.League;
import data.sport.setup.Game;
import data.team.Stadium;
import data.team.Team;

public abstract class GameRevenueCalculator implements GameRevenueBonusProvider {

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
		Team homeTeam = game.getGameContext().getHomeTeam();
		double popularityRate = popularityCalculator.calculatePopularityRate(game, date);
		Stadium stadium = homeTeam.getStadium();
		int capacity = stadium.getCapacity();
		double attendanceRate = attendanceCalculator.calculateAttendanceRate(game, date, homeTeam, popularityRate);
		int attendees = attendanceCalculator.calculateAttendees(capacity, attendanceRate);
		int ticketPrice = ticketPriceCalculator.calculateTicketPrice(homeTeam, stadium, popularityRate, attendees,
				game);

		ticketRevenueCalculator.calculateTicketRevenue(attendees, ticketPrice, game);
		concessionsRevenueCalculator.calculateConcessionsRevenue(homeTeam, attendees, popularityRate, game);
		parkingRevenueCalculator.calculateParkingRevenue(homeTeam, attendees, game);
		tvRevenueCalculator.calculateTVRevenue(game);
		merchRevenueCalculator.calculateMerchRevenue(homeTeam, popularityRate, attendees, game);
	}
}
