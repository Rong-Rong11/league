package manual;

import gui.frame.MainGui;
import process.orchestrator.interf.GUIInterface;
import process.orchestrator.manager.SimulationManager;

public class TestGUI {
	GUIInterface guiInterface = new SimulationManager();
	MainGui mainGui = new MainGui(guiInterface);
}
