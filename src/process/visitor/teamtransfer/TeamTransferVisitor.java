/*
	* Decompiled with CFR 0.152.
	*/
package process.visitor.teamtransfer;

import data.team.finance.transfer.AllIn;
import data.team.finance.transfer.Balanced;
import data.team.finance.transfer.Rebuild;
import data.team.finance.transfer.SalaryDump;
import data.team.finance.transfer.SmallAdjust;
import data.team.finance.transfer.SuperstarBuild;

public interface TeamTransferVisitor<T> {
	public T visit(AllIn var1);

	public T visit(SuperstarBuild var1);

	public T visit(SmallAdjust var1);

	public T visit(Balanced var1);

	public T visit(Rebuild var1);

	public T visit(SalaryDump var1);
}
