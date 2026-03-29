package process.service;

import data.player.Player;
import data.sport.play.action.ActionResult;
import data.sport.setup.Game;
import java.util.ArrayList;
import java.util.HashMap;
import process.visitor.actionresult.StatsVisitor;

public class LiveMatchStatistics {

   private Game game;
   private int homePoints;
   private int awayPoints;
   private int homeRebounds;
   private int awayRebounds;
   private int homeAssists;
   private int awayAssists;
   private int homeTurnovers;
   private int awayTurnovers;
   private int homeTwoMade;
   private int awayTwoMade;
   private int homeThreeMade;
   private int awayThreeMade;
   private int homeFgAttempts;
   private int awayFgAttempts;
   private int homeThreeAttempts;
   private int awayThreeAttempts;

   private HashMap<String, Integer> homePlayerPoints;
   private HashMap<String, Integer> awayPlayerPoints;
   private HashMap<String, Player> homePlayers;
   private HashMap<String, Player> awayPlayers;

   public LiveMatchStatistics() {
      homePlayerPoints = new HashMap<String, Integer>();
      awayPlayerPoints = new HashMap<String, Integer>();
      homePlayers = new HashMap<String, Player>();
      awayPlayers = new HashMap<String, Player>();
      reset();
   }

   public void reset() {
      homePoints = 0;
      awayPoints = 0;
      homeRebounds = 0;
      awayRebounds = 0;
      homeAssists = 0;
      awayAssists = 0;
      homeTurnovers = 0;
      awayTurnovers = 0;
      homeTwoMade = 0;
      awayTwoMade = 0;
      homeThreeMade = 0;
      awayThreeMade = 0;
      homeFgAttempts = 0;
      awayFgAttempts = 0;
      homeThreeAttempts = 0;
      awayThreeAttempts = 0;
      homePlayerPoints.clear();
      awayPlayerPoints.clear();
      homePlayers.clear();
      awayPlayers.clear();
   }

   public void applyAction(ActionResult action) {
      action.accept(new StatsVisitor(this));
   }

   public SavedLiveState toSavedState(int liveActionIndex) {
      SavedLiveState state = new SavedLiveState();
      state.liveActionIndex = liveActionIndex;
      state.homePoints = homePoints;
      state.awayPoints = awayPoints;
      state.homeRebounds = homeRebounds;
      state.awayRebounds = awayRebounds;
      state.homeAssists = homeAssists;
      state.awayAssists = awayAssists;
      state.homeTurnovers = homeTurnovers;
      state.awayTurnovers = awayTurnovers;
      state.homeTwoMade = homeTwoMade;
      state.awayTwoMade = awayTwoMade;
      state.homeThreeMade = homeThreeMade;
      state.awayThreeMade = awayThreeMade;
      state.homeFgAttempts = homeFgAttempts;
      state.awayFgAttempts = awayFgAttempts;
      state.homeThreeAttempts = homeThreeAttempts;
      state.awayThreeAttempts = awayThreeAttempts;
      state.homePlayerPoints = new HashMap<String, Integer>(homePlayerPoints);
      state.awayPlayerPoints = new HashMap<String, Integer>(awayPlayerPoints);
      state.homePlayers = new HashMap<String, Player>(homePlayers);
      state.awayPlayers = new HashMap<String, Player>(awayPlayers);
      return state;
   }

   public void loadFromState(SavedLiveState state) {
      homePoints = state.homePoints;
      awayPoints = state.awayPoints;
      homeRebounds = state.homeRebounds;
      awayRebounds = state.awayRebounds;
      homeAssists = state.homeAssists;
      awayAssists = state.awayAssists;
      homeTurnovers = state.homeTurnovers;
      awayTurnovers = state.awayTurnovers;
      homeTwoMade = state.homeTwoMade;
      awayTwoMade = state.awayTwoMade;
      homeThreeMade = state.homeThreeMade;
      awayThreeMade = state.awayThreeMade;
      homeFgAttempts = state.homeFgAttempts;
      awayFgAttempts = state.awayFgAttempts;
      homeThreeAttempts = state.homeThreeAttempts;
      awayThreeAttempts = state.awayThreeAttempts;
      homePlayerPoints.clear();
      awayPlayerPoints.clear();
      homePlayers.clear();
      awayPlayers.clear();
      homePlayerPoints.putAll(state.homePlayerPoints);
      awayPlayerPoints.putAll(state.awayPlayerPoints);
      homePlayers.putAll(state.homePlayers);
      awayPlayers.putAll(state.awayPlayers);
   }

