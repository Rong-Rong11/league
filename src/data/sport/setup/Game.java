package data.sport.setup;

public class Game {
	private GameContext gameContext ;
	private GameResult[] quarterResults ; 
	private int homeFinalScore ; 
	private int awayFinalScore ; 
	private boolean displayed;
	
	
	public Game(GameContext gameContext) {
		this.gameContext = gameContext ; 
		quarterResults = new GameResult[4] ; 
		homeFinalScore = 0 ;
		awayFinalScore = 0 ; 
		displayed = false;
	}
	public GameContext getGameContext() {
		return gameContext;
	}

	public void setGameContext(GameContext gameContext) {
		this.gameContext = gameContext;
	}
	public GameResult[] getQuarterResults() {
		return quarterResults;
	}
	
	public void setQuarterResults(GameResult[] quarterResults) {
		this.quarterResults = quarterResults;
	}
	public int getHomeFinalScore() {
		return homeFinalScore;
	}
	public void setHomeFinalScore(int homeFinalScore) {
		this.homeFinalScore = homeFinalScore;
	}
	public int getAwayFinalScore() {
		return awayFinalScore;
	}
	public void setAwayFinalScore(int awayFinalScore) {
		this.awayFinalScore = awayFinalScore;
	}

	public boolean isDisplayed() {
		return displayed;
	}

	public void setDisplayed(boolean displayed) {
		this.displayed = displayed;
	}
	
	
	
	
	
	
	
	
}
