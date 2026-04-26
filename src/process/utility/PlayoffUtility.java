package process.utility;

import data.sport.setup.Game;
import data.sport.setup.PlayoffSeries;

public class PlayoffUtility {
	public static boolean isEliminationGame(PlayoffSeries series) {
	  return series.getHigherTeamWins() == 3 || series.getLowerTeamWins() == 3;
	}

	public static boolean isGameSevenScenario(PlayoffSeries series) {
	  return series.getHigherTeamWins() == 3 && series.getLowerTeamWins() == 3;
	}

	public static int getTotalGamesPlayed(PlayoffSeries series) {
	  return series.getHigherTeamWins() + series.getLowerTeamWins();
	}

	public static int getNextGameNumber(PlayoffSeries series) {
	  return getTotalGamesPlayed(series) + 1;
	}

	public static boolean isSwingGame(PlayoffSeries series) {
	  return series.getHigherTeamWins() == 2 && series.getLowerTeamWins() == 2;
	}

	public static double getSeriesTensionFactor(PlayoffSeries series) {
	  if (isGameSevenScenario(series)) {
		 return 1.25;
	  }

	  if (isEliminationGame(series)) {
		 return 1.15;
	  }

	  if (isSwingGame(series)) {
		 return 1.10;
	  }

	  return 1.0;
	}

	public static String getBestOfLabel(PlayoffSeries series) {
	  if (series == null) {
		 return "";
	  }
	  return "G" + Math.min(7, getNextGameNumber(series)) + "/7";
	}

	public static String getBestOfLabel(PlayoffSeries series, Game game) {
	  int gameNumber = getGameNumber(series, game);
	  if (gameNumber == 0) {
		 return "";
	  }
	  return "G" + gameNumber + "/7";
	}

	public static int getGameNumber(PlayoffSeries series, Game game) {
	  if (series == null || game == null) {
		 return 0;
	  }
	  Game[] games = series.getExpectedGames();
	  for (int i = 0; i < games.length; i++) {
		 if (games[i] == game) {
			return i + 1;
		 }
	  }
	  return 0;
	}
}
