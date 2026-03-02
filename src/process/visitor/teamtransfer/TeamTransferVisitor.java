package process.visitor.teamtransfer;

import data.team.finance.transfer.AllIn;
import data.team.finance.transfer.Balanced;
import data.team.finance.transfer.Rebuild;
import data.team.finance.transfer.SalaryDump;
import data.team.finance.transfer.SmallAdjust;
import data.team.finance.transfer.SuperstarBuild;

public interface TeamTransferVisitor<T> {
	T visit(AllIn allIn);

	T visit(SuperstarBuild superstarBuild);

	T visit(SmallAdjust smallAdjust);

	T visit(Balanced balanced);

	T visit(Rebuild rebuild);

	T visit(SalaryDump salaryDump);

}
