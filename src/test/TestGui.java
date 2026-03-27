package test;

import gui.frame.MainGui;
import process.SimulationInterface;
import process.manager.SimulationManager;

public class TestGui {

	public static void main(String[] args) {
		SimulationInterface simulationInterface = new SimulationManager();
		MainGui gui = new MainGui(simulationInterface);

	}
}