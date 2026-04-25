package process.service.game;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import config.CalendarConfiguration;
import data.calendar.GameDay;
import data.league.League;
import data.league.Playoff;
import data.league.PlayoffRound;
import data.league.RegularSeason;
import data.sport.setup.Game;
import data.team.Team;
import process.builder.calendar.CalendarBuilder;
import process.builder.calendar.ConferenceFinalCalendarBuilder;
import process.builder.calendar.FirstRoundCalendarBuilder;
import process.builder.calendar.NbaFinalCalendarBuilder;
import process.builder.calendar.SemiCalendarBuilder;
import process.builder.league.PlayoffBuilder;
import process.service.finance.FinanceManager;
import process.service.game.tools.GameDaySimulationProcessor;
import process.service.game.tools.PlayoffGameDaySimulationProcessor;
import process.service.game.tools.RegularSeasonGameDaySimulationProcessor;
import process.service.league.TeamPopularityUpdater;
import process.service.playoff.ConferenceFinalPlayoffManager;
import process.service.playoff.FirstRoundPlayoffManager;
import process.service.playoff.NbaFinalPlayoffManager;
import process.service.playoff.PlayoffManager;
import process.service.playoff.SemiPlayoffManager;
import process.service.ranking.RegularSeasonRankingManager;
import process.simulator.GameSimulator;
import process.utility.LeagueUtility;
import log.LoggerUtility;

public class GameManager {
	private static final Logger logger = LoggerUtility.getLogger(GameManager.class, "text");

	private League league;
	private GameSimulator gameSimulator = new GameSimulator();
	private FinanceManager financeManager;
	private RegularSeasonRankingManager regularSeasonRankingManager;
	private FirstRoundPlayoffManager firstRoundPlayoffManager;
	private SemiPlayoffManager semiPlayoffManager;
	private ConferenceFinalPlayoffManager conferenceFinalPlayoffManager;
	private NbaFinalPlayoffManager nbaFinalPlayoffManager;

	public GameManager(League league, FinanceManager financeManager, CalendarBuilder calendarBuilder,
			PlayoffBuilder playoffBuilder, FirstRoundCalendarBuilder firstRoundCalendarBuilder,
			TeamPopularityUpdater teamPopularityUpdater) {
		logger.debug("Initializing game manager");
		if (league == null) {
			logger.warn("Game manager initialized with null league");
		}
		this.league = league;
		ArrayList<Team> eastTeams = new ArrayList<>();
		ArrayList<Team> westTeams = new ArrayList<>();
		LeagueUtility.getConferenceTeams(league, eastTeams, westTeams);
		logger.debug("Loaded " + eastTeams.size() + " east teams and " + westTeams.size() + " west teams");
		regularSeasonRankingManager = new RegularSeasonRankingManager(westTeams, eastTeams);
		this.financeManager = financeManager;
		this.firstRoundPlayoffManager = new FirstRoundPlayoffManager(league,
				firstRoundCalendarBuilder,
				playoffBuilder,
				financeManager,
				teamPopularityUpdater);
		this.semiPlayoffManager = new SemiPlayoffManager(league,
				new SemiCalendarBuilder(league, CalendarConfiguration.PLAYOFF_DEBUT_DATE),
				playoffBuilder,
				financeManager,
				teamPopularityUpdater);
		this.conferenceFinalPlayoffManager = new ConferenceFinalPlayoffManager(league,
				new ConferenceFinalCalendarBuilder(league, CalendarConfiguration.PLAYOFF_DEBUT_DATE),
				playoffBuilder,
				financeManager,
				teamPopularityUpdater);
		this.nbaFinalPlayoffManager = new NbaFinalPlayoffManager(league,
				new NbaFinalCalendarBuilder(league, CalendarConfiguration.PLAYOFF_DEBUT_DATE),
				playoffBuilder,
				financeManager,
				teamPopularityUpdater);
		logger.debug("Game manager initialized");
	}

	public boolean simulateRegularSeasonDay(LocalDate date, int month) {
		if (league == null || league.getRegularSeason() == null || league.getRegularSeason().getNbaCalendar() == null) {
			logger.warn("Skipping regular season day simulation because league, regular season or calendar is null");
			return false;
		}
		if (date == null) {
			logger.warn("Skipping regular season day simulation because date is null");
			return false;
		}
		RegularSeason regularSeason = league.getRegularSeason();
		TreeMap<LocalDate, GameDay> regularSeasonCalendar = regularSeason.getNbaCalendar().getCalendar();
		GameDay gameDay = regularSeasonCalendar.get(date);

		if (gameDay != null && !gameDay.isSimulated()) {
			logger.debug("Simulating regular season day " + date + " for month " + month);
			GameDaySimulationProcessor processor = new RegularSeasonGameDaySimulationProcessor(
					league,
					gameSimulator,
					financeManager,
					regularSeasonRankingManager);

			processor.simulateGameDay(gameDay, date, month);
			logger.debug("Regular season day simulated for " + date);
			return true;
		}
		logger.debug("No regular season games simulated for " + date);

		return false;
	}

	public void simulatePlayoffDay(LocalDate date, int month, PlayoffRound currentRound) {
		if (date == null) {
			logger.warn("Ignoring playoff day simulation because date is null");
			return;
		}
		if (currentRound == null) {
			logger.warn("Ignoring playoff day simulation because current round is null");
			return;
		}
		logger.debug("Simulating playoff day " + date + " for round " + currentRound);

		switch (currentRound) {
			case FIRST_ROUND:
				simulateManagedPlayoffDay(date, month, firstRoundPlayoffManager, currentRound);
				break;
			case CONFERENCE_SEMIFINALS:
				simulateManagedPlayoffDay(date, month, semiPlayoffManager, currentRound);
				break;
			case CONFERENCE_FINALS:
				simulateManagedPlayoffDay(date, month, conferenceFinalPlayoffManager, currentRound);
				break;
			case NBA_FINALS:
				simulateManagedPlayoffDay(date, month, nbaFinalPlayoffManager, currentRound);
				break;
			default:
				logger.warn("Ignoring playoff day simulation because round is unsupported: " + currentRound);
				break;
		}
	}

