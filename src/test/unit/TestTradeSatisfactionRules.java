package test.unit;

import static org.junit.Assert.*;

import org.junit.Test;

import data.team.finance.transfer.AllIn;
import data.team.finance.transfer.Balanced;
import data.team.finance.transfer.SalaryDump;
import data.team.finance.transfer.SuperstarBuild;
import process.visitor.teamtransfer.SeasonTradeSatisfactionVisitor;

public class TestTradeSatisfactionRules {

	@Test
	public void shouldKeepBalancedTeamActiveAfterSinglePreseasonTrade() {
		SeasonTradeSatisfactionVisitor visitor = new SeasonTradeSatisfactionVisitor(1, "stable");

		assertFalse(new Balanced().accept(visitor));
	}

	@Test
	public void shouldRequireExtraMovesForBuyerStrategies() {
		SeasonTradeSatisfactionVisitor visitor = new SeasonTradeSatisfactionVisitor(1, "buyer");

		assertFalse(new SuperstarBuild().accept(visitor));
		assertFalse(new AllIn().accept(visitor));
	}

	@Test
	public void shouldLimitSalaryDumpTeamsAfterMultipleSeasonTrades() {
		SeasonTradeSatisfactionVisitor visitor = new SeasonTradeSatisfactionVisitor(2, "stable");
		SeasonTradeSatisfactionVisitor sellerVisitor = new SeasonTradeSatisfactionVisitor(3, "seller");

		assertTrue(new SalaryDump().accept(visitor));
		assertFalse(new SalaryDump().accept(sellerVisitor));
	}
}
