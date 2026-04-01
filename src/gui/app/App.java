package gui.app;

import gui.frame.MainGui;
import process.orchestrator.DisplayInterface;
import process.orchestrator.MatchQueryInterface;
import process.orchestrator.SeasonQueryInterface;
import process.orchestrator.SimulationInterface;
import process.orchestrator.SimulationManager;
import process.orchestrator.TeamQueryInterface;

public class App {

public static void main(String[] args) {
	SimulationManager simulationManager = new SimulationManager();
	SimulationInterface simulationInterface = simulationManager;
	SeasonQueryInterface seasonQueryInterface = simulationManager;
	TeamQueryInterface teamQueryInterface = simulationManager;
	MatchQueryInterface matchQueryInterface = simulationManager;
	DisplayInterface displayInterface = simulationManager;
	MainGui gui = new MainGui(simulationInterface, seasonQueryInterface, teamQueryInterface, matchQueryInterface,
			displayInterface);

}
}
