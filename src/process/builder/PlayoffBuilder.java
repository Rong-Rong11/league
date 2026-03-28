package process.builder;

import java.util.ArrayList;
import java.util.TreeMap;

import data.league.League;
import data.league.Playoff;
import data.league.Ranking;
import data.sport.setup.PlayoffSeries;
import data.team.Team;

public class PlayoffBuilder {
   private League league;

   public PlayoffBuilder(League league) {
      this.league = league;
   }

   public Playoff buldFirstRoundPlayoffs() {
      Ranking ranking = league.getReagularSeason().getRanking();
      Playoff playoff = league.getPlayoff();

      TreeMap<Integer, Team> eastRanking = ranking.getEastRanking();
      TreeMap<Integer, Team> westRanking = ranking.getWestRanking();

      addEastQualifiedTeam(eastRanking, playoff);
      addWestQualifiedTeam(westRanking, playoff);

      // faire le play-in plus tard
      Team east1 = eastRanking.get(1);
      Team east2 = eastRanking.get(2);
      Team east3 = eastRanking.get(3);
      Team east4 = eastRanking.get(4);
      Team east5 = eastRanking.get(5);
      Team east6 = eastRanking.get(6);
      Team east7 = eastRanking.get(7);
      Team east8 = eastRanking.get(8);

      Team west1 = westRanking.get(1);
      Team west2 = westRanking.get(2);
      Team west3 = westRanking.get(3);
      Team west4 = westRanking.get(4);
      Team west5 = westRanking.get(5);
      Team west6 = westRanking.get(6);
      Team west7 = westRanking.get(7);
      Team west8 = westRanking.get(8);

      ArrayList<PlayoffSeries> firstWestRound = new ArrayList<>();
      firstWestRound.add(new PlayoffSeries(west1, west8));
      firstWestRound.add(new PlayoffSeries(west2, west7));
      firstWestRound.add(new PlayoffSeries(west3, west6));
      firstWestRound.add(new PlayoffSeries(west4, west5));

      ArrayList<PlayoffSeries> firstEastRound = new ArrayList<>();
      firstEastRound.add(new PlayoffSeries(east1, east8));
      firstEastRound.add(new PlayoffSeries(east2, east7));
      firstEastRound.add(new PlayoffSeries(east3, east6));
      firstEastRound.add(new PlayoffSeries(east4, east5));

      playoff.setEastFirstRound(firstEastRound);
      playoff.setWestFirstRound(firstWestRound);
      return playoff;
   }

   private void addEastQualifiedTeam(TreeMap<Integer, Team> eastRanking, Playoff playoff) {
      for (int i = 1; i <= 8; i++) {
         playoff.addQualifiedEastTeam(eastRanking.get(i));
      }
   }

   private void addWestQualifiedTeam(TreeMap<Integer, Team> westRanking, Playoff playoff) {
      for (int i = 1; i <= 8; i++) {
         playoff.addQualifiedWestTeam(westRanking.get(i));
      }
   }
}
