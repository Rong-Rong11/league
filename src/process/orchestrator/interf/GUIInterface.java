package process.orchestrator.interf;

import java.util.Map;

import data.league.League;
import data.league.Playoff;
import data.league.PlayoffRound;
import data.team.Team;

public interface GUIInterface
			extends SimulationInterface, SeasonGetterInterface, TeamGetterInterface, MatchGetterInterface,
			DisplayInterface, LiveMatchInterface {
	  League getLeague();

	  Playoff getPlayoff();

	  PlayoffRound getCurrentPlayoffRound();

	  Team getPlayoffChampion();

	  boolean hasPlayoffsStarted();

	  boolean isRegularSeasonFinished();

	  boolean hasUserConfirmedPlayoffs();

	  void setUserConfirmedPlayoffs(boolean confirmed);

	  void initializePlayoffs();

	  Map<String, String> getPlayoffPositionMap();

	  void simulateNextPlayoffMatch();

	  void simulateNextPlayoffRound();

	  void simulateAllPlayoffs();

	  int getRemainingPlayoffGames();

	  String getLastPlayoffWinnerName();

	  int getCurrentFinanceMonth();
}
