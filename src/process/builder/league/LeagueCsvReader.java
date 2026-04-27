package process.builder.league;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

import log.LoggerUtility;

public class LeagueCsvReader {
	private static final Logger logger = LoggerUtility.getLogger(LeagueCsvReader.class, "text");

	public static BufferedReader createReader() throws IOException {
		logger.debug("Searching for nba.csv in configured resource paths");
		Path[] candidatePaths = {
				Paths.get("src", "resources", "nba.csv"),
				Paths.get("resources", "nba.csv"),
				Paths.get("league", "resources", "nba.csv")
		};

		for (Path path : candidatePaths) {
			logger.trace("Checking CSV path " + path);
			if (Files.exists(path)) {
				logger.debug("Found league CSV at " + path);
				return new BufferedReader(new FileReader(path.toFile()));
			}
		}

		logger.warn("Unable to locate nba.csv in configured resource paths");
		throw new IOException("Impossible de trouver nba.csv dans src/resources/, resources/ ou league/resources/");
	}
}
