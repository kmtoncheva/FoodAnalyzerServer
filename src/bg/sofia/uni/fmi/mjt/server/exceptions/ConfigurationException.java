package bg.sofia.uni.fmi.mjt.server.exceptions;

/**
 * Thrown to indicate that a component was misconfigured,
 * such as missing required dependencies or invalid configuration values.
 */
public class ConfigurationException extends RuntimeException {
    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}