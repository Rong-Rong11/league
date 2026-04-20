package gui.app;

import gui.frame.MainGui;
import process.orchestrator.interf.GUIInterface;
import process.orchestrator.manager.SimulationManager;

public class App {

	public static void main(String[] args) {
		GUIInterface guiInterface = new SimulationManager();
		new MainGui(guiInterface);
	}
}
