package process.orchestrator.interf;

import data.sport.setup.Game;
import process.service.live.LiveMatchState;

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
