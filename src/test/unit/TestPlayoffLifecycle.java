package test.unit;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import config.CalendarConfiguration;
import data.calendar.GameDay;
import data.league.League;
import data.league.Playoff;
import data.league.PlayoffRound;
import data.sport.setup.Game;
import data.sport.setup.PlayoffSeries;
import data.team.Team;
import process.builder.calendar.FirstRoundCalendarBuilder;
import process.builder.league.LeagueBuilder;
import process.builder.league.PlayoffBuilder;
import process.orchestrator.manager.SimulationManager;
import process.repository.DivisionRepository;
import process.repository.PlayerRepository;
import process.repository.TeamRepository;
import process.service.finance.FinanceManager;
import process.service.league.TeamPopularityUpdater;
import process.service.playoff.FirstRoundPlayoffManager;
import process.service.playoff.NbaFinalPlayoffManager;
import process.service.playoff.PlayoffManager;
import process.utility.LeagueUtility;

public class TestPlayoffLifecycle {

	@Before
	public void setUp() {
		PlayerRepository.getInstance().clear();
		TeamRepository.getInstance().clear();
		DivisionRepository.getInstance().clear();
	}

	@Test
	public void shouldBuildFirstRoundSeedsFromRegularSeasonRanking() {
		League league = buildRankedLeague();
		Playoff playoff = new PlayoffBuilder(league).buildFirstRoundPlayoffs();

		assertEquals(8, playoff.getQualifiedEastTeams().size());
		assertEquals(8, playoff.getQualifiedWestTeams().size());
		assertSame(league.getRegularSeason().getRanking().getEastRanking().get(1),
				playoff.getEastFirstRound().get(0).getHigherTeam());
		assertSame(league.getRegularSeason().getRanking().getEastRanking().get(8),
				playoff.getEastFirstRound().get(0).getLowerTeam());
		assertSame(league.getRegularSeason().getRanking().getWestRanking().get(4),
				playoff.getWestFirstRound().get(3).getHigherTeam());
		assertSame(league.getRegularSeason().getRanking().getWestRanking().get(5),
				playoff.getWestFirstRound().get(3).getLowerTeam());
	}

	@Test
	public void shouldNotStartPlayoffsTwiceAtRegularSeasonEnd() {
		SimulationManager simulationManager = new SimulationManager();
		simulationManager.startSeason();
		fillRanking(simulationManager.getLeague());

		simulationManager.endRegularSeason();
		simulationManager.endRegularSeason();

		Playoff playoff = simulationManager.getPlayoff();
		assertTrue(simulationManager.hasPlayoffsStarted());
		assertEquals(PlayoffRound.FIRST_ROUND, simulationManager.getCurrentPlayoffRound());
		assertEquals(8, playoff.getQualifiedEastTeams().size());
		assertEquals(8, playoff.getQualifiedWestTeams().size());
		assertEquals(4, playoff.getEastFirstRound().size());
		assertEquals(4, playoff.getWestFirstRound().size());
	}

	@Test
	public void shouldMarkPlayoffGamesForCalendarColoring() {
		League league = buildRankedLeague();
		league.setPlayoff(new PlayoffBuilder(league).buildFirstRoundPlayoffs());
		league.getPlayoff().setCurrentRound(PlayoffRound.FIRST_ROUND);
		league.getPlayoff().setNbaCalendar(new FirstRoundCalendarBuilder(league).buildCalendar());

		GameDay firstPlayoffDay = league.getPlayoff().getNbaCalendar().getCalendar().firstEntry().getValue();
		for (Game game : firstPlayoffDay.getGames()) {
			assertEquals(PlayoffRound.FIRST_ROUND, game.getPlayoffRound());
		}
	}

	@Test
	public void shouldNotDuplicateAlreadyScheduledFirstFourPlayoffGames() {
		League league = buildRankedLeague();
		league.setPlayoff(new PlayoffBuilder(league).buildFirstRoundPlayoffs());
		league.getPlayoff().setCurrentRound(PlayoffRound.FIRST_ROUND);
		FirstRoundCalendarBuilder calendarBuilder = new FirstRoundCalendarBuilder(league);
		league.getPlayoff().setNbaCalendar(calendarBuilder.buildCalendar());
		PlayoffSeries series = league.getPlayoff().getEastFirstRound().get(0);
		int initialScheduledOccurrences = countScheduledOccurrences(league, series.getExpectedGames()[1]);

		series.setHigherTeamWins(1);
		series.setNumberPlayedGames(1);
		calendarBuilder.scheduleNextGameIfNecessary(
				league.getPlayoff().getNbaCalendar().getCalendar(),
				series,
				CalendarConfiguration.PLAYOFF_DEBUT_DATE);

		assertEquals(initialScheduledOccurrences, countScheduledOccurrences(league, series.getExpectedGames()[1]));
	}

