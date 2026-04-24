package process.service.finance.distribution;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import data.calendar.GameDay;
import data.finance.GameStat;
import data.league.League;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.mediamarket.MediaMarket;
import process.repository.TeamRepository;
import process.service.finance.FinanceManager;
import process.utility.CalendarUtility;
import process.utility.FinanceUtility;

public class MonthlyCentralRevenueCalculator {

	private final League league;
	private final TeamRepository teamRepository = TeamRepository.getInstance();
	private FinanceManager financeManager;

	public MonthlyCentralRevenueCalculator(League league) {
		this.league = league;
	}

	public void setFinanceManager(FinanceManager financeManager) {
		this.financeManager = financeManager;
	}

	public double calculateNationalTvRevenue(CentralRevenueProfile profile, int month) {
		ArrayList<Team> teams = teamRepository.getAllTeams();
		int teamCount = teams.size();

		double averagePopularity = calculateAveragePopularity(teams);
		double averagePerformance = calculateAveragePerformance(teams);
		double averagePrestige = calculateAverageHistoricalPrestige(teams);
		double averageTeamValue = calculateAverageTeamValue(teams);
		int starTeams = countTeamsWithStarPlayer(teams);

		double revenue = (0.58 * teamCount)
				+ (averagePopularity * 0.080)
				+ (averagePerformance * 1.32)
				+ (averagePrestige * 1.90)
				+ (averageTeamValue * 2.30)
				+ (starTeams * 0.17);

		revenue *= profile.getTvRate();
		revenue *= getLeagueMonthlyAttractivenessRate(month);
		revenue *= getImportantGamesRevenueRate(month, 0.0040);
		revenue *= getPlayoffGamesRevenueRate(month, 0.0045);
		revenue *= getActivePlayoffTeamsRate(month, 0.0038);
		revenue *= getSeasonMomentumRate(month, 0.10);
		revenue *= getControlledEconomicNoise(month, 0.165);
		revenue *= getRevenueTypeMonthlyRate(month, 0.018, 0.010, 0.0);
		revenue += getLeagueMonthlyAdditiveBonus(month) * 0.33;

		return revenue;
	}

	public double calculateNationalSponsoringRevenue(CentralRevenueProfile profile, int month) {
		ArrayList<Team> teams = teamRepository.getAllTeams();
		int teamCount = teams.size();

		double averagePopularity = calculateAveragePopularity(teams);
		double averageCommercialAggressiveness = calculateAverageCommercialAggressiveness(teams);
		double averageBusinessOpportunity = calculateAverageBusinessOpportunity(teams);
		double averageTeamValue = calculateAverageTeamValue(teams);
		int starTeams = countTeamsWithStarPlayer(teams);

		double revenue = (0.27 * teamCount)
				+ (averagePopularity * 0.062)
				+ (averageCommercialAggressiveness * 1.54)
				+ (averageBusinessOpportunity * 1.34)
				+ (averageTeamValue * 1.32)
				+ (starTeams * 0.12);

		revenue *= profile.getSponsoringRate();
		revenue *= getLeagueMonthlyAttractivenessRate(month);
		revenue *= getImportantGamesRevenueRate(month, 0.0055);
		revenue *= getPlayoffGamesRevenueRate(month, 0.0043);
		revenue *= getActivePlayoffTeamsRate(month, 0.0036);
		revenue *= getSeasonMomentumRate(month, 0.12);
		revenue *= getControlledEconomicNoise(month, 0.220);
		revenue *= getRevenueTypeMonthlyRate(month, 0.040, 0.022, 0.7);
		revenue += getLeagueMonthlyAdditiveBonus(month) * 0.29;

		return revenue;
	}

