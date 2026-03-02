package data.player;

public class Asset {
	private double note ;  
	private double minutesPlayedPerMatch ; 
	private double pointPerMatch ; 
	private double reboundPerMatch ; 
	private double assistPerMatch ; 
	private double interceptionPerMatch ; 
	private double blockPerMatch ; 
	private double lostBallPerMatch ; 
	private double trueShootingPercentage ; 
	
	
	
	
	public Asset(double note, double minutesPlayedPerMatch, double pointPerMatch, double reboundPerMatch,
			double assistPerMatch, double interceptionPerMatch, double blockPerMatch, double lostBallPerMatch, double trueShootingPercentage) {
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
	public void setMinutesPlayedPerMatch(double minutesPlayedPerMatch) {
		this.minutesPlayedPerMatch = minutesPlayedPerMatch;
	}
	public double getPointPerMatch() {
		return pointPerMatch;
	}
	public void setPointPerMatch(double pointPerMatch) {
		this.pointPerMatch = pointPerMatch;
	}
	public double getReboundPerMatch() {
		return reboundPerMatch;
	}
	public void setReboundPerMatch(double reboundPerMatch) {
		this.reboundPerMatch = reboundPerMatch;
	}
	public double getAssistPerMatch() {
		return assistPerMatch;
	}
	public void setAssistPerMatch(double assistPerMatch) {
		this.assistPerMatch = assistPerMatch;
	}
	public double getInterceptionPerMatch() {
		return interceptionPerMatch;
	}
	public void setInterceptionPerMatch(double interceptionPerMatch) {
		this.interceptionPerMatch = interceptionPerMatch;
	}
	public double getBlockPerMatch() {
		return blockPerMatch;
	}
	public void setBlockPerMatch(double blockPerMatch) {
		this.blockPerMatch = blockPerMatch;
	}
	public double getLostBallPerMatch() {
		return lostBallPerMatch;
	}
	public void setLostBallPerMatch(double lostBallPerMatch) {
		this.lostBallPerMatch = lostBallPerMatch;
	}
	public double getTrueShootingPercentage() {
		return trueShootingPercentage;
	}
	public void setTrueShootingPercentage(double trueShootingPercentage) {
		this.trueShootingPercentage = trueShootingPercentage;
	}
	


	@Override
	public String toString() {
		return "Asset [note=" + note + ", minutesPlayedPerMatch=" + minutesPlayedPerMatch + ", pointPerMatch=" + pointPerMatch
				+ ", reboundPerMatch=" + reboundPerMatch + ", assistPerMatch=" + assistPerMatch
				+ ", interceptionPerMatch=" + interceptionPerMatch + ", blockPerMatch=" + blockPerMatch
				+ ", lostBallPerMatch=" + lostBallPerMatch + ", trueShootingPercentage=" + trueShootingPercentage + "]";
	}
	
	
	
	
	
}
