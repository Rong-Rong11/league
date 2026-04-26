package process.builder.calendar.generator;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import config.GameConfiguration;
import data.league.Playoff;
import data.sport.setup.Game;
import data.sport.setup.PlayoffSeries;
import data.team.Team;
import log.LoggerUtility;

public class PlayoffGameGenerator {
	private static final Logger logger = LoggerUtility.getLogger(PlayoffGameGenerator.class, "text");

	public static void generateFirstRoundPlayoffGames(Playoff playoff) {
		if (playoff == null) {
			logger.warn("Skipping first round playoff games generation because playoff is null");
			return;
		}

		ArrayList<PlayoffSeries> eastFirstRound = playoff.getEastFirstRound();
		ArrayList<PlayoffSeries> westFirstRound = playoff.getWestFirstRound();

		logger.debug("Generating first round playoff games for "
				+ eastFirstRound.size()
				+ " eastern series and "
				+ westFirstRound.size()
				+ " western series");

		generateGamesForSeries(eastFirstRound);
		generateGamesForSeries(westFirstRound);
	}

	public static void generateSecondRoundPlayoffGames(Playoff playoff) {
		if (playoff == null) {
			logger.warn("Skipping second round playoff games generation because playoff is null");
			return;
		}

		ArrayList<PlayoffSeries> eastSemis = playoff.getEastConferenceSemis();
		ArrayList<PlayoffSeries> westSemis = playoff.getWestConferenceSemis();

		logger.debug("Generating second round playoff games for "
				+ eastSemis.size()
				+ " eastern series and "
				+ westSemis.size()
				+ " western series");

		generateGamesForSeries(eastSemis);
		generateGamesForSeries(westSemis);
	}

	public static void generateConferenceFinalsPlayoffGames(Playoff playoff) {
		if (playoff == null) {
			logger.warn("Skipping conference finals playoff games generation because playoff is null");
			return;
		}

		ArrayList<PlayoffSeries> eastConferenceFinals = playoff.getEastConferenceFinals();
		ArrayList<PlayoffSeries> westConferenceFinals = playoff.getWestConferenceFinals();

		logger.debug("Generating conference finals playoff games for "
				+ eastConferenceFinals.size()
				+ " eastern series and "
				+ westConferenceFinals.size()
				+ " western series");

		generateGamesForSeries(eastConferenceFinals);
		generateGamesForSeries(westConferenceFinals);
	}

	public static void generateNbaFinalsPlayoffGames(Playoff playoff) {
		if (playoff == null) {
			logger.warn("Skipping NBA finals playoff games generation because playoff is null");
			return;
		}

		ArrayList<PlayoffSeries> nbaFinals = playoff.getNbaFinals();

		logger.debug("Generating NBA finals playoff games for " + nbaFinals.size() + " series");

		generateGamesForSeries(nbaFinals);
	}

	private static void generateGamesForSeries(ArrayList<PlayoffSeries> playoffSeriesList) {
		if (playoffSeriesList == null) {
			logger.warn("Skipping playoff games generation because series list is null");
			return;
		}

		for (PlayoffSeries playoffSeries : playoffSeriesList) {
			createGameForSeries(playoffSeries);
		}
	}

	private static void createGameForSeries(PlayoffSeries playoffSeries) {
		if (playoffSeries == null) {
			logger.warn("Skipping playoff game creation because playoff series is null");
			return;
		}

		Team higherTeam = playoffSeries.getHigherTeam();
		Team lowerTeam = playoffSeries.getLowerTeam();

		if (higherTeam == null || lowerTeam == null) {
			logger.warn("Skipping playoff game creation because higher team or lower team is null");
			return;
		}

		for (int i = 1; i <= 7; i++) {
			Game game;

			if (i == 1 || i == 2 || i == 5 || i == 7) {
				game = GameScheduleHelper.createGame(
						higherTeam,
						lowerTeam,
						GameConfiguration.GAME_INTRA_CONFERENCE);
			} else {
				game = GameScheduleHelper.createGame(
						lowerTeam,
						higherTeam,
						GameConfiguration.GAME_INTRA_CONFERENCE);
			}

			GameScheduleHelper.addGameToTeam(game, lowerTeam);
			GameScheduleHelper.addGameToTeam(game, higherTeam);
			playoffSeries.addExpectedGame(game, i);
		}
	}
}
