package process.manager.financetools;

import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.finance.budget.Expense;
import data.league.League;
import process.utilitary.CalendarUtilitary;
import process.utilitary.FinanceUtilitary;

public class LeagueExpenseCalculator {

   private League league;

   public LeagueExpenseCalculator(League league) {
      this.league = league;
   }

   public void applyMonthlyExpenses(int month) {
      Budget budget = league.getLeagueFinance().getBudget();

      double administrativeCost = calculateAdministrativeCost();
      double mediaCost = calculateMediaCost(month);
      double marketingCost = calculateMarketingCost(month);
      double officiatingCost = calculateOfficiatingCost(month);

      FinanceUtilitary.addExpense(
            budget,
            new Expense(FinanceConfiguration.EXPENSE_TYPE_ADMINISTRATIVE_COST, administrativeCost),
            month);

      FinanceUtilitary.addExpense(
            budget,
            new Expense(FinanceConfiguration.EXPENSE_TYPE_MEDIA_COST, mediaCost),
            month);

      FinanceUtilitary.addExpense(
            budget,
            new Expense(FinanceConfiguration.EXPENSE_TYPE_MARKETING_COST, marketingCost),
            month);

      FinanceUtilitary.addExpense(
            budget,
            new Expense(FinanceConfiguration.EXPENSE_TYPE_OFFICIATING_COST, officiatingCost),
            month);

      FinanceUtilitary.updateBudget(budget);
   }

   private double calculateAdministrativeCost() {
      return FinanceConfiguration.LEAGUE_ADMINISTRATIVE_COST;
   }

   private double calculateMediaCost(int month) {
      double cost = FinanceConfiguration.LEAGUE_MEDIA_COST;
      if (CalendarUtilitary.isImportantMonth(month)) {
         cost *= 1.10;
      }
      return cost;
   }

   private double calculateMarketingCost(int month) {
      double cost = FinanceConfiguration.LEAGUE_MARKETING_COST;
      if (CalendarUtilitary.isImportantMonth(month)) {
         cost *= 1.20;
      }
      return cost;
   }

   private double calculateOfficiatingCost(int month) {
      double cost = FinanceConfiguration.LEAGUE_OFFICIATING_COST;
      if (CalendarUtilitary.isImportantMonth(month)) {
         cost *= 1.05;
      }
      return cost;
   }

}