	private void simulateManagedPlayoffDay(LocalDate date, int month, PlayoffManager playoffManager,
			PlayoffRound round) {
		if (league == null || league.getPlayoff() == null || league.getPlayoff().getNbaCalendar() == null) {
			logger.warn("Skipping playoff day simulation because league, playoff or playoff calendar is null");
			return;
		}
		if (playoffManager == null) {
			logger.warn("Skipping playoff day simulation because playoff manager is null for round " + round);
			return;
		}
		Playoff playoff = league.getPlayoff();
		TreeMap<LocalDate, GameDay> playoffCalendar = playoff.getNbaCalendar().getCalendar();
		GameDay gameDay = playoffCalendar.get(date);

		if (gameDay != null && !gameDay.isSimulated()) {
			logger.debug("Simulating managed playoff day " + date + " for round " + round + " month " + month);
			GameDaySimulationProcessor processor = new PlayoffGameDaySimulationProcessor(
					gameSimulator, financeManager, playoffManager, round);
			processor.simulateGameDay(gameDay, date, month);
			logger.debug("Playoff day simulated for " + date + " in round " + round);
			return;
		}
		logger.debug("No playoff games simulated for " + date + " in round " + round);
	}

	public Team getRegularSeasonWestWinner() {
		Team winner = league.getRegularSeason().getRanking().getWestRanking().get(1);
		logger.debug("Regular season west winner is " + (winner == null ? "<none>" : winner.getName()));
		return winner;
	}

	public Team getRegularSeasonEastWinner() {
		Team winner = league.getRegularSeason().getRanking().getEastRanking().get(1);
		logger.debug("Regular season east winner is " + (winner == null ? "<none>" : winner.getName()));
		return winner;
	}

	public ArrayList<Team> getGlobalRanking() {
		ArrayList<Team> ranking = regularSeasonRankingManager.getGlobalRanking(league);
		logger.trace("Returning global ranking with " + ranking.size() + " teams");
		return ranking;
	}

	public ArrayList<Team> getEastRanking() {
		ArrayList<Team> ranking = regularSeasonRankingManager.getEastRanking();
		logger.trace("Returning east ranking with " + ranking.size() + " teams");
		return ranking;
	}

	public ArrayList<Team> getWestRanking() {
		ArrayList<Team> ranking = regularSeasonRankingManager.getWestRanking();
		logger.trace("Returning west ranking with " + ranking.size() + " teams");
		return ranking;
	}

	public void simulateFirstRoundDay(LocalDate date, int month) {
		logger.debug("Simulating first round playoff day " + date);
		simulateManagedPlayoffDay(date, month, firstRoundPlayoffManager);
	}

	public void simulateSemiRoundDay(LocalDate date, int month) {
		logger.debug("Simulating conference semifinals playoff day " + date);
		simulateManagedPlayoffDay(date, month, semiPlayoffManager);
	}

	public void simulateConferenceFinalRoundDay(LocalDate date, int month) {
		logger.debug("Simulating conference finals playoff day " + date);
		simulateManagedPlayoffDay(date, month, conferenceFinalPlayoffManager);
	}

	public void simulateNbaFinalRoundDay(LocalDate date, int month) {
		logger.debug("Simulating NBA finals playoff day " + date);
		simulateManagedPlayoffDay(date, month, nbaFinalPlayoffManager);
	}

	private void simulateManagedPlayoffDay(LocalDate date, int month, PlayoffManager playoffManager) {
		if (league == null || league.getPlayoff() == null || league.getPlayoff().getNbaCalendar() == null) {
			logger.warn("Skipping managed playoff day simulation because league, playoff or playoff calendar is null");
			return;
		}
		if (date == null || playoffManager == null) {
			logger.warn("Skipping managed playoff day simulation because date or playoff manager is null");
			return;
		}
		Playoff playoff = league.getPlayoff();
		TreeMap<LocalDate, GameDay> playoffCalendar = playoff.getNbaCalendar().getCalendar();
		GameDay gameDay = playoffCalendar.get(date);
		if (gameDay != null && !gameDay.isSimulated()) {
			logger.debug("Simulating managed playoff day " + date + " for month " + month);
			simulateGameDay(gameDay, date, month);
			for (Game game : gameDay.getGames()) {
				logger.trace("Handling played playoff game for " + date);
				playoffManager.handlePlayedGame(game, date);
			}
			logger.debug("Managed playoff day simulated for " + date);
			return;
		}
		logger.debug("No managed playoff games simulated for " + date);
	}

	private void simulateGameDay(GameDay gameDay, LocalDate date, int month) {
		if (gameDay == null) {
			logger.warn("Skipping playoff game day simulation because game day is null");
			return;
		}
		logger.trace("Simulating playoff game day " + date + " with " + gameDay.getGames().size() + " games");
		for (Game game : gameDay.getGames()) {
			if (game == null) {
				logger.warn("Skipping playoff game simulation because game is null");
				continue;
			}
			gameSimulator.simulateGame(game);
			financeManager.calculatePlayoffGame(game, date, month, league.getPlayoff().getCurrentRound());
		}
		gameDay.setSimulated(true);
		logger.trace("Playoff game day marked simulated for " + date);

	}
}
