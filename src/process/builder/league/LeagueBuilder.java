package process.builder.league;

import java.io.BufferedReader;
import java.io.IOException;

import data.league.League;

public class LeagueBuilder {

	private LeagueRosterBuilder leagueRosterBuilder = new LeagueRosterBuilder();

	public LeagueBuilder() {

	}

	public League build() {
		League league = new League();
		try {
			BufferedReader bufferedReader = LeagueCsvReader.createReader();
			leagueRosterBuilder.buildRoster(league, bufferedReader);
			bufferedReader.close();

		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		LeagueFinanceBuilder.buildFinanceLeague(league);
		return league;
	}

}
