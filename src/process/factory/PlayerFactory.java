package process.factory;

import org.apache.log4j.Logger;

import data.player.Asset;
import data.player.Player;
import log.LoggerUtility;

public class PlayerFactory {
	private static final Logger logger = LoggerUtility.getLogger(PlayerFactory.class, "text");

	private static Double checkPlayerNote(String playerNote) {
		if (playerNote.equals("")) {
			logger.debug("Player note is empty, using default value 0.01");
			return 0.01;
		}
		logger.trace("Parsing player note " + playerNote);
		return Double.parseDouble(playerNote);
	}

	private static Double checkPlayerTrueShooting(String trueShooting) {
		if (trueShooting.equals("")) {
			logger.debug("Player true shooting is empty, using default value 0.01");
			return 0.01;
		}
		logger.trace("Parsing player true shooting " + trueShooting);
		return Double.parseDouble(trueShooting);
	}

	public static Player createPlayer(String line) {
		logger.trace("Parsing player CSV line");
		String[] data = line.split(",", -1);
		if (data.length <= 32) {
			logger.warn("Player CSV line has " + data.length + " fields, expected at least 33");
		}

		String playerId = data[0];
		String playerName = data[1];
		logger.debug("Creating player " + playerName + " with id " + playerId);
		boolean playerIsStar = Boolean.parseBoolean(data[13]);
		double playerNote = checkPlayerNote(data[14]);

		double minutesPlayedPerMatch = Float.valueOf(data[24]);
		double pointPerMatch = Double.parseDouble(data[25]);
		double reboundPerMatch = Double.parseDouble(data[26]);
		double assistPerMatch = Double.parseDouble(data[27]);
		double interceptionPerMatch = Double.parseDouble(data[28]);
		double blockPerMatch = Double.parseDouble(data[29]);
		double lostBallPerMatch = Double.parseDouble(data[30]);
		double salary = Double.parseDouble(data[31]) / 1_000_000;
		String playerPosition = data[32];
		double trueShooting = checkPlayerTrueShooting(data[23]);

		logger.debug("Player data parsed for "
				+ playerName
				+ " with note "
				+ playerNote
				+ ", position "
				+ playerPosition
				+ ", salary "
				+ salary
				+ "M, star "
				+ playerIsStar);

		Asset preSeasonAsset = new Asset(playerNote, minutesPlayedPerMatch, pointPerMatch, reboundPerMatch,
				assistPerMatch,
				interceptionPerMatch, blockPerMatch, lostBallPerMatch, trueShooting);
		Player player = new Player(playerId, playerName, playerNote, playerPosition, preSeasonAsset, salary,
				playerIsStar);

		logger.debug("Created player " + playerName + " with id " + playerId);
		return player;

	}
}
