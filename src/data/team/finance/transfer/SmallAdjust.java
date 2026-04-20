/*
	* Decompiled with CFR 0.152.
	*/
package data.team.finance.transfer;

import process.visitor.teamtransfer.TeamTransferVisitor;

public class SmallAdjust
		extends TeamTransferStrategy {
	public SmallAdjust() {

	}

	@Override
	public boolean isAllIn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBalanced() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRebuild() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSalaryDump() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSmallAdjust() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isSuperstarBuild() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T accept(TeamTransferVisitor<T> teamTransferVisitor) {
		return teamTransferVisitor.visit(this);
	}
}
