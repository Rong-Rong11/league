package data.player;

import java.time.LocalDate;

public class Injury {
	private String injuryType; 
	private int injuryDuration ;
	
	public Injury(String injuryType, int injuryDuration) {
		this.injuryType = injuryType;
		this.injuryDuration = injuryDuration;
	}

	public String getInjuryType() {
		return injuryType;
	}

	public void setInjuryType(String injuryType) {
		this.injuryType = injuryType;
	}

	public int getInjuryDuration() {
		return injuryDuration;
	}

	public void setInjuryDuration(int injuryDuration) {
		this.injuryDuration = injuryDuration;
	}

	@Override
	public String toString() {
		return "Injury [injuryType=" + injuryType + ", injuryDuration=" + injuryDuration + "]";
	} 
	
	
	
	
	
	
	
}
