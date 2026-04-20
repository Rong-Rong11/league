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

public class PreSeasonTradeSatisfactionVisitor
implements TeamTransferVisitor<Boolean> {
	private int transferMade;

	public PreSeasonTradeSatisfactionVisitor(int n) {
		this.transferMade = n;
	}

	@Override
	public Boolean visit(AllIn allIn) {
		return this.transferMade >= 2;
	}

	@Override
	public Boolean visit(SuperstarBuild superstarBuild) {
		return this.transferMade >= 1;
	}

	@Override
	public Boolean visit(SmallAdjust smallAdjust) {
		return this.transferMade >= 1;
	}

	@Override
	public Boolean visit(Balanced balanced) {
		return this.transferMade >= 1;
	}

	@Override
	public Boolean visit(Rebuild rebuild) {
		return this.transferMade >= 2;
	}

	@Override
	public Boolean visit(SalaryDump salaryDump) {
		return this.transferMade >= 1;
	}
}
