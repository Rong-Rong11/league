package process.visitor.gamemoment;

import data.sport.setup.Afternoon;
import data.sport.setup.Evening;
import data.sport.setup.Night;

public class GameMomentSlotKeyVisitor implements GameMomentVisitor<String> {

	@Override
	public String visit(Afternoon afternoon) {
		return "afternoon";
	}

	@Override
	public String visit(Evening evening) {
		return "evening";
	}

	@Override
	public String visit(Night night) {
		return "night";
	}
}
