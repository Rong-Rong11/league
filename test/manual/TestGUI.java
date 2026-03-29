package manual;

import gui.frame.MainGui;
import process.orchestrator.SimulationInterface;
import process.orchestrator.SimulationManager;

public class TestGUI {
	SimulationInterface simulationInterface = new SimulationManager();
	MainGui mainGui = new MainGui(simulationInterface);
}
