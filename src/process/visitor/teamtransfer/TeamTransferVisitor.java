package process.visitor.teamtransfer;

import data.team.finance.transfer.AllIn;
import data.team.finance.transfer.Balanced;
import data.team.finance.transfer.Rebuild;
import data.team.finance.transfer.SalaryDump;
import data.team.finance.transfer.SmallAdjust;
import data.team.finance.transfer.SuperstarBuild;

public interface TeamTransferVisitor<T> {
	public T visit(AllIn strategy);

	public T visit(SuperstarBuild strategy);

	public T visit(SmallAdjust strategy);

	public T visit(Balanced strategy);

	public T visit(Rebuild strategy);

	public T visit(SalaryDump strategy);
}
