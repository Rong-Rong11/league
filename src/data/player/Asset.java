package data.player;

public class Asset {
	private double note;
	private double minutesPlayedPerMatch;
	private double pointPerMatch;
	private double reboundPerMatch;
	private double assistPerMatch;
	private double interceptionPerMatch;
	private double blockPerMatch;
	private double lostBallPerMatch;
	private double trueShootingPercentage;
	private double twoPointAttemptPerMatch;
	private double threePointAttemptPerMatch;
	private double freeThrowAttemptPerMatch;

	public Asset(double note, double minutesPlayedPerMatch, double pointPerMatch, double reboundPerMatch,
			double assistPerMatch, double interceptionPerMatch, double blockPerMatch, double lostBallPerMatch,
			double trueShootingPercentage) {
		this.note = note;
		this.minutesPlayedPerMatch = minutesPlayedPerMatch;
		this.pointPerMatch = pointPerMatch;
		this.reboundPerMatch = reboundPerMatch;
		this.assistPerMatch = assistPerMatch;
		this.interceptionPerMatch = interceptionPerMatch;
		this.blockPerMatch = blockPerMatch;
		this.lostBallPerMatch = lostBallPerMatch;
		this.trueShootingPercentage = trueShootingPercentage;
		this.twoPointAttemptPerMatch = 0.0;
		this.threePointAttemptPerMatch = 0.0;
		this.freeThrowAttemptPerMatch = 0.0;
	}

	public Asset() {
		this(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
	}

	public double getNote() {
		return this.note;
	}

	public void setNote(double note) {
		this.note = note;
	}

	public double getMinutesPlayedPerMatch() {
		return this.minutesPlayedPerMatch;
	}

	public void setMinutesPlayedPerMatch(double minutesPlayedPerMatch) {
		this.minutesPlayedPerMatch = minutesPlayedPerMatch;
	}

	public double getPointPerMatch() {
		return this.pointPerMatch;
	}

	public void setPointPerMatch(double pointPerMatch) {
		this.pointPerMatch = pointPerMatch;
	}

	public double getReboundPerMatch() {
		return this.reboundPerMatch;
	}

	public void setReboundPerMatch(double reboundPerMatch) {
		this.reboundPerMatch = reboundPerMatch;
	}

	public double getAssistPerMatch() {
		return this.assistPerMatch;
	}

	public void setAssistPerMatch(double assistPerMatch) {
		this.assistPerMatch = assistPerMatch;
	}

	public double getInterceptionPerMatch() {
		return this.interceptionPerMatch;
	}

	public void setInterceptionPerMatch(double interceptionPerMatch) {
		this.interceptionPerMatch = interceptionPerMatch;
	}

	public double getBlockPerMatch() {
		return this.blockPerMatch;
	}

	public void setBlockPerMatch(double blockPerMatch) {
		this.blockPerMatch = blockPerMatch;
	}

	public double getLostBallPerMatch() {
		return this.lostBallPerMatch;
	}

	public void setLostBallPerMatch(double lostBallPerMatch) {
		this.lostBallPerMatch = lostBallPerMatch;
	}

	public double getTrueShootingPercentage() {
		return this.trueShootingPercentage;
	}

	public void setTrueShootingPercentage(double trueShootingPercentage) {
		this.trueShootingPercentage = trueShootingPercentage;
	}

	public String toString() {
		return "Asset [note=" + this.note + ", minutesPlayedPerMatch=" + this.minutesPlayedPerMatch + ", pointPerMatch=" + this.pointPerMatch + ", reboundPerMatch=" + this.reboundPerMatch + ", assistPerMatch=" + this.assistPerMatch + ", interceptionPerMatch=" + this.interceptionPerMatch + ", blockPerMatch=" + this.blockPerMatch + ", lostBallPerMatch=" + this.lostBallPerMatch + ", trueShootingPercentage=" + this.trueShootingPercentage + ", twoPointAttemptPerMatch=" + this.twoPointAttemptPerMatch + ", threePointAttemptPerMatch=" + this.threePointAttemptPerMatch + ", freeThrowAttemptPerMatch=" + this.freeThrowAttemptPerMatch + "]";
	}

	public double getTwoPointAttemptPerMatch() {
		return this.twoPointAttemptPerMatch;
	}

	public void setTwoPointAttemptPerMatch(double twoPointAttemptPerMatch) {
		this.twoPointAttemptPerMatch = twoPointAttemptPerMatch;
	}

	public double getThreePointAttemptPerMatch() {
		return this.threePointAttemptPerMatch;
	}

	public void setThreePointAttemptPerMatch(double threePointAttemptPerMatch) {
		this.threePointAttemptPerMatch = threePointAttemptPerMatch;
	}

	public double getFreeThrowAttemptPerMatch() {
		return this.freeThrowAttemptPerMatch;
	}

	public void setFreeThrowAttemptPerMatch(double freeThrowAttemptPerMatch) {
		this.freeThrowAttemptPerMatch = freeThrowAttemptPerMatch;
	}
}
