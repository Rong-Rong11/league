package data.sport.setup;

import data.team.Team;

public class PlayoffSeries {
   private Team higherTeam;
   private Team lowerTeam;

   private int higherTeamWins = 0;
   private int lowerTeamWins = 0;

   private Game[] expectedGames = new Game[7];
   private int numberPlayedGames = 0;

   private boolean finished = false;

   public PlayoffSeries(Team higherTeam, Team lowerTeam) {
      this.higherTeam = higherTeam;
      this.lowerTeam = lowerTeam;
   }

   public Team getHigherTeam() {
      return higherTeam;
   }

   public void setHigherTeam(Team higherTeam) {
      this.higherTeam = higherTeam;
   }

   public Team getLowerTeam() {
      return lowerTeam;
   }

   public void setLowerTeam(Team lowerTeam) {
      this.lowerTeam = lowerTeam;
   }

   public int getHigherTeamWins() {
      return higherTeamWins;
   }

   public void setHigherTeamWins(int higherTeamWins) {
      this.higherTeamWins = higherTeamWins;
   }

   public int getLowerTeamWins() {
      return lowerTeamWins;
   }

   public void setLowerTeamWins(int lowerTeamWins) {
      this.lowerTeamWins = lowerTeamWins;
   }

   public boolean isFinished() {
      return finished;
   }

   public void setFinished(boolean finished) {
      this.finished = finished;
   }

   public void addExpectedGame(Game game, int n) {
      expectedGames[n - 1] = game;
   }

   public int getNumberPlayedGames() {
      return numberPlayedGames;
   }

   public void setNumberPlayedGames(int numberPlayedGames) {
      this.numberPlayedGames = numberPlayedGames;
   }

   public Game[] getExpectedGames() {
      return expectedGames;
   }

   public void setExpectedGames(Game[] expectedGames) {
      this.expectedGames = expectedGames;
   }

}