	public double calculateNationalMerchandisingRevenue(CentralRevenueProfile profile, int month) {
		ArrayList<Team> teams = teamRepository.getAllTeams();
		int teamCount = teams.size();

		double averagePopularity = calculateAveragePopularity(teams);
		double averageFanLoyalty = calculateAverageFanLoyalty(teams);
		double averagePrestige = calculateAverageHistoricalPrestige(teams);
		double averageTeamValue = calculateAverageTeamValue(teams);
		int starTeams = countTeamsWithStarPlayer(teams);

		double revenue = (0.15 * teamCount)
				+ (averagePopularity * 0.049)
				+ (averageFanLoyalty * 1.42)
				+ (averagePrestige * 1.10)
				+ (averageTeamValue * 0.80)
				+ (starTeams * 0.11);

		revenue *= profile.getMerchandisingRate();
		revenue *= getLeagueMonthlyAttractivenessRate(month);
		revenue *= getImportantGamesRevenueRate(month, 0.0075);
		revenue *= getPlayoffGamesRevenueRate(month, 0.0060);
		revenue *= getActivePlayoffTeamsRate(month, 0.0048);
		revenue *= getSeasonMomentumRate(month, 0.16);
		revenue *= getControlledEconomicNoise(month, 0.285);
		revenue *= getRevenueTypeMonthlyRate(month, 0.065, 0.032, 1.4);
		revenue += getLeagueMonthlyAdditiveBonus(month) * 0.21;

		return revenue;
	}

	private double calculateAveragePopularity(List<Team> teams) {
		double total = 0.0;
		for (Team team : teams) {
			total += team.getCurrentPopularity();
		}
		return total / teams.size();
	}

	private double calculateAveragePerformance(List<Team> teams) {
		double total = 0.0;
		for (Team team : teams) {
			total += team.getTeamPerformance().getPerformanceRating();
		}
		return total / teams.size();
	}

	private double calculateAverageHistoricalPrestige(List<Team> teams) {
		double total = 0.0;
		for (Team team : teams) {
			EconomicProfil profil = team.getTeamFinance().getEconomicProfil();
			total += profil.getHistoricalPrestige();
		}
		return total / teams.size();
	}

	private double calculateAverageFanLoyalty(List<Team> teams) {
		double total = 0.0;
		for (Team team : teams) {
			EconomicProfil profil = team.getTeamFinance().getEconomicProfil();
			total += profil.getFanLoyalty();
		}
		return total / teams.size();
	}

	private double calculateAverageCommercialAggressiveness(List<Team> teams) {
		double total = 0.0;
		for (Team team : teams) {
			EconomicProfil profil = team.getTeamFinance().getEconomicProfil();
			total += profil.getCommercialAggressiveness();
		}
		return total / teams.size();
	}

	private double calculateAverageBusinessOpportunity(List<Team> teams) {
		double total = 0.0;
		for (Team team : teams) {
			MediaMarket mediaMarket = team.getTeamFinance().getMediaMarket();
			total += mediaMarket.getBusinessOpportunityModifier();
		}
		return total / teams.size();
	}

	private double calculateAverageTeamValue(List<Team> teams) {
		double total = 0.0;
		for (Team team : teams) {
			total += FinanceUtility.getNormalizedTeamValue(team);
		}
		return total / teams.size();
	}

	private int countTeamsWithStarPlayer(List<Team> teams) {
		int count = 0;
		for (Team team : teams) {
			if (team.getStarPlayer() != null) {
				count++;
			}
		}
		return count;
	}

	private double calculateMonthlyLeagueAttractiveness(int month) {
		double totalScore = 0.0;
		int gameCount = 0;

		for (GameDay gameDay : getAllGameDaysForMonth(month)) {
			LocalDate date = gameDay.getDate();
			for (Game game : gameDay.getGames()) {
				totalScore += CalendarUtility.popularityScoreGame(game, date);
				gameCount++;
			}
		}

		if (gameCount == 0) {
			return 0.0;
		}

		return totalScore / gameCount;
	}

	private double getLeagueMonthlyAttractivenessRate(int month) {
		double attractiveness = calculateMonthlyLeagueAttractiveness(month);

		if (attractiveness < 60) {
			return 0.60;
		}
		if (attractiveness < 74) {
			return 0.78;
		}
		if (attractiveness < 90) {
			return 1.00;
		}
		if (attractiveness < 108) {
			return 1.24;
		}
		return 1.52;
	}

	private double getPlayoffMonthlyBonus(int month) {
		int playoffGames = countPlayoffGamesInMonth(month);
		return playoffGames * 0.16;
	}

