package unit;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import data.finance.budget.income.Income;
import data.finance.budget.income.IncomeType;
import data.league.League;
import data.league.finance.LeagueFinancialRules;
import data.player.Player;
import data.team.Team;
import process.builder.league.LeagueBuilder;
import process.repositery.DivisionRepositery;
import process.repositery.PlayerRepositery;
import process.repositery.TeamRepositery;

public class TestLeagueBuilder {

	@Before
	public void setUp() {
		PlayerRepositery.getInstance().clear();
		TeamRepositery.getInstance().clear();
		DivisionRepositery.getInstance().clear();
	}

	@Test
	public void shouldBuildLeagueAsFirstStepOfSimulation() {
		LeagueBuilder leagueBuilder = new LeagueBuilder();

		League league = leagueBuilder.build();

		assertNotNull(league);
		assertNotNull(league.getEasternConference());
		assertNotNull(league.getWesternConference());
		assertNotNull(league.getLeagueFinance());
		assertEquals(30, league.getAllTeam().size());
		assertEquals(6, DivisionRepositery.getInstance().getAllDivisions().size());
		assertTrue(PlayerRepositery.getInstance().getAllPlayers().size() > 500);
		assertEquals(30, TeamRepositery.getInstance().getAllTeams().size());
	}

	@Test
	public void shouldCreateExpectedConferenceAndDivisionStructure() {
		LeagueBuilder leagueBuilder = new LeagueBuilder();

		League league = leagueBuilder.build();

		assertEquals(3, league.getWesternConference().getDivisions().size());
		assertEquals(3, league.getEasternConference().getDivisions().size());
		assertNotNull(league.getWesternConference().getDivisions().get("Pacific"));
		assertNotNull(league.getWesternConference().getDivisions().get("Northwest"));
		assertNotNull(league.getWesternConference().getDivisions().get("Southwest"));
		assertNotNull(league.getEasternConference().getDivisions().get("Atlantic"));
		assertNotNull(league.getEasternConference().getDivisions().get("Central"));
		assertNotNull(league.getEasternConference().getDivisions().get("Southeast"));
	}

	@Test
	public void shouldContainFiveTeamsInEachDivision() {
		League league = new LeagueBuilder().build();

		assertEquals(5, league.getWesternConference().getDivisions().get("Pacific").getTeams().size());
		assertEquals(5, league.getWesternConference().getDivisions().get("Northwest").getTeams().size());
		assertEquals(5, league.getWesternConference().getDivisions().get("Southwest").getTeams().size());
		assertEquals(5, league.getEasternConference().getDivisions().get("Atlantic").getTeams().size());
		assertEquals(5, league.getEasternConference().getDivisions().get("Central").getTeams().size());
		assertEquals(5, league.getEasternConference().getDivisions().get("Southeast").getTeams().size());
	}

	@Test
	public void shouldAssignUniqueTeamsToEachDivision() {
		League league = new LeagueBuilder().build();

		for (Team team : league.getAllTeam()) {
			int teamCount = 0;
			for (Team otherTeam : league.getAllTeam()) {
				if (team.getName().equals(otherTeam.getName())) {
					teamCount++;
				}
			}
			assertEquals(1, teamCount);
		}
	}

	@Test
	public void shouldAttachPlayersToTeamsDuringLeagueBuild() {
		LeagueBuilder leagueBuilder = new LeagueBuilder();

		League league = leagueBuilder.build();
		int totalTeamStrarPlayers = 0;
		for (Team team : league.getAllTeam()) {
			assertNotNull(team.getCurrentPlayers());
			assertTrue(team.getFormerPlayers().size() > 10);
			assertTrue(team.getCurrentPlayers().size() > 10);
			for (Player player : team.getCurrentPlayers().values()) {
				assertTrue(team.getFormerPlayers().containsValue(player));
			}
			if (team.hasStarPlayer()) {
				totalTeamStrarPlayers++;
			}
		}
		assertTrue(totalTeamStrarPlayers > 5);
	}

	@Test
	public void shouldBuildLeagueWithFinance() {
		League league = new LeagueBuilder().build();

		assertNotNull(league.getLeagueFinance());
		assertTrue(league.getLeagueFinance().getBudget().getInitialAmount() == 9278.82);
		assertTrue(league.getLeagueFinance().getBudget().getRemainingAmount() == 9278.82);
		HashMap<String, Income> initialIncomes = league.getLeagueFinance().getBudget().getIncomesForMonth(0);
		assertNotNull(initialIncomes);
		assertTrue(initialIncomes.size() > 0);
		assertTrue(initialIncomes.containsKey(IncomeType.NATIONAL_TV.name()));
		assertTrue(initialIncomes.containsKey(IncomeType.NATIONAL_SPONSORING.name()));
		assertTrue(initialIncomes.containsKey(IncomeType.NATIONAL_MERCHANDISING.name()));
		assertTrue(initialIncomes.containsKey(IncomeType.OTHER.name()));
	}

	@Test
	public void shouldBuildLeagueFinancialRules() {
		League league = new LeagueBuilder().build();
		LeagueFinancialRules financialRules = league.getLeagueFinance().getLeagueFinancialRules();

		assertNotNull(financialRules);
		assertTrue(financialRules.getSalaryCap() == 154.647);
		assertTrue(187 < financialRules.getLuxuryTaxLine()
				&& financialRules.getLuxuryTaxLine() < 188);
		assertTrue(131 < financialRules.getMinimumTeamSalary()
				&& financialRules.getMinimumTeamSalary() < 132);

	}

}
