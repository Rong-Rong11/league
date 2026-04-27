package process.orchestrator.interf;

import java.util.Map;

import data.league.League;
import data.sport.setup.Game;

public interface GUIInterface
			extends SimulationInterface, SeasonGetterInterface, TeamGetterInterface, MatchGetterInterface,
			DisplayInterface, LiveMatchInterface, FinanceNetGetterInterface, FinanceRevenueGetterInterface {
	  League getLeague();

	  boolean hasPlayoffsStarted();

	  boolean hasPlayoffData();

	  boolean arePlayoffsFinished();

	  boolean isRegularSeasonFinished();

	  boolean hasUserConfirmedPlayoffs();

	  void setUserConfirmedPlayoffs(boolean confirmed);

	  void initializePlayoffs();

	  Map<String, String> getPlayoffPositionMap();

	  int getPlayoffQualifiedTeamCount();

	  int getPlayoffSeriesCount();

	  String getCurrentPlayoffRoundLabel();

	  String getPlayoffChampionName();

	  String getPlayoffGameLabel(Game game);

	  void simulateNextPlayoffRound();

	  int getCurrentFinanceMonth();
}
