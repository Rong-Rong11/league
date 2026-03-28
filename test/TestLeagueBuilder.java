// package test;

// import static org.junit.Assert.assertNotNull;
// import static org.junit.Assert.assertTrue;

// import java.util.HashMap;

// import org.junit.Before;
// import org.junit.Test;

// import data.league.Conference;
// import data.league.Division;
// import data.league.League;
// import data.team.Team;
// import process.builder.LeagueBuilder;
// import process.repositery.CurrentSeasonAssetRepositery;
// import process.repositery.DivisionRepositery;
// import process.repositery.PlayerRepositery;
// import process.repositery.PreSeasonAssetRepositery;
// import process.repositery.TeamRepositery;

// public class TestLeagueBuilder {
	
// 	@Before
// 	public void setUp() {
// 	    PlayerRepositery.getInstance().clear();
// 	    TeamRepositery.getInstance().clear();
// 	    DivisionRepositery.getInstance().clear();
// 	}
//     @Test
//     public void test() {
//         LeagueBuilder leagueBuilder = new LeagueBuilder();
//         League league = leagueBuilder.build();

//         assertNotNull(league);
//         assertNotNull(league.getWesternConference());
//         assertNotNull(league.getEasternConference());
//         assertTrue(league.getWesternConference().getDivisions().size() > 0);
//     }
    
//     @Test
//     public void testWestConf() {
//     	LeagueBuilder leagueBuilder = new LeagueBuilder();
//         League league = leagueBuilder.build();
//     	Conference easternConference = league.getWesternConference() ; 
//     	HashMap<String, Division> divisions = easternConference.getDivisions(); 
//     	assertNotNull(divisions.get("Southwest"));
//     	assertNotNull(divisions.get("Northwest"));
//     	assertNotNull(divisions.get("Pacific"));
    	
//     }
    
//     @Test
//     public void testLeagueFinance() {
//         LeagueBuilder builder = new LeagueBuilder();
//         League league = builder.build();

//         assertNotNull(league.getLeagueFinance());
//         assertTrue(league.getLeagueFinance().getBudget().getInitialAmount() > 0);
//         assertTrue(league.getLeagueFinance().getSalaryCap() > 0);
//         assertTrue(league.getLeagueFinance().getLuxuryTaxLine() > 0);
//         assertTrue(league.getLeagueFinance().getMinimumTeamSalary() > 0);
//     }
// }
