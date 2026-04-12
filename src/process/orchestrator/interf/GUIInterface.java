package process.orchestrator.interf;

import data.league.League;

public interface GUIInterface
            extends SimulationInterface, SeasonGetterInterface, TeamGetterInterface, MatchGetterInterface,
            DisplayInterface, LiveMatchInterface {
      League getLeague();

      int getCurrentFinanceMonth();
}
