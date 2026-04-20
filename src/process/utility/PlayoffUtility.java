package process.utility;

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
}
