package test.support;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

import config.GameConfiguration;
import data.calendar.GameDay;
import data.calendar.NBACalendar;
import data.finance.GameStat;
import data.finance.budget.Budget;
import data.league.League;
import data.player.Player;
import data.sport.play.OffensiveTry;
import data.sport.play.action.EndOfTime;
import data.sport.play.action.PointScored;
import data.sport.play.action.Rebound;
import data.sport.play.action.Turnover;
import data.sport.setup.Game;
import data.sport.setup.GameContext;
import data.sport.setup.GameResult;
import data.team.Team;
import process.builder.league.LeagueBuilder;
import process.repository.DivisionRepository;
import process.repository.PlayerRepository;
import process.repository.TeamRepository;
import process.service.finance.initialization.FinanceInitializer;
import process.utility.FinanceUtility;

public class TestSupport {

	public static void clearRepositories() {
		PlayerRepository.getInstance().clear();
		TeamRepository.getInstance().clear();
		DivisionRepository.getInstance().clear();
	}

	public static League buildLeagueWithFinance() {
		PlayerRepository.getInstance().clear();
		TeamRepository.getInstance().clear();
		DivisionRepository.getInstance().clear();
		League league = new LeagueBuilder().build();
		new FinanceInitializer().initializeFinance();
		return league;
	}

	public static ArrayList<Team> firstTeams(League league, int count) {
		ArrayList<Team> teams = new ArrayList<Team>(league.getAllTeam());
		return new ArrayList<Team>(teams.subList(0, count));
	}

	public static Game createGame(Team homeTeam, Team awayTeam, int typeGame) {
		return new Game(new GameContext(homeTeam, awayTeam, typeGame));
	}

	public static Game createInterConferenceGame(Team homeTeam, Team awayTeam) {
		return createGame(homeTeam, awayTeam, GameConfiguration.GAME_INTER_CONFERENCE);
	}

	public static GameStat createGameStat(Game game, int attendees, double attendanceRate, double popularity) {
		GameStat gameStat = new GameStat(game);
		gameStat.setAttendees(attendees);
		gameStat.setAttendanceRate(attendanceRate);
		gameStat.setPopularity(popularity);
		return gameStat;
	}

	public static void resetBudget(Budget budget) {
		budget.getMonthlyIncomes().clear();
		budget.getMonthlyExpenses().clear();
		FinanceUtility.initiateBudget(budget);
		budget.setRemainingAmount(budget.getInitialAmount());
	}

	public static void setRegularSeasonCalendar(League league, GameDay... gameDays) {
		TreeMap<LocalDate, GameDay> calendar = new TreeMap<LocalDate, GameDay>();
		for (GameDay gameDay : gameDays) {
			calendar.put(gameDay.getDate(), gameDay);
		}
		league.getRegularSeason().setNbaCalendar(new NBACalendar(calendar));
	}

	public static void setPlayoffCalendar(League league, GameDay... gameDays) {
		TreeMap<LocalDate, GameDay> calendar = new TreeMap<LocalDate, GameDay>();
		for (GameDay gameDay : gameDays) {
			calendar.put(gameDay.getDate(), gameDay);
		}
		league.getPlayoff().setNbaCalendar(new NBACalendar(calendar));
	}

	public static void markGameAsSimulated(Game game) {
		GameResult[] quarterResults = new GameResult[4];
		for (int index = 0; index < quarterResults.length; index++) {
			GameResult gameResult = new GameResult();
			EndOfTime action = new EndOfTime("end");
			action.setActionTime(1);
			gameResult.addActions(action);
			quarterResults[index] = gameResult;
		}
		game.setQuarterResults(quarterResults);
	}

	public static Game createScriptedLiveGame(Team homeTeam, Team awayTeam) {
		Game game = createInterConferenceGame(homeTeam, awayTeam);
		Player homePlayer = homeTeam.getCurrentPlayers().values().iterator().next();
		Player awayPlayer = awayTeam.getCurrentPlayers().values().iterator().next();

		GameResult firstQuarter = new GameResult();
		PointScored homeThree = new PointScored("score", 3, homePlayer, null);
		homeThree.setOffensiveAction(new OffensiveTry(config.GameConfiguration.THREEPOINT));
		homeThree.setActionTime(1);
		firstQuarter.addActions(homeThree);

		GameResult secondQuarter = new GameResult();
		Rebound awayRebound = new Rebound("rebound", awayPlayer, homePlayer);
		awayRebound.setActionTime(1);
		secondQuarter.addActions(awayRebound);

		GameResult thirdQuarter = new GameResult();
		Turnover awayTurnover = new Turnover("turnover", homePlayer, awayPlayer);
		awayTurnover.setActionTime(1);
		thirdQuarter.addActions(awayTurnover);

		GameResult fourthQuarter = new GameResult();
		PointScored awayTwo = new PointScored("score", 2, awayPlayer, null);
		awayTwo.setOffensiveAction(new OffensiveTry(config.GameConfiguration.TWOPOINT));
		awayTwo.setActionTime(1);
		fourthQuarter.addActions(awayTwo);

		game.setQuarterResults(new GameResult[] { firstQuarter, secondQuarter, thirdQuarter, fourthQuarter });
		return game;
	}
}
