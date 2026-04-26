package test.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import data.finance.budget.FinanceSeasonMoment;
import data.finance.budget.expense.ExpenseType;
import data.finance.budget.income.IncomeType;
import process.utility.tools.FinanceTypeResolver;

public class TestFinanceTypeResolver {

	@Test
	public void shouldKeepRegularSeasonIncomeTypesUnchanged() {
		FinanceTypeResolver resolver = new FinanceTypeResolver(FinanceSeasonMoment.REGULAR_SEASON);

		assertEquals(IncomeType.TICKET_OFFICE, resolver.resolveIncomeType(IncomeType.TICKET_OFFICE));
		assertEquals(IncomeType.LOCAL_TV, resolver.resolveIncomeType(IncomeType.LOCAL_TV));
		assertEquals(IncomeType.OTHER, resolver.resolveIncomeType(IncomeType.OTHER));
	}

	@Test
	public void shouldResolvePlayoffIncomeTypesForGameRevenue() {
		FinanceTypeResolver resolver = new FinanceTypeResolver(FinanceSeasonMoment.PLAYOFF);

		assertEquals(IncomeType.PLAYOFF_TICKET_OFFICE, resolver.resolveIncomeType(IncomeType.TICKET_OFFICE));
		assertEquals(IncomeType.PLAYOFF_CONCESSIONS, resolver.resolveIncomeType(IncomeType.CONCESSIONS));
		assertEquals(IncomeType.PLAYOFF_PARKING, resolver.resolveIncomeType(IncomeType.PARKING));
		assertEquals(IncomeType.PLAYOFF_LOCAL_TV, resolver.resolveIncomeType(IncomeType.LOCAL_TV));
		assertEquals(IncomeType.PLAYOFF_LOCAL_MERCHANDISING,
				resolver.resolveIncomeType(IncomeType.GAME_LOCAL_MERCHANDISING));
	}

	@Test
	public void shouldResolvePlayoffExpenseTypesForGameCosts() {
		FinanceTypeResolver resolver = new FinanceTypeResolver(FinanceSeasonMoment.PLAYOFF);

		assertEquals(ExpenseType.PLAYOFF_STADIUM_COST, resolver.resolveExpenseType(ExpenseType.STADIUM_COST));
		assertEquals(ExpenseType.PLAYOFF_STAFF_COST, resolver.resolveExpenseType(ExpenseType.STAFF_COST));
		assertEquals(ExpenseType.PLAYOFF_SECURITY_COST, resolver.resolveExpenseType(ExpenseType.SECURITY_COST));
		assertEquals(ExpenseType.PLAYOFF_LOGISTIC_COST, resolver.resolveExpenseType(ExpenseType.LOGISTIC_COST));
		assertEquals(ExpenseType.PLAYOFF_TRAVEL_COST, resolver.resolveExpenseType(ExpenseType.TRAVEL_COST));
	}

	@Test
	public void shouldKeepNonGameTypesUnchangedDuringPlayoffs() {
		FinanceTypeResolver resolver = new FinanceTypeResolver(FinanceSeasonMoment.PLAYOFF);

		assertEquals(IncomeType.OTHER, resolver.resolveIncomeType(IncomeType.OTHER));
		assertEquals(ExpenseType.ADMINISTRATIVE_COST, resolver.resolveExpenseType(ExpenseType.ADMINISTRATIVE_COST));
	}
}
