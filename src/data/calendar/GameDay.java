package data.calendar;

import java.time.LocalDate;
import java.util.ArrayList;

import data.sport.setup.Game;

public class GameDay {
	private ArrayList<Game> games = new ArrayList<>();
	private LocalDate date;
	private boolean isSimulated;
	private boolean isDisplayed;

	public GameDay(LocalDate date) {
		this.date = date;
		this.isSimulated = false;
		this.isDisplayed = false;
	}
	
	public LocalDate getDate() {
		return this.date;
	}

	public boolean isEmpty() {
		return this.games.size() == 0;
	}

	public ArrayList<Game> getGames() {
		return this.games;
	}

	public void setGames(ArrayList<Game> games) {
		this.games = games;
	}

	public boolean isSimulated() {
		return this.isSimulated;
	}

	public void setSimulated(boolean simulated) {
		this.isSimulated = simulated;
	}

	public boolean isDisplayed() {
		return this.isDisplayed;
	}

	public void setDisplayed(boolean displayed) {
		this.isDisplayed = displayed;
	}

	public void addGame(Game game) {
		games.add(game);
	}
}
