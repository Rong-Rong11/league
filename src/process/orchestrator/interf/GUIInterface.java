package process.orchestrator.interf;

import java.util.Map;

import data.league.League;
import data.league.Playoff;
import data.league.PlayoffRound;
import data.sport.setup.Game;

public interface GUIInterface
			extends SimulationInterface, SeasonGetterInterface, TeamGetterInterface, MatchGetterInterface,
			DisplayInterface, LiveMatchInterface {
	  League getLeague();

	  Playoff getPlayoff();

	  PlayoffRound getCurrentPlayoffRound();

	  boolean hasPlayoffsStarted();

	  boolean isRegularSeasonFinished();

	  boolean hasUserConfirmedPlayoffs();

	  void setUserConfirmedPlayoffs(boolean confirmed);

	  void initializePlayoffs();

	  Map<String, String> getPlayoffPositionMap();

	  String getPlayoffGameLabel(Game game);

	  int getCurrentFinanceMonth();
}
