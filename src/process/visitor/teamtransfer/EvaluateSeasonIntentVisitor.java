/*
	* Decompiled with CFR 0.152.
	*/
package process.visitor.teamtransfer;

import data.team.Team;
import data.team.finance.transfer.AllIn;
import data.team.finance.transfer.Balanced;
import data.team.finance.transfer.Rebuild;
import data.team.finance.transfer.SalaryDump;
import data.team.finance.transfer.SmallAdjust;
import data.team.finance.transfer.SuperstarBuild;
import process.visitor.financialprofil.ValidateTradeVisitor;

public class EvaluateSeasonIntentVisitor
		implements TeamTransferVisitor<String> {
	private Team team;
	private double teamPerformatingRate;
	private double salaryCap;

	public EvaluateSeasonIntentVisitor(Team team, double salaryCap) {
		this.team = team;
		this.salaryCap = salaryCap;
		this.teamPerformatingRate = team.getTeamPerformance().getPerformanceRating();
	}

	@Override
	public String visit(AllIn allIn) {
		if (this.teamPerformatingRate > 0.7) {
			return "buyer";
		}
		if (this.teamPerformatingRate < 0.45) {
			return "seller";
		}
		return "stable";
	}

	@Override
	public String visit(SuperstarBuild superstarBuild) {
		if (this.teamPerformatingRate < 0.5) {
			return "seller";
		}
		if (this.teamPerformatingRate < 0.65) {
			return "buyer";
		}
		return "stable";
	}

	@Override
	public String visit(SmallAdjust smallAdjust) {
		if (this.teamPerformatingRate < 0.4) {
			return "seller";
		}
		return "stable";
	}

	@Override
	public String visit(Balanced balanced) {
		if (this.teamPerformatingRate > 0.7) {
			return "stable";
		}
		if (this.teamPerformatingRate < 0.35) {
			return "seller";
		}
		return "buyer";
	}

	@Override
	public String visit(Rebuild rebuild) {
		if (this.teamPerformatingRate > 0.75) {
			return "buyer";
		}
		return "seller";
	}

	@Override
	public String visit(SalaryDump salaryDump) {
		ValidateTradeVisitor validateTradeVisitor = new ValidateTradeVisitor(
				this.team.getTeamFinance().getCurrentPayroll(),
				this.salaryCap,
				this.team.getTeamFinance().getStructure().getMarketSize());
		if (this.team.getTeamFinance().getBehavior().getFinancialProfil().accept(validateTradeVisitor).booleanValue()) {
			return "seller";
		}
		if (this.teamPerformatingRate > 0.75) {
			return "stable";
		}
		return "seller";
	}
}
