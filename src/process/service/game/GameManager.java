package process.service.game;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

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

public class GameManager {

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
		this.league = league;
		ArrayList<Team> eastTeams = new ArrayList<>();
		ArrayList<Team> westTeams = new ArrayList<>();
		LeagueUtility.getConferenceTeams(league, eastTeams, westTeams);
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
	}

	public boolean simulateRegularSeasonDay(LocalDate date, int month) {
		RegularSeason regularSeason = league.getRegularSeason();
		TreeMap<LocalDate, GameDay> regularSeasonCalendar = regularSeason.getNbaCalendar().getCalendar();
		GameDay gameDay = regularSeasonCalendar.get(date);

		if (gameDay != null && !gameDay.isSimulated()) {
			GameDaySimulationProcessor processor = new RegularSeasonGameDaySimulationProcessor(
					league,
					gameSimulator,
					financeManager,
					regularSeasonRankingManager);

			processor.simulateGameDay(gameDay, date, month);
			return true;
		}

		return false;
	}

	public void simulatePlayoffDay(LocalDate date, int month, PlayoffRound currentRound) {
		if (currentRound == null) {
			return;
		}

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
				break;
		}
	}

	private void simulateManagedPlayoffDay(LocalDate date, int month, PlayoffManager playoffManager,
			PlayoffRound round) {
		Playoff playoff = league.getPlayoff();
		TreeMap<LocalDate, GameDay> playoffCalendar = playoff.getNbaCalendar().getCalendar();
		GameDay gameDay = playoffCalendar.get(date);

		if (gameDay != null && !gameDay.isSimulated()) {
			GameDaySimulationProcessor processor = new PlayoffGameDaySimulationProcessor(
					gameSimulator, financeManager, playoffManager, round);
			processor.simulateGameDay(gameDay, date, month);
		}
	}

	public Team getRegularSeasonWestWinner() {
		return league.getRegularSeason().getRanking().getWestRanking().get(1);
	}

	public Team getRegularSeasonEastWinner() {
		return league.getRegularSeason().getRanking().getEastRanking().get(1);
	}

	public ArrayList<Team> getGlobalRanking() {
		return regularSeasonRankingManager.getGlobalRanking(league);
	}

	public ArrayList<Team> getEastRanking() {
		return regularSeasonRankingManager.getEastRanking();
	}

	public ArrayList<Team> getWestRanking() {
		return regularSeasonRankingManager.getWestRanking();
	}

	public void simulateFirstRoundDay(LocalDate date, int month) {
		simulateManagedPlayoffDay(date, month, firstRoundPlayoffManager);
	}

	public void simulateSemiRoundDay(LocalDate date, int month) {
		simulateManagedPlayoffDay(date, month, semiPlayoffManager);
	}

	public void simulateConferenceFinalRoundDay(LocalDate date, int month) {
		simulateManagedPlayoffDay(date, month, conferenceFinalPlayoffManager);
	}

	public void simulateNbaFinalRoundDay(LocalDate date, int month) {
		simulateManagedPlayoffDay(date, month, nbaFinalPlayoffManager);
	}

	private void simulateManagedPlayoffDay(LocalDate date, int month, PlayoffManager playoffManager) {
		Playoff playoff = league.getPlayoff();
		TreeMap<LocalDate, GameDay> playoffCalendar = playoff.getNbaCalendar().getCalendar();
		GameDay gameDay = playoffCalendar.get(date);
		if (gameDay != null && !gameDay.isSimulated()) {
			simulateGameDay(gameDay, date, month);
			for (Game game : gameDay.getGames()) {
				playoffManager.handlePlayedGame(game, date);
			}
		}
	}

	private void simulateGameDay(GameDay gameDay, LocalDate date, int month) {
		for (Game game : gameDay.getGames()) {
			gameSimulator.simulateGame(game);
			financeManager.calculatePlayoffGame(game, date, month, league.getPlayoff().getCurrentRound());
		}
		gameDay.setSimulated(true);

	}
}
