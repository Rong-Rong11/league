package process.orchestrator.interfaces;

import data.sport.live.LiveMatchState;
import data.sport.setup.Game;

public interface LiveMatchInterface {

	boolean isLiveMatchAvailable(Game game);

	void setLiveGame(Game game);

	void startLiveMatch();

	void pauseLiveMatch();

	void playCurrentLiveQuarter();

	void resetLiveMatch();

	void tickLiveMatch();

	boolean isLiveMatchRunning();

	LiveMatchState getCurrentLiveState();
}
