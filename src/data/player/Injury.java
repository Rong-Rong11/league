/*
	* Decompiled with CFR 0.152.
	*/
package data.player;

public class Injury {
	private String injuryType;
	private int injuryDuration;

	public Injury(String string, int n) {
		this.injuryType = string;
		this.injuryDuration = n;
	}

	public String getInjuryType() {
		return this.injuryType;
	}

	public void setInjuryType(String string) {
		this.injuryType = string;
	}

	public int getInjuryDuration() {
		return this.injuryDuration;
	}

	public void setInjuryDuration(int n) {
		this.injuryDuration = n;
	}

	public String toString() {
		return "Injury [injuryType=" + this.injuryType + ", injuryDuration=" + this.injuryDuration + "]";
	}
}
