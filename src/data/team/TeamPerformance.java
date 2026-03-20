package data.team;

public class TeamPerformance {
    private double performanceRating = 0.5;
    private double winStreak = 0.0;
    private double loseStreak;
    private double numberWin = 0.0;
    private double numberLose = 0.0;
    private double numberPlayedGames = 0.0;

    public double getPerformanceRating() {
        return this.performanceRating;
    }

    public void setPerformanceRating(double d) {
        this.performanceRating = d;
    }

    public double getWinStreak() {
        return this.winStreak;
    }

    public void setWinStreak(double d) {
        this.winStreak = d;
    }

    public double getLoseStreak() {
        return this.loseStreak;
    }

    public void setLoseStreak(double d) {
        this.loseStreak = d;
    }

    public double getNumberWin() {
        return this.numberWin;
    }

    public void setNumberWin(double d) {
        this.numberWin = d;
    }

    public double getNumberLose() {
        return this.numberLose;
    }

    public void setNumberLose(double d) {
        this.numberLose = d;
    }

    public void incrementNumberWin() {
        this.numberWin += 1.0;
    }

    public void incrementNumberLose() {
        this.numberLose += 1.0;
    }

    public double getNumberPlayedGames() {
        return this.numberPlayedGames;
    }

    public void setNumberPlayedGames(double d) {
        this.numberPlayedGames = d;
    }

    public void incrementNmberPlayedGames() {
        this.numberPlayedGames += 1.0;
    }
}
