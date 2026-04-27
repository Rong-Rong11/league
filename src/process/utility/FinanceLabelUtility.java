package process.utility;

public final class FinanceLabelUtility {

	private FinanceLabelUtility() {
	}

	public static String formatPolicy(Object object) {
		String name = getSimpleClassName(object);
		if ("Thrifty Policy".equals(name)) {
			return "Politique econome";
		}
		if ("Balanced Policy".equals(name)) {
			return "Politique equilibree";
		}
		if ("Ambitious Policy".equals(name)) {
			return "Politique ambitieuse";
		}
		return name;
	}

	public static String formatMarket(Object object) {
		String name = getSimpleClassName(object);
		if ("Small Size".equals(name)) {
			return "Petit marche";
		}
		if ("Medium Size".equals(name)) {
			return "Marche moyen";
		}
		if ("Large Size".equals(name)) {
			return "Grand marche";
		}
		return name;
	}

	public static String formatStrategy(Object object) {
		String name = getSimpleClassName(object);
		if ("Rebuild".equals(name)) {
			return "Reconstruction";
		}
		if ("Balanced".equals(name)) {
			return "Equilibre";
		}
		if ("All In".equals(name)) {
			return "All in";
		}
		if ("Salary Dump".equals(name)) {
			return "Degraissage salarial";
		}
		if ("Small Adjust".equals(name)) {
			return "Petits ajustements";
		}
		if ("Superstar Build".equals(name)) {
			return "Construction superstar";
		}
		return name;
	}

	private static String getSimpleClassName(Object object) {
		if (object == null) {
			return "-";
		}
		return object.getClass().getSimpleName().replaceAll("([a-z])([A-Z])", "$1 $2");
	}
}
