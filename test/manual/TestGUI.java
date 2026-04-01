package manual;

import gui.frame.MainGui;
import process.orchestrator.GUIInterface;
import process.orchestrator.SimulationManager;

public class TestGUI {
	GUIInterface guiInterface = new SimulationManager();
	MainGui mainGui = new MainGui(guiInterface);
}
