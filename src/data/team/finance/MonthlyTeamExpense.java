package data.team.finance;

public class MonthlyTeamExpense {
	private double monthlyPayroll;
	private double monthlyLuxuryTax;
	private double stadiumMaintenance;
	private double staffCost;
	private double administrativeCost;

	public MonthlyTeamExpense(double monthlyPayroll, double monthlyLuxuryTax, double stadiumMaintenance,
			double staffCost, double administrativeCost) {
		this.monthlyPayroll = monthlyPayroll;
		this.monthlyLuxuryTax = monthlyLuxuryTax;
		this.stadiumMaintenance = stadiumMaintenance;
		this.staffCost = staffCost;
		this.administrativeCost = administrativeCost;
	}

	public double getMonthlyPayroll() {
		return monthlyPayroll;
	}

	public double getMonthlyLuxuryTax() {
		return monthlyLuxuryTax;
	}

	public double getStadiumMaintenance() {
		return stadiumMaintenance;
	}

	public double getStaffCost() {
		return staffCost;
	}

	public double getAdministrativeCost() {
		return administrativeCost;
	}
}
