package process.visitor.gamemoment;

import data.sport.setup.Afternoon;
import data.sport.setup.Evening;
import data.sport.setup.Night;

public interface GameMomentVisitor<T> {
	T visit(Afternoon afternoon);

	T visit(Evening evening);

	T visit(Night night);
}
