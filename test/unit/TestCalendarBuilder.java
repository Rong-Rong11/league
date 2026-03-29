package unit;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import config.CalendarConfiguration;
import data.calendar.GameDay;
import data.calendar.NBACalendar;
import data.league.League;
import data.sport.setup.Game;
import data.team.Team;
import process.builder.CalendarBuilder;
import process.builder.LeagueBuilder;
import process.repositery.DivisionRepositery;
import process.repositery.PlayerRepositery;
import process.repositery.TeamRepositery;

public class TestCalendarBuilder {

   @Before
   public void setUp() {
      PlayerRepositery.getInstance().clear();
      TeamRepositery.getInstance().clear();
      DivisionRepositery.getInstance().clear();
   }

   @Test
   public void shouldBuildRegularSeasonCalendarAfterLeagueCreation() {
      League league = new LeagueBuilder().build();
      CalendarBuilder calendarBuilder = new CalendarBuilder(league);

      NBACalendar nbaCalendar = calendarBuilder.buildRegulaSeasonCalendar();
      assertNotNull(nbaCalendar);
      assertNotNull(nbaCalendar.getCalendar());
      assertFalse(nbaCalendar.getCalendar().isEmpty());
      assertTrue(nbaCalendar.getCalendar().values().size() > 0);
   }

   @Test
   public void shouldScheduleGamesForEachGameDay() {
      League league = new LeagueBuilder().build();
      CalendarBuilder calendarBuilder = new CalendarBuilder(league);

      NBACalendar calendar = calendarBuilder.buildRegulaSeasonCalendar();
      for (GameDay gameDay : calendar.getCalendar().values()) {
         assertNotNull(gameDay.getGames());
         assertFalse(gameDay.getGames().isEmpty());
      }
   }

   @Test
   public void shouldKeepGamesWithinRegularSeasonDates() {
      League league = new LeagueBuilder().build();
      CalendarBuilder calendarBuilder = new CalendarBuilder(league);

      NBACalendar calendar = calendarBuilder.buildRegulaSeasonCalendar();
      assertFalse(calendar.getCalendar().isEmpty());
      assertFalse(calendar.getCalendar().firstKey().isBefore(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE));
      assertFalse(calendar.getCalendar().lastKey().isAfter(CalendarConfiguration.REGULAR_SEASON_END_DATE));
   }

   @Test
   public void shouldScheduleGamesInsideTeamSchedules() {
      League league = new LeagueBuilder().build();
      CalendarBuilder calendarBuilder = new CalendarBuilder(league);

      calendarBuilder.buildRegulaSeasonCalendar();
      for (Team team : TeamRepositery.getInstance().getAllTeams()) {
         assertTrue(team.getSchedule().getScheduledGames().values().size() > 0);
      }
   }

   @Test
   public void shouldNotScheduleTwoGamesForTheSameTeamOnTheSameDay() {
      League league = new LeagueBuilder().build();
      CalendarBuilder calendarBuilder = new CalendarBuilder(league);

      NBACalendar calendar = calendarBuilder.buildRegulaSeasonCalendar();
      for (GameDay gameDay : calendar.getCalendar().values()) {
         Set<String> teamsPlayingThatDay = new HashSet<>();
         for (Game game : gameDay.getGames()) {
            String homeTeamName = game.getGameContext().getHomeTeam().getName();
            String awayTeamName = game.getGameContext().getAwayTeam().getName();

            assertTrue(teamsPlayingThatDay.add(homeTeamName));
            assertTrue(teamsPlayingThatDay.add(awayTeamName));
         }
      }
   }

   @Test
   public void shouldMarkGeneratedGamesAsScheduled() {
      League league = new LeagueBuilder().build();
      CalendarBuilder calendarBuilder = new CalendarBuilder(league);

      NBACalendar calendar = calendarBuilder.buildRegulaSeasonCalendar();
      GameDay firstGameDay = calendar.getCalendar().firstEntry().getValue();
      Game firstGame = firstGameDay.getGames().get(0);
      assertNotNull(firstGameDay);
      assertNotNull(firstGame);
      assertTrue(firstGame.getGameContext().isScheduled());
      assertTrue(firstGame.getGameContext().getHomeTeam().getSchedule().isPlayingOn(firstGameDay.getDate()));
      assertTrue(firstGame.getGameContext().getAwayTeam().getSchedule().isPlayingOn(firstGameDay.getDate()));
   }

   @Test
   public void shouldRegisterPlannedGameInBothTeamSchedules() {
      League league = new LeagueBuilder().build();
      CalendarBuilder calendarBuilder = new CalendarBuilder(league);

      NBACalendar calendar = calendarBuilder.buildRegulaSeasonCalendar();
      GameDay firstGameDay = calendar.getCalendar().firstEntry().getValue();
      Game firstGame = firstGameDay.getGames().get(0);
      Team homeTeam = firstGame.getGameContext().getHomeTeam();
      Team awayTeam = firstGame.getGameContext().getAwayTeam();

      assertSame(firstGame, homeTeam.getSchedule().getScheduledGames().get(firstGameDay.getDate()));
      assertSame(firstGame, awayTeam.getSchedule().getScheduledGames().get(firstGameDay.getDate()));
   }

   @Test
   public void shouldGenerateGamesForEachTeam() {
      League league = new LeagueBuilder().build();
      CalendarBuilder calendarBuilder = new CalendarBuilder(league);
      calendarBuilder.buildRegulaSeasonCalendar();

      for (Team team : league.getAllTeam()) {
         assertTrue(team.getSchedule().getScheduledGames().values().size() > 75);
      }
   }

}