	private double getLeagueMonthlyAdditiveBonus(int month) {
		double totalAttractiveness = 0.0;
		double totalAttendance = 0.0;
		int gameCount = 0;
		int importantGames = 0;
		int premiumGames = 0;
		int highAttendanceGames = 0;
		int rivalryGames = 0;
		int starGames = 0;
		int starRivalryGames = 0;

		for (GameDay gameDay : getAllGameDaysForMonth(month)) {
			LocalDate date = gameDay.getDate();
			for (Game game : gameDay.getGames()) {
				double score = CalendarUtility.popularityScoreGame(game, date);
				totalAttractiveness += score;
				gameCount++;

				if (isImportantGame(game, date)) {
					importantGames++;
				}
				if (score >= 110) {
					premiumGames++;
				}

				boolean rivalry = game.getGameContext().isRivalry();
				boolean starGame = game.getGameContext().getHomeTeam().hasStarPlayer()
						|| game.getGameContext().getAwayTeam().hasStarPlayer();

				if (rivalry) {
					rivalryGames++;
				}
				if (starGame) {
					starGames++;
				}
				if (rivalry && starGame) {
					starRivalryGames++;
				}

				if (financeManager != null) {
					GameStat gameStat = financeManager.getGameStat(game);
					if (gameStat != null) {
						totalAttendance += gameStat.getAttendanceRate();
						if (gameStat.getAttendanceRate() >= 0.92) {
							highAttendanceGames++;
						}
					}
				}
			}
		}

		double averageAttractiveness = gameCount == 0 ? 0.0 : totalAttractiveness / gameCount;
		double averageAttendance = gameCount == 0 ? 0.0 : totalAttendance / gameCount;

		double bonus = 0.0;
		bonus += getAttractivenessBonus(averageAttractiveness);
		bonus += getAttendanceBonus(averageAttendance);
		bonus += getVolumeBonus(importantGames, premiumGames, highAttendanceGames);
		bonus += getStarRivalryBonus(rivalryGames, starGames, starRivalryGames);
		bonus += getPlayoffMonthlyBonus(month);

		return bonus;
	}

	private double getAttractivenessBonus(double averageAttractiveness) {
		if (averageAttractiveness < 60) {
			return -2.4;
		}
		if (averageAttractiveness < 74) {
			return -1.1;
		}
		if (averageAttractiveness >= 108) {
			return 3.0;
		}
		if (averageAttractiveness >= 90) {
			return 1.5;
		}
		return 0.0;
	}

	private double getAttendanceBonus(double averageAttendance) {
		if (averageAttendance < 0.72) {
			return -1.2;
		}
		if (averageAttendance >= 0.90) {
			return 1.9;
		}
		if (averageAttendance >= 0.82) {
			return 1.0;
		}
		return 0.0;
	}

	private double getVolumeBonus(int importantGames, int premiumGames, int highAttendanceGames) {
		return (importantGames * 0.10)
				+ (premiumGames * 0.16)
				+ (highAttendanceGames * 0.12);
	}

	private double getStarRivalryBonus(int rivalryGames, int starGames, int starRivalryGames) {
		return (rivalryGames * 0.04)
				+ (starGames * 0.025)
				+ (starRivalryGames * 0.09);
	}

	private double getImportantGamesRevenueRate(int month, double ratePerGame) {
		int importantGames = countImportantGamesInMonth(month);
		return 1 + (importantGames * ratePerGame);
	}

	private double getPlayoffGamesRevenueRate(int month, double ratePerGame) {
		int playoffGames = countPlayoffGamesInMonth(month);
		return 1 + (playoffGames * ratePerGame);
	}

	private double getActivePlayoffTeamsRate(int month, double ratePerTeam) {
		if (!isPlayoffMonth(month)) {
			return 1.0;
		}
		return 1 + (countActivePlayoffTeams() * ratePerTeam);
	}

	private double getSeasonMomentumRate(int month, double playoffBonusRate) {
		if (isPlayoffMonth(month)) {
			return 1 + playoffBonusRate;
		}
		if (CalendarUtility.isImportantMonth(month)) {
			return 1.28;
		}
		return 1.0;
	}

	private double getControlledEconomicNoise(int month, double maxAmplitude) {
		int importantGames = countImportantGamesInMonth(month);
		int playoffGames = countPlayoffGamesInMonth(month);
		int activeTeams = countActivePlayoffTeams();
		double wave = Math.sin((month * 1.73) + (importantGames * 0.11) + (playoffGames * 0.23) + (activeTeams * 0.19));
		return 1 + (wave * maxAmplitude);
	}

