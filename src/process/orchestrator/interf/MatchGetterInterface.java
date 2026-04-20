package process.orchestrator.interf;

import data.finance.GameStat;
import data.sport.setup.Game;

public interface MatchGetterInterface {

	GameStat getGameStat(Game game);
}
