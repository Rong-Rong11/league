package process.repository;

import java.util.ArrayList;
import java.util.HashMap;

import data.league.Division;

public class DivisionRepository {
	private HashMap<String, Division> divisions = new HashMap<>();
	private static DivisionRepository instance = new DivisionRepository();

	private DivisionRepository() {
	}

	public static DivisionRepository getInstance() {
		return instance;
	}

	public void register(String divisionName, Division division) {
		this.divisions.put(divisionName, division);
	}

	public Division getDivision(String divisionName) {
		if (this.divisions.containsKey(divisionName)) {
			return this.divisions.get(divisionName);
		}
		return null;
	}

	public ArrayList<Division> getAllDivisions() {
		ArrayList<Division> arrayList = new ArrayList<Division>(this.divisions.values());
		return arrayList;
	}

	public void clear() {
		this.divisions.clear();
	}
}