	private double getRevenueTypeMonthlyRate(int month, double primaryAmplitude, double secondaryAmplitude,
			double phaseShift) {
		double primaryWave = Math.sin((month * 1.11) + phaseShift);
		double secondaryWave = Math.cos((month * 0.67) + (phaseShift * 0.6));

		return 1 + (primaryWave * primaryAmplitude) + (secondaryWave * secondaryAmplitude);
	}

	private int countImportantGamesInMonth(int month) {
		int count = 0;
		count += countImportantGamesForSeasonMonth(month, false);
		count += countImportantGamesForSeasonMonth(month, true);
		return count;
	}

	private int countPlayoffGamesInMonth(int month) {
		int count = 0;

		for (GameDay gameDay : league.getPlayoff().getNbaCalendar().getCalendar().values()) {
			LocalDate date = gameDay.getDate();
			if (date == null || !matchesFinanceMonth(date, month)) {
				continue;
			}
			count += gameDay.getGames().size();
		}
		return count;
	}

	private List<GameDay> getAllGameDaysForMonth(int month) {
		List<GameDay> gameDays = new ArrayList<>();

		if (league.getRegularSeason() != null && league.getRegularSeason().getNbaCalendar() != null) {
			for (GameDay gameDay : league.getRegularSeason().getNbaCalendar().getCalendar().values()) {
				if (gameDay.getDate() != null && matchesFinanceMonth(gameDay.getDate(), month)) {
					gameDays.add(gameDay);
				}
			}
		}

		if (league.getPlayoff() != null && league.getPlayoff().getNbaCalendar() != null) {
			for (GameDay gameDay : league.getPlayoff().getNbaCalendar().getCalendar().values()) {
				if (gameDay.getDate() != null && matchesFinanceMonth(gameDay.getDate(), month)) {
					gameDays.add(gameDay);
				}
			}
		}

		return gameDays;
	}

	private int countImportantGamesForSeasonMonth(int month, boolean playoff) {
		int count = 0;
		if (playoff) {
			for (GameDay gameDay : league.getPlayoff().getNbaCalendar().getCalendar().values()) {
				LocalDate date = gameDay.getDate();
				if (date == null || !matchesFinanceMonth(date, month)) {
					continue;
				}
				for (Game game : gameDay.getGames()) {
					if (isImportantGame(game, date)) {
						count++;
						if (hasHighAttendance(game)) {
							count++;
						}
					}
				}
			}
			return count;
		}
		for (GameDay gameDay : league.getRegularSeason().getNbaCalendar().getCalendar().values()) {
			LocalDate date = gameDay.getDate();
			if (date == null || !matchesFinanceMonth(date, month)) {
				continue;
			}
			for (Game game : gameDay.getGames()) {
				if (isImportantGame(game, date)) {
					count++;
					if (hasHighAttendance(game)) {
						count++;
					}
				}
			}
		}
		return count;
	}

	private int countActivePlayoffTeams() {
		if (league.getPlayoff() == null || league.getPlayoff().getCurrentRound() == null) {
			return 0;
		}

		ArrayList<Team> activeTeams = new ArrayList<Team>();
		for (data.sport.setup.PlayoffSeries series : CalendarUtility.getCurrentRoundSeries(league.getPlayoff())) {
			if (series == null || series.isFinished()) {
				continue;
			}
			activeTeams.add(series.getHigherTeam());
			activeTeams.add(series.getLowerTeam());
		}
		return activeTeams.size();
	}

	private boolean isPlayoffMonth(int month) {
		return month >= 8;
	}

	private boolean matchesFinanceMonth(LocalDate date, int month) {
		int startMonth = league.getRegularSeason().getDebutDate().getMonthValue();
		int monthDelta = date.getMonthValue() - startMonth;
		if (monthDelta < 0) {
			monthDelta += 12;
		}
		return (monthDelta + 1) == month;
	}

	private boolean isImportantGame(Game game, LocalDate date) {
		return CalendarUtility.popularityScoreGame(game, date) >= 72 || game.getGameContext().isRivalry();
	}

	private boolean hasHighAttendance(Game game) {
		if (financeManager == null) {
			return false;
		}
		GameStat gameStat = financeManager.getGameStat(game);
		return gameStat != null && gameStat.getAttendanceRate() > 0.85;
	}
}
