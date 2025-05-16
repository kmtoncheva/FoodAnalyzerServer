package bg.sofia.uni.fmi.mjt.server.utility;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static bg.sofia.uni.fmi.mjt.server.constants.LoggerConstants.LOG_FILE;
import static bg.sofia.uni.fmi.mjt.server.constants.LoggerConstants.SETUP_ERROR_MSG;

public class LoggerConfig {
    public static Logger createLogger(String name) {
        Logger logger = Logger.getLogger(name);
        logger.setUseParentHandlers(false); // Avoid default console output

        File logDir = new File("logs");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }

        try {
            FileHandler fileHandler = new FileHandler(LOG_FILE, true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());

            // Console logs only warnings and errors
//            ConsoleHandler consoleHandler = new ConsoleHandler();
//            consoleHandler.setLevel(Level.WARNING);
//            consoleHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);
            //logger.addHandler(consoleHandler);
            logger.setLevel(Level.ALL);

        } catch (IOException e) {
            System.err.println(SETUP_ERROR_MSG + e.getMessage());
        }

        return logger;
    }
}