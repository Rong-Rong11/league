/*
	* Decompiled with CFR 0.152.
	*/
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

	public void register(String string, Division division) {
		this.divisions.put(string, division);
	}

	public Division getDivision(String string) {
		if (this.divisions.containsKey(string)) {
			return this.divisions.get(string);
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
