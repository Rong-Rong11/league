package data.sport.setup;

import data.team.Team;
import process.utility.CalendarUtilitary;

public class GameContext {
    private Team homeTeam;
    private Team awayTeam;
    private int typeGame;
    private GameMoment gameMoment;
    private boolean isScheduled;
    private boolean isRivalry;

    public GameContext(Team homeTeam, Team awayTeam, int typeGame) {
        this(homeTeam, awayTeam, typeGame, new Evening());
    }

    public GameContext(Team homeTeam, Team awayTeam, int typeGame, GameMoment gameMoment) {
        this.setAwayTeam(awayTeam);
        this.setHomeTeam(homeTeam);
        this.setTypeGame(typeGame);
        this.setGameMoment(gameMoment);
        this.isRivalry = CalendarUtilitary.isRivalry(this);
    }

    public boolean isScheduled() {
        return this.isScheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.isScheduled = scheduled;
    }

    public Team getHomeTeam() {
        return this.homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return this.awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getTypeGame() {
        return this.typeGame;
    }

    public void setTypeGame(int typeGame) {
        this.typeGame = typeGame;
    }

    public GameMoment getGameMoment() {
        return this.gameMoment;
    }

    public void setGameMoment(GameMoment gameMoment) {
        this.gameMoment = gameMoment;
    }

    public boolean isRivalry() {
        return this.isRivalry;
    }
}
