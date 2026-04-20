/*
	* Decompiled with CFR 0.152.
	*/
package data.league;

import java.time.LocalDate;

public class RegularSeason
		extends Season {
	private Ranking ranking;

	public RegularSeason(LocalDate localDate, LocalDate localDate2) {
		super(localDate, localDate2);
		this.ranking = new Ranking();
	}

	public Ranking getRanking() {
		return ranking;
	}

	public void setRanking(Ranking ranking) {
		this.ranking = ranking;
	}

}
