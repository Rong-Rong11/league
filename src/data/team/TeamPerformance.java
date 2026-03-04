package data.team;

public class TeamPerformance {
	private double performanceRating = 0.5 ; 
	private double winStreak = 0 ; 
	private double loseStreak ; 
	private double numberWin = 0 ; 
	private double numberLose = 0 ;
	private double numberPlayedGames = 0 ; 
	
	public TeamPerformance() {
		
	}
	public double getPerformanceRating() {
		return performanceRating;
	}
	public void setPerformanceRating(double performanceRating) {
		this.performanceRating = performanceRating;
	}
	public double getWinStreak() {
		return winStreak;
	}
	public void setWinStreak(double winStreak) {
		this.winStreak = winStreak;
	}
	public double getLoseStreak() {
		return loseStreak;
	}
	public void setLoseStreak(double loseStreak) {
		this.loseStreak = loseStreak;
	}
	public double getNumberWin() {
		return numberWin;
	}
	public void setNumberWin(double numberWin) {
		this.numberWin = numberWin;
	}
	public double getNumberLose() {
		return numberLose;
	}
	public void setNumberLose(double numberLose) {
		this.numberLose = numberLose;
	} 
	
	public void incrementNumberWin() {
		numberWin ++ ; 
	}
	public void incrementNumberLose() {
		numberLose ++ ; 
	}
	public double getNumberPlayedGames() {
		return numberPlayedGames;
	}
	public void setNumberPlayedGames(double numberPlayedGames) {
		this.numberPlayedGames = numberPlayedGames;
	}
	public void incrementNmberPlayedGames() {
		numberPlayedGames ++ ; 
	}
	
	
}