   public int getHomePoints() {
      return homePoints;
   }

   public int getAwayPoints() {
      return awayPoints;
   }

   public int getHomeRebounds() {
      return homeRebounds;
   }

   public int getAwayRebounds() {
      return awayRebounds;
   }

   public int getHomeAssists() {
      return homeAssists;
   }

   public int getAwayAssists() {
      return awayAssists;
   }

   public int getHomeTurnovers() {
      return homeTurnovers;
   }

   public int getAwayTurnovers() {
      return awayTurnovers;
   }

   public String getHomeFgPercent() {
      return formatPercent(homeTwoMade + homeThreeMade, homeFgAttempts);
   }

   public String getAwayFgPercent() {
      return formatPercent(awayTwoMade + awayThreeMade, awayFgAttempts);
   }

   public String getHomeThreePercent() {
      return formatPercent(homeThreeMade, homeThreeAttempts);
   }

   public String getAwayThreePercent() {
      return formatPercent(awayThreeMade, awayThreeAttempts);
   }

   public String getHomeBestPlayersText() {
      return buildTopPlayersText(homePlayerPoints);
   }

   public String getAwayBestPlayersText() {
      return buildTopPlayersText(awayPlayerPoints);
   }

   public PlayerLiveSummary[] getHomeBestPlayers() {
      return buildTopPlayers(homePlayerPoints, homePlayers);
   }

   public PlayerLiveSummary[] getAwayBestPlayers() {
      return buildTopPlayers(awayPlayerPoints, awayPlayers);
   }

   private String formatPercent(int made, int attempts) {
      if (attempts <= 0) {
         return "0%";
      }
      return (int) Math.round((made * 100.0) / attempts) + "%";
   }

   private void addTopPlayers(ArrayList<PlayerLiveSummary> summaries, HashMap<String, Integer> playerPoints,
         HashMap<String, Player> players) {
      for (String playerName : playerPoints.keySet()) {
         Player player = players.get(playerName);
         if (player == null) {
            continue;
         }
         insertSummary(summaries, new PlayerLiveSummary(player, playerPoints.get(playerName).intValue()));
      }
   }

   private PlayerLiveSummary[] buildTopPlayers(HashMap<String, Integer> playerPoints, HashMap<String, Player> players) {
      ArrayList<PlayerLiveSummary> summaries = new ArrayList<PlayerLiveSummary>();
      addTopPlayers(summaries, playerPoints, players);

      PlayerLiveSummary[] topPlayers = new PlayerLiveSummary[2];
      for (int i = 0; i < topPlayers.length && i < summaries.size(); i++) {
         topPlayers[i] = summaries.get(i);
      }
      return topPlayers;
   }

   private void insertSummary(ArrayList<PlayerLiveSummary> summaries, PlayerLiveSummary summary) {
      int index = 0;
      while (index < summaries.size() && summaries.get(index).getPoints() >= summary.getPoints()) {
         index++;
      }
      summaries.add(index, summary);
      while (summaries.size() > 2) {
         summaries.remove(summaries.size() - 1);
      }
   }

   private String buildTopPlayersText(HashMap<String, Integer> players) {
      String bestName = "-";
      int bestPoints = 0;
      String secondName = "-";
      int secondPoints = 0;

      for (String name : players.keySet()) {
         int points = players.get(name).intValue();
         if (points > bestPoints) {
            secondName = bestName;
            secondPoints = bestPoints;
            bestName = name;
            bestPoints = points;
         } else if (points > secondPoints) {
            secondName = name;
            secondPoints = points;
         }
      }

      if ("-".equals(bestName)) {
         return "-";
      }
      if ("-".equals(secondName)) {
         return "<html>" + bestName + " (" + bestPoints + " pts)</html>";
      }
      return "<html>" + bestName + " (" + bestPoints + " pts)<br>" + secondName + " (" + secondPoints
            + " pts)</html>";
   }

   public static class LiveAction {
      private int quarter;
      private ActionResult action;
      private int remainingTimeSeconds;

      public LiveAction(int quarter, ActionResult action, int remainingTimeSeconds) {
         this.quarter = quarter;
         this.action = action;
         this.remainingTimeSeconds = remainingTimeSeconds;
      }

      public int getQuarter() {
         return quarter;
      }

      public ActionResult getAction() {
         return action;
      }

      public int getRemainingTimeSeconds() {
         return remainingTimeSeconds;
      }
   }

