package process.orchestrator;

import data.finance.GameStat;
import data.sport.setup.Game;

public interface MatchQueryInterface {

   GameStat getGameStat(Game game);
}
