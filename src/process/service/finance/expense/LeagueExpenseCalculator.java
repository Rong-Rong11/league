package process.service.finance.expense;

import config.FinanceConfiguration;
import data.calendar.GameDay;
import data.finance.GameStat;
import data.finance.budget.Budget;
import data.finance.budget.expense.Expense;
import data.finance.budget.expense.ExpenseType;
import data.league.League;
import data.sport.setup.Game;
import data.sport.setup.PlayoffSeries;
import process.service.finance.FinanceManager;
import process.utility.CalendarUtility;
import process.utility.FinanceUtility;

public class LeagueExpenseCalculator {

   private League league;
   private FinanceManager financeManager;

   public LeagueExpenseCalculator(League league) {
      this.league = league;
   }

   public void setFinanceManager(FinanceManager financeManager) {
      this.financeManager = financeManager;
   }

   public void applyMonthlyExpenses(int month) {
      Budget budget = league.getLeagueFinance().getBudget();

      double administrativeCost = calculateAdministrativeCost();
      double mediaCost = calculateMediaCost(month);
      double marketingCost = calculateMarketingCost(month);
      double officiatingCost = calculateOfficiatingCost(month);

      FinanceUtility.addExpense(
            budget,
            new Expense(ExpenseType.ADMINISTRATIVE_COST, administrativeCost),
            month);

      FinanceUtility.addExpense(
            budget,
            new Expense(ExpenseType.MEDIA_COST, mediaCost),
            month);

      FinanceUtility.addExpense(
            budget,
            new Expense(ExpenseType.MARKETING_COST, marketingCost),
            month);

      FinanceUtility.addExpense(
            budget,
            new Expense(ExpenseType.OFFICIATING_COST, officiatingCost),
            month);

      FinanceUtility.updateBudget(budget);
   }

   // complexifier
   private double calculateAdministrativeCost() {
      return FinanceConfiguration.LEAGUE_ADMINISTRATIVE_COST;
   }

   private double calculateMediaCost(int month) {
      double cost = FinanceConfiguration.LEAGUE_MEDIA_COST;
      if (CalendarUtility.isImportantMonth(month)) {
         cost *= 1.16;
      }
      cost *= getImportantGamesExpenseRate(month, 0.0018);
      cost *= getPlayoffGamesExpenseRate(month, 0.0048);
      cost *= getActivePlayoffTeamsExpenseRate(month, 0.0032);
      cost *= getSeasonExpenseRate(month, 0.08);
      cost *= getControlledEconomicNoise(month, 0.016);
      return cost;
   }

   private double calculateMarketingCost(int month) {
      double cost = FinanceConfiguration.LEAGUE_MARKETING_COST;
      if (CalendarUtility.isImportantMonth(month)) {
         cost *= 1.28;
      }
      cost *= getImportantGamesExpenseRate(month, 0.0024);
      cost *= getPlayoffGamesExpenseRate(month, 0.0054);
      cost *= getActivePlayoffTeamsExpenseRate(month, 0.0038);
      cost *= getSeasonExpenseRate(month, 0.10);
      cost *= getControlledEconomicNoise(month, 0.020);
      return cost;
   }

   private double calculateOfficiatingCost(int month) {
      double cost = FinanceConfiguration.LEAGUE_OFFICIATING_COST;
      if (CalendarUtility.isImportantMonth(month)) {
         cost *= 1.10;
      }
      cost *= getImportantGamesExpenseRate(month, 0.0018);
      cost *= getPlayoffGamesExpenseRate(month, 0.0058);
      cost *= getActivePlayoffTeamsExpenseRate(month, 0.0038);
      cost *= getSeasonExpenseRate(month, 0.08);
      cost *= getControlledEconomicNoise(month, 0.014);
      return cost;
   }

   private double getImportantGamesExpenseRate(int month, double ratePerGame) {
      return 1 + (countImportantGamesInMonth(month) * ratePerGame);
   }

   private double getPlayoffGamesExpenseRate(int month, double ratePerGame) {
      return 1 + (countPlayoffGamesInMonth(month) * ratePerGame);
   }

