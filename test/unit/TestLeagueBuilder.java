package unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import data.league.League;
import process.builder.LeagueBuilder;
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
    public void shouldAttachPlayersToTeamsDuringLeagueBuild() {
        LeagueBuilder leagueBuilder = new LeagueBuilder();

        League league = leagueBuilder.build();

        assertNotNull(league.getAllTeam().get(0).getCurrentPlayers());
        assertTrue(league.getAllTeam().get(0).getCurrentPlayers().size() > 0);
        assertTrue(league.getAllTeam().stream().allMatch(team -> !team.getCurrentPlayers().isEmpty()));
    }
}


