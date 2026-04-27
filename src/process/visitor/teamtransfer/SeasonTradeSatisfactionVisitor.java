package process.visitor.teamtransfer;

import data.team.finance.transfer.AllIn;
import data.team.finance.transfer.Balanced;
import data.team.finance.transfer.Rebuild;
import data.team.finance.transfer.SalaryDump;
import data.team.finance.transfer.SmallAdjust;
import data.team.finance.transfer.SuperstarBuild;

public class SeasonTradeSatisfactionVisitor
implements TeamTransferVisitor<Boolean> {
	private int transfersMade;
	private String seasonIntent;

	public SeasonTradeSatisfactionVisitor(int transfersMade, String seasonIntent) {
		this.transfersMade = transfersMade;
		this.seasonIntent = seasonIntent;
	}

	@Override
	public Boolean visit(AllIn allIn) {
		return isSatisfiedForThresholds(3, 4, 5);
	}

	@Override
	public Boolean visit(SuperstarBuild superstarBuild) {
		return isSatisfiedForThresholds(2, 3, 4);
	}

	@Override
	public Boolean visit(SmallAdjust smallAdjust) {
		return this.transfersMade >= 3;
	}

	@Override
	public Boolean visit(Balanced balanced) {
		return isSatisfiedForThresholds(2, 3, 4);
	}

	@Override
	public Boolean visit(Rebuild rebuild) {
		return this.transfersMade >= 6;
	}

	@Override
	public Boolean visit(SalaryDump salaryDump) {
		return isSatisfiedForThresholds(2, 4, 4);
	}

	private Boolean isSatisfiedForThresholds(int stableThreshold, int buyerThreshold, int sellerThreshold) {
		if (this.seasonIntent.equals("seller")) {
			return this.transfersMade >= sellerThreshold;
		}
		if (this.seasonIntent.equals("buyer")) {
			return this.transfersMade >= buyerThreshold;
		}
		return this.transfersMade >= stableThreshold;
	}
}
