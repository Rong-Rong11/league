package process.builder.league;

import data.league.League;
import data.league.finance.CentralRevenueSeasonDynamics;
import data.team.Team;

public class CentralRevenueSeasonDynamicsFactory {

	public CentralRevenueSeasonDynamics create(League league) {
		double mediaMomentum = randomRate(0.62, 1.38);
		double marketCycle = randomRate(0.60, 1.40);
		double tvRate = clamp((mediaMomentum * 0.70) + (marketCycle * 0.30) + randomOffset(0.18), 0.52, 1.55);
		double sponsoringRate = clamp((marketCycle * 0.74) + (mediaMomentum * 0.26) + randomOffset(0.18), 0.52,
				1.55);
		double merchandisingRate = clamp(((mediaMomentum + marketCycle) / 2.0) + randomOffset(0.20), 0.52, 1.55);
		double economicNoisePhaseShift = randomRate(-3.10, 3.10);
		double revenueTypePhaseShift = randomRate(-2.40, 2.40);
		double importantMonthRate = randomRate(1.04, 1.12);
		double baselineAveragePopularity = calculateBaselineAveragePopularity(league);
		double leagueExpensePressure = randomRate(0.92, 1.22);
		double leagueExpenseNoisePhaseShift = randomRate(-2.80, 2.80);

		return new CentralRevenueSeasonDynamics(mediaMomentum,
				marketCycle,
				tvRate,
				sponsoringRate,
				merchandisingRate,
				economicNoisePhaseShift,
				revenueTypePhaseShift,
				importantMonthRate,
				baselineAveragePopularity,
				leagueExpensePressure,
				leagueExpenseNoisePhaseShift);
	}

	private double calculateBaselineAveragePopularity(League league) {
		if (league == null || league.getAllTeam() == null || league.getAllTeam().isEmpty()) {
			return 50.0;
		}
		double total = 0.0;
		for (Team team : league.getAllTeam()) {
			total += team.getCurrentPopularity();
		}
		return total / league.getAllTeam().size();
	}

	private double randomRate(double min, double max) {
		return min + (Math.random() * (max - min));
	}

	private double randomOffset(double amplitude) {
		return (Math.random() * (amplitude * 2.0)) - amplitude;
	}

	private double clamp(double value, double min, double max) {
		if (value < min) {
			return min;
		}
		if (value > max) {
			return max;
		}
		return value;
	}
}
