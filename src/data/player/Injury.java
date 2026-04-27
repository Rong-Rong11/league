package data.player;

public class Injury {
	private String injuryType;
	private int injuryDuration;

	public Injury(String injuryType, int injuryDuration) {
		this.injuryType = injuryType;
		this.injuryDuration = injuryDuration;
	}

	public String getInjuryType() {
		return this.injuryType;
	}

	public void setInjuryType(String injuryType) {
		this.injuryType = injuryType;
	}

	public int getInjuryDuration() {
		return this.injuryDuration;
	}

	public void setInjuryDuration(int injuryDuration) {
		this.injuryDuration = injuryDuration;
	}

	public String toString() {
		return "Injury [injuryType=" + this.injuryType + ", injuryDuration=" + this.injuryDuration + "]";
	}
}
