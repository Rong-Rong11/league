package process.visitor.gamemoment;

import data.sport.setup.Afternoon;
import data.sport.setup.Evening;
import data.sport.setup.Night;

public class GameMomentAttendanceBonusVisitor implements GameMomentVisitor<Double> {

    @Override
    public Double visit(Afternoon afternoon) {
        return -0.04;
    }

    @Override
    public Double visit(Evening evening) {
        return 0.05;
    }

    @Override
    public Double visit(Night night) {
        return 0.10;
    }
}
