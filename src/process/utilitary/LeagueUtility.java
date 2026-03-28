package process.utilitary;

import data.league.Conference;
import data.league.League;
import data.team.Team;
import java.util.ArrayList;
import process.repositery.TeamRepositery;

public class LeagueUtility {

   public static void getConferenceTeams(League league, ArrayList<Team> eastTeams, ArrayList<Team> westTeams) {
      Conference easternConference = league.getEasternConference();
      for (Team team : TeamRepositery.getInstance().getAllTeams()) {
         if (TeamUtilitary.getConferenceOfTeam(league, team).equals(league.getEasternConference())) {
            eastTeams.add(team);
         } else {
            westTeams.add(team);
         }
      }
   }
}
