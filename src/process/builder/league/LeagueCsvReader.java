package process.builder.league;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LeagueCsvReader {

	public static BufferedReader createReader() throws IOException {
		Path[] candidatePaths = {
				Paths.get("src", "resources", "nba.csv"),
				Paths.get("resources", "nba.csv"),
				Paths.get("league", "resources", "nba.csv")
		};

		for (Path path : candidatePaths) {
			if (Files.exists(path)) {
				return new BufferedReader(new FileReader(path.toFile()));
			}
		}

		throw new IOException("Impossible de trouver nba.csv dans src/resources/, resources/ ou league/resources/");
	}
}
