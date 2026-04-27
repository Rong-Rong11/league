package data.team.finance.transfer;

import process.visitor.teamtransfer.TeamTransferVisitor;

public class SalaryDump
		extends TeamTransferStrategy {
	public SalaryDump() {

	}

	@Override
	public boolean isAllIn() {
		return false;
	}

	@Override
	public boolean isBalanced() {
		return false;
	}

	@Override
	public boolean isRebuild() {
		return false;
	}

	@Override
	public boolean isSalaryDump() {
		return true;
	}

	@Override
	public boolean isSmallAdjust() {
		return false;
	}

	@Override
	public boolean isSuperstarBuild() {
		return false;
	}

	@Override
	public <T> T accept(TeamTransferVisitor<T> teamTransferVisitor) {
		return teamTransferVisitor.visit(this);
	}
}
