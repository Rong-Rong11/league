package data.league.finance;

public class CentralRevenueSeasonDynamics {
	private final double leagueSeasonMediaMomentum;
	private final double centralMarketCycle;
	private final double tvSeasonRate;
	private final double sponsoringSeasonRate;
	private final double merchandisingSeasonRate;
	private final double economicNoisePhaseShift;
	private final double revenueTypePhaseShift;
	private final double importantMonthRate;
	private final double baselineAveragePopularity;
	private final double leagueExpensePressure;
	private final double leagueExpenseNoisePhaseShift;

	public CentralRevenueSeasonDynamics(double leagueSeasonMediaMomentum, double centralMarketCycle,
			double tvSeasonRate, double sponsoringSeasonRate, double merchandisingSeasonRate,
			double economicNoisePhaseShift, double revenueTypePhaseShift, double importantMonthRate,
			double baselineAveragePopularity, double leagueExpensePressure, double leagueExpenseNoisePhaseShift) {
		this.leagueSeasonMediaMomentum = leagueSeasonMediaMomentum;
		this.centralMarketCycle = centralMarketCycle;
		this.tvSeasonRate = tvSeasonRate;
		this.sponsoringSeasonRate = sponsoringSeasonRate;
		this.merchandisingSeasonRate = merchandisingSeasonRate;
		this.economicNoisePhaseShift = economicNoisePhaseShift;
		this.revenueTypePhaseShift = revenueTypePhaseShift;
		this.importantMonthRate = importantMonthRate;
		this.baselineAveragePopularity = baselineAveragePopularity;
		this.leagueExpensePressure = leagueExpensePressure;
		this.leagueExpenseNoisePhaseShift = leagueExpenseNoisePhaseShift;
	}

	public double getLeagueSeasonMediaMomentum() {
		return leagueSeasonMediaMomentum;
	}

	public double getCentralMarketCycle() {
		return centralMarketCycle;
	}

	public double getTvSeasonRate() {
		return tvSeasonRate;
	}

	public double getSponsoringSeasonRate() {
		return sponsoringSeasonRate;
	}

	public double getMerchandisingSeasonRate() {
		return merchandisingSeasonRate;
	}

	public double getEconomicNoisePhaseShift() {
		return economicNoisePhaseShift;
	}

	public double getRevenueTypePhaseShift() {
		return revenueTypePhaseShift;
	}

	public double getImportantMonthRate() {
		return importantMonthRate;
	}

	public double getBaselineAveragePopularity() {
		return baselineAveragePopularity;
	}

	public double getLeagueExpensePressure() {
		return leagueExpensePressure;
	}

	public double getLeagueExpenseNoisePhaseShift() {
		return leagueExpenseNoisePhaseShift;
	}
}
