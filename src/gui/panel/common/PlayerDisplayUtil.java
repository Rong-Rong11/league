package gui.panel.common;

public class PlayerDisplayUtil {

	public static String formatOneDecimal(double value) {
		double roundedValue = Math.round(value * 10.0) / 10.0;
		return String.valueOf(roundedValue);
	}

	public static String formatSalary(double salary) {
		if (salary >= 1) {
			return "$" + formatOneDecimal(salary) + "M";
		}
		return "$" + Math.round(salary * 1000.0) + "K";
	}
}
