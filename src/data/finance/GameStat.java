package data.finance;

import data.sport.setup.Game;

public class GameStat {

    private Game game;
    private	int attendees = 0 ; 
    private double ticketPrice = 0 ;
    private double attendanceRate = 0 ; 
    private double popularity = 0 ; 

    private TeamGameFinance homeFinance = new TeamGameFinance() ; 
    private TeamGameFinance awayFinance = new TeamGameFinance() ; 

    public GameStat(Game game) {
        this.game = game;
    }

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public TeamGameFinance getHomeFinance() {
		return homeFinance;
	}

	public void setHomeFinance(TeamGameFinance homeFinance) {
		this.homeFinance = homeFinance;
	}

	public TeamGameFinance getAwayFinance() {
		return awayFinance;
	}

	public void setAwayFinance(TeamGameFinance awayFinance) {
		this.awayFinance = awayFinance;
	}

	public int getAttendees() {
		return attendees;
	}

	public void setAttendees(int attendees) {
		this.attendees = attendees;
	}

	public double getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public double getAttendanceRate() {
		return attendanceRate;
	}

	public void setAttendanceRate(double attendanceRate) {
		this.attendanceRate = attendanceRate;
	}

	public double getPopularity() {
		return popularity;
	}

	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}
	
	
	
	
	
	

    
}
