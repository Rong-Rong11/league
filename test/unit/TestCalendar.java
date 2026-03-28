package unit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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

public class TestCalendar {

   @Before
   public void setUp() {
      PlayerRepositery.getInstance().clear();
      TeamRepositery.getInstance().clear();
      DivisionRepositery.getInstance().clear();
   }

   @Test
   public void buildRegularSeasonCalendarAfterLeagueCreation() {
      League league = new LeagueBuilder().build();
      CalendarBuilder calendarBuilder = new CalendarBuilder(league);

      NBACalendar nbaCalendar = calendarBuilder.buildRegulaSeasonCalendar();

      assertNotNull(nbaCalendar);
      assertNotNull(nbaCalendar.getCalendar());
      assertFalse(nbaCalendar.getCalendar().isEmpty());
      assertTrue(nbaCalendar.getCalendar().values().size() > 0);
   }

   @Test
   public void scheduleGamesInsideTeamSchedules() {
      League league = new LeagueBuilder().build();
      CalendarBuilder calendarBuilder = new CalendarBuilder(league);

      NBACalendar calendar = calendarBuilder.buildRegulaSeasonCalendar();
      for (Team team : TeamRepositery.getInstance().getAllTeams()) {
         assertTrue(team.getSchedule().getScheduledGames().values().size() > 0);
      }
   }

   @Test
   public void markGeneratedGamesAsScheduled() {
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
}
