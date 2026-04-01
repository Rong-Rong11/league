package gui.app;

import gui.frame.MainGui;
import process.orchestrator.SimulationInterface;
import process.orchestrator.SimulationManager;

public class App {

	public static void main(String[] args) {
		SimulationInterface simulationInterface = new SimulationManager();
		MainGui gui = new MainGui(simulationInterface);

	}
}
