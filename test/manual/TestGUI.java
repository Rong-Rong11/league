package manual;

import gui.frame.MainGui;
import process.orchestrator.DisplayInterface;
import process.orchestrator.MatchQueryInterface;
import process.orchestrator.SeasonQueryInterface;
import process.orchestrator.SimulationInterface;
import process.orchestrator.SimulationManager;
import process.orchestrator.TeamQueryInterface;

public class TestGUI {
	SimulationManager simulationManager = new SimulationManager();
	SimulationInterface simulationInterface = simulationManager;
	SeasonQueryInterface seasonQueryInterface = simulationManager;
	TeamQueryInterface teamQueryInterface = simulationManager;
	MatchQueryInterface matchQueryInterface = simulationManager;
	DisplayInterface displayInterface = simulationManager;
	MainGui mainGui = new MainGui(
			simulationInterface,
			seasonQueryInterface,
			teamQueryInterface,
			matchQueryInterface,
			displayInterface);
}
