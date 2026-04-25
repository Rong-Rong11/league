package process.service.finance.playoff;

import data.league.PlayoffRound;

public class PlayoffFinancialRules {
	private PlayoffRound round;

	public PlayoffFinancialRules(PlayoffRound round) {
		this.round = round;
	}

	public double getRoundTicketBonusRate() {
		if (round == null) {
			return 0.0;
		}

		switch (round) {
			case FIRST_ROUND:
				return 0.18;
			case CONFERENCE_SEMIFINALS:
				return 0.32;
			case CONFERENCE_FINALS:
				return 0.48;
			case NBA_FINALS:
				return 0.72;
			default:
				return 0.0;
		}
	}

	public double getRoundPopularityBonusRate() {
		if (round == null) {
			return 0.0;
		}

		switch (round) {
			case FIRST_ROUND:
				return 0.08;
			case CONFERENCE_SEMIFINALS:
				return 0.14;
			case CONFERENCE_FINALS:
				return 0.22;
			case NBA_FINALS:
				return 0.34;
			default:
				return 0.0;
		}
	}

	public double getRoundAttendanceBonusRate() {
		if (round == null) {
			return 0.0;
		}

		switch (round) {
			case FIRST_ROUND:
				return 0.10;
			case CONFERENCE_SEMIFINALS:
				return 0.16;
			case CONFERENCE_FINALS:
				return 0.24;
			case NBA_FINALS:
				return 0.38;
			default:
				return 0.0;
		}
	}

	public double getRoundTvBonusRate() {
		if (round == null) {
			return 0.0;
		}

		switch (round) {
			case FIRST_ROUND:
				return 0.50;
			case CONFERENCE_SEMIFINALS:
				return 0.80;
			case CONFERENCE_FINALS:
				return 1.20;
			case NBA_FINALS:
				return 1.90;
			default:
				return 0.0;
		}
	}

	public double getRoundMerchBonusRate() {
		if (round == null) {
			return 0.0;
		}

		switch (round) {
			case FIRST_ROUND:
				return 0.42;
			case CONFERENCE_SEMIFINALS:
				return 0.66;
			case CONFERENCE_FINALS:
				return 0.96;
			case NBA_FINALS:
				return 1.45;
			default:
				return 0.0;
		}
	}

	public double getRoundConcessionsBonusRate() {
		if (round == null) {
			return 0.0;
		}

		switch (round) {
			case FIRST_ROUND:
				return 0.24;
			case CONFERENCE_SEMIFINALS:
				return 0.38;
			case CONFERENCE_FINALS:
				return 0.54;
			case NBA_FINALS:
				return 0.78;
			default:
				return 0.0;
		}
	}

	public double getRoundParkingBonusRate() {
		if (round == null) {
			return 0.0;
		}

		switch (round) {
			case FIRST_ROUND:
				return 0.08;
			case CONFERENCE_SEMIFINALS:
				return 0.12;
			case CONFERENCE_FINALS:
				return 0.18;
			case NBA_FINALS:
				return 0.24;
			default:
				return 0.0;
		}
	}

	public double getRoundStadiumCostBonusRate() {
		if (round == null) {
			return 0.0;
		}

		switch (round) {
			case FIRST_ROUND:
				return 0.06;
			case CONFERENCE_SEMIFINALS:
				return 0.10;
			case CONFERENCE_FINALS:
				return 0.16;
			case NBA_FINALS:
				return 0.24;
			default:
				return 0.0;
		}
	}

	public double getRoundStaffCostBonusRate() {
		if (round == null) {
			return 0.0;
		}

		switch (round) {
			case FIRST_ROUND:
				return 0.05;
			case CONFERENCE_SEMIFINALS:
				return 0.08;
			case CONFERENCE_FINALS:
				return 0.12;
			case NBA_FINALS:
				return 0.18;
			default:
				return 0.0;
		}
	}

	public double getRoundSecurityBonusRate() {
		if (round == null) {
			return 0.0;
		}

		switch (round) {
			case FIRST_ROUND:
				return 0.12;
			case CONFERENCE_SEMIFINALS:
				return 0.20;
			case CONFERENCE_FINALS:
				return 0.30;
			case NBA_FINALS:
				return 0.45;
			default:
				return 0.0;
		}
	}

	public double getRoundLogisticsBonusRate() {
		if (round == null) {
			return 0.0;
		}

		switch (round) {
			case FIRST_ROUND:
				return 0.06;
			case CONFERENCE_SEMIFINALS:
				return 0.10;
			case CONFERENCE_FINALS:
				return 0.16;
			case NBA_FINALS:
				return 0.24;
			default:
				return 0.0;
		}
	}

	public double getRoundTravelBonusRate() {
		if (round == null) {
			return 0.0;
		}

		switch (round) {
			case FIRST_ROUND:
				return 0.05;
			case CONFERENCE_SEMIFINALS:
				return 0.08;
			case CONFERENCE_FINALS:
				return 0.14;
			case NBA_FINALS:
				return 0.22;
			default:
				return 0.0;
		}
	}

	public double getLeaguePlayoffRetentionRate() {
		if (round == null) {
			return 0.0;
		}

		switch (round) {
			case FIRST_ROUND:
				return 0.06;
			case CONFERENCE_SEMIFINALS:
				return 0.08;
			case CONFERENCE_FINALS:
				return 0.10;
			case NBA_FINALS:
				return 0.12;
			default:
				return 0.0;
		}
	}

	public double getRoundQualificationBonus() {
		if (round == null) {
			return 0.0;
		}

		switch (round) {
			case FIRST_ROUND:
				return 5.0;
			case CONFERENCE_SEMIFINALS:
				return 8.0;
			case CONFERENCE_FINALS:
				return 12.0;
			case NBA_FINALS:
				return 18.0;
			default:
				return 0.0;
		}
	}

	public double getGameSevenBonusRate() {
		return 0.15;
	}

	public double getEliminationGameBonusRate() {
		return 0.10;
	}
}
