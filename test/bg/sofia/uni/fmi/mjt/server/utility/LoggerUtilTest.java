package bg.sofia.uni.fmi.mjt.server.utility;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoggerUtilTest {
    @Test
    void testCreateLoggerCreatesLogFile() {
        File file = new File("logs/server.log");

        if (file.exists()) {
            file.delete();
        }

        Logger logger = LoggerUtil.createLogger("TestLogger");

        assertNotNull(logger);
        assertTrue(file.exists());

        file.delete();
    }
}
