package process.orchestrator;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

public class SimulationClock {
   private final LocalDate startDate;

   private LocalDate currentDate;
   private int currentWeek;
   private int currentMonth;

   public SimulationClock(LocalDate startDate) {
      this.startDate = startDate;
      reset();
   }

   public void reset() {
      currentDate = startDate;
      currentWeek = 1;
      currentMonth = 1;
   }

   public void setDate(LocalDate date) {
      if (date != null) {
         currentDate = date;
      }
   }

   public void nextDay() {
      currentDate = currentDate.plusDays(1);
   }

   public int computeWeek() {
      long daysBetween = ChronoUnit.DAYS.between(startDate, currentDate);
      return (int) (daysBetween / 7) + 1;
   }

   public int computeMonth() {
      Month startMonth = startDate.getMonth();
      Month currentMonthDate = currentDate.getMonth();

      int monthsBetween = currentMonthDate.getValue() - startMonth.getValue();
      if (monthsBetween < 0) {
         monthsBetween += 12;
      }

      return monthsBetween + 1;
   }

   public boolean hasWeekChanged() {
      return computeWeek() != currentWeek;
   }

   public boolean hasMonthChanged() {
      return computeMonth() != currentMonth;
   }

   public int refreshWeek() {
      currentWeek = computeWeek();
      return currentWeek;
   }

   public int refreshMonth() {
      currentMonth = computeMonth();
      return currentMonth;
   }

   public LocalDate getCurrentDate() {
      return currentDate;
   }

   public int getCurrentWeek() {
      return currentWeek;
   }

   public int getCurrentMonth() {
      return currentMonth;
   }
}
