package data.calendar;

import java.time.LocalDate;
import java.util.ArrayList;

import data.sport.setup.Game;

public class GameDay {
	private ArrayList<Game> games;
	private LocalDate date;
	private boolean isSimulated;

	public GameDay(LocalDate date) {
		games = new ArrayList<Game>();
		this.date = date;
		isSimulated = false;
	}

	public LocalDate getDate() {
		return date;
	}

	public boolean isEmpty() {
		return games.size() == 0;
	}

	public ArrayList<Game> getGames() {
		return games;
	}

	public void setGames(ArrayList<Game> games) {
		this.games = games;
	}

	public boolean isSimulated() {
		return isSimulated;
	}

	public void setSimulated(boolean isSimulated) {
		this.isSimulated = isSimulated;
	}

}
