package data.team.finance.transfer;

import process.visitor.teamtransfer.TeamTransferVisitor;

public class Balanced
		extends TeamTransferStrategy {
	public Balanced() {

	}

	@Override
	public boolean isAllIn() {
		return false;
	}

	@Override
	public boolean isBalanced() {
		return true;
	}

	@Override
	public boolean isRebuild() {
		return false;
	}

	@Override
	public boolean isSalaryDump() {
		return false;
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
