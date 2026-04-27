package data.player;


public class HealthStatus {
	private double fatigue = 0.0;
	private Injury injury = new Injury("none", 0);
	private boolean isInjured = false;

	public double getFatigue() {
		return this.fatigue;
	}

	public void setFatigue(double fatigue) {
		this.fatigue = fatigue;
	}

	public boolean isInjured() {
		return this.isInjured;
	}

	public void setInjured(boolean injured) {
		this.isInjured = injured;
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
