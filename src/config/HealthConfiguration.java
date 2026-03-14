package config;

public class HealthConfiguration {
	// Health and fatigue
	public static final int FATIGUE_WINDOW = 5;
	public static final String NO_INJURY = "none" ; 
	public static final double INJURY_PROBABILITY = 0.00015; 

	// Injury types
	public static final String MINOR_INJURY = "minor injury" ; 
	public static final int MINOR_INJURY_DURATION = 1 ; 
	public static final String MEDIUM_INJURY = "medium injury" ; 
	public static final int MEDIUM_INJURY_DURATION = 5 ; 
	public static final String SERIOUS_INJURY = "serious injury"; 
	public static final int SERIOUS_INJURY_DURATION = 9 ; 

	private HealthConfiguration() {
	}
}
