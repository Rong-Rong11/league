/*
	* Decompiled with CFR 0.152.
	*/
package data.player;

import data.player.Injury;

public class HealthStatus {
	private double fatigue = 0.0;
	private Injury injury = new Injury("none", 0);
	private boolean isInjured = false;

	public double getFatigue() {
		return this.fatigue;
	}

	public void setFatigue(double d) {
		this.fatigue = d;
	}

	public boolean isInjured() {
		return this.isInjured;
	}

	public void setInjured(boolean bl) {
		this.isInjured = bl;
	}

	public Injury getInjury() {
		return this.injury;
	}

	public void setInjury(Injury injury) {
		this.injury = injury;
	}

	public String toString() {
		return "HealthStatus [fatigue=" + this.fatigue + ", injury=" + this.injury.toString() + ", isInjured=" + this.isInjured + "]";
	}
}
