package process.visitor.financialpolicy;

import data.team.finance.financialpolicy.AmbitiousPolicy;
import data.team.finance.financialpolicy.BalancedPolicy;
import data.team.finance.financialpolicy.ThriftyPolicy;

public class GameMatchProfilWeightVisitor implements FinancialPolicyVisitor<Double> {

   @Override
   public Double visit(ThriftyPolicy thriftyPolicy) {
      return 0.8;
   }

   @Override
   public Double visit(BalancedPolicy policy) {
      return 0.6;
   }

   @Override
   public Double visit(AmbitiousPolicy policy) {
      return 0.4;
   }

}
