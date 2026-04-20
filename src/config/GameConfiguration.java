/*
	* Decompiled with CFR 0.152.
	*/
package config;

public class GameConfiguration {
	public static final int GAME_INTER_CONFERENCE = 0;
	public static final int GAME_INTRA_CONFERENCE = 1;
	public static final int GAME_INTRA_DIVISION = 2;
	public static final int QUARTER_DURATION = 720;
	public static final String TEAM_OFFENSIVE_MATCH_PROFIL = "offensive";
	public static final String TEAM_DEFENSIVE_MATCH_PROFIL = "defensive";
	public static final String TEAM_BALANCED_MATCH_PROFIL = "balanced";
	public static final String PLAYER_POSITION_POINT_GUARD = "PG";
	public static final String PLAYER_POSITION_SHOOTING_GUARD = "SG";
	public static final String PLAYER_POSITION_SMALL_FORWARD = "SF";
	public static final String PLAYER_POSITION_POWER_FORWARD = "PF";
	public static final String PLAYER_POSITION_CENTER = "C";
	public static final int AVERAGE_POINTS_PER_MATCH = 10;
	public static final int AVERAGE_REBOUND_PER_MATCH = 5;
	public static final int AVERAGE_INTERCEPTION_PER_MATCH = 1;
	public static final int AVERAGE_ASSIST_PER_MATCH = 3;
	public static final int AVERAGE_BLOCK_PER_MATCH = 1;
	public static final int AVERAGE_LOST_BALL_PER_MATCH = 2;
	public static final int MAX_ASSIST_PER_MATCH = 10;
	public static final int MAX_REBOUND_PER_MATCH = 20;
	public static final int MAX_BLOCK_PER_MATCH = 5;
	public static final int MAX_TURNOVER_PER_MATCH = 5;
	public static final double FOULDRAW_PROBABILITY = 0.18;
	public static final double THREEPOINT_PROBABILITY = 0.38;
	public static final double TWOPOINT_PROBABILITY = 0.38;
	public static final double ASSIST_PROBABILITY = 0.6;
	public static final double BLOCK_PROBABILTY = 0.08;
	public static final double OFFENSIVE_REBOUND_PROBABILITY = 0.15;
	public static final double DEFENSIVE_REBOUND_PROBABILITY = 0.85;
	public static final double THREEPOINT_PROBABILITY_SUCCESS = 0.39;
	public static final double TWO_PROBABILITY_SUCCESS = 0.57;
	public static final double FOULDRAW_PROBABILITY_SUCESS = 0.82;
	public static final String THREEPOINT = "threepoint";
	public static final String TWOPOINT = "twopoint";
	public static final String FOULDRAW = "fouldraw";
	public static final String TURNOVER_ACTION = "turnover";
	public static final String SCORED_ACTION = "scored";
	public static final String END_OF_TIME_ACTION = "end of time";
	public static final String OFFENSIVE_REBOUND_ACTION = "offensive rebound";
	public static final String DEFENSIVE_REBOUND_ACTION = "defensive rebound ";
	public static final String BLOCK_ACTION = "block action ";

	private GameConfiguration() {
	}
}
