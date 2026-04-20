package data.sport.setup;

import process.visitor.gamemoment.GameMomentVisitor;

public class Evening extends GameMoment {
	public Evening() {
		super();
	}

	@Override
	public <T> T accept(GameMomentVisitor<T> visitor) {
		return visitor.visit(this);
	}

}
