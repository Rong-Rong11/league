/*
	* Decompiled with CFR 0.152.
	*/
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

	public Asset(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
		this.note = d;
		this.minutesPlayedPerMatch = d2;
		this.pointPerMatch = d3;
		this.reboundPerMatch = d4;
		this.assistPerMatch = d5;
		this.interceptionPerMatch = d6;
		this.blockPerMatch = d7;
		this.lostBallPerMatch = d8;
		this.trueShootingPercentage = d9;
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

	public void setNote(double d) {
		this.note = d;
	}

	public double getMinutesPlayedPerMatch() {
		return this.minutesPlayedPerMatch;
	}

	public void setMinutesPlayedPerMatch(double d) {
		this.minutesPlayedPerMatch = d;
	}

	public double getPointPerMatch() {
		return this.pointPerMatch;
	}

	public void setPointPerMatch(double d) {
		this.pointPerMatch = d;
	}

	public double getReboundPerMatch() {
		return this.reboundPerMatch;
	}

	public void setReboundPerMatch(double d) {
		this.reboundPerMatch = d;
	}

	public double getAssistPerMatch() {
		return this.assistPerMatch;
	}

	public void setAssistPerMatch(double d) {
		this.assistPerMatch = d;
	}

	public double getInterceptionPerMatch() {
		return this.interceptionPerMatch;
	}

	public void setInterceptionPerMatch(double d) {
		this.interceptionPerMatch = d;
	}

	public double getBlockPerMatch() {
		return this.blockPerMatch;
	}

	public void setBlockPerMatch(double d) {
		this.blockPerMatch = d;
	}

	public double getLostBallPerMatch() {
		return this.lostBallPerMatch;
	}

	public void setLostBallPerMatch(double d) {
		this.lostBallPerMatch = d;
	}

	public double getTrueShootingPercentage() {
		return this.trueShootingPercentage;
	}

	public void setTrueShootingPercentage(double d) {
		this.trueShootingPercentage = d;
	}

	public String toString() {
		return "Asset [note=" + this.note + ", minutesPlayedPerMatch=" + this.minutesPlayedPerMatch + ", pointPerMatch=" + this.pointPerMatch + ", reboundPerMatch=" + this.reboundPerMatch + ", assistPerMatch=" + this.assistPerMatch + ", interceptionPerMatch=" + this.interceptionPerMatch + ", blockPerMatch=" + this.blockPerMatch + ", lostBallPerMatch=" + this.lostBallPerMatch + ", trueShootingPercentage=" + this.trueShootingPercentage + ", twoPointAttemptPerMatch=" + this.twoPointAttemptPerMatch + ", threePointAttemptPerMatch=" + this.threePointAttemptPerMatch + ", freeThrowAttemptPerMatch=" + this.freeThrowAttemptPerMatch + "]";
	}

	public double getTwoPointAttemptPerMatch() {
		return this.twoPointAttemptPerMatch;
	}

	public void setTwoPointAttemptPerMatch(double d) {
		this.twoPointAttemptPerMatch = d;
	}

	public double getThreePointAttemptPerMatch() {
		return this.threePointAttemptPerMatch;
	}

	public void setThreePointAttemptPerMatch(double d) {
		this.threePointAttemptPerMatch = d;
	}

	public double getFreeThrowAttemptPerMatch() {
		return this.freeThrowAttemptPerMatch;
	}

	public void setFreeThrowAttemptPerMatch(double d) {
		this.freeThrowAttemptPerMatch = d;
	}
}
