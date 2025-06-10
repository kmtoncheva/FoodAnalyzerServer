package bg.sofia.uni.fmi.mjt.server.utility;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static bg.sofia.uni.fmi.mjt.server.constants.LoggerConstants.LOG_FILE;
import static bg.sofia.uni.fmi.mjt.server.constants.LoggerConstants.SETUP_ERROR_MSG;

/**
 * This class is a configuration utility that wraps setup logic for java.util.logging.Logger.
 * Its core job is to instantiate and configure an object.
 */
public class LoggerConfig {
    /**
     * Creates and configures a {@link java.util.logging.Logger} instance with the specified name.
     *
     * @param name the name of the logger to create
     * @return a configured {@link java.util.logging.Logger} instance
     */
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

            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);

        } catch (IOException e) {
            System.err.println(SETUP_ERROR_MSG + e.getMessage());
        }

        return logger;
    }
}