package process;

import data.finance.budget.Budget;
import data.finance.budget.Expense;
import data.finance.budget.Income;
import data.league.LeagueFinance;
import data.player.Player;
import data.team.Team;

//les sommes en millions
public class FinanceManager {
	public static double getAverageSalary(Team team) {
		int numberOfPlayers = 0 ; 
		double sumOfSalary  = 0 ; 
		for(Player player : team.getPlayers().values()) {
			sumOfSalary += player.getSalary() ; 
			numberOfPlayers ++ ; 
		}
		return sumOfSalary / numberOfPlayers ; 
	}
	
	public static void updateBudget(Budget budget) {
		double amount = 0 ; 
		for(Income income : budget.getIncomes().values()) {
			amount += income.getAmount() ; 
		}
		for(Expense expense : budget.getExpenses().values()) {
			amount -= expense.getAmount() ; 
		}
		budget.setRemainingAmount(amount); 
	}
	
}
