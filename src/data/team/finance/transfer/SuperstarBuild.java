/*
	* Decompiled with CFR 0.152.
	*/
package data.team.finance.transfer;

import process.visitor.teamtransfer.TeamTransferVisitor;

public class SuperstarBuild
		extends TeamTransferStrategy {
	public SuperstarBuild() {

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
		return false;
	}

	@Override
	public boolean isSmallAdjust() {
		return false;
	}

	@Override
	public boolean isSuperstarBuild() {
		return true;
	}

	@Override
	public <T> T accept(TeamTransferVisitor<T> teamTransferVisitor) {
		return teamTransferVisitor.visit(this);
	}
}