   public static class SavedLiveState {
      int liveActionIndex;
      int homePoints;
      int awayPoints;
      int homeRebounds;
      int awayRebounds;
      int homeAssists;
      int awayAssists;
      int homeTurnovers;
      int awayTurnovers;
      int homeTwoMade;
      int awayTwoMade;
      int homeThreeMade;
      int awayThreeMade;
      int homeFgAttempts;
      int awayFgAttempts;
      int homeThreeAttempts;
      int awayThreeAttempts;
      HashMap<String, Integer> homePlayerPoints;
      HashMap<String, Integer> awayPlayerPoints;
      HashMap<String, Player> homePlayers;
      HashMap<String, Player> awayPlayers;

      public int getLiveActionIndex() {
         return liveActionIndex;
      }

      public void setLiveActionIndex(int liveActionIndex) {
         this.liveActionIndex = liveActionIndex;
      }
   }

   public static class PlayerLiveSummary {
      private Player player;
      private int points;

      public PlayerLiveSummary(Player player, int points) {
         this.player = player;
         this.points = points;
      }

      public Player getPlayer() {
         return player;
      }

      public int getPoints() {
         return points;
      }
   }

   public int getHomeTwoMade() {
      return homeTwoMade;
   }

   public void setHomeTwoMade(int homeTwoMade) {
      this.homeTwoMade = homeTwoMade;
   }

   public int getAwayTwoMade() {
      return awayTwoMade;
   }

   public void setAwayTwoMade(int awayTwoMade) {
      this.awayTwoMade = awayTwoMade;
   }

   public int getHomeThreeMade() {
      return homeThreeMade;
   }

   public void setHomeThreeMade(int homeThreeMade) {
      this.homeThreeMade = homeThreeMade;
   }

   public int getAwayThreeMade() {
      return awayThreeMade;
   }

   public void setAwayThreeMade(int awayThreeMade) {
      this.awayThreeMade = awayThreeMade;
   }

   public int getHomeFgAttempts() {
      return homeFgAttempts;
   }

   public void setHomeFgAttempts(int homeFgAttempts) {
      this.homeFgAttempts = homeFgAttempts;
   }

   public int getAwayFgAttempts() {
      return awayFgAttempts;
   }

   public void setAwayFgAttempts(int awayFgAttempts) {
      this.awayFgAttempts = awayFgAttempts;
   }

   public int getHomeThreeAttempts() {
      return homeThreeAttempts;
   }

   public void setHomeThreeAttempts(int homeThreeAttempts) {
      this.homeThreeAttempts = homeThreeAttempts;
   }

   public int getAwayThreeAttempts() {
      return awayThreeAttempts;
   }

   public void setAwayThreeAttempts(int awayThreeAttempts) {
      this.awayThreeAttempts = awayThreeAttempts;
   }

   public HashMap<String, Integer> getHomePlayerPoints() {
      return homePlayerPoints;
   }

   public void setHomePlayerPoints(HashMap<String, Integer> homePlayerPoints) {
      this.homePlayerPoints = homePlayerPoints;
   }

   public HashMap<String, Integer> getAwayPlayerPoints() {
      return awayPlayerPoints;
   }

   public void setAwayPlayerPoints(HashMap<String, Integer> awayPlayerPoints) {
      this.awayPlayerPoints = awayPlayerPoints;
   }

   public HashMap<String, Player> getHomePlayers() {
      return homePlayers;
   }

   public void setHomePlayers(HashMap<String, Player> homePlayers) {
      this.homePlayers = homePlayers;
   }

   public HashMap<String, Player> getAwayPlayers() {
      return awayPlayers;
   }

   public void setAwayPlayers(HashMap<String, Player> awayPlayers) {
      this.awayPlayers = awayPlayers;
   }

   public void setHomePoints(int homePoints) {
      this.homePoints = homePoints;
   }

   public void setAwayPoints(int awayPoints) {
      this.awayPoints = awayPoints;
   }

   public void setHomeRebounds(int homeRebounds) {
      this.homeRebounds = homeRebounds;
   }

   public void setAwayRebounds(int awayRebounds) {
      this.awayRebounds = awayRebounds;
   }

   public void setHomeAssists(int homeAssists) {
      this.homeAssists = homeAssists;
   }

   public void setAwayAssists(int awayAssists) {
      this.awayAssists = awayAssists;
   }

   public void setHomeTurnovers(int homeTurnovers) {
      this.homeTurnovers = homeTurnovers;
   }

   public void setAwayTurnovers(int awayTurnovers) {
      this.awayTurnovers = awayTurnovers;
   }

   public Game getGame() {
      return game;
   }

   public void setGame(Game game) {
      this.game = game;
   }
}
