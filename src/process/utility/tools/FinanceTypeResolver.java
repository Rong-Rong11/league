package process.utility.tools;

import data.finance.budget.FinanceSeasonMoment;
import data.finance.budget.expense.ExpenseType;
import data.finance.budget.income.IncomeType;

public class FinanceTypeResolver {

	private FinanceSeasonMoment seasonMoment;

	public FinanceTypeResolver(FinanceSeasonMoment seasonMoment) {
		this.seasonMoment = seasonMoment;
	}

	public IncomeType resolveIncomeType(IncomeType baseType) {
		if (seasonMoment == FinanceSeasonMoment.PLAYOFF) {
			switch (baseType) {
				case TICKET_OFFICE:
					return IncomeType.PLAYOFF_TICKET_OFFICE;
				case CONCESSIONS:
					return IncomeType.PLAYOFF_CONCESSIONS;
				case PARKING:
					return IncomeType.PLAYOFF_PARKING;
				case LOCAL_TV:
					return IncomeType.PLAYOFF_LOCAL_TV;
				case GAME_LOCAL_MERCHANDISING:
					return IncomeType.PLAYOFF_LOCAL_MERCHANDISING;
				default:
					return baseType;
			}
		}
		return baseType;
	}

	public ExpenseType resolveExpenseType(ExpenseType baseType) {
		if (seasonMoment == FinanceSeasonMoment.PLAYOFF) {
			switch (baseType) {
				case STADIUM_COST:
					return ExpenseType.PLAYOFF_STADIUM_COST;
				case STAFF_COST:
					return ExpenseType.PLAYOFF_STAFF_COST;
				case SECURITY_COST:
					return ExpenseType.PLAYOFF_SECURITY_COST;
				case LOGISTIC_COST:
					return ExpenseType.PLAYOFF_LOGISTIC_COST;
				case TRAVEL_COST:
					return ExpenseType.PLAYOFF_TRAVEL_COST;
				default:
					return baseType;
			}
		}
		return baseType;
	}
}
