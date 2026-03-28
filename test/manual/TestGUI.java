package manual;

import gui.app.App;
import gui.frame.MainGui;
import process.SimulationInterface;
import process.manager.SimulationManager;

public class TestGUI {
	SimulationInterface simulationInterface = new SimulationManager(); 
	MainGui mainGui = new MainGui(simulationInterface) ; 
}
