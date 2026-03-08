package test;

import data.league.League;
import process.builder.LeagueBuilder;

public class Test {

	public static void main(String[] args) {
		LeagueBuilder leagueBuilder = new LeagueBuilder();
        League league = leagueBuilder.build();
        System.out.println("Western conf: " + league.getWesternConference());
        System.out.println("Divisions: " + league.getWesternConference().getDivisions());

	}

}
