package test.usage;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import data.team.Team;
import process.orchestrator.manager.SimulationManager;
import test.support.TestSupport;

public class TestSetupUsage {

	private SimulationManager simulationManager;

	@Before
	public void setUp() {
		TestSupport.clearRepositories();
		simulationManager = new SimulationManager();
	}

	@Test
	public void shouldLetUserAssignDifferentPoliciesAndMarketSizesBeforeSeason() {
		ArrayList<Team> teams = simulationManager.getTeams();
		Team ambitiousTeam = teams.get(0);
		Team balancedTeam = teams.get(1);
		Team thriftyTeam = teams.get(2);

		simulationManager.chooseAmbitiousPolicy(ambitiousTeam);
		simulationManager.chooseBalancedPolicy(balancedTeam);
		simulationManager.chooseThriftyPolicy(thriftyTeam);

		simulationManager.chooseLargeMarketSize(ambitiousTeam);
		simulationManager.chooseMediumMarketSize(balancedTeam);
		simulationManager.chooseSmallMarketSize(thriftyTeam);

		assertEquals("Ambitieuse", simulationManager.getTeamFinancialPolicyLabel(ambitiousTeam));
		assertEquals("Equilibree", simulationManager.getTeamFinancialPolicyLabel(balancedTeam));
		assertEquals("Economique", simulationManager.getTeamFinancialPolicyLabel(thriftyTeam));

		assertEquals("Grand", simulationManager.getTeamMarketSizeLabel(ambitiousTeam));
		assertEquals("Moyen", simulationManager.getTeamMarketSizeLabel(balancedTeam));
		assertEquals("Petit", simulationManager.getTeamMarketSizeLabel(thriftyTeam));
	}

	@Test
	public void shouldLetUserRandomizeFinancialSetupBeforeSeason() {
		simulationManager.randomFinance();

		for (Team team : simulationManager.getTeams()) {
			assertNotEquals("-", simulationManager.getTeamFinancialPolicyLabel(team));
			assertNotEquals("-", simulationManager.getTeamMarketSizeLabel(team));
		}
	}
}
