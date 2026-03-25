package data.finance.budget.expense;

import data.finance.budget.FinanceScope;

public final class ExpenseDefinition {
   private final String label;
   private final FinanceScope scope;
   private final ExpenseCategory category;

   public ExpenseDefinition(String label, FinanceScope scope, ExpenseCategory category) {
      this.label = label;
      this.scope = scope;
      this.category = category;
   }

   public String getLabel() {
      return this.label;
   }

   public FinanceScope getScope() {
      return this.scope;
   }

   public ExpenseCategory getCategory() {
      return this.category;
   }
}
