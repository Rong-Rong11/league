package process.service.finance.game.revenue;

import java.time.LocalDate;

import data.finance.GameStat;
import data.league.League;
import data.sport.setup.Game;
import data.sport.setup.GameMoment;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.mediamarket.MediaMarket;
import process.utility.CalendarUtility;
import process.utility.FinanceUtility;
import process.visitor.gamemoment.GameMomentAttendanceBonusVisitor;

public class GameAttendanceCalculator {

	private League league;
	private GameStat gameStat;
	private GameRevenueBonusProvider bonusProvider;

	public GameAttendanceCalculator(League league, GameStat gameStat, GameRevenueBonusProvider bonusProvider) {
		this.league = league;
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
	}

	public int calculateAttendees(int capacity, double attendanceRate) {
		int attendees = (int) (capacity * attendanceRate);
		gameStat.setAttendees(attendees);
		return attendees;
	}

	public double calculateAttendanceRate(Game game, LocalDate date, Team homeTeam, double popularityRate) {
		MediaMarket mediaMarket = homeTeam.getTeamFinance().getMediaMarket();
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();
		double teamValueFactor = FinanceUtility.getNormalizedTeamValue(homeTeam);

		double importantDayBonus = CalendarUtility.isImportantDay(date)
				|| CalendarUtility.isSpecialEvent(league.getRegularSeason(), date) ? 0.20 : 0.0;
		double gameTimeBonus = getGameTimeAttendanceBonus(game);

		double attendanceRate = (0.32
				+ (popularityRate * 0.58)
				+ importantDayBonus);

		attendanceRate += mediaMarket.getFanBaseModifier() * 0.12;
		attendanceRate += economicProfil.getFanLoyalty() * 0.16;
		attendanceRate += economicProfil.getHistoricalPrestige() * 0.06;
		attendanceRate += teamValueFactor * 0.05;
		attendanceRate += bonusProvider.getAttendanceBonusRate(homeTeam, popularityRate);
		if (homeTeam.hasStarPlayer()) {
			attendanceRate += 0.04;
		}
		attendanceRate += gameTimeBonus;

		double financeBoost = (mediaMarket.getFanBaseModifier() * 0.40)
				+ (economicProfil.getFanLoyalty() * 0.35)
				+ (economicProfil.getHistoricalPrestige() * 0.15)
				+ (teamValueFactor * 0.10);

		if (popularityRate > 0.82) {
			attendanceRate += 0.10 + (financeBoost * 0.05);
		} else if (popularityRate > 0.70) {
			attendanceRate += 0.06 + (financeBoost * 0.03);
		} else if (popularityRate > 0.62) {
			attendanceRate += 0.05;
		} else if (popularityRate < 0.30) {
			attendanceRate -= 0.14 + ((1 - teamValueFactor) * 0.03);
		} else if (popularityRate < 0.35) {
			attendanceRate -= 0.10 + ((1 - mediaMarket.getFanBaseModifier()) * 0.02);
		} else if (popularityRate < 0.45) {
			attendanceRate -= 0.06;
		}

		double randomFactor = 0.88 + (Math.random() * 0.24);

		attendanceRate *= randomFactor;
		attendanceRate = Math.max(0.30, Math.min(1.00, attendanceRate));
		gameStat.setAttendanceRate(attendanceRate);
		return attendanceRate;
	}

	private double getGameTimeAttendanceBonus(Game game) {
		GameMoment gameMoment = game.getGameContext().getGameMoment();

		if (gameMoment == null) {
			return 0.0;
		}

		return gameMoment.accept(new GameMomentAttendanceBonusVisitor());
	}
}
