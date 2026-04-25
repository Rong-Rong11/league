package process.builder.calendar.selector;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import config.CalendarConfiguration;
import data.league.League;
import data.sport.setup.Game;
import log.LoggerUtility;

public class GameSelector {
	private static final Logger logger = LoggerUtility.getLogger(GameSelector.class, "text");

	private LocalDate date;
	private GameCandidateFinder candidateFinder;
	private GameScoreCalculator scoreCalculator;
	private GameScheduleConstraintChecker constraintChecker;

	public GameSelector(LocalDate date, League league) {
		super();
		this.date = date;
		this.constraintChecker = new GameScheduleConstraintChecker();
		this.candidateFinder = new GameCandidateFinder(constraintChecker);
		this.scoreCalculator = new GameScoreCalculator(league.getRegularSeason(), constraintChecker);
	}

	public ArrayList<Game> selectGamesForDay() {
		logger.debug("Selecting games for " + date);
		ArrayList<Game> selectedGames = new ArrayList<Game>();
		ArrayList<Game> candidates = candidateFinder.getCandidates(date);
		logger.debug("Found " + candidates.size() + " candidate games for " + date);
		Collections.shuffle(candidates);

		TreeMap<Double, ArrayList<Game>> scoreMap = new TreeMap<Double, ArrayList<Game>>();
		for (Game game : candidates) {
			double totalScore = scoreCalculator.calculateScore(game, date);
			if (scoreMap.containsKey(totalScore)) {
				scoreMap.get(totalScore).add(game);
			} else {
				ArrayList<Game> list = new ArrayList<Game>();
				list.add(game);
				scoreMap.put(totalScore, list);
			}
		}

		for (Double score : scoreMap.descendingKeySet()) {
			for (Game game : scoreMap.get(score)) {
				if (selectedGames.size() >= CalendarConfiguration.MAX_GAMES_PER_DAY) {
					break;
				}
				if (constraintChecker.conflictWithSelected(game, selectedGames)) {
					logger.trace("Skipping game "
							+ game.getGameContext().getHomeTeam().getName()
							+ " vs "
							+ game.getGameContext().getAwayTeam().getName()
							+ " because of conflict with selected games");
					continue;
				}
				selectedGames.add(game);
			}

		}
		logger.debug("Selected " + selectedGames.size() + " games for " + date);
		logger.debug("Game selection completed for " + date);
		return selectedGames;
	}

	public void setDate(LocalDate date) {
		this.date = date;
		logger.debug("Game selector date set to " + date);
	}

}
