package gui.app;

import org.apache.log4j.Logger;

import gui.frame.MainGui;
import log.LoggerUtility;
import process.orchestrator.interfaces.GUIInterface;
import process.orchestrator.manager.SimulationManager;

public class App {
	private static final Logger logger = LoggerUtility.getLogger(App.class, "text");

	public static void main(String[] args) {
		logger.info("Starting League application");
		GUIInterface guiInterface = new SimulationManager();
		logger.info("Simulation manager initialized");
		new MainGui(guiInterface);
		logger.info("Main GUI initialized");
	}
}
