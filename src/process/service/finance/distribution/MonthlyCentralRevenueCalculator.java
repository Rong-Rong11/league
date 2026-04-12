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

        double revenue = (0.82 * teamCount)
                + (averagePopularity * 0.100)
                + (averagePerformance * 1.48)
                + (averagePrestige * 2.78)
                + (averageTeamValue * 3.45)
                + (starTeams * 0.27);

        revenue *= profile.getTvRate();
        revenue *= getImportantGamesRevenueRate(month, 0.0015);
        revenue *= getPlayoffGamesRevenueRate(month, 0.0045);
        revenue *= getActivePlayoffTeamsRate(month, 0.0038);
        revenue *= getSeasonMomentumRate(month, 0.06);
        revenue *= getControlledEconomicNoise(month, 0.012);
        revenue *= getRevenueTypeMonthlyRate(month, 0.012, 0.006, 0.0);

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

        double revenue = (0.40 * teamCount)
                + (averagePopularity * 0.080)
                + (averageCommercialAggressiveness * 2.28)
                + (averageBusinessOpportunity * 1.96)
                + (averageTeamValue * 1.98)
                + (starTeams * 0.20);

        revenue *= profile.getSponsoringRate();
        revenue *= getImportantGamesRevenueRate(month, 0.0022);
        revenue *= getPlayoffGamesRevenueRate(month, 0.0043);
        revenue *= getActivePlayoffTeamsRate(month, 0.0036);
        revenue *= getSeasonMomentumRate(month, 0.07);
        revenue *= getControlledEconomicNoise(month, 0.022);
        revenue *= getRevenueTypeMonthlyRate(month, 0.030, 0.016, 0.7);

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

        double revenue = (0.23 * teamCount)
                + (averagePopularity * 0.062)
                + (averageFanLoyalty * 2.05)
                + (averagePrestige * 1.68)
                + (averageTeamValue * 1.24)
                + (starTeams * 0.19);

        revenue *= profile.getMerchandisingRate();
        revenue *= getImportantGamesRevenueRate(month, 0.0032);
        revenue *= getPlayoffGamesRevenueRate(month, 0.0060);
        revenue *= getActivePlayoffTeamsRate(month, 0.0048);
        revenue *= getSeasonMomentumRate(month, 0.10);
        revenue *= getControlledEconomicNoise(month, 0.030);
        revenue *= getRevenueTypeMonthlyRate(month, 0.050, 0.024, 1.4);

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
            return 1.03;
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
        return CalendarUtility.popularityScoreGame(game, date) >= 80 || game.getGameContext().isRivalry();
    }

    private boolean hasHighAttendance(Game game) {
        if (financeManager == null) {
            return false;
        }
        GameStat gameStat = financeManager.getGameStat(game);
        return gameStat != null && gameStat.getAttendanceRate() > 0.85;
    }
}
