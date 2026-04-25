package process.service.finance.game.revenue;

import java.time.LocalDate;

import org.apache.log4j.Logger;

import data.finance.GameStat;
import data.league.League;
import data.sport.setup.Game;
import data.sport.setup.GameMoment;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.mediamarket.MediaMarket;
import log.LoggerUtility;
import process.utility.CalendarUtility;
import process.utility.FinanceUtility;
import process.visitor.gamemoment.GameMomentAttendanceBonusVisitor;

public class GameAttendanceCalculator {
	private static final Logger logger = LoggerUtility.getLogger(GameAttendanceCalculator.class, "text");

	private League league;
	private GameStat gameStat;
	private GameRevenueBonusProvider bonusProvider;

	public GameAttendanceCalculator(League league, GameStat gameStat, GameRevenueBonusProvider bonusProvider) {
		this.league = league;
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
		logger.debug("Game attendance calculator initialized");
	}

	public int calculateAttendees(int capacity, double attendanceRate) {
		int attendees = (int) (capacity * attendanceRate);
		gameStat.setAttendees(attendees);
		logger.debug("Calculated attendees "
				+ attendees
				+ " from capacity "
				+ capacity
				+ " and attendance rate "
				+ attendanceRate);
		return attendees;
	}

	public double calculateAttendanceRate(Game game, LocalDate date, Team homeTeam, double popularityRate) {
		if (game == null) {
			logger.warn("Skipping attendance rate calculation because game is null");
			return 0.0;
		}
		if (homeTeam == null) {
			logger.warn("Skipping attendance rate calculation because home team is null");
			return 0.0;
		}
		if (gameStat == null) {
			logger.warn("Skipping attendance rate calculation because game stat is null");
			return 0.0;
		}
		logger.debug("Calculating attendance rate for "
				+ homeTeam.getName()
				+ " on "
				+ date
				+ " with popularity rate "
				+ popularityRate);
		MediaMarket mediaMarket = homeTeam.getTeamFinance().getStructure().getMediaMarket();
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getStructure().getEconomicProfil();
		double teamValueFactor = FinanceUtility.getNormalizedTeamValue(homeTeam);

		double importantDayBonus = CalendarUtility.isImportantDay(date)
				|| CalendarUtility.isSpecialEvent(league.getRegularSeason(), date) ? 0.20 : 0.0;
		double gameTimeBonus = getGameTimeAttendanceBonus(game);
		logger.trace("Attendance bonuses: importantDay="
				+ importantDayBonus
				+ ", gameTime="
				+ gameTimeBonus
				+ ", teamValueFactor="
				+ teamValueFactor);

		double attendanceRate = (0.32
				+ (popularityRate * 0.58)
				+ importantDayBonus);

		attendanceRate += mediaMarket.getFanBaseModifier() * 0.12;
		attendanceRate += economicProfil.getFanLoyalty() * 0.16;
		attendanceRate += economicProfil.getHistoricalPrestige() * 0.06;
		attendanceRate += teamValueFactor * 0.05;
		double providerBonus = bonusProvider.getAttendanceBonusRate(homeTeam, popularityRate);
		attendanceRate += providerBonus;
		logger.trace("Attendance rate after market, economic profile and provider bonus "
				+ providerBonus
				+ " is "
				+ attendanceRate);
		if (homeTeam.hasStarPlayer()) {
			logger.trace("Applying star player attendance bonus for " + homeTeam.getName());
			attendanceRate += 0.04;
		}
		attendanceRate += gameTimeBonus;

		double financeBoost = (mediaMarket.getFanBaseModifier() * 0.40)
				+ (economicProfil.getFanLoyalty() * 0.35)
				+ (economicProfil.getHistoricalPrestige() * 0.15)
				+ (teamValueFactor * 0.10);

		if (popularityRate > 0.82) {
			logger.trace("Applying elite popularity attendance boost");
			attendanceRate += 0.10 + (financeBoost * 0.05);
		} else if (popularityRate > 0.70) {
			logger.trace("Applying high popularity attendance boost");
			attendanceRate += 0.06 + (financeBoost * 0.03);
		} else if (popularityRate > 0.62) {
			logger.trace("Applying medium popularity attendance boost");
			attendanceRate += 0.05;
		} else if (popularityRate < 0.30) {
			logger.trace("Applying very low popularity attendance penalty");
			attendanceRate -= 0.14 + ((1 - teamValueFactor) * 0.03);
		} else if (popularityRate < 0.35) {
			logger.trace("Applying low popularity attendance penalty");
			attendanceRate -= 0.10 + ((1 - mediaMarket.getFanBaseModifier()) * 0.02);
		} else if (popularityRate < 0.45) {
			logger.trace("Applying moderate low popularity attendance penalty");
			attendanceRate -= 0.06;
		}

		double randomFactor = 0.88 + (Math.random() * 0.24);
		logger.trace("Applying attendance random factor " + randomFactor);

		attendanceRate *= randomFactor;
		attendanceRate = Math.max(0.30, Math.min(1.00, attendanceRate));
		gameStat.setAttendanceRate(attendanceRate);
		logger.debug("Calculated attendance rate " + attendanceRate + " for " + homeTeam.getName());
		return attendanceRate;
	}

	private double getGameTimeAttendanceBonus(Game game) {
		GameMoment gameMoment = game.getGameContext().getGameMoment();

		if (gameMoment == null) {
			logger.trace("Game time attendance bonus is 0.0 because game moment is null");
			return 0.0;
		}

		double bonus = gameMoment.accept(new GameMomentAttendanceBonusVisitor());
		logger.trace("Game time attendance bonus is " + bonus + " for " + gameMoment.getClass().getSimpleName());
		return bonus;
	}
}