	@Test
	public void shouldNotBuildSecondRoundBeforeFirstRoundIsFinished() {
		League league = buildRankedLeague();
		PlayoffBuilder playoffBuilder = new PlayoffBuilder(league);
		league.setPlayoff(playoffBuilder.buildFirstRoundPlayoffs());

		try {
			playoffBuilder.buildSecondRoundPlayoffs();
			fail("Second round should not be built before first round series are finished.");
		} catch (IllegalStateException expected) {
			assertTrue(expected.getMessage().contains("round suivant"));
		}
	}

	@Test
	public void shouldScheduleGameFiveOnlyAfterFourPlayedGames() {
		League league = buildRankedLeague();
		league.setPlayoff(new PlayoffBuilder(league).buildFirstRoundPlayoffs());
		league.getPlayoff().setCurrentRound(PlayoffRound.FIRST_ROUND);
		FirstRoundCalendarBuilder calendarBuilder = new FirstRoundCalendarBuilder(league);
		league.getPlayoff().setNbaCalendar(calendarBuilder.buildCalendar());
		PlayoffSeries series = league.getPlayoff().getEastFirstRound().get(0);
		Game gameFive = series.getExpectedGames()[4];

		series.setHigherTeamWins(2);
		series.setLowerTeamWins(2);
		series.setNumberPlayedGames(4);
		calendarBuilder.scheduleNextGameIfNecessary(
				league.getPlayoff().getNbaCalendar().getCalendar(),
				series,
				CalendarConfiguration.PLAYOFF_DEBUT_DATE.plusDays(7));

		assertEquals(1, countScheduledOccurrences(league, gameFive));
	}

	@Test
	public void shouldKeepPreviousPlayoffCalendarWhenAdvancingRound() {
		League league = buildRankedLeague();
		PlayoffBuilder playoffBuilder = new PlayoffBuilder(league);
		league.setPlayoff(playoffBuilder.buildFirstRoundPlayoffs());
		league.getPlayoff().setCurrentRound(PlayoffRound.FIRST_ROUND);
		league.getPlayoff().setNbaCalendar(new FirstRoundCalendarBuilder(league).buildCalendar());
		int firstRoundGameDays = league.getPlayoff().getNbaCalendar().getCalendar().size();
		finishAllSeries(league.getPlayoff().getEastFirstRound());
		finishAllSeries(league.getPlayoff().getWestFirstRound());

		FirstRoundPlayoffManager manager = new FirstRoundPlayoffManager(
				league,
				new FirstRoundCalendarBuilder(league),
				playoffBuilder,
				new FinanceManager(league),
				new TeamPopularityUpdater());
		manager.advanceToNextRound(CalendarConfiguration.PLAYOFF_DEBUT_DATE.plusDays(12));

		assertEquals(PlayoffRound.CONFERENCE_SEMIFINALS, league.getPlayoff().getCurrentRound());
		assertFalse(league.getPlayoff().getNbaCalendar().getCalendar().size() <= firstRoundGameDays);
		assertEquals(2, league.getPlayoff().getEastConferenceSemis().size());
		assertEquals(2, league.getPlayoff().getWestConferenceSemis().size());
	}

	@Test
	public void shouldFillBracketPositionsFromExistingPlayoffSeries() {
		SimulationManager simulationManager = new SimulationManager();
		simulationManager.startSeason();
		fillRanking(simulationManager.getLeague());
		simulationManager.endRegularSeason();
		Playoff playoff = simulationManager.getPlayoff();
		finishAllSeries(playoff.getEastFirstRound());
		finishAllSeries(playoff.getWestFirstRound());
		new PlayoffBuilder(simulationManager.getLeague()).buildSecondRoundPlayoffs();

		Map<String, String> positions = simulationManager.getPlayoffPositionMap();

		assertTrue(positions.containsKey("b1"));
		assertTrue(positions.containsKey("b2"));
		assertTrue(positions.containsKey("b3"));
		assertTrue(positions.containsKey("b4"));
		assertTrue(positions.containsKey("b5"));
		assertTrue(positions.containsKey("b6"));
		assertTrue(positions.containsKey("b7"));
		assertTrue(positions.containsKey("b8"));
	}

