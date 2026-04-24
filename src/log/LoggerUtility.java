package log;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoggerUtility {
	private static final String TEXT_LOG_CONFIG = "src/log/log4j-text.properties";
	private static final String HTML_LOG_CONFIG = "src/log/log4j-html.properties";

	public static Logger getLogger(Class<?> logClass, String logFileType) {
		if ("text".equals(logFileType)) {
			configureLogger(TEXT_LOG_CONFIG);
		} else if ("html".equals(logFileType)) {
			configureLogger(HTML_LOG_CONFIG);
		} else {
			throw new IllegalArgumentException("Unknown log file type !");
		}

		String className = logClass.getName();
		return Logger.getLogger(className);
	}

	private static void configureLogger(String configPath) {
		URL resource = LoggerUtility.class.getClassLoader().getResource(configPath);
		if (resource != null) {
			PropertyConfigurator.configure(resource);
			return;
		}

		File configFile = new File(configPath);
		if (configFile.exists()) {
			PropertyConfigurator.configure(configFile.getPath());
			return;
		}

		File srcConfigFile = new File("src", configPath);
		if (srcConfigFile.exists()) {
			PropertyConfigurator.configure(srcConfigFile.getPath());
			return;
		}

		PropertyConfigurator.configure(configPath);
	}
}
