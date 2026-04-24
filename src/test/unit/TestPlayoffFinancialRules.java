package test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import data.league.PlayoffRound;
import process.service.finance.playoff.PlayoffFinancialRules;

public class TestPlayoffFinancialRules {

	@Test
	public void shouldReturnZeroWhenRoundIsNull() {
		PlayoffFinancialRules rules = new PlayoffFinancialRules(null);

		assertEquals(0.0, rules.getRoundTicketBonusRate(), 0.0001);
		assertEquals(0.0, rules.getRoundPopularityBonusRate(), 0.0001);
		assertEquals(0.0, rules.getRoundAttendanceBonusRate(), 0.0001);
		assertEquals(0.0, rules.getRoundTvBonusRate(), 0.0001);
		assertEquals(0.0, rules.getRoundQualificationBonus(), 0.0001);
	}

	@Test
	public void shouldIncreaseRoundBonusesThroughPlayoffStages() {
		PlayoffFinancialRules firstRound = new PlayoffFinancialRules(PlayoffRound.FIRST_ROUND);
		PlayoffFinancialRules semis = new PlayoffFinancialRules(PlayoffRound.CONFERENCE_SEMIFINALS);
		PlayoffFinancialRules finals = new PlayoffFinancialRules(PlayoffRound.NBA_FINALS);

		assertTrue(semis.getRoundTicketBonusRate() > firstRound.getRoundTicketBonusRate());
		assertTrue(finals.getRoundTicketBonusRate() > semis.getRoundTicketBonusRate());
		assertTrue(finals.getRoundTvBonusRate() > semis.getRoundTvBonusRate());
		assertTrue(finals.getRoundSecurityBonusRate() > semis.getRoundSecurityBonusRate());
		assertTrue(finals.getRoundQualificationBonus() > semis.getRoundQualificationBonus());
		assertTrue(finals.getLeaguePlayoffRetentionRate() > firstRound.getLeaguePlayoffRetentionRate());
	}

	@Test
	public void shouldExposeStableGameSevenAndEliminationRates() {
		PlayoffFinancialRules rules = new PlayoffFinancialRules(PlayoffRound.CONFERENCE_FINALS);

		assertEquals(0.15, rules.getGameSevenBonusRate(), 0.0001);
		assertEquals(0.10, rules.getEliminationGameBonusRate(), 0.0001);
	}
}
