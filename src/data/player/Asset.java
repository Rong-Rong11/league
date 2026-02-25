package data.player;

public class Asset {
	private double note ;  
	private double minutesPlayedPerMatch ; 
	private int pointPerMatch ; 
	private int reboundPerMatch ; 
	private int assistPerMatch ; 
	private int interceptionPerMatch ; 
	private int blockPerMatch ; 
	private int lostBallPerMatch ; 
	private double trueShootingPercentage ; 
	
	
	
	
	public Asset(double note, double minutesPlayedPerMatch, int pointPerMatch, int reboundPerMatch,
			int assistPerMatch, int interceptionPerMatch, int blockPerMatch, int lostBallPerMatch, double trueShootingPercentage) {
		super();
		this.note = note;
		this.minutesPlayedPerMatch = minutesPlayedPerMatch;
		this.pointPerMatch = pointPerMatch;
		this.reboundPerMatch = reboundPerMatch;
		this.assistPerMatch = assistPerMatch;
		this.interceptionPerMatch = interceptionPerMatch;
		this.blockPerMatch = blockPerMatch;
		this.lostBallPerMatch = lostBallPerMatch;
		this.trueShootingPercentage = trueShootingPercentage;
	}
	
	public Asset() {
		this(0,0,0,0,0,0,0,0,0) ; 
	}
	
	public double getNote() {
		return note;
	}
	public void setNote(double note) {
		this.note = note;
	}
	
	public double getMinutesPlayedPerMatch() {
		return minutesPlayedPerMatch;
	}
	public void setMinutesPlayed(double minutesPlayed) {
		this.minutesPlayedPerMatch = minutesPlayed;
	}
	public int getPointPerMatch() {
		return pointPerMatch;
	}
	public void setPointPerMatch(int pointPerMatch) {
		this.pointPerMatch = pointPerMatch;
	}
	public int getReboundPerMatch() {
		return reboundPerMatch;
	}
	public void setReboundPerMatch(int reboundPerMatch) {
		this.reboundPerMatch = reboundPerMatch;
	}
	public int getAssistPerMatch() {
		return assistPerMatch;
	}
	public void setAssistPerMatch(int assistPerMatch) {
		this.assistPerMatch = assistPerMatch;
	}
	public int getInterceptionPerMatch() {
		return interceptionPerMatch;
	}
	public void setInterceptionPerMatch(int interceptionPerMatch) {
		this.interceptionPerMatch = interceptionPerMatch;
	}
	public int getBlockPerMatch() {
		return blockPerMatch;
	}
	public void setBlockPerMatch(int blockPerMatch) {
		this.blockPerMatch = blockPerMatch;
	}
	public int getLostBallPerMatch() {
		return lostBallPerMatch;
	}
	public void setLostBallPerMatch(int lostBallPerMatch) {
		this.lostBallPerMatch = lostBallPerMatch;
	}
	public double getTrueShootingPercentage() {
		return trueShootingPercentage;
	}
	public void setTrueShootingPercentage(double trueShootingPercentage) {
		this.trueShootingPercentage = trueShootingPercentage;
	}
	
	public void incrementInterceptionPerMatch() {
		interceptionPerMatch ++ ; 
	}
	
	public void incrementLostBallPerMatch() {
		lostBallPerMatch ++ ; 
	}

	@Override
	public String toString() {
		return "Asset [note=" + note + ", minutesPlayedPerMatch=" + minutesPlayedPerMatch + ", pointPerMatch=" + pointPerMatch
				+ ", reboundPerMatch=" + reboundPerMatch + ", assistPerMatch=" + assistPerMatch
				+ ", interceptionPerMatch=" + interceptionPerMatch + ", blockPerMatch=" + blockPerMatch
				+ ", lostBallPerMatch=" + lostBallPerMatch + ", trueShootingPercentage=" + trueShootingPercentage + "]";
	}
	
	
	
	
	
}
