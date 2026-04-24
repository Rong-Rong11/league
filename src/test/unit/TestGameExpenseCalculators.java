package test.unit;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import config.GameConfiguration;
import data.finance.GameStat;
import data.league.League;
import data.league.PlayoffRound;
import data.sport.setup.Game;
import data.sport.setup.GameContext;
import data.team.Team;
import process.service.finance.game.expense.PlayoffGameExpenseCalculator;
import process.service.finance.game.expense.RegularSeasonGameExpenseCalculator;
import test.support.TestSupport;

public class TestGameExpenseCalculators {

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
	public void shouldIncreaseRegularSeasonCostsForRivalryGames() {
		homeTeam.setRival("shared-rival");
		awayTeam.setRival("neutral");
		Game normalGame = new Game(new GameContext(homeTeam, awayTeam, GameConfiguration.GAME_INTRA_DIVISION));
		GameStat normalStat = TestSupport.createGameStat(normalGame, 18500, 0.92, 0.78);
		new RegularSeasonGameExpenseCalculator(normalStat).calculateGameExpenses(normalGame);

		awayTeam.setRival("shared-rival");
		Game rivalryGame = new Game(
				new GameContext(homeTeam, awayTeam, GameConfiguration.GAME_INTRA_DIVISION));
		GameStat rivalryStat = TestSupport.createGameStat(rivalryGame, 18500, 0.92, 0.78);
		new RegularSeasonGameExpenseCalculator(rivalryStat).calculateGameExpenses(rivalryGame);

		assertTrue(rivalryStat.getHomeFinance().getArenaCosts() > normalStat.getHomeFinance().getArenaCosts());
		assertTrue(rivalryStat.getHomeFinance().getSecurityCosts() > normalStat.getHomeFinance().getSecurityCosts());
		assertTrue(rivalryStat.getHomeFinance().getStaffCosts() > normalStat.getHomeFinance().getStaffCosts());
		assertTrue(rivalryStat.getHomeFinance().getLogisticsCosts() > normalStat.getHomeFinance().getLogisticsCosts());
	}

	@Test
	public void shouldIncreaseTravelCostAcrossRegularSeasonGameTypes() {
		homeTeam.setRival("none");
		awayTeam.setRival("other");

		Game intraDivisionGame = TestSupport.createGame(homeTeam, awayTeam, GameConfiguration.GAME_INTRA_DIVISION);
		GameStat intraDivisionStat = TestSupport.createGameStat(intraDivisionGame, 16000, 0.80, 0.65);
		new RegularSeasonGameExpenseCalculator(intraDivisionStat).calculateGameExpenses(intraDivisionGame);

		Game intraConferenceGame = TestSupport.createGame(homeTeam, awayTeam, GameConfiguration.GAME_INTRA_CONFERENCE);
		GameStat intraConferenceStat = TestSupport.createGameStat(intraConferenceGame, 16000, 0.80, 0.65);
		new RegularSeasonGameExpenseCalculator(intraConferenceStat).calculateGameExpenses(intraConferenceGame);

		Game interConferenceGame = TestSupport.createGame(homeTeam, awayTeam, GameConfiguration.GAME_INTER_CONFERENCE);
		GameStat interConferenceStat = TestSupport.createGameStat(interConferenceGame, 16000, 0.80, 0.65);
		new RegularSeasonGameExpenseCalculator(interConferenceStat).calculateGameExpenses(interConferenceGame);

		assertTrue(intraDivisionStat.getAwayFinance().getTravelCosts() < intraConferenceStat.getAwayFinance()
				.getTravelCosts());
		assertTrue(intraConferenceStat.getAwayFinance().getTravelCosts() < interConferenceStat.getAwayFinance()
				.getTravelCosts());
	}

	@Test
	public void shouldApplyHigherPlayoffCostsInLaterRounds() {
		Game game = TestSupport.createInterConferenceGame(homeTeam, awayTeam);

		GameStat firstRoundStat = TestSupport.createGameStat(game, 19000, 0.95, 0.82);
		new PlayoffGameExpenseCalculator(firstRoundStat, PlayoffRound.FIRST_ROUND).calculateGameExpenses(game);

		GameStat finalsStat = TestSupport.createGameStat(game, 19000, 0.95, 0.82);
		new PlayoffGameExpenseCalculator(finalsStat, PlayoffRound.NBA_FINALS).calculateGameExpenses(game);

		assertTrue(finalsStat.getHomeFinance().getArenaCosts() > firstRoundStat.getHomeFinance().getArenaCosts());
		assertTrue(finalsStat.getHomeFinance().getStaffCosts() > firstRoundStat.getHomeFinance().getStaffCosts());
		assertTrue(finalsStat.getHomeFinance().getSecurityCosts() > firstRoundStat.getHomeFinance().getSecurityCosts());
		assertTrue(finalsStat.getHomeFinance().getLogisticsCosts() > firstRoundStat.getHomeFinance().getLogisticsCosts());
		assertTrue(finalsStat.getAwayFinance().getTravelCosts() > firstRoundStat.getAwayFinance().getTravelCosts());
	}

	@Test
	public void shouldKeepAllComputedCostsPositive() {
		Game game = TestSupport.createInterConferenceGame(homeTeam, awayTeam);
		GameStat gameStat = TestSupport.createGameStat(game, 15000, 0.75, 0.60);

		new RegularSeasonGameExpenseCalculator(gameStat).calculateGameExpenses(game);

		assertTrue(gameStat.getHomeFinance().getArenaCosts() > 0);
		assertTrue(gameStat.getHomeFinance().getStaffCosts() > 0);
		assertTrue(gameStat.getHomeFinance().getSecurityCosts() > 0);
		assertTrue(gameStat.getHomeFinance().getLogisticsCosts() > 0);
		assertTrue(gameStat.getAwayFinance().getTravelCosts() > 0);
	}

	@Test
	public void shouldApplyNoRegularSeasonTravelBonusOutsideBaseCalculation() {
		Game game = TestSupport.createGame(homeTeam, awayTeam, GameConfiguration.GAME_INTER_CONFERENCE);
		GameStat gameStat = TestSupport.createGameStat(game, 17000, 0.85, 0.70);

		new RegularSeasonGameExpenseCalculator(gameStat).calculateGameExpenses(game);

		double loyaltyFactor = 1 + awayTeam.getTeamFinance().getEconomicProfil().getFanLoyalty() * 0.32;
		double expected = config.FinanceConfiguration.BASE_TRAVEL_INTER_CONFERENCE_COST * loyaltyFactor;
		assertEquals(expected, gameStat.getAwayFinance().getTravelCosts(), 0.0001);
	}
}
