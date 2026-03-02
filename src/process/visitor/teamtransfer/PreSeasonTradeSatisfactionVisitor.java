package process.visitor.teamtransfer;

import data.team.finance.transfer.AllIn;
import data.team.finance.transfer.Balanced;
import data.team.finance.transfer.Rebuild;
import data.team.finance.transfer.SalaryDump;
import data.team.finance.transfer.SmallAdjust;
import data.team.finance.transfer.SuperstarBuild;

public class PreSeasonTradeSatisfactionVisitor implements TeamTransferVisitor<Boolean> {

  private int transferMade;

  public PreSeasonTradeSatisfactionVisitor(int transferMade) {
    this.transferMade = transferMade;
  }

  public Boolean visit(AllIn allIn) {
    return transferMade >= 2;
  }

  @Override
  public Boolean visit(SuperstarBuild superstarBuild) {
    return transferMade >= 1;
  }

  @Override
  public Boolean visit(SmallAdjust smallAdjust) {
    return transferMade >= 1;
  }

  @Override
  public Boolean visit(Balanced balanced) {
    return transferMade >= 1;
  }

  @Override
  public Boolean visit(Rebuild rebuild) {
    return transferMade >= 2;
  }

  @Override
  public Boolean visit(SalaryDump salaryDump) {
    return transferMade >= 1;
  }
}
