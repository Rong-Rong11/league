package data.team.finance.transfer;

import process.visitor.teamtransfer.TeamTransferVisitor;

public abstract class TeamTransferStrategy {
	private String seasonIntent = "stable";

	public TeamTransferStrategy() {

	}

	public String getSeasonIntent() {
		return this.seasonIntent;
	}

	public void setSeasonIntent(String seasonIntent) {
		this.seasonIntent = seasonIntent;
	}

	public abstract boolean isAllIn();

	public abstract boolean isBalanced();

	public abstract boolean isRebuild();

	public abstract boolean isSalaryDump();

	public abstract boolean isSmallAdjust();

	public abstract boolean isSuperstarBuild();

	public abstract <T> T accept(TeamTransferVisitor<T> visitor);
}
