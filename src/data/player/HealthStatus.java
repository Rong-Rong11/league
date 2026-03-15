package data.player;
import config.HealthConfiguration;


public class HealthStatus {
	private double fatigue ; 
	private Injury injury ; 
	private boolean isInjured ;
	
	public HealthStatus() {
		fatigue = 0 ; 
		injury = new Injury(HealthConfiguration.NO_INJURY, 0) ; 
		isInjured = false ; 
	}
	public double getFatigue() {
		return fatigue;
	}
	public void setFatigue(double fatigue) {
		this.fatigue = fatigue;
	}
	public boolean isInjured() {
		return isInjured;
	}
	public void setInjured(boolean isInjured) {
		this.isInjured = isInjured;
	}
	public Injury getInjury() {
		return injury;
	}
	public void setInjury(Injury injury) {
		this.injury = injury;
	}
	@Override
	public String toString() {
		return "HealthStatus [fatigue=" + fatigue + ", injury=" + injury.toString() + ", isInjured=" + isInjured + "]";
	}
	
	
	
	
}
