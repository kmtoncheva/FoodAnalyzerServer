package bg.sofia.uni.fmi.mjt.server.exceptions.api;

/**
 * Exception indicating that the USDA FoodData Central API service is currently unavailable.
 * <p>
 * This represents an "infrastructure" problem, such as the inability to reach
 * the underlying service or a downtime situation.
 * <p>
 * Contains both a detailed log message for internal diagnostics and a client-friendly
 * message explaining the issue.
 */
public class ApiServiceUnavailableException extends ApiException {
    public ApiServiceUnavailableException(String logMessage, String clientMessage) {
        super(logMessage, clientMessage);
    }

    public ApiServiceUnavailableException(String logMessage, String clientMessage, Throwable cause) {
        super(logMessage, clientMessage, cause);
    }
}