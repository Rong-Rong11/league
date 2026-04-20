package data.sport.setup;

import process.visitor.gamemoment.GameMomentVisitor;

public class Afternoon extends GameMoment {
	public Afternoon() {
	  super();
	}

	@Override
	public <T> T accept(GameMomentVisitor<T> visitor) {
	  return visitor.visit(this);
	}

}
