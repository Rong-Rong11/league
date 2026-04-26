package test.unit;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import data.finance.budget.income.IncomeType;
import data.league.League;
import data.league.PlayoffRound;
import data.team.Team;
import process.service.finance.FinanceManager;
import test.support.TestSupport;

public class TestFinanceManager {

	private League league;
	private FinanceManager financeManager;
	private Team team;

	@Before
	public void setUp() {
		league = TestSupport.buildLeagueWithFinance();
		financeManager = new FinanceManager(league);
		team = TestSupport.firstTeams(league, 1).get(0);
		TestSupport.resetBudget(team.getTeamFinance().getBudget());
	}

	@Test
	public void shouldApplyPlayoffQualificationBonusToOneTeam() {
		double initialAmount = team.getTeamFinance().getBudget().getRemainingAmount();

		financeManager.applyPlayoffQualificationBonus(team, 8);

		assertTrue(team.getTeamFinance().getBudget().getIncomesForMonth(8)
				.containsKey(IncomeType.PLAYOFF_QUALIFICATION_BONUS.name()));
		assertTrue(team.getTeamFinance().getBudget().getRemainingAmount() > initialAmount);
	}

	@Test
	public void shouldApplyPlayoffQualificationBonusToEveryTeamInList() {
		ArrayList<Team> teams = TestSupport.firstTeams(league, 3);
		for (Team currentTeam : teams) {
			TestSupport.resetBudget(currentTeam.getTeamFinance().getBudget());
		}

		financeManager.applyPlayoffQualificationBonus(teams, 8);

		for (Team currentTeam : teams) {
			assertTrue(currentTeam.getTeamFinance().getBudget().getIncomesForMonth(8)
					.containsKey(IncomeType.PLAYOFF_QUALIFICATION_BONUS.name()));
		}
	}

	@Test
	public void shouldApplyPlayoffRoundBonusAccordingToRoundRules() {
		financeManager.applyPlayoffRoundBonus(team, 9, PlayoffRound.CONFERENCE_FINALS);

		assertEquals(5.5,
				team.getTeamFinance().getBudget().getIncomesForMonth(9).get(IncomeType.PLAYOFF_ROUND_BONUS.name())
						.getAmount(),
				0.0001);
	}
}