	@Test
	public void shouldResolveTiedPlayoffGameSoSeriesCanAdvance() {
		League league = buildRankedLeague();
		PlayoffBuilder playoffBuilder = new PlayoffBuilder(league);
		league.setPlayoff(playoffBuilder.buildFirstRoundPlayoffs());
		league.getPlayoff().setCurrentRound(PlayoffRound.FIRST_ROUND);
		league.getPlayoff().setNbaCalendar(new FirstRoundCalendarBuilder(league).buildCalendar());
		PlayoffSeries series = league.getPlayoff().getEastFirstRound().get(0);
		Game tiedGame = series.getExpectedGames()[0];
		tiedGame.setHomeFinalScore(100);
		tiedGame.setAwayFinalScore(100);

		PlayoffManager manager = new FirstRoundPlayoffManager(
				league,
				new FirstRoundCalendarBuilder(league),
				playoffBuilder,
				new FinanceManager(league),
				new TeamPopularityUpdater());
		manager.handlePlayedGame(tiedGame, CalendarConfiguration.PLAYOFF_DEBUT_DATE);

		assertEquals(1, series.getNumberPlayedGames());
		assertEquals(1, series.getHigherTeamWins() + series.getLowerTeamWins());
		assertNotNull(tiedGame.getWinner());
	}

	@Test
	public void shouldRegisterChampionWhenNbaFinalEnds() {
		League league = buildRankedLeague();
		PlayoffBuilder playoffBuilder = new PlayoffBuilder(league);
		Team eastChampion = league.getRegularSeason().getRanking().getEastRanking().get(1);
		Team westChampion = league.getRegularSeason().getRanking().getWestRanking().get(1);
		PlayoffSeries finalSeries = new PlayoffSeries(eastChampion, westChampion);
		finalSeries.setHigherTeamWins(4);
		finalSeries.setLowerTeamWins(2);
		finalSeries.setFinished(true);
		league.getPlayoff().getNbaFinals().add(finalSeries);
		league.getPlayoff().setCurrentRound(PlayoffRound.NBA_FINALS);

		NbaFinalPlayoffManager manager = new NbaFinalPlayoffManager(
				league,
				new process.builder.calendar.NbaFinalCalendarBuilder(league, LocalDate.now()),
				playoffBuilder,
				new FinanceManager(league),
				new TeamPopularityUpdater());
		manager.advanceToNextRound(LocalDate.now());

		assertSame(eastChampion, league.getPlayoff().getChampion());
		assertEquals(PlayoffRound.FINISHED, league.getPlayoff().getCurrentRound());
	}

	private League buildRankedLeague() {
		League league = new LeagueBuilder().build();
		fillRanking(league);
		return league;
	}

	private void fillRanking(League league) {
		ArrayList<Team> eastTeams = new ArrayList<Team>();
		ArrayList<Team> westTeams = new ArrayList<Team>();
		LeagueUtility.getConferenceTeams(league, eastTeams, westTeams);
		for (int i = 0; i < 15; i++) {
			league.getRegularSeason().getRanking().getEastRanking().put(i + 1, eastTeams.get(i));
			league.getRegularSeason().getRanking().getWestRanking().put(i + 1, westTeams.get(i));
		}
	}

	private void finishAllSeries(ArrayList<PlayoffSeries> seriesList) {
		for (PlayoffSeries series : seriesList) {
			series.setHigherTeamWins(4);
			series.setLowerTeamWins(0);
			series.setFinished(true);
			series.setNumberPlayedGames(4);
		}
	}

	private int countScheduledOccurrences(League league, Game game) {
		int occurrences = 0;
		for (GameDay gameDay : league.getPlayoff().getNbaCalendar().getCalendar().values()) {
			for (Game scheduledGame : gameDay.getGames()) {
				if (scheduledGame == game) {
					occurrences++;
				}
			}
		}
		return occurrences;
	}
}
