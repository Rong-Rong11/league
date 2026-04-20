package process.service.live;

import java.util.ArrayList;

import config.GameConfiguration;
import data.sport.play.action.ActionResult;
import data.sport.setup.Game;
import data.sport.setup.GameResult;
import process.visitor.actionresult.LiveActionTextVisitor;

public class LiveMatchService {
private static final int LIVE_ROWS = 10;
private static final int GAME_SECONDS_PER_TICK = 2;

private Game game;
private String homeTeamName;
private String awayTeamName;
private ArrayList<LiveMatchStatistics.LiveAction> liveActions;
private LiveMatchStatistics liveMatchStatistics;
private int liveActionIndex;
private int displayedQuarter;
private int displayedRemainingTimeSeconds;
private int currentActionRemainingTimeSeconds;
private boolean running;

public LiveMatchService() {
	homeTeamName = "HOME";
	awayTeamName = "AWAY";
	liveActions = new ArrayList<LiveMatchStatistics.LiveAction>();
	liveMatchStatistics = new LiveMatchStatistics();
	resetLiveMatch();
}

public void setGame(Game game) {
	pauseLiveMatch();
	this.game = game;
	if (game == null) {
		homeTeamName = "HOME";
		awayTeamName = "AWAY";
	} else {
		homeTeamName = game.getGameContext().getHomeTeam().getName();
		awayTeamName = game.getGameContext().getAwayTeam().getName();
	}
	liveMatchStatistics.setGame(game);
	buildLiveActions();
	resetLiveMatch();
}

public boolean isLiveMatchAvailable(Game game) {
	return isGameSimulated(game);
}

public void startLiveMatch() {
	if (!isMatchAvailable() || running) {
		return;
	}
	if (liveActionIndex >= liveActions.size()) {
		resetLiveMatch();
	}

	running = true;
	if (liveActionIndex < liveActions.size()) {
		LiveMatchStatistics.LiveAction currentAction = liveActions.get(liveActionIndex);
		displayedQuarter = currentAction.getQuarter();
		if (liveActionIndex == 0) {
			displayedRemainingTimeSeconds = GameConfiguration.QUARTER_DURATION;
		}
		currentActionRemainingTimeSeconds = Math.max(1, currentAction.getAction().getActionTime());
	}
}

public void pauseLiveMatch() {
	running = false;
}

public void playCurrentLiveQuarter() {
	pauseLiveMatch();
	if (!isMatchAvailable() || liveActionIndex >= liveActions.size()) {
		return;
	}
	int quarterToPlay = liveActions.get(liveActionIndex).getQuarter();
	while (liveActionIndex < liveActions.size() && liveActions.get(liveActionIndex).getQuarter() == quarterToPlay) {
		playNextAction();
	}
}

public void resetLiveMatch() {
	pauseLiveMatch();
	liveActionIndex = 0;
	liveMatchStatistics.reset();
	displayedQuarter = 1;
	displayedRemainingTimeSeconds = GameConfiguration.QUARTER_DURATION;
	currentActionRemainingTimeSeconds = liveActions.isEmpty()
			? 0
			: Math.max(1, liveActions.get(0).getAction().getActionTime());
}

public void tickLiveMatch() {
	if (!running) {
		return;
	}
	if (!isMatchAvailable() || liveActionIndex >= liveActions.size()) {
		pauseLiveMatch();
		return;
	}

	decrementChronometer();
	if (!running) {
		return;
	}
	if (currentActionRemainingTimeSeconds <= 0) {
		playNextAction();
	}
}

public boolean isRunning() {
	return running;
}

public LiveMatchState getCurrentState() {
	LiveMatchState state = new LiveMatchState();

	if (game != null) {
		state.setHomeTeam(game.getGameContext().getHomeTeam());
		state.setAwayTeam(game.getGameContext().getAwayTeam());
	}

	state.setHomePoints(liveMatchStatistics.getHomePoints());
	state.setAwayPoints(liveMatchStatistics.getAwayPoints());
	state.setHomeRebounds(liveMatchStatistics.getHomeRebounds());
	state.setAwayRebounds(liveMatchStatistics.getAwayRebounds());
	state.setHomeAssists(liveMatchStatistics.getHomeAssists());
	state.setAwayAssists(liveMatchStatistics.getAwayAssists());
	state.setHomeTurnovers(liveMatchStatistics.getHomeTurnovers());
	state.setAwayTurnovers(liveMatchStatistics.getAwayTurnovers());
	state.setHomeFgPercent(liveMatchStatistics.getHomeFgPercent());
	state.setAwayFgPercent(liveMatchStatistics.getAwayFgPercent());
	state.setHomeThreePercent(liveMatchStatistics.getHomeThreePercent());
	state.setAwayThreePercent(liveMatchStatistics.getAwayThreePercent());
	state.setHomeBestPlayers(liveMatchStatistics.getHomeBestPlayers());
	state.setAwayBestPlayers(liveMatchStatistics.getAwayBestPlayers());
	state.setQuarterLabel(buildQuarterLabel());
	state.setQuarterTimeText(buildQuarterTimeText());
	state.setDisplayedRows(buildDisplayedRows());
	state.setCenterMessage(buildCenterMessage());

	return state;
}

private void buildLiveActions() {
	liveActions.clear();
	if (game == null || game.getQuarterResults() == null) {
		return;
	}
	GameResult[] quarterResults = game.getQuarterResults();
	for (int quarterIndex = 0; quarterIndex < quarterResults.length; quarterIndex++) {
		GameResult quarter = quarterResults[quarterIndex];
		if (quarter == null || quarter.getActions() == null) {
			continue;
		}
		int remainingTime = GameConfiguration.QUARTER_DURATION;
		for (ActionResult action : quarter.getActions()) {
			remainingTime -= action.getActionTime();
			if (remainingTime < 0) {
			remainingTime = 0;
			}
			liveActions.add(new LiveMatchStatistics.LiveAction(quarterIndex + 1, action, remainingTime));
		}
	}
}

private void playNextAction() {
	if (!isMatchAvailable()) {
		pauseLiveMatch();
		return;
	}
	if (liveActionIndex >= liveActions.size()) {
		revealCurrentGame();
		pauseLiveMatch();
		return;
	}

	LiveMatchStatistics.LiveAction liveAction = liveActions.get(liveActionIndex);
	liveMatchStatistics.applyAction(liveAction.getAction());
	liveActionIndex++;
	displayedQuarter = liveAction.getQuarter();
	displayedRemainingTimeSeconds = liveAction.getRemainingTimeSeconds();
	if (liveActionIndex >= liveActions.size()) {
		revealCurrentGame();
		pauseLiveMatch();
	} else {
		LiveMatchStatistics.LiveAction nextAction = liveActions.get(liveActionIndex);
		currentActionRemainingTimeSeconds = Math.max(1, nextAction.getAction().getActionTime());
		if (nextAction.getQuarter() != displayedQuarter) {
			displayedQuarter = nextAction.getQuarter();
			displayedRemainingTimeSeconds = GameConfiguration.QUARTER_DURATION;
		}
	}
}

private void decrementChronometer() {
	if (!isMatchAvailable() || liveActionIndex >= liveActions.size()) {
		pauseLiveMatch();
		return;
	}
	if (displayedRemainingTimeSeconds > 0) {
		displayedRemainingTimeSeconds -= GAME_SECONDS_PER_TICK;
		if (displayedRemainingTimeSeconds < 0) {
			displayedRemainingTimeSeconds = 0;
		}
	}
	currentActionRemainingTimeSeconds -= GAME_SECONDS_PER_TICK;
}

private String buildQuarterLabel() {
	if (!isMatchAvailable()) {
		return "Q-";
	}
	if (liveActionIndex >= liveActions.size()) {
		return "FIN";
	}
	return "Q" + displayedQuarter;
}

private String buildQuarterTimeText() {
	if (!isMatchAvailable()) {
		return "--:--";
	}
	int min = displayedRemainingTimeSeconds / 60;
	int sec = displayedRemainingTimeSeconds % 60;
	return String.format("%d:%02d", min, sec);
}

private String[] buildDisplayedRows() {
	String[] rows = new String[LIVE_ROWS];
	for (int i = 0; i < LIVE_ROWS; i++) {
		rows[i] = " ";
	}
	if (!isMatchAvailable()) {
		return rows;
	}
	int startIndex = Math.max(0, liveActionIndex - LIVE_ROWS);
	int rowIndex = LIVE_ROWS - (liveActionIndex - startIndex);
	for (int actionIndex = startIndex; actionIndex < liveActionIndex; actionIndex++) {
		rows[rowIndex] = buildActionLabel(liveActions.get(actionIndex));
		rowIndex++;
	}
	return rows;
}

private String buildCenterMessage() {
	if (game == null) {
		return "Aucun match selectionne.";
	}
	if (!isMatchAvailable()) {
		return "Match non disponible.";
	}
	if (liveActionIndex == 0) {
		return "Clique sur Play pour lancer le match.";
	}
	return "";
}

private String buildActionLabel(LiveMatchStatistics.LiveAction liveAction) {
	ActionResult action = liveAction.getAction();
	int remaining = liveAction.getRemainingTimeSeconds();
	int min = remaining / 60;
	int sec = remaining % 60;
	return "Q" + liveAction.getQuarter() + " " + String.format("%d:%02d", min, sec) + " - "
			+ action.accept(new LiveActionTextVisitor(game, homeTeamName, awayTeamName));
}

private boolean isGameSimulated(Game game) {
	if (game == null || game.getQuarterResults() == null || game.getQuarterResults().length == 0) {
		return false;
	}
	for (GameResult quarterResult : game.getQuarterResults()) {
		if (quarterResult == null || quarterResult.getActions() == null || quarterResult.getActions().isEmpty()) {
			return false;
		}
	}
	return true;
}

private boolean isMatchAvailable() {
	return isGameSimulated(game) && !liveActions.isEmpty();
}

private void revealCurrentGame() {
	if (game != null) {
		game.setDisplayed(true);
	}
}
}
