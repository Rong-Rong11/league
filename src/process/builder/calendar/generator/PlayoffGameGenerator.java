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
		logger.info("Generating first round playoff games");
		ArrayList<PlayoffSeries> eastFirstRound = playoff.getEastFirstRound();
		ArrayList<PlayoffSeries> westFirstRound = playoff.getWestFirstRound();
		logger.debug("First round contains "
				+ eastFirstRound.size()
				+ " eastern series and "
				+ westFirstRound.size()
				+ " western series");
		for (PlayoffSeries playoffSeries : eastFirstRound) {
			createGameForSeries(playoffSeries);
		}
		for (PlayoffSeries playoffSeries : westFirstRound) {
			createGameForSeries(playoffSeries);
		}
		logger.info("First round playoff games generated");
	}

	public static void generateSecondRoundPlayoffGames(Playoff playoff) {
		logger.info("Generating second round playoff games");
		ArrayList<PlayoffSeries> eastSemis = playoff.getEastConferenceSemis();
		ArrayList<PlayoffSeries> westSemis = playoff.getWestConferenceSemis();
		logger.debug("Second round contains "
				+ eastSemis.size()
				+ " eastern series and "
				+ westSemis.size()
				+ " western series");
		for (PlayoffSeries playoffSeries : eastSemis) {
			createGameForSeries(playoffSeries);
		}
		for (PlayoffSeries playoffSeries : westSemis) {
			createGameForSeries(playoffSeries);
		}
		logger.info("Second round playoff games generated");
	}

	public static void generateConferenceFinalsPlayoffGames(Playoff playoff) {
		logger.info("Generating conference finals playoff games");
		ArrayList<PlayoffSeries> eastConferenceFinals = playoff.getEastConferenceFinals();
		ArrayList<PlayoffSeries> westConferenceFinals = playoff.getWestConferenceFinals();
		logger.debug("Conference finals contain "
				+ eastConferenceFinals.size()
				+ " eastern series and "
				+ westConferenceFinals.size()
				+ " western series");
		for (PlayoffSeries playoffSeries : eastConferenceFinals) {
			createGameForSeries(playoffSeries);
		}
		for (PlayoffSeries playoffSeries : westConferenceFinals) {
			createGameForSeries(playoffSeries);
		}
		logger.info("Conference finals playoff games generated");
	}

	public static void generateNbaFinalsPlayoffGames(Playoff playoff) {
		logger.info("Generating NBA finals playoff games");
		ArrayList<PlayoffSeries> nbaFinals = playoff.getNbaFinals();
		logger.debug("NBA finals contain " + nbaFinals.size() + " series");
		for (PlayoffSeries playoffSeries : nbaFinals) {
			createGameForSeries(playoffSeries);
		}
		logger.info("NBA finals playoff games generated");
	}

	private static void createGameForSeries(PlayoffSeries playoffSeries) {
		Team higherTeam = playoffSeries.getHigherTeam();
		Team lowerTeam = playoffSeries.getLowerTeam();
		logger.debug("Creating expected playoff games for series "
				+ higherTeam.getName()
				+ " vs "
				+ lowerTeam.getName());
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
			logger.trace("Creating expected playoff game "
					+ i
					+ " with home team "
					+ game.getGameContext().getHomeTeam().getName());
			GameScheduleHelper.addGameToTeam(game, lowerTeam);
			GameScheduleHelper.addGameToTeam(game, higherTeam);
			playoffSeries.addExpectedGame(game, i);
		}
		logger.debug("Created 7 expected playoff games for series "
				+ higherTeam.getName()
				+ " vs "
				+ lowerTeam.getName());
	}
}
