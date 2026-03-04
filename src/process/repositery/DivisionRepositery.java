package process.repositery;

<<<<<<< HEAD
import java.util.HashMap;

import data.league.Division;
import data.team.Team;

public class DivisionRepositery {
	private HashMap<String, Division> divisions = new HashMap<String, Division>() ; 
	private static DivisionRepositery instance = new DivisionRepositery() ; 
	
	private DivisionRepositery() {
		
	}
	
	public static DivisionRepositery getInstance() {
		return instance ; 
	}
	
	public void register(String name, Division division) {
		divisions.put(name, division) ; 
	}
	
	public Division getDivision(String name) {
		if(divisions.containsKey(name)) {
			return divisions.get(name) ; 
		}
		
		return null ; 
=======
import java.util.ArrayList;
import java.util.HashMap;

import data.league.Division;

public class DivisionRepositery {
	private HashMap<String, Division> divisions = new HashMap<String, Division>();
	private static DivisionRepositery instance = new DivisionRepositery();

	private DivisionRepositery() {

	}

	public static DivisionRepositery getInstance() {
		return instance;
	}

	public void register(String name, Division division) {
		divisions.put(name, division);
	}

	public Division getDivision(String name) {
		if (divisions.containsKey(name)) {
			return divisions.get(name);
		}

		return null;
	}

	public ArrayList<Division> getAllDivisions() {
		ArrayList<Division> allDivisions = new ArrayList<Division>(divisions.values());
		return allDivisions;
>>>>>>> Fatima2
	}
}
