package process.builder.league;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import data.league.League;
import log.LoggerUtility;

public class LeagueBuilder {
	private static final Logger logger = LoggerUtility.getLogger(LeagueBuilder.class, "text");

	private LeagueRosterBuilder leagueRosterBuilder = new LeagueRosterBuilder();

	public LeagueBuilder() {

	}

	public League build() {
		logger.info("Building league");
		League league = new League();
		try {
			logger.debug("Creating CSV reader for league roster");
			BufferedReader bufferedReader = LeagueCsvReader.createReader();
			logger.debug("Building league roster from CSV data");
			leagueRosterBuilder.buildRoster(league, bufferedReader);
			bufferedReader.close();
			logger.debug("League roster built successfully");

		} catch (IOException e) {
			logger.error("Unable to build league roster from CSV", e);
		}
		logger.debug("Building league finance data");
		LeagueFinanceBuilder.buildFinanceLeague(league);
		logger.info("League built successfully");
		return league;
	}

}
