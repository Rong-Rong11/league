package gui.panel.common;

import java.util.HashMap;

public class TeamDisplayUtil {

	private static final HashMap<String, String> TEAM_ABBREVIATIONS = createAbbreviations();

	public static String getAbbreviation(String teamName) {
		if (teamName == null || teamName.isEmpty()) {
			return "---";
		}

		String abbreviation = TEAM_ABBREVIATIONS.get(teamName);
		if (abbreviation != null) {
			return abbreviation;
		}

		return "---";
	}

	public static String getShortName(String teamName) {
		if (teamName == null || teamName.isEmpty()) {
			return "-";
		}

		String[] words = teamName.split(" ");
		if (words.length == 0) {
			return teamName;
		}
		return words[words.length - 1];
	}

	private static HashMap<String, String> createAbbreviations() {
		HashMap<String, String> abbreviations = new HashMap<String, String>();

		abbreviations.put("Atlanta Hawks", "ATL");
		abbreviations.put("Boston Celtics", "BOS");
		abbreviations.put("Brooklyn Nets", "BKN");
		abbreviations.put("Charlotte Hornets", "CHA");
		abbreviations.put("Chicago Bulls", "CHI");
		abbreviations.put("Cleveland Cavaliers", "CLE");
		abbreviations.put("Dallas Mavericks", "DAL");
		abbreviations.put("Denver Nuggets", "DEN");
		abbreviations.put("Detroit Pistons", "DET");
		abbreviations.put("Golden State Warriors", "GSW");
		abbreviations.put("Houston Rockets", "HOU");
		abbreviations.put("Indiana Pacers", "IND");
		abbreviations.put("Los Angeles Clippers", "LAC");
		abbreviations.put("Los Angeles Lakers", "LAL");
		abbreviations.put("Memphis Grizzlies", "MEM");
		abbreviations.put("Miami Heat", "MIA");
		abbreviations.put("Milwaukee Bucks", "MIL");
		abbreviations.put("Minnesota Timberwolves", "MIN");
		abbreviations.put("New Orleans Pelicans", "NOP");
		abbreviations.put("New York Knicks", "NYK");
		abbreviations.put("Oklahoma City Thunder", "OKC");
		abbreviations.put("Orlando Magic", "ORL");
		abbreviations.put("Philadelphia 76ers", "PHI");
		abbreviations.put("Phoenix Suns", "PHX");
		abbreviations.put("Portland Trail Blazers", "POR");
		abbreviations.put("Sacramento Kings", "SAC");
		abbreviations.put("San Antonio Spurs", "SAS");
		abbreviations.put("Toronto Raptors", "TOR");
		abbreviations.put("Utah Jazz", "UTA");
		abbreviations.put("Washington Wizards", "WAS");

		return abbreviations;
	}
}
