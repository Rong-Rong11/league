package process.visitor.financialpolicy;

import data.team.finance.financialpolicy.AmbitiousPolicy;
import data.team.finance.financialpolicy.BalancedPolicy;
import data.team.finance.financialpolicy.ThriftyPolicy;

public class GameMatchProfilWeightVisitor implements FinancialProfilVisitor<Double> {

   @Override
   public Double visit(ThriftyPolicy thriftyPolicy) {
      // TODO Auto-generated method stub
      return 0.8;
   }

   @Override
   public Double visit(BalancedPolicy var1) {
      // TODO Auto-generated method stub
      return 0.6;
   }

   @Override
   public Double visit(AmbitiousPolicy var1) {
      // TODO Auto-generated method stub
      return 0.4;
   }

}
