package process.visitor.financialpolicy;

import data.team.finance.financialpolicy.AmbitiousPolicy;
import data.team.finance.financialpolicy.BalancedPolicy;
import data.team.finance.financialpolicy.ThriftyPolicy;

public class GameEcoWeightVisitor implements FinancialProfilVisitor<Double> {

   @Override
   public Double visit(ThriftyPolicy thriftyPolicy) {
      // TODO Auto-generated method stub
      return 0.2;
   }

   @Override
   public Double visit(BalancedPolicy balancedPolicy) {
      // TODO Auto-generated method stub
      return 0.4;
   }

   @Override
   public Double visit(AmbitiousPolicy ambitiousPolicy) {
      // TODO Auto-generated method stub
      return 0.6;
   }

}
