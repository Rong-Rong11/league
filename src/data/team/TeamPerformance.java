package data.team;

public class TeamPerformance {
	private double performanceRating = 0.5;
	private int currentWinStreak = 0;
	private int currentLoseStreak = 0;
	private int maxWinsStreak = 0;
	private int maxLoseStreak = 0;
	private int numberWin = 0;
	private int numberLose = 0;
	private int numberPlayedGames = 0;

	public TeamPerformance() {

	}

	public double getPerformanceRating() {
		return performanceRating;
	}

	public void setPerformanceRating(int performanceRating) {
		this.performanceRating = performanceRating;
	}

	public int getCurrentWinStreak() {
		return currentWinStreak;
	}

	public void setCurrentWinStreak(int winStreak) {
		this.currentWinStreak = winStreak;
	}

	public int getCurrentLoseStreak() {
		return currentLoseStreak;
	}

	public void setCurrentLoseStreak(int loseStreak) {
		this.currentLoseStreak = loseStreak;
	}

	public int getNumberWin() {
		return numberWin;
	}

	public void setNumberWin(int numberWin) {
		this.numberWin = numberWin;
	}

	public int getNumberLose() {
		return numberLose;
	}

	public void setNumberLose(int numberLose) {
		this.numberLose = numberLose;
	}

	public void incrementNumberWin() {
		numberWin++;
	}

	public void incrementNumberLose() {
		numberLose++;
	}

	public int getNumberPlayedGames() {
		return numberPlayedGames;
	}

	public void setNumberPlayedGames(int numberPlayedGames) {
		this.numberPlayedGames = numberPlayedGames;
	}

	public void incrementNmberPlayedGames() {
		numberPlayedGames++;
	}

	public void incrementCurrentWinStreak() {
		currentWinStreak++;
	}

	public void incrementCurrentLoseStreak() {
		currentLoseStreak++;
	}

	public void setPerformanceRating(double performanceRating) {
		this.performanceRating = performanceRating;
	}

	public int getMaxWinsStreak() {
		return maxWinsStreak;
	}

	public void setMaxWinsStreak(int maxWinsStreak) {
		this.maxWinsStreak = maxWinsStreak;
	}

	public int getMaxLoseStreak() {
		return maxLoseStreak;
	}

	public void setMaxLoseStreak(int maxLoseStreak) {
		this.maxLoseStreak = maxLoseStreak;
	}

}
