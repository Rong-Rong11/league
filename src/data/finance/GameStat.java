package data.finance;


import data.sport.setup.Game;

public class GameStat {
	private Game game;
	private int attendees = 0;
	private double ticketPrice = 0.0;
	private double attendanceRate = 0.0;
	private double popularity = 0.0;
	private TeamGameFinance homeFinance = new TeamGameFinance();
	private TeamGameFinance awayFinance = new TeamGameFinance();
	
	public GameStat(Game game) {
		this.game = game;
	}

	public Game getGame() {
		return this.game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public TeamGameFinance getHomeFinance() {
		return this.homeFinance;
	}

	public void setHomeFinance(TeamGameFinance teamGameFinance) {
		this.homeFinance = teamGameFinance;
	}

	public TeamGameFinance getAwayFinance() {
		return this.awayFinance;
	}

	public void setAwayFinance(TeamGameFinance teamGameFinance) {
		this.awayFinance = teamGameFinance;
	}

	public int getAttendees() {
		return this.attendees;
	}

	public void setAttendees(int attendees) {
		this.attendees = attendees;
	}

	public double getTicketPrice() {
		return this.ticketPrice;
	}

	public void setTicketPrice(double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public double getAttendanceRate() {
		return this.attendanceRate;
	}

	public void setAttendanceRate(double attendanceRate) {
		this.attendanceRate = attendanceRate;
	}

	public double getPopularity() {
		return this.popularity;
	}

	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}
}
