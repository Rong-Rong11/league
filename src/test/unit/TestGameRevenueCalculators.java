package test.unit;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import config.CalendarConfiguration;
import config.GameConfiguration;
import data.finance.GameStat;
import data.league.League;
import data.league.PlayoffRound;
import data.sport.setup.Game;
import data.sport.setup.GameContext;
import data.team.Team;
import process.service.finance.game.revenue.PlayoffGameRevenueCalculator;
import process.service.finance.game.revenue.RegularSeasonGameRevenueCalculator;
import test.support.TestSupport;

public class TestGameRevenueCalculators {

	private League league;
	private Team homeTeam;
	private Team awayTeam;

	@Before
	public void setUp() {
		league = TestSupport.buildLeagueWithFinance();
		ArrayList<Team> teams = TestSupport.firstTeams(league, 2);
		homeTeam = teams.get(0);
		awayTeam = teams.get(1);
	}

	@Test
	public void shouldComputePositiveRegularSeasonRevenueValues() {
		Game game = TestSupport.createInterConferenceGame(homeTeam, awayTeam);
		GameStat gameStat = new GameStat(game);

		new RegularSeasonGameRevenueCalculator(league, gameStat).calculateGameRevenue(game, LocalDate.of(2025, 10, 22));

		assertTrue(gameStat.getPopularity() > 0);
		assertTrue(gameStat.getAttendanceRate() > 0);
		assertTrue(gameStat.getAttendees() > 0);
		assertTrue(gameStat.getTicketPrice() > 0);
		assertTrue(gameStat.getHomeFinance().getTicketRevenue() > 0);
		assertTrue(gameStat.getHomeFinance().getConcessionsRevenue() > 0);
		assertTrue(gameStat.getHomeFinance().getParkingRevenue() > 0);
		assertTrue(gameStat.getHomeFinance().getTvRevenue() > 0);
		assertTrue(gameStat.getAwayFinance().getTvRevenue() > 0);
		assertTrue(gameStat.getHomeFinance().getMerchRevenue() > 0);
	}

	@Test
	public void shouldIncreaseRegularSeasonTvRevenueForRivalryGames() {
		homeTeam.setRival("shared");
		awayTeam.setRival("neutral");
		Game normalGame = new Game(new GameContext(homeTeam, awayTeam, GameConfiguration.GAME_INTRA_DIVISION));
		GameStat normalStat = new GameStat(normalGame);
		new RegularSeasonGameRevenueCalculator(league, normalStat).calculateGameRevenue(normalGame,
				LocalDate.of(2025, 10, 22));

		awayTeam.setRival("shared");
		Game rivalryGame = new Game(new GameContext(homeTeam, awayTeam, GameConfiguration.GAME_INTRA_DIVISION));
		GameStat rivalryStat = new GameStat(rivalryGame);
		new RegularSeasonGameRevenueCalculator(league, rivalryStat).calculateGameRevenue(rivalryGame,
				LocalDate.of(2025, 10, 22));

		assertTrue(rivalryStat.getHomeFinance().getTvRevenue() > normalStat.getHomeFinance().getTvRevenue());
		assertTrue(rivalryStat.getAwayFinance().getTvRevenue() > normalStat.getAwayFinance().getTvRevenue());
	}

	@Test
	public void shouldIncreaseRegularSeasonRevenueWithHigherPopularityContext() {
		homeTeam.setCurrentPopularity(10.0);
		awayTeam.setCurrentPopularity(10.0);
		Game lowPopularityGame = TestSupport.createInterConferenceGame(homeTeam, awayTeam);
		GameStat lowPopularityStat = new GameStat(lowPopularityGame);
		new RegularSeasonGameRevenueCalculator(league, lowPopularityStat).calculateGameRevenue(lowPopularityGame,
				LocalDate.of(2025, 10, 21));

		homeTeam.setCurrentPopularity(95.0);
		awayTeam.setCurrentPopularity(95.0);
		Game highPopularityGame = TestSupport.createInterConferenceGame(homeTeam, awayTeam);
		GameStat highPopularityStat = new GameStat(highPopularityGame);
		new RegularSeasonGameRevenueCalculator(league, highPopularityStat).calculateGameRevenue(highPopularityGame,
				CalendarConfiguration.CHRISTMAS_DAY);

		assertTrue(highPopularityStat.getPopularity() >= lowPopularityStat.getPopularity());
		assertTrue(highPopularityStat.getTicketPrice() >= lowPopularityStat.getTicketPrice());
		assertTrue(highPopularityStat.getAttendees() >= lowPopularityStat.getAttendees());
	}

	@Test
	public void shouldIncreasePlayoffRevenueInLaterRounds() {
		Game game = TestSupport.createInterConferenceGame(homeTeam, awayTeam);

		GameStat firstRoundStat = new GameStat(game);
		new PlayoffGameRevenueCalculator(league, firstRoundStat, PlayoffRound.FIRST_ROUND)
				.calculateGameRevenue(game, LocalDate.of(2026, 4, 20));

		GameStat finalsStat = new GameStat(game);
		new PlayoffGameRevenueCalculator(league, finalsStat, PlayoffRound.NBA_FINALS)
				.calculateGameRevenue(game, LocalDate.of(2026, 6, 10));

		assertTrue(finalsStat.getPopularity() >= firstRoundStat.getPopularity());
		assertTrue(finalsStat.getAttendanceRate() >= firstRoundStat.getAttendanceRate());
		assertTrue(finalsStat.getTicketPrice() >= firstRoundStat.getTicketPrice());
		assertTrue(finalsStat.getHomeFinance().getTvRevenue() > firstRoundStat.getHomeFinance().getTvRevenue());
		assertTrue(finalsStat.getAwayFinance().getTvRevenue() > firstRoundStat.getAwayFinance().getTvRevenue());
		assertTrue(finalsStat.getHomeFinance().getMerchRevenue() > firstRoundStat.getHomeFinance().getMerchRevenue());
	}
}
