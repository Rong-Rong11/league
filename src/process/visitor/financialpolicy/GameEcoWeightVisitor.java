package process.visitor.financialpolicy;

import data.team.finance.financialpolicy.AmbitiousPolicy;
import data.team.finance.financialpolicy.BalancedPolicy;
import data.team.finance.financialpolicy.ThriftyPolicy;

public class GameEcoWeightVisitor implements FinancialPolicyVisitor<Double> {

   @Override
   public Double visit(ThriftyPolicy thriftyPolicy) {
      return 0.2;
   }

   @Override
   public Double visit(BalancedPolicy balancedPolicy) {
      return 0.4;
   }

   @Override
   public Double visit(AmbitiousPolicy ambitiousPolicy) {
      return 0.6;
   }

}
