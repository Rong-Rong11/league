package process.utility;

import java.util.ArrayList;

import data.league.Conference;
import data.league.League;
import data.team.Team;
import process.repository.TeamRepository;

public class LeagueUtility {

   public static void getConferenceTeams(League league, ArrayList<Team> eastTeams, ArrayList<Team> westTeams) {
      Conference easternConference = league.getEasternConference();
      for (Team team : TeamRepository.getInstance().getAllTeams()) {
         if (TeamUtility.getConferenceOfTeam(league, team).equals(league.getEasternConference())) {
            eastTeams.add(team);
         } else {
            westTeams.add(team);
         }
      }
   }
}
