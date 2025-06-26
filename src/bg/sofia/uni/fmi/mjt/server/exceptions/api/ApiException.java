package bg.sofia.uni.fmi.mjt.server.exceptions.api;

/**
 * Abstract base exception class for USDA FoodData Central API-related errors.
 * <p>
 * Contains a log message intended for internal logging and a client message
 * intended to be sent back to the client for user-friendly error reporting.
 */
public abstract class ApiException extends Exception {
    private final String clientMessage;

    public ApiException(String logMessage, String clientMessage) {
        super(logMessage);
        this.clientMessage = clientMessage;
    }

    public ApiException(String logMessage, String clientMessage, Throwable cause) {
        super(logMessage, cause);
        this.clientMessage = clientMessage;
    }

    public String getClientMessage() {
        return clientMessage;
    }
}