   private double getActivePlayoffTeamsExpenseRate(int month, double ratePerTeam) {
      if (!isPlayoffMonth(month)) {
         return 1.0;
      }
      return 1 + (countActivePlayoffTeams() * ratePerTeam);
   }

   private double getSeasonExpenseRate(int month, double playoffBonusRate) {
      if (isPlayoffMonth(month)) {
         return 1 + playoffBonusRate;
      }
      if (CalendarUtility.isImportantMonth(month)) {
         return 1.03;
      }
      return 1.0;
   }

   private double getControlledEconomicNoise(int month, double maxAmplitude) {
      int importantGames = countImportantGamesInMonth(month);
      int playoffGames = countPlayoffGamesInMonth(month);
      int activeTeams = countActivePlayoffTeams();
      double wave = Math.cos((month * 1.41) + (importantGames * 0.13) + (playoffGames * 0.21) + (activeTeams * 0.17));
      return 1 + (wave * maxAmplitude);
   }

   private int countImportantGamesInMonth(int month) {
      int count = 0;
      count += countImportantGamesForSeasonMonth(month, false);
      count += countImportantGamesForSeasonMonth(month, true);
      return count;
   }

   private int countPlayoffGamesInMonth(int month) {
      if (league.getPlayoff() == null || league.getPlayoff().getNbaCalendar() == null) {
         return 0;
      }

      int count = 0;
      for (GameDay gameDay : league.getPlayoff().getNbaCalendar().getCalendar().values()) {
         if (gameDay.getDate() == null || !matchesFinanceMonth(gameDay.getDate(), month)) {
            continue;
         }
         count += gameDay.getGames().size();
      }
      return count;
   }

   private int countImportantGamesForSeasonMonth(int month, boolean playoff) {
      int count = 0;
      if (playoff) {
         if (league.getPlayoff() == null || league.getPlayoff().getNbaCalendar() == null) {
            return 0;
         }
         for (GameDay gameDay : league.getPlayoff().getNbaCalendar().getCalendar().values()) {
            if (gameDay.getDate() == null || !matchesFinanceMonth(gameDay.getDate(), month)) {
               continue;
            }
            for (Game game : gameDay.getGames()) {
               if (isImportantGame(game, gameDay.getDate())) {
                  count++;
                  if (hasHighAttendance(game)) {
                     count++;
                  }
               }
            }
         }
         return count;
      }

      if (league.getRegularSeason() == null || league.getRegularSeason().getNbaCalendar() == null) {
         return 0;
      }
      for (GameDay gameDay : league.getRegularSeason().getNbaCalendar().getCalendar().values()) {
         if (gameDay.getDate() == null || !matchesFinanceMonth(gameDay.getDate(), month)) {
            continue;
         }
         for (Game game : gameDay.getGames()) {
            if (isImportantGame(game, gameDay.getDate())) {
               count++;
               if (hasHighAttendance(game)) {
                  count++;
               }
            }
         }
      }
      return count;
   }

   private int countActivePlayoffTeams() {
      if (league.getPlayoff() == null || league.getPlayoff().getCurrentRound() == null) {
         return 0;
      }

      int count = 0;
      for (PlayoffSeries series : CalendarUtility.getCurrentRoundSeries(league.getPlayoff())) {
         if (series == null || series.isFinished()) {
            continue;
         }
         count += 2;
      }
      return count;
   }

   private boolean isPlayoffMonth(int month) {
      return month >= 8;
   }

   private boolean matchesFinanceMonth(java.time.LocalDate date, int month) {
      int startMonth = league.getRegularSeason().getDebutDate().getMonthValue();
      int monthDelta = date.getMonthValue() - startMonth;
      if (monthDelta < 0) {
         monthDelta += 12;
      }
      return (monthDelta + 1) == month;
   }

   private boolean isImportantGame(Game game, java.time.LocalDate date) {
      return CalendarUtility.popularityScoreGame(game, date) >= 95 || game.getGameContext().isRivalry();
   }

   private boolean hasHighAttendance(Game game) {
      if (financeManager == null) {
         return false;
      }
      GameStat gameStat = financeManager.getGameStat(game);
      return gameStat != null && gameStat.getAttendanceRate() > 0.85;
   }

}
