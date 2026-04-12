package process.service.live;

import data.team.Team;
import process.service.live.LiveMatchStatistics.PlayerLiveSummary;

public class LiveMatchState {

   private Team homeTeam;
   private Team awayTeam;
   private int homePoints;
   private int awayPoints;
   private int homeRebounds;
   private int awayRebounds;
   private int homeAssists;
   private int awayAssists;
   private int homeTurnovers;
   private int awayTurnovers;
   private String homeFgPercent;
   private String awayFgPercent;
   private String homeThreePercent;
   private String awayThreePercent;
   private PlayerLiveSummary[] homeBestPlayers;
   private PlayerLiveSummary[] awayBestPlayers;
   private String quarterLabel;
   private String quarterTimeText;
   private String[] displayedRows;
   private String centerMessage;

   public Team getHomeTeam() {
      return homeTeam;
   }

   public void setHomeTeam(Team homeTeam) {
      this.homeTeam = homeTeam;
   }

   public Team getAwayTeam() {
      return awayTeam;
   }

   public void setAwayTeam(Team awayTeam) {
      this.awayTeam = awayTeam;
   }

   public int getHomePoints() {
      return homePoints;
   }

   public void setHomePoints(int homePoints) {
      this.homePoints = homePoints;
   }

   public int getAwayPoints() {
      return awayPoints;
   }

   public void setAwayPoints(int awayPoints) {
      this.awayPoints = awayPoints;
   }

   public int getHomeRebounds() {
      return homeRebounds;
   }

   public void setHomeRebounds(int homeRebounds) {
      this.homeRebounds = homeRebounds;
   }

   public int getAwayRebounds() {
      return awayRebounds;
   }

   public void setAwayRebounds(int awayRebounds) {
      this.awayRebounds = awayRebounds;
   }

   public int getHomeAssists() {
      return homeAssists;
   }

   public void setHomeAssists(int homeAssists) {
      this.homeAssists = homeAssists;
   }

   public int getAwayAssists() {
      return awayAssists;
   }

   public void setAwayAssists(int awayAssists) {
      this.awayAssists = awayAssists;
   }

   public int getHomeTurnovers() {
      return homeTurnovers;
   }

   public void setHomeTurnovers(int homeTurnovers) {
      this.homeTurnovers = homeTurnovers;
   }

   public int getAwayTurnovers() {
      return awayTurnovers;
   }

   public void setAwayTurnovers(int awayTurnovers) {
      this.awayTurnovers = awayTurnovers;
   }

   public String getHomeFgPercent() {
      return homeFgPercent;
   }

   public void setHomeFgPercent(String homeFgPercent) {
      this.homeFgPercent = homeFgPercent;
   }

   public String getAwayFgPercent() {
      return awayFgPercent;
   }

   public void setAwayFgPercent(String awayFgPercent) {
      this.awayFgPercent = awayFgPercent;
   }

   public String getHomeThreePercent() {
      return homeThreePercent;
   }

   public void setHomeThreePercent(String homeThreePercent) {
      this.homeThreePercent = homeThreePercent;
   }

   public String getAwayThreePercent() {
      return awayThreePercent;
   }

   public void setAwayThreePercent(String awayThreePercent) {
      this.awayThreePercent = awayThreePercent;
   }

   public PlayerLiveSummary[] getHomeBestPlayers() {
      return homeBestPlayers;
   }

   public void setHomeBestPlayers(PlayerLiveSummary[] homeBestPlayers) {
      this.homeBestPlayers = homeBestPlayers;
   }

   public PlayerLiveSummary[] getAwayBestPlayers() {
      return awayBestPlayers;
   }

   public void setAwayBestPlayers(PlayerLiveSummary[] awayBestPlayers) {
      this.awayBestPlayers = awayBestPlayers;
   }

   public String getQuarterLabel() {
      return quarterLabel;
   }

   public void setQuarterLabel(String quarterLabel) {
      this.quarterLabel = quarterLabel;
   }

   public String getQuarterTimeText() {
      return quarterTimeText;
   }

   public void setQuarterTimeText(String quarterTimeText) {
      this.quarterTimeText = quarterTimeText;
   }

   public String[] getDisplayedRows() {
      return displayedRows;
   }

   public void setDisplayedRows(String[] displayedRows) {
      this.displayedRows = displayedRows;
   }

   public String getCenterMessage() {
      return centerMessage;
   }

   public void setCenterMessage(String centerMessage) {
      this.centerMessage = centerMessage;
   }
}
