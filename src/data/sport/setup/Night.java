package data.sport.setup;

import process.visitor.gamemoment.GameMomentVisitor;

public class Night extends GameMoment {
	public Night() {
	  super();
	}

	@Override
	public <T> T accept(GameMomentVisitor<T> visitor) {
	  return visitor.visit(this);
	}

}
