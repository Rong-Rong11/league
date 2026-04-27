package process.orchestrator.interf;

import java.util.ArrayList;
import java.util.Map;

import data.finance.transfer.Trade;
import data.league.League;
import data.sport.setup.Game;
import data.team.Team;

public interface GUIInterface
<<<<<<< HEAD
			extends SimulationInterface, SeasonGetterInterface, TeamGetterInterface, MatchGetterInterface,
			DisplayInterface, LiveMatchInterface, FinanceNetGetterInterface, FinanceRevenueGetterInterface,
			TradeGetterInterface {
	  League getLeague();
=======
		extends SimulationInterface, SeasonGetterInterface, TeamGetterInterface, MatchGetterInterface,
		DisplayInterface, LiveMatchInterface, FinanceNetGetterInterface, FinanceRevenueGetterInterface,
		TradeGetterInterface {
	League getLeague();
>>>>>>> 1a53c982b130332ac2b35bb2a4d5c56e761b4b07

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

	public ArrayList<Trade> getTradesForTeam(Team team);

}
